/*
 * Copyright (C) 2007 The Android Open Source Project
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

import android.content.Context;
<<<<<<< HEAD
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
=======
>>>>>>> 54b6cfa... Initial Contribution
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * A helper class to manage database creation and version management.
<<<<<<< HEAD
 *
 * <p>You create a subclass implementing {@link #onCreate}, {@link #onUpgrade} and
=======
 * You create a subclass implementing {@link #onCreate}, {@link #onUpgrade} and
>>>>>>> 54b6cfa... Initial Contribution
 * optionally {@link #onOpen}, and this class takes care of opening the database
 * if it exists, creating it if it does not, and upgrading it as necessary.
 * Transactions are used to make sure the database is always in a sensible state.
 *
<<<<<<< HEAD
 * <p>This class makes it easy for {@link android.content.ContentProvider}
 * implementations to defer opening and upgrading the database until first use,
 * to avoid blocking application startup with long-running database upgrades.
 *
 * <p>For an example, see the NotePadProvider class in the NotePad sample application,
 * in the <em>samples/</em> directory of the SDK.</p>
 *
 * <p class="note"><strong>Note:</strong> this class assumes
 * monotonically increasing version numbers for upgrades.</p>
=======
 * @see com.google.provider.NotePad.NotePadProvider
>>>>>>> 54b6cfa... Initial Contribution
 */
public abstract class SQLiteOpenHelper {
    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();

<<<<<<< HEAD
    // When true, getReadableDatabase returns a read-only database if it is just being opened.
    // The database handle is reopened in read/write mode when getWritableDatabase is called.
    // We leave this behavior disabled in production because it is inefficient and breaks
    // many applications.  For debugging purposes it can be useful to turn on strict
    // read-only semantics to catch applications that call getReadableDatabase when they really
    // wanted getWritableDatabase.
    private static final boolean DEBUG_STRICT_READONLY = false;

=======
>>>>>>> 54b6cfa... Initial Contribution
    private final Context mContext;
    private final String mName;
    private final CursorFactory mFactory;
    private final int mNewVersion;

<<<<<<< HEAD
    private SQLiteDatabase mDatabase;
    private boolean mIsInitializing;
    private boolean mEnableWriteAheadLogging;
    private final DatabaseErrorHandler mErrorHandler;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *     newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }
=======
    private SQLiteDatabase mDatabase = null;
    private boolean mIsInitializing = false;
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Create a helper object to create, open, and/or manage a database.
     * The database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     *
<<<<<<< HEAD
     * <p>Accepts input param: a concrete instance of {@link DatabaseErrorHandler} to be
     * used to handle corruption when sqlite reports database corruption.</p>
     *
=======
>>>>>>> 54b6cfa... Initial Contribution
     * @param context to use to open or create the database
     * @param name of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
