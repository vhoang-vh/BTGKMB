package com.example.bit240102_foodmanager.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CategoryDao_Impl(
  __db: RoomDatabase,
) : CategoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDishCategory: EntityInsertAdapter<DishCategory>

  private val __updateAdapterOfDishCategory: EntityDeleteOrUpdateAdapter<DishCategory>
  init {
    this.__db = __db
    this.__insertAdapterOfDishCategory = object : EntityInsertAdapter<DishCategory>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `BIT240102_Category` (`BIT240102_Id`,`BIT240102_Name`,`BIT240102_Description`,`BIT240102_RemoteId`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DishCategory) {
        statement.bindLong(1, entity.BIT240102_Id)
        statement.bindText(2, entity.BIT240102_Name)
        statement.bindText(3, entity.BIT240102_Description)
        val _tmpBIT240102_RemoteId: String? = entity.BIT240102_RemoteId
        if (_tmpBIT240102_RemoteId == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpBIT240102_RemoteId)
        }
      }
    }
    this.__updateAdapterOfDishCategory = object : EntityDeleteOrUpdateAdapter<DishCategory>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `BIT240102_Category` SET `BIT240102_Id` = ?,`BIT240102_Name` = ?,`BIT240102_Description` = ?,`BIT240102_RemoteId` = ? WHERE `BIT240102_Id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DishCategory) {
        statement.bindLong(1, entity.BIT240102_Id)
        statement.bindText(2, entity.BIT240102_Name)
        statement.bindText(3, entity.BIT240102_Description)
        val _tmpBIT240102_RemoteId: String? = entity.BIT240102_RemoteId
        if (_tmpBIT240102_RemoteId == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpBIT240102_RemoteId)
        }
        statement.bindLong(5, entity.BIT240102_Id)
      }
    }
  }

  public override suspend fun insert(category: DishCategory): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfDishCategory.insertAndReturnId(_connection, category)
    _result
  }

  public override suspend fun update(category: DishCategory): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfDishCategory.handle(_connection, category)
  }

  public override fun observeAll(): Flow<List<DishCategory>> {
    val _sql: String = "SELECT * FROM BIT240102_Category ORDER BY BIT240102_Name COLLATE NOCASE"
    return createFlow(__db, false, arrayOf("BIT240102_Category")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102Name: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Name")
        val _columnIndexOfBIT240102Description: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Description")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _result: MutableList<DishCategory> = mutableListOf()
        while (_stmt.step()) {
          val _item: DishCategory
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
          _item = DishCategory(_tmpBIT240102_Id,_tmpBIT240102_Name,_tmpBIT240102_Description,_tmpBIT240102_RemoteId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAll(): List<DishCategory> {
    val _sql: String = "SELECT * FROM BIT240102_Category ORDER BY BIT240102_Id"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102Name: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Name")
        val _columnIndexOfBIT240102Description: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Description")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _result: MutableList<DishCategory> = mutableListOf()
        while (_stmt.step()) {
          val _item: DishCategory
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
          _item = DishCategory(_tmpBIT240102_Id,_tmpBIT240102_Name,_tmpBIT240102_Description,_tmpBIT240102_RemoteId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): DishCategory? {
    val _sql: String = "SELECT * FROM BIT240102_Category WHERE BIT240102_Id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102Name: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Name")
        val _columnIndexOfBIT240102Description: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Description")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _result: DishCategory?
        if (_stmt.step()) {
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
          _result = DishCategory(_tmpBIT240102_Id,_tmpBIT240102_Name,_tmpBIT240102_Description,_tmpBIT240102_RemoteId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun countDishes(categoryId: Long): Int {
    val _sql: String = """
        |
        |        SELECT COUNT(*) FROM BIT240102_Dish
        |        WHERE BIT240102_DishCategoryId = ?
        |        
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, categoryId)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: Long) {
    val _sql: String = "DELETE FROM BIT240102_Category WHERE BIT240102_Id = ?"
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
    val _sql: String = "UPDATE BIT240102_Category SET BIT240102_RemoteId = ? WHERE BIT240102_Id = ?"
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
