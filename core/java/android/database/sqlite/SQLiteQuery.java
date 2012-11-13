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

import android.database.CursorWindow;
<<<<<<< HEAD
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.util.Log;

/**
 * Represents a query that reads the resulting rows into a {@link SQLiteQuery}.
 * This class is used by {@link SQLiteCursor} and isn't useful itself.
 * <p>
 * This class is not thread-safe.
 * </p>
 */
public final class SQLiteQuery extends SQLiteProgram {
    private static final String TAG = "SQLiteQuery";

    private final CancellationSignal mCancellationSignal;

    SQLiteQuery(SQLiteDatabase db, String query, CancellationSignal cancellationSignal) {
        super(db, query, null, cancellationSignal);

        mCancellationSignal = cancellationSignal;
    }

    /**
     * Reads rows into a buffer.
     *
     * @param window The window to fill into
     * @param startPos The start position for filling the window.
     * @param requiredPos The position of a row that MUST be in the window.
     * If it won't fit, then the query should discard part of what it filled.
     * @param countAllRows True to count all rows that the query would
     * return regardless of whether they fit in the window.
     * @return Number of rows that were enumerated.  Might not be all rows
     * unless countAllRows is true.
     *
     * @throws SQLiteException if an error occurs.
     * @throws OperationCanceledException if the operation was canceled.
     */
    int fillWindow(CursorWindow window, int startPos, int requiredPos, boolean countAllRows) {
        acquireReference();
        try {
            window.acquireReference();
            try {
                int numRows = getSession().executeForCursorWindow(getSql(), getBindArgs(),
                        window, startPos, requiredPos, countAllRows, getConnectionFlags(),
                        mCancellationSignal);
                return numRows;
            } catch (SQLiteDatabaseCorruptException ex) {
                onCorruption();
                throw ex;
            } catch (SQLiteException ex) {
                Log.e(TAG, "exception: " + ex.getMessage() + "; query: " + getSql());
                throw ex;
            } finally {
                window.releaseReference();
            }
        } finally {
            releaseReference();
=======

/**
 * A SQLite program that represents a query that reads the resulting rows into a CursorWindow.
 * This class is used by SQLiteCursor and isn't useful itself.
 */
public class SQLiteQuery extends SQLiteProgram {
    //private static final String TAG = "Cursor";

    /** The index of the unbound OFFSET parameter */
    private int mOffsetIndex;
    
    /** The SQL used to create this query */
    private String mQuery;

    /** Args to bind on requery */
    private String[] mBindArgs;

    private boolean mClosed = false;

    /**
     * Create a persistent query object.
     * 
     * @param db The database that this query object is associated with
     * @param query The SQL string for this query. It must include "INDEX -1
     *            OFFSET ?" at the end
     * @param offsetIndex The 1-based index to the OFFSET parameter
     */
    /* package */ SQLiteQuery(SQLiteDatabase db, String query, int offsetIndex, String[] bindArgs) {
        super(db, query);

        mOffsetIndex = offsetIndex;
        mQuery = query;
        mBindArgs = bindArgs;
    }

    /**
     * Reads rows into a buffer. This method acquires the database lock.
     * 
     * @param window The window to fill into
     * @param startPos The position to start reading rows from
     * @return number of total rows in the query
     */
    /* package */ int fillWindow(CursorWindow window, int startPos) {
        if (startPos < 0) {
            throw new IllegalArgumentException("startPos should > 0");
        }
        window.setStartPosition(startPos);
        mDatabase.lock();
        try {
            acquireReference();
            window.acquireReference();
            return native_fill_window(window, startPos, mOffsetIndex);
        } catch (IllegalStateException e){
            // simply ignore it
            return 0;
        } catch (SQLiteDatabaseCorruptException e) {
            mDatabase.onCorruption();
            throw e;
        } finally {
            window.releaseReference();
            releaseReference();
            mDatabase.unlock();
        }
    }

    /**
     * Get the column count for the statement. Only valid on query based
     * statements. The database must be locked
     * when calling this method.
     * 
     * @return The number of column in the statement's result set.
     */
    /* package */ int columnCountLocked() {
        acquireReference();
        try {
            return native_column_count();
        } finally {
            releaseReference();
        }
    }

    /**
     * Retrieves the column name for the given column index. The database must be locked
     * when calling this method.
     * 
     * @param columnIndex the index of the column to get the name for
     * @return The requested column's name
     */
    /* package */ String columnNameLocked(int columnIndex) {
        acquireReference();
        try {
            return native_column_name(columnIndex);
        } finally {
            releaseReference();
        }
    }

    @Override
    public void close() {
        super.close();
        mClosed = true;
    }

    /**
     * Called by SQLiteCursor when it is requeried.
     */
    /* package */ void requery() {
        boolean oldMClosed = mClosed;
        if (mClosed) {
            mClosed = false;
            compile(mQuery, false);
        }
        if (mBindArgs != null) {
            int len = mBindArgs.length;
            try {
                for (int i = 0; i < len; i++) {
                    super.bindString(i + 1, mBindArgs[i]);
                }
            } catch (SQLiteMisuseException e) {
                StringBuilder errMsg = new StringBuilder
                    ("old mClosed " + oldMClosed + " mQuery " + mQuery);
                for (int i = 0; i < len; i++) {
                    errMsg.append(" ");
                    errMsg.append(mBindArgs[i]);
                }
                errMsg.append(" ");
                IllegalStateException leakProgram = new IllegalStateException(
                        errMsg.toString(), e);
                throw leakProgram;                
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    @Override
<<<<<<< HEAD
    public String toString() {
        return "SQLiteQuery: " + getSql();
    }
=======
    public void bindNull(int index) {
        mBindArgs[index - 1] = null;
        if (!mClosed) super.bindNull(index);
    }

    @Override
    public void bindLong(int index, long value) {
        mBindArgs[index - 1] = Long.toString(value);
        if (!mClosed) super.bindLong(index, value);
    }

    @Override
    public void bindDouble(int index, double value) {
        mBindArgs[index - 1] = Double.toString(value);
        if (!mClosed) super.bindDouble(index, value);
    }

    @Override
    public void bindString(int index, String value) {
        mBindArgs[index - 1] = value;
        if (!mClosed) super.bindString(index, value);
    }

    private final native int native_fill_window(CursorWindow window, int startPos, int offsetParam);

    private final native int native_column_count();

    private final native String native_column_name(int columnIndex);
>>>>>>> 54b6cfa... Initial Contribution
}
