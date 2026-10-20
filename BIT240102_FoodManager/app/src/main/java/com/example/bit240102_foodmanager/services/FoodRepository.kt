package com.example.bit240102_foodmanager.services

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import com.example.bit240102_foodmanager.data.AppLanguage
import com.example.bit240102_foodmanager.data.CategoryDao
import com.example.bit240102_foodmanager.data.Dish
import com.example.bit240102_foodmanager.data.DishCategory
import com.example.bit240102_foodmanager.data.DishDao
import com.example.bit240102_foodmanager.data.DishFilter
import com.example.bit240102_foodmanager.data.DishImage
import com.example.bit240102_foodmanager.data.DishImageDao
import com.example.bit240102_foodmanager.data.DishSort
import com.example.bit240102_foodmanager.data.DishWithDetails
import com.example.bit240102_foodmanager.data.FoodDatabase
import com.example.bit240102_foodmanager.data.FoodValidationException
import com.example.bit240102_foodmanager.data.MockApiCategory
import com.example.bit240102_foodmanager.data.MockApiDish
import com.example.bit240102_foodmanager.data.MockApiImage
import com.example.bit240102_foodmanager.data.PendingSync
import com.example.bit240102_foodmanager.data.PendingSyncDao
import java.io.IOException
import java.net.URI
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class FoodRepository(context: Context, private val database: FoodDatabase) {
    private val applicationContext = context.applicationContext
    private val categoryDao: CategoryDao = database.categoryDao()
    private val dishDao: DishDao = database.dishDao()
    private val imageDao: DishImageDao = database.dishImageDao()
    private val syncDao: PendingSyncDao = database.pendingSyncDao()

    fun observeCategories(): Flow<List<DishCategory>> = categoryDao.observeAll()

    fun observeDishes(filter: DishFilter): Flow<List<DishWithDetails>> =
        dishDao.observeFiltered(
            query = filter.BIT240102_Query.trim(),
            categoryId = filter.BIT240102_CategoryId,
            availability = filter.BIT240102_Availability,
            minPrice = filter.BIT240102_MinPrice,
            maxPrice = filter.BIT240102_MaxPrice,
            sort = filter.BIT240102_Sort.name,
        )

    suspend fun getDish(id: Long): DishWithDetails? = dishDao.getDetails(id)

    suspend fun saveDish(
        id: Long?,
        name: String,
        priceInput: String,
        preparationInput: String,
        categoryId: Long?,
        available: Boolean,
        description: String,
        imageInput: String,
    ): Long {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) throw FoodValidationException("error_name_required")
        val price = priceInput.toDoubleOrNull()
            ?.takeIf { it.isFinite() && it > 0 }
            ?: throw FoodValidationException("error_price_invalid")
        val preparationTime = preparationInput.toIntOrNull()
            ?.takeIf { it > 0 }
            ?: throw FoodValidationException("error_time_invalid")
        val selectedCategoryId = categoryId
            ?: throw FoodValidationException("error_category_required")
        val images = parseImageUrls(imageInput)
        return database.withTransaction {
            val category = categoryDao.getById(selectedCategoryId)
                ?: throw FoodValidationException("error_category_required")
            val previous = id?.let { dishDao.getDetails(it) }?.BIT240102_Dish
            if (id != null && previous == null) {
                throw FoodValidationException("error_not_found")
            }
            if (dishDao.hasDuplicateName(category.BIT240102_Id, cleanName, id ?: 0)) {
                throw FoodValidationException("error_duplicate_name")
            }
            val dish = Dish(
                BIT240102_Id = id ?: 0,
                BIT240102_Name = cleanName,
                BIT240102_Price = price,
                BIT240102_PreparationTime = preparationTime,
                BIT240102_IsAvailable = available,
                BIT240102_Description = description.trim(),
                BIT240102_DishCategoryId = category.BIT240102_Id,
                BIT240102_RemoteId = previous?.BIT240102_RemoteId,
            )
            val savedId = if (previous == null) dishDao.insert(dish) else {
                dishDao.update(dish)
                previous.BIT240102_Id
            }
            imageDao.deleteForDish(savedId)
            if (images.isNotEmpty()) {
                imageDao.insertAll(
                    images.mapIndexed { index, url ->
                        DishImage(
                            BIT240102_DishId = savedId,
                            BIT240102_ImageUrl = url,
                            BIT240102_IsThumbnail = index == 0,
                        )
                    },
                )
            }
            enqueue("DISH", savedId, "UPSERT")
            savedId
        }.also { SyncScheduler.schedule(applicationContext) }
    }

    suspend fun deleteDish(id: Long) {
        database.withTransaction {
            val dish = dishDao.getDetails(id)?.BIT240102_Dish
                ?: throw FoodValidationException("error_not_found")
            dishDao.deleteById(id)
            val pending = syncDao.getOne("DISH", id)
            if (dish.BIT240102_RemoteId == null && pending == null) {
                syncDao.remove("DISH", id)
            } else {
                enqueue("DISH", id, "DELETE", dish.BIT240102_RemoteId)
            }
        }
        SyncScheduler.schedule(applicationContext)
    }

    suspend fun setThumbnail(dishId: Long, imageId: Long) {
        database.withTransaction {
            if (dishDao.getDetails(dishId) == null) {
                throw FoodValidationException("error_not_found")
            }
            imageDao.clearThumbnails(dishId)
            if (imageDao.selectThumbnail(dishId, imageId) != 1) {
                throw FoodValidationException("error_image_not_found")
            }
            enqueue("DISH", dishId, "UPSERT")
        }
        SyncScheduler.schedule(applicationContext)
    }

    suspend fun saveCategory(id: Long?, name: String, description: String) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) throw FoodValidationException("error_category_name")
        database.withTransaction {
            val category = id?.let { categoryDao.getById(it) }
            if (id != null && category == null) {
                throw FoodValidationException("error_not_found")
            }
            val savedId = if (category == null) {
                categoryDao.insert(DishCategory(BIT240102_Name = cleanName, BIT240102_Description = description.trim()))
            } else {
                categoryDao.update(
                    category.copy(
                        BIT240102_Name = cleanName,
                        BIT240102_Description = description.trim(),
                    ),
                )
                category.BIT240102_Id
            }
            enqueue("CATEGORY", savedId, "UPSERT")
        }
        SyncScheduler.schedule(applicationContext)
    }

    suspend fun deleteCategory(id: Long) {
        database.withTransaction {
            val category = categoryDao.getById(id)
                ?: throw FoodValidationException("error_not_found")
            if (categoryDao.countDishes(id) > 0) {
                throw FoodValidationException("error_category_in_use")
            }
            categoryDao.deleteById(id)
            val pending = syncDao.getOne("CATEGORY", id)
            if (category.BIT240102_RemoteId == null && pending == null) {
                syncDao.remove("CATEGORY", id)
            } else {
                enqueue("CATEGORY", id, "DELETE", category.BIT240102_RemoteId)
            }
        }
        SyncScheduler.schedule(applicationContext)
    }

    fun language(): AppLanguage {
        val preferences = applicationContext.getSharedPreferences("BIT240102_Settings", Context.MODE_PRIVATE)
        val saved = preferences.getString("BIT240102_Language", AppLanguage.ENGLISH.name)
        return AppLanguage.entries.firstOrNull { it.name == saved } ?: run {
            Log.w("BIT240102_Language", "Saved language is invalid; using English.")
            AppLanguage.ENGLISH
        }
    }

    fun saveLanguage(language: AppLanguage) {
        applicationContext.getSharedPreferences("BIT240102_Settings", Context.MODE_PRIVATE)
            .edit()
            .putString("BIT240102_Language", language.name)
            .apply()
    }

    suspend fun syncPending() {
        val api = MockApiServiceFactory.create(applicationContext)
        while (true) {
            val pendingItems = syncDao.getPending()
            if (pendingItems.isEmpty()) return
            for (pending in pendingItems) {
                when (pending.BIT240102_EntityType) {
                    "CATEGORY" -> syncCategory(api, pending)
                    "DISH" -> syncDish(api, pending)
                    else -> throw IOException("Unknown pending entity type.")
                }
            }
            if (syncDao.getPending() == pendingItems) {
                throw IOException("MockAPI synchronization made no progress.")
            }
        }
    }

    private suspend fun syncCategory(api: MockApiService, pending: PendingSync) {
        val category = categoryDao.getById(pending.BIT240102_LocalId)
        val remoteId = category?.BIT240102_RemoteId ?: pending.BIT240102_RemoteId
        if (pending.BIT240102_Operation == "DELETE") {
            val remoteRecords = if (remoteId != null) listOf(remoteId)
            else api.findCategories(pending.BIT240102_LocalId).mapNotNull {
                it.BIT240102_RemoteGeneratedId
            }
            for (recordId in remoteRecords) {
                val response = api.deleteCategory(recordId)
                if (!response.isSuccessful && response.code() != 404) throw HttpException(response)
            }
            syncDao.acknowledge("CATEGORY", pending.BIT240102_LocalId, pending.BIT240102_Revision)
            return
        }
        val record = category ?: run {
            syncDao.acknowledge("CATEGORY", pending.BIT240102_LocalId, pending.BIT240102_Revision)
            return
        }
        val payload = MockApiCategory(
            BIT240102_LocalId = record.BIT240102_Id,
            BIT240102_Name = record.BIT240102_Name,
            BIT240102_Description = record.BIT240102_Description,
        )
        val saved = if (remoteId != null) {
            api.updateCategory(remoteId, payload)
        } else {
            val existing = api.findCategories(record.BIT240102_Id).firstOrNull()
            if (existing?.BIT240102_RemoteGeneratedId != null) {
                api.updateCategory(existing.BIT240102_RemoteGeneratedId, payload)
            }
            else api.createCategory(payload)
        }
        val actualRemoteId = saved.BIT240102_RemoteGeneratedId
            ?: remoteId
            ?: throw IOException("MockAPI did not return a category ID.")
        database.withTransaction {
            categoryDao.setRemoteId(record.BIT240102_Id, actualRemoteId)
            syncDao.acknowledge("CATEGORY", record.BIT240102_Id, pending.BIT240102_Revision)
        }
    }

    private suspend fun syncDish(api: MockApiService, pending: PendingSync) {
        val dish = dishDao.getDetails(pending.BIT240102_LocalId)?.BIT240102_Dish
        val remoteId = dish?.BIT240102_RemoteId ?: pending.BIT240102_RemoteId
        if (pending.BIT240102_Operation == "DELETE") {
            val remoteRecords = if (remoteId != null) listOf(remoteId)
            else api.findDishes(pending.BIT240102_LocalId).mapNotNull {
                it.BIT240102_RemoteGeneratedId
            }
            for (recordId in remoteRecords) {
                val response = api.deleteDish(recordId)
                if (!response.isSuccessful && response.code() != 404) throw HttpException(response)
            }
            syncDao.acknowledge("DISH", pending.BIT240102_LocalId, pending.BIT240102_Revision)
            return
        }
        val record = dish ?: run {
            syncDao.acknowledge("DISH", pending.BIT240102_LocalId, pending.BIT240102_Revision)
            return
        }
        val categoryRemoteId = categoryDao.getById(record.BIT240102_DishCategoryId)?.BIT240102_RemoteId
            ?: throw IOException("The dish category has not synced yet.")
        val images = imageDao.getForDish(record.BIT240102_Id).map {
            MockApiImage(it.BIT240102_ImageUrl, it.BIT240102_IsThumbnail)
        }
        val payload = MockApiDish(
            BIT240102_LocalId = record.BIT240102_Id,
            BIT240102_Name = record.BIT240102_Name,
            BIT240102_Price = record.BIT240102_Price,
            BIT240102_PreparationTime = record.BIT240102_PreparationTime,
            BIT240102_IsAvailable = record.BIT240102_IsAvailable,
            BIT240102_Description = record.BIT240102_Description,
            BIT240102_DishCategoryId = record.BIT240102_DishCategoryId,
            BIT240102_DishCategoryRemoteId = categoryRemoteId,
            BIT240102_Images = images,
        )
        val saved = if (remoteId != null) {
            api.updateDish(remoteId, payload)
        } else {
            val existing = api.findDishes(record.BIT240102_Id).firstOrNull()
            if (existing?.BIT240102_RemoteGeneratedId != null) {
                api.updateDish(existing.BIT240102_RemoteGeneratedId, payload)
            }
            else api.createDish(payload)
        }
        val actualRemoteId = saved.BIT240102_RemoteGeneratedId
            ?: remoteId
            ?: throw IOException("MockAPI did not return a dish ID.")
        database.withTransaction {
            dishDao.setRemoteId(record.BIT240102_Id, actualRemoteId)
            syncDao.acknowledge("DISH", record.BIT240102_Id, pending.BIT240102_Revision)
        }
    }

    private suspend fun enqueue(type: String, localId: Long, operation: String, remoteId: String? = null) {
        val previous = syncDao.getOne(type, localId)
        val revision = (previous?.BIT240102_Revision ?: 0L) + 1
        syncDao.enqueue(
            PendingSync(
                BIT240102_EntityType = type,
                BIT240102_LocalId = localId,
                BIT240102_Operation = operation,
                BIT240102_Revision = revision,
                BIT240102_RemoteId = remoteId ?: previous?.BIT240102_RemoteId,
            ),
        )
    }

    private fun parseImageUrls(input: String): List<String> {
        val urls = input.lineSequence().map(String::trim).filter(String::isNotEmpty).distinct().toList()
        if (urls.any { url ->
                val scheme = try {
                    URI(url).scheme?.lowercase(Locale.ROOT)
                } catch (_: java.net.URISyntaxException) {
                    null
                }
                scheme != "https" && scheme != "http"
            }
        ) {
            throw FoodValidationException("error_image_url")
        }
        return urls
    }

    companion object {
        const val BIT240102_SYNC_TAG = "BIT240102_FoodManager"
    }
}

fun String.toPriceFilter(): Double? =
    trim().takeIf(String::isNotEmpty)?.replace(',', '.')?.toDoubleOrNull()
        ?.takeIf { it.isFinite() && it >= 0 }
