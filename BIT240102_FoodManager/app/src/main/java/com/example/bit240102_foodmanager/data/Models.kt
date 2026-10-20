package com.example.bit240102_foodmanager.data

import com.google.gson.annotations.SerializedName
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "BIT240102_Category")
data class DishCategory(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "BIT240102_Id") val BIT240102_Id: Long = 0,
    @ColumnInfo(name = "BIT240102_Name") val BIT240102_Name: String,
    @ColumnInfo(name = "BIT240102_Description") val BIT240102_Description: String = "",
    @ColumnInfo(name = "BIT240102_RemoteId") val BIT240102_RemoteId: String? = null,
)

@Entity(
    tableName = "BIT240102_Dish",
    foreignKeys = [
        ForeignKey(
            entity = DishCategory::class,
            parentColumns = ["BIT240102_Id"],
            childColumns = ["BIT240102_DishCategoryId"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index("BIT240102_DishCategoryId"), Index(value = ["BIT240102_RemoteId"])],
)
data class Dish(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "BIT240102_Id") val BIT240102_Id: Long = 0,
    @ColumnInfo(name = "BIT240102_Name") val BIT240102_Name: String,
    @ColumnInfo(name = "BIT240102_Price") val BIT240102_Price: Double,
    @ColumnInfo(name = "BIT240102_PreparationTime") val BIT240102_PreparationTime: Int,
    @ColumnInfo(name = "BIT240102_IsAvailable") val BIT240102_IsAvailable: Boolean,
    @ColumnInfo(name = "BIT240102_Description") val BIT240102_Description: String,
    @ColumnInfo(name = "BIT240102_DishCategoryId") val BIT240102_DishCategoryId: Long,
    @ColumnInfo(name = "BIT240102_RemoteId") val BIT240102_RemoteId: String? = null,
)

@Entity(
    tableName = "BIT240102_DishImage",
    foreignKeys = [
        ForeignKey(
            entity = Dish::class,
            parentColumns = ["BIT240102_Id"],
            childColumns = ["BIT240102_DishId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("BIT240102_DishId")],
)
data class DishImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "BIT240102_Id") val BIT240102_Id: Long = 0,
    @ColumnInfo(name = "BIT240102_DishId") val BIT240102_DishId: Long,
    @ColumnInfo(name = "BIT240102_ImageUrl") val BIT240102_ImageUrl: String,
    @ColumnInfo(name = "BIT240102_IsThumbnail") val BIT240102_IsThumbnail: Boolean,
)

@Entity(
    tableName = "BIT240102_SyncQueue",
    indices = [Index(value = ["BIT240102_EntityType", "BIT240102_LocalId"], unique = true)],
)
data class PendingSync(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "BIT240102_Id") val BIT240102_Id: Long = 0,
    @ColumnInfo(name = "BIT240102_EntityType") val BIT240102_EntityType: String,
    @ColumnInfo(name = "BIT240102_LocalId") val BIT240102_LocalId: Long,
    @ColumnInfo(name = "BIT240102_Operation") val BIT240102_Operation: String,
    @ColumnInfo(name = "BIT240102_Revision") val BIT240102_Revision: Long,
    @ColumnInfo(name = "BIT240102_RemoteId") val BIT240102_RemoteId: String? = null,
)

data class DishWithDetails(
    @Embedded val BIT240102_Dish: Dish,
    @Relation(
        parentColumn = "BIT240102_DishCategoryId",
        entityColumn = "BIT240102_Id",
    )
    val BIT240102_Category: DishCategory,
    @Relation(
        parentColumn = "BIT240102_Id",
        entityColumn = "BIT240102_DishId",
    )
    val BIT240102_Images: List<DishImage>,
)

data class MockApiImage(
    val BIT240102_ImageUrl: String,
    val BIT240102_IsThumbnail: Boolean,
)

data class MockApiCategory(
    val BIT240102_LocalId: Long,
    val BIT240102_Name: String,
    val BIT240102_Description: String,
    @SerializedName(value = "BIT240102_RemoteId", alternate = ["id"])
    val BIT240102_RemoteGeneratedId: String? = null,
)

data class MockApiDish(
    val BIT240102_LocalId: Long,
    val BIT240102_Name: String,
    val BIT240102_Price: Double,
    val BIT240102_PreparationTime: Int,
    val BIT240102_IsAvailable: Boolean,
    val BIT240102_Description: String,
    val BIT240102_DishCategoryId: Long,
    val BIT240102_DishCategoryRemoteId: String?,
    val BIT240102_Images: List<MockApiImage>,
    @SerializedName(value = "BIT240102_RemoteId", alternate = ["id"])
    val BIT240102_RemoteGeneratedId: String? = null,
)

enum class DishSort {
    NAME,
    PRICE_ASC,
    PRICE_DESC,
    PREPARATION_ASC,
}

data class DishFilter(
    val BIT240102_Query: String = "",
    val BIT240102_CategoryId: Long? = null,
    val BIT240102_Availability: Int = 0,
    val BIT240102_MinPrice: Double? = null,
    val BIT240102_MaxPrice: Double? = null,
    val BIT240102_Sort: DishSort = DishSort.NAME,
)

enum class AppLanguage(val BIT240102_Tag: String) {
    ENGLISH("en"),
    VIETNAMESE("vi");

    fun next(): AppLanguage = if (this == ENGLISH) VIETNAMESE else ENGLISH
}

class FoodValidationException(val BIT240102_MessageKey: String) : IllegalArgumentException()
