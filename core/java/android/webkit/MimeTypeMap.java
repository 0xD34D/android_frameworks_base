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
import android.text.TextUtils;
import java.util.regex.Pattern;
import libcore.net.MimeUtils;

/**
 * Two-way map that maps MIME-types to file extensions and vice versa.
 *
 * <p>See also {@link java.net.URLConnection#guessContentTypeFromName}
 * and {@link java.net.URLConnection#guessContentTypeFromStream}. This
 * class and {@code URLConnection} share the same MIME-type database.
 */
public class MimeTypeMap {
    private static final MimeTypeMap sMimeTypeMap = new MimeTypeMap();

    private MimeTypeMap() {
=======
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Two-way map that maps MIME-types to file extensions and vice versa.
 */
public /* package */ class MimeTypeMap {

    /**
     * Singleton MIME-type map instance:
     */
    private static MimeTypeMap sMimeTypeMap;

    /**
     * MIME-type to file extension mapping:
     */
    private HashMap<String, String> mMimeTypeToExtensionMap;

    /**
     * File extension to MIME type mapping:
     */
    private HashMap<String, String> mExtensionToMimeTypeMap;


    /**
     * Creates a new MIME-type map.
     */
    private MimeTypeMap() {
        mMimeTypeToExtensionMap = new HashMap<String, String>();
        mExtensionToMimeTypeMap = new HashMap<String, String>();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Returns the file extension or an empty string iff there is no
<<<<<<< HEAD
     * extension. This method is a convenience method for obtaining the
     * extension of a url and has undefined results for other Strings.
     * @param url
     * @return The file extension of the given url.
     */
    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

=======
     * extension.
     */
    public static String getFileExtensionFromUrl(String url) {
        if (url != null && url.length() > 0) {
>>>>>>> 54b6cfa... Initial Contribution
            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }
<<<<<<< HEAD

=======
>>>>>>> 54b6cfa... Initial Contribution
            int filenamePos = url.lastIndexOf('/');
            String filename =
                0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
<<<<<<< HEAD
            if (!filename.isEmpty() &&
                Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", filename)) {
=======
            if (filename.length() > 0 &&
                Pattern.matches("[a-zA-Z_0-9\\.\\-]+", filename)) {
>>>>>>> 54b6cfa... Initial Contribution
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }

    /**
<<<<<<< HEAD
     * Return true if the given MIME type has an entry in the map.
     * @param mimeType A MIME type (i.e. text/plain)
     * @return True iff there is a mimeType entry in the map.
     */
    public boolean hasMimeType(String mimeType) {
        return MimeUtils.hasMimeType(mimeType);
    }

    /**
     * Return the MIME type for the given extension.
     * @param extension A file extension without the leading '.'
     * @return The MIME type for the given extension or null iff there is none.
     */
    public String getMimeTypeFromExtension(String extension) {
        return MimeUtils.guessMimeTypeFromExtension(extension);
    }

    // Static method called by jni.
    private static String mimeTypeFromExtension(String extension) {
        return MimeUtils.guessMimeTypeFromExtension(extension);
    }

    /**
     * Return true if the given extension has a registered MIME type.
     * @param extension A file extension without the leading '.'
     * @return True iff there is an extension entry in the map.
     */
    public boolean hasExtension(String extension) {
        return MimeUtils.hasExtension(extension);
    }

    /**
     * Return the registered extension for the given MIME type. Note that some
     * MIME types map to multiple extensions. This call will return the most
     * common extension for the given MIME type.
     * @param mimeType A MIME type (i.e. text/plain)
     * @return The extension for the given MIME type or null iff there is none.
     */
    public String getExtensionFromMimeType(String mimeType) {
        return MimeUtils.guessExtensionFromMimeType(mimeType);
    }

    /**
     * If the given MIME type is null, or one of the "generic" types (text/plain
     * or application/octet-stream) map it to a type that Android can deal with.
     * If the given type is not generic, return it unchanged.
     *
     * @param mimeType MIME type provided by the server.
     * @param url URL of the data being loaded.
     * @param contentDisposition Content-disposition header given by the server.
     * @return The MIME type that should be used for this data.
     */
    /* package */ String remapGenericMimeType(String mimeType, String url,
            String contentDisposition) {
        // If we have one of "generic" MIME types, try to deduce
        // the right MIME type from the file extension (if any):
        if ("text/plain".equals(mimeType) ||
                "application/octet-stream".equals(mimeType)) {

            // for attachment, use the filename in the Content-Disposition
            // to guess the mimetype
            String filename = null;
            if (contentDisposition != null) {
                filename = URLUtil.parseContentDisposition(contentDisposition);
            }
            if (filename != null) {
                url = filename;
            }
            String extension = getFileExtensionFromUrl(url);
            String newMimeType = getMimeTypeFromExtension(extension);
            if (newMimeType != null) {
                mimeType = newMimeType;
            }
        } else if ("text/vnd.wap.wml".equals(mimeType)) {
            // As we don't support wml, render it as plain text
            mimeType = "text/plain";
        } else {
            // It seems that xhtml+xml and vnd.wap.xhtml+xml mime
            // subtypes are used interchangeably. So treat them the same.
            if ("application/vnd.wap.xhtml+xml".equals(mimeType)) {
                mimeType = "application/xhtml+xml";
            }
        }
        return mimeType;
    }

    /**
     * Get the singleton instance of MimeTypeMap.
     * @return The singleton instance of the MIME-type map.
     */
    public static MimeTypeMap getSingleton() {
=======
     * Load an entry into the map. This does not check if the item already
     * exists, it trusts the caller!
     */
    private void loadEntry(String mimeType, String extension, 
            boolean textType) {
        //
        // if we have an existing x --> y mapping, we do not want to
        // override it with another mapping x --> ?
        // this is mostly because of the way the mime-type map below
        // is constructed (if a mime type maps to several extensions
        // the first extension is considered the most popular and is
        // added first; we do not want to overwrite it later).
        //
        if (!mMimeTypeToExtensionMap.containsKey(mimeType)) {
            mMimeTypeToExtensionMap.put(mimeType, extension);
        }

        //
        // here, we don't want to map extensions to text MIME types;
        // otherwise, we will start replacing generic text/plain and
        // text/html with text MIME types that our platform does not
        // understand.
        //
        if (!textType) {
            mExtensionToMimeTypeMap.put(extension, mimeType);
        }
    }

    /**
     * @return True iff there is a mimeType entry in the map.
     */
    public boolean hasMimeType(String mimeType) {
        if (mimeType != null && mimeType.length() > 0) {
            return mMimeTypeToExtensionMap.containsKey(mimeType);
        }

        return false;
    }

    /**
     * @return The extension for the MIME type or null iff there is none.
     */
    public String getMimeTypeFromExtension(String extension) {
        if (extension != null && extension.length() > 0) {
            return mExtensionToMimeTypeMap.get(extension);
        }

        return null;
    }

    /**
     * @return True iff there is an extension entry in the map.
     */
    public boolean hasExtension(String extension) {
        if (extension != null && extension.length() > 0) {
            return mExtensionToMimeTypeMap.containsKey(extension);
        }

        return false;
    }

    /**
     * @return The MIME type for the extension or null iff there is none.
     */
    public String getExtensionFromMimeType(String mimeType) {
        if (mimeType != null && mimeType.length() > 0) {
            return mMimeTypeToExtensionMap.get(mimeType);
        }

        return null;
    }

    /**
     * @return The singleton instance of the MIME-type map.
     */
    public static MimeTypeMap getSingleton() {
        if (sMimeTypeMap == null) {
            sMimeTypeMap = new MimeTypeMap();

            // The following table is based on /etc/mime.types data minus
            // chemical/* MIME types and MIME types that don't map to any
            // file extensions. We also exclude top-level domain names to
            // deal with cases like:
            //
            // mail.google.com/a/google.com
            //
            // and "active" MIME types (due to potential security issues).
            //
            // Also, notice that not all data from this table is actually
            // added (see loadEntry method for more details).

            sMimeTypeMap.loadEntry("application/andrew-inset", "ez", false);
            sMimeTypeMap.loadEntry("application/dsptype", "tsp", false);
            sMimeTypeMap.loadEntry("application/futuresplash", "spl", false);
            sMimeTypeMap.loadEntry("application/hta", "hta", false);
            sMimeTypeMap.loadEntry("application/mac-binhex40", "hqx", false);
            sMimeTypeMap.loadEntry("application/mac-compactpro", "cpt", false);
            sMimeTypeMap.loadEntry("application/mathematica", "nb", false);
            sMimeTypeMap.loadEntry("application/msaccess", "mdb", false);
            sMimeTypeMap.loadEntry("application/oda", "oda", false);
            sMimeTypeMap.loadEntry("application/ogg", "ogg", false);
            sMimeTypeMap.loadEntry("application/pdf", "pdf", false);
            sMimeTypeMap.loadEntry("application/pgp-keys", "key", false);
            sMimeTypeMap.loadEntry("application/pgp-signature", "pgp", false);
            sMimeTypeMap.loadEntry("application/pics-rules", "prf", false);
            sMimeTypeMap.loadEntry("application/rar", "rar", false);
            sMimeTypeMap.loadEntry("application/rdf+xml", "rdf", false);
            sMimeTypeMap.loadEntry("application/rss+xml", "rss", false);
            sMimeTypeMap.loadEntry("application/zip", "zip", false);
            sMimeTypeMap.loadEntry("application/vnd.android.package-archive", 
                    "apk", false);
            sMimeTypeMap.loadEntry("application/vnd.cinderella", "cdy", false);
            sMimeTypeMap.loadEntry("application/vnd.ms-pki.stl", "stl", false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.database", "odb", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.formula", "odf", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.graphics", "odg", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.graphics-template",
                    "otg", false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.image", "odi", false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.spreadsheet", "ods", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.spreadsheet-template",
                    "ots", false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.text", "odt", false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.text-master", "odm", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.text-template", "ott", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.oasis.opendocument.text-web", "oth", 
                    false);
            sMimeTypeMap.loadEntry("application/vnd.rim.cod", "cod", false);
            sMimeTypeMap.loadEntry("application/vnd.smaf", "mmf", false);
            sMimeTypeMap.loadEntry("application/vnd.stardivision.calc", "sdc", 
                    false);
            sMimeTypeMap.loadEntry("application/vnd.stardivision.draw", "sda", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.stardivision.impress", "sdd", false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.stardivision.impress", "sdp", false);
            sMimeTypeMap.loadEntry("application/vnd.stardivision.math", "smf", 
                    false);
            sMimeTypeMap.loadEntry("application/vnd.stardivision.writer", "sdw", 
                    false);
            sMimeTypeMap.loadEntry("application/vnd.stardivision.writer", "vor", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.stardivision.writer-global", "sgl", false);
            sMimeTypeMap.loadEntry("application/vnd.sun.xml.calc", "sxc", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.sun.xml.calc.template", "stc", false);
            sMimeTypeMap.loadEntry("application/vnd.sun.xml.draw", "sxd", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.sun.xml.draw.template", "std", false);
            sMimeTypeMap.loadEntry("application/vnd.sun.xml.impress", "sxi", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.sun.xml.impress.template", "sti", false);
            sMimeTypeMap.loadEntry("application/vnd.sun.xml.math", "sxm", 
                    false);
            sMimeTypeMap.loadEntry("application/vnd.sun.xml.writer", "sxw", 
                    false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.sun.xml.writer.global", "sxg", false);
            sMimeTypeMap.loadEntry(
                    "application/vnd.sun.xml.writer.template", "stw", false);
            sMimeTypeMap.loadEntry("application/vnd.visio", "vsd", false);
            sMimeTypeMap.loadEntry("application/x-abiword", "abw", false);
            sMimeTypeMap.loadEntry("application/x-apple-diskimage", "dmg", 
                    false);
            sMimeTypeMap.loadEntry("application/x-bcpio", "bcpio", false);
            sMimeTypeMap.loadEntry("application/x-bittorrent", "torrent", 
                    false);
            sMimeTypeMap.loadEntry("application/x-cdf", "cdf", false);
            sMimeTypeMap.loadEntry("application/x-cdlink", "vcd", false);
            sMimeTypeMap.loadEntry("application/x-chess-pgn", "pgn", false);
            sMimeTypeMap.loadEntry("application/x-cpio", "cpio", false);
            sMimeTypeMap.loadEntry("application/x-debian-package", "deb", 
                    false);
            sMimeTypeMap.loadEntry("application/x-debian-package", "udeb", 
                    false);
            sMimeTypeMap.loadEntry("application/x-director", "dcr", false);
            sMimeTypeMap.loadEntry("application/x-director", "dir", false);
            sMimeTypeMap.loadEntry("application/x-director", "dxr", false);
            sMimeTypeMap.loadEntry("application/x-dms", "dms", false);
            sMimeTypeMap.loadEntry("application/x-doom", "wad", false);
            sMimeTypeMap.loadEntry("application/x-dvi", "dvi", false);
            sMimeTypeMap.loadEntry("application/x-flac", "flac", false);
            sMimeTypeMap.loadEntry("application/x-font", "pfa", false);
            sMimeTypeMap.loadEntry("application/x-font", "pfb", false);
            sMimeTypeMap.loadEntry("application/x-font", "gsf", false);
            sMimeTypeMap.loadEntry("application/x-font", "pcf", false);
            sMimeTypeMap.loadEntry("application/x-font", "pcf.Z", false);
            sMimeTypeMap.loadEntry("application/x-freemind", "mm", false);
            sMimeTypeMap.loadEntry("application/x-futuresplash", "spl", false);
            sMimeTypeMap.loadEntry("application/x-gnumeric", "gnumeric", false);
            sMimeTypeMap.loadEntry("application/x-go-sgf", "sgf", false);
            sMimeTypeMap.loadEntry("application/x-graphing-calculator", "gcf", 
                    false);
            sMimeTypeMap.loadEntry("application/x-gtar", "gtar", false);
            sMimeTypeMap.loadEntry("application/x-gtar", "tgz", false);
            sMimeTypeMap.loadEntry("application/x-gtar", "taz", false);
            sMimeTypeMap.loadEntry("application/x-hdf", "hdf", false);
            sMimeTypeMap.loadEntry("application/x-ica", "ica", false);
            sMimeTypeMap.loadEntry("application/x-internet-signup", "ins", 
                    false);
            sMimeTypeMap.loadEntry("application/x-internet-signup", "isp", 
                    false);
            sMimeTypeMap.loadEntry("application/x-iphone", "iii", false);
            sMimeTypeMap.loadEntry("application/x-iso9660-image", "iso", false);
            sMimeTypeMap.loadEntry("application/x-jmol", "jmz", false);
            sMimeTypeMap.loadEntry("application/x-kchart", "chrt", false);
            sMimeTypeMap.loadEntry("application/x-killustrator", "kil", false);
            sMimeTypeMap.loadEntry("application/x-koan", "skp", false);
            sMimeTypeMap.loadEntry("application/x-koan", "skd", false);
            sMimeTypeMap.loadEntry("application/x-koan", "skt", false);
            sMimeTypeMap.loadEntry("application/x-koan", "skm", false);
            sMimeTypeMap.loadEntry("application/x-kpresenter", "kpr", false);
            sMimeTypeMap.loadEntry("application/x-kpresenter", "kpt", false);
            sMimeTypeMap.loadEntry("application/x-kspread", "ksp", false);
            sMimeTypeMap.loadEntry("application/x-kword", "kwd", false);
            sMimeTypeMap.loadEntry("application/x-kword", "kwt", false);
            sMimeTypeMap.loadEntry("application/x-latex", "latex", false);
            sMimeTypeMap.loadEntry("application/x-lha", "lha", false);
            sMimeTypeMap.loadEntry("application/x-lzh", "lzh", false);
            sMimeTypeMap.loadEntry("application/x-lzx", "lzx", false);
            sMimeTypeMap.loadEntry("application/x-maker", "frm", false);
            sMimeTypeMap.loadEntry("application/x-maker", "maker", false);
            sMimeTypeMap.loadEntry("application/x-maker", "frame", false);
            sMimeTypeMap.loadEntry("application/x-maker", "fb", false);
            sMimeTypeMap.loadEntry("application/x-maker", "book", false);
            sMimeTypeMap.loadEntry("application/x-maker", "fbdoc", false);
            sMimeTypeMap.loadEntry("application/x-mif", "mif", false);
            sMimeTypeMap.loadEntry("application/x-ms-wmd", "wmd", false);
            sMimeTypeMap.loadEntry("application/x-ms-wmz", "wmz", false);
            sMimeTypeMap.loadEntry("application/x-msi", "msi", false);
            sMimeTypeMap.loadEntry("application/x-ns-proxy-autoconfig", "pac", 
                    false);
            sMimeTypeMap.loadEntry("application/x-nwc", "nwc", false);
            sMimeTypeMap.loadEntry("application/x-object", "o", false);
            sMimeTypeMap.loadEntry("application/x-oz-application", "oza", 
                    false);
            sMimeTypeMap.loadEntry("application/x-pkcs7-certreqresp", "p7r", 
                    false);
            sMimeTypeMap.loadEntry("application/x-pkcs7-crl", "crl", false);
            sMimeTypeMap.loadEntry("application/x-quicktimeplayer", "qtl", 
                    false);
            sMimeTypeMap.loadEntry("application/x-shar", "shar", false);
            sMimeTypeMap.loadEntry("application/x-stuffit", "sit", false);
            sMimeTypeMap.loadEntry("application/x-sv4cpio", "sv4cpio", false);
            sMimeTypeMap.loadEntry("application/x-sv4crc", "sv4crc", false);
            sMimeTypeMap.loadEntry("application/x-tar", "tar", false);
            sMimeTypeMap.loadEntry("application/x-texinfo", "texinfo", false);
            sMimeTypeMap.loadEntry("application/x-texinfo", "texi", false);
            sMimeTypeMap.loadEntry("application/x-troff", "t", false);
            sMimeTypeMap.loadEntry("application/x-troff", "roff", false);
            sMimeTypeMap.loadEntry("application/x-troff-man", "man", false);
            sMimeTypeMap.loadEntry("application/x-ustar", "ustar", false);
            sMimeTypeMap.loadEntry("application/x-wais-source", "src", false);
            sMimeTypeMap.loadEntry("application/x-wingz", "wz", false);
            sMimeTypeMap.loadEntry(
                    "application/x-webarchive", "webarchive", false); // added
            sMimeTypeMap.loadEntry("application/x-x509-ca-cert", "crt", false);
            sMimeTypeMap.loadEntry("application/x-xcf", "xcf", false);
            sMimeTypeMap.loadEntry("application/x-xfig", "fig", false);
            sMimeTypeMap.loadEntry("audio/basic", "snd", false);
            sMimeTypeMap.loadEntry("audio/midi", "mid", false);
            sMimeTypeMap.loadEntry("audio/midi", "midi", false);
            sMimeTypeMap.loadEntry("audio/midi", "kar", false);
            sMimeTypeMap.loadEntry("audio/mpeg", "mpga", false);
            sMimeTypeMap.loadEntry("audio/mpeg", "mpega", false);
            sMimeTypeMap.loadEntry("audio/mpeg", "mp2", false);
            sMimeTypeMap.loadEntry("audio/mpeg", "mp3", false);
            sMimeTypeMap.loadEntry("audio/mpeg", "m4a", false);
            sMimeTypeMap.loadEntry("audio/mpegurl", "m3u", false);
            sMimeTypeMap.loadEntry("audio/prs.sid", "sid", false);
            sMimeTypeMap.loadEntry("audio/x-aiff", "aif", false);
            sMimeTypeMap.loadEntry("audio/x-aiff", "aiff", false);
            sMimeTypeMap.loadEntry("audio/x-aiff", "aifc", false);
            sMimeTypeMap.loadEntry("audio/x-gsm", "gsm", false);
            sMimeTypeMap.loadEntry("audio/x-mpegurl", "m3u", false);
            sMimeTypeMap.loadEntry("audio/x-ms-wma", "wma", false);
            sMimeTypeMap.loadEntry("audio/x-ms-wax", "wax", false);
            sMimeTypeMap.loadEntry("audio/x-pn-realaudio", "ra", false);
            sMimeTypeMap.loadEntry("audio/x-pn-realaudio", "rm", false);
            sMimeTypeMap.loadEntry("audio/x-pn-realaudio", "ram", false);
            sMimeTypeMap.loadEntry("audio/x-realaudio", "ra", false);
            sMimeTypeMap.loadEntry("audio/x-scpls", "pls", false);
            sMimeTypeMap.loadEntry("audio/x-sd2", "sd2", false);
            sMimeTypeMap.loadEntry("audio/x-wav", "wav", false);
            sMimeTypeMap.loadEntry("image/bmp", "bmp", false); // added
            sMimeTypeMap.loadEntry("image/gif", "gif", false);
            sMimeTypeMap.loadEntry("image/ico", "cur", false); // added
            sMimeTypeMap.loadEntry("image/ico", "ico", false); // added
            sMimeTypeMap.loadEntry("image/ief", "ief", false);
            sMimeTypeMap.loadEntry("image/jpeg", "jpeg", false);
            sMimeTypeMap.loadEntry("image/jpeg", "jpg", false);
            sMimeTypeMap.loadEntry("image/jpeg", "jpe", false);
            sMimeTypeMap.loadEntry("image/pcx", "pcx", false);
            sMimeTypeMap.loadEntry("image/png", "png", false);
            sMimeTypeMap.loadEntry("image/svg+xml", "svg", false);
            sMimeTypeMap.loadEntry("image/svg+xml", "svgz", false);
            sMimeTypeMap.loadEntry("image/tiff", "tiff", false);
            sMimeTypeMap.loadEntry("image/tiff", "tif", false);
            sMimeTypeMap.loadEntry("image/vnd.djvu", "djvu", false);
            sMimeTypeMap.loadEntry("image/vnd.djvu", "djv", false);
            sMimeTypeMap.loadEntry("image/vnd.wap.wbmp", "wbmp", false);
            sMimeTypeMap.loadEntry("image/x-cmu-raster", "ras", false);
            sMimeTypeMap.loadEntry("image/x-coreldraw", "cdr", false);
            sMimeTypeMap.loadEntry("image/x-coreldrawpattern", "pat", false);
            sMimeTypeMap.loadEntry("image/x-coreldrawtemplate", "cdt", false);
            sMimeTypeMap.loadEntry("image/x-corelphotopaint", "cpt", false);
            sMimeTypeMap.loadEntry("image/x-icon", "ico", false);
            sMimeTypeMap.loadEntry("image/x-jg", "art", false);
            sMimeTypeMap.loadEntry("image/x-jng", "jng", false);
            sMimeTypeMap.loadEntry("image/x-ms-bmp", "bmp", false);
            sMimeTypeMap.loadEntry("image/x-photoshop", "psd", false);
            sMimeTypeMap.loadEntry("image/x-portable-anymap", "pnm", false);
            sMimeTypeMap.loadEntry("image/x-portable-bitmap", "pbm", false);
            sMimeTypeMap.loadEntry("image/x-portable-graymap", "pgm", false);
            sMimeTypeMap.loadEntry("image/x-portable-pixmap", "ppm", false);
            sMimeTypeMap.loadEntry("image/x-rgb", "rgb", false);
            sMimeTypeMap.loadEntry("image/x-xbitmap", "xbm", false);
            sMimeTypeMap.loadEntry("image/x-xpixmap", "xpm", false);
            sMimeTypeMap.loadEntry("image/x-xwindowdump", "xwd", false);
            sMimeTypeMap.loadEntry("model/iges", "igs", false);
            sMimeTypeMap.loadEntry("model/iges", "iges", false);
            sMimeTypeMap.loadEntry("model/mesh", "msh", false);
            sMimeTypeMap.loadEntry("model/mesh", "mesh", false);
            sMimeTypeMap.loadEntry("model/mesh", "silo", false);
            sMimeTypeMap.loadEntry("text/calendar", "ics", true);
            sMimeTypeMap.loadEntry("text/calendar", "icz", true);
            sMimeTypeMap.loadEntry("text/comma-separated-values", "csv", true);
            sMimeTypeMap.loadEntry("text/css", "css", true);
            sMimeTypeMap.loadEntry("text/h323", "323", true);
            sMimeTypeMap.loadEntry("text/iuls", "uls", true);
            sMimeTypeMap.loadEntry("text/mathml", "mml", true);
            sMimeTypeMap.loadEntry("text/plain", "asc", true);
            sMimeTypeMap.loadEntry("text/plain", "txt", true);
            sMimeTypeMap.loadEntry("text/plain", "text", true);
            sMimeTypeMap.loadEntry("text/plain", "diff", true);
            sMimeTypeMap.loadEntry("text/plain", "pot", true);
            sMimeTypeMap.loadEntry("text/richtext", "rtx", true);
            sMimeTypeMap.loadEntry("text/rtf", "rtf", true);
            sMimeTypeMap.loadEntry("text/texmacs", "ts", true);
            sMimeTypeMap.loadEntry("text/text", "phps", true);
            sMimeTypeMap.loadEntry("text/tab-separated-values", "tsv", true);
            sMimeTypeMap.loadEntry("text/x-bibtex", "bib", true);
            sMimeTypeMap.loadEntry("text/x-boo", "boo", true);
            sMimeTypeMap.loadEntry("text/x-c++hdr", "h++", true);
            sMimeTypeMap.loadEntry("text/x-c++hdr", "hpp", true);
            sMimeTypeMap.loadEntry("text/x-c++hdr", "hxx", true);
            sMimeTypeMap.loadEntry("text/x-c++hdr", "hh", true);
            sMimeTypeMap.loadEntry("text/x-c++src", "c++", true);
            sMimeTypeMap.loadEntry("text/x-c++src", "cpp", true);
            sMimeTypeMap.loadEntry("text/x-c++src", "cxx", true);
            sMimeTypeMap.loadEntry("text/x-chdr", "h", true);
            sMimeTypeMap.loadEntry("text/x-component", "htc", true);
            sMimeTypeMap.loadEntry("text/x-csh", "csh", true);
            sMimeTypeMap.loadEntry("text/x-csrc", "c", true);
            sMimeTypeMap.loadEntry("text/x-dsrc", "d", true);
            sMimeTypeMap.loadEntry("text/x-haskell", "hs", true);
            sMimeTypeMap.loadEntry("text/x-java", "java", true);
            sMimeTypeMap.loadEntry("text/x-literate-haskell", "lhs", true);
            sMimeTypeMap.loadEntry("text/x-moc", "moc", true);
            sMimeTypeMap.loadEntry("text/x-pascal", "p", true);
            sMimeTypeMap.loadEntry("text/x-pascal", "pas", true);
            sMimeTypeMap.loadEntry("text/x-pcs-gcd", "gcd", true);
            sMimeTypeMap.loadEntry("text/x-setext", "etx", true);
            sMimeTypeMap.loadEntry("text/x-tcl", "tcl", true);
            sMimeTypeMap.loadEntry("text/x-tex", "tex", true);
            sMimeTypeMap.loadEntry("text/x-tex", "ltx", true);
            sMimeTypeMap.loadEntry("text/x-tex", "sty", true);
            sMimeTypeMap.loadEntry("text/x-tex", "cls", true);
            sMimeTypeMap.loadEntry("text/x-vcalendar", "vcs", true);
            sMimeTypeMap.loadEntry("text/x-vcard", "vcf", true);
            sMimeTypeMap.loadEntry("video/dl", "dl", false);
            sMimeTypeMap.loadEntry("video/dv", "dif", false);
            sMimeTypeMap.loadEntry("video/dv", "dv", false);
            sMimeTypeMap.loadEntry("video/fli", "fli", false);
            sMimeTypeMap.loadEntry("video/mpeg", "mpeg", false);
            sMimeTypeMap.loadEntry("video/mpeg", "mpg", false);
            sMimeTypeMap.loadEntry("video/mpeg", "mpe", false);
            sMimeTypeMap.loadEntry("video/mp4", "mp4", false);
            sMimeTypeMap.loadEntry("video/quicktime", "qt", false);
            sMimeTypeMap.loadEntry("video/quicktime", "mov", false);
            sMimeTypeMap.loadEntry("video/vnd.mpegurl", "mxu", false);
            sMimeTypeMap.loadEntry("video/x-la-asf", "lsf", false);
            sMimeTypeMap.loadEntry("video/x-la-asf", "lsx", false);
            sMimeTypeMap.loadEntry("video/x-mng", "mng", false);
            sMimeTypeMap.loadEntry("video/x-ms-asf", "asf", false);
            sMimeTypeMap.loadEntry("video/x-ms-asf", "asx", false);
            sMimeTypeMap.loadEntry("video/x-ms-wm", "wm", false);
            sMimeTypeMap.loadEntry("video/x-ms-wmv", "wmv", false);
            sMimeTypeMap.loadEntry("video/x-ms-wmx", "wmx", false);
            sMimeTypeMap.loadEntry("video/x-ms-wvx", "wvx", false);
            sMimeTypeMap.loadEntry("video/x-msvideo", "avi", false);
            sMimeTypeMap.loadEntry("video/x-sgi-movie", "movie", false);
            sMimeTypeMap.loadEntry("x-conference/x-cooltalk", "ice", false);
        }

>>>>>>> 54b6cfa... Initial Contribution
        return sMimeTypeMap;
    }
}
