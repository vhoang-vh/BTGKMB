package com.example.bit240102_foodmanager.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.getTotalChangedRows
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DishImageDao_Impl(
  __db: RoomDatabase,
) : DishImageDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDishImage: EntityInsertAdapter<DishImage>
  init {
    this.__db = __db
    this.__insertAdapterOfDishImage = object : EntityInsertAdapter<DishImage>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `BIT240102_DishImage` (`BIT240102_Id`,`BIT240102_DishId`,`BIT240102_ImageUrl`,`BIT240102_IsThumbnail`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DishImage) {
        statement.bindLong(1, entity.BIT240102_Id)
        statement.bindLong(2, entity.BIT240102_DishId)
        statement.bindText(3, entity.BIT240102_ImageUrl)
        val _tmp: Int = if (entity.BIT240102_IsThumbnail) 1 else 0
        statement.bindLong(4, _tmp.toLong())
      }
    }
  }

  public override suspend fun insertAll(images: List<DishImage>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDishImage.insert(_connection, images)
  }

  public override suspend fun getForDish(dishId: Long): List<DishImage> {
    val _sql: String = "SELECT * FROM BIT240102_DishImage WHERE BIT240102_DishId = ? ORDER BY BIT240102_Id"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dishId)
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102DishId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_DishId")
        val _columnIndexOfBIT240102ImageUrl: Int = getColumnIndexOrThrow(_stmt, "BIT240102_ImageUrl")
        val _columnIndexOfBIT240102IsThumbnail: Int = getColumnIndexOrThrow(_stmt, "BIT240102_IsThumbnail")
        val _result: MutableList<DishImage> = mutableListOf()
        while (_stmt.step()) {
          val _item: DishImage
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
          _item = DishImage(_tmpBIT240102_Id,_tmpBIT240102_DishId,_tmpBIT240102_ImageUrl,_tmpBIT240102_IsThumbnail)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteForDish(dishId: Long) {
    val _sql: String = "DELETE FROM BIT240102_DishImage WHERE BIT240102_DishId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dishId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearThumbnails(dishId: Long) {
    val _sql: String = """
        |
        |        UPDATE BIT240102_DishImage SET BIT240102_IsThumbnail = 0
        |        WHERE BIT240102_DishId = ?
        |        
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, dishId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun selectThumbnail(dishId: Long, imageId: Long): Int {
    val _sql: String = """
        |
        |        UPDATE BIT240102_DishImage SET BIT240102_IsThumbnail = 1
        |        WHERE BIT240102_Id = ? AND BIT240102_DishId = ?
        |        
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, imageId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, dishId)
        _stmt.step()
        getTotalChangedRows(_connection)
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
