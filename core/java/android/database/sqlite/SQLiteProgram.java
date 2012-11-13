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
import android.database.DatabaseUtils;
import android.os.CancellationSignal;

import java.util.Arrays;

/**
 * A base class for compiled SQLite programs.
 * <p>
 * This class is not thread-safe.
 * </p>
 */
public abstract class SQLiteProgram extends SQLiteClosable {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private final SQLiteDatabase mDatabase;
    private final String mSql;
    private final boolean mReadOnly;
    private final String[] mColumnNames;
    private final int mNumParameters;
    private final Object[] mBindArgs;

    SQLiteProgram(SQLiteDatabase db, String sql, Object[] bindArgs,
            CancellationSignal cancellationSignalForPrepare) {
        mDatabase = db;
        mSql = sql.trim();

        int n = DatabaseUtils.getSqlStatementType(mSql);
        switch (n) {
            case DatabaseUtils.STATEMENT_BEGIN:
            case DatabaseUtils.STATEMENT_COMMIT:
            case DatabaseUtils.STATEMENT_ABORT:
                mReadOnly = false;
                mColumnNames = EMPTY_STRING_ARRAY;
                mNumParameters = 0;
                break;

            default:
                boolean assumeReadOnly = (n == DatabaseUtils.STATEMENT_SELECT);
                SQLiteStatementInfo info = new SQLiteStatementInfo();
                db.getThreadSession().prepare(mSql,
                        db.getThreadDefaultConnectionFlags(assumeReadOnly),
                        cancellationSignalForPrepare, info);
                mReadOnly = info.readOnly;
                mColumnNames = info.columnNames;
                mNumParameters = info.numParameters;
                break;
        }

        if (bindArgs != null && bindArgs.length > mNumParameters) {
            throw new IllegalArgumentException("Too many bind arguments.  "
                    + bindArgs.length + " arguments were provided but the statement needs "
                    + mNumParameters + " arguments.");
        }

        if (mNumParameters != 0) {
            mBindArgs = new Object[mNumParameters];
            if (bindArgs != null) {
                System.arraycopy(bindArgs, 0, mBindArgs, 0, bindArgs.length);
            }
        } else {
            mBindArgs = null;
        }
    }

    final SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    final String getSql() {
        return mSql;
    }

    final Object[] getBindArgs() {
        return mBindArgs;
    }

    final String[] getColumnNames() {
        return mColumnNames;
    }

    /** @hide */
    protected final SQLiteSession getSession() {
        return mDatabase.getThreadSession();
    }

    /** @hide */
    protected final int getConnectionFlags() {
        return mDatabase.getThreadDefaultConnectionFlags(mReadOnly);
    }

    /** @hide */
    protected final void onCorruption() {
        mDatabase.onCorruption();
    }

    /**
     * Unimplemented.
     * @deprecated This method is deprecated and must not be used.
     */
    @Deprecated
    public final int getUniqueId() {
        return -1;
    }

    /**
=======
import android.util.Log;

/**
 * A base class for compiled SQLite programs.
 */
public abstract class SQLiteProgram extends SQLiteClosable {
    static final String TAG = "SQLiteProgram";

    /** The database this program is compiled against. */
    protected SQLiteDatabase mDatabase;

    /**
     * Native linkage, do not modify. This comes from the database and should not be modified
     * in here or in the native code.
     */
    protected int nHandle = 0;

    /**
     * Native linkage, do not modify. When non-0 this holds a reference to a valid
     * sqlite3_statement object. It is only updated by the native code, but may be
     * checked in this class when the database lock is held to determine if there
     * is a valid native-side program or not.
     */
    protected int nStatement = 0;

    /**
     * Used to find out where a cursor was allocated in case it never got
     * released.
     */
    private StackTraceElement[] mStackTraceElements;    
 
    /* package */ SQLiteProgram(SQLiteDatabase db, String sql) {
        if (SQLiteDebug.DEBUG_SQL_STATEMENTS) {
            mStackTraceElements = new Exception().getStackTrace();
        }
        
        mDatabase = db;
        db.acquireReference();
        db.addSQLiteClosable(this);
        this.nHandle = db.mNativeHandle;
        compile(sql, false);
    }    
    
    @Override
    protected void onAllReferencesReleased() {
        // Note that native_finalize() checks to make sure that nStatement is
        // non-null before destroying it.
        native_finalize();
        mDatabase.releaseReference();
        mDatabase.removeSQLiteClosable(this);
    }
    
    @Override
    protected void onAllReferencesReleasedFromContainer(){
        // Note that native_finalize() checks to make sure that nStatement is
        // non-null before destroying it.
        native_finalize();
        mDatabase.releaseReference();        
    }

    /**
     * Returns a unique identifier for this program.
     * 
     * @return a unique identifier for this program
     */
    public final int getUniqueId() {
        return nStatement;
    }

