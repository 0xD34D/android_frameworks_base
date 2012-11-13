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

package android.webkit;

<<<<<<< HEAD
import android.app.ActivityManager;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.net.WebAddress;
import android.net.http.ErrorStrings;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Surface;
import android.view.ViewRootImpl;
import android.view.WindowManager;

import junit.framework.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.nio.charset.Charsets;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.harmony.security.provider.cert.X509CertImpl;
import org.apache.harmony.xnet.provider.jsse.OpenSSLDSAPrivateKey;
import org.apache.harmony.xnet.provider.jsse.OpenSSLRSAPrivateKey;
=======
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.WebAddress;
import android.net.http.SslCertificate;
import android.os.Handler;
import android.os.Message;
import android.util.Config;
import android.util.Log;

import junit.framework.Assert;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
>>>>>>> 54b6cfa... Initial Contribution

class BrowserFrame extends Handler {

    private static final String LOGTAG = "webkit";

    /**
     * Cap the number of LoadListeners that will be instantiated, so
     * we don't blow the GREF count.  Attempting to queue more than
     * this many requests will prompt an error() callback on the
     * request's LoadListener
     */
    private final static int MAX_OUTSTANDING_REQUESTS = 300;

    private final CallbackProxy mCallbackProxy;
<<<<<<< HEAD
    private final WebSettingsClassic mSettings;
    private final Context mContext;
    private final WebViewCore mWebViewCore;
    /* package */ boolean mLoadInitFromJava;
    private int mLoadType;
    private boolean mFirstLayoutDone = true;
    private boolean mCommitted = true;
    // Flag for blocking messages. This is used during destroy() so
    // that if the UI thread posts any messages after the message
    // queue has been cleared,they are ignored.
    private boolean mBlockMessages = false;
    private int mOrientation = -1;
=======
    private final WebSettings mSettings;
    private final Context mContext;
    private final WebViewDatabase mDatabase;
    private final WebViewCore mWebViewCore;
    private boolean mLoadInitFromJava;
    private String mCurrentUrl;
    private int mLoadType;
    private String mCompletedUrl;
    private boolean mFirstLayoutDone = true;
    private boolean mCommitted = true;
>>>>>>> 54b6cfa... Initial Contribution

    // Is this frame the main frame?
    private boolean mIsMainFrame;

    // Attached Javascript interfaces
<<<<<<< HEAD
    private Map<String, Object> mJavaScriptObjects;
    private Set<Object> mRemovedJavaScriptObjects;

    // Key store handler when Chromium HTTP stack is used.
    private KeyStoreHandler mKeyStoreHandler = null;

    // Implementation of the searchbox API.
    private final SearchBoxImpl mSearchBox;
=======
    private HashMap mJSInterfaceMap;
>>>>>>> 54b6cfa... Initial Contribution

    // message ids
    // a message posted when a frame loading is completed
    static final int FRAME_COMPLETED = 1001;
<<<<<<< HEAD
    // orientation change message
    static final int ORIENTATION_CHANGED = 1002;
=======
>>>>>>> 54b6cfa... Initial Contribution
    // a message posted when the user decides the policy
    static final int POLICY_FUNCTION = 1003;

    // Note: need to keep these in sync with FrameLoaderTypes.h in native
    static final int FRAME_LOADTYPE_STANDARD = 0;
    static final int FRAME_LOADTYPE_BACK = 1;
    static final int FRAME_LOADTYPE_FORWARD = 2;
    static final int FRAME_LOADTYPE_INDEXEDBACKFORWARD = 3;
    static final int FRAME_LOADTYPE_RELOAD = 4;
    static final int FRAME_LOADTYPE_RELOADALLOWINGSTALEDATA = 5;
    static final int FRAME_LOADTYPE_SAME = 6;
    static final int FRAME_LOADTYPE_REDIRECT = 7;
    static final int FRAME_LOADTYPE_REPLACE = 8;

    // A progress threshold to switch from history Picture to live Picture
    private static final int TRANSITION_SWITCH_THRESHOLD = 75;

    // This is a field accessed by native code as well as package classes.
    /*package*/ int mNativeFrame;

    // Static instance of a JWebCoreJavaBridge to handle timer and cookie
    // requests from WebCore.
    static JWebCoreJavaBridge sJavaBridge;

<<<<<<< HEAD
    private static class ConfigCallback implements ComponentCallbacks {
        private final ArrayList<WeakReference<Handler>> mHandlers =
                new ArrayList<WeakReference<Handler>>();
        private final WindowManager mWindowManager;

        ConfigCallback(WindowManager wm) {
            mWindowManager = wm;
        }

        public synchronized void addHandler(Handler h) {
            // No need to ever remove a Handler. If the BrowserFrame is
            // destroyed, it will be collected and the WeakReference set to
            // null. If it happens to still be around during a configuration
            // change, the message will be ignored.
            mHandlers.add(new WeakReference<Handler>(h));
        }

        public void onConfigurationChanged(Configuration newConfig) {
            if (mHandlers.size() == 0) {
                return;
            }
            int orientation =
                    mWindowManager.getDefaultDisplay().getOrientation();
            switch (orientation) {
                case Surface.ROTATION_90:
                    orientation = 90;
                    break;
                case Surface.ROTATION_180:
                    orientation = 180;
                    break;
                case Surface.ROTATION_270:
                    orientation = -90;
                    break;
                case Surface.ROTATION_0:
                    orientation = 0;
                    break;
                default:
                    break;
            }
            synchronized (this) {
                // Create a list of handlers to remove. Go ahead and make it
                // the same size to avoid resizing.
                ArrayList<WeakReference> handlersToRemove =
                        new ArrayList<WeakReference>(mHandlers.size());
                for (WeakReference<Handler> wh : mHandlers) {
                    Handler h = wh.get();
                    if (h != null) {
                        h.sendMessage(h.obtainMessage(ORIENTATION_CHANGED,
                                    orientation, 0));
                    } else {
                        handlersToRemove.add(wh);
                    }
                }
                // Now remove all the null references.
                for (WeakReference weak : handlersToRemove) {
                    mHandlers.remove(weak);
                }
            }
        }

