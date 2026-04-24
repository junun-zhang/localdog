package com.calsync.app.data.local.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.calsync.app.data.local.entity.CalendarEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CalendarDao_Impl implements CalendarDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CalendarEntity> __insertionAdapterOfCalendarEntity;

  private final EntityDeletionOrUpdateAdapter<CalendarEntity> __deletionAdapterOfCalendarEntity;

  private final EntityDeletionOrUpdateAdapter<CalendarEntity> __updateAdapterOfCalendarEntity;

  public CalendarDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCalendarEntity = new EntityInsertionAdapter<CalendarEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `calendars` (`id`,`name`,`color`,`isVisible`,`isShared`,`ownerUserId`,`role`,`inviteCode`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getColor());
        final int _tmp = entity.isVisible() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isShared() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        if (entity.getOwnerUserId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOwnerUserId());
        }
        statement.bindLong(7, entity.getRole());
        if (entity.getInviteCode() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInviteCode());
        }
      }
    };
    this.__deletionAdapterOfCalendarEntity = new EntityDeletionOrUpdateAdapter<CalendarEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `calendars` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfCalendarEntity = new EntityDeletionOrUpdateAdapter<CalendarEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `calendars` SET `id` = ?,`name` = ?,`color` = ?,`isVisible` = ?,`isShared` = ?,`ownerUserId` = ?,`role` = ?,`inviteCode` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CalendarEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getColor());
        final int _tmp = entity.isVisible() ? 1 : 0;
        statement.bindLong(4, _tmp);
        final int _tmp_1 = entity.isShared() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        if (entity.getOwnerUserId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOwnerUserId());
        }
        statement.bindLong(7, entity.getRole());
        if (entity.getInviteCode() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInviteCode());
        }
        if (entity.getId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getId());
        }
      }
    };
  }

  @Override
  public Object insertCalendar(final CalendarEntity calendar,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCalendarEntity.insert(calendar);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCalendar(final CalendarEntity calendar,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCalendarEntity.handle(calendar);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCalendar(final CalendarEntity calendar,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCalendarEntity.handle(calendar);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CalendarEntity>> getAllCalendars() {
    final String _sql = "SELECT * FROM calendars";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendars"}, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "isVisible");
          final int _cursorIndexOfIsShared = CursorUtil.getColumnIndexOrThrow(_cursor, "isShared");
          final int _cursorIndexOfOwnerUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerUserId");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfInviteCode = CursorUtil.getColumnIndexOrThrow(_cursor, "inviteCode");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsShared;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsShared);
            _tmpIsShared = _tmp_1 != 0;
            final String _tmpOwnerUserId;
            if (_cursor.isNull(_cursorIndexOfOwnerUserId)) {
              _tmpOwnerUserId = null;
            } else {
              _tmpOwnerUserId = _cursor.getString(_cursorIndexOfOwnerUserId);
            }
            final int _tmpRole;
            _tmpRole = _cursor.getInt(_cursorIndexOfRole);
            final String _tmpInviteCode;
            if (_cursor.isNull(_cursorIndexOfInviteCode)) {
              _tmpInviteCode = null;
            } else {
              _tmpInviteCode = _cursor.getString(_cursorIndexOfInviteCode);
            }
            _item = new CalendarEntity(_tmpId,_tmpName,_tmpColor,_tmpIsVisible,_tmpIsShared,_tmpOwnerUserId,_tmpRole,_tmpInviteCode);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCalendarById(final String id,
      final Continuation<? super CalendarEntity> $completion) {
    final String _sql = "SELECT * FROM calendars WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CalendarEntity>() {
      @Override
      @Nullable
      public CalendarEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "isVisible");
          final int _cursorIndexOfIsShared = CursorUtil.getColumnIndexOrThrow(_cursor, "isShared");
          final int _cursorIndexOfOwnerUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerUserId");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfInviteCode = CursorUtil.getColumnIndexOrThrow(_cursor, "inviteCode");
          final CalendarEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsShared;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsShared);
            _tmpIsShared = _tmp_1 != 0;
            final String _tmpOwnerUserId;
            if (_cursor.isNull(_cursorIndexOfOwnerUserId)) {
              _tmpOwnerUserId = null;
            } else {
              _tmpOwnerUserId = _cursor.getString(_cursorIndexOfOwnerUserId);
            }
            final int _tmpRole;
            _tmpRole = _cursor.getInt(_cursorIndexOfRole);
            final String _tmpInviteCode;
            if (_cursor.isNull(_cursorIndexOfInviteCode)) {
              _tmpInviteCode = null;
            } else {
              _tmpInviteCode = _cursor.getString(_cursorIndexOfInviteCode);
            }
            _result = new CalendarEntity(_tmpId,_tmpName,_tmpColor,_tmpIsVisible,_tmpIsShared,_tmpOwnerUserId,_tmpRole,_tmpInviteCode);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CalendarEntity>> getVisibleCalendars() {
    final String _sql = "SELECT * FROM calendars WHERE isVisible = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"calendars"}, new Callable<List<CalendarEntity>>() {
      @Override
      @NonNull
      public List<CalendarEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfIsVisible = CursorUtil.getColumnIndexOrThrow(_cursor, "isVisible");
          final int _cursorIndexOfIsShared = CursorUtil.getColumnIndexOrThrow(_cursor, "isShared");
          final int _cursorIndexOfOwnerUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerUserId");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfInviteCode = CursorUtil.getColumnIndexOrThrow(_cursor, "inviteCode");
          final List<CalendarEntity> _result = new ArrayList<CalendarEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CalendarEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpColor;
            _tmpColor = _cursor.getInt(_cursorIndexOfColor);
            final boolean _tmpIsVisible;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVisible);
            _tmpIsVisible = _tmp != 0;
            final boolean _tmpIsShared;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsShared);
            _tmpIsShared = _tmp_1 != 0;
            final String _tmpOwnerUserId;
            if (_cursor.isNull(_cursorIndexOfOwnerUserId)) {
              _tmpOwnerUserId = null;
            } else {
              _tmpOwnerUserId = _cursor.getString(_cursorIndexOfOwnerUserId);
            }
            final int _tmpRole;
            _tmpRole = _cursor.getInt(_cursorIndexOfRole);
            final String _tmpInviteCode;
            if (_cursor.isNull(_cursorIndexOfInviteCode)) {
              _tmpInviteCode = null;
            } else {
              _tmpInviteCode = _cursor.getString(_cursorIndexOfInviteCode);
            }
            _item = new CalendarEntity(_tmpId,_tmpName,_tmpColor,_tmpIsVisible,_tmpIsShared,_tmpOwnerUserId,_tmpRole,_tmpInviteCode);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
