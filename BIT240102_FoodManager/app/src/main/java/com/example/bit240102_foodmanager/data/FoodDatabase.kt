package com.example.bit240102_foodmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [DishCategory::class, Dish::class, DishImage::class, PendingSync::class],
    version = 2,
    exportSchema = false,
)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun dishDao(): DishDao
    abstract fun dishImageDao(): DishImageDao
    abstract fun pendingSyncDao(): PendingSyncDao

    companion object {
        fun create(context: Context): FoodDatabase =
            Room.databaseBuilder(context, FoodDatabase::class.java, "BIT240102_FoodManager.db")
                .addMigrations(BIT240102_MIGRATION_1_2)
                .addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            createThumbnailGuards(db)
                            seed(db)
                        }
                    },
                )
                .build()

        private fun seed(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                INSERT INTO BIT240102_Category
                    (BIT240102_Id, BIT240102_Name, BIT240102_Description, BIT240102_RemoteId)
                VALUES
                    (1, 'Main Courses', 'Hearty everyday favorites', NULL),
                    (2, 'Appetizers', 'Small plates and starters', NULL),
                    (3, 'Desserts', 'Sweet treats and desserts', NULL)
                """.trimIndent(),
            )
            db.execSQL(
                """
                INSERT INTO BIT240102_Dish
                    (BIT240102_Id, BIT240102_Name, BIT240102_Price,
                     BIT240102_PreparationTime, BIT240102_IsAvailable,
                     BIT240102_Description, BIT240102_DishCategoryId, BIT240102_RemoteId)
                VALUES
                    (1, 'Vietnamese Beef Pho', 12.50, 12, 1, 'Rice noodles in fragrant beef broth.', 1, NULL),
                    (2, 'Grilled Lemongrass Chicken', 14.00, 25, 1, 'Marinated chicken grilled with lemongrass.', 1, NULL),
                    (3, 'Fresh Spring Rolls', 6.50, 10, 1, 'Fresh rice paper rolls with herbs.', 2, NULL),
                    (4, 'Crispy Spring Rolls', 7.00, 20, 1, 'Crisp rolls served with dipping sauce.', 2, NULL),
                    (5, 'Coconut Panna Cotta', 5.50, 15, 0, 'Silky coconut dessert; temporarily unavailable.', 3, NULL)
                """.trimIndent(),
            )
            db.execSQL(
                """
                INSERT INTO BIT240102_DishImage
                    (BIT240102_DishId, BIT240102_ImageUrl, BIT240102_IsThumbnail)
                VALUES
                    (1, 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=900', 1),
                    (2, 'https://images.unsplash.com/photo-1532550907401-a500c9a57435?w=900', 1),
                    (3, 'https://images.unsplash.com/photo-1548507200-8bfc9f8b02c6?w=900', 1),
                    (4, 'https://images.unsplash.com/photo-1544025162-d76694265947?w=900', 1),
                    (5, 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=900', 1)
                """.trimIndent(),
            )
            db.execSQL(
                """
                INSERT INTO BIT240102_SyncQueue
                    (BIT240102_EntityType, BIT240102_LocalId,
                     BIT240102_Operation, BIT240102_Revision)
                VALUES
                    ('CATEGORY', 1, 'UPSERT', 1),
                    ('CATEGORY', 2, 'UPSERT', 1),
                    ('CATEGORY', 3, 'UPSERT', 1),
                    ('DISH', 1, 'UPSERT', 1),
                    ('DISH', 2, 'UPSERT', 1),
                    ('DISH', 3, 'UPSERT', 1),
                    ('DISH', 4, 'UPSERT', 1),
                    ('DISH', 5, 'UPSERT', 1)
                """.trimIndent(),
            )
        }

        private val BIT240102_MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val hasRemoteId = db.query("PRAGMA table_info(BIT240102_SyncQueue)").use { cursor ->
                    val nameColumn = cursor.getColumnIndexOrThrow("name")
                    generateSequence { if (cursor.moveToNext()) cursor.getString(nameColumn) else null }
                        .any { it == "BIT240102_RemoteId" }
                }
                if (!hasRemoteId) {
                    db.execSQL(
                        """
                        ALTER TABLE BIT240102_SyncQueue
                        ADD COLUMN BIT240102_RemoteId TEXT
                        """.trimIndent(),
                    )
                }
                db.execSQL("DROP INDEX IF EXISTS BIT240102_OneThumbnailPerDish")
                createThumbnailGuards(db)
            }
        }

        private fun createThumbnailGuards(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TRIGGER IF NOT EXISTS BIT240102_OneThumbnailOnInsert
                BEFORE INSERT ON BIT240102_DishImage
                WHEN NEW.BIT240102_IsThumbnail = 1 AND EXISTS (
                    SELECT 1 FROM BIT240102_DishImage
                    WHERE BIT240102_DishId = NEW.BIT240102_DishId
                      AND BIT240102_IsThumbnail = 1
                )
                BEGIN
                    SELECT RAISE(ABORT, 'A dish can have only one thumbnail.');
                END
                """.trimIndent(),
            )
            db.execSQL(
                """
                CREATE TRIGGER IF NOT EXISTS BIT240102_OneThumbnailOnUpdate
                BEFORE UPDATE OF BIT240102_DishId, BIT240102_IsThumbnail
                ON BIT240102_DishImage
                WHEN NEW.BIT240102_IsThumbnail = 1 AND EXISTS (
                    SELECT 1 FROM BIT240102_DishImage
                    WHERE BIT240102_DishId = NEW.BIT240102_DishId
                      AND BIT240102_IsThumbnail = 1
                      AND BIT240102_Id != NEW.BIT240102_Id
                )
                BEGIN
                    SELECT RAISE(ABORT, 'A dish can have only one thumbnail.');
                END
                """.trimIndent(),
            )
        }
    }
}
