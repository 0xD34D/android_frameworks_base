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

import android.database.AbstractWindowedCursor;
import android.database.CursorWindow;
<<<<<<< HEAD
import android.database.DatabaseUtils;
import android.os.StrictMode;
import android.util.Log;

import java.util.HashMap;
=======
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
>>>>>>> 54b6cfa... Initial Contribution
import java.util.Map;

/**
 * A Cursor implementation that exposes results from a query on a
 * {@link SQLiteDatabase}.
<<<<<<< HEAD
 *
 * SQLiteCursor is not internally synchronized so code using a SQLiteCursor from multiple
 * threads should perform its own synchronization when using the SQLiteCursor.
 */
public class SQLiteCursor extends AbstractWindowedCursor {
    static final String TAG = "SQLiteCursor";
    static final int NO_COUNT = -1;

    /** The name of the table to edit */
    private final String mEditTable;

    /** The names of the columns in the rows */
    private final String[] mColumns;

    /** The query object for the cursor */
    private final SQLiteQuery mQuery;

    /** The compiled query this cursor came from */
    private final SQLiteCursorDriver mDriver;
=======
 */
public class SQLiteCursor extends AbstractWindowedCursor {
    static final String TAG = "Cursor";
    static final int NO_COUNT = -1;

    /** The name of the table to edit */
    private String mEditTable;

    /** The names of the columns in the rows */
    private String[] mColumns;

    /** The query object for the cursor */
    private SQLiteQuery mQuery;

    /** The database the cursor was created from */
    private SQLiteDatabase mDatabase;

    /** The compiled query this cursor came from */
    private SQLiteCursorDriver mDriver;
>>>>>>> 54b6cfa... Initial Contribution

    /** The number of rows in the cursor */
    private int mCount = NO_COUNT;

<<<<<<< HEAD
    /** The number of rows that can fit in the cursor window, 0 if unknown */
    private int mCursorWindowCapacity;

    /** A mapping of column names to column indices, to speed up lookups */
    private Map<String, Integer> mColumnNameMap;

    /** Used to find out where a cursor was allocated in case it never got released. */
    private final Throwable mStackTrace;
=======
    /** A mapping of column names to column indices, to speed up lookups */
    private Map<String, Integer> mColumnNameMap;

    /** Used to find out where a cursor was allocated in case it never got
     * released. */
    private StackTraceElement[] mStackTraceElements;
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Execute a query and provide access to its result set through a Cursor
     * interface. For a query such as: {@code SELECT name, birth, phone FROM
     * myTable WHERE ... LIMIT 1,20 ORDER BY...} the column names (name, birth,
     * phone) would be in the projection argument and everything from
<<<<<<< HEAD
     * {@code FROM} onward would be in the params argument.
     *
     * @param db a reference to a Database object that is already constructed
     *     and opened. This param is not used any longer
     * @param editTable the name of the table used for this query
     * @param query the rest of the query terms
     *     cursor is finalized
     * @deprecated use {@link #SQLiteCursor(SQLiteCursorDriver, String, SQLiteQuery)} instead
     */
    @Deprecated
    public SQLiteCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
            String editTable, SQLiteQuery query) {
        this(driver, editTable, query);
    }

    /**
     * Execute a query and provide access to its result set through a Cursor
     * interface. For a query such as: {@code SELECT name, birth, phone FROM
     * myTable WHERE ... LIMIT 1,20 ORDER BY...} the column names (name, birth,
     * phone) would be in the projection argument and everything from
     * {@code FROM} onward would be in the params argument.
     *
     * @param editTable the name of the table used for this query
     * @param query the {@link SQLiteQuery} object associated with this cursor object.
     */
    public SQLiteCursor(SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("query object cannot be null");
        }
        if (StrictMode.vmSqliteObjectLeaksEnabled()) {
            mStackTrace = new DatabaseObjectNotClosedException().fillInStackTrace();
        } else {
            mStackTrace = null;
        }
=======
     * {@code FROM} onward would be in the params argument. This constructor
     * has package scope.
     *
     * @param db a reference to a Database object that is already constructed
     *     and opened
     * @param editTable the name of the table used for this query
     * @param query the rest of the query terms
     *     cursor is finalized
     */
    public SQLiteCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
            String editTable, SQLiteQuery query) {
        // The AbstractCursor constructor needs to do some setup.
        super();

        if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
            mStackTraceElements = new Exception().getStackTrace();
        }

