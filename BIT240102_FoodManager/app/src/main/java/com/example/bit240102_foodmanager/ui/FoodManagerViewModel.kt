package com.example.bit240102_foodmanager.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bit240102_foodmanager.FoodManagerApplication
import com.example.bit240102_foodmanager.data.AppLanguage
import com.example.bit240102_foodmanager.data.DishCategory
import com.example.bit240102_foodmanager.data.DishFilter
import com.example.bit240102_foodmanager.data.DishSort
import com.example.bit240102_foodmanager.data.DishWithDetails
import com.example.bit240102_foodmanager.data.FoodValidationException
import com.example.bit240102_foodmanager.services.FoodRepository
import com.example.bit240102_foodmanager.services.SyncScheduler
import com.example.bit240102_foodmanager.services.toPriceFilter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private data class DishAndCategoryState(
    val BIT240102_Dishes: List<DishWithDetails>,
    val BIT240102_Categories: List<DishCategory>,
)

private data class FilterAndLanguageState(
    val BIT240102_Filter: DishFilter,
    val BIT240102_Language: AppLanguage,
    val BIT240102_Prices: Pair<String, String>,
)

private data class DetailAndErrorState(
    val BIT240102_FilterError: String?,
    val BIT240102_SelectedDish: DishWithDetails?,
    val BIT240102_Error: String?,
    val BIT240102_Saving: Boolean,
)

data class FoodManagerUiState(
    val BIT240102_Language: AppLanguage = AppLanguage.ENGLISH,
    val BIT240102_Dishes: List<DishWithDetails> = emptyList(),
    val BIT240102_Categories: List<DishCategory> = emptyList(),
    val BIT240102_Filter: DishFilter = DishFilter(),
    val BIT240102_MinPriceInput: String = "",
    val BIT240102_MaxPriceInput: String = "",
    val BIT240102_FilterError: String? = null,
    val BIT240102_SelectedDish: DishWithDetails? = null,
    val BIT240102_Error: String? = null,
    val BIT240102_Saving: Boolean = false,
)