        public void onLowMemory() {}
    }
    static ConfigCallback sConfigCallback;

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Create a new BrowserFrame to be used in an application.
     * @param context An application context to use when retrieving assets.
     * @param w A WebViewCore used as the view for this frame.
     * @param proxy A CallbackProxy for posting messages to the UI thread and
     *              querying a client for information.
     * @param settings A WebSettings object that holds all settings.
     * XXX: Called by WebCore thread.
     */
    public BrowserFrame(Context context, WebViewCore w, CallbackProxy proxy,
<<<<<<< HEAD
            WebSettingsClassic settings, Map<String, Object> javascriptInterfaces) {

        Context appContext = context.getApplicationContext();

=======
            WebSettings settings) {
>>>>>>> 54b6cfa... Initial Contribution
        // Create a global JWebCoreJavaBridge to handle timers and
        // cookies in the WebCore thread.
        if (sJavaBridge == null) {
            sJavaBridge = new JWebCoreJavaBridge();
            // set WebCore native cache size
<<<<<<< HEAD
            ActivityManager am = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            if (am.getMemoryClass() > 16) {
                sJavaBridge.setCacheSize(8 * 1024 * 1024);
            } else {
                sJavaBridge.setCacheSize(4 * 1024 * 1024);
            }
            // initialize CacheManager
            CacheManager.init(appContext);
            // create CookieSyncManager with current Context
            CookieSyncManager.createInstance(appContext);
            // create PluginManager with current Context
            PluginManager.getInstance(appContext);
        }

        if (sConfigCallback == null) {
            sConfigCallback = new ConfigCallback(
                    (WindowManager) appContext.getSystemService(
                            Context.WINDOW_SERVICE));
            ViewRootImpl.addConfigCallback(sConfigCallback);
        }
        sConfigCallback.addHandler(this);

        mJavaScriptObjects = javascriptInterfaces;
        if (mJavaScriptObjects == null) {
            mJavaScriptObjects = new HashMap<String, Object>();
        }
        mRemovedJavaScriptObjects = new HashSet<Object>();
=======
            sJavaBridge.setCacheSize(4 * 1024 * 1024);
            // initialize CacheManager
            CacheManager.init(context);
            // create CookieSyncManager with current Context
            CookieSyncManager.createInstance(context);
        }
        AssetManager am = context.getAssets();
        nativeCreateFrame(am, proxy.getBackForwardList());
        // Create a native FrameView and attach it to the native frame.
        nativeCreateView(w);
>>>>>>> 54b6cfa... Initial Contribution

        mSettings = settings;
        mContext = context;
        mCallbackProxy = proxy;
<<<<<<< HEAD
        mWebViewCore = w;

        mSearchBox = new SearchBoxImpl(mWebViewCore, mCallbackProxy);
        mJavaScriptObjects.put(SearchBoxImpl.JS_INTERFACE_NAME, mSearchBox);

        AssetManager am = context.getAssets();
        nativeCreateFrame(w, am, proxy.getBackForwardList());

        if (DebugFlags.BROWSER_FRAME) {
=======
        mDatabase = WebViewDatabase.getInstance(context);
        mWebViewCore = w;

        if (Config.LOGV) {
>>>>>>> 54b6cfa... Initial Contribution
            Log.v(LOGTAG, "BrowserFrame constructor: this=" + this);
        }
    }

