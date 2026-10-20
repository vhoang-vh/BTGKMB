package com.example.bit240102_foodmanager.`data`

import androidx.collection.LongSparseArray
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DishDao_Impl(
  __db: RoomDatabase,
) : DishDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDish: EntityInsertAdapter<Dish>

  private val __updateAdapterOfDish: EntityDeleteOrUpdateAdapter<Dish>
  init {
    this.__db = __db
    this.__insertAdapterOfDish = object : EntityInsertAdapter<Dish>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `BIT240102_Dish` (`BIT240102_Id`,`BIT240102_Name`,`BIT240102_Price`,`BIT240102_PreparationTime`,`BIT240102_IsAvailable`,`BIT240102_Description`,`BIT240102_DishCategoryId`,`BIT240102_RemoteId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Dish) {
        statement.bindLong(1, entity.BIT240102_Id)
        statement.bindText(2, entity.BIT240102_Name)
        statement.bindDouble(3, entity.BIT240102_Price)
        statement.bindLong(4, entity.BIT240102_PreparationTime.toLong())
        val _tmp: Int = if (entity.BIT240102_IsAvailable) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.BIT240102_Description)
        statement.bindLong(7, entity.BIT240102_DishCategoryId)
        val _tmpBIT240102_RemoteId: String? = entity.BIT240102_RemoteId
        if (_tmpBIT240102_RemoteId == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpBIT240102_RemoteId)
        }
      }
    }
    this.__updateAdapterOfDish = object : EntityDeleteOrUpdateAdapter<Dish>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `BIT240102_Dish` SET `BIT240102_Id` = ?,`BIT240102_Name` = ?,`BIT240102_Price` = ?,`BIT240102_PreparationTime` = ?,`BIT240102_IsAvailable` = ?,`BIT240102_Description` = ?,`BIT240102_DishCategoryId` = ?,`BIT240102_RemoteId` = ? WHERE `BIT240102_Id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Dish) {
        statement.bindLong(1, entity.BIT240102_Id)
        statement.bindText(2, entity.BIT240102_Name)
        statement.bindDouble(3, entity.BIT240102_Price)
        statement.bindLong(4, entity.BIT240102_PreparationTime.toLong())
        val _tmp: Int = if (entity.BIT240102_IsAvailable) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.BIT240102_Description)
        statement.bindLong(7, entity.BIT240102_DishCategoryId)
        val _tmpBIT240102_RemoteId: String? = entity.BIT240102_RemoteId
        if (_tmpBIT240102_RemoteId == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpBIT240102_RemoteId)
        }
        statement.bindLong(9, entity.BIT240102_Id)
      }
    }
  }

  public override suspend fun insert(dish: Dish): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfDish.insertAndReturnId(_connection, dish)
    _result
  }

  public override suspend fun update(dish: Dish): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfDish.handle(_connection, dish)
  }

  public override fun observeFiltered(
    query: String,
    categoryId: Long?,
    availability: Int,
    minPrice: Double?,
    maxPrice: Double?,
    sort: String,
  ): Flow<List<DishWithDetails>> {
    val _sql: String = """
        |
        |        SELECT d.* FROM BIT240102_Dish AS d
        |        WHERE (? = '' OR d.BIT240102_Name LIKE '%' || ? || '%' COLLATE NOCASE)
        |          AND (? IS NULL OR d.BIT240102_DishCategoryId = ?)
        |          AND (
        |              ? = 0
        |              OR (? = 1 AND d.BIT240102_IsAvailable = 1)
        |              OR (? = 2 AND d.BIT240102_IsAvailable = 0)
        |          )
        |          AND (? IS NULL OR d.BIT240102_Price >= ?)
        |          AND (? IS NULL OR d.BIT240102_Price <= ?)
        |        ORDER BY
        |            CASE WHEN ? = 'PRICE_ASC' THEN d.BIT240102_Price END ASC,
        |            CASE WHEN ? = 'PRICE_DESC' THEN d.BIT240102_Price END DESC,
        |            CASE WHEN ? = 'PREPARATION_ASC' THEN d.BIT240102_PreparationTime END ASC,
        |            d.BIT240102_Name COLLATE NOCASE ASC
        |        
        """.trimMargin()
    return createFlow(__db, true, arrayOf("BIT240102_Category", "BIT240102_DishImage", "BIT240102_Dish")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        _argIndex = 3
        if (categoryId == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, categoryId)
        }
        _argIndex = 4
        if (categoryId == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindLong(_argIndex, categoryId)
        }
        _argIndex = 5
        _stmt.bindLong(_argIndex, availability.toLong())
        _argIndex = 6
        _stmt.bindLong(_argIndex, availability.toLong())
        _argIndex = 7
        _stmt.bindLong(_argIndex, availability.toLong())
        _argIndex = 8
        if (minPrice == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, minPrice)
        }
        _argIndex = 9
        if (minPrice == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, minPrice)
        }
        _argIndex = 10
        if (maxPrice == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, maxPrice)
        }
        _argIndex = 11
        if (maxPrice == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindDouble(_argIndex, maxPrice)
        }
        _argIndex = 12
        _stmt.bindText(_argIndex, sort)
        _argIndex = 13
        _stmt.bindText(_argIndex, sort)
        _argIndex = 14
        _stmt.bindText(_argIndex, sort)
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102Name: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Name")
        val _columnIndexOfBIT240102Price: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Price")
        val _columnIndexOfBIT240102PreparationTime: Int = getColumnIndexOrThrow(_stmt, "BIT240102_PreparationTime")
        val _columnIndexOfBIT240102IsAvailable: Int = getColumnIndexOrThrow(_stmt, "BIT240102_IsAvailable")
        val _columnIndexOfBIT240102Description: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Description")
        val _columnIndexOfBIT240102DishCategoryId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_DishCategoryId")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _collectionBIT240102Category: LongSparseArray<DishCategory?> = LongSparseArray<DishCategory?>()
        val _collectionBIT240102Images: LongSparseArray<MutableList<DishImage>> = LongSparseArray<MutableList<DishImage>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfBIT240102DishCategoryId)
          _collectionBIT240102Category.put(_tmpKey, null)
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfBIT240102Id)
          if (!_collectionBIT240102Images.containsKey(_tmpKey_1)) {
            _collectionBIT240102Images.put(_tmpKey_1, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipBIT240102CategoryAscomExampleBit240102FoodmanagerDataDishCategory(_connection, _collectionBIT240102Category)
        __fetchRelationshipBIT240102DishImageAscomExampleBit240102FoodmanagerDataDishImage(_connection, _collectionBIT240102Images)
        val _result: MutableList<DishWithDetails> = mutableListOf()
        while (_stmt.step()) {
          val _item: DishWithDetails
          val _tmpBIT240102_Dish: Dish
          val _tmpBIT240102_Id: Long
          _tmpBIT240102_Id = _stmt.getLong(_columnIndexOfBIT240102Id)
          val _tmpBIT240102_Name: String
          _tmpBIT240102_Name = _stmt.getText(_columnIndexOfBIT240102Name)
          val _tmpBIT240102_Price: Double
          _tmpBIT240102_Price = _stmt.getDouble(_columnIndexOfBIT240102Price)
          val _tmpBIT240102_PreparationTime: Int
          _tmpBIT240102_PreparationTime = _stmt.getLong(_columnIndexOfBIT240102PreparationTime).toInt()
          val _tmpBIT240102_IsAvailable: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfBIT240102IsAvailable).toInt()
          _tmpBIT240102_IsAvailable = _tmp != 0
          val _tmpBIT240102_Description: String
          _tmpBIT240102_Description = _stmt.getText(_columnIndexOfBIT240102Description)
          val _tmpBIT240102_DishCategoryId: Long
          _tmpBIT240102_DishCategoryId = _stmt.getLong(_columnIndexOfBIT240102DishCategoryId)
          val _tmpBIT240102_RemoteId: String?
          if (_stmt.isNull(_columnIndexOfBIT240102RemoteId)) {
            _tmpBIT240102_RemoteId = null
          } else {
            _tmpBIT240102_RemoteId = _stmt.getText(_columnIndexOfBIT240102RemoteId)
          }
          _tmpBIT240102_Dish = Dish(_tmpBIT240102_Id,_tmpBIT240102_Name,_tmpBIT240102_Price,_tmpBIT240102_PreparationTime,_tmpBIT240102_IsAvailable,_tmpBIT240102_Description,_tmpBIT240102_DishCategoryId,_tmpBIT240102_RemoteId)
          val _tmpBIT240102Category: DishCategory?
          val _tmpKey_2: Long
          _tmpKey_2 = _stmt.getLong(_columnIndexOfBIT240102DishCategoryId)
          _tmpBIT240102Category = _collectionBIT240102Category.get(_tmpKey_2)
          if (_tmpBIT240102Category == null) {
            error("Relationship item 'BIT240102_Category' was expected to be NON-NULL but is NULL in @Relation involving a parent column named 'BIT240102_DishCategoryId' and entityColumn named 'BIT240102_Id'.")
          }
          val _tmpBIT240102ImagesCollection: MutableList<DishImage>
          val _tmpKey_3: Long
          _tmpKey_3 = _stmt.getLong(_columnIndexOfBIT240102Id)
          _tmpBIT240102ImagesCollection = checkNotNull(_collectionBIT240102Images.get(_tmpKey_3))
          _item = DishWithDetails(_tmpBIT240102_Dish,_tmpBIT240102Category,_tmpBIT240102ImagesCollection)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDetails(id: Long): DishWithDetails? {
    val _sql: String = "SELECT * FROM BIT240102_Dish WHERE BIT240102_Id = ?"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102Name: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Name")
        val _columnIndexOfBIT240102Price: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Price")
        val _columnIndexOfBIT240102PreparationTime: Int = getColumnIndexOrThrow(_stmt, "BIT240102_PreparationTime")
        val _columnIndexOfBIT240102IsAvailable: Int = getColumnIndexOrThrow(_stmt, "BIT240102_IsAvailable")
        val _columnIndexOfBIT240102Description: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Description")
        val _columnIndexOfBIT240102DishCategoryId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_DishCategoryId")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _collectionBIT240102Category: LongSparseArray<DishCategory?> = LongSparseArray<DishCategory?>()
        val _collectionBIT240102Images: LongSparseArray<MutableList<DishImage>> = LongSparseArray<MutableList<DishImage>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfBIT240102DishCategoryId)
          _collectionBIT240102Category.put(_tmpKey, null)
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfBIT240102Id)
          if (!_collectionBIT240102Images.containsKey(_tmpKey_1)) {
            _collectionBIT240102Images.put(_tmpKey_1, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipBIT240102CategoryAscomExampleBit240102FoodmanagerDataDishCategory(_connection, _collectionBIT240102Category)
        __fetchRelationshipBIT240102DishImageAscomExampleBit240102FoodmanagerDataDishImage(_connection, _collectionBIT240102Images)
        val _result: DishWithDetails?
        if (_stmt.step()) {
          val _tmpBIT240102_Dish: Dish
          val _tmpBIT240102_Id: Long
          _tmpBIT240102_Id = _stmt.getLong(_columnIndexOfBIT240102Id)
          val _tmpBIT240102_Name: String
          _tmpBIT240102_Name = _stmt.getText(_columnIndexOfBIT240102Name)
          val _tmpBIT240102_Price: Double
          _tmpBIT240102_Price = _stmt.getDouble(_columnIndexOfBIT240102Price)
          val _tmpBIT240102_PreparationTime: Int
          _tmpBIT240102_PreparationTime = _stmt.getLong(_columnIndexOfBIT240102PreparationTime).toInt()
          val _tmpBIT240102_IsAvailable: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfBIT240102IsAvailable).toInt()
          _tmpBIT240102_IsAvailable = _tmp != 0
          val _tmpBIT240102_Description: String
          _tmpBIT240102_Description = _stmt.getText(_columnIndexOfBIT240102Description)
          val _tmpBIT240102_DishCategoryId: Long
          _tmpBIT240102_DishCategoryId = _stmt.getLong(_columnIndexOfBIT240102DishCategoryId)
          val _tmpBIT240102_RemoteId: String?
          if (_stmt.isNull(_columnIndexOfBIT240102RemoteId)) {
            _tmpBIT240102_RemoteId = null
          } else {
            _tmpBIT240102_RemoteId = _stmt.getText(_columnIndexOfBIT240102RemoteId)
          }
          _tmpBIT240102_Dish = Dish(_tmpBIT240102_Id,_tmpBIT240102_Name,_tmpBIT240102_Price,_tmpBIT240102_PreparationTime,_tmpBIT240102_IsAvailable,_tmpBIT240102_Description,_tmpBIT240102_DishCategoryId,_tmpBIT240102_RemoteId)
          val _tmpBIT240102Category: DishCategory?
          val _tmpKey_2: Long
          _tmpKey_2 = _stmt.getLong(_columnIndexOfBIT240102DishCategoryId)
          _tmpBIT240102Category = _collectionBIT240102Category.get(_tmpKey_2)
          if (_tmpBIT240102Category == null) {
            error("Relationship item 'BIT240102_Category' was expected to be NON-NULL but is NULL in @Relation involving a parent column named 'BIT240102_DishCategoryId' and entityColumn named 'BIT240102_Id'.")
          }
          val _tmpBIT240102ImagesCollection: MutableList<DishImage>
          val _tmpKey_3: Long
          _tmpKey_3 = _stmt.getLong(_columnIndexOfBIT240102Id)
          _tmpBIT240102ImagesCollection = checkNotNull(_collectionBIT240102Images.get(_tmpKey_3))
          _result = DishWithDetails(_tmpBIT240102_Dish,_tmpBIT240102Category,_tmpBIT240102ImagesCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun hasDuplicateName(
    categoryId: Long,
    name: String,
    exceptId: Long,
  ): Boolean {
    val _sql: String = """
        |
        |        SELECT EXISTS(
        |            SELECT 1 FROM BIT240102_Dish
        |            WHERE BIT240102_DishCategoryId = ?
        |              AND BIT240102_Name = ? COLLATE NOCASE
        |              AND BIT240102_Id != ?
        |        )
        |        
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, categoryId)
        _argIndex = 2
        _stmt.bindText(_argIndex, name)
        _argIndex = 3
        _stmt.bindLong(_argIndex, exceptId)
        val _result: Boolean
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp != 0
        } else {
          _result = false
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAll(): List<Dish> {
    val _sql: String = "SELECT * FROM BIT240102_Dish ORDER BY BIT240102_Id"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102Name: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Name")
        val _columnIndexOfBIT240102Price: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Price")
        val _columnIndexOfBIT240102PreparationTime: Int = getColumnIndexOrThrow(_stmt, "BIT240102_PreparationTime")
        val _columnIndexOfBIT240102IsAvailable: Int = getColumnIndexOrThrow(_stmt, "BIT240102_IsAvailable")
        val _columnIndexOfBIT240102Description: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Description")
        val _columnIndexOfBIT240102DishCategoryId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_DishCategoryId")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _result: MutableList<Dish> = mutableListOf()
        while (_stmt.step()) {
          val _item: Dish
          val _tmpBIT240102_Id: Long
          _tmpBIT240102_Id = _stmt.getLong(_columnIndexOfBIT240102Id)
          val _tmpBIT240102_Name: String
          _tmpBIT240102_Name = _stmt.getText(_columnIndexOfBIT240102Name)
          val _tmpBIT240102_Price: Double
          _tmpBIT240102_Price = _stmt.getDouble(_columnIndexOfBIT240102Price)
          val _tmpBIT240102_PreparationTime: Int
          _tmpBIT240102_PreparationTime = _stmt.getLong(_columnIndexOfBIT240102PreparationTime).toInt()
          val _tmpBIT240102_IsAvailable: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfBIT240102IsAvailable).toInt()
          _tmpBIT240102_IsAvailable = _tmp != 0
          val _tmpBIT240102_Description: String
          _tmpBIT240102_Description = _stmt.getText(_columnIndexOfBIT240102Description)
          val _tmpBIT240102_DishCategoryId: Long
          _tmpBIT240102_DishCategoryId = _stmt.getLong(_columnIndexOfBIT240102DishCategoryId)
          val _tmpBIT240102_RemoteId: String?
          if (_stmt.isNull(_columnIndexOfBIT240102RemoteId)) {
            _tmpBIT240102_RemoteId = null
          } else {
            _tmpBIT240102_RemoteId = _stmt.getText(_columnIndexOfBIT240102RemoteId)
          }
          _item = Dish(_tmpBIT240102_Id,_tmpBIT240102_Name,_tmpBIT240102_Price,_tmpBIT240102_PreparationTime,_tmpBIT240102_IsAvailable,_tmpBIT240102_Description,_tmpBIT240102_DishCategoryId,_tmpBIT240102_RemoteId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getRemoteId(id: Long): String? {
    val _sql: String = "SELECT BIT240102_RemoteId FROM BIT240102_Dish WHERE BIT240102_Id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _result: String?
        if (_stmt.step()) {
          if (_stmt.isNull(0)) {
            _result = null
          } else {
            _result = _stmt.getText(0)
          }
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: Long) {
    val _sql: String = "DELETE FROM BIT240102_Dish WHERE BIT240102_Id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setRemoteId(id: Long, remoteId: String) {
    val _sql: String = "UPDATE BIT240102_Dish SET BIT240102_RemoteId = ? WHERE BIT240102_Id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, remoteId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private fun __fetchRelationshipBIT240102CategoryAscomExampleBit240102FoodmanagerDataDishCategory(_connection: SQLiteConnection, _map: LongSparseArray<DishCategory?>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, false) { _tmpMap ->
        __fetchRelationshipBIT240102CategoryAscomExampleBit240102FoodmanagerDataDishCategory(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `BIT240102_Id`,`BIT240102_Name`,`BIT240102_Description`,`BIT240102_RemoteId` FROM `BIT240102_Category` WHERE `BIT240102_Id` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "BIT240102_Id")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfBIT240102Id: Int = 0
      val _columnIndexOfBIT240102Name: Int = 1
      val _columnIndexOfBIT240102Description: Int = 2
      val _columnIndexOfBIT240102RemoteId: Int = 3
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        if (_map.containsKey(_tmpKey)) {
          val _item_1: DishCategory
          val _tmpBIT240102_Id: Long
          _tmpBIT240102_Id = _stmt.getLong(_columnIndexOfBIT240102Id)
          val _tmpBIT240102_Name: String
          _tmpBIT240102_Name = _stmt.getText(_columnIndexOfBIT240102Name)
          val _tmpBIT240102_Description: String
          _tmpBIT240102_Description = _stmt.getText(_columnIndexOfBIT240102Description)
          val _tmpBIT240102_RemoteId: String?
          if (_stmt.isNull(_columnIndexOfBIT240102RemoteId)) {
            _tmpBIT240102_RemoteId = null
          } else {
            _tmpBIT240102_RemoteId = _stmt.getText(_columnIndexOfBIT240102RemoteId)
          }
          _item_1 = DishCategory(_tmpBIT240102_Id,_tmpBIT240102_Name,_tmpBIT240102_Description,_tmpBIT240102_RemoteId)
          _map.put(_tmpKey, _item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  private fun __fetchRelationshipBIT240102DishImageAscomExampleBit240102FoodmanagerDataDishImage(_connection: SQLiteConnection, _map: LongSparseArray<MutableList<DishImage>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshipBIT240102DishImageAscomExampleBit240102FoodmanagerDataDishImage(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `BIT240102_Id`,`BIT240102_DishId`,`BIT240102_ImageUrl`,`BIT240102_IsThumbnail` FROM `BIT240102_DishImage` WHERE `BIT240102_DishId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "BIT240102_DishId")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfBIT240102Id: Int = 0
      val _columnIndexOfBIT240102DishId: Int = 1
      val _columnIndexOfBIT240102ImageUrl: Int = 2
      val _columnIndexOfBIT240102IsThumbnail: Int = 3
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<DishImage>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: DishImage
          val _tmpBIT240102_Id: Long
          _tmpBIT240102_Id = _stmt.getLong(_columnIndexOfBIT240102Id)
          val _tmpBIT240102_DishId: Long
          _tmpBIT240102_DishId = _stmt.getLong(_columnIndexOfBIT240102DishId)
          val _tmpBIT240102_ImageUrl: String
          _tmpBIT240102_ImageUrl = _stmt.getText(_columnIndexOfBIT240102ImageUrl)
          val _tmpBIT240102_IsThumbnail: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfBIT240102IsThumbnail).toInt()
          _tmpBIT240102_IsThumbnail = _tmp != 0
          _item_1 = DishImage(_tmpBIT240102_Id,_tmpBIT240102_DishId,_tmpBIT240102_ImageUrl,_tmpBIT240102_IsThumbnail)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
