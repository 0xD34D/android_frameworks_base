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

import android.content.Context;
import android.net.http.Headers;
<<<<<<< HEAD
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;


/**
 * Manages the HTTP cache used by an application's {@link WebView} instances.
 * @deprecated Access to the HTTP cache will be removed in a future release.
 */
// The class CacheManager provides the persistent cache of content that is
// received over the network. The component handles parsing of HTTP headers and
// utilizes the relevant cache headers to determine if the content should be
// stored and if so, how long it is valid for. Network requests are provided to
// this component and if they can not be resolved by the cache, the HTTP headers
// are attached, as appropriate, to the request for revalidation of content. The
// class also manages the cache size.
//
// CacheManager may only be used if your activity contains a WebView.
@Deprecated
=======
import android.os.FileUtils;
import android.util.Config;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;

/**
 * The class CacheManager provides the persistent cache of content that is
 * received over the network. The component handles parsing of HTTP headers and
 * utilizes the relevant cache headers to determine if the content should be
 * stored and if so, how long it is valid for. Network requests are provided to
 * this component and if they can not be resolved by the cache, the HTTP headers
 * are attached, as appropriate, to the request for revalidation of content. The
 * class also manages the cache size.
 */
>>>>>>> 54b6cfa... Initial Contribution
public final class CacheManager {

    private static final String LOGTAG = "cache";

    static final String HEADER_KEY_IFMODIFIEDSINCE = "if-modified-since";
    static final String HEADER_KEY_IFNONEMATCH = "if-none-match";

<<<<<<< HEAD
    private static File mBaseDir;
    
    /**
     * Represents a resource stored in the HTTP cache. Instances of this class
     * can be obtained by calling
     * {@link CacheManager#getCacheFile CacheManager.getCacheFile(String, Map<String, String>))}.
     *
     * @deprecated Access to the HTTP cache will be removed in a future release.
     */
    @Deprecated
=======
    private static final String NO_STORE = "no-store";
    private static final String NO_CACHE = "no-cache";
    private static final String MAX_AGE = "max-age";

    private static long CACHE_THRESHOLD = 6 * 1024 * 1024;
    private static long CACHE_TRIM_AMOUNT = 2 * 1024 * 1024;

    private static boolean mDisabled;

    // Reference count the enable/disable transaction
    private static int mRefCount;

    private static WebViewDatabase mDataBase;
    private static File mBaseDir;

>>>>>>> 54b6cfa... Initial Contribution
    public static class CacheResult {
        // these fields are saved to the database
        int httpStatusCode;
        long contentLength;
        long expires;
<<<<<<< HEAD
        String expiresString;
=======
>>>>>>> 54b6cfa... Initial Contribution
        String localPath;
        String lastModified;
        String etag;
        String mimeType;
        String location;
        String encoding;
<<<<<<< HEAD
        String contentdisposition;
        String crossDomain;
=======
>>>>>>> 54b6cfa... Initial Contribution

        // these fields are NOT saved to the database
        InputStream inStream;
        OutputStream outStream;
        File outFile;

<<<<<<< HEAD
        /**
         * Gets the status code of this cache entry.
         *
         * @return the status code of this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public int getHttpStatusCode() {
            return httpStatusCode;
        }

<<<<<<< HEAD
        /**
         * Gets the content length of this cache entry.
         *
         * @return the content length of this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public long getContentLength() {
            return contentLength;
        }

<<<<<<< HEAD
        /**
         * Gets the path of the file used to store the content of this cache
         * entry, relative to the base directory of the cache. See
         * {@link CacheManager#getCacheFileBaseDir CacheManager.getCacheFileBaseDir()}.
         *
         * @return the path of the file used to store this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public String getLocalPath() {
            return localPath;
        }

<<<<<<< HEAD
        /**
         * Gets the expiry date of this cache entry, expressed in milliseconds
         * since midnight, January 1, 1970 UTC.
         *
         * @return the expiry date of this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public long getExpires() {
            return expires;
        }

<<<<<<< HEAD
        /**
         * Gets the expiry date of this cache entry, expressed as a string.
         *
         * @return the expiry date of this cache entry
         *
         */
        public String getExpiresString() {
            return expiresString;
        }