    /**
     * Load a url from the network or the filesystem into the main frame.
<<<<<<< HEAD
     * Following the same behaviour as Safari, javascript: URLs are not passed
     * to the main frame, instead they are evaluated immediately.
     * @param url The url to load.
     * @param extraHeaders The extra headers sent with this url. This should not
     *            include the common headers like "user-agent". If it does, it
     *            will be replaced by the intrinsic value of the WebView.
     */
    public void loadUrl(String url, Map<String, String> extraHeaders) {
=======
     * Following the same behaviour as Safari, javascript: URLs are not
     * passed to the main frame, instead they are evaluated immediately.
     * @param url The url to load.
     */
    public void loadUrl(String url) {
>>>>>>> 54b6cfa... Initial Contribution
        mLoadInitFromJava = true;
        if (URLUtil.isJavaScriptUrl(url)) {
            // strip off the scheme and evaluate the string
            stringByEvaluatingJavaScriptFromString(
                    url.substring("javascript:".length()));
        } else {
<<<<<<< HEAD
            nativeLoadUrl(url, extraHeaders);
=======
            if (!nativeLoadUrl(url)) {
                reportError(android.net.http.EventHandler.ERROR_BAD_URL,
                        mContext.getString(com.android.internal.R.string.httpErrorBadUrl),
                        url);
            }
>>>>>>> 54b6cfa... Initial Contribution
        }
        mLoadInitFromJava = false;
    }

    /**
<<<<<<< HEAD
     * Load a url with "POST" method from the network into the main frame.
     * @param url The url to load.
     * @param data The data for POST request.
     */
    public void postUrl(String url, byte[] data) {
        mLoadInitFromJava = true;
        nativePostUrl(url, data);
        mLoadInitFromJava = false;
    }

    /**
     * Load the content as if it was loaded by the provided base URL. The
     * historyUrl is used as the history entry for the load data.
=======
     * Load the content as if it was loaded by the provided base URL. The
     * failUrl is used as the history entry for the load data. If null or
     * an empty string is passed for the failUrl, then no history entry is
     * created.
>>>>>>> 54b6cfa... Initial Contribution
     * 
     * @param baseUrl Base URL used to resolve relative paths in the content
     * @param data Content to render in the browser
     * @param mimeType Mimetype of the data being passed in
     * @param encoding Character set encoding of the provided data.
<<<<<<< HEAD
     * @param historyUrl URL to use as the history entry.
     */
    public void loadData(String baseUrl, String data, String mimeType,
            String encoding, String historyUrl) {
        mLoadInitFromJava = true;
        if (historyUrl == null || historyUrl.length() == 0) {
            historyUrl = "about:blank";
=======
     * @param failUrl URL to use if the content fails to load or null.
     */
    public void loadData(String baseUrl, String data, String mimeType,
            String encoding, String failUrl) {
        mLoadInitFromJava = true;
        if (failUrl == null) {
            failUrl = "";
>>>>>>> 54b6cfa... Initial Contribution
        }
        if (data == null) {
            data = "";
        }
        
        // Setup defaults for missing values. These defaults where taken from
        // WebKit's WebFrame.mm
        if (baseUrl == null || baseUrl.length() == 0) {
            baseUrl = "about:blank";
        }
        if (mimeType == null || mimeType.length() == 0) {
            mimeType = "text/html";
        }
<<<<<<< HEAD
        nativeLoadData(baseUrl, data, mimeType, encoding, historyUrl);
        mLoadInitFromJava = false;
    }

    /**
     * Saves the contents of the frame as a web archive.
     *
     * @param basename The filename where the archive should be placed.
     * @param autoname If false, takes filename to be a file. If true, filename
     *                 is assumed to be a directory in which a filename will be
     *                 chosen according to the url of the current page.
     */
    /* package */ String saveWebArchive(String basename, boolean autoname) {
        return nativeSaveWebArchive(basename, autoname);
    }

    /**
     * Go back or forward the number of steps given.
     * @param steps A negative or positive number indicating the direction
     *              and number of steps to move.
     */
    public void goBackOrForward(int steps) {
        mLoadInitFromJava = true;
        nativeGoBackOrForward(steps);
=======
        nativeLoadData(baseUrl, data, mimeType, encoding, failUrl);
>>>>>>> 54b6cfa... Initial Contribution
        mLoadInitFromJava = false;
    }

    /**
     * native callback
     * Report an error to an activity.
     * @param errorCode The HTTP error code.
<<<<<<< HEAD
     * @param description Optional human-readable description. If no description
     *     is given, we'll use a standard localized error message.
     * @param failingUrl The URL that was being loaded when the error occurred.
=======
     * @param description A String description.
>>>>>>> 54b6cfa... Initial Contribution
     * TODO: Report all errors including resource errors but include some kind
     * of domain identifier. Change errorCode to an enum for a cleaner
     * interface.
     */
<<<<<<< HEAD
    private void reportError(int errorCode, String description, String failingUrl) {
        // As this is called for the main resource and loading will be stopped
        // after, reset the state variables.
        resetLoadingStates();
        if (description == null || description.isEmpty()) {
            description = ErrorStrings.getString(errorCode, mContext);
        }
        mCallbackProxy.onReceivedError(errorCode, description, failingUrl);
    }

    private void resetLoadingStates() {
        mCommitted = true;
        mFirstLayoutDone = true;
=======
    private void reportError(final int errorCode, final String description,
            final String failingUrl) {
        // As this is called for the main resource and loading will be stopped
        // after, reset the state variables.
        mCommitted = true;
        mWebViewCore.mEndScaleZoom = mFirstLayoutDone == false;
        mFirstLayoutDone = true;
        mCallbackProxy.onReceivedError(errorCode, description, failingUrl);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /* package */boolean committed() {
        return mCommitted;
    }

    /* package */boolean firstLayoutDone() {
        return mFirstLayoutDone;
    }

    /* package */int loadType() {
        return mLoadType;
    }

<<<<<<< HEAD
    /* package */void didFirstLayout() {
        if (!mFirstLayoutDone) {
            mFirstLayoutDone = true;
            // ensure {@link WebViewCore#webkitDraw} is called as we were
            // blocking the update in {@link #loadStarted}
            mWebViewCore.contentDraw();
        }
=======
    /* package */String currentUrl() {
        return mCurrentUrl;
    }

    /* package */void didFirstLayout(String url) {
        // this is common case
        if (url.equals(mCurrentUrl)) {
            if (!mFirstLayoutDone) {
                mFirstLayoutDone = true;
                // ensure {@link WebViewCore#webkitDraw} is called as we were
                // blocking the update in {@link #loadStarted}
                mWebViewCore.contentInvalidate();
            }
        } else if (url.equals(mCompletedUrl)) {
            /*
             * FIXME: when loading http://www.google.com/m, 
             * mCurrentUrl will be http://www.google.com/m, 
             * mCompletedUrl will be http://www.google.com/m#search 
             * and url will be http://www.google.com/m#search. 
             * This is probably a bug in WebKit. If url matches mCompletedUrl, 
             * also set mFirstLayoutDone to be true and update.
             */
            if (!mFirstLayoutDone) {
                mFirstLayoutDone = true;
                // ensure {@link WebViewCore#webkitDraw} is called as we were
                // blocking the update in {@link #loadStarted}
                mWebViewCore.contentInvalidate();
            }
        }
        mWebViewCore.mEndScaleZoom = true;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * native callback
     * Indicates the beginning of a new load.
     * This method will be called once for the main frame.
     */
    private void loadStarted(String url, Bitmap favicon, int loadType,
            boolean isMainFrame) {
        mIsMainFrame = isMainFrame;

        if (isMainFrame || loadType == FRAME_LOADTYPE_STANDARD) {
<<<<<<< HEAD
=======
            mCurrentUrl = url;
            mCompletedUrl = null;
>>>>>>> 54b6cfa... Initial Contribution
            mLoadType = loadType;

            if (isMainFrame) {
                // Call onPageStarted for main frames.
                mCallbackProxy.onPageStarted(url, favicon);
                // as didFirstLayout() is only called for the main frame, reset 
                // mFirstLayoutDone only for the main frames
                mFirstLayoutDone = false;
                mCommitted = false;
                // remove pending draw to block update until mFirstLayoutDone is
                // set to true in didFirstLayout()
<<<<<<< HEAD
                mWebViewCore.clearContent();
                mWebViewCore.removeMessages(WebViewCore.EventHub.WEBKIT_DRAW);
            }
        }
    }

    @SuppressWarnings("unused")
    private void saveFormData(HashMap<String, String> data) {
        if (mSettings.getSaveFormData()) {
            final WebHistoryItem h = mCallbackProxy.getBackForwardList()
                    .getCurrentItem();
            if (h != null) {
                String url = WebTextView.urlForAutoCompleteData(h.getUrl());
                if (url != null) {
                    WebViewDatabaseClassic.getInstance(mContext).setFormData(
                            url, data);
=======
                mWebViewCore.removeMessages(WebViewCore.EventHub.WEBKIT_DRAW);
            }

            // Note: only saves committed form data in standard load
            if (loadType == FRAME_LOADTYPE_STANDARD
                    && mSettings.getSaveFormData()) {
                final WebHistoryItem h = mCallbackProxy.getBackForwardList()
                        .getCurrentItem();
                if (h != null) {
                    String currentUrl = h.getUrl();
                    if (currentUrl != null) {
                        mDatabase.setFormData(currentUrl, getFormTextData());
                    }
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        }
    }

<<<<<<< HEAD
    @SuppressWarnings("unused")
    private boolean shouldSaveFormData() {
        if (mSettings.getSaveFormData()) {
            final WebHistoryItem h = mCallbackProxy.getBackForwardList()
                    .getCurrentItem();
            return h != null && h.getUrl() != null;
        }
        return false;
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * native callback
     * Indicates the WebKit has committed to the new load
     */
    private void transitionToCommitted(int loadType, boolean isMainFrame) {
        // loadType is not used yet
        if (isMainFrame) {
            mCommitted = true;
<<<<<<< HEAD
            mWebViewCore.getWebViewClassic().mViewManager.postResetStateAll();
=======
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * native callback
     * <p>
     * Indicates the end of a new load.
     * This method will be called once for the main frame.
     */
    private void loadFinished(String url, int loadType, boolean isMainFrame) {
        // mIsMainFrame and isMainFrame are better be equal!!!

        if (isMainFrame || loadType == FRAME_LOADTYPE_STANDARD) {
<<<<<<< HEAD
            if (isMainFrame) {
                resetLoadingStates();
=======
            mCompletedUrl = url;
            if (isMainFrame) {
>>>>>>> 54b6cfa... Initial Contribution
                mCallbackProxy.switchOutDrawHistory();
                mCallbackProxy.onPageFinished(url);
            }
        }
    }

    /**
<<<<<<< HEAD
=======
     * We have received an SSL certificate for the main top-level page.
     *
     * !!!Called from the network thread!!!
     */
    void certificate(SslCertificate certificate) {
        if (mIsMainFrame) {
            // we want to make this call even if the certificate is null
            // (ie, the site is not secure)
            mCallbackProxy.onReceivedCertificate(certificate);
        }
    }

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Destroy all native components of the BrowserFrame.
     */
    public void destroy() {
        nativeDestroyFrame();
<<<<<<< HEAD
        mBlockMessages = true;
=======
>>>>>>> 54b6cfa... Initial Contribution
        removeCallbacksAndMessages(null);
    }

    /**
     * Handle messages posted to us.
     * @param msg The message to handle.
     */
    @Override
    public void handleMessage(Message msg) {
<<<<<<< HEAD
        if (mBlockMessages) {
            return;
        }
        switch (msg.what) {
            case FRAME_COMPLETED: {
                if (mSettings.getSavePassword() && hasPasswordField()) {
                    WebHistoryItem item = mCallbackProxy.getBackForwardList()
                            .getCurrentItem();
                    if (item != null) {
                        WebAddress uri = new WebAddress(item.getUrl());
                        String schemePlusHost = uri.getScheme() + uri.getHost();
                        String[] up =
                                WebViewDatabaseClassic.getInstance(mContext)
                                        .getUsernamePassword(schemePlusHost);
                        if (up != null && up[0] != null) {
                            setUsernamePassword(up[0], up[1]);
                        }
                    }
                }
=======
        switch (msg.what) {
            case FRAME_COMPLETED: {
                if (mSettings.getSavePassword() && hasPasswordField()) {
                    if (Config.DEBUG) {
                        Assert.assertNotNull(mCallbackProxy.getBackForwardList()
                                .getCurrentItem());
                    }
                    WebAddress uri = new WebAddress(
                            mCallbackProxy.getBackForwardList().getCurrentItem()
                            .getUrl());
                    String host = uri.mHost;
                    String[] up = mDatabase.getUsernamePassword(host);
                    if (up != null && up[0] != null) {
                        setUsernamePassword(up[0], up[1]);
                    }
                }
                CacheManager.trimCacheIfNeeded();
>>>>>>> 54b6cfa... Initial Contribution
                break;
            }

            case POLICY_FUNCTION: {
                nativeCallPolicyFunction(msg.arg1, msg.arg2);
                break;
            }

<<<<<<< HEAD
            case ORIENTATION_CHANGED: {
                if (mOrientation != msg.arg1) {
                    mOrientation = msg.arg1;
                    nativeOrientationChanged(msg.arg1);
                }
                break;
            }

=======
>>>>>>> 54b6cfa... Initial Contribution
            default:
                break;
        }
    }

    /**
     * Punch-through for WebCore to set the document
     * title. Inform the Activity of the new title.
     * @param title The new title of the document.
     */
    private void setTitle(String title) {
        // FIXME: The activity must call getTitle (a native method) to get the
        // title. We should try and cache the title if we can also keep it in
        // sync with the document.
        mCallbackProxy.onReceivedTitle(title);
    }

    /**
     * Retrieves the render tree of this frame and puts it as the object for
     * the message and sends the message.
     * @param callback the message to use to send the render tree
     */
    public void externalRepresentation(Message callback) {
        callback.obj = externalRepresentation();;
        callback.sendToTarget();
    }

    /**
     * Return the render tree as a string
     */
    private native String externalRepresentation();

    /**
<<<<<<< HEAD
     * Retrieves the visual text of the frames, puts it as the object for
=======
     * Retrieves the visual text of the current frame, puts it as the object for
>>>>>>> 54b6cfa... Initial Contribution
     * the message and sends the message.
     * @param callback the message to use to send the visual text
     */
    public void documentAsText(Message callback) {
<<<<<<< HEAD
        StringBuilder text = new StringBuilder();
        if (callback.arg1 != 0) {
            // Dump top frame as text.
            text.append(documentAsText());
        }
        if (callback.arg2 != 0) {
            // Dump child frames as text.
            text.append(childFramesAsText());
        }
        callback.obj = text.toString();
=======
        callback.obj = documentAsText();;
>>>>>>> 54b6cfa... Initial Contribution
        callback.sendToTarget();
    }

    /**
     * Return the text drawn on the screen as a string
     */
    private native String documentAsText();

<<<<<<< HEAD
    /**
     * Return the text drawn on the child frames as a string
     */
    private native String childFramesAsText();

=======
>>>>>>> 54b6cfa... Initial Contribution
    /*
     * This method is called by WebCore to inform the frame that
     * the Javascript window object has been cleared.
     * We should re-attach any attached js interfaces.
     */
    private void windowObjectCleared(int nativeFramePointer) {
<<<<<<< HEAD
        Iterator<String> iter = mJavaScriptObjects.keySet().iterator();
        while (iter.hasNext())  {
            String interfaceName = iter.next();
            Object object = mJavaScriptObjects.get(interfaceName);
            if (object != null) {
                nativeAddJavascriptInterface(nativeFramePointer,
                        mJavaScriptObjects.get(interfaceName), interfaceName);
            }
        }
        mRemovedJavaScriptObjects.clear();

        stringByEvaluatingJavaScriptFromString(SearchBoxImpl.JS_BRIDGE);
=======
        if (mJSInterfaceMap != null) {
            Iterator iter = mJSInterfaceMap.keySet().iterator();
            while (iter.hasNext())  {
                String interfaceName = (String) iter.next();
                nativeAddJavascriptInterface(nativeFramePointer,
                        mJSInterfaceMap.get(interfaceName), interfaceName);
            }
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * This method is called by WebCore to check whether application
     * wants to hijack url loading
     */
    public boolean handleUrl(String url) {
        if (mLoadInitFromJava == true) {
            return false;
        }
<<<<<<< HEAD
        if (mCallbackProxy.shouldOverrideUrlLoading(url)) {
            // if the url is hijacked, reset the state of the BrowserFrame
            didFirstLayout();
            return true;
        } else {
            return false;
        }
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        assert obj != null;
        removeJavascriptInterface(interfaceName);

        mJavaScriptObjects.put(interfaceName, obj);
    }

    public void removeJavascriptInterface(String interfaceName) {
        // We keep a reference to the removed object because the native side holds only a weak
        // reference and we need to allow the object to continue to be used until the page has been
        // navigated.
        if (mJavaScriptObjects.containsKey(interfaceName)) {
            mRemovedJavaScriptObjects.add(mJavaScriptObjects.remove(interfaceName));
        }
    }

    /**
     * Called by JNI.  Given a URI, find the associated file and return its size
     * @param uri A String representing the URI of the desired file.
     * @return int The size of the given file.
     */
    private int getFileSize(String uri) {
        int size = 0;
        try {
            InputStream stream = mContext.getContentResolver()
                            .openInputStream(Uri.parse(uri));
            size = stream.available();
            stream.close();
        } catch (Exception e) {}
        return size;
    }

    /**
     * Called by JNI.  Given a URI, a buffer, and an offset into the buffer,
     * copy the resource into buffer.
     * @param uri A String representing the URI of the desired file.
     * @param buffer The byte array to copy the data into.
     * @param offset The offet into buffer to place the data.
     * @param expectedSize The size that the buffer has allocated for this file.
     * @return int The size of the given file, or zero if it fails.
     */
    private int getFile(String uri, byte[] buffer, int offset,
            int expectedSize) {
        int size = 0;
        try {
            InputStream stream = mContext.getContentResolver()
                            .openInputStream(Uri.parse(uri));
            size = stream.available();
            if (size <= expectedSize && buffer != null
                    && buffer.length - offset >= size) {
                stream.read(buffer, offset, size);
            } else {
                size = 0;
            }
            stream.close();
        } catch (java.io.FileNotFoundException e) {
            Log.e(LOGTAG, "FileNotFoundException:" + e);
            size = 0;
        } catch (java.io.IOException e2) {
            Log.e(LOGTAG, "IOException: " + e2);
            size = 0;
        }
        return size;
    }

    /**
     * Get the InputStream for an Android resource
     * There are three different kinds of android resources:
     * - file:///android_res
     * - file:///android_asset
     * - content://
     * @param url The url to load.
     * @return An InputStream to the android resource
     */
    private InputStream inputStreamForAndroidResource(String url) {
        final String ANDROID_ASSET = URLUtil.ASSET_BASE;
        final String ANDROID_RESOURCE = URLUtil.RESOURCE_BASE;
        final String ANDROID_CONTENT = URLUtil.CONTENT_BASE;

        if (url.startsWith(ANDROID_RESOURCE)) {
            url = url.replaceFirst(ANDROID_RESOURCE, "");
            if (url == null || url.length() == 0) {
                Log.e(LOGTAG, "url has length 0 " + url);
                return null;
            }
            int slash = url.indexOf('/');
            int dot = url.indexOf('.', slash);
            if (slash == -1 || dot == -1) {
                Log.e(LOGTAG, "Incorrect res path: " + url);
                return null;
            }
            String subClassName = url.substring(0, slash);
            String fieldName = url.substring(slash + 1, dot);
            String errorMsg = null;
            try {
                final Class<?> d = mContext.getApplicationContext()
                        .getClassLoader().loadClass(
                                mContext.getPackageName() + ".R$"
                                        + subClassName);
                final java.lang.reflect.Field field = d.getField(fieldName);
                final int id = field.getInt(null);
                TypedValue value = new TypedValue();
                mContext.getResources().getValue(id, value, true);
                if (value.type == TypedValue.TYPE_STRING) {
                    return mContext.getAssets().openNonAsset(
                            value.assetCookie, value.string.toString(),
                            AssetManager.ACCESS_STREAMING);
                } else {
                    // Old stack only supports TYPE_STRING for res files
                    Log.e(LOGTAG, "not of type string: " + url);
                    return null;
                }
            } catch (Exception e) {
                Log.e(LOGTAG, "Exception: " + url);
                return null;
            }
        } else if (url.startsWith(ANDROID_ASSET)) {
            url = url.replaceFirst(ANDROID_ASSET, "");
            try {
                AssetManager assets = mContext.getAssets();
                Uri uri = Uri.parse(url);
                return assets.open(uri.getPath(), AssetManager.ACCESS_STREAMING);
            } catch (IOException e) {
                return null;
            }
        } else if (mSettings.getAllowContentAccess() &&
                   url.startsWith(ANDROID_CONTENT)) {
            try {
                // Strip off MIME type. If we don't do this, we can fail to
                // load Gmail attachments, because the URL being loaded doesn't
                // exactly match the URL we have permission to read.
                int mimeIndex = url.lastIndexOf('?');
                if (mimeIndex != -1) {
                    url = url.substring(0, mimeIndex);
                }
                Uri uri = Uri.parse(url);
                return mContext.getContentResolver().openInputStream(uri);
            } catch (Exception e) {
                Log.e(LOGTAG, "Exception: " + url);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * If this looks like a POST request (form submission) containing a username
     * and password, give the user the option of saving them. Will either do
     * nothing, or block until the UI interaction is complete.
     *
     * Called directly by WebKit.
     *
     * @param postData The data about to be sent as the body of a POST request.
     * @param username The username entered by the user (sniffed from the DOM).
     * @param password The password entered by the user (sniffed from the DOM).
     */
    private void maybeSavePassword(
            byte[] postData, String username, String password) {
        if (postData == null
                || username == null || username.isEmpty()
                || password == null || password.isEmpty()) {
            return; // No password to save.
        }

        if (!mSettings.getSavePassword()) {
            return; // User doesn't want to save passwords.
        }

        try {
            if (DebugFlags.BROWSER_FRAME) {
                Assert.assertNotNull(mCallbackProxy.getBackForwardList()
                        .getCurrentItem());
            }
            WebAddress uri = new WebAddress(mCallbackProxy
                    .getBackForwardList().getCurrentItem().getUrl());
            String schemePlusHost = uri.getScheme() + uri.getHost();
            // Check to see if the username & password appear in
            // the post data (there could be another form on the
            // page and that was posted instead.
            String postString = new String(postData);
            WebViewDatabaseClassic db = WebViewDatabaseClassic.getInstance(mContext);
            if (postString.contains(URLEncoder.encode(username)) &&
                    postString.contains(URLEncoder.encode(password))) {
                String[] saved = db.getUsernamePassword(schemePlusHost);
                if (saved != null) {
                    // null username implies that user has chosen not to
                    // save password
                    if (saved[0] != null) {
                        // non-null username implies that user has
                        // chosen to save password, so update the
                        // recorded password
                        db.setUsernamePassword(schemePlusHost, username,
                                password);
                    }
                } else {
                    // CallbackProxy will handle creating the resume
                    // message
                    mCallbackProxy.onSavePassword(schemePlusHost, username,
                            password, null);
                }
            }
        } catch (ParseException ex) {
            // if it is bad uri, don't save its password
        }
    }

    // Called by jni from the chrome network stack.
    private WebResourceResponse shouldInterceptRequest(String url) {
        InputStream androidResource = inputStreamForAndroidResource(url);
        if (androidResource != null) {
            return new WebResourceResponse(null, null, androidResource);
        }

        // Note that we check this after looking for an android_asset or
        // android_res URL, as we allow those even if file access is disabled.
        if (!mSettings.getAllowFileAccess() && url.startsWith("file://")) {
            return new WebResourceResponse(null, null, null);
        }

        WebResourceResponse response = mCallbackProxy.shouldInterceptRequest(url);
        if (response == null && "browser:incognito".equals(url)) {
            try {
                Resources res = mContext.getResources();
                InputStream ins = res.openRawResource(
                        com.android.internal.R.raw.incognito_mode_start_page);
                response = new WebResourceResponse("text/html", "utf8", ins);
            } catch (NotFoundException ex) {
                // This shouldn't happen, but try and gracefully handle it jic
                Log.w(LOGTAG, "Failed opening raw.incognito_mode_start_page", ex);
            }
        }
        return response;
=======
        return mCallbackProxy.shouldOverrideUrlLoading(url);
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        if (mJSInterfaceMap == null) {
            mJSInterfaceMap = new HashMap<String, Object>();
        }
        if (mJSInterfaceMap.containsKey(interfaceName)) {
            mJSInterfaceMap.remove(interfaceName);
        }
        mJSInterfaceMap.put(interfaceName, obj);
    }

    /**
     * Start loading a resource.
     * @param loaderHandle The native ResourceLoader that is the target of the
     *                     data.
     * @param url The url to load.
     * @param method The http method.
     * @param headers The http headers.
     * @param postData If the method is "POST" postData is sent as the request
     *                 body.
     * @param cacheMode The cache mode to use when loading this resource.
     * @param isHighPriority True if this resource needs to be put at the front
     *                       of the network queue.
     * @param synchronous True if the load is synchronous.
     * @return A newly created LoadListener object.
     */
    private LoadListener startLoadingResource(int loaderHandle,
                                              String url,
                                              String method,
                                              HashMap headers,
                                              String postData,
                                              int cacheMode,
                                              boolean isHighPriority,
                                              boolean synchronous) {
        PerfChecker checker = new PerfChecker();

        if (mSettings.getCacheMode() != WebSettings.LOAD_DEFAULT) {
            cacheMode = mSettings.getCacheMode();
        }

        if (method.equals("POST")) {
            // Don't use the cache on POSTs when issuing a normal POST
            // request.
            if (cacheMode == WebSettings.LOAD_NORMAL) {
                cacheMode = WebSettings.LOAD_NO_CACHE;
            }
            if (mSettings.getSavePassword() && hasPasswordField()) {
                try {
                    if (Config.DEBUG) {
                        Assert.assertNotNull(mCallbackProxy.getBackForwardList()
                                .getCurrentItem());
                    }
                    WebAddress uri = new WebAddress(mCallbackProxy
                            .getBackForwardList().getCurrentItem().getUrl());
                    String host = uri.mHost;
                    String[] ret = getUsernamePassword();
                    if (ret != null && postData != null && ret[0].length() > 0
                            && ret[1].length() > 0
                            && postData.contains(URLEncoder.encode(ret[0]))
                            && postData.contains(URLEncoder.encode(ret[1]))) {
                        String[] saved = mDatabase.getUsernamePassword(host);
                        if (saved != null) {
                            // null username implies that user has chosen not to
                            // save password
                            if (saved[0] != null) {
                                // non-null username implies that user has
                                // chosen to save password, so update the 
                                // recorded password
                                mDatabase.setUsernamePassword(host, ret[0],
                                        ret[1]);
                            }
                        } else {
                            // CallbackProxy will handle creating the resume
                            // message
                            mCallbackProxy.onSavePassword(host, ret[0], ret[1],
                                    null);
                        }
                    }
                } catch (ParseException ex) {
                    // if it is bad uri, don't save its password
                }
            }
            if (postData == null) {
                postData = "";
            }
        }

        // is this resource the main-frame top-level page?
        boolean isMainFramePage = mIsMainFrame && url.equals(mCurrentUrl);

        if (Config.LOGV) {
            Log.v(LOGTAG, "startLoadingResource: url=" + url + ", method="
                    + method + ", postData=" + postData + ", isHighPriority="
                    + isHighPriority + ", isMainFramePage=" + isMainFramePage);
        }

        // Create a LoadListener
        LoadListener loadListener = LoadListener.getLoadListener(mContext, this, url,
                loaderHandle, synchronous, isMainFramePage);

        mCallbackProxy.onLoadResource(url);

        if (LoadListener.getNativeLoaderCount() > MAX_OUTSTANDING_REQUESTS) {
            loadListener.error(
                    android.net.http.EventHandler.ERROR, mContext.getString(
                            com.android.internal.R.string.httpErrorTooManyRequests));
            loadListener.notifyError();
            loadListener.tearDown();
            return null;
        }

        // during synchronous load, the WebViewCore thread is blocked, so we
        // need to endCacheTransaction first so that http thread won't be 
        // blocked in setupFile() when createCacheFile.
        if (synchronous) {
            CacheManager.endCacheTransaction();
        }

        FrameLoader loader = new FrameLoader(loadListener,
                mSettings.getUserAgentString(), method, isHighPriority);
        loader.setHeaders(headers);
        loader.setPostData(postData);
        loader.setCacheMode(cacheMode); // Set the load mode to the mode used
                                        // for the current page.
        // Set referrer to current URL?
        if (!loader.executeLoad()) {
            checker.responseAlert("startLoadingResource fail");
        }
        checker.responseAlert("startLoadingResource succeed");

        if (synchronous) {
            CacheManager.startCacheTransaction();
        }

        return !synchronous ? loadListener : null;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Set the progress for the browser activity.  Called by native code.
     * Uses a delay so it does not happen too often.
     * @param newProgress An int between zero and one hundred representing
     *                    the current progress percentage of loading the page.
     */
    private void setProgress(int newProgress) {
        mCallbackProxy.onProgressChanged(newProgress);
        if (newProgress == 100) {
            sendMessageDelayed(obtainMessage(FRAME_COMPLETED), 100);
        }
        // FIXME: Need to figure out a better way to switch out of the history
        // drawing mode. Maybe we can somehow compare the history picture with 
        // the current picture, and switch when it contains more content.
        if (mFirstLayoutDone && newProgress > TRANSITION_SWITCH_THRESHOLD) {
            mCallbackProxy.switchOutDrawHistory();
        }
    }

    /**
     * Send the icon to the activity for display.
     * @param icon A Bitmap representing a page's favicon.
     */
    private void didReceiveIcon(Bitmap icon) {
        mCallbackProxy.onReceivedIcon(icon);
    }

<<<<<<< HEAD
    // Called by JNI when an apple-touch-icon attribute was found.
    private void didReceiveTouchIconUrl(String url, boolean precomposed) {
        mCallbackProxy.onReceivedTouchIconUrl(url, precomposed);
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Request a new window from the client.
     * @return The BrowserFrame object stored in the new WebView.
     */
    private BrowserFrame createWindow(boolean dialog, boolean userGesture) {
<<<<<<< HEAD
        return mCallbackProxy.createWindow(dialog, userGesture);
=======
        WebView w = mCallbackProxy.createWindow(dialog, userGesture);
        if (w != null) {
            return w.getWebViewCore().getBrowserFrame();
        }
        return null;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Try to focus this WebView.
     */
    private void requestFocus() {
        mCallbackProxy.onRequestFocus();
    }

    /**
     * Close this frame and window.
     */
    private void closeWindow(WebViewCore w) {
<<<<<<< HEAD
        mCallbackProxy.onCloseWindow(w.getWebViewClassic());
=======
        mCallbackProxy.onCloseWindow(w.getWebView());
>>>>>>> 54b6cfa... Initial Contribution
    }

    // XXX: Must match PolicyAction in FrameLoaderTypes.h in webcore
    static final int POLICY_USE = 0;
    static final int POLICY_IGNORE = 2;

    private void decidePolicyForFormResubmission(int policyFunction) {
        Message dontResend = obtainMessage(POLICY_FUNCTION, policyFunction,
                POLICY_IGNORE);
        Message resend = obtainMessage(POLICY_FUNCTION, policyFunction,
                POLICY_USE);
        mCallbackProxy.onFormResubmission(dontResend, resend);
    }

    /**
     * Tell the activity to update its global history.
     */
    private void updateVisitedHistory(String url, boolean isReload) {
        mCallbackProxy.doUpdateVisitedHistory(url, isReload);
    }

    /**
     * Get the CallbackProxy for sending messages to the UI thread.
     */
    /* package */ CallbackProxy getCallbackProxy() {
        return mCallbackProxy;
    }

    /**
     * Returns the User Agent used by this frame
     */
    String getUserAgentString() {
        return mSettings.getUserAgentString();
    }

<<<<<<< HEAD
    // These ids need to be in sync with enum rawResId in PlatformBridge.h
    private static final int NODOMAIN = 1;
    private static final int LOADERROR = 2;
    /* package */ static final int DRAWABLEDIR = 3;
    private static final int FILE_UPLOAD_LABEL = 4;
    private static final int RESET_LABEL = 5;
    private static final int SUBMIT_LABEL = 6;
    private static final int FILE_UPLOAD_NO_FILE_CHOSEN = 7;

    private String getRawResFilename(int id) {
        return getRawResFilename(id, mContext);
    }
    /* package */ static String getRawResFilename(int id, Context context) {
        int resid;
        switch (id) {
            case NODOMAIN:
                resid = com.android.internal.R.raw.nodomain;
                break;

            case LOADERROR:
                resid = com.android.internal.R.raw.loaderror;
                break;

            case DRAWABLEDIR:
                // use one known resource to find the drawable directory
                resid = com.android.internal.R.drawable.btn_check_off;
                break;

            case FILE_UPLOAD_LABEL:
                return context.getResources().getString(
                        com.android.internal.R.string.upload_file);

            case RESET_LABEL:
                return context.getResources().getString(
                        com.android.internal.R.string.reset);

            case SUBMIT_LABEL:
                return context.getResources().getString(
                        com.android.internal.R.string.submit);

            case FILE_UPLOAD_NO_FILE_CHOSEN:
                return context.getResources().getString(
                        com.android.internal.R.string.no_file_chosen);

            default:
                Log.e(LOGTAG, "getRawResFilename got incompatible resource ID");
                return "";
        }
        TypedValue value = new TypedValue();
        context.getResources().getValue(resid, value, true);
        if (id == DRAWABLEDIR) {
            String path = value.string.toString();
            int index = path.lastIndexOf('/');
            if (index < 0) {
                Log.e(LOGTAG, "Can't find drawable directory.");
                return "";
            }
            return path.substring(0, index + 1);
        }
        return value.string.toString();
    }

    private float density() {
        return mContext.getResources().getDisplayMetrics().density;
    }

    /**
     * Called by JNI when the native HTTP stack gets an authentication request.
     *
     * We delegate the request to CallbackProxy, and route its response to
     * {@link #nativeAuthenticationProceed(int, String, String)} or
     * {@link #nativeAuthenticationCancel(int)}.
     *
     * We don't care what thread the callback is invoked on. All threading is
     * handled on the C++ side, because the WebKit thread may be blocked on a
     * synchronous call and unable to pump our MessageQueue.
     */
    private void didReceiveAuthenticationChallenge(
            final int handle, String host, String realm, final boolean useCachedCredentials,
            final boolean suppressDialog) {

        HttpAuthHandler handler = new HttpAuthHandler() {

            @Override
            public boolean useHttpAuthUsernamePassword() {
                return useCachedCredentials;
            }

            @Override
            public void proceed(String username, String password) {
                nativeAuthenticationProceed(handle, username, password);
            }

            @Override
            public void cancel() {
                nativeAuthenticationCancel(handle);
            }

            @Override
            public boolean suppressDialog() {
                return suppressDialog;
            }
        };
        mCallbackProxy.onReceivedHttpAuthRequest(handler, host, realm);
    }

    /**
     * Called by JNI when the Chromium HTTP stack gets an invalid certificate chain.
     *
     * We delegate the request to CallbackProxy, and route its response to
     * {@link #nativeSslCertErrorProceed(int)} or
     * {@link #nativeSslCertErrorCancel(int, int)}.
     */
    private void reportSslCertError(final int handle, final int certError, byte certDER[],
            String url) {
        final SslError sslError;
        try {
            X509Certificate cert = new X509CertImpl(certDER);
            SslCertificate sslCert = new SslCertificate(cert);
            sslError = SslError.SslErrorFromChromiumErrorCode(certError, sslCert, url);
        } catch (IOException e) {
            // Can't get the certificate, not much to do.
            Log.e(LOGTAG, "Can't get the certificate from WebKit, canceling");
            nativeSslCertErrorCancel(handle, certError);
            return;
        }

        if (SslCertLookupTable.getInstance().isAllowed(sslError)) {
            nativeSslCertErrorProceed(handle);
            mCallbackProxy.onProceededAfterSslError(sslError);
            return;
        }

        SslErrorHandler handler = new SslErrorHandler() {
            @Override
            public void proceed() {
                SslCertLookupTable.getInstance().setIsAllowed(sslError);
                post(new Runnable() {
                        public void run() {
                            nativeSslCertErrorProceed(handle);
                        }
                    });
            }
            @Override
            public void cancel() {
                post(new Runnable() {
                        public void run() {
                            nativeSslCertErrorCancel(handle, certError);
                        }
                    });
            }
        };
        mCallbackProxy.onReceivedSslError(handler, sslError);
    }

    /**
     * Called by JNI when the native HTTPS stack gets a client
     * certificate request.
     *
     * We delegate the request to CallbackProxy, and route its response to
     * {@link #nativeSslClientCert(int, X509Certificate)}.
     */
    private void requestClientCert(int handle, String hostAndPort) {
        SslClientCertLookupTable table = SslClientCertLookupTable.getInstance();
        if (table.IsAllowed(hostAndPort)) {
            // previously allowed
            PrivateKey pkey = table.PrivateKey(hostAndPort);
            if (pkey instanceof OpenSSLRSAPrivateKey) {
                nativeSslClientCert(handle,
                                    ((OpenSSLRSAPrivateKey)pkey).getPkeyContext(),
                                    table.CertificateChain(hostAndPort));
            } else if (pkey instanceof OpenSSLDSAPrivateKey) {
                nativeSslClientCert(handle,
                                    ((OpenSSLDSAPrivateKey)pkey).getPkeyContext(),
                                    table.CertificateChain(hostAndPort));
            } else {
                nativeSslClientCert(handle,
                                    pkey.getEncoded(),
                                    table.CertificateChain(hostAndPort));
            }
        } else if (table.IsDenied(hostAndPort)) {
            // previously denied
            nativeSslClientCert(handle, 0, null);
        } else {
            // previously ignored or new
            mCallbackProxy.onReceivedClientCertRequest(
                    new ClientCertRequestHandler(this, handle, hostAndPort, table), hostAndPort);
        }
    }

    /**
     * Called by JNI when the native HTTP stack needs to download a file.
     *
     * We delegate the request to CallbackProxy, which owns the current app's
     * DownloadListener.
     */
    private void downloadStart(String url, String userAgent,
            String contentDisposition, String mimeType, long contentLength) {
        // This will only work if the url ends with the filename
        if (mimeType.isEmpty()) {
            try {
                String extension = url.substring(url.lastIndexOf('.') + 1);
                mimeType = libcore.net.MimeUtils.guessMimeTypeFromExtension(extension);
                // MimeUtils might return null, not sure if downloadmanager is happy with that
                if (mimeType == null)
                    mimeType = "";
            } catch(IndexOutOfBoundsException exception) {
                // mimeType string end with a '.', not much to do
            }
        }
        mimeType = MimeTypeMap.getSingleton().remapGenericMimeType(
                mimeType, url, contentDisposition);

        if (CertTool.getCertType(mimeType) != null) {
            mKeyStoreHandler = new KeyStoreHandler(mimeType);
        } else {
            mCallbackProxy.onDownloadStart(url, userAgent,
                contentDisposition, mimeType, contentLength);
        }
    }

    /**
     * Called by JNI for Chrome HTTP stack when the Java side needs to access the data.
     */
    private void didReceiveData(byte data[], int size) {
        if (mKeyStoreHandler != null) mKeyStoreHandler.didReceiveData(data, size);
    }

    private void didFinishLoading() {
      if (mKeyStoreHandler != null) {
          mKeyStoreHandler.installCert(mContext);
          mKeyStoreHandler = null;
      }
    }

    /**
     * Called by JNI when we recieve a certificate for the page's main resource.
     * Used by the Chromium HTTP stack only.
     */
    private void setCertificate(byte cert_der[]) {
        try {
            X509Certificate cert = new X509CertImpl(cert_der);
            mCallbackProxy.onReceivedCertificate(new SslCertificate(cert));
        } catch (IOException e) {
            // Can't get the certificate, not much to do.
            Log.e(LOGTAG, "Can't get the certificate from WebKit, canceling");
            return;
        }
    }

    /*package*/ SearchBox getSearchBox() {
        return mSearchBox;
    }

    /**
     * Called by JNI when processing the X-Auto-Login header.
     */
    private void autoLogin(String realm, String account, String args) {
        mCallbackProxy.onReceivedLoginRequest(realm, account, args);
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    //==========================================================================
    // native functions
    //==========================================================================

    /**
<<<<<<< HEAD
     * Create a new native frame for a given WebView
     * @param w     A WebView that the frame draws into.
=======
     * Create a new native frame.
>>>>>>> 54b6cfa... Initial Contribution
     * @param am    AssetManager to use to get assets.
     * @param list  The native side will add and remove items from this list as
     *              the native list changes.
     */
<<<<<<< HEAD
    private native void nativeCreateFrame(WebViewCore w, AssetManager am,
            WebBackForwardList list);

    /**
     * Destroy the native frame.
     */
    public native void nativeDestroyFrame();

    private native void nativeCallPolicyFunction(int policyFunction,
            int decision);
=======
    private native void nativeCreateFrame(AssetManager am,
            WebBackForwardList list);

    /**
     * Create a native view attached to a WebView.
     * @param w A WebView that the frame draws into.
     */
    private native void nativeCreateView(WebViewCore w);

    private native void nativeCallPolicyFunction(int policyFunction,
            int decision);
    /**
     * Destroy the native frame.
     */
    public native void nativeDestroyFrame();

    /**
     * Detach the view from the frame.
     */
    private native void nativeDetachView();
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Reload the current main frame.
     */
    public native void reload(boolean allowStale);

    /**
     * Go back or forward the number of steps given.
     * @param steps A negative or positive number indicating the direction
     *              and number of steps to move.
     */
<<<<<<< HEAD
    private native void nativeGoBackOrForward(int steps);
=======
    public native void goBackOrForward(int steps);
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * stringByEvaluatingJavaScriptFromString will execute the
     * JS passed in in the context of this browser frame.
     * @param script A javascript string to execute
     * 
     * @return string result of execution or null
     */
    public native String stringByEvaluatingJavaScriptFromString(String script);

    /**
     * Add a javascript interface to the main frame.
     */
    private native void nativeAddJavascriptInterface(int nativeFramePointer,
            Object obj, String interfaceName);

<<<<<<< HEAD
=======
    /**
     * Enable or disable the native cache.
     */
    /* FIXME: The native cache is always on for now until we have a better
     * solution for our 2 caches. */
    private native void setCacheDisabled(boolean disabled);

    public native boolean cacheDisabled();

>>>>>>> 54b6cfa... Initial Contribution
    public native void clearCache();

    /**
     * Returns false if the url is bad.
     */
<<<<<<< HEAD
    private native void nativeLoadUrl(String url, Map<String, String> headers);

    private native void nativePostUrl(String url, byte[] postData);

    private native void nativeLoadData(String baseUrl, String data,
            String mimeType, String encoding, String historyUrl);
=======
    private native boolean nativeLoadUrl(String url);

    private native void nativeLoadData(String baseUrl, String data,
            String mimeType, String encoding, String failUrl);
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Stop loading the current page.
     */
<<<<<<< HEAD
    public void stopLoading() {
        if (mIsMainFrame) {
            resetLoadingStates();
        }
        nativeStopLoading();
    }

    private native void nativeStopLoading();
=======
    public native void stopLoading();
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Return true if the document has images.
     */
    public native boolean documentHasImages();

    /**
     * @return TRUE if there is a password field in the current frame
     */
    private native boolean hasPasswordField();

    /**
     * Get username and password in the current frame. If found, String[0] is
     * username and String[1] is password. Otherwise return NULL.
     * @return String[]
     */
    private native String[] getUsernamePassword();

    /**
     * Set username and password to the proper fields in the current frame
     * @param username
     * @param password
     */
    private native void setUsernamePassword(String username, String password);

<<<<<<< HEAD
    private native String nativeSaveWebArchive(String basename, boolean autoname);

    private native void nativeOrientationChanged(int orientation);

    private native void nativeAuthenticationProceed(int handle, String username, String password);
    private native void nativeAuthenticationCancel(int handle);

    private native void nativeSslCertErrorProceed(int handle);
    private native void nativeSslCertErrorCancel(int handle, int certError);

    native void nativeSslClientCert(int handle,
                                    int ctx,
                                    byte[][] asn1DerEncodedCertificateChain);

    native void nativeSslClientCert(int handle,
                                    byte[] pkey,
                                    byte[][] asn1DerEncodedCertificateChain);

    /**
     * Returns true when the contents of the frame is an RTL or vertical-rl
     * page. This is used for determining whether a frame should be initially
     * scrolled right-most as opposed to left-most.
     * @return true when the frame should be initially scrolled right-most
     * based on the text direction and writing mode.
     */
    /* package */ boolean getShouldStartScrolledRight() {
        return nativeGetShouldStartScrolledRight(mNativeFrame);
    }

    private native boolean nativeGetShouldStartScrolledRight(int nativeBrowserFrame);
=======
    /**
     * Get form's "text" type data associated with the current frame.
     * @return HashMap If succeed, returns a list of name/value pair. Otherwise
     *         returns null.
     */
    private native HashMap getFormTextData();
>>>>>>> 54b6cfa... Initial Contribution
}