        mDatabase = db;
>>>>>>> 54b6cfa... Initial Contribution
        mDriver = driver;
        mEditTable = editTable;
        mColumnNameMap = null;
        mQuery = query;

<<<<<<< HEAD
        mColumns = query.getColumnNames();
        mRowIdColumnIndex = DatabaseUtils.findRowIdColumnIndex(mColumns);
    }

    /**
     * Get the database that this cursor is associated with.
     * @return the SQLiteDatabase that this cursor is associated with.
     */
    public SQLiteDatabase getDatabase() {
        return mQuery.getDatabase();
=======
        try {
            db.lock();

            // Setup the list of columns
            int columnCount = mQuery.columnCountLocked();
            mColumns = new String[columnCount];

            // Read in all column names
            for (int i = 0; i < columnCount; i++) {
                String columnName = mQuery.columnNameLocked(i);
                mColumns[i] = columnName;
                if (Config.LOGV) {
                    Log.v("DatabaseWindow", "mColumns[" + i + "] is "
                            + mColumns[i]);
                }
    
                // Make note of the row ID column index for quick access to it
                if ("_id".equals(columnName)) {
                    mRowIdColumnIndex = i;
                }
            }
        } finally {
            db.unlock();
        }
    }

    /**
     * @return the SQLiteDatabase that this cursor is associated with.
     */
    public SQLiteDatabase getDatabase() {
        return mDatabase;
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        // Make sure the row at newPosition is present in the window
        if (mWindow == null || newPosition < mWindow.getStartPosition() ||
                newPosition >= (mWindow.getStartPosition() + mWindow.getNumRows())) {
            fillWindow(newPosition);
        }

        return true;
    }

    @Override
    public int getCount() {
        if (mCount == NO_COUNT) {
            fillWindow(0);
        }
        return mCount;
    }

<<<<<<< HEAD
    private void fillWindow(int requiredPos) {
        clearOrCreateWindow(getDatabase().getPath());

        if (mCount == NO_COUNT) {
            int startPos = DatabaseUtils.cursorPickFillWindowStartPosition(requiredPos, 0);
            mCount = mQuery.fillWindow(mWindow, startPos, requiredPos, true);
            mCursorWindowCapacity = mWindow.getNumRows();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "received count(*) from native_fill_window: " + mCount);
            }
        } else {
            int startPos = DatabaseUtils.cursorPickFillWindowStartPosition(requiredPos,
                    mCursorWindowCapacity);
            mQuery.fillWindow(mWindow, startPos, requiredPos, false);
        }
