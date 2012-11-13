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

import android.graphics.Bitmap;

<<<<<<< HEAD
import java.net.MalformedURLException;
import java.net.URL;

=======
>>>>>>> 54b6cfa... Initial Contribution
/**
 * A convenience class for accessing fields in an entry in the back/forward list
 * of a WebView. Each WebHistoryItem is a snapshot of the requested history
 * item. Each history item may be updated during the load of a page.
 * @see WebBackForwardList
 */
public class WebHistoryItem implements Cloneable {
    // Global identifier count.
    private static int sNextId = 0;
    // Unique identifier.
    private final int mId;
<<<<<<< HEAD
    // A point to a native WebHistoryItem instance which contains the actual data
    private int mNativeBridge;
=======
    // The title of this item's document.
    private String mTitle;
    // The base url of this item.
    private String mUrl;
>>>>>>> 54b6cfa... Initial Contribution
    // The favicon for this item.
    private Bitmap mFavicon;
    // The pre-flattened data used for saving the state.
    private byte[] mFlattenedData;
<<<<<<< HEAD
    // The apple-touch-icon url for use when adding the site to the home screen,
    // as obtained from a <link> element in the page.
    private String mTouchIconUrlFromLink;
    // If no <link> is specified, this holds the default location of the
    // apple-touch-icon.
    private String mTouchIconUrlServerDefault;
    // Custom client data that is not flattened or read by native code.
    private Object mCustomData;
=======
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Basic constructor that assigns a unique id to the item. Called by JNI
     * only.
     */
<<<<<<< HEAD
    private WebHistoryItem(int nativeBridge) {
        synchronized (WebHistoryItem.class) {
            mId = sNextId++;
        }
        mNativeBridge = nativeBridge;
        nativeRef(mNativeBridge);
    }

