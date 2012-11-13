/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
<<<<<<< HEAD
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.DefaultDatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.OperationCanceledException;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Printer;

import dalvik.system.CloseGuard;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Exposes methods to manage a SQLite database.
 *
 * <p>
 * SQLiteDatabase has methods to create, delete, execute SQL commands, and
 * perform other common database management tasks.
 * </p><p>
 * See the Notepad sample application in the SDK for an example of creating
 * and managing a database.
 * </p><p>
 * Database names must be unique within an application, not across all applications.
 * </p>
 *
 * <h3>Localized Collation - ORDER BY</h3>
 * <p>
 * In addition to SQLite's default <code>BINARY</code> collator, Android supplies
 * two more, <code>LOCALIZED</code>, which changes with the system's current locale,
 * and <code>UNICODE</code>, which is the Unicode Collation Algorithm and not tailored
 * to the current locale.
 * </p>
 */
public final class SQLiteDatabase extends SQLiteClosable {
    private static final String TAG = "SQLiteDatabase";

    private static final int EVENT_DB_CORRUPT = 75004;

    // Stores reference to all databases opened in the current process.
    // (The referent Object is not used at this time.)
    // INVARIANT: Guarded by sActiveDatabases.
    private static WeakHashMap<SQLiteDatabase, Object> sActiveDatabases =
            new WeakHashMap<SQLiteDatabase, Object>();

    // Thread-local for database sessions that belong to this database.
    // Each thread has its own database session.
    // INVARIANT: Immutable.
    private final ThreadLocal<SQLiteSession> mThreadSession = new ThreadLocal<SQLiteSession>() {
        @Override
        protected SQLiteSession initialValue() {
            return createSession();
        }
    };

    // The optional factory to use when creating new Cursors.  May be null.
    // INVARIANT: Immutable.
    private final CursorFactory mCursorFactory;

    // Error handler to be used when SQLite returns corruption errors.
    // INVARIANT: Immutable.
    private final DatabaseErrorHandler mErrorHandler;

    // Shared database state lock.
    // This lock guards all of the shared state of the database, such as its
    // configuration, whether it is open or closed, and so on.  This lock should
    // be held for as little time as possible.
    //
    // The lock MUST NOT be held while attempting to acquire database connections or
    // while executing SQL statements on behalf of the client as it can lead to deadlock.
    //
    // It is ok to hold the lock while reconfiguring the connection pool or dumping
    // statistics because those operations are non-reentrant and do not try to acquire
    // connections that might be held by other threads.
    //
    // Basic rule: grab the lock, access or modify global state, release the lock, then
    // do the required SQL work.
    private final Object mLock = new Object();

    // Warns if the database is finalized without being closed properly.
    // INVARIANT: Guarded by mLock.
    private final CloseGuard mCloseGuardLocked = CloseGuard.get();

    // The database configuration.
    // INVARIANT: Guarded by mLock.
    private final SQLiteDatabaseConfiguration mConfigurationLocked;

    // The connection pool for the database, null when closed.
    // The pool itself is thread-safe, but the reference to it can only be acquired
    // when the lock is held.
    // INVARIANT: Guarded by mLock.
    private SQLiteConnectionPool mConnectionPoolLocked;

    // True if the database has attached databases.
    // INVARIANT: Guarded by mLock.
    private boolean mHasAttachedDbsLocked;

    /**
     * When a constraint violation occurs, an immediate ROLLBACK occurs,
     * thus ending the current transaction, and the command aborts with a
     * return code of SQLITE_CONSTRAINT. If no transaction is active
     * (other than the implied transaction that is created on every command)
     * then this algorithm works the same as ABORT.
     */
    public static final int CONFLICT_ROLLBACK = 1;

    /**
     * When a constraint violation occurs,no ROLLBACK is executed
     * so changes from prior commands within the same transaction
     * are preserved. This is the default behavior.
     */
    public static final int CONFLICT_ABORT = 2;

    /**
     * When a constraint violation occurs, the command aborts with a return
     * code SQLITE_CONSTRAINT. But any changes to the database that
     * the command made prior to encountering the constraint violation
     * are preserved and are not backed out.
     */
    public static final int CONFLICT_FAIL = 3;

    /**
     * When a constraint violation occurs, the one row that contains
     * the constraint violation is not inserted or changed.
     * But the command continues executing normally. Other rows before and
     * after the row that contained the constraint violation continue to be
     * inserted or updated normally. No error is returned.
     */
    public static final int CONFLICT_IGNORE = 4;

    /**
     * When a UNIQUE constraint violation occurs, the pre-existing rows that
     * are causing the constraint violation are removed prior to inserting
     * or updating the current row. Thus the insert or update always occurs.
     * The command continues executing normally. No error is returned.
     * If a NOT NULL constraint violation occurs, the NULL value is replaced
     * by the default value for that column. If the column has no default
     * value, then the ABORT algorithm is used. If a CHECK constraint
     * violation occurs then the IGNORE algorithm is used. When this conflict
     * resolution strategy deletes rows in order to satisfy a constraint,
     * it does not invoke delete triggers on those rows.
     * This behavior might change in a future release.
     */
    public static final int CONFLICT_REPLACE = 5;

    /**
     * Use the following when no conflict action is specified.
     */
    public static final int CONFLICT_NONE = 0;

