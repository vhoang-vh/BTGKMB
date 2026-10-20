package com.example.bit240102_foodmanager.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM BIT240102_Category ORDER BY BIT240102_Name COLLATE NOCASE")
    fun observeAll(): Flow<List<DishCategory>>

    @Query("SELECT * FROM BIT240102_Category ORDER BY BIT240102_Id")
    suspend fun getAll(): List<DishCategory>

    @Query("SELECT * FROM BIT240102_Category WHERE BIT240102_Id = :id")
    suspend fun getById(id: Long): DishCategory?

    @Query(
        """
        SELECT COUNT(*) FROM BIT240102_Dish
        WHERE BIT240102_DishCategoryId = :categoryId
        """,
    )
    suspend fun countDishes(categoryId: Long): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: DishCategory): Long

    @Update
    suspend fun update(category: DishCategory)

    @Query("DELETE FROM BIT240102_Category WHERE BIT240102_Id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE BIT240102_Category SET BIT240102_RemoteId = :remoteId WHERE BIT240102_Id = :id")
    suspend fun setRemoteId(id: Long, remoteId: String)
}

@Dao
interface DishDao {
    @Transaction
    @Query(
        """
        SELECT d.* FROM BIT240102_Dish AS d
        WHERE (:query = '' OR d.BIT240102_Name LIKE '%' || :query || '%' COLLATE NOCASE)
          AND (:categoryId IS NULL OR d.BIT240102_DishCategoryId = :categoryId)
          AND (
              :availability = 0
              OR (:availability = 1 AND d.BIT240102_IsAvailable = 1)
              OR (:availability = 2 AND d.BIT240102_IsAvailable = 0)
          )
          AND (:minPrice IS NULL OR d.BIT240102_Price >= :minPrice)
          AND (:maxPrice IS NULL OR d.BIT240102_Price <= :maxPrice)
        ORDER BY
            CASE WHEN :sort = 'PRICE_ASC' THEN d.BIT240102_Price END ASC,
            CASE WHEN :sort = 'PRICE_DESC' THEN d.BIT240102_Price END DESC,
            CASE WHEN :sort = 'PREPARATION_ASC' THEN d.BIT240102_PreparationTime END ASC,
            d.BIT240102_Name COLLATE NOCASE ASC
        """,
    )
    fun observeFiltered(
        query: String,
        categoryId: Long?,
        availability: Int,
        minPrice: Double?,
        maxPrice: Double?,
        sort: String,
    ): Flow<List<DishWithDetails>>

    @Transaction
    @Query("SELECT * FROM BIT240102_Dish WHERE BIT240102_Id = :id")
    suspend fun getDetails(id: Long): DishWithDetails?

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM BIT240102_Dish
            WHERE BIT240102_DishCategoryId = :categoryId
              AND BIT240102_Name = :name COLLATE NOCASE
              AND BIT240102_Id != :exceptId
        )
        """,
    )
    suspend fun hasDuplicateName(categoryId: Long, name: String, exceptId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(dish: Dish): Long

    @Update
    suspend fun update(dish: Dish)

    @Query("DELETE FROM BIT240102_Dish WHERE BIT240102_Id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM BIT240102_Dish ORDER BY BIT240102_Id")
    suspend fun getAll(): List<Dish>

    @Query("UPDATE BIT240102_Dish SET BIT240102_RemoteId = :remoteId WHERE BIT240102_Id = :id")
    suspend fun setRemoteId(id: Long, remoteId: String)

    @Query("SELECT BIT240102_RemoteId FROM BIT240102_Dish WHERE BIT240102_Id = :id")
    suspend fun getRemoteId(id: Long): String?
}

@Dao
interface DishImageDao {
    @Query("SELECT * FROM BIT240102_DishImage WHERE BIT240102_DishId = :dishId ORDER BY BIT240102_Id")
    suspend fun getForDish(dishId: Long): List<DishImage>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(images: List<DishImage>)

    @Query("DELETE FROM BIT240102_DishImage WHERE BIT240102_DishId = :dishId")
    suspend fun deleteForDish(dishId: Long)

    @Query(
        """
        UPDATE BIT240102_DishImage SET BIT240102_IsThumbnail = 0
        WHERE BIT240102_DishId = :dishId
        """,
    )
    suspend fun clearThumbnails(dishId: Long)

    @Query(
        """
        UPDATE BIT240102_DishImage SET BIT240102_IsThumbnail = 1
        WHERE BIT240102_Id = :imageId AND BIT240102_DishId = :dishId
        """,
    )
    suspend fun selectThumbnail(dishId: Long, imageId: Long): Int
}

@Dao
interface PendingSyncDao {
    @Query("SELECT * FROM BIT240102_SyncQueue ORDER BY BIT240102_Id")
    suspend fun getPending(): List<PendingSync>

    @Query(
        """
        SELECT * FROM BIT240102_SyncQueue
        WHERE BIT240102_EntityType = :entityType AND BIT240102_LocalId = :localId
        """,
    )
    suspend fun getOne(entityType: String, localId: Long): PendingSync?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enqueue(item: PendingSync)

    @Query(
        """
        DELETE FROM BIT240102_SyncQueue
        WHERE BIT240102_EntityType = :entityType AND BIT240102_LocalId = :localId
        """,
    )
    suspend fun remove(entityType: String, localId: Long)

    @Query(
        """
        DELETE FROM BIT240102_SyncQueue
        WHERE BIT240102_EntityType = :entityType AND BIT240102_LocalId = :localId
          AND BIT240102_Revision = :revision
        """,
    )
    suspend fun acknowledge(entityType: String, localId: Long, revision: Long)
}
