package com.example.bit240102_foodmanager.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
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

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PendingSyncDao_Impl(
  __db: RoomDatabase,
) : PendingSyncDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPendingSync: EntityInsertAdapter<PendingSync>
  init {
    this.__db = __db
    this.__insertAdapterOfPendingSync = object : EntityInsertAdapter<PendingSync>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `BIT240102_SyncQueue` (`BIT240102_Id`,`BIT240102_EntityType`,`BIT240102_LocalId`,`BIT240102_Operation`,`BIT240102_Revision`,`BIT240102_RemoteId`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PendingSync) {
        statement.bindLong(1, entity.BIT240102_Id)
        statement.bindText(2, entity.BIT240102_EntityType)
        statement.bindLong(3, entity.BIT240102_LocalId)
        statement.bindText(4, entity.BIT240102_Operation)
        statement.bindLong(5, entity.BIT240102_Revision)
        val _tmpBIT240102_RemoteId: String? = entity.BIT240102_RemoteId
        if (_tmpBIT240102_RemoteId == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpBIT240102_RemoteId)
        }
      }
    }
  }

  public override suspend fun enqueue(item: PendingSync): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPendingSync.insert(_connection, item)
  }

  public override suspend fun getPending(): List<PendingSync> {
    val _sql: String = "SELECT * FROM BIT240102_SyncQueue ORDER BY BIT240102_Id"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102EntityType: Int = getColumnIndexOrThrow(_stmt, "BIT240102_EntityType")
        val _columnIndexOfBIT240102LocalId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_LocalId")
        val _columnIndexOfBIT240102Operation: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Operation")
        val _columnIndexOfBIT240102Revision: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Revision")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _result: MutableList<PendingSync> = mutableListOf()
        while (_stmt.step()) {
          val _item: PendingSync
          val _tmpBIT240102_Id: Long
          _tmpBIT240102_Id = _stmt.getLong(_columnIndexOfBIT240102Id)
          val _tmpBIT240102_EntityType: String
          _tmpBIT240102_EntityType = _stmt.getText(_columnIndexOfBIT240102EntityType)
          val _tmpBIT240102_LocalId: Long
          _tmpBIT240102_LocalId = _stmt.getLong(_columnIndexOfBIT240102LocalId)
          val _tmpBIT240102_Operation: String
          _tmpBIT240102_Operation = _stmt.getText(_columnIndexOfBIT240102Operation)
          val _tmpBIT240102_Revision: Long
          _tmpBIT240102_Revision = _stmt.getLong(_columnIndexOfBIT240102Revision)
          val _tmpBIT240102_RemoteId: String?
          if (_stmt.isNull(_columnIndexOfBIT240102RemoteId)) {
            _tmpBIT240102_RemoteId = null
          } else {
            _tmpBIT240102_RemoteId = _stmt.getText(_columnIndexOfBIT240102RemoteId)
          }
          _item = PendingSync(_tmpBIT240102_Id,_tmpBIT240102_EntityType,_tmpBIT240102_LocalId,_tmpBIT240102_Operation,_tmpBIT240102_Revision,_tmpBIT240102_RemoteId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getOne(entityType: String, localId: Long): PendingSync? {
    val _sql: String = """
        |
        |        SELECT * FROM BIT240102_SyncQueue
        |        WHERE BIT240102_EntityType = ? AND BIT240102_LocalId = ?
        |        
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, entityType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, localId)
        val _columnIndexOfBIT240102Id: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Id")
        val _columnIndexOfBIT240102EntityType: Int = getColumnIndexOrThrow(_stmt, "BIT240102_EntityType")
        val _columnIndexOfBIT240102LocalId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_LocalId")
        val _columnIndexOfBIT240102Operation: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Operation")
        val _columnIndexOfBIT240102Revision: Int = getColumnIndexOrThrow(_stmt, "BIT240102_Revision")
        val _columnIndexOfBIT240102RemoteId: Int = getColumnIndexOrThrow(_stmt, "BIT240102_RemoteId")
        val _result: PendingSync?
        if (_stmt.step()) {
          val _tmpBIT240102_Id: Long
          _tmpBIT240102_Id = _stmt.getLong(_columnIndexOfBIT240102Id)
          val _tmpBIT240102_EntityType: String
          _tmpBIT240102_EntityType = _stmt.getText(_columnIndexOfBIT240102EntityType)
          val _tmpBIT240102_LocalId: Long
          _tmpBIT240102_LocalId = _stmt.getLong(_columnIndexOfBIT240102LocalId)
          val _tmpBIT240102_Operation: String
          _tmpBIT240102_Operation = _stmt.getText(_columnIndexOfBIT240102Operation)
          val _tmpBIT240102_Revision: Long
          _tmpBIT240102_Revision = _stmt.getLong(_columnIndexOfBIT240102Revision)
          val _tmpBIT240102_RemoteId: String?
          if (_stmt.isNull(_columnIndexOfBIT240102RemoteId)) {
            _tmpBIT240102_RemoteId = null
          } else {
            _tmpBIT240102_RemoteId = _stmt.getText(_columnIndexOfBIT240102RemoteId)
          }
          _result = PendingSync(_tmpBIT240102_Id,_tmpBIT240102_EntityType,_tmpBIT240102_LocalId,_tmpBIT240102_Operation,_tmpBIT240102_Revision,_tmpBIT240102_RemoteId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun remove(entityType: String, localId: Long) {
    val _sql: String = """
        |
        |        DELETE FROM BIT240102_SyncQueue
        |        WHERE BIT240102_EntityType = ? AND BIT240102_LocalId = ?
        |        
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, entityType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, localId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun acknowledge(
    entityType: String,
    localId: Long,
    revision: Long,
  ) {
    val _sql: String = """
        |
        |        DELETE FROM BIT240102_SyncQueue
        |        WHERE BIT240102_EntityType = ? AND BIT240102_LocalId = ?
        |          AND BIT240102_Revision = ?
        |        
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, entityType)
        _argIndex = 2
        _stmt.bindLong(_argIndex, localId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, revision)
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
