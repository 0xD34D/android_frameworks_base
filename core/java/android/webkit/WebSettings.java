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

package android.webkit;

<<<<<<< HEAD
import android.os.Message;
import android.os.Build;
=======
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import java.util.Locale;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * Manages settings state for a WebView. When a WebView is first created, it
 * obtains a set of default settings. These default settings will be returned
 * from any getter call. A WebSettings object obtained from
 * WebView.getSettings() is tied to the life of the WebView. If a WebView has
 * been destroyed, any method call on WebSettings will throw an
 * IllegalStateException.
 */
<<<<<<< HEAD
// This is an abstract base class: concrete WebViewProviders must
// create a class derived from this, and return an instance of it in the
// WebViewProvider.getWebSettingsProvider() method implementation.
public abstract class WebSettings {
    /**
     * Enum for controlling the layout of html.
     * <ul>
     *   <li>NORMAL means no rendering changes.</li>
     *   <li>SINGLE_COLUMN moves all content into one column that is the width of the
     *       view.</li>
     *   <li>NARROW_COLUMNS makes all columns no wider than the screen if possible.</li>
     * </ul>
=======
public class WebSettings {
    /**
     * Enum for controlling the layout of html.
     * NORMAL means no rendering changes.
     * SINGLE_COLUMN moves all content into one column that is the width of the
     * view.
     * NARROW_COLUMNS makes all columns no wider than the screen if possible.
>>>>>>> 54b6cfa... Initial Contribution
     */
    // XXX: These must match LayoutAlgorithm in Settings.h in WebCore.
    public enum LayoutAlgorithm {
        NORMAL,
<<<<<<< HEAD
        /**
         * @deprecated This algorithm is now obsolete.
         */
        @Deprecated
=======
>>>>>>> 54b6cfa... Initial Contribution
        SINGLE_COLUMN,
        NARROW_COLUMNS
    }

    /**
     * Enum for specifying the text size.
<<<<<<< HEAD
     * <ul>
     *   <li>SMALLEST is 50%</li>
     *   <li>SMALLER is 75%</li>
     *   <li>NORMAL is 100%</li>
     *   <li>LARGER is 150%</li>
     *   <li>LARGEST is 200%</li>
     * </ul>
     *
     * @deprecated Use {@link WebSettings#setTextZoom(int)} and {@link WebSettings#getTextZoom()} instead.
=======
     * SMALLEST is 50%
     * SMALLER is 75%
     * NORMAL is 100%
     * LARGER is 150%
     * LARGEST is 200%
>>>>>>> 54b6cfa... Initial Contribution
     */
    public enum TextSize {
        SMALLEST(50),
        SMALLER(75),
        NORMAL(100),
        LARGER(150),
        LARGEST(200);
        TextSize(int size) {
            value = size;
        }
        int value;
    }
<<<<<<< HEAD

    /**
     * Enum for specifying the WebView's desired density.
     * <ul>
     *   <li>FAR makes 100% looking like in 240dpi</li>
     *   <li>MEDIUM makes 100% looking like in 160dpi</li>
     *   <li>CLOSE makes 100% looking like in 120dpi</li>
     * </ul>
     */
    public enum ZoomDensity {
        FAR(150),      // 240dpi
        MEDIUM(100),    // 160dpi
        CLOSE(75);     // 120dpi
        ZoomDensity(int size) {
            value = size;
        }
        int value;
    }

    /**
     * Default cache usage pattern. Use with {@link #setCacheMode}.
=======
    
    /**
     * Default cache usage pattern  Use with {@link #setCacheMode}.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public static final int LOAD_DEFAULT = -1;

    /**
<<<<<<< HEAD
     * Normal cache usage pattern. Use with {@link #setCacheMode}.
=======
     * Normal cache usage pattern  Use with {@link #setCacheMode}.
>>>>>>> 54b6cfa... Initial Contribution
     */
    public static final int LOAD_NORMAL = 0;

    /**
<<<<<<< HEAD
     * Use cache if content is there, even if expired (eg, history nav).
=======
     * Use cache if content is there, even if expired (eg, history nav)
>>>>>>> 54b6cfa... Initial Contribution
     * If it is not in the cache, load from network.
     * Use with {@link #setCacheMode}.
     */
    public static final int LOAD_CACHE_ELSE_NETWORK = 1;

