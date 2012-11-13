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

package android.os;

<<<<<<< HEAD
import android.util.Log;
import android.util.Printer;
import android.util.PrefixPrinter;
=======
import android.util.Config;
import android.util.Printer;
>>>>>>> 54b6cfa... Initial Contribution

/**
  * Class used to run a message loop for a thread.  Threads by default do
  * not have a message loop associated with them; to create one, call
  * {@link #prepare} in the thread that is to run the loop, and then
  * {@link #loop} to have it process messages until the loop is stopped.
  * 
  * <p>Most interaction with a message loop is through the
  * {@link Handler} class.
  * 
  * <p>This is a typical example of the implementation of a Looper thread,
  * using the separation of {@link #prepare} and {@link #loop} to create an
  * initial Handler to communicate with the Looper.
<<<<<<< HEAD
  *
  * <pre>
  *  class LooperThread extends Thread {
  *      public Handler mHandler;
  *
  *      public void run() {
  *          Looper.prepare();
  *
=======
  * 
  * <pre>
  *  class LooperThread extends Thread {
  *      public Handler mHandler;
  *      
  *      public void run() {
  *          Looper.prepare();
  *          
>>>>>>> 54b6cfa... Initial Contribution
  *          mHandler = new Handler() {
  *              public void handleMessage(Message msg) {
  *                  // process incoming messages here
  *              }
  *          };
<<<<<<< HEAD
  *
=======
  *          
>>>>>>> 54b6cfa... Initial Contribution
  *          Looper.loop();
  *      }
  *  }</pre>
  */
public class Looper {
<<<<<<< HEAD
    private static final String TAG = "Looper";

    // sThreadLocal.get() will return null unless you've called prepare().
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    private static Looper sMainLooper;  // guarded by Looper.class

    final MessageQueue mQueue;
    final Thread mThread;
    volatile boolean mRun;

    private Printer mLogging;

=======
    private static final boolean DEBUG = false;
    private static final boolean localLOGV = DEBUG ? Config.LOGD : Config.LOGV;

    // sThreadLocal.get() will return null unless you've called prepare().
    private static final ThreadLocal sThreadLocal = new ThreadLocal();

