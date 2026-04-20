package com.example.ireader.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.ireader.data.model.Book;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class BookDao_Impl implements BookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Book> __insertionAdapterOfBook;

  private final EntityDeletionOrUpdateAdapter<Book> __updateAdapterOfBook;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBookProgress;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReadProgress;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReadingSettings;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBook;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllBooks;

  public BookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBook = new EntityInsertionAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `books` (`id`,`title`,`author`,`coverUri`,`filePath`,`fileSize`,`pageCount`,`lastReadPage`,`lastReadChapter`,`lastReadMode`,`lastScrollPosition`,`lastFontSize`,`lastZoom`,`lastReadTime`,`progress`,`format`,`addedTime`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getAuthor() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAuthor());
        }
        if (entity.getCoverUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCoverUri());
        }
        if (entity.getFilePath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFilePath());
        }
        statement.bindLong(6, entity.getFileSize());
        statement.bindLong(7, entity.getPageCount());
        statement.bindLong(8, entity.getLastReadPage());
        statement.bindLong(9, entity.getLastReadChapter());
        if (entity.getLastReadMode() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLastReadMode());
        }
        statement.bindLong(11, entity.getLastScrollPosition());
        statement.bindLong(12, entity.getLastFontSize());
        statement.bindDouble(13, entity.getLastZoom());
        statement.bindLong(14, entity.getLastReadTime());
        statement.bindLong(15, entity.getProgress());
        if (entity.getFormat() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getFormat());
        }
        statement.bindLong(17, entity.getAddedTime());
      }
    };
    this.__updateAdapterOfBook = new EntityDeletionOrUpdateAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `books` SET `id` = ?,`title` = ?,`author` = ?,`coverUri` = ?,`filePath` = ?,`fileSize` = ?,`pageCount` = ?,`lastReadPage` = ?,`lastReadChapter` = ?,`lastReadMode` = ?,`lastScrollPosition` = ?,`lastFontSize` = ?,`lastZoom` = ?,`lastReadTime` = ?,`progress` = ?,`format` = ?,`addedTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getAuthor() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAuthor());
        }
        if (entity.getCoverUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCoverUri());
        }
        if (entity.getFilePath() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getFilePath());
        }
        statement.bindLong(6, entity.getFileSize());
        statement.bindLong(7, entity.getPageCount());
        statement.bindLong(8, entity.getLastReadPage());
        statement.bindLong(9, entity.getLastReadChapter());
        if (entity.getLastReadMode() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLastReadMode());
        }
        statement.bindLong(11, entity.getLastScrollPosition());
        statement.bindLong(12, entity.getLastFontSize());
        statement.bindDouble(13, entity.getLastZoom());
        statement.bindLong(14, entity.getLastReadTime());
        statement.bindLong(15, entity.getProgress());
        if (entity.getFormat() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getFormat());
        }
        statement.bindLong(17, entity.getAddedTime());
        if (entity.getId() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getId());
        }
      }
    };
    this.__preparedStmtOfUpdateBookProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET progress = ?, lastReadPage = ?, lastReadTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateReadProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET progress = ?, lastReadPage = ?, lastReadChapter = ?, lastReadMode = ?, lastScrollPosition = ?, lastFontSize = ?, lastZoom = ?, lastReadTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateReadingSettings = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET lastReadMode = ?, lastFontSize = ?, lastReadTime = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteBook = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM books WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllBooks = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM books";
        return _query;
      }
    };
  }

  @Override
  public Object insertBook(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBook.insert(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBooks(final List<Book> books, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBook.insert(books);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBook(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBook.handle(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBookProgress(final String id, final int progress, final int lastReadPage,
      final long lastReadTime, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBookProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, progress);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, lastReadPage);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, lastReadTime);
        _argIndex = 4;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateBookProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReadProgress(final String id, final int progress, final int lastReadPage,
      final int lastReadChapter, final String lastReadMode, final int lastScrollPosition,
      final int lastFontSize, final float lastZoom, final long lastReadTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReadProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, progress);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, lastReadPage);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, lastReadChapter);
        _argIndex = 4;
        if (lastReadMode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastReadMode);
        }
        _argIndex = 5;
        _stmt.bindLong(_argIndex, lastScrollPosition);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, lastFontSize);
        _argIndex = 7;
        _stmt.bindDouble(_argIndex, lastZoom);
        _argIndex = 8;
        _stmt.bindLong(_argIndex, lastReadTime);
        _argIndex = 9;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateReadProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReadingSettings(final String id, final String lastReadMode,
      final int lastFontSize, final long lastReadTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReadingSettings.acquire();
        int _argIndex = 1;
        if (lastReadMode == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastReadMode);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, lastFontSize);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, lastReadTime);
        _argIndex = 4;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateReadingSettings.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBook(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBook.acquire();
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteBook.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllBooks(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllBooks.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllBooks.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Book>> getAllBooks() {
    final String _sql = "SELECT * FROM books ORDER BY lastReadTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfLastReadPage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadPage");
          final int _cursorIndexOfLastReadChapter = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadChapter");
          final int _cursorIndexOfLastReadMode = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadMode");
          final int _cursorIndexOfLastScrollPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastScrollPosition");
          final int _cursorIndexOfLastFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "lastFontSize");
          final int _cursorIndexOfLastZoom = CursorUtil.getColumnIndexOrThrow(_cursor, "lastZoom");
          final int _cursorIndexOfLastReadTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTime");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfAddedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "addedTime");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final int _tmpLastReadPage;
            _tmpLastReadPage = _cursor.getInt(_cursorIndexOfLastReadPage);
            final int _tmpLastReadChapter;
            _tmpLastReadChapter = _cursor.getInt(_cursorIndexOfLastReadChapter);
            final String _tmpLastReadMode;
            if (_cursor.isNull(_cursorIndexOfLastReadMode)) {
              _tmpLastReadMode = null;
            } else {
              _tmpLastReadMode = _cursor.getString(_cursorIndexOfLastReadMode);
            }
            final int _tmpLastScrollPosition;
            _tmpLastScrollPosition = _cursor.getInt(_cursorIndexOfLastScrollPosition);
            final int _tmpLastFontSize;
            _tmpLastFontSize = _cursor.getInt(_cursorIndexOfLastFontSize);
            final float _tmpLastZoom;
            _tmpLastZoom = _cursor.getFloat(_cursorIndexOfLastZoom);
            final long _tmpLastReadTime;
            _tmpLastReadTime = _cursor.getLong(_cursorIndexOfLastReadTime);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final String _tmpFormat;
            if (_cursor.isNull(_cursorIndexOfFormat)) {
              _tmpFormat = null;
            } else {
              _tmpFormat = _cursor.getString(_cursorIndexOfFormat);
            }
            final long _tmpAddedTime;
            _tmpAddedTime = _cursor.getLong(_cursorIndexOfAddedTime);
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpCoverUri,_tmpFilePath,_tmpFileSize,_tmpPageCount,_tmpLastReadPage,_tmpLastReadChapter,_tmpLastReadMode,_tmpLastScrollPosition,_tmpLastFontSize,_tmpLastZoom,_tmpLastReadTime,_tmpProgress,_tmpFormat,_tmpAddedTime);
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
  public Object getAllBooksOnce(final Continuation<? super List<Book>> $completion) {
    final String _sql = "SELECT * FROM books ORDER BY lastReadTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfLastReadPage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadPage");
          final int _cursorIndexOfLastReadChapter = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadChapter");
          final int _cursorIndexOfLastReadMode = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadMode");
          final int _cursorIndexOfLastScrollPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastScrollPosition");
          final int _cursorIndexOfLastFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "lastFontSize");
          final int _cursorIndexOfLastZoom = CursorUtil.getColumnIndexOrThrow(_cursor, "lastZoom");
          final int _cursorIndexOfLastReadTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTime");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfAddedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "addedTime");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final int _tmpLastReadPage;
            _tmpLastReadPage = _cursor.getInt(_cursorIndexOfLastReadPage);
            final int _tmpLastReadChapter;
            _tmpLastReadChapter = _cursor.getInt(_cursorIndexOfLastReadChapter);
            final String _tmpLastReadMode;
            if (_cursor.isNull(_cursorIndexOfLastReadMode)) {
              _tmpLastReadMode = null;
            } else {
              _tmpLastReadMode = _cursor.getString(_cursorIndexOfLastReadMode);
            }
            final int _tmpLastScrollPosition;
            _tmpLastScrollPosition = _cursor.getInt(_cursorIndexOfLastScrollPosition);
            final int _tmpLastFontSize;
            _tmpLastFontSize = _cursor.getInt(_cursorIndexOfLastFontSize);
            final float _tmpLastZoom;
            _tmpLastZoom = _cursor.getFloat(_cursorIndexOfLastZoom);
            final long _tmpLastReadTime;
            _tmpLastReadTime = _cursor.getLong(_cursorIndexOfLastReadTime);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final String _tmpFormat;
            if (_cursor.isNull(_cursorIndexOfFormat)) {
              _tmpFormat = null;
            } else {
              _tmpFormat = _cursor.getString(_cursorIndexOfFormat);
            }
            final long _tmpAddedTime;
            _tmpAddedTime = _cursor.getLong(_cursorIndexOfAddedTime);
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpCoverUri,_tmpFilePath,_tmpFileSize,_tmpPageCount,_tmpLastReadPage,_tmpLastReadChapter,_tmpLastReadMode,_tmpLastScrollPosition,_tmpLastFontSize,_tmpLastZoom,_tmpLastReadTime,_tmpProgress,_tmpFormat,_tmpAddedTime);
            _result.add(_item);
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
  public Object getBookById(final String id, final Continuation<? super Book> $completion) {
    final String _sql = "SELECT * FROM books WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Book>() {
      @Override
      @Nullable
      public Book call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfLastReadPage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadPage");
          final int _cursorIndexOfLastReadChapter = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadChapter");
          final int _cursorIndexOfLastReadMode = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadMode");
          final int _cursorIndexOfLastScrollPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastScrollPosition");
          final int _cursorIndexOfLastFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "lastFontSize");
          final int _cursorIndexOfLastZoom = CursorUtil.getColumnIndexOrThrow(_cursor, "lastZoom");
          final int _cursorIndexOfLastReadTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTime");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfAddedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "addedTime");
          final Book _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final int _tmpLastReadPage;
            _tmpLastReadPage = _cursor.getInt(_cursorIndexOfLastReadPage);
            final int _tmpLastReadChapter;
            _tmpLastReadChapter = _cursor.getInt(_cursorIndexOfLastReadChapter);
            final String _tmpLastReadMode;
            if (_cursor.isNull(_cursorIndexOfLastReadMode)) {
              _tmpLastReadMode = null;
            } else {
              _tmpLastReadMode = _cursor.getString(_cursorIndexOfLastReadMode);
            }
            final int _tmpLastScrollPosition;
            _tmpLastScrollPosition = _cursor.getInt(_cursorIndexOfLastScrollPosition);
            final int _tmpLastFontSize;
            _tmpLastFontSize = _cursor.getInt(_cursorIndexOfLastFontSize);
            final float _tmpLastZoom;
            _tmpLastZoom = _cursor.getFloat(_cursorIndexOfLastZoom);
            final long _tmpLastReadTime;
            _tmpLastReadTime = _cursor.getLong(_cursorIndexOfLastReadTime);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final String _tmpFormat;
            if (_cursor.isNull(_cursorIndexOfFormat)) {
              _tmpFormat = null;
            } else {
              _tmpFormat = _cursor.getString(_cursorIndexOfFormat);
            }
            final long _tmpAddedTime;
            _tmpAddedTime = _cursor.getLong(_cursorIndexOfAddedTime);
            _result = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpCoverUri,_tmpFilePath,_tmpFileSize,_tmpPageCount,_tmpLastReadPage,_tmpLastReadChapter,_tmpLastReadMode,_tmpLastScrollPosition,_tmpLastFontSize,_tmpLastZoom,_tmpLastReadTime,_tmpProgress,_tmpFormat,_tmpAddedTime);
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
  public Object bookExists(final String filePath, final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM books WHERE filePath = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (filePath == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filePath);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
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
  public Object bookExistsByTitle(final String title,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM books WHERE title = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (title == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, title);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
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
  public Flow<List<Book>> getBooksByFormat(final String format) {
    final String _sql = "SELECT * FROM books WHERE format = ? ORDER BY lastReadTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (format == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, format);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "fileSize");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfLastReadPage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadPage");
          final int _cursorIndexOfLastReadChapter = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadChapter");
          final int _cursorIndexOfLastReadMode = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadMode");
          final int _cursorIndexOfLastScrollPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastScrollPosition");
          final int _cursorIndexOfLastFontSize = CursorUtil.getColumnIndexOrThrow(_cursor, "lastFontSize");
          final int _cursorIndexOfLastZoom = CursorUtil.getColumnIndexOrThrow(_cursor, "lastZoom");
          final int _cursorIndexOfLastReadTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTime");
          final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
          final int _cursorIndexOfFormat = CursorUtil.getColumnIndexOrThrow(_cursor, "format");
          final int _cursorIndexOfAddedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "addedTime");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final long _tmpFileSize;
            _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final int _tmpLastReadPage;
            _tmpLastReadPage = _cursor.getInt(_cursorIndexOfLastReadPage);
            final int _tmpLastReadChapter;
            _tmpLastReadChapter = _cursor.getInt(_cursorIndexOfLastReadChapter);
            final String _tmpLastReadMode;
            if (_cursor.isNull(_cursorIndexOfLastReadMode)) {
              _tmpLastReadMode = null;
            } else {
              _tmpLastReadMode = _cursor.getString(_cursorIndexOfLastReadMode);
            }
            final int _tmpLastScrollPosition;
            _tmpLastScrollPosition = _cursor.getInt(_cursorIndexOfLastScrollPosition);
            final int _tmpLastFontSize;
            _tmpLastFontSize = _cursor.getInt(_cursorIndexOfLastFontSize);
            final float _tmpLastZoom;
            _tmpLastZoom = _cursor.getFloat(_cursorIndexOfLastZoom);
            final long _tmpLastReadTime;
            _tmpLastReadTime = _cursor.getLong(_cursorIndexOfLastReadTime);
            final int _tmpProgress;
            _tmpProgress = _cursor.getInt(_cursorIndexOfProgress);
            final String _tmpFormat;
            if (_cursor.isNull(_cursorIndexOfFormat)) {
              _tmpFormat = null;
            } else {
              _tmpFormat = _cursor.getString(_cursorIndexOfFormat);
            }
            final long _tmpAddedTime;
            _tmpAddedTime = _cursor.getLong(_cursorIndexOfAddedTime);
            _item = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpCoverUri,_tmpFilePath,_tmpFileSize,_tmpPageCount,_tmpLastReadPage,_tmpLastReadChapter,_tmpLastReadMode,_tmpLastScrollPosition,_tmpLastFontSize,_tmpLastZoom,_tmpLastReadTime,_tmpProgress,_tmpFormat,_tmpAddedTime);
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
