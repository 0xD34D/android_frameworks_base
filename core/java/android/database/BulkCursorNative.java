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

package android.database;

import android.os.Binder;
<<<<<<< HEAD
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
=======
import android.os.RemoteException;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * Native implementation of the bulk cursor. This is only for use in implementing
 * IPC, application code should use the Cursor interface.
<<<<<<< HEAD
 *
=======
 * 
>>>>>>> 54b6cfa... Initial Contribution
 * {@hide}
 */
public abstract class BulkCursorNative extends Binder implements IBulkCursor
{
    public BulkCursorNative()
    {
        attachInterface(this, descriptor);
    }

    /**
     * Cast a Binder object into a content resolver interface, generating
     * a proxy if needed.
     */
    static public IBulkCursor asInterface(IBinder obj)
    {
        if (obj == null) {
            return null;
        }
        IBulkCursor in = (IBulkCursor)obj.queryLocalInterface(descriptor);
        if (in != null) {
            return in;
        }

        return new BulkCursorProxy(obj);
    }
    
    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException {
        try {
            switch (code) {
                case GET_CURSOR_WINDOW_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    int startPos = data.readInt();
                    CursorWindow window = getWindow(startPos);
<<<<<<< HEAD
                    reply.writeNoException();
                    if (window == null) {
                        reply.writeInt(0);
                    } else {
                        reply.writeInt(1);
                        window.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
=======
                    if (window == null) {
                        reply.writeInt(0);
                        return true;
                    }
                    reply.writeNoException();
                    reply.writeInt(1);
                    window.writeToParcel(reply, 0);
                    return true;
                }

                case COUNT_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    int count = count();
                    reply.writeNoException();
                    reply.writeInt(count);
                    return true;
                }

                case GET_COLUMN_NAMES_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    String[] columnNames = getColumnNames();
                    reply.writeNoException();
                    reply.writeInt(columnNames.length);
                    int length = columnNames.length;
                    for (int i = 0; i < length; i++) {
                        reply.writeString(columnNames[i]);
>>>>>>> 54b6cfa... Initial Contribution
                    }
                    return true;
                }

                case DEACTIVATE_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    deactivate();
                    reply.writeNoException();
                    return true;
                }
                
                case CLOSE_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    close();
                    reply.writeNoException();
                    return true;
                }

                case REQUERY_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    IContentObserver observer =
<<<<<<< HEAD
                            IContentObserver.Stub.asInterface(data.readStrongBinder());
                    int count = requery(observer);
=======
                        IContentObserver.Stub.asInterface(data.readStrongBinder());
                    CursorWindow window = CursorWindow.CREATOR.createFromParcel(data);
                    int count = requery(observer, window);
>>>>>>> 54b6cfa... Initial Contribution
                    reply.writeNoException();
                    reply.writeInt(count);
                    reply.writeBundle(getExtras());
                    return true;
                }

<<<<<<< HEAD
=======
                case UPDATE_ROWS_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    // TODO - what ClassLoader should be passed to readHashMap?
                    // TODO - switch to Bundle
                    HashMap<Long, Map<String, Object>> values = data.readHashMap(null);
                    boolean result = updateRows(values);
                    reply.writeNoException();
                    reply.writeInt((result == true ? 1 : 0));
                    return true;
                }

                case DELETE_ROW_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    int position = data.readInt();
                    boolean result = deleteRow(position);
                    reply.writeNoException();
                    reply.writeInt((result == true ? 1 : 0));
                    return true;
                }

>>>>>>> 54b6cfa... Initial Contribution
                case ON_MOVE_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    int position = data.readInt();
                    onMove(position);
                    reply.writeNoException();
                    return true;
                }

<<<<<<< HEAD
=======
                case WANTS_ON_MOVE_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    boolean result = getWantsAllOnMoveCalls();
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }

>>>>>>> 54b6cfa... Initial Contribution
                case GET_EXTRAS_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    Bundle extras = getExtras();
                    reply.writeNoException();
                    reply.writeBundle(extras);
                    return true;
                }

                case RESPOND_TRANSACTION: {
                    data.enforceInterface(IBulkCursor.descriptor);
                    Bundle extras = data.readBundle();
                    Bundle returnExtras = respond(extras);
                    reply.writeNoException();
                    reply.writeBundle(returnExtras);
                    return true;
                }
            }
        } catch (Exception e) {
            DatabaseUtils.writeExceptionToParcel(reply, e);
            return true;
        }

        return super.onTransact(code, data, reply, flags);
    }

    public IBinder asBinder()
    {
        return this;
    }
}


final class BulkCursorProxy implements IBulkCursor {
    private IBinder mRemote;
    private Bundle mExtras;

    public BulkCursorProxy(IBinder remote)
    {
        mRemote = remote;
        mExtras = null;
    }

    public IBinder asBinder()
    {
        return mRemote;
    }

<<<<<<< HEAD
    public CursorWindow getWindow(int position) throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IBulkCursor.descriptor);
            data.writeInt(position);

            mRemote.transact(GET_CURSOR_WINDOW_TRANSACTION, data, reply, 0);
            DatabaseUtils.readExceptionFromParcel(reply);

            CursorWindow window = null;
            if (reply.readInt() == 1) {
                window = CursorWindow.newFromParcel(reply);
            }
            return window;
        } finally {
            data.recycle();
            reply.recycle();
        }