        /**
         * Gets the date at which this cache entry was last modified, expressed
         * as a string.
         *
         * @return the date at which this cache entry was last modified
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public String getLastModified() {
            return lastModified;
        }

<<<<<<< HEAD
        /**
         * Gets the entity tag of this cache entry.
         *
         * @return the entity tag of this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public String getETag() {
            return etag;
        }

<<<<<<< HEAD
        /**
         * Gets the MIME type of this cache entry.
         *
         * @return the MIME type of this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public String getMimeType() {
            return mimeType;
        }

<<<<<<< HEAD
        /**
         * Gets the value of the HTTP 'Location' header with which this cache
         * entry was received.
         *
         * @return the HTTP 'Location' header for this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public String getLocation() {
            return location;
        }

<<<<<<< HEAD
        /**
         * Gets the encoding of this cache entry.
         *
         * @return the encoding of this cache entry
         */
=======
>>>>>>> 54b6cfa... Initial Contribution
        public String getEncoding() {
            return encoding;
        }

<<<<<<< HEAD
        /**
         * Gets the value of the HTTP 'Content-Disposition' header with which
         * this cache entry was received.
         *
         * @return the HTTP 'Content-Disposition' header for this cache entry
         *
         */
        public String getContentDisposition() {
            return contentdisposition;
        }

        /**
         * Gets the input stream to the content of this cache entry, to allow
         * content to be read. See
         * {@link CacheManager#getCacheFile CacheManager.getCacheFile(String, Map<String, String>)}.
         *
         * @return an input stream to the content of this cache entry
         */
=======
        // For out-of-package access to the underlying streams.
>>>>>>> 54b6cfa... Initial Contribution
        public InputStream getInputStream() {
            return inStream;
        }

<<<<<<< HEAD
        /**
         * Gets an output stream to the content of this cache entry, to allow
         * content to be written. See
         * {@link CacheManager#saveCacheFile CacheManager.saveCacheFile(String, CacheResult)}.
         *
         * @return an output stream to the content of this cache entry
         */
        // Note that this is always null for objects returned by getCacheFile()!
=======
>>>>>>> 54b6cfa... Initial Contribution
        public OutputStream getOutputStream() {
            return outStream;
        }

<<<<<<< HEAD

        /**
         * Sets an input stream to the content of this cache entry.
         *
         * @param stream an input stream to the content of this cache entry
         */
=======
        // These fields can be set manually.
>>>>>>> 54b6cfa... Initial Contribution
        public void setInputStream(InputStream stream) {
            this.inStream = stream;
        }

<<<<<<< HEAD
        /**
         * Sets the encoding of this cache entry.
         *
         * @param encoding the encoding of this cache entry
         */
        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        /**
         * @hide
         */
        public void setContentLength(long contentLength) {
            this.contentLength = contentLength;
        }
    }

    /**
     * Initializes the HTTP cache. This method must be called before any
     * CacheManager methods are used. Note that this is called automatically
     * when a {@link WebView} is created.
     *
     * @param context the application context
     */
    static void init(Context context) {
        // This isn't actually where the real cache lives, but where we put files for the
        // purpose of getCacheFile().
        mBaseDir = new File(context.getCacheDir(), "webviewCacheChromiumStaging");
        if (!mBaseDir.exists()) {
            mBaseDir.mkdirs();
=======
        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }
    }