    protected void finalize() throws Throwable {
        if (mNativeBridge != 0) {
            nativeUnref(mNativeBridge);
            mNativeBridge = 0;
        }
=======
    private WebHistoryItem() {
        synchronized (WebHistoryItem.class) {
            mId = sNextId++;
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Construct a new WebHistoryItem with initial flattened data.
     * @param data The pre-flattened data coming from restoreState.
     */
    /*package*/ WebHistoryItem(byte[] data) {
<<<<<<< HEAD
=======
        mUrl = null; // This will be updated natively
>>>>>>> 54b6cfa... Initial Contribution
        mFlattenedData = data;
        synchronized (WebHistoryItem.class) {
            mId = sNextId++;
        }
    }

    /**
     * Construct a clone of a WebHistoryItem from the given item.
     * @param item The history item to clone.
     */
    private WebHistoryItem(WebHistoryItem item) {
<<<<<<< HEAD
        mFlattenedData = item.mFlattenedData;
        mId = item.mId;
        mFavicon = item.mFavicon;
        mNativeBridge = item.mNativeBridge;
        if (mNativeBridge != 0) {
            nativeRef(mNativeBridge);
        }
    }
=======
        mUrl = item.mUrl;
        mTitle = item.mTitle;
        mFlattenedData = item.mFlattenedData;
        mFavicon = item.mFavicon;
        mId = item.mId;
}
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Return an identifier for this history item. If an item is a copy of
     * another item, the identifiers will be the same even if they are not the
     * same object.
     * @return The id for this item.
<<<<<<< HEAD
     * @deprecated This method is now obsolete.
     */
    @Deprecated
=======
     */
>>>>>>> 54b6cfa... Initial Contribution
    public int getId() {
        return mId;
    }

    /**
     * Return the url of this history item. The url is the base url of this
     * history item. See getTargetUrl() for the url that is the actual target of
     * this history item.
     * @return The base url of this history item.
     * Note: The VM ensures 32-bit atomic read/write operations so we don't have
     * to synchronize this method.
     */
    public String getUrl() {
<<<<<<< HEAD
        if (mNativeBridge == 0) return null;
        return nativeGetUrl(mNativeBridge);
    }

    /**
     * Return the original url of this history item. This was the requested
     * url, the final url may be different as there might have been 
     * redirects while loading the site.
     * @return The original url of this history item.
     */
    public String getOriginalUrl() {
        if (mNativeBridge == 0) return null;
        return nativeGetOriginalUrl(mNativeBridge);
    }
    
    /**
=======
        return mUrl;
    }

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Return the document title of this history item.
     * @return The document title of this history item.
     * Note: The VM ensures 32-bit atomic read/write operations so we don't have
     * to synchronize this method.
     */
    public String getTitle() {
<<<<<<< HEAD
        if (mNativeBridge == 0) return null;
        return nativeGetTitle(mNativeBridge);
=======
        return mTitle;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Return the favicon of this history item or null if no favicon was found.
     * @return A Bitmap containing the favicon for this history item or null.
     * Note: The VM ensures 32-bit atomic read/write operations so we don't have
     * to synchronize this method.
     */
    public Bitmap getFavicon() {
<<<<<<< HEAD
        if (mFavicon == null && mNativeBridge != 0) {
            mFavicon = nativeGetFavicon(mNativeBridge);
        }
=======
>>>>>>> 54b6cfa... Initial Contribution
        return mFavicon;
    }

    /**
<<<<<<< HEAD
     * Return the touch icon url.
     * If no touch icon <link> tag was specified, returns
     * <host>/apple-touch-icon.png. The DownloadTouchIcon class that
     * attempts to retrieve the touch icon will handle the case where
     * that file does not exist. An icon set by a <link> tag is always
     * used in preference to an icon saved on the server.
     * @hide
     */
    public String getTouchIconUrl() {
        if (mTouchIconUrlFromLink != null) {
            return mTouchIconUrlFromLink;
        } else if (mTouchIconUrlServerDefault != null) {
            return mTouchIconUrlServerDefault;
        }

        try {
            URL url = new URL(getOriginalUrl());
            mTouchIconUrlServerDefault = new URL(url.getProtocol(), url.getHost(), url.getPort(),
                    "/apple-touch-icon.png").toString();
        } catch (MalformedURLException e) {
            return null;
        }
        return mTouchIconUrlServerDefault;
    }

    /**
     * Return the custom data provided by the client.
     * @hide
     */
    public Object getCustomData() {
        return mCustomData;
    }

    /**
     * Set the custom data field.
     * @param data An Object containing any data the client wishes to associate
     *             with the item.
     * @hide
     */
    public void setCustomData(Object data) {
        // NOTE: WebHistoryItems are used in multiple threads. However, the
        // public facing apis are all getters with the exception of this one
        // api. Since this api is exclusive to clients, we don't make any
        // promises about thread safety.
        mCustomData = data;
    }

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * Set the favicon.
     * @param icon A Bitmap containing the favicon for this history item.
     * Note: The VM ensures 32-bit atomic read/write operations so we don't have
     * to synchronize this method.
     */
    /*package*/ void setFavicon(Bitmap icon) {
        mFavicon = icon;
    }

    /**
<<<<<<< HEAD
     * Set the touch icon url. Will not overwrite an icon that has been
     * set already from a <link> tag, unless the new icon is precomposed.
     * @hide
     */
    /*package*/ void setTouchIconUrl(String url, boolean precomposed) {
        if (precomposed || mTouchIconUrlFromLink == null) {
            mTouchIconUrlFromLink = url;
        }
    }

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * Get the pre-flattened data.
     * Note: The VM ensures 32-bit atomic read/write operations so we don't have
     * to synchronize this method.
     */
    /*package*/ byte[] getFlattenedData() {
<<<<<<< HEAD
        if (mNativeBridge != 0) {
            return nativeGetFlattenedData(mNativeBridge);
        }
=======
>>>>>>> 54b6cfa... Initial Contribution
        return mFlattenedData;
    }

    /**
     * Inflate this item.
     * Note: The VM ensures 32-bit atomic read/write operations so we don't have
     * to synchronize this method.
     */
    /*package*/ void inflate(int nativeFrame) {
<<<<<<< HEAD
        mNativeBridge = inflate(nativeFrame, mFlattenedData);
        mFlattenedData = null;
=======
        inflate(nativeFrame, mFlattenedData);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Clone the history item for use by clients of WebView.
     */
    protected synchronized WebHistoryItem clone() {
        return new WebHistoryItem(this);
    }

    /* Natively inflate this item, this method is called in the WebCore thread.
     */
<<<<<<< HEAD
    private native int inflate(int nativeFrame, byte[] data);
    private native void nativeRef(int nptr);
    private native void nativeUnref(int nptr);
    private native String nativeGetTitle(int nptr);
    private native String nativeGetUrl(int nptr);
    private native String nativeGetOriginalUrl(int nptr);
    private native byte[] nativeGetFlattenedData(int nptr);
    private native Bitmap nativeGetFavicon(int nptr);

=======
    private native void inflate(int nativeFrame, byte[] data);

    /* Called by jni when the item is updated */
    private void update(String url, String title, Bitmap favicon, byte[] data) {
        mUrl = url;
        mTitle = title;
        mFavicon = favicon;
        mFlattenedData = data;
    }
>>>>>>> 54b6cfa... Initial Contribution
}