    /**
     * Compiles the given SQL into a SQLite byte code program using sqlite3_prepare_v2(). If
     * this method has been called previously without a call to close and forCompilation is set
     * to false the previous compilation will be used. Setting forceCompilation to true will
     * always re-compile the program and should be done if you pass differing SQL strings to this
     * method.
     *
     * <P>Note: this method acquires the database lock.</P>
     *
     * @param sql the SQL string to compile
     * @param forceCompilation forces the SQL to be recompiled in the event that there is an
     *  existing compiled SQL program already around
     */
    protected void compile(String sql, boolean forceCompilation) {
        // Only compile if we don't have a valid statement already or the caller has
        // explicitly requested a recompile. 
        if (nStatement == 0 || forceCompilation) {
            mDatabase.lock();
            try {
                // Note that the native_compile() takes care of destroying any previously
                // existing programs before it compiles.
                acquireReference();                
                native_compile(sql);
            } finally {
                releaseReference();
                mDatabase.unlock();
            }        
        }
    } 
  
    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Bind a NULL value to this statement. The value remains bound until
     * {@link #clearBindings} is called.
     *
     * @param index The 1-based index to the parameter to bind null to
     */
    public void bindNull(int index) {
<<<<<<< HEAD
        bind(index, null);
=======
        acquireReference();
        try {
            native_bind_null(index);
        } finally {
            releaseReference();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Bind a long value to this statement. The value remains bound until
     * {@link #clearBindings} is called.
<<<<<<< HEAD
     *addToBindArgs
=======
     *
>>>>>>> 54b6cfa... Initial Contribution
     * @param index The 1-based index to the parameter to bind
     * @param value The value to bind
     */
    public void bindLong(int index, long value) {
<<<<<<< HEAD
        bind(index, value);
=======
        acquireReference();
        try {
            native_bind_long(index, value);
        } finally {
            releaseReference();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Bind a double value to this statement. The value remains bound until
     * {@link #clearBindings} is called.
     *
     * @param index The 1-based index to the parameter to bind
     * @param value The value to bind
     */
    public void bindDouble(int index, double value) {
<<<<<<< HEAD
        bind(index, value);
=======
        acquireReference();
        try {
            native_bind_double(index, value);
        } finally {
            releaseReference();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Bind a String value to this statement. The value remains bound until
     * {@link #clearBindings} is called.
     *
     * @param index The 1-based index to the parameter to bind
<<<<<<< HEAD
     * @param value The value to bind, must not be null
=======
     * @param value The value to bind
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void bindString(int index, String value) {
        if (value == null) {
            throw new IllegalArgumentException("the bind value at index " + index + " is null");
        }
<<<<<<< HEAD
        bind(index, value);
=======
        acquireReference();
        try {
            native_bind_string(index, value);
        } finally {
            releaseReference();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Bind a byte array value to this statement. The value remains bound until
     * {@link #clearBindings} is called.
     *
     * @param index The 1-based index to the parameter to bind
<<<<<<< HEAD
     * @param value The value to bind, must not be null
=======
     * @param value The value to bind
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void bindBlob(int index, byte[] value) {
        if (value == null) {
            throw new IllegalArgumentException("the bind value at index " + index + " is null");
        }
<<<<<<< HEAD
        bind(index, value);
=======
        acquireReference();
        try {
            native_bind_blob(index, value);
        } finally {
            releaseReference();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Clears all existing bindings. Unset bindings are treated as NULL.
     */
    public void clearBindings() {
<<<<<<< HEAD
        if (mBindArgs != null) {
            Arrays.fill(mBindArgs, null);
=======
        acquireReference();
        try {
            native_clear_bindings();
        } finally {
            releaseReference();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Given an array of String bindArgs, this method binds all of them in one single call.
     *
     * @param bindArgs the String array of bind args, none of which must be null.
     */
    public void bindAllArgsAsStrings(String[] bindArgs) {
        if (bindArgs != null) {
            for (int i = bindArgs.length; i != 0; i--) {
                bindString(i, bindArgs[i - 1]);
            }
        }
    }

    @Override
    protected void onAllReferencesReleased() {
        clearBindings();
    }

    private void bind(int index, Object value) {
        if (index < 1 || index > mNumParameters) {
            throw new IllegalArgumentException("Cannot bind argument at index "
                    + index + " because the index is out of range.  "
                    + "The statement has " + mNumParameters + " parameters.");
        }
        mBindArgs[index - 1] = value;
    }
}
=======
     * Release this program's resources, making it invalid.
     */
    public void close() {
        mDatabase.lock();
        try {
            releaseReference();
        } finally {
            mDatabase.unlock();
        }        
    }
    
    /**
     * Make sure that the native resource is cleaned up.
     */
    @Override
    protected void finalize() {
        if (nStatement != 0) {
            if (SQLiteDebug.DEBUG_SQL_STATEMENTS) {
                String message = "Finalizing " + this +  
                    " that has not been closed";

                Log.d(TAG, message + "\nThis cursor was created in:");
                for (StackTraceElement ste : mStackTraceElements) {
                    Log.d(TAG, "      " + ste);
                }
            }
            onAllReferencesReleased();
        }
    }

    /**
     * Compiles SQL into a SQLite program.
     * 
     * <P>The database lock must be held when calling this method.
     * @param sql The SQL to compile.
     */
    protected final native void native_compile(String sql);
    protected final native void native_finalize();

    protected final native void native_bind_null(int index);
    protected final native void native_bind_long(int index, long value);
    protected final native void native_bind_double(int index, double value);
    protected final native void native_bind_string(int index, String value);
    protected final native void native_bind_blob(int index, byte[] value);
    private final native void native_clear_bindings();
}

>>>>>>> 54b6cfa... Initial Contribution