    /**
<<<<<<< HEAD
     * Don't use the cache, load from network.
     * Use with {@link #setCacheMode}.
     */
    public static final int LOAD_NO_CACHE = 2;

=======
     * Don't use the cache, load from network
     * Use with {@link #setCacheMode}.
     */
    public static final int LOAD_NO_CACHE = 2;
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Don't use the network, load from cache only.
     * Use with {@link #setCacheMode}.
     */
    public static final int LOAD_CACHE_ONLY = 3;

    public enum RenderPriority {
        NORMAL,
        HIGH,
        LOW
    }

<<<<<<< HEAD
    /**
     * The plugin state effects how plugins are treated on a page. ON means
     * that any object will be loaded even if a plugin does not exist to handle
     * the content. ON_DEMAND means that if there is a plugin installed that
     * can handle the content, a placeholder is shown until the user clicks on
     * the placeholder. Once clicked, the plugin will be enabled on the page.
     * OFF means that all plugins will be turned off and any fallback content
     * will be used.
     */
    public enum PluginState {
        ON,
        ON_DEMAND,
        OFF
    }

    /**
     * Hidden constructor to prevent clients from creating a new settings
     * instance or deriving the class.
     *
     * @hide
     */
    protected WebSettings() {
=======
    // BrowserFrame used to access the native frame pointer.
    private BrowserFrame mBrowserFrame;
    // Flag to prevent multiple SYNC messages at one time.
    private boolean mSyncPending = false;
    // Custom handler that queues messages until the WebCore thread is active.
    private final EventHandler mEventHandler;
    // Private settings so we don't have to go into native code to
    // retrieve the values. After setXXX, postSync() needs to be called.
    // XXX: The default values need to match those in WebSettings.cpp
    private LayoutAlgorithm mLayoutAlgorithm = LayoutAlgorithm.NARROW_COLUMNS;
    private TextSize        mTextSize = TextSize.NORMAL;
    private String          mStandardFontFamily = "sans-serif";
    private String          mFixedFontFamily = "monospace";
    private String          mSansSerifFontFamily = "sans-serif";
    private String          mSerifFontFamily = "serif";
    private String          mCursiveFontFamily = "cursive";
    private String          mFantasyFontFamily = "fantasy";
    private String          mDefaultTextEncoding = "Latin-1";
    private String          mUserAgent = ANDROID_USERAGENT;
    private String          mPluginsPath = "";
    private int             mMinimumFontSize = 8;
    private int             mMinimumLogicalFontSize = 8;
    private int             mDefaultFontSize = 16;
    private int             mDefaultFixedFontSize = 13;
    private boolean         mLoadsImagesAutomatically = true;
    private boolean         mBlockNetworkImage = false;
    private boolean         mJavaScriptEnabled = false;
    private boolean         mPluginsEnabled = false;
    private boolean         mJavaScriptCanOpenWindowsAutomatically = false;
    private boolean         mUseDoubleTree = false;
    private boolean         mUseWideViewport = false;
    private boolean         mSupportMultipleWindows = false;
    // Don't need to synchronize the get/set methods as they
    // are basic types, also none of these values are used in
    // native WebCore code.
    private RenderPriority  mRenderPriority = RenderPriority.NORMAL;
    private int             mOverrideCacheMode = LOAD_DEFAULT;
    private boolean         mSaveFormData = true;
    private boolean         mSavePassword = true;
    private boolean         mLightTouchEnabled = false;
    private boolean         mNeedInitialFocus = true;
    private boolean         mNavDump = false;
    private boolean         mSupportZoom = true;

    // Class to handle messages before WebCore is ready.
    private class EventHandler {
        // Message id for syncing
        static final int SYNC = 0;
        // Message id for setting priority
        static final int PRIORITY = 1;
        // Actual WebCore thread handler
        private Handler mHandler;

        private synchronized void createHandler() {
            // as mRenderPriority can be set before thread is running, sync up
            setRenderPriority();

            // create a new handler
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case SYNC:
                            synchronized (WebSettings.this) {
                                if (mBrowserFrame.mNativeFrame != 0) {
                                    nativeSync(mBrowserFrame.mNativeFrame);
                                }
                                mSyncPending = false;
                            }
                            break;

                        case PRIORITY: {
                            setRenderPriority();
                            break;
                        }
                    }
                }
            };
        }

        private void setRenderPriority() {
            synchronized (WebSettings.this) {
                if (mRenderPriority == RenderPriority.NORMAL) {
                    android.os.Process.setThreadPriority(
                            android.os.Process.THREAD_PRIORITY_DEFAULT);
                } else if (mRenderPriority == RenderPriority.HIGH) {
                    android.os.Process.setThreadPriority(
                            android.os.Process.THREAD_PRIORITY_FOREGROUND +
                            android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
                } else if (mRenderPriority == RenderPriority.LOW) {
                    android.os.Process.setThreadPriority(
                            android.os.Process.THREAD_PRIORITY_BACKGROUND);
                }
            }
        }

        /**
         * Send a message to the private queue or handler.
         */
        private synchronized boolean sendMessage(Message msg) {
            if (mHandler != null) {
                mHandler.sendMessage(msg);
                return true;
            } else {
                return false;
            }
        }
    }

    // User agent strings.
    private static final String DESKTOP_USERAGENT =
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en) AppleWebKit/522+ " +
            "(KHTML, like Gecko) Safari/419.3";
    private static final String IPHONE_USERAGENT = "Mozilla/5.0 (iPhone; U; " +
            "CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) " +
            "Version/3.0 Mobile/1A543 Safari/419.3";
    private static String ANDROID_USERAGENT;