    final MessageQueue mQueue;
    volatile boolean mRun;
    Thread mThread;
    private Printer mLogging = null;
    private static Looper mMainLooper = null;
    
>>>>>>> 54b6cfa... Initial Contribution
     /** Initialize the current thread as a looper.
      * This gives you a chance to create handlers that then reference
      * this looper, before actually starting the loop. Be sure to call
      * {@link #loop()} after calling this method, and end it by calling
      * {@link #quit()}.
      */
<<<<<<< HEAD
    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }

    /**
     * Initialize the current thread as a looper, marking it as an
     * application's main looper. The main looper for your application
     * is created by the Android environment, so you should never need
     * to call this function yourself.  See also: {@link #prepare()}
     */
    public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    /** Returns the application's main looper, which lives in the main thread of the application.
     */
    public static Looper getMainLooper() {
        synchronized (Looper.class) {
            return sMainLooper;
        }
    }

    /**
     * Run the message queue in this thread. Be sure to call
     * {@link #quit()} to end the loop.
     */
    public static void loop() {
        final Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        // Make sure the identity of this thread is that of the local process,
        // and keep track of what that identity token actually is.
        Binder.clearCallingIdentity();
        final long ident = Binder.clearCallingIdentity();

        for (;;) {
            Message msg = queue.next(); // might block
            if (msg == null) {
                // No message indicates that the message queue is quitting.
                return;
            }

            // This must be in a local variable, in case a UI event sets the logger
            Printer logging = me.mLogging;
            if (logging != null) {
                logging.println(">>>>> Dispatching to " + msg.target + " " +
                        msg.callback + ": " + msg.what);
            }

            msg.target.dispatchMessage(msg);

            if (logging != null) {
                logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
            }

            // Make sure that during the course of dispatching the
            // identity of the thread wasn't corrupted.
            final long newIdent = Binder.clearCallingIdentity();
            if (ident != newIdent) {
                Log.wtf(TAG, "Thread identity changed from 0x"
                        + Long.toHexString(ident) + " to 0x"
                        + Long.toHexString(newIdent) + " while dispatching to "
                        + msg.target.getClass().getName() + " "
                        + msg.callback + " what=" + msg.what);
            }

            msg.recycle();
=======
    public static final void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }
    
    /** Initialize the current thread as a looper, marking it as an application's main 
     *  looper. The main looper for your application is created by the Android environment,
     *  so you should never need to call this function yourself.
     * {@link #prepare()}
     */
     
    public static final void prepareMainLooper() {
        prepare();
        setMainLooper(myLooper());
        if (Process.supportsProcesses()) {
            myLooper().mQueue.mQuitAllowed = false;
        }
    }

    private synchronized static void setMainLooper(Looper looper) {
        mMainLooper = looper;
    }
    
    /** Returns the application's main looper, which lives in the main thread of the application.
     */
    public synchronized static final Looper getMainLooper() {
        return mMainLooper;
    }

    /**
     *  Run the message queue in this thread. Be sure to call
     * {@link #quit()} to end the loop.
     */
    public static final void loop() {
        Looper me = myLooper();
        MessageQueue queue = me.mQueue;
        while (true) {
            Message msg = queue.next(); // might block
            //if (!me.mRun) {
            //    break;
            //}
            if (msg != null) {
                if (msg.target == null) {
                    // No target is a magic identifier for the quit message.
                    return;
                }
                if (me.mLogging!= null) me.mLogging.println(
                        ">>>>> Dispatching to " + msg.target + " "
                        + msg.callback + ": " + msg.what
                        );
                msg.target.dispatchMessage(msg);
                if (me.mLogging!= null) me.mLogging.println(
                        "<<<<< Finished to    " + msg.target + " "
                        + msg.callback);
                msg.recycle();
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Return the Looper object associated with the current thread.  Returns
     * null if the calling thread is not associated with a Looper.
     */
    public static Looper myLooper() {
        return sThreadLocal.get();
=======
     * Return the Looper object associated with the current thread.
     */
    public static final Looper myLooper() {
        return (Looper)sThreadLocal.get();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Control logging of messages as they are processed by this Looper.  If
     * enabled, a log message will be written to <var>printer</var> 
     * at the beginning and ending of each message dispatch, identifying the
     * target Handler and message contents.
     * 
     * @param printer A Printer object that will receive log messages, or
     * null to disable message logging.
     */
    public void setMessageLogging(Printer printer) {
        mLogging = printer;
    }
    
    /**
     * Return the {@link MessageQueue} object associated with the current
<<<<<<< HEAD
     * thread.  This must be called from a thread running a Looper, or a
     * NullPointerException will be thrown.
     */
    public static MessageQueue myQueue() {
        return myLooper().mQueue;
    }

    private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
=======
     * thread.
     */
    public static final MessageQueue myQueue() {
        return myLooper().mQueue;
    }

    private Looper() {
        mQueue = new MessageQueue();
>>>>>>> 54b6cfa... Initial Contribution
        mRun = true;
        mThread = Thread.currentThread();
    }

<<<<<<< HEAD
    /**
     * Quits the looper.
     *
     * Causes the {@link #loop} method to terminate as soon as possible.
     */
    public void quit() {
        mQueue.quit();
    }

    /**
     * Posts a synchronization barrier to the Looper's message queue.
     *
     * Message processing occurs as usual until the message queue encounters the
     * synchronization barrier that has been posted.  When the barrier is encountered,
     * later synchronous messages in the queue are stalled (prevented from being executed)
     * until the barrier is released by calling {@link #removeSyncBarrier} and specifying
     * the token that identifies the synchronization barrier.
     *
     * This method is used to immediately postpone execution of all subsequently posted
     * synchronous messages until a condition is met that releases the barrier.
     * Asynchronous messages (see {@link Message#isAsynchronous} are exempt from the barrier
     * and continue to be processed as usual.
     *
     * This call must be always matched by a call to {@link #removeSyncBarrier} with
     * the same token to ensure that the message queue resumes normal operation.
     * Otherwise the application will probably hang!
     *
     * @return A token that uniquely identifies the barrier.  This token must be
     * passed to {@link #removeSyncBarrier} to release the barrier.
     *
     * @hide
     */
    public final int postSyncBarrier() {
        return mQueue.enqueueSyncBarrier(SystemClock.uptimeMillis());
    }


    /**
     * Removes a synchronization barrier.
     *
     * @param token The synchronization barrier token that was returned by
     * {@link #postSyncBarrier}.
     *
     * @throws IllegalStateException if the barrier was not found.
     *
     * @hide
     */
    public final void removeSyncBarrier(int token) {
        mQueue.removeSyncBarrier(token);
    }

    /**
     * Return the Thread associated with this Looper.
     */
    public Thread getThread() {
        return mThread;
    }

    /** @hide */
    public MessageQueue getQueue() {
        return mQueue;
    }

    public void dump(Printer pw, String prefix) {
        pw = PrefixPrinter.create(pw, prefix);
        pw.println(this.toString());
        pw.println("mRun=" + mRun);
        pw.println("mThread=" + mThread);
        pw.println("mQueue=" + ((mQueue != null) ? mQueue : "(null"));
        if (mQueue != null) {
            synchronized (mQueue) {
                long now = SystemClock.uptimeMillis();
                Message msg = mQueue.mMessages;
                int n = 0;
                while (msg != null) {
                    pw.println("  Message " + n + ": " + msg.toString(now));
                    n++;
                    msg = msg.next;
                }
                pw.println("(Total messages: " + n + ")");
=======
    public void quit() {
        Message msg = Message.obtain();
        // NOTE: By enqueueing directly into the message queue, the
        // message is left with a null target.  This is how we know it is
        // a quit message.
        mQueue.enqueueMessage(msg, 0);
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + this);
        pw.println(prefix + "mRun=" + mRun);
        pw.println(prefix + "mThread=" + mThread);
        pw.println(prefix + "mQueue=" + ((mQueue != null) ? mQueue : "(null"));
        if (mQueue != null) {
            synchronized (mQueue) {
                Message msg = mQueue.mMessages;
                int n = 0;
                while (msg != null) {
                    pw.println(prefix + "  Message " + n + ": " + msg);
                    n++;
                    msg = msg.next;
                }
                pw.println(prefix + "(Total messages: " + n + ")");
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }

    public String toString() {
<<<<<<< HEAD
        return "Looper{" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }
}
=======
        return "Looper{"
            + Integer.toHexString(System.identityHashCode(this))
            + "}";
    }

    static class HandlerException extends Exception {

        HandlerException(Message message, Throwable cause) {
            super(createMessage(cause), cause);
        }

        static String createMessage(Throwable cause) {
            String causeMsg = cause.getMessage();
            if (causeMsg == null) {
                causeMsg = cause.toString();
            }
            return causeMsg;
        }
    }
}

>>>>>>> 54b6cfa... Initial Contribution