    private static final String[] CONFLICT_VALUES = new String[]
            {"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
=======
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.os.Debug;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Exposes methods to manage a SQLite database.
 * <p>SQLiteDatabase has methods to create, delete, execute SQL commands, and
 * perform other common database management tasks.
 * <p>See the Notepad sample application in the SDK for an example of creating
 * and managing a database.
 * <p> Database names must be unique within an application, not across all
 * applications.
 *
 * <h3>Localized Collation - ORDER BY</h3>
 * <p>In addition to SQLite's default <code>BINARY</code> collator, Android supplies
 * two more, <code>LOCALIZED</code>, which changes with the system's current locale
 * if you wire it up correctly (XXX a link needed!), and <code>UNICODE</code>, which
 * is the Unicode Collation Algorithm and not tailored to the current locale.
 */
public class SQLiteDatabase extends SQLiteClosable {
    private final static String TAG = "Database";
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Maximum Length Of A LIKE Or GLOB Pattern
     * The pattern matching algorithm used in the default LIKE and GLOB implementation
     * of SQLite can exhibit O(N^2) performance (where N is the number of characters in
     * the pattern) for certain pathological cases. To avoid denial-of-service attacks
     * the length of the LIKE or GLOB pattern is limited to SQLITE_MAX_LIKE_PATTERN_LENGTH bytes.
     * The default value of this limit is 50000. A modern workstation can evaluate
     * even a pathological LIKE or GLOB pattern of 50000 bytes relatively quickly.
     * The denial of service problem only comes into play when the pattern length gets
     * into millions of bytes. Nevertheless, since most useful LIKE or GLOB patterns
     * are at most a few dozen bytes in length, paranoid application developers may
     * want to reduce this parameter to something in the range of a few hundred
     * if they know that external users are able to generate arbitrary patterns.
     */
    public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;

    /**
<<<<<<< HEAD
     * Open flag: Flag for {@link #openDatabase} to open the database for reading and writing.
=======
     * Flag for {@link #openDatabase} to open the database for reading and writing.
>>>>>>> 54b6cfa... Initial Contribution
     * If the disk is full, this may fail even before you actually write anything.
     *
     * {@more} Note that the value of this flag is 0, so it is the default.
     */
    public static final int OPEN_READWRITE = 0x00000000;          // update native code if changing

    /**
<<<<<<< HEAD
     * Open flag: Flag for {@link #openDatabase} to open the database for reading only.
=======
     * Flag for {@link #openDatabase} to open the database for reading only.
>>>>>>> 54b6cfa... Initial Contribution
     * This is the only reliable way to open a database if the disk may be full.
     */
    public static final int OPEN_READONLY = 0x00000001;           // update native code if changing

    private static final int OPEN_READ_MASK = 0x00000001;         // update native code if changing

    /**
<<<<<<< HEAD
     * Open flag: Flag for {@link #openDatabase} to open the database without support for
     * localized collators.
=======
     * Flag for {@link #openDatabase} to open the database without support for localized collators.
>>>>>>> 54b6cfa... Initial Contribution
     *
     * {@more} This causes the collator <code>LOCALIZED</code> not to be created.
     * You must be consistent when using this flag to use the setting the database was
     * created with.  If this is set, {@link #setLocale} will do nothing.
     */
    public static final int NO_LOCALIZED_COLLATORS = 0x00000010;  // update native code if changing

    /**
<<<<<<< HEAD
     * Open flag: Flag for {@link #openDatabase} to create the database file if it does not
     * already exist.
=======
     * Flag for {@link #openDatabase} to create the database file if it does not already exist.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public static final int CREATE_IF_NECESSARY = 0x10000000;     // update native code if changing

    /**
<<<<<<< HEAD
     * Open flag: Flag for {@link #openDatabase} to open the database file with
     * write-ahead logging enabled by default.  Using this flag is more efficient
     * than calling {@link #enableWriteAheadLogging}.
     *
     * Write-ahead logging cannot be used with read-only databases so the value of
     * this flag is ignored if the database is opened read-only.
     *
     * @see #enableWriteAheadLogging
     */
    public static final int ENABLE_WRITE_AHEAD_LOGGING = 0x20000000;

    /**
     * Absolute max value that can be set by {@link #setMaxSqlCacheSize(int)}.
     *
     * Each prepared-statement is between 1K - 6K, depending on the complexity of the
     * SQL statement & schema.  A large SQL cache may use a significant amount of memory.
     */
    public static final int MAX_SQL_CACHE_SIZE = 100;

    private SQLiteDatabase(String path, int openFlags, CursorFactory cursorFactory,
            DatabaseErrorHandler errorHandler) {
        mCursorFactory = cursorFactory;
        mErrorHandler = errorHandler != null ? errorHandler : new DefaultDatabaseErrorHandler();
        mConfigurationLocked = new SQLiteDatabaseConfiguration(path, openFlags);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    @Override
    protected void onAllReferencesReleased() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        final SQLiteConnectionPool pool;
        synchronized (mLock) {
            if (mCloseGuardLocked != null) {
                if (finalized) {
                    mCloseGuardLocked.warnIfOpen();
                }
                mCloseGuardLocked.close();
            }

            pool = mConnectionPoolLocked;
            mConnectionPoolLocked = null;
        }

        if (!finalized) {
            synchronized (sActiveDatabases) {
                sActiveDatabases.remove(this);
            }

            if (pool != null) {
                pool.close();
            }
=======
     * Indicates whether the most-recently started transaction has been marked as successful.
     */
    private boolean mInnerTransactionIsSuccessful;

    /**
     * Valid during the life of a transaction, and indicates whether the entire transaction (the
     * outer one and all of the inner ones) so far has been successful.
     */
    private boolean mTransactionIsSuccessful;

    /** Synchronize on this when accessing the database */
    private final ReentrantLock mLock = new ReentrantLock(true);

    private long mLockAcquiredWallTime = 0L;
    private long mLockAcquiredThreadTime = 0L;
    
    // limit the frequency of complaints about each database to one within 20 sec
    // unless run command adb shell setprop log.tag.Database VERBOSE  
    private static final int LOCK_WARNING_WINDOW_IN_MS = 20000;
    /** If the lock is held this long then a warning will be printed when it is released. */
    private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS = 300;
    private static final int LOCK_ACQUIRED_WARNING_THREAD_TIME_IN_MS = 100;
    private static final int LOCK_ACQUIRED_WARNING_TIME_IN_MS_ALWAYS_PRINT = 2000;

    private long mLastLockMessageTime = 0L;
    
    /** Used by native code, do not rename */
    /* package */ int mNativeHandle = 0;

    /** Used to make temp table names unique */
    /* package */ int mTempTableSequence = 0;

    /** The path for the database file */
    private String mPath;

    /** The flags passed to open/create */
    private int mFlags;

    /** The optional factory to use when creating new Cursors */
    private CursorFactory mFactory;
    
    private WeakHashMap<SQLiteClosable, Object> mPrograms;
 
    private final RuntimeException mLeakedException;
    /**
     * @param closable
     */
    void addSQLiteClosable(SQLiteClosable closable) {
        lock();
        try {
            mPrograms.put(closable, null);
        } finally {
            unlock();
        }
    }
    
    void removeSQLiteClosable(SQLiteClosable closable) {
        lock();
        try {
            mPrograms.remove(closable);
        } finally {
            unlock();
        }
    }    
   
    @Override
    protected void onAllReferencesReleased() {
        if (isOpen()) {
            dbclose();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Attempts to release memory that SQLite holds but does not require to
     * operate properly. Typically this memory will come from the page cache.
<<<<<<< HEAD
     *
     * @return the number of bytes actually released
     */
    public static int releaseMemory() {
        return SQLiteGlobal.releaseMemory();
    }
=======
     * 
     * @return the number of bytes actually released
     */
    static public native int releaseMemory(); 
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Control whether or not the SQLiteDatabase is made thread-safe by using locks
     * around critical sections. This is pretty expensive, so if you know that your
     * DB will only be used by a single thread then you should set this to false.
     * The default is true.
     * @param lockingEnabled set to true to enable locks, false otherwise
<<<<<<< HEAD
     *
     * @deprecated This method now does nothing.  Do not use.
     */
    @Deprecated
    public void setLockingEnabled(boolean lockingEnabled) {
    }

    /**
     * Gets a label to use when describing the database in log messages.
     * @return The label.
     */
    String getLabel() {
        synchronized (mLock) {
            return mConfigurationLocked.label;
=======
     */
    public void setLockingEnabled(boolean lockingEnabled) {
        mLockingEnabled = lockingEnabled;
    }

    /**
     * If set then the SQLiteDatabase is made thread-safe by using locks
     * around critical sections
     */
    private boolean mLockingEnabled = true;

    /* package */ void onCorruption() {
        try {
            // Close the database (if we can), which will cause subsequent operations to fail.
            close();
        } finally {
            Log.e(TAG, "Removing corrupt database: " + mPath);
            // Delete the corrupt file.  Don't re-create it now -- that would just confuse people
            // -- but the next time someone tries to open it, they can set it up from scratch.
            new File(mPath).delete();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Sends a corruption message to the database error handler.
     */
    void onCorruption() {
        EventLog.writeEvent(EVENT_DB_CORRUPT, getLabel());
        mErrorHandler.onCorruption(this);
    }

    /**
     * Gets the {@link SQLiteSession} that belongs to this thread for this database.
     * Once a thread has obtained a session, it will continue to obtain the same
     * session even after the database has been closed (although the session will not
     * be usable).  However, a thread that does not already have a session cannot
     * obtain one after the database has been closed.
     *
     * The idea is that threads that have active connections to the database may still
     * have work to complete even after the call to {@link #close}.  Active database
     * connections are not actually disposed until they are released by the threads
     * that own them.
     *
     * @return The session, never null.
     *
     * @throws IllegalStateException if the thread does not yet have a session and
     * the database is not open.
     */
    SQLiteSession getThreadSession() {
        return mThreadSession.get(); // initialValue() throws if database closed
    }

    SQLiteSession createSession() {
        final SQLiteConnectionPool pool;
        synchronized (mLock) {
            throwIfNotOpenLocked();
            pool = mConnectionPoolLocked;
        }
        return new SQLiteSession(pool);
    }

    /**
     * Gets default connection flags that are appropriate for this thread, taking into
     * account whether the thread is acting on behalf of the UI.
     *
     * @param readOnly True if the connection should be read-only.
     * @return The connection flags.
     */
    int getThreadDefaultConnectionFlags(boolean readOnly) {
        int flags = readOnly ? SQLiteConnectionPool.CONNECTION_FLAG_READ_ONLY :
                SQLiteConnectionPool.CONNECTION_FLAG_PRIMARY_CONNECTION_AFFINITY;
        if (isMainThread()) {
            flags |= SQLiteConnectionPool.CONNECTION_FLAG_INTERACTIVE;
        }
        return flags;
    }

    private static boolean isMainThread() {
        // FIXME: There should be a better way to do this.
        // Would also be nice to have something that would work across Binder calls.
        Looper looper = Looper.myLooper();
        return looper != null && looper == Looper.getMainLooper();
    }

    /**
     * Begins a transaction in EXCLUSIVE mode.
     * <p>
     * Transactions can be nested.
     * When the outer transaction is ended all of
     * the work done in that transaction and all of the nested transactions will be committed or
     * rolled back. The changes will be rolled back if any transaction is ended without being
     * marked as clean (by calling setTransactionSuccessful). Otherwise they will be committed.
     * </p>
     * <p>Here is the standard idiom for transactions:
     *
     * <pre>
     *   db.beginTransaction();
     *   try {
     *     ...
     *     db.setTransactionSuccessful();
     *   } finally {
     *     db.endTransaction();
     *   }
     * </pre>
     */
    public void beginTransaction() {
        beginTransaction(null /* transactionStatusCallback */, true);
    }

    /**
     * Begins a transaction in IMMEDIATE mode. Transactions can be nested. When
     * the outer transaction is ended all of the work done in that transaction
     * and all of the nested transactions will be committed or rolled back. The
     * changes will be rolled back if any transaction is ended without being
     * marked as clean (by calling setTransactionSuccessful). Otherwise they
     * will be committed.
     * <p>
     * Here is the standard idiom for transactions:
     *
     * <pre>
     *   db.beginTransactionNonExclusive();
     *   try {
     *     ...
     *     db.setTransactionSuccessful();
     *   } finally {
     *     db.endTransaction();
     *   }
     * </pre>
     */
    public void beginTransactionNonExclusive() {
        beginTransaction(null /* transactionStatusCallback */, false);
    }

    /**
     * Begins a transaction in EXCLUSIVE mode.
     * <p>
     * Transactions can be nested.
     * When the outer transaction is ended all of
     * the work done in that transaction and all of the nested transactions will be committed or
     * rolled back. The changes will be rolled back if any transaction is ended without being
     * marked as clean (by calling setTransactionSuccessful). Otherwise they will be committed.
     * </p>
     * <p>Here is the standard idiom for transactions:
     *
     * <pre>
     *   db.beginTransactionWithListener(listener);
     *   try {
     *     ...
     *     db.setTransactionSuccessful();
     *   } finally {
     *     db.endTransaction();
     *   }
     * </pre>
     *
     * @param transactionListener listener that should be notified when the transaction begins,
     * commits, or is rolled back, either explicitly or by a call to
     * {@link #yieldIfContendedSafely}.
     */
    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        beginTransaction(transactionListener, true);
    }

    /**
     * Begins a transaction in IMMEDIATE mode. Transactions can be nested. When
     * the outer transaction is ended all of the work done in that transaction
     * and all of the nested transactions will be committed or rolled back. The
     * changes will be rolled back if any transaction is ended without being
     * marked as clean (by calling setTransactionSuccessful). Otherwise they
     * will be committed.
     * <p>
     * Here is the standard idiom for transactions:
     *
     * <pre>
     *   db.beginTransactionWithListenerNonExclusive(listener);
=======
     * Locks the database for exclusive access. The database lock must be held when
     * touch the native sqlite3* object since it is single threaded and uses
     * a polling lock contention algorithm. The lock is recursive, and may be acquired
     * multiple times by the same thread. This is a no-op if mLockingEnabled is false.
     * 
     * @see #unlock()
     */
    /* package */ void lock() {
        if (!mLockingEnabled) return;
        mLock.lock();
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING) {
            if (mLock.getHoldCount() == 1) {
                // Use elapsed real-time since the CPU may sleep when waiting for IO
                mLockAcquiredWallTime = SystemClock.elapsedRealtime();
                mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
            }
        }
    }

    /**
     * Locks the database for exclusive access. The database lock must be held when
     * touch the native sqlite3* object since it is single threaded and uses
     * a polling lock contention algorithm. The lock is recursive, and may be acquired
     * multiple times by the same thread.
     *
     * @see #unlockForced()
     */
    private void lockForced() {
        mLock.lock();
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING) {
            if (mLock.getHoldCount() == 1) {
                // Use elapsed real-time since the CPU may sleep when waiting for IO
                mLockAcquiredWallTime = SystemClock.elapsedRealtime();
                mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
            }
        }
    }

    /**
     * Releases the database lock. This is a no-op if mLockingEnabled is false.
     * 
     * @see #unlock()
     */
    /* package */ void unlock() {
        if (!mLockingEnabled) return;
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING) {
            if (mLock.getHoldCount() == 1) {
                checkLockHoldTime();
            }
        }
        mLock.unlock();
    }

    /**
     * Releases the database lock.
     *
     * @see #unlockForced()
     */
    private void unlockForced() {
        if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING) {
            if (mLock.getHoldCount() == 1) {
                checkLockHoldTime();
            }
        }
        mLock.unlock();
    }

    private void checkLockHoldTime() {
        // Use elapsed real-time since the CPU may sleep when waiting for IO
        long elapsedTime = SystemClock.elapsedRealtime();
        long lockedTime = elapsedTime - mLockAcquiredWallTime;                
        if (lockedTime < LOCK_ACQUIRED_WARNING_TIME_IN_MS_ALWAYS_PRINT &&
                !Log.isLoggable(TAG, Log.VERBOSE) &&
                (elapsedTime - mLastLockMessageTime) < LOCK_WARNING_WINDOW_IN_MS) {
            return;
        }
        if (lockedTime > LOCK_ACQUIRED_WARNING_TIME_IN_MS) {
            int threadTime = (int)
                    ((Debug.threadCpuTimeNanos() - mLockAcquiredThreadTime) / 1000000);
            if (threadTime > LOCK_ACQUIRED_WARNING_THREAD_TIME_IN_MS ||
                    lockedTime > LOCK_ACQUIRED_WARNING_TIME_IN_MS_ALWAYS_PRINT) {
                mLastLockMessageTime = elapsedTime;
                String msg = "lock held on " + mPath + " for " + lockedTime + "ms. Thread time was "
                        + threadTime + "ms";
                if (SQLiteDebug.DEBUG_LOCK_TIME_TRACKING_STACK_TRACE) {
                    Log.d(TAG, msg, new Exception());
                } else {
                    Log.d(TAG, msg);
                }
            }
        }
    }

    /**
     * Begins a transaction. Transactions can be nested. When the outer transaction is ended all of
     * the work done in that transaction and all of the nested transactions will be committed or
     * rolled back. The changes will be rolled back if any transaction is ended without being
     * marked as clean (by calling setTransactionSuccessful). Otherwise they will be committed.
     *
     * <p>Here is the standard idiom for transactions:
     *
     * <pre>
     *   db.beginTransaction();
>>>>>>> 54b6cfa... Initial Contribution
     *   try {
     *     ...
     *     db.setTransactionSuccessful();
     *   } finally {
     *     db.endTransaction();
     *   }
     * </pre>
<<<<<<< HEAD
     *
     * @param transactionListener listener that should be notified when the
     *            transaction begins, commits, or is rolled back, either
     *            explicitly or by a call to {@link #yieldIfContendedSafely}.
     */
    public void beginTransactionWithListenerNonExclusive(
            SQLiteTransactionListener transactionListener) {
        beginTransaction(transactionListener, false);
    }

    private void beginTransaction(SQLiteTransactionListener transactionListener,
            boolean exclusive) {
        acquireReference();
        try {
            getThreadSession().beginTransaction(
                    exclusive ? SQLiteSession.TRANSACTION_MODE_EXCLUSIVE :
                            SQLiteSession.TRANSACTION_MODE_IMMEDIATE,
                    transactionListener,
                    getThreadDefaultConnectionFlags(false /*readOnly*/), null);
        } finally {
            releaseReference();
=======
     */
    public void beginTransaction() {
        lockForced();
        boolean ok = false;
        try {
            // If this thread already had the lock then get out
            if (mLock.getHoldCount() > 1) {
                if (mInnerTransactionIsSuccessful) {
                    String msg = "Cannot call beginTransaction between "
                            + "calling setTransactionSuccessful and endTransaction";
                    IllegalStateException e = new IllegalStateException(msg);
                    Log.e(TAG, "beginTransaction() failed", e);
                    throw e;
                }
                ok = true;
                return;
            }

            // This thread didn't already have the lock, so begin a database
            // transaction now.
            execSQL("BEGIN EXCLUSIVE;");
            mTransactionIsSuccessful = true;
            mInnerTransactionIsSuccessful = false;
            ok = true;
        } finally {
            if (!ok) {
                // beginTransaction is called before the try block so we must release the lock in
                // the case of failure.
                unlockForced();
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * End a transaction. See beginTransaction for notes about how to use this and when transactions
     * are committed and rolled back.
     */
    public void endTransaction() {
<<<<<<< HEAD
        acquireReference();
        try {
            getThreadSession().endTransaction(null);
        } finally {
            releaseReference();
=======
        if (!mLock.isHeldByCurrentThread()) {
            throw new IllegalStateException("no transaction pending");
        }
        try {
            if (mInnerTransactionIsSuccessful) {
                mInnerTransactionIsSuccessful = false;
            } else {
                mTransactionIsSuccessful = false;
            }
            if (mLock.getHoldCount() != 1) {
                return;
            }
            if (mTransactionIsSuccessful) {
                execSQL("COMMIT;");
            } else {
                execSQL("ROLLBACK;");
            }
        } finally {
            unlockForced();
            if (Config.LOGV) {
                Log.v(TAG, "unlocked " + Thread.currentThread()
                        + ", holdCount is " + mLock.getHoldCount());
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Marks the current transaction as successful. Do not do any more database work between
     * calling this and calling endTransaction. Do as little non-database work as possible in that
     * situation too. If any errors are encountered between this and endTransaction the transaction
     * will still be committed.
     *
     * @throws IllegalStateException if the current thread is not in a transaction or the
     * transaction is already marked as successful.
     */
    public void setTransactionSuccessful() {
<<<<<<< HEAD
        acquireReference();
        try {
            getThreadSession().setTransactionSuccessful();
        } finally {
            releaseReference();
        }
    }

    /**
     * Returns true if the current thread has a transaction pending.
     *
     * @return True if the current thread is in a transaction.
     */
    public boolean inTransaction() {
        acquireReference();
        try {
            return getThreadSession().hasTransaction();
        } finally {
            releaseReference();
        }
    }

    /**
     * Returns true if the current thread is holding an active connection to the database.
     * <p>
     * The name of this method comes from a time when having an active connection
     * to the database meant that the thread was holding an actual lock on the
     * database.  Nowadays, there is no longer a true "database lock" although threads
     * may block if they cannot acquire a database connection to perform a
     * particular operation.
     * </p>
     *
     * @return True if the current thread is holding an active connection to the database.
     */
    public boolean isDbLockedByCurrentThread() {
        acquireReference();
        try {
            return getThreadSession().hasConnection();
        } finally {
            releaseReference();
        }
    }

    /**
     * Always returns false.
     * <p>
     * There is no longer the concept of a database lock, so this method always returns false.
     * </p>
     *
     * @return False.
     * @deprecated Always returns false.  Do not use this method.
     */
    @Deprecated
    public boolean isDbLockedByOtherThreads() {
        return false;
=======
        if (!mLock.isHeldByCurrentThread()) {
            throw new IllegalStateException("no transaction pending");
        }
        if (mInnerTransactionIsSuccessful) {
            throw new IllegalStateException(
                    "setTransactionSuccessful may only be called once per call to beginTransaction");
        }
        mInnerTransactionIsSuccessful = true;
    }

    /**
     * return true if there is a transaction pending
     */
    public boolean inTransaction() {
        return mLock.getHoldCount() > 0;
    }

    /**
     * Checks if the database lock is held by this thread.
     *
     * @return true, if this thread is holding the database lock.
     */
    public boolean isDbLockedByCurrentThread() {
        return mLock.isHeldByCurrentThread();
    }

    /**
     * Checks if the database is locked by another thread. This is
     * just an estimate, since this status can change at any time,
     * including after the call is made but before the result has
     * been acted upon.
     *
     * @return true, if the database is locked by another thread
     */
    public boolean isDbLockedByOtherThreads() {
        return !mLock.isHeldByCurrentThread() && mLock.isLocked();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Temporarily end the transaction to let other threads run. The transaction is assumed to be
     * successful so far. Do not call setTransactionSuccessful before calling this. When this
     * returns a new transaction will have been created but not marked as successful.
     * @return true if the transaction was yielded
<<<<<<< HEAD
     * @deprecated if the db is locked more than once (becuase of nested transactions) then the lock
     *   will not be yielded. Use yieldIfContendedSafely instead.
     */
    @Deprecated
    public boolean yieldIfContended() {
        return yieldIfContendedHelper(false /* do not check yielding */,
                -1 /* sleepAfterYieldDelay */);
    }

    /**
     * Temporarily end the transaction to let other threads run. The transaction is assumed to be
     * successful so far. Do not call setTransactionSuccessful before calling this. When this
     * returns a new transaction will have been created but not marked as successful. This assumes
     * that there are no nested transactions (beginTransaction has only been called once) and will
     * throw an exception if that is not the case.
     * @return true if the transaction was yielded
     */
    public boolean yieldIfContendedSafely() {
        return yieldIfContendedHelper(true /* check yielding */, -1 /* sleepAfterYieldDelay*/);
    }

    /**
     * Temporarily end the transaction to let other threads run. The transaction is assumed to be
     * successful so far. Do not call setTransactionSuccessful before calling this. When this
     * returns a new transaction will have been created but not marked as successful. This assumes
     * that there are no nested transactions (beginTransaction has only been called once) and will
     * throw an exception if that is not the case.
     * @param sleepAfterYieldDelay if > 0, sleep this long before starting a new transaction if
     *   the lock was actually yielded. This will allow other background threads to make some
     *   more progress than they would if we started the transaction immediately.
     * @return true if the transaction was yielded
     */
    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return yieldIfContendedHelper(true /* check yielding */, sleepAfterYieldDelay);
    }

    private boolean yieldIfContendedHelper(boolean throwIfUnsafe, long sleepAfterYieldDelay) {
        acquireReference();
        try {
            return getThreadSession().yieldTransaction(sleepAfterYieldDelay, throwIfUnsafe, null);
        } finally {
            releaseReference();
=======
     */
    public boolean yieldIfContended() {
        if (mLock.getQueueLength() == 0) {
            // Reset the lock acquire time since we know that the thread was willing to yield
            // the lock at this time.
            mLockAcquiredWallTime = SystemClock.elapsedRealtime();
            mLockAcquiredThreadTime = Debug.threadCpuTimeNanos();
            return false;
        }
        setTransactionSuccessful();
        endTransaction();
        beginTransaction();
        return true;
    }

    /** Maps table names to info about what to which _sync_time column to set
     * to NULL on an update. This is used to support syncing. */
    private final Map<String, SyncUpdateInfo> mSyncUpdateInfo =
            new HashMap<String, SyncUpdateInfo>();

    public Map<String, String> getSyncedTables() {
        synchronized(mSyncUpdateInfo) {
            HashMap<String, String> tables = new HashMap<String, String>();
            for (String table : mSyncUpdateInfo.keySet()) {
                SyncUpdateInfo info = mSyncUpdateInfo.get(table);
                if (info.deletedTable != null) {
                    tables.put(table, info.deletedTable);
                }
            }
            return tables;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Deprecated.
     * @deprecated This method no longer serves any useful purpose and has been deprecated.
     */
    @Deprecated
    public Map<String, String> getSyncedTables() {
        return new HashMap<String, String>(0);
    }

    /**
     * Open the database according to the flags {@link #OPEN_READWRITE}
     * {@link #OPEN_READONLY} {@link #CREATE_IF_NECESSARY} and/or {@link #NO_LOCALIZED_COLLATORS}.
     *
     * <p>Sets the locale of the database to the  the system's current locale.
     * Call {@link #setLocale} if you would like something else.</p>
     *
     * @param path to database file to open and/or create
     * @param factory an optional factory class that is called to instantiate a
     *            cursor when query is called, or null for default
     * @param flags to control database access mode
     * @return the newly opened database
     * @throws SQLiteException if the database cannot be opened
     */
    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags) {
        return openDatabase(path, factory, flags, null);
=======
     * Internal class used to keep track what needs to be marked as changed
     * when an update occurs. This is used for syncing, so the sync engine
     * knows what data has been updated locally.
     */
    static private class SyncUpdateInfo {
        /**
         * Creates the SyncUpdateInfo class.
         *
         * @param masterTable The table to set _sync_time to NULL in
         * @param deletedTable The deleted table that corresponds to the
         *          master table
         * @param foreignKey The key that refers to the primary key in table
         */
        SyncUpdateInfo(String masterTable, String deletedTable,
                String foreignKey) {
            this.masterTable = masterTable;
            this.deletedTable = deletedTable;
            this.foreignKey = foreignKey;
        }

        /** The table containing the _sync_time column */
        String masterTable;

        /** The deleted table that corresponds to the master table */
        String deletedTable;

        /** The key in the local table the row in table. It may be _id, if table
         * is the local table. */
        String foreignKey;
    }

    /**
     * Used to allow returning sub-classes of {@link Cursor} when calling query.
     */
    public interface CursorFactory {
        /**
         * See
         * {@link SQLiteCursor#SQLiteCursor(SQLiteDatabase, SQLiteCursorDriver,
         * String, SQLiteQuery)}.
         */
        public Cursor newCursor(SQLiteDatabase db,
                SQLiteCursorDriver masterQuery, String editTable,
                SQLiteQuery query);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Open the database according to the flags {@link #OPEN_READWRITE}
     * {@link #OPEN_READONLY} {@link #CREATE_IF_NECESSARY} and/or {@link #NO_LOCALIZED_COLLATORS}.
     *
     * <p>Sets the locale of the database to the  the system's current locale.
     * Call {@link #setLocale} if you would like something else.</p>
     *
<<<<<<< HEAD
     * <p>Accepts input param: a concrete instance of {@link DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
=======
>>>>>>> 54b6cfa... Initial Contribution
     * @param path to database file to open and/or create
     * @param factory an optional factory class that is called to instantiate a
     *            cursor when query is called, or null for default
     * @param flags to control database access mode
<<<<<<< HEAD
     * @param errorHandler the {@link DatabaseErrorHandler} obj to be used to handle corruption
     * when sqlite reports database corruption
     * @return the newly opened database
     * @throws SQLiteException if the database cannot be opened
     */
    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags,
            DatabaseErrorHandler errorHandler) {
        SQLiteDatabase db = new SQLiteDatabase(path, flags, factory, errorHandler);
        db.open();
        return db;
=======
     * @return the newly opened database
     * @throws SQLiteException if the database cannot be opened
     */
    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags) {
        SQLiteDatabase db = null;
        try {
            // Open the database.
            return new SQLiteDatabase(path, factory, flags);
        } catch (SQLiteDatabaseCorruptException e) {
            // Try to recover from this, if we can.
            // TODO: should we do this for other open failures?
            Log.e(TAG, "Deleting and re-creating corrupt database " + path, e);
            new File(path).delete();
            return new SQLiteDatabase(path, factory, flags);
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Equivalent to openDatabase(file.getPath(), factory, CREATE_IF_NECESSARY).
     */
    public static SQLiteDatabase openOrCreateDatabase(File file, CursorFactory factory) {
        return openOrCreateDatabase(file.getPath(), factory);
    }

    /**
     * Equivalent to openDatabase(path, factory, CREATE_IF_NECESSARY).
     */
    public static SQLiteDatabase openOrCreateDatabase(String path, CursorFactory factory) {
<<<<<<< HEAD
        return openDatabase(path, factory, CREATE_IF_NECESSARY, null);
    }

    /**
     * Equivalent to openDatabase(path, factory, CREATE_IF_NECESSARY, errorHandler).
     */
    public static SQLiteDatabase openOrCreateDatabase(String path, CursorFactory factory,
            DatabaseErrorHandler errorHandler) {
        return openDatabase(path, factory, CREATE_IF_NECESSARY, errorHandler);
    }

    /**
     * Deletes a database including its journal file and other auxiliary files
     * that may have been created by the database engine.
     *
     * @param file The database file path.
     * @return True if the database was successfully deleted.
     */
    public static boolean deleteDatabase(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        boolean deleted = false;
        deleted |= file.delete();
        deleted |= new File(file.getPath() + "-journal").delete();
        deleted |= new File(file.getPath() + "-shm").delete();
        deleted |= new File(file.getPath() + "-wal").delete();

        File dir = file.getParentFile();
        if (dir != null) {
            final String prefix = file.getName() + "-mj";
            final FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File candidate) {
                    return candidate.getName().startsWith(prefix);
                }
            };
            for (File masterJournal : dir.listFiles(filter)) {
                deleted |= masterJournal.delete();
            }
        }
        return deleted;
    }

    /**
     * Reopens the database in read-write mode.
     * If the database is already read-write, does nothing.
     *
     * @throws SQLiteException if the database could not be reopened as requested, in which
     * case it remains open in read only mode.
     * @throws IllegalStateException if the database is not open.
     *
     * @see #isReadOnly()
     * @hide
     */
    public void reopenReadWrite() {
        synchronized (mLock) {
            throwIfNotOpenLocked();

            if (!isReadOnlyLocked()) {
                return; // nothing to do
            }

            // Reopen the database in read-write mode.
            final int oldOpenFlags = mConfigurationLocked.openFlags;
            mConfigurationLocked.openFlags = (mConfigurationLocked.openFlags & ~OPEN_READ_MASK)
                    | OPEN_READWRITE;
            try {
                mConnectionPoolLocked.reconfigure(mConfigurationLocked);
            } catch (RuntimeException ex) {
                mConfigurationLocked.openFlags = oldOpenFlags;
                throw ex;
            }
        }
    }

    private void open() {
        try {
            try {
                openInner();
            } catch (SQLiteDatabaseCorruptException ex) {
                onCorruption();
                openInner();
            }
        } catch (SQLiteException ex) {
            Log.e(TAG, "Failed to open database '" + getLabel() + "'.", ex);
            close();
            throw ex;
        }
    }

    private void openInner() {
        synchronized (mLock) {
            assert mConnectionPoolLocked == null;
            mConnectionPoolLocked = SQLiteConnectionPool.open(mConfigurationLocked);
            mCloseGuardLocked.open("close");
        }

        synchronized (sActiveDatabases) {
            sActiveDatabases.put(this, null);
        }
=======
        return openDatabase(path, factory, CREATE_IF_NECESSARY);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Create a memory backed SQLite database.  Its contents will be destroyed
     * when the database is closed.
     *
     * <p>Sets the locale of the database to the  the system's current locale.
     * Call {@link #setLocale} if you would like something else.</p>
     *
     * @param factory an optional factory class that is called to instantiate a
     *            cursor when query is called
     * @return a SQLiteDatabase object, or null if the database can't be created
     */
    public static SQLiteDatabase create(CursorFactory factory) {
        // This is a magic string with special meaning for SQLite.
<<<<<<< HEAD
        return openDatabase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH,
                factory, CREATE_IF_NECESSARY);
    }

    /**
     * Registers a CustomFunction callback as a function that can be called from
     * SQLite database triggers.
     *
     * @param name the name of the sqlite3 function
     * @param numArgs the number of arguments for the function
     * @param function callback to call when the function is executed
     * @hide
     */
    public void addCustomFunction(String name, int numArgs, CustomFunction function) {
        // Create wrapper (also validates arguments).
        SQLiteCustomFunction wrapper = new SQLiteCustomFunction(name, numArgs, function);

        synchronized (mLock) {
            throwIfNotOpenLocked();

            mConfigurationLocked.customFunctions.add(wrapper);
            try {
                mConnectionPoolLocked.reconfigure(mConfigurationLocked);
            } catch (RuntimeException ex) {
                mConfigurationLocked.customFunctions.remove(wrapper);
                throw ex;
            }
        }
    }

=======
        return openDatabase(":memory:", factory, CREATE_IF_NECESSARY);
    }

    /**
     * Close the database.
     */
    public void close() {
        lock();
        try {
            closeClosable();
            releaseReference();
        } finally {
            unlock();
        }
    }

    private void closeClosable() {
        Iterator<Map.Entry<SQLiteClosable, Object>> iter = mPrograms.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<SQLiteClosable, Object> entry = iter.next();
            SQLiteClosable program = entry.getKey();
            if (program != null) {
                program.onAllReferencesReleasedFromContainer();
            }
        }        
    }
    
    /**
     * Native call to close the database.
     */
    private native void dbclose();

>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Gets the database version.
     *
     * @return the database version
     */
    public int getVersion() {
<<<<<<< HEAD
        return ((Long) DatabaseUtils.longForQuery(this, "PRAGMA user_version;", null)).intValue();
=======
        SQLiteStatement prog = null;
        lock();
        try {
            prog = new SQLiteStatement(this, "PRAGMA user_version;");
            long version = prog.simpleQueryForLong();
            return (int) version;
        } finally {
            if (prog != null) prog.close();
            unlock();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Sets the database version.
     *
     * @param version the new database version
     */
    public void setVersion(int version) {
        execSQL("PRAGMA user_version = " + version);
    }

    /**
     * Returns the maximum size the database may grow to.
     *
     * @return the new maximum database size
     */
    public long getMaximumSize() {
<<<<<<< HEAD
        long pageCount = DatabaseUtils.longForQuery(this, "PRAGMA max_page_count;", null);
        return pageCount * getPageSize();
=======
        SQLiteStatement prog = null;
        lock();
        try {
            prog = new SQLiteStatement(this,
                    "PRAGMA max_page_count;");
            long pageCount = prog.simpleQueryForLong();
            return pageCount * getPageSize();
        } finally {
            if (prog != null) prog.close();
            unlock();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Sets the maximum size the database will grow to. The maximum size cannot
     * be set below the current size.
     *
     * @param numBytes the maximum database size, in bytes
     * @return the new maximum database size
     */
    public long setMaximumSize(long numBytes) {
<<<<<<< HEAD
        long pageSize = getPageSize();
        long numPages = numBytes / pageSize;
        // If numBytes isn't a multiple of pageSize, bump up a page
        if ((numBytes % pageSize) != 0) {
            numPages++;
        }
        long newPageCount = DatabaseUtils.longForQuery(this, "PRAGMA max_page_count = " + numPages,
                null);
        return newPageCount * pageSize;
    }

    /**
     * Returns the current database page size, in bytes.
     *
     * @return the database page size, in bytes
     */
    public long getPageSize() {
        return DatabaseUtils.longForQuery(this, "PRAGMA page_size;", null);
=======
        SQLiteStatement prog = null;
        lock();
        try {
            long pageSize = getPageSize();
            long numPages = numBytes / pageSize;
            // If numBytes isn't a multiple of pageSize, bump up a page
            if ((numBytes % pageSize) != 0) {
                numPages++;
            }
            prog = new SQLiteStatement(this,
                    "PRAGMA max_page_count = " + numPages);
            long newPageCount = prog.simpleQueryForLong();
            return newPageCount * pageSize;
        } finally {
            if (prog != null) prog.close();
            unlock();
        }
    }

    /**
     * Returns the maximum size the database may grow to.
     *
     * @return the new maximum database size
     */
    public long getPageSize() {
        SQLiteStatement prog = null;
        lock();
        try {
            prog = new SQLiteStatement(this,
                    "PRAGMA page_size;");
            long size = prog.simpleQueryForLong();
            return size;
        } finally {
            if (prog != null) prog.close();
            unlock();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Sets the database page size. The page size must be a power of two. This
     * method does not work if any data has been written to the database file,
     * and must be called right after the database has been created.
     *
     * @param numBytes the database page size, in bytes
     */
    public void setPageSize(long numBytes) {
        execSQL("PRAGMA page_size = " + numBytes);
    }

    /**
     * Mark this table as syncable. When an update occurs in this table the
     * _sync_dirty field will be set to ensure proper syncing operation.
     *
     * @param table the table to mark as syncable
     * @param deletedTable The deleted table that corresponds to the
     *          syncable table
<<<<<<< HEAD
     * @deprecated This method no longer serves any useful purpose and has been deprecated.
     */
    @Deprecated
    public void markTableSyncable(String table, String deletedTable) {
=======
     */
    public void markTableSyncable(String table, String deletedTable) {
        markTableSyncable(table, "_id", table, deletedTable);
    }

    /**
     * Mark this table as syncable, with the _sync_dirty residing in another
     * table. When an update occurs in this table the _sync_dirty field of the
     * row in updateTable with the _id in foreignKey will be set to
     * ensure proper syncing operation.
     *
     * @param table an update on this table will trigger a sync time removal
     * @param foreignKey this is the column in table whose value is an _id in
     *          updateTable
     * @param updateTable this is the table that will have its _sync_dirty
     */
    public void markTableSyncable(String table, String foreignKey,
            String updateTable) {
        markTableSyncable(table, foreignKey, updateTable, null);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Mark this table as syncable, with the _sync_dirty residing in another
     * table. When an update occurs in this table the _sync_dirty field of the
     * row in updateTable with the _id in foreignKey will be set to
     * ensure proper syncing operation.
     *
     * @param table an update on this table will trigger a sync time removal
     * @param foreignKey this is the column in table whose value is an _id in
     *          updateTable
     * @param updateTable this is the table that will have its _sync_dirty
<<<<<<< HEAD
     * @deprecated This method no longer serves any useful purpose and has been deprecated.
     */
    @Deprecated
    public void markTableSyncable(String table, String foreignKey, String updateTable) {
=======
     * @param deletedTable The deleted table that corresponds to the
     *          updateTable
     */
    private void markTableSyncable(String table, String foreignKey,
            String updateTable, String deletedTable) {
        lock();
        try {
            native_execSQL("SELECT _sync_dirty FROM " + updateTable
                    + " LIMIT 0");
            native_execSQL("SELECT " + foreignKey + " FROM " + table
                    + " LIMIT 0");
        } finally {
            unlock();
        }

        SyncUpdateInfo info = new SyncUpdateInfo(updateTable, deletedTable,
                foreignKey);
        synchronized (mSyncUpdateInfo) {
            mSyncUpdateInfo.put(table, info);
        }
    }

    /**
     * Call for each row that is updated in a cursor.
     *
     * @param table the table the row is in
     * @param rowId the row ID of the updated row
     */
    /* package */ void rowUpdated(String table, long rowId) {
        SyncUpdateInfo info;
        synchronized (mSyncUpdateInfo) {
            info = mSyncUpdateInfo.get(table);
        }
        if (info != null) {
            execSQL("UPDATE " + info.masterTable
                    + " SET _sync_dirty=1 WHERE _id=(SELECT " + info.foreignKey
                    + " FROM " + table + " WHERE _id=" + rowId + ")");
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Finds the name of the first table, which is editable.
     *
     * @param tables a list of tables
     * @return the first table listed
     */
    public static String findEditTable(String tables) {
        if (!TextUtils.isEmpty(tables)) {
            // find the first word terminated by either a space or a comma
            int spacepos = tables.indexOf(' ');
            int commapos = tables.indexOf(',');

            if (spacepos > 0 && (spacepos < commapos || commapos < 0)) {
                return tables.substring(0, spacepos);
            } else if (commapos > 0 && (commapos < spacepos || spacepos < 0) ) {
                return tables.substring(0, commapos);
            }
            return tables;
        } else {
            throw new IllegalStateException("Invalid tables");
        }
    }

    /**
     * Compiles an SQL statement into a reusable pre-compiled statement object.
     * The parameters are identical to {@link #execSQL(String)}. You may put ?s in the
     * statement and fill in those values with {@link SQLiteProgram#bindString}
     * and {@link SQLiteProgram#bindLong} each time you want to run the
     * statement. Statements may not return result sets larger than 1x1.
<<<<<<< HEAD
     *<p>
     * No two threads should be using the same {@link SQLiteStatement} at the same time.
     *
     * @param sql The raw SQL statement, may contain ? for unknown values to be
     *            bound later.
     * @return A pre-compiled {@link SQLiteStatement} object. Note that
     * {@link SQLiteStatement}s are not synchronized, see the documentation for more details.
     */
    public SQLiteStatement compileStatement(String sql) throws SQLException {
        acquireReference();
        try {
            return new SQLiteStatement(this, sql, null);
        } finally {
            releaseReference();
=======
     *
     * @param sql The raw SQL statement, may contain ? for unknown values to be
     *            bound later.
     * @return a pre-compiled statement object.
     */
    public SQLiteStatement compileStatement(String sql) throws SQLException {
        lock();
        try {
            return new SQLiteStatement(this, sql);
        } finally {
            unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Query the given URL, returning a {@link Cursor} over the result set.
     *
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
<<<<<<< HEAD
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     * @see Cursor
     */
    public Cursor query(boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit, null);
    }

    /**
     * Query the given URL, returning a {@link Cursor} over the result set.
     *
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @param cancellationSignal A signal to cancel the operation in progress, or null if none.
     * If the operation is canceled, then {@link OperationCanceledException} will be thrown
     * when the query is executed.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     * @see Cursor
     */
    public Cursor query(boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit, cancellationSignal);
    }

    /**
     * Query the given URL, returning a {@link Cursor} over the result set.
     *
     * @param cursorFactory the cursor factory to use, or null for the default factory
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     * @see Cursor
     */
    public Cursor queryWithFactory(CursorFactory cursorFactory,
            boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit) {
        return queryWithFactory(cursorFactory, distinct, table, columns, selection,
                selectionArgs, groupBy, having, orderBy, limit, null);
=======
     * @return A Cursor object, which is positioned before the first entry
     * @see Cursor
     */
    public Cursor query(boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs,
                groupBy, having, orderBy, limit);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Query the given URL, returning a {@link Cursor} over the result set.
     *
     * @param cursorFactory the cursor factory to use, or null for the default factory
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
<<<<<<< HEAD
     * @param cancellationSignal A signal to cancel the operation in progress, or null if none.
     * If the operation is canceled, then {@link OperationCanceledException} will be thrown
     * when the query is executed.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
=======
     * @return A Cursor object, which is positioned before the first entry
>>>>>>> 54b6cfa... Initial Contribution
     * @see Cursor
     */
    public Cursor queryWithFactory(CursorFactory cursorFactory,
            boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
<<<<<<< HEAD
            String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        acquireReference();
        try {
            String sql = SQLiteQueryBuilder.buildQueryString(
                    distinct, table, columns, selection, groupBy, having, orderBy, limit);

            return rawQueryWithFactory(cursorFactory, sql, selectionArgs,
                    findEditTable(table), cancellationSignal);
        } finally {
            releaseReference();
        }
=======
            String having, String orderBy, String limit) {
        String sql = SQLiteQueryBuilder.buildQueryString(
                distinct, table, columns, selection, groupBy, having, orderBy, limit);

        return rawQueryWithFactory(
                cursorFactory, sql, selectionArgs, findEditTable(table));
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Query the given table, returning a {@link Cursor} over the result set.
     *
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
<<<<<<< HEAD
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
=======
     * @return A {@link Cursor} object, which is positioned before the first entry
>>>>>>> 54b6cfa... Initial Contribution
     * @see Cursor
     */
    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy) {

        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, null /* limit */);
    }

    /**
     * Query the given table, returning a {@link Cursor} over the result set.
     *
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
<<<<<<< HEAD
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
=======
     * @return A {@link Cursor} object, which is positioned before the first entry
>>>>>>> 54b6cfa... Initial Contribution
     * @see Cursor
     */
    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy, String limit) {

        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, limit);
    }

    /**
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     *
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query,
     *     which will be replaced by the values from selectionArgs. The
     *     values will be bound as Strings.
<<<<<<< HEAD
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return rawQueryWithFactory(null, sql, selectionArgs, null, null);
    }

    /**
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     *
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query,
     *     which will be replaced by the values from selectionArgs. The
     *     values will be bound as Strings.
     * @param cancellationSignal A signal to cancel the operation in progress, or null if none.
     * If the operation is canceled, then {@link OperationCanceledException} will be thrown
     * when the query is executed.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    public Cursor rawQuery(String sql, String[] selectionArgs,
            CancellationSignal cancellationSignal) {
        return rawQueryWithFactory(null, sql, selectionArgs, null, cancellationSignal);
=======
     * @return A {@link Cursor} object, which is positioned before the first entry
     */
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return rawQueryWithFactory(null, sql, selectionArgs, null);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Runs the provided SQL and returns a cursor over the result set.
     *
     * @param cursorFactory the cursor factory to use, or null for the default factory
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query,
     *     which will be replaced by the values from selectionArgs. The
     *     values will be bound as Strings.
     * @param editTable the name of the first table, which is editable
<<<<<<< HEAD
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
=======
     * @return A {@link Cursor} object, which is positioned before the first entry
>>>>>>> 54b6cfa... Initial Contribution
     */
    public Cursor rawQueryWithFactory(
            CursorFactory cursorFactory, String sql, String[] selectionArgs,
            String editTable) {
<<<<<<< HEAD
        return rawQueryWithFactory(cursorFactory, sql, selectionArgs, editTable, null);
    }

    /**
     * Runs the provided SQL and returns a cursor over the result set.
     *
     * @param cursorFactory the cursor factory to use, or null for the default factory
     * @param sql the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query,
     *     which will be replaced by the values from selectionArgs. The
     *     values will be bound as Strings.
     * @param editTable the name of the first table, which is editable
     * @param cancellationSignal A signal to cancel the operation in progress, or null if none.
     * If the operation is canceled, then {@link OperationCanceledException} will be thrown
     * when the query is executed.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    public Cursor rawQueryWithFactory(
            CursorFactory cursorFactory, String sql, String[] selectionArgs,
            String editTable, CancellationSignal cancellationSignal) {
        acquireReference();
        try {
            SQLiteCursorDriver driver = new SQLiteDirectCursorDriver(this, sql, editTable,
                    cancellationSignal);
            return driver.query(cursorFactory != null ? cursorFactory : mCursorFactory,
                    selectionArgs);
        } finally {
            releaseReference();
=======
        long timeStart = 0;

        if (Config.LOGV) {
            timeStart = System.currentTimeMillis();
        }

        SQLiteCursorDriver driver = new SQLiteDirectCursorDriver(this, sql, editTable);

        try {
            return driver.query(
                    cursorFactory != null ? cursorFactory : mFactory,
                    selectionArgs);
        } finally {
            if (Config.LOGV) {
                long duration = System.currentTimeMillis() - timeStart;

                Log.v(SQLiteCursor.TAG,
                      "query (" + duration + " ms): " + driver.toString() + ", args are "
                              + (selectionArgs != null
                              ? TextUtils.join(",", selectionArgs)
                              : "<null>"));
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Convenience method for inserting a row into the database.
     *
     * @param table the table to insert the row into
<<<<<<< HEAD
     * @param nullColumnHack optional; may be <code>null</code>.
     *            SQL doesn't allow inserting a completely empty row without
     *            naming at least one column name.  If your provided <code>values</code> is
     *            empty, no column names are known and an empty row can't be inserted.
     *            If not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert a NULL into
     *            in the case where your <code>values</code> is empty.
=======
     * @param nullColumnHack SQL doesn't allow inserting a completely empty row,
     *            so if initialValues is empty this column will explicitly be
     *            assigned a NULL value
>>>>>>> 54b6cfa... Initial Contribution
     * @param values this map contains the initial column values for the
     *            row. The keys should be the column names and the values the
     *            column values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(String table, String nullColumnHack, ContentValues values) {
        try {
<<<<<<< HEAD
            return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
=======
            return insertOrReplace(table, nullColumnHack, values, false);
>>>>>>> 54b6cfa... Initial Contribution
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
            return -1;
        }
    }

    /**
     * Convenience method for inserting a row into the database.
     *
     * @param table the table to insert the row into
<<<<<<< HEAD
     * @param nullColumnHack optional; may be <code>null</code>.
     *            SQL doesn't allow inserting a completely empty row without
     *            naming at least one column name.  If your provided <code>values</code> is
     *            empty, no column names are known and an empty row can't be inserted.
     *            If not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert a NULL into
     *            in the case where your <code>values</code> is empty.
=======
     * @param nullColumnHack SQL doesn't allow inserting a completely empty row,
     *            so if initialValues is empty this column will explicitly be
     *            assigned a NULL value
>>>>>>> 54b6cfa... Initial Contribution
     * @param values this map contains the initial column values for the
     *            row. The keys should be the column names and the values the
     *            column values
     * @throws SQLException
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertOrThrow(String table, String nullColumnHack, ContentValues values)
            throws SQLException {
<<<<<<< HEAD
        return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
=======
        return insertOrReplace(table, nullColumnHack, values, false) ;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Convenience method for replacing a row in the database.
     *
     * @param table the table in which to replace the row
<<<<<<< HEAD
     * @param nullColumnHack optional; may be <code>null</code>.
     *            SQL doesn't allow inserting a completely empty row without
     *            naming at least one column name.  If your provided <code>initialValues</code> is
     *            empty, no column names are known and an empty row can't be inserted.
     *            If not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert a NULL into
     *            in the case where your <code>initialValues</code> is empty.
     * @param initialValues this map contains the initial column values for
     *   the row.
=======
     * @param nullColumnHack SQL doesn't allow inserting a completely empty row,
     *            so if initialValues is empty this row will explicitly be
     *            assigned a NULL value
     * @param initialValues this map contains the initial column values for
     *   the row. The key
>>>>>>> 54b6cfa... Initial Contribution
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        try {
<<<<<<< HEAD
            return insertWithOnConflict(table, nullColumnHack, initialValues,
                    CONFLICT_REPLACE);
=======
            return insertOrReplace(table, nullColumnHack, initialValues, true);
>>>>>>> 54b6cfa... Initial Contribution
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + initialValues, e);
            return -1;
        }
    }

    /**
     * Convenience method for replacing a row in the database.
     *
     * @param table the table in which to replace the row
<<<<<<< HEAD
     * @param nullColumnHack optional; may be <code>null</code>.
     *            SQL doesn't allow inserting a completely empty row without
     *            naming at least one column name.  If your provided <code>initialValues</code> is
     *            empty, no column names are known and an empty row can't be inserted.
     *            If not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert a NULL into
     *            in the case where your <code>initialValues</code> is empty.
=======
     * @param nullColumnHack SQL doesn't allow inserting a completely empty row,
     *            so if initialValues is empty this row will explicitly be
     *            assigned a NULL value
>>>>>>> 54b6cfa... Initial Contribution
     * @param initialValues this map contains the initial column values for
     *   the row. The key
     * @throws SQLException
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long replaceOrThrow(String table, String nullColumnHack,
            ContentValues initialValues) throws SQLException {
<<<<<<< HEAD
        return insertWithOnConflict(table, nullColumnHack, initialValues,
                CONFLICT_REPLACE);
    }

    /**
     * General method for inserting a row into the database.
     *
     * @param table the table to insert the row into
     * @param nullColumnHack optional; may be <code>null</code>.
     *            SQL doesn't allow inserting a completely empty row without
     *            naming at least one column name.  If your provided <code>initialValues</code> is
     *            empty, no column names are known and an empty row can't be inserted.
     *            If not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert a NULL into
     *            in the case where your <code>initialValues</code> is empty.
     * @param initialValues this map contains the initial column values for the
     *            row. The keys should be the column names and the values the
     *            column values
     * @param conflictAlgorithm for insert conflict resolver
     * @return the row ID of the newly inserted row
     * OR the primary key of the existing row if the input param 'conflictAlgorithm' =
     * {@link #CONFLICT_IGNORE}
     * OR -1 if any error
     */
    public long insertWithOnConflict(String table, String nullColumnHack,
            ContentValues initialValues, int conflictAlgorithm) {
        acquireReference();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(" INTO ");
            sql.append(table);
            sql.append('(');

            Object[] bindArgs = null;
            int size = (initialValues != null && initialValues.size() > 0)
                    ? initialValues.size() : 0;
            if (size > 0) {
                bindArgs = new Object[size];
                int i = 0;
                for (String colName : initialValues.keySet()) {
                    sql.append((i > 0) ? "," : "");
                    sql.append(colName);
                    bindArgs[i++] = initialValues.get(colName);
                }
                sql.append(')');
                sql.append(" VALUES (");
                for (i = 0; i < size; i++) {
                    sql.append((i > 0) ? ",?" : "?");
                }
            } else {
                sql.append(nullColumnHack + ") VALUES (NULL");
            }
            sql.append(')');

            SQLiteStatement statement = new SQLiteStatement(this, sql.toString(), bindArgs);
            try {
                return statement.executeInsert();
            } finally {
                statement.close();
            }
        } finally {
            releaseReference();
=======
        return insertOrReplace(table, nullColumnHack, initialValues, true);
    }

    private long insertOrReplace(String table, String nullColumnHack,
            ContentValues initialValues, boolean allowReplace) {
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }

        // Measurements show most sql lengths <= 152
        StringBuilder sql = new StringBuilder(152);
        sql.append("INSERT ");
        if (allowReplace) {
            sql.append("OR REPLACE ");
        }
        sql.append("INTO ");
        sql.append(table);
        // Measurements show most values lengths < 40
        StringBuilder values = new StringBuilder(40);

        Set<Map.Entry<String, Object>> entrySet = null;
        if (initialValues != null && initialValues.size() > 0) {
            entrySet = initialValues.valueSet();
            Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
            sql.append('(');

            boolean needSeparator = false;
            while (entriesIter.hasNext()) {
                if (needSeparator) {
                    sql.append(", ");
                    values.append(", ");
                }
                needSeparator = true;
                Map.Entry<String, Object> entry = entriesIter.next();
                sql.append(entry.getKey());
                values.append('?');
            }

            sql.append(')');
        } else {
            sql.append("(" + nullColumnHack + ") ");
            values.append("NULL");
        }

        sql.append(" VALUES(");
        sql.append(values);
        sql.append(");");

        lock();
        SQLiteStatement statement = null;
        try {
            statement = compileStatement(sql.toString());

            // Bind the values
            if (entrySet != null) {
                int size = entrySet.size();
                Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
                for (int i = 0; i < size; i++) {
                    Map.Entry<String, Object> entry = entriesIter.next();
                    DatabaseUtils.bindObjectToProgram(statement, i + 1, entry.getValue());
                }
            }

            // Run the program and then cleanup
            statement.execute();

            long insertedRowId = lastInsertRow();
            if (insertedRowId == -1) {
                Log.e(TAG, "Error inserting " + initialValues + " using " + sql);
            } else {
                if (Config.LOGD && Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "Inserting row " + insertedRowId + " from "
                            + initialValues + " using " + sql);
                }
            }
            return insertedRowId;
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
            unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Convenience method for deleting rows in the database.
     *
     * @param table the table to delete from
     * @param whereClause the optional WHERE clause to apply when deleting.
     *            Passing null will delete all rows.
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
<<<<<<< HEAD
        acquireReference();
        try {
            SQLiteStatement statement =  new SQLiteStatement(this, "DELETE FROM " + table +
                    (!TextUtils.isEmpty(whereClause) ? " WHERE " + whereClause : ""), whereArgs);
            try {
                return statement.executeUpdateDelete();
            } finally {
                statement.close();
            }
        } finally {
            releaseReference();
=======
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }
        lock();
        SQLiteStatement statement = null;
        try {
            statement = compileStatement("DELETE FROM " + table
                    + (!TextUtils.isEmpty(whereClause)
                    ? " WHERE " + whereClause : ""));
            if (whereArgs != null) {
                int numArgs = whereArgs.length;
                for (int i = 0; i < numArgs; i++) {
                    DatabaseUtils.bindObjectToProgram(statement, i + 1, whereArgs[i]);
                }
            }
            statement.execute();
            statement.close();
            return lastChangeCount();
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
            unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Convenience method for updating rows in the database.
     *
     * @param table the table to update in
     * @param values a map from column names to new column values. null is a
     *            valid value that will be translated to NULL.
     * @param whereClause the optional WHERE clause to apply when updating.
     *            Passing null will update all rows.
     * @return the number of rows affected
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
<<<<<<< HEAD
        return updateWithOnConflict(table, values, whereClause, whereArgs, CONFLICT_NONE);
    }

    /**
     * Convenience method for updating rows in the database.
     *
     * @param table the table to update in
     * @param values a map from column names to new column values. null is a
     *            valid value that will be translated to NULL.
     * @param whereClause the optional WHERE clause to apply when updating.
     *            Passing null will update all rows.
     * @param conflictAlgorithm for update conflict resolver
     * @return the number of rows affected
     */
    public int updateWithOnConflict(String table, ContentValues values,
            String whereClause, String[] whereArgs, int conflictAlgorithm) {
=======
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }

>>>>>>> 54b6cfa... Initial Contribution
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }

<<<<<<< HEAD
        acquireReference();
        try {
            StringBuilder sql = new StringBuilder(120);
            sql.append("UPDATE ");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(table);
            sql.append(" SET ");

            // move all bind args to one array
            int setValuesSize = values.size();
            int bindArgsSize = (whereArgs == null) ? setValuesSize : (setValuesSize + whereArgs.length);
            Object[] bindArgs = new Object[bindArgsSize];
            int i = 0;
            for (String colName : values.keySet()) {
                sql.append((i > 0) ? "," : "");
                sql.append(colName);
                bindArgs[i++] = values.get(colName);
                sql.append("=?");
            }
            if (whereArgs != null) {
                for (i = setValuesSize; i < bindArgsSize; i++) {
                    bindArgs[i] = whereArgs[i - setValuesSize];
                }
            }
            if (!TextUtils.isEmpty(whereClause)) {
                sql.append(" WHERE ");
                sql.append(whereClause);
            }

            SQLiteStatement statement = new SQLiteStatement(this, sql.toString(), bindArgs);
            try {
                return statement.executeUpdateDelete();
            } finally {
                statement.close();
            }
        } finally {
            releaseReference();
=======
        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE ");
        sql.append(table);
        sql.append(" SET ");

        Set<Map.Entry<String, Object>> entrySet = values.valueSet();
        Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();

        while (entriesIter.hasNext()) {
            Map.Entry<String, Object> entry = entriesIter.next();
            sql.append(entry.getKey());
            sql.append("=?");
            if (entriesIter.hasNext()) {
                sql.append(", ");
            }
        }

        if (!TextUtils.isEmpty(whereClause)) {
            sql.append(" WHERE ");
            sql.append(whereClause);
        }

        lock();
        SQLiteStatement statement = null;
        try {
            statement = compileStatement(sql.toString());

            // Bind the values
            int size = entrySet.size();
            entriesIter = entrySet.iterator();
            int bindArg = 1;
            for (int i = 0; i < size; i++) {
                Map.Entry<String, Object> entry = entriesIter.next();
                DatabaseUtils.bindObjectToProgram(statement, bindArg, entry.getValue());
                bindArg++;
            }

            if (whereArgs != null) {
                size = whereArgs.length;
                for (int i = 0; i < size; i++) {
                    statement.bindString(bindArg, whereArgs[i]);
                    bindArg++;
                }
            }

            // Run the program and then cleanup
            statement.execute();
            statement.close();
            int numChangedRows = lastChangeCount();
            if (Config.LOGD && Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Updated " + numChangedRows + " using " + values + " and " + sql);
            }
            return numChangedRows;
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } catch (SQLException e) {
            Log.e(TAG, "Error updating " + values + " using " + sql);
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
            unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Execute a single SQL statement that is NOT a SELECT
     * or any other SQL statement that returns data.
     * <p>
     * It has no means to return any data (such as the number of affected rows).
     * Instead, you're encouraged to use {@link #insert(String, String, ContentValues)},
     * {@link #update(String, ContentValues, String, String[])}, et al, when possible.
     * </p>
     * <p>
     * When using {@link #enableWriteAheadLogging()}, journal_mode is
     * automatically managed by this class. So, do not set journal_mode
     * using "PRAGMA journal_mode'<value>" statement if your app is using
     * {@link #enableWriteAheadLogging()}
     * </p>
     *
     * @param sql the SQL statement to be executed. Multiple statements separated by semicolons are
     * not supported.
     * @throws SQLException if the SQL string is invalid
     */
    public void execSQL(String sql) throws SQLException {
        executeSql(sql, null);
    }

    /**
     * Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE.
     * <p>
     * For INSERT statements, use any of the following instead.
     * <ul>
     *   <li>{@link #insert(String, String, ContentValues)}</li>
     *   <li>{@link #insertOrThrow(String, String, ContentValues)}</li>
     *   <li>{@link #insertWithOnConflict(String, String, ContentValues, int)}</li>
     * </ul>
     * <p>
     * For UPDATE statements, use any of the following instead.
     * <ul>
     *   <li>{@link #update(String, ContentValues, String, String[])}</li>
     *   <li>{@link #updateWithOnConflict(String, ContentValues, String, String[], int)}</li>
     * </ul>
     * <p>
     * For DELETE statements, use any of the following instead.
     * <ul>
     *   <li>{@link #delete(String, String, String[])}</li>
     * </ul>
     * <p>
     * For example, the following are good candidates for using this method:
     * <ul>
     *   <li>ALTER TABLE</li>
     *   <li>CREATE or DROP table / trigger / view / index / virtual table</li>
     *   <li>REINDEX</li>
     *   <li>RELEASE</li>
     *   <li>SAVEPOINT</li>
     *   <li>PRAGMA that returns no data</li>
     * </ul>
     * </p>
     * <p>
     * When using {@link #enableWriteAheadLogging()}, journal_mode is
     * automatically managed by this class. So, do not set journal_mode
     * using "PRAGMA journal_mode'<value>" statement if your app is using
     * {@link #enableWriteAheadLogging()}
     * </p>
     *
     * @param sql the SQL statement to be executed. Multiple statements separated by semicolons are
     * not supported.
     * @param bindArgs only byte[], String, Long and Double are supported in bindArgs.
     * @throws SQLException if the SQL string is invalid
=======
     * Execute a single SQL statement that is not a query. For example, CREATE
     * TABLE, DELETE, INSERT, etc. Multiple statements separated by ;s are not
     * supported. it takes a write lock
     *
     * @throws SQLException If the SQL string is invalid for some reason
     */
    public void execSQL(String sql) throws SQLException {
        long timeStart = 0;
        if (Config.LOGV) {
            timeStart = System.currentTimeMillis();
        }
        lock();
        try {
            native_execSQL(sql);
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } finally {
            unlock();
        }
        if (Config.LOGV) {
            long timeEnd = System.currentTimeMillis();
            Log.v(TAG, "Executed (" + (timeEnd - timeStart) + " ms):" + sql);
        }
    }

    /**
     * Execute a single SQL statement that is not a query. For example, CREATE
     * TABLE, DELETE, INSERT, etc. Multiple statements separated by ;s are not
     * supported. it takes a write lock,
     *
     * @param sql
     * @param bindArgs only byte[], String, Long and Double are supported in bindArgs.
     * @throws SQLException If the SQL string is invalid for some reason
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        if (bindArgs == null) {
            throw new IllegalArgumentException("Empty bindArgs");
        }
<<<<<<< HEAD
        executeSql(sql, bindArgs);
    }

    private int executeSql(String sql, Object[] bindArgs) throws SQLException {
        acquireReference();
        try {
            if (DatabaseUtils.getSqlStatementType(sql) == DatabaseUtils.STATEMENT_ATTACH) {
                boolean disableWal = false;
                synchronized (mLock) {
                    if (!mHasAttachedDbsLocked) {
                        mHasAttachedDbsLocked = true;
                        disableWal = true;
                    }
                }
                if (disableWal) {
                    disableWriteAheadLogging();
                }
            }

            SQLiteStatement statement = new SQLiteStatement(this, sql, bindArgs);
            try {
                return statement.executeUpdateDelete();
            } finally {
                statement.close();
            }
        } finally {
            releaseReference();
        }
    }

    /**
     * Returns true if the database is opened as read only.
     *
     * @return True if database is opened as read only.
     */
    public boolean isReadOnly() {
        synchronized (mLock) {
            return isReadOnlyLocked();
        }
    }

    private boolean isReadOnlyLocked() {
        return (mConfigurationLocked.openFlags & OPEN_READ_MASK) == OPEN_READONLY;
    }

    /**
     * Returns true if the database is in-memory db.
     *
     * @return True if the database is in-memory.
     * @hide
     */
    public boolean isInMemoryDatabase() {
        synchronized (mLock) {
            return mConfigurationLocked.isInMemoryDb();
=======
        long timeStart = 0;
        if (Config.LOGV) {
            timeStart = System.currentTimeMillis();
        }
        lock();
        SQLiteStatement statement = null;
        try {
            statement = compileStatement(sql);
            if (bindArgs != null) {
                int numArgs = bindArgs.length;
                for (int i = 0; i < numArgs; i++) {
                    DatabaseUtils.bindObjectToProgram(statement, i + 1, bindArgs[i]);
                }
            }
            statement.execute();
        } catch (SQLiteDatabaseCorruptException e) {
            onCorruption();
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
            unlock();
        }
        if (Config.LOGV) {
            long timeEnd = System.currentTimeMillis();
            Log.v(TAG, "Executed (" + (timeEnd - timeStart) + " ms):" + sql);
        }
    }

    @Override
    protected void finalize() {
        if (isOpen()) {
            if (mPrograms.isEmpty()) {
                Log.e(TAG, "Leak found", mLeakedException);
            } else {
                IllegalStateException leakProgram = new IllegalStateException(
                        "mPrograms size " + mPrograms.size(), mLeakedException);
                Log.e(TAG, "Leak found", leakProgram);
            }
            closeClosable();
            onAllReferencesReleased();
        }
    }

    /**
     * Private constructor. See {@link createDatabase} and {@link openDatabase}.
     *
     * @param path The full path to the database
     * @param factory The factory to use when creating cursors, may be NULL.
     * @param flags 0 or {@link #NO_LOCALIZED_COLLATORS}.  If the database file already
     *              exists, mFlags will be updated appropriately.
     */
    private SQLiteDatabase(String path, CursorFactory factory, int flags) {
        if (path == null) {
            throw new IllegalArgumentException("path should not be null");
        }
        mFlags = flags;
        mPath = path;
        mLeakedException = new IllegalStateException(path +
            " SQLiteDatabase created and never closed");
        mFactory = factory;
        dbopen(mPath, mFlags);
        mPrograms = new WeakHashMap<SQLiteClosable,Object>();
        try {
            setLocale(Locale.getDefault());
        } catch (RuntimeException e) {
            Log.e(TAG, "Failed to setLocale() when constructing, closing the database", e);
            dbclose();
            throw e;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Returns true if the database is currently open.
     *
     * @return True if the database is currently open (has not been closed).
     */
    public boolean isOpen() {
        synchronized (mLock) {
            return mConnectionPoolLocked != null;
        }
    }

    /**
     * Returns true if the new version code is greater than the current database version.
     *
     * @param newVersion The new version code.
     * @return True if the new version code is greater than the current database version. 
     */
=======
     * return whether the DB is opened as read only.
     * @return true if DB is opened as read only
     */
    public boolean isReadOnly() {
        return (mFlags & OPEN_READ_MASK) == OPEN_READONLY;
    }

    /**
     * @return true if the DB is currently open (has not been closed)
     */
    public boolean isOpen() {
        return mNativeHandle != 0;
    }

>>>>>>> 54b6cfa... Initial Contribution
    public boolean needUpgrade(int newVersion) {
        return newVersion > getVersion();
    }

    /**
<<<<<<< HEAD
     * Gets the path to the database file.
     *
     * @return The path to the database file.
     */
    public final String getPath() {
        synchronized (mLock) {
            return mConfigurationLocked.path;
        }
=======
     * Getter for the path to the database file.
     *
     * @return the path to our database file.
     */
    public final String getPath() {
        return mPath;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Sets the locale for this database.  Does nothing if this database has
<<<<<<< HEAD
     * the {@link #NO_LOCALIZED_COLLATORS} flag set or was opened read only.
     *
     * @param locale The new locale.
     *
=======
     * the NO_LOCALIZED_COLLATORS flag set or was opened read only.
>>>>>>> 54b6cfa... Initial Contribution
     * @throws SQLException if the locale could not be set.  The most common reason
     * for this is that there is no collator available for the locale you requested.
     * In this case the database remains unchanged.
     */
    public void setLocale(Locale locale) {
<<<<<<< HEAD
        if (locale == null) {
            throw new IllegalArgumentException("locale must not be null.");
        }

        synchronized (mLock) {
            throwIfNotOpenLocked();

            final Locale oldLocale = mConfigurationLocked.locale;
            mConfigurationLocked.locale = locale;
            try {
                mConnectionPoolLocked.reconfigure(mConfigurationLocked);
            } catch (RuntimeException ex) {
                mConfigurationLocked.locale = oldLocale;
                throw ex;
            }
        }
    }

    /**
     * Sets the maximum size of the prepared-statement cache for this database.
     * (size of the cache = number of compiled-sql-statements stored in the cache).
     *<p>
     * Maximum cache size can ONLY be increased from its current size (default = 10).
     * If this method is called with smaller size than the current maximum value,
     * then IllegalStateException is thrown.
     *<p>
     * This method is thread-safe.
     *
     * @param cacheSize the size of the cache. can be (0 to {@link #MAX_SQL_CACHE_SIZE})
     * @throws IllegalStateException if input cacheSize > {@link #MAX_SQL_CACHE_SIZE}.
     */
    public void setMaxSqlCacheSize(int cacheSize) {
        if (cacheSize > MAX_SQL_CACHE_SIZE || cacheSize < 0) {
            throw new IllegalStateException(
                    "expected value between 0 and " + MAX_SQL_CACHE_SIZE);
        }

        synchronized (mLock) {
            throwIfNotOpenLocked();

            final int oldMaxSqlCacheSize = mConfigurationLocked.maxSqlCacheSize;
            mConfigurationLocked.maxSqlCacheSize = cacheSize;
            try {
                mConnectionPoolLocked.reconfigure(mConfigurationLocked);
            } catch (RuntimeException ex) {
                mConfigurationLocked.maxSqlCacheSize = oldMaxSqlCacheSize;
                throw ex;
            }
=======
        lock();
        try {
            native_setLocale(locale.toString(), mFlags);
        } finally {
            unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Sets whether foreign key constraints are enabled for the database.
     * <p>
     * By default, foreign key constraints are not enforced by the database.
     * This method allows an application to enable foreign key constraints.
     * It must be called each time the database is opened to ensure that foreign
     * key constraints are enabled for the session.
     * </p><p>
     * A good time to call this method is right after calling {@link #openOrCreateDatabase}
     * or in the {@link SQLiteOpenHelper#onConfigure} callback.
     * </p><p>
     * When foreign key constraints are disabled, the database does not check whether
     * changes to the database will violate foreign key constraints.  Likewise, when
     * foreign key constraints are disabled, the database will not execute cascade
     * delete or update triggers.  As a result, it is possible for the database
     * state to become inconsistent.  To perform a database integrity check,
     * call {@link #isDatabaseIntegrityOk}.
     * </p><p>
     * This method must not be called while a transaction is in progress.
     * </p><p>
     * See also <a href="http://sqlite.org/foreignkeys.html">SQLite Foreign Key Constraints</a>
     * for more details about foreign key constraint support.
     * </p>
     *
     * @param enable True to enable foreign key constraints, false to disable them.
     *
     * @throws IllegalStateException if the are transactions is in progress
     * when this method is called.
     */
    public void setForeignKeyConstraintsEnabled(boolean enable) {
        synchronized (mLock) {
            throwIfNotOpenLocked();

            if (mConfigurationLocked.foreignKeyConstraintsEnabled == enable) {
                return;
            }

            mConfigurationLocked.foreignKeyConstraintsEnabled = enable;
            try {
                mConnectionPoolLocked.reconfigure(mConfigurationLocked);
            } catch (RuntimeException ex) {
                mConfigurationLocked.foreignKeyConstraintsEnabled = !enable;
                throw ex;
            }
        }
    }

    /**
     * This method enables parallel execution of queries from multiple threads on the
     * same database.  It does this by opening multiple connections to the database
     * and using a different database connection for each query.  The database
     * journal mode is also changed to enable writes to proceed concurrently with reads.
     * <p>
     * When write-ahead logging is not enabled (the default), it is not possible for
     * reads and writes to occur on the database at the same time.  Before modifying the
     * database, the writer implicitly acquires an exclusive lock on the database which
     * prevents readers from accessing the database until the write is completed.
     * </p><p>
     * In contrast, when write-ahead logging is enabled (by calling this method), write
     * operations occur in a separate log file which allows reads to proceed concurrently.
     * While a write is in progress, readers on other threads will perceive the state
     * of the database as it was before the write began.  When the write completes, readers
     * on other threads will then perceive the new state of the database.
     * </p><p>
     * It is a good idea to enable write-ahead logging whenever a database will be
     * concurrently accessed and modified by multiple threads at the same time.
     * However, write-ahead logging uses significantly more memory than ordinary
     * journaling because there are multiple connections to the same database.
     * So if a database will only be used by a single thread, or if optimizing
     * concurrency is not very important, then write-ahead logging should be disabled.
     * </p><p>
     * After calling this method, execution of queries in parallel is enabled as long as
     * the database remains open.  To disable execution of queries in parallel, either
     * call {@link #disableWriteAheadLogging} or close the database and reopen it.
     * </p><p>
     * The maximum number of connections used to execute queries in parallel is
     * dependent upon the device memory and possibly other properties.
     * </p><p>
     * If a query is part of a transaction, then it is executed on the same database handle the
     * transaction was begun.
     * </p><p>
     * Writers should use {@link #beginTransactionNonExclusive()} or
     * {@link #beginTransactionWithListenerNonExclusive(SQLiteTransactionListener)}
     * to start a transaction.  Non-exclusive mode allows database file to be in readable
     * by other threads executing queries.
     * </p><p>
     * If the database has any attached databases, then execution of queries in parallel is NOT
     * possible.  Likewise, write-ahead logging is not supported for read-only databases
     * or memory databases.  In such cases, {@link #enableWriteAheadLogging} returns false.
     * </p><p>
     * The best way to enable write-ahead logging is to pass the
     * {@link #ENABLE_WRITE_AHEAD_LOGGING} flag to {@link #openDatabase}.  This is
     * more efficient than calling {@link #enableWriteAheadLogging}.
     * <code><pre>
     *     SQLiteDatabase db = SQLiteDatabase.openDatabase("db_filename", cursorFactory,
     *             SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.ENABLE_WRITE_AHEAD_LOGGING,
     *             myDatabaseErrorHandler);
     *     db.enableWriteAheadLogging();
     * </pre></code>
     * </p><p>
     * Another way to enable write-ahead logging is to call {@link #enableWriteAheadLogging}
     * after opening the database.
     * <code><pre>
     *     SQLiteDatabase db = SQLiteDatabase.openDatabase("db_filename", cursorFactory,
     *             SQLiteDatabase.CREATE_IF_NECESSARY, myDatabaseErrorHandler);
     *     db.enableWriteAheadLogging();
     * </pre></code>
     * </p><p>
     * See also <a href="http://sqlite.org/wal.html">SQLite Write-Ahead Logging</a> for
     * more details about how write-ahead logging works.
     * </p>
     *
     * @return True if write-ahead logging is enabled.
     *
     * @throws IllegalStateException if there are transactions in progress at the
     * time this method is called.  WAL mode can only be changed when there are no
     * transactions in progress.
     *
     * @see #ENABLE_WRITE_AHEAD_LOGGING
     * @see #disableWriteAheadLogging
     */
    public boolean enableWriteAheadLogging() {
        synchronized (mLock) {
            throwIfNotOpenLocked();

            if ((mConfigurationLocked.openFlags & ENABLE_WRITE_AHEAD_LOGGING) != 0) {
                return true;
            }

            if (isReadOnlyLocked()) {
                // WAL doesn't make sense for readonly-databases.
                // TODO: True, but connection pooling does still make sense...
                return false;
            }

            if (mConfigurationLocked.isInMemoryDb()) {
                Log.i(TAG, "can't enable WAL for memory databases.");
                return false;
            }

            // make sure this database has NO attached databases because sqlite's write-ahead-logging
            // doesn't work for databases with attached databases
            if (mHasAttachedDbsLocked) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "this database: " + mConfigurationLocked.label
                            + " has attached databases. can't  enable WAL.");
                }
                return false;
            }

            mConfigurationLocked.openFlags |= ENABLE_WRITE_AHEAD_LOGGING;
            try {
                mConnectionPoolLocked.reconfigure(mConfigurationLocked);
            } catch (RuntimeException ex) {
                mConfigurationLocked.openFlags &= ~ENABLE_WRITE_AHEAD_LOGGING;
                throw ex;
            }
        }
        return true;
    }

    /**
     * This method disables the features enabled by {@link #enableWriteAheadLogging()}.
     *
     * @throws IllegalStateException if there are transactions in progress at the
     * time this method is called.  WAL mode can only be changed when there are no
     * transactions in progress.
     *
     * @see #enableWriteAheadLogging
     */
    public void disableWriteAheadLogging() {
        synchronized (mLock) {
            throwIfNotOpenLocked();

            if ((mConfigurationLocked.openFlags & ENABLE_WRITE_AHEAD_LOGGING) == 0) {
                return;
            }

            mConfigurationLocked.openFlags &= ~ENABLE_WRITE_AHEAD_LOGGING;
            try {
                mConnectionPoolLocked.reconfigure(mConfigurationLocked);
            } catch (RuntimeException ex) {
                mConfigurationLocked.openFlags |= ENABLE_WRITE_AHEAD_LOGGING;
                throw ex;
            }
        }
    }

    /**
     * Returns true if write-ahead logging has been enabled for this database.
     *
     * @return True if write-ahead logging has been enabled for this database.
     *
     * @see #enableWriteAheadLogging
     * @see #ENABLE_WRITE_AHEAD_LOGGING
     */
    public boolean isWriteAheadLoggingEnabled() {
        synchronized (mLock) {
            throwIfNotOpenLocked();

            return (mConfigurationLocked.openFlags & ENABLE_WRITE_AHEAD_LOGGING) != 0;
        }
    }

    /**
     * Collect statistics about all open databases in the current process.
     * Used by bug report.
     */
    static ArrayList<DbStats> getDbStats() {
        ArrayList<DbStats> dbStatsList = new ArrayList<DbStats>();
        for (SQLiteDatabase db : getActiveDatabases()) {
            db.collectDbStats(dbStatsList);
        }
        return dbStatsList;
    }

    private void collectDbStats(ArrayList<DbStats> dbStatsList) {
        synchronized (mLock) {
            if (mConnectionPoolLocked != null) {
                mConnectionPoolLocked.collectDbStats(dbStatsList);
            }
        }
    }

    private static ArrayList<SQLiteDatabase> getActiveDatabases() {
        ArrayList<SQLiteDatabase> databases = new ArrayList<SQLiteDatabase>();
        synchronized (sActiveDatabases) {
            databases.addAll(sActiveDatabases.keySet());
        }
        return databases;
    }

    /**
     * Dump detailed information about all open databases in the current process.
     * Used by bug report.
     */
    static void dumpAll(Printer printer, boolean verbose) {
        for (SQLiteDatabase db : getActiveDatabases()) {
            db.dump(printer, verbose);
        }
    }

    private void dump(Printer printer, boolean verbose) {
        synchronized (mLock) {
            if (mConnectionPoolLocked != null) {
                printer.println("");
                mConnectionPoolLocked.dump(printer, verbose);
            }
        }
    }

    /**
     * Returns list of full pathnames of all attached databases including the main database
     * by executing 'pragma database_list' on the database.
     *
     * @return ArrayList of pairs of (database name, database file path) or null if the database
     * is not open.
     */
    public List<Pair<String, String>> getAttachedDbs() {
        ArrayList<Pair<String, String>> attachedDbs = new ArrayList<Pair<String, String>>();
        synchronized (mLock) {
            if (mConnectionPoolLocked == null) {
                return null; // not open
            }

            if (!mHasAttachedDbsLocked) {
                // No attached databases.
                // There is a small window where attached databases exist but this flag is not
                // set yet.  This can occur when this thread is in a race condition with another
                // thread that is executing the SQL statement: "attach database <blah> as <foo>"
                // If this thread is NOT ok with such a race condition (and thus possibly not
                // receivethe entire list of attached databases), then the caller should ensure
                // that no thread is executing any SQL statements while a thread is calling this
                // method.  Typically, this method is called when 'adb bugreport' is done or the
                // caller wants to collect stats on the database and all its attached databases.
                attachedDbs.add(new Pair<String, String>("main", mConfigurationLocked.path));
                return attachedDbs;
            }

            acquireReference();
        }

        try {
            // has attached databases. query sqlite to get the list of attached databases.
            Cursor c = null;
            try {
                c = rawQuery("pragma database_list;", null);
                while (c.moveToNext()) {
                    // sqlite returns a row for each database in the returned list of databases.
                    //   in each row,
                    //       1st column is the database name such as main, or the database
                    //                              name specified on the "ATTACH" command
                    //       2nd column is the database file path.
                    attachedDbs.add(new Pair<String, String>(c.getString(1), c.getString(2)));
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
            return attachedDbs;
        } finally {
            releaseReference();
        }
    }

    /**
     * Runs 'pragma integrity_check' on the given database (and all the attached databases)
     * and returns true if the given database (and all its attached databases) pass integrity_check,
     * false otherwise.
     *<p>
     * If the result is false, then this method logs the errors reported by the integrity_check
     * command execution.
     *<p>
     * Note that 'pragma integrity_check' on a database can take a long time.
     *
     * @return true if the given database (and all its attached databases) pass integrity_check,
     * false otherwise.
     */
    public boolean isDatabaseIntegrityOk() {
        acquireReference();
        try {
            List<Pair<String, String>> attachedDbs = null;
            try {
                attachedDbs = getAttachedDbs();
                if (attachedDbs == null) {
                    throw new IllegalStateException("databaselist for: " + getPath() + " couldn't " +
                            "be retrieved. probably because the database is closed");
                }
            } catch (SQLiteException e) {
                // can't get attachedDb list. do integrity check on the main database
                attachedDbs = new ArrayList<Pair<String, String>>();
                attachedDbs.add(new Pair<String, String>("main", getPath()));
            }

            for (int i = 0; i < attachedDbs.size(); i++) {
                Pair<String, String> p = attachedDbs.get(i);
                SQLiteStatement prog = null;
                try {
                    prog = compileStatement("PRAGMA " + p.first + ".integrity_check(1);");
                    String rslt = prog.simpleQueryForString();
                    if (!rslt.equalsIgnoreCase("ok")) {
                        // integrity_checker failed on main or attached databases
                        Log.e(TAG, "PRAGMA integrity_check on " + p.second + " returned: " + rslt);
                        return false;
                    }
                } finally {
                    if (prog != null) prog.close();
                }
            }
        } finally {
            releaseReference();
        }
        return true;
    }

    @Override
    public String toString() {
        return "SQLiteDatabase: " + getPath();
    }

    private void throwIfNotOpenLocked() {
        if (mConnectionPoolLocked == null) {
            throw new IllegalStateException("The database '" + mConfigurationLocked.label
                    + "' is not open.");
        }
    }

    /**
     * Used to allow returning sub-classes of {@link Cursor} when calling query.
     */
    public interface CursorFactory {
        /**
         * See {@link SQLiteCursor#SQLiteCursor(SQLiteCursorDriver, String, SQLiteQuery)}.
         */
        public Cursor newCursor(SQLiteDatabase db,
                SQLiteCursorDriver masterQuery, String editTable,
                SQLiteQuery query);
    }

    /**
     * A callback interface for a custom sqlite3 function.
     * This can be used to create a function that can be called from
     * sqlite3 database triggers.
     * @hide
     */
    public interface CustomFunction {
        public void callback(String[] args);
    }
=======
     * Native call to open the database.
     *
     * @param path The full path to the database
     */
    private native void dbopen(String path, int flags);

    /**
     * Native call to execute a raw SQL statement. {@link mLock} must be held
     * when calling this method.
     *
     * @param sql The raw SQL string
     * @throws SQLException
     */
    /* package */ native void native_execSQL(String sql) throws SQLException;

    /**
     * Native call to set the locale.  {@link mLock} must be held when calling
     * this method.
     * @throws SQLException
     */
    /* package */ native void native_setLocale(String loc, int flags);

    /**
     * Returns the row ID of the last row inserted into the database.
     *
     * @return the row ID of the last row inserted into the database.
     */
    /* package */ native long lastInsertRow();

    /**
     * Returns the number of changes made in the last statement executed.
     *
     * @return the number of changes made in the last statement executed.
     */
    /* package */ native int lastChangeCount();
>>>>>>> 54b6cfa... Initial Contribution
}
