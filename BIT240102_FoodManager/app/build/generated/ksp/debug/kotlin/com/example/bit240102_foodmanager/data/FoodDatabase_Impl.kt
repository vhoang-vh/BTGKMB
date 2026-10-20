package com.example.bit240102_foodmanager.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FoodDatabase_Impl : FoodDatabase() {
  private val _categoryDao: Lazy<CategoryDao> = lazy {
    CategoryDao_Impl(this)
  }

  private val _dishDao: Lazy<DishDao> = lazy {
    DishDao_Impl(this)
  }

  private val _dishImageDao: Lazy<DishImageDao> = lazy {
    DishImageDao_Impl(this)
  }

  private val _pendingSyncDao: Lazy<PendingSyncDao> = lazy {
    PendingSyncDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2, "54a7a96301650cdc5128776e3532a038", "8924e44d7ac2648f5d0a0ed08cd28784") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `BIT240102_Category` (`BIT240102_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `BIT240102_Name` TEXT NOT NULL, `BIT240102_Description` TEXT NOT NULL, `BIT240102_RemoteId` TEXT)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `BIT240102_Dish` (`BIT240102_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `BIT240102_Name` TEXT NOT NULL, `BIT240102_Price` REAL NOT NULL, `BIT240102_PreparationTime` INTEGER NOT NULL, `BIT240102_IsAvailable` INTEGER NOT NULL, `BIT240102_Description` TEXT NOT NULL, `BIT240102_DishCategoryId` INTEGER NOT NULL, `BIT240102_RemoteId` TEXT, FOREIGN KEY(`BIT240102_DishCategoryId`) REFERENCES `BIT240102_Category`(`BIT240102_Id`) ON UPDATE NO ACTION ON DELETE RESTRICT )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_BIT240102_Dish_BIT240102_DishCategoryId` ON `BIT240102_Dish` (`BIT240102_DishCategoryId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_BIT240102_Dish_BIT240102_RemoteId` ON `BIT240102_Dish` (`BIT240102_RemoteId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `BIT240102_DishImage` (`BIT240102_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `BIT240102_DishId` INTEGER NOT NULL, `BIT240102_ImageUrl` TEXT NOT NULL, `BIT240102_IsThumbnail` INTEGER NOT NULL, FOREIGN KEY(`BIT240102_DishId`) REFERENCES `BIT240102_Dish`(`BIT240102_Id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_BIT240102_DishImage_BIT240102_DishId` ON `BIT240102_DishImage` (`BIT240102_DishId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `BIT240102_SyncQueue` (`BIT240102_Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `BIT240102_EntityType` TEXT NOT NULL, `BIT240102_LocalId` INTEGER NOT NULL, `BIT240102_Operation` TEXT NOT NULL, `BIT240102_Revision` INTEGER NOT NULL, `BIT240102_RemoteId` TEXT)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_BIT240102_SyncQueue_BIT240102_EntityType_BIT240102_LocalId` ON `BIT240102_SyncQueue` (`BIT240102_EntityType`, `BIT240102_LocalId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '54a7a96301650cdc5128776e3532a038')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `BIT240102_Category`")
        connection.execSQL("DROP TABLE IF EXISTS `BIT240102_Dish`")
        connection.execSQL("DROP TABLE IF EXISTS `BIT240102_DishImage`")
        connection.execSQL("DROP TABLE IF EXISTS `BIT240102_SyncQueue`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsBIT240102Category: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBIT240102Category.put("BIT240102_Id", TableInfo.Column("BIT240102_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Category.put("BIT240102_Name", TableInfo.Column("BIT240102_Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Category.put("BIT240102_Description", TableInfo.Column("BIT240102_Description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Category.put("BIT240102_RemoteId", TableInfo.Column("BIT240102_RemoteId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBIT240102Category: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBIT240102Category: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoBIT240102Category: TableInfo = TableInfo("BIT240102_Category", _columnsBIT240102Category, _foreignKeysBIT240102Category, _indicesBIT240102Category)
        val _existingBIT240102Category: TableInfo = read(connection, "BIT240102_Category")
        if (!_infoBIT240102Category.equals(_existingBIT240102Category)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |BIT240102_Category(com.example.bit240102_foodmanager.data.DishCategory).
              | Expected:
              |""".trimMargin() + _infoBIT240102Category + """
              |
              | Found:
              |""".trimMargin() + _existingBIT240102Category)
        }
        val _columnsBIT240102Dish: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBIT240102Dish.put("BIT240102_Id", TableInfo.Column("BIT240102_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Dish.put("BIT240102_Name", TableInfo.Column("BIT240102_Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Dish.put("BIT240102_Price", TableInfo.Column("BIT240102_Price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Dish.put("BIT240102_PreparationTime", TableInfo.Column("BIT240102_PreparationTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Dish.put("BIT240102_IsAvailable", TableInfo.Column("BIT240102_IsAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Dish.put("BIT240102_Description", TableInfo.Column("BIT240102_Description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Dish.put("BIT240102_DishCategoryId", TableInfo.Column("BIT240102_DishCategoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102Dish.put("BIT240102_RemoteId", TableInfo.Column("BIT240102_RemoteId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBIT240102Dish: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysBIT240102Dish.add(TableInfo.ForeignKey("BIT240102_Category", "RESTRICT", "NO ACTION", listOf("BIT240102_DishCategoryId"), listOf("BIT240102_Id")))
        val _indicesBIT240102Dish: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBIT240102Dish.add(TableInfo.Index("index_BIT240102_Dish_BIT240102_DishCategoryId", false, listOf("BIT240102_DishCategoryId"), listOf("ASC")))
        _indicesBIT240102Dish.add(TableInfo.Index("index_BIT240102_Dish_BIT240102_RemoteId", false, listOf("BIT240102_RemoteId"), listOf("ASC")))
        val _infoBIT240102Dish: TableInfo = TableInfo("BIT240102_Dish", _columnsBIT240102Dish, _foreignKeysBIT240102Dish, _indicesBIT240102Dish)
        val _existingBIT240102Dish: TableInfo = read(connection, "BIT240102_Dish")
        if (!_infoBIT240102Dish.equals(_existingBIT240102Dish)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |BIT240102_Dish(com.example.bit240102_foodmanager.data.Dish).
              | Expected:
              |""".trimMargin() + _infoBIT240102Dish + """
              |
              | Found:
              |""".trimMargin() + _existingBIT240102Dish)
        }
        val _columnsBIT240102DishImage: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBIT240102DishImage.put("BIT240102_Id", TableInfo.Column("BIT240102_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102DishImage.put("BIT240102_DishId", TableInfo.Column("BIT240102_DishId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102DishImage.put("BIT240102_ImageUrl", TableInfo.Column("BIT240102_ImageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102DishImage.put("BIT240102_IsThumbnail", TableInfo.Column("BIT240102_IsThumbnail", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBIT240102DishImage: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysBIT240102DishImage.add(TableInfo.ForeignKey("BIT240102_Dish", "CASCADE", "NO ACTION", listOf("BIT240102_DishId"), listOf("BIT240102_Id")))
        val _indicesBIT240102DishImage: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBIT240102DishImage.add(TableInfo.Index("index_BIT240102_DishImage_BIT240102_DishId", false, listOf("BIT240102_DishId"), listOf("ASC")))
        val _infoBIT240102DishImage: TableInfo = TableInfo("BIT240102_DishImage", _columnsBIT240102DishImage, _foreignKeysBIT240102DishImage, _indicesBIT240102DishImage)
        val _existingBIT240102DishImage: TableInfo = read(connection, "BIT240102_DishImage")
        if (!_infoBIT240102DishImage.equals(_existingBIT240102DishImage)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |BIT240102_DishImage(com.example.bit240102_foodmanager.data.DishImage).
              | Expected:
              |""".trimMargin() + _infoBIT240102DishImage + """
              |
              | Found:
              |""".trimMargin() + _existingBIT240102DishImage)
        }
        val _columnsBIT240102SyncQueue: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBIT240102SyncQueue.put("BIT240102_Id", TableInfo.Column("BIT240102_Id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102SyncQueue.put("BIT240102_EntityType", TableInfo.Column("BIT240102_EntityType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102SyncQueue.put("BIT240102_LocalId", TableInfo.Column("BIT240102_LocalId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102SyncQueue.put("BIT240102_Operation", TableInfo.Column("BIT240102_Operation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102SyncQueue.put("BIT240102_Revision", TableInfo.Column("BIT240102_Revision", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBIT240102SyncQueue.put("BIT240102_RemoteId", TableInfo.Column("BIT240102_RemoteId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBIT240102SyncQueue: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBIT240102SyncQueue: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBIT240102SyncQueue.add(TableInfo.Index("index_BIT240102_SyncQueue_BIT240102_EntityType_BIT240102_LocalId", true, listOf("BIT240102_EntityType", "BIT240102_LocalId"), listOf("ASC", "ASC")))
        val _infoBIT240102SyncQueue: TableInfo = TableInfo("BIT240102_SyncQueue", _columnsBIT240102SyncQueue, _foreignKeysBIT240102SyncQueue, _indicesBIT240102SyncQueue)
        val _existingBIT240102SyncQueue: TableInfo = read(connection, "BIT240102_SyncQueue")
        if (!_infoBIT240102SyncQueue.equals(_existingBIT240102SyncQueue)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |BIT240102_SyncQueue(com.example.bit240102_foodmanager.data.PendingSync).
              | Expected:
              |""".trimMargin() + _infoBIT240102SyncQueue + """
              |
              | Found:
              |""".trimMargin() + _existingBIT240102SyncQueue)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "BIT240102_Category", "BIT240102_Dish", "BIT240102_DishImage", "BIT240102_SyncQueue")
  }

  public override fun clearAllTables() {
    super.performClear(true, "BIT240102_Dish", "BIT240102_Category", "BIT240102_DishImage", "BIT240102_SyncQueue")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(CategoryDao::class, CategoryDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DishDao::class, DishDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DishImageDao::class, DishImageDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PendingSyncDao::class, PendingSyncDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun categoryDao(): CategoryDao = _categoryDao.value

  public override fun dishDao(): DishDao = _dishDao.value

  public override fun dishImageDao(): DishImageDao = _dishImageDao.value

  public override fun pendingSyncDao(): PendingSyncDao = _pendingSyncDao.value
}
