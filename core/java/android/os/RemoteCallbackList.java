/*
 * Copyright (C) 2008 The Android Open Source Project
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

package android.os;

import java.util.HashMap;

/**
 * Takes care of the grunt work of maintaining a list of remote interfaces,
 * typically for the use of performing callbacks from a
 * {@link android.app.Service} to its clients.  In particular, this:
<<<<<<< HEAD
 *
=======
 * 
>>>>>>> 54b6cfa... Initial Contribution
 * <ul>
 * <li> Keeps track of a set of registered {@link IInterface} callbacks,
 * taking care to identify them through their underlying unique {@link IBinder}
 * (by calling {@link IInterface#asBinder IInterface.asBinder()}.
 * <li> Attaches a {@link IBinder.DeathRecipient IBinder.DeathRecipient} to
 * each registered interface, so that it can be cleaned out of the list if its
 * process goes away.
 * <li> Performs locking of the underlying list of interfaces to deal with
 * multithreaded incoming calls, and a thread-safe way to iterate over a
 * snapshot of the list without holding its lock.
 * </ul>
<<<<<<< HEAD
 *
=======
 * 
>>>>>>> 54b6cfa... Initial Contribution
 * <p>To use this class, simply create a single instance along with your
 * service, and call its {@link #register} and {@link #unregister} methods
 * as client register and unregister with your service.  To call back on to
 * the registered clients, use {@link #beginBroadcast},
 * {@link #getBroadcastItem}, and {@link #finishBroadcast}.
<<<<<<< HEAD
 *
=======
 * 
>>>>>>> 54b6cfa... Initial Contribution
 * <p>If a registered callback's process goes away, this class will take
 * care of automatically removing it from the list.  If you want to do
 * additional work in this situation, you can create a subclass that
 * implements the {@link #onCallbackDied} method.
 */
public class RemoteCallbackList<E extends IInterface> {
    /*package*/ HashMap<IBinder, Callback> mCallbacks
            = new HashMap<IBinder, Callback>();
<<<<<<< HEAD
    private Object[] mActiveBroadcast;
    private int mBroadcastCount = -1;
    private boolean mKilled = false;

    private final class Callback implements IBinder.DeathRecipient {
        final E mCallback;
        final Object mCookie;
        
        Callback(E callback, Object cookie) {
            mCallback = callback;
            mCookie = cookie;
        }

=======
    private IInterface[] mActiveBroadcast;
    private boolean mKilled = false;
    
    private final class Callback implements IBinder.DeathRecipient {
        final E mCallback;
        
        Callback(E callback) {
            mCallback = callback;
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        public void binderDied() {
            synchronized (mCallbacks) {
                mCallbacks.remove(mCallback.asBinder());
            }
<<<<<<< HEAD
            onCallbackDied(mCallback, mCookie);
        }
    }

    /**
     * Simple version of {@link RemoteCallbackList#register(E, Object)}
     * that does not take a cookie object.
     */
    public boolean register(E callback) {
        return register(callback, null);
    }
=======
            onCallbackDied(mCallback);
        }
    }
>>>>>>> 54b6cfa... Initial Contribution
    