=======
    private void fillWindow (int startPos) {
        if (mWindow == null) {
            // If there isn't a window set already it will only be accessed locally
            mWindow = new CursorWindow(true /* the window is local only */);
        } else {
            mWindow.clear();
        }

        // mWindow must be cleared
        mCount = mQuery.fillWindow(mWindow, startPos);
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public int getColumnIndex(String columnName) {
        // Create mColumnNameMap on demand
        if (mColumnNameMap == null) {
            String[] columns = mColumns;
            int columnCount = columns.length;
            HashMap<String, Integer> map = new HashMap<String, Integer>(columnCount, 1);
            for (int i = 0; i < columnCount; i++) {
                map.put(columns[i], i);
            }
            mColumnNameMap = map;
        }

        // Hack according to bug 903852
        final int periodIndex = columnName.lastIndexOf('.');
        if (periodIndex != -1) {
            Exception e = new Exception();
            Log.e(TAG, "requesting column name with table name -- " + columnName, e);
            columnName = columnName.substring(periodIndex + 1);
        }

        Integer i = mColumnNameMap.get(columnName);
        if (i != null) {
            return i.intValue();
        } else {
            return -1;
        }
    }

<<<<<<< HEAD
=======
    /**
     * @hide
     * @deprecated
     */
    @Override
    public boolean deleteRow() {
        checkPosition();

        // Only allow deletes if there is an ID column, and the ID has been read from it
        if (mRowIdColumnIndex == -1 || mCurrentRowID == null) {
            Log.e(TAG,
                    "Could not delete row because either the row ID column is not available or it" +
                    "has not been read.");
            return false;
        }

        boolean success;

        /*
         * Ensure we don't change the state of the database when another
         * thread is holding the database lock. requery() and moveTo() are also
         * synchronized here to make sure they get the state of the database
         * immediately following the DELETE.
         */
        mDatabase.lock();
        try {
            try {
                mDatabase.delete(mEditTable, mColumns[mRowIdColumnIndex] + "=?",
                        new String[] {mCurrentRowID.toString()});
                success = true;
            } catch (SQLException e) {
                success = false;
            }

            int pos = mPos;
            requery();

            /*
             * Ensure proper cursor state. Note that mCurrentRowID changes
             * in this call.
             */
            moveToPosition(pos);
        } finally {
            mDatabase.unlock();
        }

        if (success) {
            onChange(true);
            return true;
        } else {
            return false;
        }
    }

>>>>>>> 54b6cfa... Initial Contribution
    @Override
    public String[] getColumnNames() {
        return mColumns;
    }

<<<<<<< HEAD
    @Override
    public void deactivate() {
        super.deactivate();
=======
    /**
     * @hide
     * @deprecated
     */
    @Override
    public boolean supportsUpdates() {
        return super.supportsUpdates() && !TextUtils.isEmpty(mEditTable);
    }

    /**
     * @hide
     * @deprecated
     */
    @Override
    public boolean commitUpdates(Map<? extends Long,
            ? extends Map<String, Object>> additionalValues) {
        if (!supportsUpdates()) {
            Log.e(TAG, "commitUpdates not supported on this cursor, did you "
                    + "include the _id column?");
            return false;
        }

        /*
         * Prevent other threads from changing the updated rows while they're
         * being processed here.
         */
        synchronized (mUpdatedRows) {
            if (additionalValues != null) {
                mUpdatedRows.putAll(additionalValues);
            }

            if (mUpdatedRows.size() == 0) {
                return true;
            }

            /*
             * Prevent other threads from changing the database state while
             * we process the updated rows, and prevents us from changing the
             * database behind the back of another thread.
             */
            mDatabase.beginTransaction();
            try {
                StringBuilder sql = new StringBuilder(128);

                // For each row that has been updated
                for (Map.Entry<Long, Map<String, Object>> rowEntry :
                        mUpdatedRows.entrySet()) {
                    Map<String, Object> values = rowEntry.getValue();
                    Long rowIdObj = rowEntry.getKey();

                    if (rowIdObj == null || values == null) {
                        throw new IllegalStateException("null rowId or values found! rowId = "
                                + rowIdObj + ", values = " + values);
                    }

                    if (values.size() == 0) {
                        continue;
                    }

                    long rowId = rowIdObj.longValue();

                    Iterator<Map.Entry<String, Object>> valuesIter =
                            values.entrySet().iterator();

                    sql.setLength(0);
                    sql.append("UPDATE " + mEditTable + " SET ");

                    // For each column value that has been updated
                    Object[] bindings = new Object[values.size()];
                    int i = 0;
                    while (valuesIter.hasNext()) {
                        Map.Entry<String, Object> entry = valuesIter.next();
                        sql.append(entry.getKey());
                        sql.append("=?");
                        bindings[i] = entry.getValue();
                        if (valuesIter.hasNext()) {
                            sql.append(", ");
                        }
                        i++;
                    }

                    sql.append(" WHERE " + mColumns[mRowIdColumnIndex]
                            + '=' + rowId);
                    sql.append(';');
                    mDatabase.execSQL(sql.toString(), bindings);
                    mDatabase.rowUpdated(mEditTable, rowId);
                }
                mDatabase.setTransactionSuccessful();
            } finally {
                mDatabase.endTransaction();
            }

            mUpdatedRows.clear();
        }

        // Let any change observers know about the update
        onChange(true);

        return true;
    }

    private void deactivateCommon() {
        if (Config.LOGV) Log.v(TAG, "<<< Releasing cursor " + this);
        if (mWindow != null) {
            mWindow.close();
            mWindow = null;
        }
        if (Config.LOGV) Log.v("DatabaseWindow", "closing window in release()");
    }

    @Override
    public void deactivate() {
        super.deactivate();
        deactivateCommon();
>>>>>>> 54b6cfa... Initial Contribution
        mDriver.cursorDeactivated();
    }

    @Override
    public void close() {
        super.close();
<<<<<<< HEAD
        synchronized (this) {
            mQuery.close();
            mDriver.cursorClosed();
        }
=======
        deactivateCommon();
        mQuery.close();
        mDriver.cursorClosed();
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public boolean requery() {
<<<<<<< HEAD
        if (isClosed()) {
            return false;
        }

        synchronized (this) {
            if (!mQuery.getDatabase().isOpen()) {
                return false;
            }

=======
        long timeStart = 0;
        if (Config.LOGV) {
            timeStart = System.currentTimeMillis();
        }
        /*
         * Synchronize on the database lock to ensure that mCount matches the
         * results of mQuery.requery().
         */
        mDatabase.lock();
        try {
>>>>>>> 54b6cfa... Initial Contribution
            if (mWindow != null) {
                mWindow.clear();
            }
            mPos = -1;
<<<<<<< HEAD
            mCount = NO_COUNT;

            mDriver.cursorRequeried(this);
        }

        try {
            return super.requery();
        } catch (IllegalStateException e) {
            // for backwards compatibility, just return false
            Log.w(TAG, "requery() failed " + e.getMessage(), e);
            return false;
        }
=======
            // This one will recreate the temp table, and get its count
            mDriver.cursorRequeried(this);
            mCount = NO_COUNT;
            // Requery the program that runs over the temp table
            mQuery.requery();
        } finally {
            mDatabase.unlock();
        }

        if (Config.LOGV) {
            Log.v("DatabaseWindow", "closing window in requery()");
            Log.v(TAG, "--- Requery()ed cursor " + this + ": " + mQuery);
        }

        boolean result = super.requery();
        if (Config.LOGV) {
            long timeEnd = System.currentTimeMillis();
            Log.v(TAG, "requery (" + (timeEnd - timeStart) + " ms): " + mDriver.toString());
        }
        return result;
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public void setWindow(CursorWindow window) {
<<<<<<< HEAD
        super.setWindow(window);
        mCount = NO_COUNT;
=======
        if (mWindow != null) {
            mWindow.close();
            mCount = NO_COUNT;
        }
        mWindow = window;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Changes the selection arguments. The new values take effect after a call to requery().
     */
    public void setSelectionArguments(String[] selectionArgs) {
        mDriver.setBindArguments(selectionArgs);
    }

    /**
     * Release the native resources, if they haven't been released yet.
     */
    @Override
    protected void finalize() {
        try {
<<<<<<< HEAD
            // if the cursor hasn't been closed yet, close it first
            if (mWindow != null) {
                if (mStackTrace != null) {
                    String sql = mQuery.getSql();
                    int len = sql.length();
                    StrictMode.onSqliteObjectLeaked(
                        "Finalizing a Cursor that has not been deactivated or closed. " +
                        "database = " + mQuery.getDatabase().getLabel() +
                        ", table = " + mEditTable +
                        ", query = " + sql.substring(0, (len > 1000) ? 1000 : len),
                        mStackTrace);
                }
                close();
=======
            if (mWindow != null) {
                close();
                String message = "Finalizing cursor " + this + " on " + mEditTable
                        + " that has not been deactivated or closed";
                if (SQLiteDebug.DEBUG_ACTIVE_CURSOR_FINALIZATION) {
                    Log.d(TAG, message + "\nThis cursor was created in:");
                    for (StackTraceElement ste : mStackTraceElements) {
                        Log.d(TAG, "      " + ste);
                    }
                }
                SQLiteDebug.notifyActiveCursorFinalized();
                throw new IllegalStateException(message);
            } else {
                if (Config.LOGV) {
                    Log.v(TAG, "Finalizing cursor " + this + " on " + mEditTable);
                }
>>>>>>> 54b6cfa... Initial Contribution
            }
        } finally {
            super.finalize();
        }
    }
}
