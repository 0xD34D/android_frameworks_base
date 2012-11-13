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

<<<<<<< HEAD
import android.os.ParcelFileDescriptor;

/**
 * Represents a statement that can be executed against a database.  The statement
 * cannot return multiple rows or columns, but single value (1 x 1) result sets
 * are supported.
 * <p>
 * This class is not thread-safe.
 * </p>
 */
public final class SQLiteStatement extends SQLiteProgram {
    SQLiteStatement(SQLiteDatabase db, String sql, Object[] bindArgs) {
        super(db, sql, bindArgs, null);
    }

    /**
     * Execute this SQL statement, if it is not a SELECT / INSERT / DELETE / UPDATE, for example
     * CREATE / DROP table, view, trigger, index etc.
     *
     * @throws android.database.SQLException If the SQL string is invalid for
     *         some reason
     */
    public void execute() {
        acquireReference();
        try {
            getSession().execute(getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
        }
    }

    /**
     * Execute this SQL statement, if the the number of rows affected by execution of this SQL
     * statement is of any importance to the caller - for example, UPDATE / DELETE SQL statements.
     *
     * @return the number of rows affected by this SQL statement execution.
     * @throws android.database.SQLException If the SQL string is invalid for
     *         some reason
     */
    public int executeUpdateDelete() {
        acquireReference();
        try {
            return getSession().executeForChangedRowCount(
                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
=======
/**
 * A pre-compiled statement against a {@link SQLiteDatabase} that can be reused.
 * The statement cannot return multiple rows, but 1x1 result sets are allowed.
 * Don't use SQLiteStatement constructor directly, please use
 * {@link SQLiteDatabase#compileStatement(String)}
 */
public class SQLiteStatement extends SQLiteProgram
{
    /**
     * Don't use SQLiteStatement constructor directly, please use
     * {@link SQLiteDatabase#compileStatement(String)}
     * @param db
     * @param sql
     */
    /* package */ SQLiteStatement(SQLiteDatabase db, String sql) {
        super(db, sql);
    }

    /**
     * Execute this SQL statement, if it is not a query. For example,
     * CREATE TABLE, DELTE, INSERT, etc.
     *
     * @throws android.database.SQLException If the SQL string is invalid for
     *         some reason
     */
    public void execute() {
        mDatabase.lock();
        acquireReference();
        try {
            native_execute();
        } finally {
            releaseReference();
            mDatabase.unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Execute this SQL statement and return the ID of the row inserted due to this call.
     * The SQL statement should be an INSERT for this to be a useful call.
     *
     * @return the row ID of the last row inserted, if this insert is successful. -1 otherwise.
=======
     * Execute this SQL statement and return the ID of the most
     * recently inserted row.  The SQL statement should probably be an
     * INSERT for this to be a useful call.
     *
     * @return the row ID of the last row inserted.
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @throws android.database.SQLException If the SQL string is invalid for
     *         some reason
     */
    public long executeInsert() {
<<<<<<< HEAD
        acquireReference();
        try {
            return getSession().executeForLastInsertedRowId(
                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
=======
        mDatabase.lock();
        acquireReference();
        try {
            native_execute();
            return mDatabase.lastInsertRow();
        } finally {
            releaseReference();
            mDatabase.unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Execute a statement that returns a 1 by 1 table with a numeric value.
     * For example, SELECT COUNT(*) FROM table;
     *
     * @return The result of the query.
     *
     * @throws android.database.sqlite.SQLiteDoneException if the query returns zero rows
     */
    public long simpleQueryForLong() {
<<<<<<< HEAD
        acquireReference();
        try {
            return getSession().executeForLong(
                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
=======
        mDatabase.lock();
        acquireReference();
        try {
            return native_1x1_long();
        } finally {
            releaseReference();
            mDatabase.unlock();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Execute a statement that returns a 1 by 1 table with a text value.
     * For example, SELECT COUNT(*) FROM table;
     *
     * @return The result of the query.
     *
     * @throws android.database.sqlite.SQLiteDoneException if the query returns zero rows
     */
    public String simpleQueryForString() {
<<<<<<< HEAD
        acquireReference();
        try {
            return getSession().executeForString(
                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
        }
    }

    /**
     * Executes a statement that returns a 1 by 1 table with a blob value.
     *
     * @return A read-only file descriptor for a copy of the blob value, or {@code null}
     *         if the value is null or could not be read for some reason.
     *
     * @throws android.database.sqlite.SQLiteDoneException if the query returns zero rows
     */
    public ParcelFileDescriptor simpleQueryForBlobFileDescriptor() {
        acquireReference();
        try {
            return getSession().executeForBlobFileDescriptor(
                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
        }
    }

    @Override
    public String toString() {
        return "SQLiteProgram: " + getSql();
    }
=======
        mDatabase.lock();
        acquireReference();
        try {
            return native_1x1_string();
        } finally {
            releaseReference();
            mDatabase.unlock();
        }
    }

    private final native void native_execute();
    private final native long native_1x1_long();
    private final native String native_1x1_string();
>>>>>>> 54b6cfa... Initial Contribution
}