    /**
     * Add a new callback to the list.  This callback will remain in the list
     * until a corresponding call to {@link #unregister} or its hosting process
     * goes away.  If the callback was already registered (determined by
     * checking to see if the {@link IInterface#asBinder callback.asBinder()}
     * object is already in the list), then it will be left as-is.
     * Registrations are not counted; a single call to {@link #unregister}
     * will remove a callback after any number calls to register it.
<<<<<<< HEAD
     *
=======
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * @param callback The callback interface to be added to the list.  Must
     * not be null -- passing null here will cause a NullPointerException.
     * Most services will want to check for null before calling this with
     * an object given from a client, so that clients can't crash the
     * service with bad data.
<<<<<<< HEAD
     *
     * @param cookie Optional additional data to be associated with this
     * callback.
=======
>>>>>>> 54b6cfa... Initial Contribution
     * 
     * @return Returns true if the callback was successfully added to the list.
     * Returns false if it was not added, either because {@link #kill} had
     * previously been called or the callback's process has gone away.
<<<<<<< HEAD
     *
=======
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * @see #unregister
     * @see #kill
     * @see #onCallbackDied
     */
<<<<<<< HEAD
    public boolean register(E callback, Object cookie) {
=======
    public boolean register(E callback) {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (mCallbacks) {
            if (mKilled) {
                return false;
            }
            IBinder binder = callback.asBinder();
            try {
<<<<<<< HEAD
                Callback cb = new Callback(callback, cookie);
=======
                Callback cb = new Callback(callback);
>>>>>>> 54b6cfa... Initial Contribution
                binder.linkToDeath(cb, 0);
                mCallbacks.put(binder, cb);
                return true;
            } catch (RemoteException e) {
                return false;
            }
        }
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Remove from the list a callback that was previously added with
     * {@link #register}.  This uses the
     * {@link IInterface#asBinder callback.asBinder()} object to correctly
     * find the previous registration.
     * Registrations are not counted; a single unregister call will remove
     * a callback after any number calls to {@link #register} for it.
<<<<<<< HEAD
     *
     * @param callback The callback to be removed from the list.  Passing
     * null here will cause a NullPointerException, so you will generally want
     * to check for null before calling.
     *
     * @return Returns true if the callback was found and unregistered.  Returns
     * false if the given callback was not found on the list.
     *
=======
     * 
     * @param callback The callback to be removed from the list.  Passing
     * null here will cause a NullPointerException, so you will generally want
     * to check for null before calling.
     * 
     * @return Returns true if the callback was found and unregistered.  Returns
     * false if the given callback was not found on the list.
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * @see #register
     */
    public boolean unregister(E callback) {
        synchronized (mCallbacks) {
            Callback cb = mCallbacks.remove(callback.asBinder());
            if (cb != null) {
                cb.mCallback.asBinder().unlinkToDeath(cb, 0);
                return true;
            }
            return false;
        }
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Disable this callback list.  All registered callbacks are unregistered,
     * and the list is disabled so that future calls to {@link #register} will
     * fail.  This should be used when a Service is stopping, to prevent clients
     * from registering callbacks after it is stopped.
<<<<<<< HEAD
     *
=======
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * @see #register
     */
    public void kill() {
        synchronized (mCallbacks) {
            for (Callback cb : mCallbacks.values()) {
                cb.mCallback.asBinder().unlinkToDeath(cb, 0);
            }
            mCallbacks.clear();
            mKilled = true;
        }
    }
<<<<<<< HEAD

    /**
     * Old version of {@link #onCallbackDied(E, Object)} that
     * does not provide a cookie.
     */
    public void onCallbackDied(E callback) {
    }
    
    /**
     * Called when the process hosting a callback in the list has gone away.
     * The default implementation calls {@link #onCallbackDied(E)}
     * for backwards compatibility.
=======
    
    /**
     * Called when the process hosting a callback in the list has gone away.
     * The default implementation does nothing.
>>>>>>> 54b6cfa... Initial Contribution
     * 
     * @param callback The callback whose process has died.  Note that, since
     * its process has died, you can not make any calls on to this interface.
     * You can, however, retrieve its IBinder and compare it with another
     * IBinder to see if it is the same object.
<<<<<<< HEAD
     * @param cookie The cookie object original provided to
     * {@link #register(E, Object)}.
     * 
     * @see #register
     */
    public void onCallbackDied(E callback, Object cookie) {
        onCallbackDied(callback);
    }

=======
     * 
     * @see #register
     */
    public void onCallbackDied(E callback) {
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Prepare to start making calls to the currently registered callbacks.
     * This creates a copy of the callback list, which you can retrieve items
     * from using {@link #getBroadcastItem}.  Note that only one broadcast can
     * be active at a time, so you must be sure to always call this from the
<<<<<<< HEAD
     * same thread (usually by scheduling with {@link Handler}) or
     * do your own synchronization.  You must call {@link #finishBroadcast}
     * when done.
     *
     * <p>A typical loop delivering a broadcast looks like this:
     *
     * <pre>
     * int i = callbacks.beginBroadcast();
     * while (i &gt; 0) {
     *     i--;
=======
     * same thread (usually by scheduling with {@link Handler} or
     * do your own synchronization.  You must call {@link #finishBroadcast}
     * when done.
     * 
     * <p>A typical loop delivering a broadcast looks like this:
     * 
     * <pre>
     * final int N = callbacks.beginBroadcast();
     * for (int i=0; i<N; i++) {
>>>>>>> 54b6cfa... Initial Contribution
     *     try {
     *         callbacks.getBroadcastItem(i).somethingHappened();
     *     } catch (RemoteException e) {
     *         // The RemoteCallbackList will take care of removing
     *         // the dead object for us.
     *     }
     * }
     * callbacks.finishBroadcast();</pre>
<<<<<<< HEAD
     *
     * @return Returns the number of callbacks in the broadcast, to be used
     * with {@link #getBroadcastItem} to determine the range of indices you
     * can supply.
     *
=======
     * 
     * @return Returns the number of callbacks in the broadcast, to be used
     * with {@link #getBroadcastItem} to determine the range of indices you
     * can supply.
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * @see #getBroadcastItem
     * @see #finishBroadcast
     */
    public int beginBroadcast() {
        synchronized (mCallbacks) {
<<<<<<< HEAD
            if (mBroadcastCount > 0) {
                throw new IllegalStateException(
                        "beginBroadcast() called while already in a broadcast");
            }
            
            final int N = mBroadcastCount = mCallbacks.size();
            if (N <= 0) {
                return 0;
            }
            Object[] active = mActiveBroadcast;
            if (active == null || active.length < N) {
                mActiveBroadcast = active = new Object[N];
            }
            int i=0;
            for (Callback cb : mCallbacks.values()) {
                active[i++] = cb;
            }
            return i;
        }
    }

=======
            final int N = mCallbacks.size();
            if (N <= 0) {
                return 0;
            }
            IInterface[] active = mActiveBroadcast;
            if (active == null || active.length < N) {
                mActiveBroadcast = active = new IInterface[N];
            }
            int i=0;
            for (Callback cb : mCallbacks.values()) {
                active[i++] = cb.mCallback;
            }
            return N;
        }
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Retrieve an item in the active broadcast that was previously started
     * with {@link #beginBroadcast}.  This can <em>only</em> be called after
     * the broadcast is started, and its data is no longer valid after
     * calling {@link #finishBroadcast}.
<<<<<<< HEAD
     *
=======
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * <p>Note that it is possible for the process of one of the returned
     * callbacks to go away before you call it, so you will need to catch
     * {@link RemoteException} when calling on to the returned object.
     * The callback list itself, however, will take care of unregistering
     * these objects once it detects that it is no longer valid, so you can
     * handle such an exception by simply ignoring it.
<<<<<<< HEAD
     *
     * @param index Which of the registered callbacks you would like to
     * retrieve.  Ranges from 0 to 1-{@link #beginBroadcast}.
     *
     * @return Returns the callback interface that you can call.  This will
     * always be non-null.
     *
     * @see #beginBroadcast
     */
    public E getBroadcastItem(int index) {
        return ((Callback)mActiveBroadcast[index]).mCallback;
    }
    
    /**
     * Retrieve the cookie associated with the item
     * returned by {@link #getBroadcastItem(int)}.
     * 
     * @see #getBroadcastItem
     */
    public Object getBroadcastCookie(int index) {
        return ((Callback)mActiveBroadcast[index]).mCookie;
    }

    /**
     * Clean up the state of a broadcast previously initiated by calling
     * {@link #beginBroadcast}.  This must always be called when you are done
     * with a broadcast.
     *
     * @see #beginBroadcast
     */
    public void finishBroadcast() {
        if (mBroadcastCount < 0) {
            throw new IllegalStateException(
                    "finishBroadcast() called outside of a broadcast");
        }
        
        Object[] active = mActiveBroadcast;
        if (active != null) {
            final int N = mBroadcastCount;
=======
     * 
     * @param index Which of the registered callbacks you would like to
     * retrieve.  Ranges from 0 to 1-{@link #beginBroadcast}.
     * 
     * @return Returns the callback interface that you can call.  This will
     * always be non-null.
     * 
     * @see #beginBroadcast
     */
    public E getBroadcastItem(int index) {
        return (E)mActiveBroadcast[index];
    }
    
    /**
     * Clean up the state of a broadcast previously initiated by calling
     * {@link #beginBroadcast}.  This must always be called when you are done
     * with a broadcast.
     * 
     * @see #beginBroadcast
     */
    public void finishBroadcast() {
        IInterface[] active = mActiveBroadcast;
        if (active != null) {
            final int N = active.length;
>>>>>>> 54b6cfa... Initial Contribution
            for (int i=0; i<N; i++) {
                active[i] = null;
            }
        }
<<<<<<< HEAD
        
        mBroadcastCount = -1;
=======
>>>>>>> 54b6cfa... Initial Contribution
    }
}