    /**
     * Package constructor to prevent clients from creating a new settings
     * instance.
     */
    WebSettings(Context context) {
        if (ANDROID_USERAGENT == null) {
            StringBuffer arg = new StringBuffer();
            // Add version
            final String version = Build.VERSION.RELEASE;
            if (version.length() > 0) {
                arg.append(version);
            } else {
                // default to "1.0"
                arg.append("1.0");
            }
            arg.append("; ");
            // Initialize the mobile user agent with the default locale.
            final Locale l = Locale.getDefault();
            final String language = l.getLanguage();
            if (language != null) {
                arg.append(language.toLowerCase());
                final String country = l.getCountry();
                if (country != null) {
                    arg.append("-");
                    arg.append(country.toLowerCase());
                }
            } else {
                // default to "en"
                arg.append("en");
            }
            // Add device name
            final String device = Build.DEVICE;
            if (device.length() > 0) {
                arg.append("; ");
                arg.append(device);
            }
            final String base = context.getResources().getText(
                    com.android.internal.R.string.web_user_agent).toString();
            ANDROID_USERAGENT = String.format(base, arg);
            mUserAgent = ANDROID_USERAGENT;
        }
        mEventHandler = new EventHandler();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Enables dumping the pages navigation cache to a text file.
<<<<<<< HEAD
     *
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public void setNavDump(boolean enabled) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether dumping the navigation cache is enabled.
     *
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public boolean getNavDump() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should support zooming using its on-screen zoom
     * controls and gestures. The particular zoom mechanisms that should be used
     * can be set with {@link #setBuiltInZoomControls}. This setting does not
     * affect zooming performed using the {@link WebView#zoomIn()} and
     * {@link WebView#zoomOut()} methods. The default is true.
     *
     * @param support whether the WebView should support zoom
     */
    public void setSupportZoom(boolean support) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView supports zoom.
     *
     * @return true if the WebView supports zoom
     * @see #setSupportZoom
     */
    public boolean supportZoom() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should use its built-in zoom mechanisms. The
     * built-in zoom mechanisms comprise on-screen zoom controls, which are
     * displayed over the WebView's content, and the use of a pinch gesture to
     * control zooming. Whether or not these on-screen controls are displayed
     * can be set with {@link #setDisplayZoomControls}. The default is false.
     * <p>
     * The built-in mechanisms are the only currently supported zoom
     * mechanisms, so it is recommended that this setting is always enabled.
     *
     * @param enabled whether the WebView should use its built-in zoom mechanisms
     */
    // This method was intended to select between the built-in zoom mechanisms
    // and the separate zoom controls. The latter were obtained using
    // {@link WebView#getZoomControls}, which is now hidden.
    public void setBuiltInZoomControls(boolean enabled) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the zoom mechanisms built into WebView are being used.
     *
     * @return true if the zoom mechanisms built into WebView are being used
     * @see #setBuiltInZoomControls
     */
    public boolean getBuiltInZoomControls() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should display on-screen zoom controls when
     * using the built-in zoom mechanisms. See {@link #setBuiltInZoomControls}.
     * The default is true.
     *
     * @param enabled whether the WebView should display on-screen zoom controls
     */
    public void setDisplayZoomControls(boolean enabled) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView displays on-screen zoom controls when using
     * the built-in zoom mechanisms.
     *
     * @return true if the WebView displays on-screen zoom controls when using
     *         the built-in zoom mechanisms
     * @see #setDisplayZoomControls
     */
    public boolean getDisplayZoomControls() {
        throw new MustOverrideException();
    }

    /**
     * Enables or disables file access within WebView. File access is enabled by
     * default.  Note that this enables or disables file system access only.
     * Assets and resources are still accessible using file:///android_asset and
     * file:///android_res.
     */
    public void setAllowFileAccess(boolean allow) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView supports file access.
     *
     * @see #setAllowFileAccess
     */
    public boolean getAllowFileAccess() {
        throw new MustOverrideException();
    }

    /**
     * Enables or disables content URL access within WebView.  Content URL
     * access allows WebView to load content from a content provider installed
     * in the system. The default is enabled.
     */
    public void setAllowContentAccess(boolean allow) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView supports content URL access.
     *
     * @see #setAllowContentAccess
     */
    public boolean getAllowContentAccess() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView loads a page with overview mode.
     */
    public void setLoadWithOverviewMode(boolean overview) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView loads pages with overview mode.
     */
    public boolean getLoadWithOverviewMode() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView will enable smooth transition while panning or
     * zooming or while the window hosting the WebView does not have focus.
     * If it is true, WebView will choose a solution to maximize the performance.
     * e.g. the WebView's content may not be updated during the transition.
     * If it is false, WebView will keep its fidelity. The default value is false.
     */
    public void setEnableSmoothTransition(boolean enable) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView enables smooth transition while panning or
     * zooming.
     *
     * @see #setEnableSmoothTransition
     */
    public boolean enableSmoothTransition() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView uses its background for over scroll background.
     * If true, it will use the WebView's background. If false, it will use an
     * internal pattern. Default is true.
     *
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public void setUseWebViewBackgroundForOverscrollBackground(boolean view) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether this WebView uses WebView's background instead of
     * internal pattern for over scroll background.
     *
     * @see #setUseWebViewBackgroundForOverscrollBackground
     * @deprecated This method is now obsolete.
     */
    @Deprecated
    public boolean getUseWebViewBackgroundForOverscrollBackground() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView is saving form data.
     */
    public void setSaveFormData(boolean save) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is saving form data and displaying prior
     * entries/autofill++.  Always false in private browsing mode.
     */
    public boolean getSaveFormData() {
        throw new MustOverrideException();
    }

    /**
     * Stores whether the WebView is saving password.
     */
    public void setSavePassword(boolean save) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is saving password.
     */
    public boolean getSavePassword() {
        throw new MustOverrideException();
    }

    /**
     * Sets the text zoom of the page in percent. Default is 100.
     *
     * @param textZoom the percent value for increasing or decreasing the text
     */
    public synchronized void setTextZoom(int textZoom) {
        throw new MustOverrideException();
    }

    /**
     * Gets the text zoom of the page in percent.
     *
     * @return a percent value describing the text zoom
     * @see #setTextSizeZoom
     */
    public synchronized int getTextZoom() {
        throw new MustOverrideException();
    }

    /**
     * Sets the text size of the page.
     *
     * @param t the TextSize value for increasing or decreasing the text
     * @see WebSettings.TextSize
     * @deprecated Use {@link #setTextZoom(int)} instead.
     */
    public synchronized void setTextSize(TextSize t) {
        throw new MustOverrideException();
    }

    /**
     * Gets the text size of the page. If the text size was previously specified
     * in percent using {@link #setTextZoom(int)}, this will return
     * the closest matching {@link TextSize}.
     *
     * @return a TextSize enum value describing the text size
     * @see WebSettings.TextSize
     * @deprecated Use {@link #getTextZoom()} instead.
     */
    public synchronized TextSize getTextSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the default zoom density of the page. This should be called from UI
     * thread.
     *
     * @param zoom a ZoomDensity value
     * @see WebSettings.ZoomDensity
     */
    public void setDefaultZoom(ZoomDensity zoom) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default zoom density of the page. This should be called from UI
     * thread.
     * @return a ZoomDensity value
     * @see WebSettings.ZoomDensity
     */
    public ZoomDensity getDefaultZoom() {
        throw new MustOverrideException();
=======
     */
    public void setNavDump(boolean enabled) {
        mNavDump = enabled;
    }

    /**
     * Returns true if dumping the navigation cache is enabled.
     */
    public boolean getNavDump() {
        return mNavDump;
    }

    /**
     * Set whether the WebView supports zoom
     */
    public void setSupportZoom(boolean support) {
        mSupportZoom = support;
    }

    /**
     * Returns whether the WebView supports zoom
     */
    public boolean supportZoom() {
        return mSupportZoom;
    }

    /**
     * Store whether the WebView is saving form data.
     */
    public void setSaveFormData(boolean save) {
        mSaveFormData = save;
    }

    /**
     *  Return whether the WebView is saving form data.
     */
    public boolean getSaveFormData() {
        return mSaveFormData;
    }

    /**
     *  Store whether the WebView is saving password.
     */
    public void setSavePassword(boolean save) {
        mSavePassword = save;
    }

    /**
     *  Return whether the WebView is saving password.
     */
    public boolean getSavePassword() {
        return mSavePassword;
    }

    /**
     * Set the text size of the page.
     * @param t A TextSize value for increasing or decreasing the text.
     * @see WebSettings.TextSize
     */
    public synchronized void setTextSize(TextSize t) {
        mTextSize = t;
        postSync();
    }

    /**
     * Get the text size of the page.
     * @return A TextSize enum value describing the text size.
     * @see WebSettings.TextSize
     */
    public synchronized TextSize getTextSize() {
        return mTextSize;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Enables using light touches to make a selection and activate mouseovers.
     */
    public void setLightTouchEnabled(boolean enabled) {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Gets whether light touches are enabled.
     */
    public boolean getLightTouchEnabled() {
        throw new MustOverrideException();
    }

    /**
     * Controlled a rendering optimization that is no longer present. Setting
     * it now has no effect.
     *
     * @deprecated This setting now has no effect.
     */
    @Deprecated
    public synchronized void setUseDoubleTree(boolean use) {
        // Specified to do nothing, so no need for derived classes to override.
    }

    /**
     * Controlled a rendering optimization that is no longer present. Setting
     * it now has no effect.
     *
     * @deprecated This setting now has no effect.
     */
    @Deprecated
    public synchronized boolean getUseDoubleTree() {
        // Returns false unconditionally, so no need for derived classes to override.
        return false;
    }

    /**
     * Tells the WebView about user-agent string.
     *
     * @param ua 0 if the WebView should use an Android user-agent string,
     *           1 if the WebView should use a desktop user-agent string
     * @deprecated Please use setUserAgentString instead.
     */
    @Deprecated
    public synchronized void setUserAgent(int ua) {
        throw new MustOverrideException();
    }

    /**
     * Gets the user-agent as an int.
     *
     * @return 0 if the WebView is using an Android user-agent string,
     *         1 if the WebView is using a desktop user-agent string,
     *         -1 if the WebView is using user defined user-agent string
     * @deprecated Please use getUserAgentString instead.
     */
    @Deprecated
    public synchronized int getUserAgent() {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to use the wide viewport.
     */
    public synchronized void setUseWideViewPort(boolean use) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is using a wide viewport.
     *
     * @return true if the WebView is using a wide viewport
     */
    public synchronized boolean getUseWideViewPort() {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView whether it supports multiple windows. TRUE means
     * that {@link WebChromeClient#onCreateWindow(WebView, boolean,
     * boolean, Message)} is implemented by the host application.
     */
    public synchronized void setSupportMultipleWindows(boolean support) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView is supporting multiple windows.
     *
     * @return true if the WebView is supporting multiple windows. This means
=======
        mLightTouchEnabled = enabled;
    }

    /**
     * Returns true if light touches are enabled.
     */
    public boolean getLightTouchEnabled() {
        return mLightTouchEnabled;
    }

    /**
     * Tell the WebView to use the double tree rendering algorithm.
     * @param use True if the WebView is to use double tree rendering, false
     *            otherwise.
     */
    public synchronized void setUseDoubleTree(boolean use) {
        if (mUseDoubleTree != use) {
            mUseDoubleTree = use;
            postSync();
        }
    }

    /**
     * Return true if the WebView is using the double tree rendering algorithm.
     * @return True if the WebView is using the double tree rendering
     *         algorithm.
     */
    public synchronized boolean getUseDoubleTree() {
        return mUseDoubleTree;
    }

    /**
     * Tell the WebView about user-agent string.
     * @param ua 0 if the WebView should use an Android user-agent string,
     *           1 if the WebView should use a desktop user-agent string.
     *           2 if the WebView should use an iPhone user-agent string.
     */
    public synchronized void setUserAgent(int ua) {
        if (ua == 0 && !ANDROID_USERAGENT.equals(mUserAgent)) {
            mUserAgent = ANDROID_USERAGENT;
            postSync();
        } else if (ua == 1 && !DESKTOP_USERAGENT.equals(mUserAgent)) {
            mUserAgent = DESKTOP_USERAGENT;
            postSync();
        } else if (ua == 2 && !IPHONE_USERAGENT.equals(mUserAgent)) {
            mUserAgent = IPHONE_USERAGENT;
            postSync();
        }
    }

    /**
     * Return user-agent as int
     * @return int  0 if the WebView is using an Android user-agent string.
     *              1 if the WebView is using a desktop user-agent string.
     *              2 if the WebView is using an iPhone user-agent string.
     */
    public synchronized int getUserAgent() {
        if (DESKTOP_USERAGENT.equals(mUserAgent)) {
            return 1;
        } else if (IPHONE_USERAGENT.equals(mUserAgent)) {
            return 2;
        }
        return 0;
    }

    /**
     * Tell the WebView to use the wide viewport
     */
    public synchronized void setUseWideViewPort(boolean use) {
        if (mUseWideViewport != use) {
            mUseWideViewport = use;
            postSync();
        }
    }

    /**
     * @return True if the WebView is using a wide viewport
     */
    public synchronized boolean getUseWideViewPort() {
        return mUseWideViewport;
    }

    /**
     * Tell the WebView whether it supports multiple windows. TRUE means
     *         that {@link WebChromeClient#onCreateWindow(WebView, boolean,
     *         boolean, Message)} is implemented by the host application.
     */
    public synchronized void setSupportMultipleWindows(boolean support) {
        if (mSupportMultipleWindows != support) {
            mSupportMultipleWindows = support;
            postSync();
        }
    }

    /**
     * @return True if the WebView is supporting multiple windows. This means
>>>>>>> 54b6cfa... Initial Contribution
     *         that {@link WebChromeClient#onCreateWindow(WebView, boolean,
     *         boolean, Message)} is implemented by the host application.
     */
    public synchronized boolean supportMultipleWindows() {
<<<<<<< HEAD
        throw new MustOverrideException();
    }

    /**
     * Sets the underlying layout algorithm. This will cause a relayout of the
     * WebView. The default is NARROW_COLUMNS.
     *
     * @param l a LayoutAlgorithm enum specifying the algorithm to use
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized void setLayoutAlgorithm(LayoutAlgorithm l) {
        throw new MustOverrideException();
    }

    /**
     * Gets the current layout algorithm.
     *
     * @return a LayoutAlgorithm enum value describing the layout algorithm
     *         being used
     * @see #setLayoutAlgorithm
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized LayoutAlgorithm getLayoutAlgorithm() {
        throw new MustOverrideException();
    }

    /**
     * Sets the standard font family name. The default is "sans-serif".
     *
     * @param font a font family name
     */
    public synchronized void setStandardFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the standard font family name.
     *
     * @return the standard font family name as a string
     * @see #setStandardFontFamily
     */
    public synchronized String getStandardFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the fixed font family name. The default is "monospace".
     *
     * @param font a font family name
     */
    public synchronized void setFixedFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the fixed font family name.
     *
     * @return the fixed font family name as a string
     * @see #setFixedFontFamily
     */
    public synchronized String getFixedFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the sans-serif font family name.
     *
     * @param font a font family name
     */
    public synchronized void setSansSerifFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the sans-serif font family name.
     *
     * @return the sans-serif font family name as a string
     */
    public synchronized String getSansSerifFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the serif font family name. The default is "sans-serif".
     *
     * @param font a font family name
     */
    public synchronized void setSerifFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the serif font family name. The default is "serif".
     *
     * @return the serif font family name as a string
     * @see #setSerifFontFamily
     */
    public synchronized String getSerifFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the cursive font family name. The default is "cursive".
     *
     * @param font a font family name
     */
    public synchronized void setCursiveFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the cursive font family name.
     *
     * @return the cursive font family name as a string
     * @see #setCursiveFontFamily
     */
    public synchronized String getCursiveFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the fantasy font family name. The default is "fantasy".
     *
     * @param font a font family name
     */
    public synchronized void setFantasyFontFamily(String font) {
        throw new MustOverrideException();
    }

    /**
     * Gets the fantasy font family name.
     *
     * @return the fantasy font family name as a string
     * @see #setFantasyFontFamily
     */
    public synchronized String getFantasyFontFamily() {
        throw new MustOverrideException();
    }

    /**
     * Sets the minimum font size. The default is 8.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setMinimumFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the minimum font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setMinimumFontSize
     */
    public synchronized int getMinimumFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the minimum logical font size. The default is 8.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setMinimumLogicalFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the minimum logical font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setMinimumLogicalFontSize
     */
    public synchronized int getMinimumLogicalFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the default font size. The default is 16.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setDefaultFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setDefaultFontSize
     */
    public synchronized int getDefaultFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets the default fixed font size. The default is 16.
     *
     * @param size a non-negative integer between 1 and 72. Any number outside
     *             the specified range will be pinned.
     */
    public synchronized void setDefaultFixedFontSize(int size) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default fixed font size.
     *
     * @return a non-negative integer between 1 and 72
     * @see #setDefaultFixedFontSize
     */
    public synchronized int getDefaultFixedFontSize() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should load image resources. Note that this method
     * controls loading of all images, including those embedded using the data
     * URI scheme. Use {@link #setBlockNetworkImage} to control loading only
     * of images specified using network URI schemes. Note that if the value of this
     * setting is changed from false to true, all images resources referenced
     * by content currently displayed by the WebView are loaded automatically.
     * The default is true.
     *
     * @param flag whether the WebView should load image resources
     */
    public synchronized void setLoadsImagesAutomatically(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView loads image resources. This includes
     * images embedded using the data URI scheme.
     *
     * @return true if the WebView loads image resources
     * @see #setLoadsImagesAutomatically
     */
    public synchronized boolean getLoadsImagesAutomatically() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should not load image resources from the
     * network (resources accessed via http and https URI schemes).  Note
     * that this method has no effect unless
     * {@link #getLoadsImagesAutomatically} returns true. Also note that
     * disabling all network loads using {@link #setBlockNetworkLoads}
     * will also prevent network images from loading, even if this flag is set
     * to false. When the value of this setting is changed from true to false,
     * network images resources referenced by content currently displayed by
     * the WebView are fetched automatically. The default is false.
     *
     * @param flag whether the WebView should not load image resources from the
     *             network
     * @see #setBlockNetworkLoads
     */
    public synchronized void setBlockNetworkImage(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView does not load image resources from the network.
     *
     * @return true if the WebView does not load image resources from the network
     * @see #setBlockNetworkImage
     */
    public synchronized boolean getBlockNetworkImage() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the WebView should not load resources from the network.
     * Use {@link #setBlockNetworkImage} to only avoid loading
     * image resources. Note that if the value of this setting is
     * changed from true to false, network resources referenced by content
     * currently displayed by the WebView are not fetched until
     * {@link android.webkit.WebView#reload} is called.
     * If the application does not have the
     * {@link android.Manifest.permission#INTERNET} permission, attempts to set
     * a value of false will cause a {@link java.lang.SecurityException}
     * to be thrown. The default value is false if the application has the
     * {@link android.Manifest.permission#INTERNET} permission, otherwise it is
     * true.
     *
     * @param flag whether the WebView should not load any resources from the
     *             network
     * @see android.webkit.WebView#reload
     */
    public synchronized void setBlockNetworkLoads(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the WebView does not load any resources from the network.
     *
     * @return true if the WebView does not load any resources from the network
     * @see #setBlockNetworkLoads
     */
    public synchronized boolean getBlockNetworkLoads() {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to enable JavaScript execution.
     * <b>The default is false.</b>
     *
     * @param flag true if the WebView should execute JavaScript
     */
    public synchronized void setJavaScriptEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets whether JavaScript running in the context of a file scheme URL
     * should be allowed to access content from any origin. This includes
     * access to content from other file scheme URLs. See
     * {@link #setAllowFileAccessFromFileURLs}. To enable the most restrictive,
     * and therefore secure policy, this setting should be disabled.
     * <p>
     * The default value is true for API level
     * {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH_MR1} and below,
     * and false for API level {@link android.os.Build.VERSION_CODES#JELLY_BEAN}
     * and above.
     *
     * @param flag whether JavaScript running in the context of a file scheme
     *             URL should be allowed to access content from any origin
     */
    public abstract void setAllowUniversalAccessFromFileURLs(boolean flag);

    /**
     * Sets whether JavaScript running in the context of a file scheme URL
     * should be allowed to access content from other file scheme URLs. To
     * enable the most restrictive, and therefore secure policy, this setting
     * should be disabled. Note that the value of this setting is ignored if
     * the value of {@link #getAllowUniversalAccessFromFileURLs} is true.
     * <p>
     * The default value is true for API level
     * {@link android.os.Build.VERSION_CODES#ICE_CREAM_SANDWICH_MR1} and below,
     * and false for API level {@link android.os.Build.VERSION_CODES#JELLY_BEAN}
     * and above.
     *
     * @param flag whether JavaScript running in the context of a file scheme
     *             URL should be allowed to access content from other file
     *             scheme URLs
     */
    public abstract void setAllowFileAccessFromFileURLs(boolean flag);

    /**
     * Tells the WebView to enable plugins.
     *
     * @param flag true if the WebView should load plugins
     * @deprecated This method has been deprecated in favor of
     *             {@link #setPluginState}
     */
    @Deprecated
    public synchronized void setPluginsEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to enable, disable, or have plugins on demand. On
     * demand mode means that if a plugin exists that can handle the embedded
     * content, a placeholder icon will be shown instead of the plugin. When
     * the placeholder is clicked, the plugin will be enabled.
     *
     * @param state a PluginState value
     */
    public synchronized void setPluginState(PluginState state) {
        throw new MustOverrideException();
    }

    /**
     * Sets a custom path to plugins used by the WebView. This method is
     * obsolete since each plugin is now loaded from its own package.
     *
     * @param pluginsPath a String path to the directory containing plugins
     * @deprecated This method is no longer used as plugins are loaded from
     *             their own APK via the system's package manager.
     */
    @Deprecated
    public synchronized void setPluginsPath(String pluginsPath) {
        // Specified to do nothing, so no need for derived classes to override.
    }

    /**
     * Sets the path to where database storage API databases should be saved.
     * Note that the WebCore Database Tracker only allows the path to be set once.
     *
     * @param databasePath a String path to the directory where databases should
     *                     be saved. May be the empty string but should never
     *                     be null.
     */
    // This will update WebCore when the Sync runs in the C++ side.
    public synchronized void setDatabasePath(String databasePath) {
        throw new MustOverrideException();
    }

    /**
     * Sets the path where the Geolocation permissions database should be saved.
     *
     * @param databasePath a String path to the directory where the Geolocation
     *                     permissions database should be saved. May be the
     *                     empty string but should never be null.
     */
    // This will update WebCore when the Sync runs in the C++ side.
    public synchronized void setGeolocationDatabasePath(String databasePath) {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView to enable Application Caches API.
     *
     * @param flag true if the WebView should enable Application Caches
     */
    public synchronized void setAppCacheEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets a custom path to the Application Caches files. The client
     * must ensure it exists before this call.
     *
     * @param appCachePath a String path to the directory containing
     *                     Application Caches files. The appCache path can be
     *                     the empty string but should not be null. Passing
     *                     null for this parameter will result in a no-op.
     */
    public synchronized void setAppCachePath(String appCachePath) {
        throw new MustOverrideException();
    }

    /**
     * Sets the maximum size for the Application Caches content.
     *
     * @param appCacheMaxSize the maximum size in bytes
     */
    public synchronized void setAppCacheMaxSize(long appCacheMaxSize) {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the database storage API is enabled.
     *
     * @param flag true if the WebView should use the database storage API
     */
    public synchronized void setDatabaseEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets whether the DOM storage API is enabled.
     *
     * @param flag true if the WebView should use the DOM storage API
     */
    public synchronized void setDomStorageEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the DOM Storage APIs are enabled.
     *
     * @return true if the DOM Storage APIs are enabled
     */
    public synchronized boolean getDomStorageEnabled() {
        throw new MustOverrideException();
    }
    /**
     * Gets the path to where database storage API databases are saved for
     * the current WebView.
     *
     * @return the String path to the database storage API databases
     */
    public synchronized String getDatabasePath() {
        throw new MustOverrideException();
    }

    /**
     * Gets whether the database storage API is enabled.
     *
     * @return true if the database storage API is enabled
     */
    public synchronized boolean getDatabaseEnabled() {
        throw new MustOverrideException();
    }

    /**
     * Sets whether Geolocation is enabled.
     *
     * @param flag whether Geolocation should be enabled
     */
    public synchronized void setGeolocationEnabled(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether JavaScript is enabled.
     *
     * @return true if JavaScript is enabled
     * @see #setJavaScriptEnabled
     */
    public synchronized boolean getJavaScriptEnabled() {
        throw new MustOverrideException();
    }

    /**
     * Gets whether JavaScript running in the context of a file scheme URL can
     * access content from any origin. This includes access to content from
     * other file scheme URLs.
     *
     * @return whether JavaScript running in the context of a file scheme URL
     *         can access content from any origin
     * @see #setAllowUniversalAccessFromFileURLs
     */
    public abstract boolean getAllowUniversalAccessFromFileURLs();

    /**
     * Gets whether JavaScript running in the context of a file scheme URL can
     * access content from other file scheme URLs.
     *
     * @return whether JavaScript running in the context of a file scheme URL
     *         can access content from other file scheme URLs
     * @see #setAllowFileAccessFromFileURLs
     */
    public abstract boolean getAllowFileAccessFromFileURLs();

    /**
     * Gets whether plugins are enabled.
     *
     * @return true if plugins are enabled
     * @deprecated This method has been replaced by {@link #getPluginState}
     */
    @Deprecated
    public synchronized boolean getPluginsEnabled() {
        throw new MustOverrideException();
    }

    /**
     * Gets the current plugin state.
     *
     * @return a value corresponding to the enum PluginState
     */
    public synchronized PluginState getPluginState() {
        throw new MustOverrideException();
    }

    /**
     * Gets the directory that contains the plugin libraries. This method is
     * obsolete since each plugin is now loaded from its own package.
     *
     * @return an empty string
     * @deprecated This method is no longer used as plugins are loaded from
     * their own APK via the system's package manager.
     */
    @Deprecated
    public synchronized String getPluginsPath() {
        // Unconditionally returns empty string, so no need for derived classes to override.
        return "";
    }

    /**
     * Tells JavaScript to open windows automatically. This applies to the
     * JavaScript function window.open(). The default is false.
     *
     * @param flag true if JavaScript can open windows automatically
     */
    public synchronized void setJavaScriptCanOpenWindowsAutomatically(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Gets whether JavaScript can open windows automatically.
     *
     * @return true if JavaScript can open windows automatically during
     *         window.open()
     * @see #setJavaScriptCanOpenWindowsAutomatically
     */
    public synchronized boolean getJavaScriptCanOpenWindowsAutomatically() {
        throw new MustOverrideException();
    }
    /**
     * Sets the default text encoding name to use when decoding html pages.
     * The default is "Latin-1".
     *
     * @param encoding the text encoding name
     */
    public synchronized void setDefaultTextEncodingName(String encoding) {
        throw new MustOverrideException();
    }

    /**
     * Gets the default text encoding name.
     *
     * @return the default text encoding name as a string
     * @see #setDefaultTextEncodingName
     */
    public synchronized String getDefaultTextEncodingName() {
        throw new MustOverrideException();
    }

    /**
     * Sets the WebView's user-agent string. If the string "ua" is null or empty,
     * it will use the system default user-agent string.
     */
    public synchronized void setUserAgentString(String ua) {
        throw new MustOverrideException();
    }

    /**
     * Gets the WebView's user-agent string.
     */
    public synchronized String getUserAgentString() {
        throw new MustOverrideException();
    }

    /**
     * Tells the WebView whether it needs to set a node to have focus when
     * {@link WebView#requestFocus(int, android.graphics.Rect)} is called.
     *
     * @param flag whether the WebView needs to set a node
     */
    public void setNeedInitialFocus(boolean flag) {
        throw new MustOverrideException();
    }

    /**
     * Sets the priority of the Render thread. Unlike the other settings, this
     * one only needs to be called once per process. The default is NORMAL.
     *
     * @param priority a RenderPriority
     */
    public synchronized void setRenderPriority(RenderPriority priority) {
        throw new MustOverrideException();
    }

    /**
     * Overrides the way the cache is used. The way the cache is used is based
=======
        return mSupportMultipleWindows;
    }

    /**
     * Set the underlying layout algorithm. This will cause a relayout of the
     * WebView.
     * @param l A LayoutAlgorithm enum specifying the algorithm to use.
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized void setLayoutAlgorithm(LayoutAlgorithm l) {
        // XXX: This will only be affective if libwebcore was built with
        // ANDROID_LAYOUT defined.
        if (mLayoutAlgorithm != l) {
            mLayoutAlgorithm = l;
            postSync();
        }
    }

    /**
     * Return the current layout algorithm.
     * @return LayoutAlgorithm enum value describing the layout algorithm
     *         being used.
     * @see WebSettings.LayoutAlgorithm
     */
    public synchronized LayoutAlgorithm getLayoutAlgorithm() {
        return mLayoutAlgorithm;
    }

    /**
     * Set the standard font family name.
     * @param font A font family name.
     */
    public synchronized void setStandardFontFamily(String font) {
        if (font != null && !font.equals(mStandardFontFamily)) {
            mStandardFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the standard font family name.
     * @return The standard font family name as a string.
     */
    public synchronized String getStandardFontFamily() {
        return mStandardFontFamily;
    }

    /**
     * Set the fixed font family name.
     * @param font A font family name.
     */
    public synchronized void setFixedFontFamily(String font) {
        if (font != null && !font.equals(mFixedFontFamily)) {
            mFixedFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the fixed font family name.
     * @return The fixed font family name as a string.
     */
    public synchronized String getFixedFontFamily() {
        return mFixedFontFamily;
    }

    /**
     * Set the sans-serif font family name.
     * @param font A font family name.
     */
    public synchronized void setSansSerifFontFamily(String font) {
        if (font != null && !font.equals(mSansSerifFontFamily)) {
            mSansSerifFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the sans-serif font family name.
     * @return The sans-serif font family name as a string.
     */
    public synchronized String getSansSerifFontFamily() {
        return mSansSerifFontFamily;
    }

    /**
     * Set the serif font family name.
     * @param font A font family name.
     */
    public synchronized void setSerifFontFamily(String font) {
        if (font != null && !font.equals(mSerifFontFamily)) {
            mSerifFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the serif font family name.
     * @return The serif font family name as a string.
     */
    public synchronized String getSerifFontFamily() {
        return mSerifFontFamily;
    }

    /**
     * Set the cursive font family name.
     * @param font A font family name.
     */
    public synchronized void setCursiveFontFamily(String font) {
        if (font != null && !font.equals(mCursiveFontFamily)) {
            mCursiveFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the cursive font family name.
     * @return The cursive font family name as a string.
     */
    public synchronized String getCursiveFontFamily() {
        return mCursiveFontFamily;
    }

    /**
     * Set the fantasy font family name.
     * @param font A font family name.
     */
    public synchronized void setFantasyFontFamily(String font) {
        if (font != null && !font.equals(mFantasyFontFamily)) {
            mFantasyFontFamily = font;
            postSync();
        }
    }

    /**
     * Get the fantasy font family name.
     * @return The fantasy font family name as a string.
     */
    public synchronized String getFantasyFontFamily() {
        return mFantasyFontFamily;
    }

    /**
     * Set the minimum font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setMinimumFontSize(int size) {
        size = pin(size);
        if (mMinimumFontSize != size) {
            mMinimumFontSize = size;
            postSync();
        }
    }

    /**
     * Get the minimum font size.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getMinimumFontSize() {
        return mMinimumFontSize;
    }

    /**
     * Set the minimum logical font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setMinimumLogicalFontSize(int size) {
        size = pin(size);
        if (mMinimumLogicalFontSize != size) {
            mMinimumLogicalFontSize = size;
            postSync();
        }
    }

    /**
     * Get the minimum logical font size.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getMinimumLogicalFontSize() {
        return mMinimumLogicalFontSize;
    }

    /**
     * Set the default font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setDefaultFontSize(int size) {
        size = pin(size);
        if (mDefaultFontSize != size) {
            mDefaultFontSize = size;
            postSync();
        }
    }

    /**
     * Get the default font size.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getDefaultFontSize() {
        return mDefaultFontSize;
    }

    /**
     * Set the default fixed font size.
     * @param size A non-negative integer between 1 and 72.
     * Any number outside the specified range will be pinned.
     */
    public synchronized void setDefaultFixedFontSize(int size) {
        size = pin(size);
        if (mDefaultFixedFontSize != size) {
            mDefaultFixedFontSize = size;
            postSync();
        }
    }

    /**
     * Get the default fixed font size.
     * @return A non-negative integer between 1 and 72.
     */
    public synchronized int getDefaultFixedFontSize() {
        return mDefaultFixedFontSize;
    }

    /**
     * Tell the WebView to load image resources automatically.
     * @param flag True if the WebView should load images automatically.
     */
    public synchronized void setLoadsImagesAutomatically(boolean flag) {
        if (mLoadsImagesAutomatically != flag) {
            mLoadsImagesAutomatically = flag;
            postSync();
        }
    }

    /**
     * Return true if the WebView will load image resources automatically.
     * @return True if the WebView loads images automatically.
     */
    public synchronized boolean getLoadsImagesAutomatically() {
        return mLoadsImagesAutomatically;
    }

    /**
     * Tell the WebView to block network image. This is only checked when
     * getLoadsImagesAutomatically() is true.
     * @param flag True if the WebView should block network image
     */
    public synchronized void setBlockNetworkImage(boolean flag) {
        if (mBlockNetworkImage != flag) {
            mBlockNetworkImage = flag;
            postSync();
        }
    }

    /**
     * Return true if the WebView will block network image.
     * @return True if the WebView blocks network image.
     */
    public synchronized boolean getBlockNetworkImage() {
        return mBlockNetworkImage;
    }

    /**
     * Tell the WebView to enable javascript execution.
     * @param flag True if the WebView should execute javascript.
     */
    public synchronized void setJavaScriptEnabled(boolean flag) {
        if (mJavaScriptEnabled != flag) {
            mJavaScriptEnabled = flag;
            postSync();
        }
    }

    /**
     * Tell the WebView to enable plugins.
     * @param flag True if the WebView should load plugins.
     */
    public synchronized void setPluginsEnabled(boolean flag) {
        if (mPluginsEnabled != flag) {
            mPluginsEnabled = flag;
            postSync();
        }
    }

    /**
     * Set a custom path to plugins used by the WebView. The client
     * must ensure it exists before this call.
     * @param pluginsPath String path to the directory containing plugins.
     */
    public synchronized void setPluginsPath(String pluginsPath) {
        if (pluginsPath != null && !pluginsPath.equals(mPluginsPath)) {
            mPluginsPath = pluginsPath;
            postSync();
        }
    }

    /**
     * Return true if javascript is enabled.
     * @return True if javascript is enabled.
     */
    public synchronized boolean getJavaScriptEnabled() {
        return mJavaScriptEnabled;
    }

    /**
     * Return true if plugins are enabled.
     * @return True if plugins are enabled.
     */
    public synchronized boolean getPluginsEnabled() {
        return mPluginsEnabled;
    }

    /**
     * Return the current path used for plugins in the WebView.
     * @return The string path to the WebView plugins.
     */
    public synchronized String getPluginsPath() {
        return mPluginsPath;
    }

    /**
     * Tell javascript to open windows automatically. This applies to the
     * javascript function window.open().
     * @param flag True if javascript can open windows automatically.
     */
    public synchronized void setJavaScriptCanOpenWindowsAutomatically(
            boolean flag) {
        if (mJavaScriptCanOpenWindowsAutomatically != flag) {
            mJavaScriptCanOpenWindowsAutomatically = flag;
            postSync();
        }
    }

    /**
     * Return true if javascript can open windows automatically.
     * @return True if javascript can open windows automatically during
     *         window.open().
     */
    public synchronized boolean getJavaScriptCanOpenWindowsAutomatically() {
        return mJavaScriptCanOpenWindowsAutomatically;
    }

    /**
     * Set the default text encoding name to use when decoding html pages.
     * @param encoding The text encoding name.
     */
    public synchronized void setDefaultTextEncodingName(String encoding) {
        if (encoding != null && !encoding.equals(mDefaultTextEncoding)) {
            mDefaultTextEncoding = encoding;
            postSync();
        }
    }

    /**
     * Get the default text encoding name.
     * @return The default text encoding name as a string.
     */
    public synchronized String getDefaultTextEncodingName() {
        return mDefaultTextEncoding;
    }

    /* Package api to grab the user agent string. */
    /*package*/ synchronized String getUserAgentString() {
        return mUserAgent;
    }

    /**
     * Tell the WebView whether it needs to set a node to have focus when
     * {@link WebView#requestFocus(int, android.graphics.Rect)} is called.
     * 
     * @param flag
     */
    public void setNeedInitialFocus(boolean flag) {
        if (mNeedInitialFocus != flag) {
            mNeedInitialFocus = flag;
        }
    }

    /* Package api to get the choice whether it needs to set initial focus. */
    /* package */ boolean getNeedInitialFocus() {
        return mNeedInitialFocus;
    }

    /**
     * Set the priority of the Render thread. Unlike the other settings, this
     * one only needs to be called once per process.
     * 
     * @param priority RenderPriority, can be normal, high or low.
     */
    public synchronized void setRenderPriority(RenderPriority priority) {
        if (mRenderPriority != priority) {
            mRenderPriority = priority;
            mEventHandler.sendMessage(Message.obtain(null,
                    EventHandler.PRIORITY));
        }
    }
    
    /**
     * Override the way the cache is used. The way the cache is used is based
>>>>>>> 54b6cfa... Initial Contribution
     * on the navigation option. For a normal page load, the cache is checked
     * and content is re-validated as needed. When navigating back, content is
     * not revalidated, instead the content is just pulled from the cache.
     * This function allows the client to override this behavior.
<<<<<<< HEAD
     *
     * @param mode one of the LOAD_ values
     */
    public void setCacheMode(int mode) {
        throw new MustOverrideException();
    }

    /**
     * Gets the current setting for overriding the cache mode. For a full
     * description, see the {@link #setCacheMode(int)} function.
     */
    public int getCacheMode() {
        throw new MustOverrideException();
    }
=======
     * @param mode One of the LOAD_ values.
     */
    public void setCacheMode(int mode) {
        if (mode != mOverrideCacheMode) {
            mOverrideCacheMode = mode;
        }
    }
    
    /**
     * Return the current setting for overriding the cache mode. For a full
     * description, see the {@link #setCacheMode(int)} function.
     */
    public int getCacheMode() {
        return mOverrideCacheMode;
    }

    /**
     * Transfer messages from the queue to the new WebCoreThread. Called from
     * WebCore thread.
     */
    /*package*/
    synchronized void syncSettingsAndCreateHandler(BrowserFrame frame) {
        mBrowserFrame = frame;
        if (android.util.Config.DEBUG) {
            junit.framework.Assert.assertTrue(frame.mNativeFrame != 0);
        }
        nativeSync(frame.mNativeFrame);
        mSyncPending = false;
        mEventHandler.createHandler();
    }

    private int pin(int size) {
        // FIXME: 72 is just an arbitrary max text size value.
        if (size < 1) {
            return 1;
        } else if (size > 72) {
            return 72;
        }
        return size;
    }

    /* Post a SYNC message to handle syncing the native settings. */
    private synchronized void postSync() {
        // Only post if a sync is not pending
        if (!mSyncPending) {
            mSyncPending = mEventHandler.sendMessage(
                    Message.obtain(null, EventHandler.SYNC));
        }
    }

    // Synchronize the native and java settings.
    private native void nativeSync(int nativeFrame);
>>>>>>> 54b6cfa... Initial Contribution
}