=======
    public CursorWindow getWindow(int startPos) throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        data.writeInt(startPos);

        mRemote.transact(GET_CURSOR_WINDOW_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);
        
        CursorWindow window = null;
        if (reply.readInt() == 1) {
            window = CursorWindow.newFromParcel(reply);
        }

        data.recycle();
        reply.recycle();

        return window;
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void onMove(int position) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
<<<<<<< HEAD
        try {
            data.writeInterfaceToken(IBulkCursor.descriptor);
            data.writeInt(position);

            mRemote.transact(ON_MOVE_TRANSACTION, data, reply, 0);
            DatabaseUtils.readExceptionFromParcel(reply);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void deactivate() throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IBulkCursor.descriptor);

            mRemote.transact(DEACTIVATE_TRANSACTION, data, reply, 0);
            DatabaseUtils.readExceptionFromParcel(reply);
        } finally {
            data.recycle();
            reply.recycle();
        }
=======

        data.writeInterfaceToken(IBulkCursor.descriptor);

        data.writeInt(position);

        mRemote.transact(ON_MOVE_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);

        data.recycle();
        reply.recycle();
    }

    public int count() throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        boolean result = mRemote.transact(COUNT_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);
        
        int count;
        if (result == false) {
            count = -1;
        } else {
            count = reply.readInt();
        }
        data.recycle();
        reply.recycle();
        return count;
    }

    public String[] getColumnNames() throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        mRemote.transact(GET_COLUMN_NAMES_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);
        
        String[] columnNames = null;
        int numColumns = reply.readInt();
        columnNames = new String[numColumns];
        for (int i = 0; i < numColumns; i++) {
            columnNames[i] = reply.readString();
        }
        
        data.recycle();
        reply.recycle();
        return columnNames;
    }

    public void deactivate() throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        mRemote.transact(DEACTIVATE_TRANSACTION, data, reply, 0);
        DatabaseUtils.readExceptionFromParcel(reply);

        data.recycle();
        reply.recycle();
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void close() throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
<<<<<<< HEAD
        try {
            data.writeInterfaceToken(IBulkCursor.descriptor);

            mRemote.transact(CLOSE_TRANSACTION, data, reply, 0);
            DatabaseUtils.readExceptionFromParcel(reply);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }
    
    public int requery(IContentObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IBulkCursor.descriptor);
            data.writeStrongInterface(observer);

            boolean result = mRemote.transact(REQUERY_TRANSACTION, data, reply, 0);
            DatabaseUtils.readExceptionFromParcel(reply);

            int count;
            if (!result) {
                count = -1;
            } else {
                count = reply.readInt();
                mExtras = reply.readBundle();
            }
            return count;
        } finally {
            data.recycle();
            reply.recycle();
        }
=======

        data.writeInterfaceToken(IBulkCursor.descriptor);

        mRemote.transact(CLOSE_TRANSACTION, data, reply, 0);
        DatabaseUtils.readExceptionFromParcel(reply);

        data.recycle();
        reply.recycle();
    }
    
    public int requery(IContentObserver observer, CursorWindow window) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        data.writeStrongInterface(observer);
        window.writeToParcel(data, 0);

        boolean result = mRemote.transact(REQUERY_TRANSACTION, data, reply, 0);
        
        DatabaseUtils.readExceptionFromParcel(reply);

        int count;
        if (!result) {
            count = -1;
        } else {
            count = reply.readInt();
            mExtras = reply.readBundle();
        }

        data.recycle();
        reply.recycle();

        return count;
    }

    public boolean updateRows(Map values) throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        data.writeMap(values);

        mRemote.transact(UPDATE_ROWS_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);
        
        boolean result = (reply.readInt() == 1 ? true : false);

        data.recycle();
        reply.recycle();

        return result;
    }

    public boolean deleteRow(int position) throws RemoteException
    {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        data.writeInt(position);

        mRemote.transact(DELETE_ROW_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);
        
        boolean result = (reply.readInt() == 1 ? true : false);

        data.recycle();
        reply.recycle();

        return result;
    }

    public boolean getWantsAllOnMoveCalls() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken(IBulkCursor.descriptor);

        mRemote.transact(WANTS_ON_MOVE_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);

        int result = reply.readInt();
        data.recycle();
        reply.recycle();
        return result != 0;
>>>>>>> 54b6cfa... Initial Contribution
    }

    public Bundle getExtras() throws RemoteException {
        if (mExtras == null) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
<<<<<<< HEAD
            try {
                data.writeInterfaceToken(IBulkCursor.descriptor);

                mRemote.transact(GET_EXTRAS_TRANSACTION, data, reply, 0);
                DatabaseUtils.readExceptionFromParcel(reply);

                mExtras = reply.readBundle();
            } finally {
                data.recycle();
                reply.recycle();
            }
=======

            data.writeInterfaceToken(IBulkCursor.descriptor);

            mRemote.transact(GET_EXTRAS_TRANSACTION, data, reply, 0);

            DatabaseUtils.readExceptionFromParcel(reply);

            mExtras = reply.readBundle();
            data.recycle();
            reply.recycle();
>>>>>>> 54b6cfa... Initial Contribution
        }
        return mExtras;
    }

    public Bundle respond(Bundle extras) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
<<<<<<< HEAD
        try {
            data.writeInterfaceToken(IBulkCursor.descriptor);
            data.writeBundle(extras);

            mRemote.transact(RESPOND_TRANSACTION, data, reply, 0);
            DatabaseUtils.readExceptionFromParcel(reply);

            Bundle returnExtras = reply.readBundle();
            return returnExtras;
        } finally {
            data.recycle();
            reply.recycle();
        }
=======

        data.writeInterfaceToken(IBulkCursor.descriptor);

        data.writeBundle(extras);

        mRemote.transact(RESPOND_TRANSACTION, data, reply, 0);

        DatabaseUtils.readExceptionFromParcel(reply);

        Bundle returnExtras = reply.readBundle();
        data.recycle();
        reply.recycle();
        return returnExtras;
>>>>>>> 54b6cfa... Initial Contribution
    }
}