<<<<<<< HEAD
     *     {@link #onUpgrade} will be used to upgrade the database; if the database is
     *     newer, {@link #onDowngrade} will be used to downgrade the database
     * @param errorHandler the {@link DatabaseErrorHandler} to be used when sqlite reports database
     * corruption, or null to use the default error handler.
     */
    public SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version,
            DatabaseErrorHandler errorHandler) {
=======
     *     {@link #onUpgrade} will be used to upgrade the database
     */
    public SQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
>>>>>>> 54b6cfa... Initial Contribution
        if (version < 1) throw new IllegalArgumentException("Version must be >= 1, was " + version);

        mContext = context;
        mName = name;
        mFactory = factory;
        mNewVersion = version;
<<<<<<< HEAD
        mErrorHandler = errorHandler;
    }

    /**
     * Return the name of the SQLite database being opened, as given to
     * the constructor.
     */
    public String getDatabaseName() {
        return mName;
    }

    /**
     * Enables or disables the use of write-ahead logging for the database.
     *
     * Write-ahead logging cannot be used with read-only databases so the value of
     * this flag is ignored if the database is opened read-only.
     *
     * @param enabled True if write-ahead logging should be enabled, false if it
     * should be disabled.
     *
     * @see SQLiteDatabase#enableWriteAheadLogging()
     */
    public void setWriteAheadLoggingEnabled(boolean enabled) {
        synchronized (this) {
            if (mEnableWriteAheadLogging != enabled) {
                if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
                    if (enabled) {
                        mDatabase.enableWriteAheadLogging();
                    } else {
                        mDatabase.disableWriteAheadLogging();
                    }
                }
                mEnableWriteAheadLogging = enabled;
            }
        }
=======
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Create and/or open a database that will be used for reading and writing.
<<<<<<< HEAD
     * The first time this is called, the database will be opened and
     * {@link #onCreate}, {@link #onUpgrade} and/or {@link #onOpen} will be
     * called.
     *
     * <p>Once opened successfully, the database is cached, so you can
     * call this method every time you need to write to the database.
     * (Make sure to call {@link #close} when you no longer need the database.)
     * Errors such as bad permissions or a full disk may cause this method
     * to fail, but future attempts may succeed if the problem is fixed.</p>
     *
     * <p class="caution">Database upgrade may take a long time, you
     * should not call this method from the application main thread, including
     * from {@link android.content.ContentProvider#onCreate ContentProvider.onCreate()}.
     *
     * @throws SQLiteException if the database cannot be opened for writing
     * @return a read/write database object valid until {@link #close} is called
     */
    public SQLiteDatabase getWritableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(true);
        }
    }

    /**
     * Create and/or open a database.  This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk,
     * requires the database to be opened read-only.  In that case, a read-only
     * database object will be returned.  If the problem is fixed, a future call
     * to {@link #getWritableDatabase} may succeed, in which case the read-only
     * database object will be closed and the read/write object will be returned
     * in the future.
     *
     * <p class="caution">Like {@link #getWritableDatabase}, this method may
     * take a long time to return, so you should not call it from the
     * application main thread, including from
     * {@link android.content.ContentProvider#onCreate ContentProvider.onCreate()}.
     *
     * @throws SQLiteException if the database cannot be opened
     * @return a database object valid until {@link #getWritableDatabase}
     *     or {@link #close} is called.
     */
    public SQLiteDatabase getReadableDatabase() {
        synchronized (this) {
            return getDatabaseLocked(false);
        }
    }

    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        if (mDatabase != null) {
            if (!mDatabase.isOpen()) {
                // Darn!  The user closed the database by calling mDatabase.close().
                mDatabase = null;
            } else if (!writable || !mDatabase.isReadOnly()) {
                // The database is already open for business.
                return mDatabase;
            }
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }

        SQLiteDatabase db = mDatabase;
        try {
            mIsInitializing = true;

            if (db != null) {
                if (writable && db.isReadOnly()) {
                    db.reopenReadWrite();
                }
            } else if (mName == null) {
                db = SQLiteDatabase.create(null);
            } else {
                try {
                    if (DEBUG_STRICT_READONLY && !writable) {
                        final String path = mContext.getDatabasePath(mName).getPath();
                        db = SQLiteDatabase.openDatabase(path, mFactory,
                                SQLiteDatabase.OPEN_READONLY, mErrorHandler);
                    } else {
                        db = mContext.openOrCreateDatabase(mName, mEnableWriteAheadLogging ?
                                Context.MODE_ENABLE_WRITE_AHEAD_LOGGING : 0,
                                mFactory, mErrorHandler);
                    }
                } catch (SQLiteException ex) {
                    if (writable) {
                        throw ex;
                    }
                    Log.e(TAG, "Couldn't open " + mName
                            + " for writing (will try read-only):", ex);
                    final String path = mContext.getDatabasePath(mName).getPath();
                    db = SQLiteDatabase.openDatabase(path, mFactory,
                            SQLiteDatabase.OPEN_READONLY, mErrorHandler);
                }
            }

            onConfigure(db);

            final int version = db.getVersion();
            if (version != mNewVersion) {
                if (db.isReadOnly()) {
                    throw new SQLiteException("Can't upgrade read-only database from version " +
                            db.getVersion() + " to " + mNewVersion + ": " + mName);
                }

=======
     * Once opened successfully, the database is cached, so you can call this
     * method every time you need to write to the database.  Make sure to call
     * {@link #close} when you no longer need it.
     *
     * <p>Errors such as bad permissions or a full disk may cause this operation
     * to fail, but future attempts may succeed if the problem is fixed.</p>
     *
     * @throws SQLiteException if the database cannot be opened for writing
     * @return a read/write database object valid until {@link #close} is called
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
            return mDatabase;  // The database is already open for business
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getWritableDatabase called recursively");
        }

        // If we have a read-only database open, someone could be using it
        // (though they shouldn't), which would cause a lock to be held on
        // the file, and our attempts to open the database read-write would
        // fail waiting for the file lock.  To prevent that, we acquire the
        // lock on the read-only database, which shuts out other users.

        boolean success = false;
        SQLiteDatabase db = null;
        if (mDatabase != null) mDatabase.lock();
        try {
            mIsInitializing = true;
            if (mName == null) {
                db = SQLiteDatabase.create(null);
            } else {
                db = mContext.openOrCreateDatabase(mName, 0, mFactory);
            }

            int version = db.getVersion();
            if (version != mNewVersion) {
>>>>>>> 54b6cfa... Initial Contribution
                db.beginTransaction();
                try {
                    if (version == 0) {
                        onCreate(db);
                    } else {
<<<<<<< HEAD
                        if (version > mNewVersion) {
                            onDowngrade(db, version, mNewVersion);
                        } else {
                            onUpgrade(db, version, mNewVersion);
                        }
=======
                        onUpgrade(db, version, mNewVersion);
>>>>>>> 54b6cfa... Initial Contribution
                    }
                    db.setVersion(mNewVersion);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            onOpen(db);
<<<<<<< HEAD

            if (db.isReadOnly()) {
                Log.w(TAG, "Opened " + mName + " in read-only mode");
            }

            mDatabase = db;
            return db;
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase) {
                db.close();
            }
=======
            success = true;
            return db;
        } finally {
            mIsInitializing = false;
            if (success) {
                if (mDatabase != null) {
                    try { mDatabase.close(); } catch (Exception e) { }
                    mDatabase.unlock();
                }
                mDatabase = db;
            } else {
                if (mDatabase != null) mDatabase.unlock();
                if (db != null) db.close();
            }
        }
    }

    /**
     * Create and/or open a database.  This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk,
     * requires the database to be opened read-only.  In that case, a read-only
     * database object will be returned.  If the problem is fixed, a future call
     * to {@link #getWritableDatabase} may succeed, in which case the read-only
     * database object will be closed and the read/write object will be returned
     * in the future.
     *
     * @throws SQLiteException if the database cannot be opened
     * @return a database object valid until {@link #getWritableDatabase}
     *     or {@link #close} is called.
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (mDatabase != null && mDatabase.isOpen()) {
            return mDatabase;  // The database is already open for business
        }

        if (mIsInitializing) {
            throw new IllegalStateException("getReadableDatabase called recursively");
        }

        try {
            return getWritableDatabase();
        } catch (SQLiteException e) {
            if (mName == null) throw e;  // Can't open a temp database read-only!
            Log.e(TAG, "Couldn't open " + mName + " for writing (will try read-only):", e);
        }

        SQLiteDatabase db = null;
        try {
            mIsInitializing = true;
            String path = mContext.getDatabasePath(mName).getPath();
            db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READONLY);
            if (db.getVersion() != mNewVersion) {
                throw new SQLiteException("Can't upgrade read-only database from version " +
                        db.getVersion() + " to " + mNewVersion + ": " + path);
            }

            onOpen(db);
            Log.w(TAG, "Opened " + mName + " in read-only mode");
            mDatabase = db;
            return mDatabase;
        } finally {
            mIsInitializing = false;
            if (db != null && db != mDatabase) db.close();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Close any open database object.
     */
    public synchronized void close() {
        if (mIsInitializing) throw new IllegalStateException("Closed during initialization");

        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    /**
<<<<<<< HEAD
     * Called when the database connection is being configured, to enable features
     * such as write-ahead logging or foreign key support.
     * <p>
     * This method is called before {@link #onCreate}, {@link #onUpgrade},
     * {@link #onDowngrade}, or {@link #onOpen} are called.  It should not modify
     * the database except to configure the database connection as required.
     * </p><p>
     * This method should only call methods that configure the parameters of the
     * database connection, such as {@link SQLiteDatabase#enableWriteAheadLogging}
     * {@link SQLiteDatabase#setForeignKeyConstraintsEnabled},
     * {@link SQLiteDatabase#setLocale}, {@link SQLiteDatabase#setMaximumSize},
     * or executing PRAGMA statements.
     * </p>
     *
     * @param db The database.
     */
    public void onConfigure(SQLiteDatabase db) {}

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    public abstract void onCreate(SQLiteDatabase db);

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
<<<<<<< HEAD
     * <p>
     * The SQLite ALTER TABLE documentation can be found
=======
     * <p>The SQLite ALTER TABLE documentation can be found
>>>>>>> 54b6cfa... Initial Contribution
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
<<<<<<< HEAD
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
=======
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
<<<<<<< HEAD
     * Called when the database needs to be downgraded. This is strictly similar to
     * {@link #onUpgrade} method, but is called whenever current version is newer than requested one.
     * However, this method is not abstract, so it is not mandatory for a customer to
     * implement it. If not overridden, default implementation will reject downgrade and
     * throws SQLiteException
     *
     * <p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }

    /**
     * Called when the database has been opened.  The implementation
     * should check {@link SQLiteDatabase#isReadOnly} before updating the
     * database.
     * <p>
     * This method is called after the database connection has been configured
     * and after the database schema has been created, upgraded or downgraded as necessary.
     * If the database connection must be configured in some way before the schema
     * is created, upgraded, or downgraded, do it in {@link #onConfigure} instead.
     * </p>
=======
     * Called when the database has been opened.
     * Override method should check {@link SQLiteDatabase#isReadOnly} before
     * updating the database.
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @param db The database.
     */
    public void onOpen(SQLiteDatabase db) {}
}