    /**
     * initialize the CacheManager. WebView should handle this for each process.
     * 
     * @param context The application context.
     */
    static void init(Context context) {
        mDataBase = WebViewDatabase.getInstance(context);
        mBaseDir = new File(context.getCacheDir(), "webviewCache");
        if (!mBaseDir.exists()) {
            if(!mBaseDir.mkdirs()) {
                Log.w(LOGTAG, "Unable to create webviewCache directory");
                return;
            }
            FileUtils.setPermissions(
                    mBaseDir.toString(),
                    FileUtils.S_IRWXU|FileUtils.S_IRWXG|FileUtils.S_IXOTH,
                    -1, -1);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Gets the base directory in which the files used to store the contents of
     * cache entries are placed. See
     * {@link CacheManager.CacheResult#getLocalPath CacheManager.CacheResult.getLocalPath()}.
     *
     * @return the base directory of the cache
     * @deprecated Access to the HTTP cache will be removed in a future release.
     */
    @Deprecated
=======
     * get the base directory of the cache. With localPath of the CacheResult,
     * it identifies the cache file.
     * 
     * @return File The base directory of the cache.
     */
>>>>>>> 54b6cfa... Initial Contribution
    public static File getCacheFileBaseDir() {
        return mBaseDir;
    }

    /**
<<<<<<< HEAD
     * Gets whether the HTTP cache is disabled.
     *
     * @return true if the HTTP cache is disabled
     * @deprecated Access to the HTTP cache will be removed in a future release.
     */
    @Deprecated
    public static boolean cacheDisabled() {
        return false;
    }

    /**
     * Starts a cache transaction. Returns true if this is the only running
     * transaction. Otherwise, this transaction is nested inside currently
     * running transactions and false is returned.
     *
     * @return true if this is the only running transaction
     * @deprecated This method no longer has any effect and always returns false.
     */
    @Deprecated
    public static boolean startCacheTransaction() {
        return false;
    }

    /**
     * Ends the innermost cache transaction and returns whether this was the
     * only running transaction.
     *
     * @return true if this was the only running transaction
     * @deprecated This method no longer has any effect and always returns false.
     */
    @Deprecated
    public static boolean endCacheTransaction() {
        return false;
    }

    /**
     * Gets the cache entry for the specified URL, or null if none is found.
     * If a non-null value is provided for the HTTP headers map, and the cache
     * entry needs validation, appropriate headers will be added to the map.
     * The input stream of the CacheEntry object should be closed by the caller
     * when access to the underlying file is no longer required.
     *
     * @param url the URL for which a cache entry is requested
     * @param headers a map from HTTP header name to value, to be populated
     *                for the returned cache entry
     * @return the cache entry for the specified URL
     * @deprecated Access to the HTTP cache will be removed in a future release.
     */
    @Deprecated
    public static CacheResult getCacheFile(String url,
            Map<String, String> headers) {
        return getCacheFile(url, 0, headers);
    }

    static CacheResult getCacheFile(String url, long postIdentifier,
            Map<String, String> headers) {
        CacheResult result = nativeGetCacheResult(url);
        if (result == null) {
            return null;
        }
        // A temporary local file will have been created native side and localPath set
        // appropriately.
        File src = new File(mBaseDir, result.localPath);
        try {
            // Open the file here so that even if it is deleted, the content
            // is still readable by the caller until close() is called.
            result.inStream = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            Log.v(LOGTAG, "getCacheFile(): Failed to open file: " + e);
            // TODO: The files in the cache directory can be removed by the
            // system. If it is gone, what should we do?
            return null;
        }

        // A null value for headers is used by CACHE_MODE_CACHE_ONLY to imply
        // that we should provide the cache result even if it is expired.
        // Note that a negative expires value means a time in the far future.
=======
     * set the flag to control whether cache is enabled or disabled
     * 
     * @param disabled true to disable the cache
     */
    // only called from WebCore thread
    static void setCacheDisabled(boolean disabled) {
        if (disabled == mDisabled) {
            return;
        }
        mDisabled = disabled;
        if (mDisabled) {
            removeAllCacheFiles();
        }
    }

    /**
     * get the state of the current cache, enabled or disabled
     * 
     * @return return if it is disabled
     */
    public static boolean cacheDisabled() {
        return mDisabled;
    }

    // only called from WebCore thread
    // make sure to call enableTransaction/disableTransaction in pair
    static boolean enableTransaction() {
        if (++mRefCount == 1) {
            mDataBase.startCacheTransaction();
            return true;
        }
        return false;
    }

    // only called from WebCore thread
    // make sure to call enableTransaction/disableTransaction in pair
    static boolean disableTransaction() {
        if (mRefCount == 0) {
            Log.e(LOGTAG, "disableTransaction is out of sync");
        }
        if (--mRefCount == 0) {
            mDataBase.endCacheTransaction();
            return true;
        }
        return false;
    }

    // only called from WebCore thread
    // make sure to call startCacheTransaction/endCacheTransaction in pair
    public static boolean startCacheTransaction() {
        return mDataBase.startCacheTransaction();
    }

    // only called from WebCore thread
    // make sure to call startCacheTransaction/endCacheTransaction in pair
    public static boolean endCacheTransaction() {
        return mDataBase.endCacheTransaction();
    }

    /**
     * Given a url, returns the CacheResult if exists. Otherwise returns null.
     * If headers are provided and a cache needs validation,
     * HEADER_KEY_IFNONEMATCH or HEADER_KEY_IFMODIFIEDSINCE will be set in the
     * cached headers.
     * 
     * @return the CacheResult for a given url
     */
    // only called from WebCore thread
    public static CacheResult getCacheFile(String url,
            Map<String, String> headers) {
        if (mDisabled) {
            return null;
        }

        CacheResult result = mDataBase.getCache(url);
        if (result != null) {
            if (result.contentLength == 0) {
                if (result.httpStatusCode != 301
                        && result.httpStatusCode != 302
                        && result.httpStatusCode != 307) {
                    // this should not happen. If it does, remove it.
                    mDataBase.removeCache(url);
                    return null;
                }
            } else {
                File src = new File(mBaseDir, result.localPath);
                try {
                    // open here so that even the file is deleted, the content
                    // is still readable by the caller until close() is called
                    result.inStream = new FileInputStream(src);
                } catch (FileNotFoundException e) {
                    // the files in the cache directory can be removed by the
                    // system. If it is gone, clean up the database
                    mDataBase.removeCache(url);
                    return null;
                }
            }
        } else {
            return null;
        }

        // null headers request coming from CACHE_MODE_CACHE_ONLY
        // which implies that it needs cache even it is expired.
        // negative expires means time in the far future.
>>>>>>> 54b6cfa... Initial Contribution
        if (headers != null && result.expires >= 0
                && result.expires <= System.currentTimeMillis()) {
            if (result.lastModified == null && result.etag == null) {
                return null;
            }
<<<<<<< HEAD
            // Return HEADER_KEY_IFNONEMATCH or HEADER_KEY_IFMODIFIEDSINCE
            // for requesting validation.
=======
            // return HEADER_KEY_IFNONEMATCH or HEADER_KEY_IFMODIFIEDSINCE
            // for requesting validation
>>>>>>> 54b6cfa... Initial Contribution
            if (result.etag != null) {
                headers.put(HEADER_KEY_IFNONEMATCH, result.etag);
            }
            if (result.lastModified != null) {
                headers.put(HEADER_KEY_IFMODIFIEDSINCE, result.lastModified);
            }
        }

<<<<<<< HEAD
        if (DebugFlags.CACHE_MANAGER) {
=======
        if (Config.LOGV) {
>>>>>>> 54b6cfa... Initial Contribution
            Log.v(LOGTAG, "getCacheFile for url " + url);
        }

        return result;
    }

    /**
<<<<<<< HEAD
     * Given a URL and its full headers, gets a CacheResult if a local cache
=======
     * Given a url and its full headers, returns CacheResult if a local cache
>>>>>>> 54b6cfa... Initial Contribution
     * can be stored. Otherwise returns null. The mimetype is passed in so that
     * the function can use the mimetype that will be passed to WebCore which
     * could be different from the mimetype defined in the headers.
     * forceCache is for out-of-package callers to force creation of a
     * CacheResult, and is used to supply surrogate responses for URL
     * interception.
<<<<<<< HEAD
     *
     * @return a CacheResult for a given URL
     */
    static CacheResult createCacheFile(String url, int statusCode,
            Headers headers, String mimeType, boolean forceCache) {
        // This method is public but hidden. We break functionality.
        return null;
    }

    /**
     * Adds a cache entry to the HTTP cache for the specicifed URL. Also closes
     * the cache entry's output stream.
     *
     * @param url the URL for which the cache entry should be added
     * @param cacheResult the cache entry to add
     * @deprecated Access to the HTTP cache will be removed in a future release.
     */
    @Deprecated
    public static void saveCacheFile(String url, CacheResult cacheResult) {
        saveCacheFile(url, 0, cacheResult);
    }

    static void saveCacheFile(String url, long postIdentifier,
            CacheResult cacheRet) {
=======
     * @return CacheResult for a given url
     * @hide - hide createCacheFile since it has a parameter of type headers, which is
     * in a hidden package.
     */
    // can be called from any thread
    public static CacheResult createCacheFile(String url, int statusCode,
            Headers headers, String mimeType, boolean forceCache) {
        if (!forceCache && mDisabled) {
            return null;
        }

        CacheResult ret = parseHeaders(statusCode, headers, mimeType);
        if (ret != null) {
            setupFiles(url, ret);
            try {
                ret.outStream = new FileOutputStream(ret.outFile);
            } catch (FileNotFoundException e) {
                return null;
            }
            ret.mimeType = mimeType;
        }

        return ret;
    }

    /**
     * Save the info of a cache file for a given url to the CacheMap so that it
     * can be reused later
     */
    // only called from WebCore thread
    public static void saveCacheFile(String url, CacheResult cacheRet) {
>>>>>>> 54b6cfa... Initial Contribution
        try {
            cacheRet.outStream.close();
        } catch (IOException e) {
            return;
        }

<<<<<<< HEAD
        // This method is exposed in the public API but the API provides no
        // way to obtain a new CacheResult object with a non-null output
        // stream ...
        // - CacheResult objects returned by getCacheFile() have a null
        //   output stream.
        // - new CacheResult objects have a null output stream and no
        //   setter is provided.
        // Since this method throws a null pointer exception in this case,
        // it is effectively useless from the point of view of the public
        // API.
        //
        // With the Chromium HTTP stack we continue to throw the same
        // exception for 'backwards compatibility' with the Android HTTP
        // stack.
        //
        // This method is not used from within this package, and for public API
        // use, we should already have thrown an exception above.
        assert false;
    }

    /**
     * Removes all cache files.
     *
     * @return whether the removal succeeded
     */
    static boolean removeAllCacheFiles() {
        // delete cache files in a separate thread to not block UI.
=======
        if (!cacheRet.outFile.exists()) {
            // the file in the cache directory can be removed by the system
            return;
        }

        cacheRet.contentLength = cacheRet.outFile.length();
        if (cacheRet.httpStatusCode == 301
                || cacheRet.httpStatusCode == 302
                || cacheRet.httpStatusCode == 307) {
            // location is in database, no need to keep the file
            cacheRet.contentLength = 0;
            cacheRet.localPath = new String();
            cacheRet.outFile.delete();
        } else if (cacheRet.contentLength == 0) {
            cacheRet.outFile.delete();
            return;
        }

        mDataBase.addCache(url, cacheRet);

        if (Config.LOGV) {
            Log.v(LOGTAG, "saveCacheFile for url " + url);
        }
    }

    /**
     * remove all cache files
     * 
     * @return true if it succeeds
     */
    // only called from WebCore thread
    static boolean removeAllCacheFiles() {
        // delete cache in a separate thread to not block UI.
>>>>>>> 54b6cfa... Initial Contribution
        final Runnable clearCache = new Runnable() {
            public void run() {
                // delete all cache files
                try {
                    String[] files = mBaseDir.list();
<<<<<<< HEAD
                    // if mBaseDir doesn't exist, files can be null.
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            File f = new File(mBaseDir, files[i]);
                            if (!f.delete()) {
                                Log.e(LOGTAG, f.getPath() + " delete failed.");
                            }
                        }
=======
                    for (int i = 0; i < files.length; i++) {
                        new File(mBaseDir, files[i]).delete();
>>>>>>> 54b6cfa... Initial Contribution
                    }
                } catch (SecurityException e) {
                    // Ignore SecurityExceptions.
                }
<<<<<<< HEAD
=======
                // delete database
                mDataBase.clearCache();
>>>>>>> 54b6cfa... Initial Contribution
            }
        };
        new Thread(clearCache).start();
        return true;
    }

<<<<<<< HEAD
    private static native CacheResult nativeGetCacheResult(String url);
=======
    /**
     * Return true if the cache is empty.
     */
    // only called from WebCore thread
    static boolean cacheEmpty() {
        return mDataBase.hasCache();
    }

    // only called from WebCore thread
    static void trimCacheIfNeeded() {
        if (mDataBase.getCacheTotalSize() > CACHE_THRESHOLD) {
            ArrayList<String> pathList = mDataBase.trimCache(CACHE_TRIM_AMOUNT);
            int size = pathList.size();
            for (int i = 0; i < size; i++) {
                new File(mBaseDir, pathList.get(i)).delete();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void setupFiles(String url, CacheResult cacheRet) {
        if (true) {
            // Note: SHA1 is much stronger hash. But the cost of setupFiles() is
            // 3.2% cpu time for a fresh load of nytimes.com. While a simple
            // String.hashCode() is only 0.6%. If adding the collision resolving
            // to String.hashCode(), it makes the cpu time to be 1.6% for a 
            // fresh load, but 5.3% for the worst case where all the files 
            // already exist in the file system, but database is gone. So it
            // needs to resolve collision for every file at least once.
            int hashCode = url.hashCode();
            StringBuffer ret = new StringBuffer(8);
            appendAsHex(hashCode, ret);
            String path = ret.toString();
            File file = new File(mBaseDir, path);
            if (true) {
                boolean checkOldPath = true;
                // Check hash collision. If the hash file doesn't exist, just
                // continue. There is a chance that the old cache file is not
                // same as the hash file. As mDataBase.getCache() is more 
                // expansive than "leak" a file until clear cache, don't bother.
                // If the hash file exists, make sure that it is same as the 
                // cache file. If it is not, resolve the collision.
                while (file.exists()) {
                    if (checkOldPath) {
                        // as this is called from http thread through 
                        // createCacheFile, we need endCacheTransaction before 
                        // database access.
                        WebViewCore.endCacheTransaction();
                        CacheResult oldResult = mDataBase.getCache(url);
                        WebViewCore.startCacheTransaction();
                        if (oldResult != null && oldResult.contentLength > 0) {
                            if (path.equals(oldResult.localPath)) {
                                path = oldResult.localPath;
                            } else {
                                path = oldResult.localPath;
                                file = new File(mBaseDir, path);
                            }
                            break;
                        }
                        checkOldPath = false;
                    }
                    ret = new StringBuffer(8);
                    appendAsHex(++hashCode, ret);
                    path = ret.toString();
                    file = new File(mBaseDir, path);
                }
            }
            cacheRet.localPath = path;
            cacheRet.outFile = file;
        } else {
            // get hash in byte[]
            Digest digest = new SHA1Digest();
            int digestLen = digest.getDigestSize();
            byte[] hash = new byte[digestLen];
            int urlLen = url.length();
            byte[] data = new byte[urlLen];
            url.getBytes(0, urlLen, data, 0);
            digest.update(data, 0, urlLen);
            digest.doFinal(hash, 0);
            // convert byte[] to hex String
            StringBuffer result = new StringBuffer(2 * digestLen);
            for (int i = 0; i < digestLen; i = i + 4) {
                int h = (0x00ff & hash[i]) << 24 | (0x00ff & hash[i + 1]) << 16
                        | (0x00ff & hash[i + 2]) << 8 | (0x00ff & hash[i + 3]);
                appendAsHex(h, result);
            }
            cacheRet.localPath = result.toString();
            cacheRet.outFile = new File(mBaseDir, cacheRet.localPath);
        }
    }

    private static void appendAsHex(int i, StringBuffer ret) {
        String hex = Integer.toHexString(i);
        switch (hex.length()) {
            case 1:
                ret.append("0000000");
                break;
            case 2:
                ret.append("000000");
                break;
            case 3:
                ret.append("00000");
                break;
            case 4:
                ret.append("0000");
                break;
            case 5:
                ret.append("000");
                break;
            case 6:
                ret.append("00");
                break;
            case 7:
                ret.append("0");
                break;
        }
        ret.append(hex);
    }

    private static CacheResult parseHeaders(int statusCode, Headers headers,
            String mimeType) {
        // TODO: if authenticated or secure, return null
        CacheResult ret = new CacheResult();
        ret.httpStatusCode = statusCode;

        String location = headers.getLocation();
        if (location != null) ret.location = location;

        ret.expires = -1;
        String expires = headers.getExpires();
        if (expires != null) {
            try {
                ret.expires = HttpDateTime.parse(expires);
            } catch (IllegalArgumentException ex) {
                // Take care of the special "-1" and "0" cases
                if ("-1".equals(expires) || "0".equals(expires)) {
                    // make it expired, but can be used for history navigation
                    ret.expires = 0;
                } else {
                    Log.e(LOGTAG, "illegal expires: " + expires);
                }
            }
        }

        String lastModified = headers.getLastModified();
        if (lastModified != null) ret.lastModified = lastModified;

        String etag = headers.getEtag();
        if (etag != null) ret.etag = etag;

        String cacheControl = headers.getCacheControl();
        if (cacheControl != null) {
            String[] controls = cacheControl.toLowerCase().split("[ ,;]");
            for (int i = 0; i < controls.length; i++) {
                if (NO_STORE.equals(controls[i])) {
                    return null;
                }
                // According to the spec, 'no-cache' means that the content
                // must be re-validated on every load. It does not mean that
                // the content can not be cached. set to expire 0 means it
                // can only be used in CACHE_MODE_CACHE_ONLY case
                if (NO_CACHE.equals(controls[i])) {
                    ret.expires = 0;
                } else if (controls[i].startsWith(MAX_AGE)) {
                    int separator = controls[i].indexOf('=');
                    if (separator < 0) {
                        separator = controls[i].indexOf(':');
                    }
                    if (separator > 0) {
                        String s = controls[i].substring(separator + 1);
                        try {
                            long sec = Long.parseLong(s);
                            if (sec >= 0) {
                                ret.expires = System.currentTimeMillis() + 1000
                                        * sec;
                            }
                        } catch (NumberFormatException ex) {
                            if ("1d".equals(s)) {
                                // Take care of the special "1d" case
                                ret.expires = System.currentTimeMillis() + 86400000; // 24*60*60*1000
                            } else {
                                Log.e(LOGTAG, "exception in parseHeaders for "
                                        + "max-age:"
                                        + controls[i].substring(separator + 1));
                                ret.expires = 0;
                            }
                        }
                    }
                }
            }
        }

        // According to RFC 2616 section 14.32:
        // HTTP/1.1 caches SHOULD treat "Pragma: no-cache" as if the
        // client had sent "Cache-Control: no-cache"
        if (NO_CACHE.equals(headers.getPragma())) {
            ret.expires = 0;
        }

        // According to RFC 2616 section 13.2.4, if an expiration has not been
        // explicitly defined a heuristic to set an expiration may be used.
        if (ret.expires == -1) {
            if (ret.httpStatusCode == 301) {
                // If it is a permanent redirect, and it did not have an
                // explicit cache directive, then it never expires
                ret.expires = Long.MAX_VALUE;
            } else if (ret.httpStatusCode == 302 || ret.httpStatusCode == 307) {
                // If it is temporary redirect, expires
                ret.expires = 0;
            } else if (ret.lastModified == null) {
                // When we have no last-modified, then expire the content with
                // in 24hrs as, according to the RFC, longer time requires a
                // warning 113 to be added to the response.

                // Only add the default expiration for non-html markup. Some
                // sites like news.google.com have no cache directives.
                if (!mimeType.startsWith("text/html")) {
                    ret.expires = System.currentTimeMillis() + 86400000; // 24*60*60*1000
                } else {
                    // Setting a expires as zero will cache the result for
                    // forward/back nav.
                    ret.expires = 0;
                }
            } else {
                // If we have a last-modified value, we could use it to set the
                // expiration. Suggestion from RFC is 10% of time since
                // last-modified. As we are on mobile, loads are expensive,
                // increasing this to 20%.

                // 24 * 60 * 60 * 1000
                long lastmod = System.currentTimeMillis() + 86400000;
                try {
                    lastmod = HttpDateTime.parse(ret.lastModified);
                } catch (IllegalArgumentException ex) {
                    Log.e(LOGTAG, "illegal lastModified: " + ret.lastModified);
                }
                long difference = System.currentTimeMillis() - lastmod;
                if (difference > 0) {
                    ret.expires = System.currentTimeMillis() + difference / 5;
                } else {
                    // last modified is in the future, expire the content
                    // on the last modified
                    ret.expires = lastmod;
                }
            }
        }

        return ret;
    }
>>>>>>> 54b6cfa... Initial Contribution
}