@OptIn(ExperimentalCoroutinesApi::class)
class FoodManagerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as FoodManagerApplication).repository
    private val filter = MutableStateFlow(DishFilter())
    private val language = MutableStateFlow(repository.language())
    private val priceInput = MutableStateFlow("" to "")
    private val filterError = MutableStateFlow<String?>(null)
    private val selectedDish = MutableStateFlow<DishWithDetails?>(null)
    private val error = MutableStateFlow<String?>(null)
    private val saving = MutableStateFlow(false)

    private val dishesAndCategories = combine(
        filter.flatMapLatest(repository::observeDishes),
        repository.observeCategories(),
    ) { dishes, categories ->
        DishAndCategoryState(dishes, categories)
    }
    private val filterAndLanguage = combine(filter, language, priceInput) { currentFilter, currentLanguage, prices ->
        FilterAndLanguageState(currentFilter, currentLanguage, prices)
    }
    private val detailAndError = combine(filterError, selectedDish, error, saving) { rangeError, dish, currentError, isSaving ->
        DetailAndErrorState(rangeError, dish, currentError, isSaving)
    }

    val uiState = combine(dishesAndCategories, filterAndLanguage, detailAndError) { data, filters, details ->
        FoodManagerUiState(
            BIT240102_Dishes = data.BIT240102_Dishes,
            BIT240102_Categories = data.BIT240102_Categories,
            BIT240102_Filter = filters.BIT240102_Filter,
            BIT240102_Language = filters.BIT240102_Language,
            BIT240102_MinPriceInput = filters.BIT240102_Prices.first,
            BIT240102_MaxPriceInput = filters.BIT240102_Prices.second,
            BIT240102_FilterError = details.BIT240102_FilterError,
            BIT240102_SelectedDish = details.BIT240102_SelectedDish,
            BIT240102_Error = details.BIT240102_Error,
            BIT240102_Saving = details.BIT240102_Saving,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FoodManagerUiState())

    fun updateQuery(query: String) {
        filter.value = filter.value.copy(BIT240102_Query = query)
    }

    fun updateCategory(id: Long?) {
        filter.value = filter.value.copy(BIT240102_CategoryId = id)
    }

    fun updateAvailability(value: Int) {
        filter.value = filter.value.copy(BIT240102_Availability = value)
    }

    fun updateSort(sort: DishSort) {
        filter.value = filter.value.copy(BIT240102_Sort = sort)
    }

    fun updatePriceRange(minimum: String, maximum: String) {
        priceInput.value = minimum to maximum
        val minPrice = minimum.toPriceFilter()
        val maxPrice = maximum.toPriceFilter()
        val invalidInput =
            (minimum.isNotBlank() && minPrice == null) ||
                (maximum.isNotBlank() && maxPrice == null)
        val invalidRange = minPrice != null && maxPrice != null && minPrice > maxPrice
        if (invalidInput || invalidRange) {
            filterError.value = if (invalidRange) "error_price_range" else "error_price_filter"
            filter.value = filter.value.copy(BIT240102_MinPrice = null, BIT240102_MaxPrice = null)
        } else {
            filterError.value = null
            filter.value = filter.value.copy(BIT240102_MinPrice = minPrice, BIT240102_MaxPrice = maxPrice)
        }
    }

    fun toggleLanguage() {
        val next = language.value.next()
        repository.saveLanguage(next)
        language.value = next
    }

    fun loadDish(id: Long) {
        viewModelScope.launch {
            try {
                selectedDish.value = repository.getDish(id)
            } catch (exception: Exception) {
                Log.e("BIT240102_Dishes", "Unable to load dish $id.", exception)
                error.value = "error_unexpected"
            }
        }
    }

    fun saveDish(
        id: Long?,
        name: String,
        price: String,
        preparationTime: String,
        categoryId: Long?,
        available: Boolean,
        description: String,
        images: String,
        onSaved: (Long) -> Unit,
    ) {
        viewModelScope.launch {
            saving.value = true
            error.value = null
            try {
                val savedId = repository.saveDish(
                    id,
                    name,
                    price,
                    preparationTime,
                    categoryId,
                    available,
                    description,
                    images,
                )
                selectedDish.value = repository.getDish(savedId)
                onSaved(savedId)
            } catch (exception: FoodValidationException) {
                error.value = exception.BIT240102_MessageKey
            } catch (exception: Exception) {
                Log.e("BIT240102_Dishes", "Unable to save dish.", exception)
                error.value = "error_unexpected"
            } finally {
                saving.value = false
            }
        }
    }

    fun deleteDish(id: Long, onDeleted: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.deleteDish(id)
                selectedDish.value = null
                error.value = null
                onDeleted()
            } catch (exception: FoodValidationException) {
                error.value = exception.BIT240102_MessageKey
            } catch (exception: Exception) {
                Log.e("BIT240102_Dishes", "Unable to delete dish $id.", exception)
                error.value = "error_unexpected"
            }
        }
    }

    fun chooseThumbnail(dishId: Long, imageId: Long) {
        viewModelScope.launch {
            try {
                repository.setThumbnail(dishId, imageId)
                selectedDish.value = repository.getDish(dishId)
                error.value = null
            } catch (exception: FoodValidationException) {
                error.value = exception.BIT240102_MessageKey
            } catch (exception: Exception) {
                Log.e("BIT240102_Dishes", "Unable to select the thumbnail.", exception)
                error.value = "error_unexpected"
            }
        }
    }

    fun saveCategory(id: Long?, name: String, description: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.saveCategory(id, name, description)
                error.value = null
                onSaved()
            } catch (exception: FoodValidationException) {
                error.value = exception.BIT240102_MessageKey
            } catch (exception: Exception) {
                Log.e("BIT240102_Categories", "Unable to save category.", exception)
                error.value = "error_unexpected"
            }
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(id)
                error.value = null
            } catch (exception: FoodValidationException) {
                error.value = exception.BIT240102_MessageKey
            } catch (exception: Exception) {
                Log.e("BIT240102_Categories", "Unable to delete category $id.", exception)
                error.value = "error_unexpected"
            }
        }
    }

    fun clearError() {
        error.value = null
    }

    fun syncIfConfigured() {
        SyncScheduler.schedule(getApplication())
    }
}
