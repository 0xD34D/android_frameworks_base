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

package android.media;

<<<<<<< HEAD
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IContentProvider;
import android.database.Cursor;
import android.database.SQLException;
import android.drm.DrmManagerClient;
import android.graphics.BitmapFactory;
import android.mtp.MtpConstants;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.provider.Settings;
=======
import android.content.ContentValues;
import android.content.Context;
import android.content.IContentProvider;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Audio.Genres;
import android.provider.MediaStore.Audio.Playlists;
>>>>>>> 54b6cfa... Initial Contribution
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.RootElement;
import android.text.TextUtils;
<<<<<<< HEAD
import android.util.Log;
import android.util.Xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import libcore.io.ErrnoException;
import libcore.io.Libcore;

/**
 * Internal service helper that no-one should use directly.
 *
 * The way the scan currently works is:
 * - The Java MediaScannerService creates a MediaScanner (this class), and calls
 *   MediaScanner.scanDirectories on it.
 * - scanDirectories() calls the native processDirectory() for each of the specified directories.
 * - the processDirectory() JNI method wraps the provided mediascanner client in a native
 *   'MyMediaScannerClient' class, then calls processDirectory() on the native MediaScanner
 *   object (which got created when the Java MediaScanner was created).
 * - native MediaScanner.processDirectory() calls
 *   doProcessDirectory(), which recurses over the folder, and calls
 *   native MyMediaScannerClient.scanFile() for every file whose extension matches.
 * - native MyMediaScannerClient.scanFile() calls back on Java MediaScannerClient.scanFile,
 *   which calls doScanFile, which after some setup calls back down to native code, calling
 *   MediaScanner.processFile().
 * - MediaScanner.processFile() calls one of several methods, depending on the type of the
 *   file: parseMP3, parseMP4, parseMidi, parseOgg or parseWMA.
 * - each of these methods gets metadata key/value pairs from the file, and repeatedly
 *   calls native MyMediaScannerClient.handleStringTag, which calls back up to its Java
 *   counterparts in this file.
 * - Java handleStringTag() gathers the key/value pairs that it's interested in.
 * - once processFile returns and we're back in Java code in doScanFile(), it calls
 *   Java MyMediaScannerClient.endFile(), which takes all the data that's been
 *   gathered and inserts an entry in to the database.
 *
 * In summary:
 * Java MediaScannerService calls
 * Java MediaScanner scanDirectories, which calls
 * Java MediaScanner processDirectory (native method), which calls
 * native MediaScanner processDirectory, which calls
 * native MyMediaScannerClient scanFile, which calls
 * Java MyMediaScannerClient scanFile, which calls
 * Java MediaScannerClient doScanFile, which calls
 * Java MediaScanner processFile (native method), which calls
 * native MediaScanner processFile, which calls
 * native parseMP3, parseMP4, parseMidi, parseOgg or parseWMA, which calls
 * native MyMediaScanner handleStringTag, which calls
 * Java MyMediaScanner handleStringTag.
 * Once MediaScanner processFile returns, an entry is inserted in to the database.
 *
 * The MediaScanner class is not thread-safe, so it should only be used in a single threaded manner.
=======
import android.util.Config;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Internal service that no-one should use directly.
>>>>>>> 54b6cfa... Initial Contribution
 *
 * {@hide}
 */
public class MediaScanner
<<<<<<< HEAD
{
    static {
        System.loadLibrary("media_jni");
        native_init();
=======
{    
    static {
        System.loadLibrary("media_jni");
>>>>>>> 54b6cfa... Initial Contribution
    }

    private final static String TAG = "MediaScanner";

<<<<<<< HEAD
    private static final String[] FILES_PRESCAN_PROJECTION = new String[] {
            Files.FileColumns._ID, // 0
            Files.FileColumns.DATA, // 1
            Files.FileColumns.FORMAT, // 2
            Files.FileColumns.DATE_MODIFIED, // 3
    };

    private static final String[] ID_PROJECTION = new String[] {
            Files.FileColumns._ID,
    };

    private static final int FILES_PRESCAN_ID_COLUMN_INDEX = 0;
    private static final int FILES_PRESCAN_PATH_COLUMN_INDEX = 1;
    private static final int FILES_PRESCAN_FORMAT_COLUMN_INDEX = 2;
    private static final int FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX = 3;
=======
    private static final String[] AUDIO_PROJECTION = new String[] {
            Audio.Media._ID, // 0
            Audio.Media.DATA, // 1
            Audio.Media.DATE_MODIFIED, // 2
    };
    
    private static final int ID_AUDIO_COLUMN_INDEX = 0;
    private static final int PATH_AUDIO_COLUMN_INDEX = 1;
    private static final int DATE_MODIFIED_AUDIO_COLUMN_INDEX = 2;
 
    private static final String[] VIDEO_PROJECTION = new String[] {
            Video.Media._ID, // 0
            Video.Media.DATA, // 1
            Video.Media.DATE_MODIFIED, // 2
    };
    
    private static final int ID_VIDEO_COLUMN_INDEX = 0;
    private static final int PATH_VIDEO_COLUMN_INDEX = 1;
    private static final int DATE_MODIFIED_VIDEO_COLUMN_INDEX = 2;

    private static final String[] IMAGES_PROJECTION = new String[] {
            Images.Media._ID, // 0
            Images.Media.DATA, // 1
            Images.Media.DATE_MODIFIED, // 2
    };
    
    private static final int ID_IMAGES_COLUMN_INDEX = 0;
    private static final int PATH_IMAGES_COLUMN_INDEX = 1;
    private static final int DATE_MODIFIED_IMAGES_COLUMN_INDEX = 2;
    
    private static final String[] PLAYLISTS_PROJECTION = new String[] {
            Audio.Playlists._ID, // 0
            Audio.Playlists.DATA, // 1
            Audio.Playlists.DATE_MODIFIED, // 2
    };
>>>>>>> 54b6cfa... Initial Contribution

    private static final String[] PLAYLIST_MEMBERS_PROJECTION = new String[] {
            Audio.Playlists.Members.PLAYLIST_ID, // 0
     };

    private static final int ID_PLAYLISTS_COLUMN_INDEX = 0;
    private static final int PATH_PLAYLISTS_COLUMN_INDEX = 1;
    private static final int DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX = 2;

<<<<<<< HEAD
=======
    private static final String[] GENRE_LOOKUP_PROJECTION = new String[] {
            Audio.Genres._ID, // 0
            Audio.Genres.NAME, // 1
    };

>>>>>>> 54b6cfa... Initial Contribution
    private static final String RINGTONES_DIR = "/ringtones/";
    private static final String NOTIFICATIONS_DIR = "/notifications/";
    private static final String ALARMS_DIR = "/alarms/";
    private static final String MUSIC_DIR = "/music/";
<<<<<<< HEAD
    private static final String PODCAST_DIR = "/podcasts/";

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    private static final String[] ID3_GENRES = {
        // ID3v1 Genres
        "Blues",
        "Classic Rock",
        "Country",
        "Dance",
        "Disco",
        "Funk",
        "Grunge",
        "Hip-Hop",
        "Jazz",
        "Metal",
        "New Age",
        "Oldies",
        "Other",
        "Pop",
        "R&B",
        "Rap",
        "Reggae",
        "Rock",
        "Techno",
        "Industrial",
        "Alternative",
        "Ska",
        "Death Metal",
        "Pranks",
        "Soundtrack",
        "Euro-Techno",
        "Ambient",
        "Trip-Hop",
        "Vocal",
        "Jazz+Funk",
        "Fusion",
        "Trance",
        "Classical",
        "Instrumental",
        "Acid",
        "House",
        "Game",
        "Sound Clip",
        "Gospel",
        "Noise",
        "AlternRock",
        "Bass",
        "Soul",
        "Punk",
        "Space",
        "Meditative",
        "Instrumental Pop",
        "Instrumental Rock",
        "Ethnic",
        "Gothic",
        "Darkwave",
        "Techno-Industrial",
        "Electronic",
        "Pop-Folk",
        "Eurodance",
        "Dream",
        "Southern Rock",
        "Comedy",
        "Cult",
        "Gangsta",
        "Top 40",
        "Christian Rap",
        "Pop/Funk",
        "Jungle",
        "Native American",
        "Cabaret",
        "New Wave",
        "Psychadelic",
        "Rave",
        "Showtunes",
        "Trailer",
        "Lo-Fi",
        "Tribal",
        "Acid Punk",
        "Acid Jazz",
        "Polka",
        "Retro",
        "Musical",
        "Rock & Roll",
        "Hard Rock",
        // The following genres are Winamp extensions
        "Folk",
        "Folk-Rock",
        "National Folk",
        "Swing",
        "Fast Fusion",
        "Bebob",
        "Latin",
        "Revival",
        "Celtic",
        "Bluegrass",
        "Avantgarde",
        "Gothic Rock",
        "Progressive Rock",
        "Psychedelic Rock",
        "Symphonic Rock",
        "Slow Rock",
        "Big Band",
        "Chorus",
        "Easy Listening",
        "Acoustic",
        "Humour",
        "Speech",
        "Chanson",
        "Opera",
        "Chamber Music",
        "Sonata",
        "Symphony",
        "Booty Bass",
        "Primus",
        "Porn Groove",
        "Satire",
        "Slow Jam",
        "Club",
        "Tango",
        "Samba",
        "Folklore",
        "Ballad",
        "Power Ballad",
        "Rhythmic Soul",
        "Freestyle",
        "Duet",
        "Punk Rock",
        "Drum Solo",
        "A capella",
        "Euro-House",
<<<<<<< HEAD
        "Dance Hall",
        // The following ones seem to be fairly widely supported as well
        "Goa",
        "Drum & Bass",
        "Club-House",
        "Hardcore",
        "Terror",
        "Indie",
        "Britpop",
        "Negerpunk",
        "Polsk Punk",
        "Beat",
        "Christian Gangsta",
        "Heavy Metal",
        "Black Metal",
        "Crossover",
        "Contemporary Christian",
        "Christian Rock",
        "Merengue",
        "Salsa",
        "Thrash Metal",
        "Anime",
        "JPop",
        "Synthpop",
        // 148 and up don't seem to have been defined yet.
=======
        "Dance Hall"
>>>>>>> 54b6cfa... Initial Contribution
    };

    private int mNativeContext;
    private Context mContext;
    private IContentProvider mMediaProvider;
    private Uri mAudioUri;
    private Uri mVideoUri;
    private Uri mImagesUri;
    private Uri mThumbsUri;
<<<<<<< HEAD
    private Uri mPlaylistsUri;
    private Uri mFilesUri;
    private boolean mProcessPlaylists, mProcessGenres;
    private int mMtpObjectHandle;

    private final String mExternalStoragePath;

    /** whether to use bulk inserts or individual inserts for each item */
    private static final boolean ENABLE_BULK_INSERTS = true;
=======
    private Uri mGenresUri;
    private Uri mPlaylistsUri;
    private boolean mProcessPlaylists, mProcessGenres;
>>>>>>> 54b6cfa... Initial Contribution

    // used when scanning the image database so we know whether we have to prune
    // old thumbnail files
    private int mOriginalCount;
<<<<<<< HEAD
    /** Whether the database had any entries in it before the scan started */
    private boolean mWasEmptyPriorToScan = false;
=======
>>>>>>> 54b6cfa... Initial Contribution
    /** Whether the scanner has set a default sound for the ringer ringtone. */
    private boolean mDefaultRingtoneSet;
    /** Whether the scanner has set a default sound for the notification ringtone. */
    private boolean mDefaultNotificationSet;
<<<<<<< HEAD
    /** Whether the scanner has set a default sound for the alarm ringtone. */
    private boolean mDefaultAlarmSet;
=======
>>>>>>> 54b6cfa... Initial Contribution
    /** The filename for the default sound for the ringer ringtone. */
    private String mDefaultRingtoneFilename;
    /** The filename for the default sound for the notification ringtone. */
    private String mDefaultNotificationFilename;
<<<<<<< HEAD
    /** The filename for the default sound for the alarm ringtone. */
    private String mDefaultAlarmAlertFilename;
=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * The prefix for system properties that define the default sound for
     * ringtones. Concatenate the name of the setting from Settings
     * to get the full system property.
     */
    private static final String DEFAULT_RINGTONE_PROPERTY_PREFIX = "ro.config.";
<<<<<<< HEAD

    // set to true if file path comparisons should be case insensitive.
    // this should be set when scanning files on a case insensitive file system.
    private boolean mCaseInsensitivePaths;

    private final BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();

    private static class FileEntry {
        long mRowId;
        String mPath;
        long mLastModified;
        int mFormat;
        boolean mLastModifiedChanged;

        FileEntry(long rowId, String path, long lastModified, int format) {
            mRowId = rowId;
            mPath = path;
            mLastModified = lastModified;
            mFormat = format;
=======
    
    // set to true if file path comparisons should be case insensitive.
    // this should be set when scanning files on a case insensitive file system.
    private boolean mCaseInsensitivePaths;
    
    private BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();

    private static class FileCacheEntry {
        Uri mTableUri;
        long mRowId;
        String mPath;
        long mLastModified;
        boolean mSeenInFileSystem;
        boolean mLastModifiedChanged;
        
        FileCacheEntry(Uri tableUri, long rowId, String path, long lastModified) {
            mTableUri = tableUri;
            mRowId = rowId;
            mPath = path;
            mLastModified = lastModified;
            mSeenInFileSystem = false;
>>>>>>> 54b6cfa... Initial Contribution
            mLastModifiedChanged = false;
        }

        @Override
        public String toString() {
<<<<<<< HEAD
            return mPath + " mRowId: " + mRowId;
        }
    }

    private static class PlaylistEntry {
        String path;
        long bestmatchid;
        int bestmatchlevel;
    }

    private ArrayList<PlaylistEntry> mPlaylistEntries = new ArrayList<PlaylistEntry>();

    private MediaInserter mMediaInserter;

    private ArrayList<FileEntry> mPlayLists;

    private DrmManagerClient mDrmManagerClient = null;
=======
            return mPath;
        }
    }
    
    // hashes file path to FileCacheEntry.  
    // path should be lower case if mCaseInsensitivePaths is true
    private HashMap<String, FileCacheEntry> mFileCache; 

    private ArrayList<FileCacheEntry> mPlayLists;
    private HashMap<String, Uri> mGenreCache;

>>>>>>> 54b6cfa... Initial Contribution

    public MediaScanner(Context c) {
        native_setup();
        mContext = c;
        mBitmapOptions.inSampleSize = 1;
        mBitmapOptions.inJustDecodeBounds = true;
<<<<<<< HEAD

        setDefaultRingtoneFileNames();

        mExternalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //mClient.testGenreNameConverter();
=======
        
        setDefaultRingtoneFileNames();
>>>>>>> 54b6cfa... Initial Contribution
    }

    private void setDefaultRingtoneFileNames() {
        mDefaultRingtoneFilename = SystemProperties.get(DEFAULT_RINGTONE_PROPERTY_PREFIX
                + Settings.System.RINGTONE);
        mDefaultNotificationFilename = SystemProperties.get(DEFAULT_RINGTONE_PROPERTY_PREFIX
                + Settings.System.NOTIFICATION_SOUND);
<<<<<<< HEAD
        mDefaultAlarmAlertFilename = SystemProperties.get(DEFAULT_RINGTONE_PROPERTY_PREFIX
                + Settings.System.ALARM_ALERT);
    }

    private final MyMediaScannerClient mClient = new MyMediaScannerClient();

    private boolean isDrmEnabled() {
        String prop = SystemProperties.get("drm.service.enabled");
        return prop != null && prop.equals("true");
    }

    private class MyMediaScannerClient implements MediaScannerClient {

=======
    }
    
    private MyMediaScannerClient mClient = new MyMediaScannerClient();
    
    private class MyMediaScannerClient implements MediaScannerClient {
    
>>>>>>> 54b6cfa... Initial Contribution
        private String mArtist;
        private String mAlbumArtist;    // use this if mArtist is missing
        private String mAlbum;
        private String mTitle;
        private String mComposer;
        private String mGenre;
        private String mMimeType;
        private int mFileType;
        private int mTrack;
        private int mYear;
        private int mDuration;
        private String mPath;
        private long mLastModified;
        private long mFileSize;
<<<<<<< HEAD
        private String mWriter;
        private int mCompilation;
        private boolean mIsDrm;
        private boolean mNoMedia;   // flag to suppress file from appearing in media tables
        private int mWidth;
        private int mHeight;

        public FileEntry beginFile(String path, String mimeType, long lastModified,
                long fileSize, boolean isDirectory, boolean noMedia) {
            mMimeType = mimeType;
            mFileType = 0;
            mFileSize = fileSize;

            if (!isDirectory) {
                if (!noMedia && isNoMediaFile(path)) {
                    noMedia = true;
                }
                mNoMedia = noMedia;

                // try mimeType first, if it is specified
                if (mimeType != null) {
                    mFileType = MediaFile.getFileTypeForMimeType(mimeType);
                }

                // if mimeType was not specified, compute file type based on file extension.
                if (mFileType == 0) {
                    MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
                    if (mediaFileType != null) {
                        mFileType = mediaFileType.fileType;
                        if (mMimeType == null) {
                            mMimeType = mediaFileType.mimeType;
                        }
                    }
                }

                if (isDrmEnabled() && MediaFile.isDrmFileType(mFileType)) {
                    mFileType = getFileTypeFromDrm(path);
                }
            }

            FileEntry entry = makeEntryFor(path);
            // add some slack to avoid a rounding error
            long delta = (entry != null) ? (lastModified - entry.mLastModified) : 0;
            boolean wasModified = delta > 1 || delta < -1;
            if (entry == null || wasModified) {
                if (wasModified) {
                    entry.mLastModified = lastModified;
                } else {
                    entry = new FileEntry(0, path, lastModified,
                            (isDirectory ? MtpConstants.FORMAT_ASSOCIATION : 0));
                }
                entry.mLastModifiedChanged = true;
            }

=======
    
        public FileCacheEntry beginFile(String path, String mimeType, long lastModified, long fileSize) {
            
            // special case certain file names
            // I use regionMatches() instead of substring() below 
            // to avoid memory allocation
            int lastSlash = path.lastIndexOf('/');
            if (lastSlash >= 0 && lastSlash + 2 < path.length()) {
                // ignore those ._* files created by MacOS
                if (path.regionMatches(lastSlash + 1, "._", 0, 2)) {
                    return null;
                }
                
                // ignore album art files created by Windows Media Player:
                // Folder.jpg, AlbumArtSmall.jpg, AlbumArt_{...}_Large.jpg and AlbumArt_{...}_Small.jpg
                if (path.regionMatches(true, path.length() - 4, ".jpg", 0, 4)) {
                    if (path.regionMatches(true, lastSlash + 1, "AlbumArt_{", 0, 10) ||
                            path.regionMatches(true, lastSlash + 1, "AlbumArt.", 0, 9)) {
                        return null;
                    }
                    int length = path.length() - lastSlash - 1;
                    if ((length == 17 && path.regionMatches(true, lastSlash + 1, "AlbumArtSmall", 0, 13)) ||
                            (length == 10 && path.regionMatches(true, lastSlash + 1, "Folder", 0, 6))) {
                        return null;
                    }
                }
            }
            
            mMimeType = null;
            // try mimeType first, if it is specified
            if (mimeType != null) {
                mFileType = MediaFile.getFileTypeForMimeType(mimeType);
                if (mFileType != 0) {
                    mMimeType = mimeType;
                }
            }
            mFileSize = fileSize;

            // if mimeType was not specified, compute file type based on file extension.
            if (mMimeType == null) {
                MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
                if (mediaFileType != null) {
                    mFileType = mediaFileType.fileType;
                    mMimeType = mediaFileType.mimeType;
                }
            }
            
            String key = path;
            if (mCaseInsensitivePaths) {
                key = path.toLowerCase();
            }
            FileCacheEntry entry = mFileCache.get(key);
            if (entry == null) {
                entry = new FileCacheEntry(null, 0, path, 0);
                mFileCache.put(key, entry);
            }
            entry.mSeenInFileSystem = true;
            
            // add some slack to avoid a rounding error
            long delta = lastModified - entry.mLastModified;
            if (delta > 1 || delta < -1) {
                entry.mLastModified = lastModified;
                entry.mLastModifiedChanged = true;
            }
                           
>>>>>>> 54b6cfa... Initial Contribution
            if (mProcessPlaylists && MediaFile.isPlayListFileType(mFileType)) {
                mPlayLists.add(entry);
                // we don't process playlists in the main scan, so return null
                return null;
            }
<<<<<<< HEAD

=======
            
>>>>>>> 54b6cfa... Initial Contribution
            // clear all the metadata
            mArtist = null;
            mAlbumArtist = null;
            mAlbum = null;
            mTitle = null;
            mComposer = null;
            mGenre = null;
            mTrack = 0;
            mYear = 0;
            mDuration = 0;
            mPath = path;
            mLastModified = lastModified;
<<<<<<< HEAD
            mWriter = null;
            mCompilation = 0;
            mIsDrm = false;
            mWidth = 0;
            mHeight = 0;

            return entry;
        }

        @Override
        public void scanFile(String path, long lastModified, long fileSize,
                boolean isDirectory, boolean noMedia) {
            // This is the callback funtion from native codes.
            // Log.v(TAG, "scanFile: "+path);
            doScanFile(path, null, lastModified, fileSize, isDirectory, false, noMedia);
        }

        public Uri doScanFile(String path, String mimeType, long lastModified,
                long fileSize, boolean isDirectory, boolean scanAlways, boolean noMedia) {
            Uri result = null;
//            long t1 = System.currentTimeMillis();
            try {
                FileEntry entry = beginFile(path, mimeType, lastModified,
                        fileSize, isDirectory, noMedia);

                // if this file was just inserted via mtp, set the rowid to zero
                // (even though it already exists in the database), to trigger
                // the correct code path for updating its entry
                if (mMtpObjectHandle != 0) {
                    entry.mRowId = 0;
                }
                // rescan for metadata if file was modified since last scan
                if (entry != null && (entry.mLastModifiedChanged || scanAlways)) {
                    if (noMedia) {
                        result = endFile(entry, false, false, false, false, false);
                    } else {
                        String lowpath = path.toLowerCase();
                        boolean ringtones = (lowpath.indexOf(RINGTONES_DIR) > 0);
                        boolean notifications = (lowpath.indexOf(NOTIFICATIONS_DIR) > 0);
                        boolean alarms = (lowpath.indexOf(ALARMS_DIR) > 0);
                        boolean podcasts = (lowpath.indexOf(PODCAST_DIR) > 0);
                        boolean music = (lowpath.indexOf(MUSIC_DIR) > 0) ||
                            (!ringtones && !notifications && !alarms && !podcasts);

                        // we only extract metadata for audio and video files
                        if (MediaFile.isAudioFileType(mFileType)
                                || MediaFile.isVideoFileType(mFileType)) {
                            processFile(path, mimeType, this);
                        }

                        if (MediaFile.isImageFileType(mFileType)) {
                            processImageFile(path);
                        }

                        result = endFile(entry, ringtones, notifications, alarms, music, podcasts);
                    }
=======
            
            return entry;
        }
        
        public void scanFile(String path, long lastModified, long fileSize) {
            doScanFile(path, null, lastModified, fileSize, false);
        }

        public void scanFile(String path, String mimeType, long lastModified, long fileSize) {
            doScanFile(path, mimeType, lastModified, fileSize, false);
        }

        public Uri doScanFile(String path, String mimeType, long lastModified, long fileSize, boolean scanAlways) {
            Uri result = null;
//            long t1 = System.currentTimeMillis();
            try {
                FileCacheEntry entry = beginFile(path, mimeType, lastModified, fileSize);
                // rescan for metadata if file was modified since last scan
                if (entry != null && (entry.mLastModifiedChanged || scanAlways)) {
                    boolean ringtones = (path.indexOf(RINGTONES_DIR) > 0);
                    boolean notifications = (path.indexOf(NOTIFICATIONS_DIR) > 0);
                    boolean alarms = (path.indexOf(ALARMS_DIR) > 0);
                    boolean music = (path.indexOf(MUSIC_DIR) > 0) ||
                        (!ringtones && !notifications && !alarms);

                    if (mFileType == MediaFile.FILE_TYPE_MP3 ||
                            mFileType == MediaFile.FILE_TYPE_MP4 ||
                            mFileType == MediaFile.FILE_TYPE_M4A ||
                            mFileType == MediaFile.FILE_TYPE_3GPP ||
                            mFileType == MediaFile.FILE_TYPE_3GPP2 ||
                            mFileType == MediaFile.FILE_TYPE_OGG ||
                            mFileType == MediaFile.FILE_TYPE_MID ||
                            mFileType == MediaFile.FILE_TYPE_WMA) {
                        // we only extract metadata from MP3, M4A, OGG, MID and WMA files.
                        // check MP4 files, to determine if they contain only audio.
                        processFile(path, mimeType, this);
                    } else if (MediaFile.isImageFileType(mFileType)) {
                        // we used to compute the width and height but it's not worth it
                    }
                    
                    result = endFile(entry, ringtones, notifications, alarms, music);
>>>>>>> 54b6cfa... Initial Contribution
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
            }
//            long t2 = System.currentTimeMillis();
//            Log.v(TAG, "scanFile: " + path + " took " + (t2-t1));
            return result;
        }

        private int parseSubstring(String s, int start, int defaultValue) {
            int length = s.length();
            if (start == length) return defaultValue;

            char ch = s.charAt(start++);
            // return defaultValue if we have no integer at all
            if (ch < '0' || ch > '9') return defaultValue;
<<<<<<< HEAD

=======
            
>>>>>>> 54b6cfa... Initial Contribution
            int result = ch - '0';
            while (start < length) {
                ch = s.charAt(start++);
                if (ch < '0' || ch > '9') return result;
                result = result * 10 + (ch - '0');
            }
<<<<<<< HEAD

            return result;
        }

        public void handleStringTag(String name, String value) {
            if (name.equalsIgnoreCase("title") || name.startsWith("title;")) {
                // Don't trim() here, to preserve the special \001 character
                // used to force sorting. The media provider will trim() before
                // inserting the title in to the database.
                mTitle = value;
            } else if (name.equalsIgnoreCase("artist") || name.startsWith("artist;")) {
                mArtist = value.trim();
            } else if (name.equalsIgnoreCase("albumartist") || name.startsWith("albumartist;")
                    || name.equalsIgnoreCase("band") || name.startsWith("band;")) {
=======
            
            return result;
        }                                
                                
        public void handleStringTag(String name, String value) {
            if (name.equalsIgnoreCase("title") || name.startsWith("title;")) {
                mTitle = value.trim();
            } else if (name.equalsIgnoreCase("artist") || name.startsWith("artist;")) {
                mArtist = value.trim();
            } else if (name.equalsIgnoreCase("albumartist") || name.startsWith("albumartist;")) {
>>>>>>> 54b6cfa... Initial Contribution
                mAlbumArtist = value.trim();
            } else if (name.equalsIgnoreCase("album") || name.startsWith("album;")) {
                mAlbum = value.trim();
            } else if (name.equalsIgnoreCase("composer") || name.startsWith("composer;")) {
                mComposer = value.trim();
<<<<<<< HEAD
            } else if (mProcessGenres &&
                    (name.equalsIgnoreCase("genre") || name.startsWith("genre;"))) {
                mGenre = getGenreName(value);
=======
            } else if (name.equalsIgnoreCase("genre") || name.startsWith("genre;")) {
                // handle numeric genres, which PV sometimes encodes like "(20)"
                if (value.length() > 0) {
                    int genreCode = -1;
                    char ch = value.charAt(0);
                    if (ch == '(') {
                        genreCode = parseSubstring(value, 1, -1);
                    } else if (ch >= '0' && ch <= '9') {
                        genreCode = parseSubstring(value, 0, -1);
                    }
                    if (genreCode >= 0 && genreCode < ID3_GENRES.length) {
                        value = ID3_GENRES[genreCode];
                    }
                }
                mGenre = value;
>>>>>>> 54b6cfa... Initial Contribution
            } else if (name.equalsIgnoreCase("year") || name.startsWith("year;")) {
                mYear = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("tracknumber") || name.startsWith("tracknumber;")) {
                // track number might be of the form "2/12"
                // we just read the number before the slash
                int num = parseSubstring(value, 0, 0);
<<<<<<< HEAD
                mTrack = (mTrack / 1000) * 1000 + num;
=======
                mTrack = (mTrack / 1000) * 1000 + num; 
>>>>>>> 54b6cfa... Initial Contribution
            } else if (name.equalsIgnoreCase("discnumber") ||
                    name.equals("set") || name.startsWith("set;")) {
                // set number might be of the form "1/3"
                // we just read the number before the slash
                int num = parseSubstring(value, 0, 0);
                mTrack = (num * 1000) + (mTrack % 1000);
            } else if (name.equalsIgnoreCase("duration")) {
                mDuration = parseSubstring(value, 0, 0);
<<<<<<< HEAD
            } else if (name.equalsIgnoreCase("writer") || name.startsWith("writer;")) {
                mWriter = value.trim();
            } else if (name.equalsIgnoreCase("compilation")) {
                mCompilation = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("isdrm")) {
                mIsDrm = (parseSubstring(value, 0, 0) == 1);
            } else if (name.equalsIgnoreCase("width")) {
                mWidth = parseSubstring(value, 0, 0);
            } else if (name.equalsIgnoreCase("height")) {
                mHeight = parseSubstring(value, 0, 0);
            } else {
                //Log.v(TAG, "unknown tag: " + name + " (" + mProcessGenres + ")");
            }
        }

        private boolean convertGenreCode(String input, String expected) {
            String output = getGenreName(input);
            if (output.equals(expected)) {
                return true;
            } else {
                Log.d(TAG, "'" + input + "' -> '" + output + "', expected '" + expected + "'");
                return false;
            }
        }
        private void testGenreNameConverter() {
            convertGenreCode("2", "Country");
            convertGenreCode("(2)", "Country");
            convertGenreCode("(2", "(2");
            convertGenreCode("2 Foo", "Country");
            convertGenreCode("(2) Foo", "Country");
            convertGenreCode("(2 Foo", "(2 Foo");
            convertGenreCode("2Foo", "2Foo");
            convertGenreCode("(2)Foo", "Country");
            convertGenreCode("200 Foo", "Foo");
            convertGenreCode("(200) Foo", "Foo");
            convertGenreCode("200Foo", "200Foo");
            convertGenreCode("(200)Foo", "Foo");
            convertGenreCode("200)Foo", "200)Foo");
            convertGenreCode("200) Foo", "200) Foo");
        }

        public String getGenreName(String genreTagValue) {

            if (genreTagValue == null) {
                return null;
            }
            final int length = genreTagValue.length();

            if (length > 0) {
                boolean parenthesized = false;
                StringBuffer number = new StringBuffer();
                int i = 0;
                for (; i < length; ++i) {
                    char c = genreTagValue.charAt(i);
                    if (i == 0 && c == '(') {
                        parenthesized = true;
                    } else if (Character.isDigit(c)) {
                        number.append(c);
                    } else {
                        break;
                    }
                }
                char charAfterNumber = i < length ? genreTagValue.charAt(i) : ' ';
                if ((parenthesized && charAfterNumber == ')')
                        || !parenthesized && Character.isWhitespace(charAfterNumber)) {
                    try {
                        short genreIndex = Short.parseShort(number.toString());
                        if (genreIndex >= 0) {
                            if (genreIndex < ID3_GENRES.length) {
                                return ID3_GENRES[genreIndex];
                            } else if (genreIndex == 0xFF) {
                                return null;
                            } else if (genreIndex < 0xFF && (i + 1) < length) {
                                // genre is valid but unknown,
                                // if there is a string after the value we take it
                                if (parenthesized && charAfterNumber == ')') {
                                    i++;
                                }
                                String ret = genreTagValue.substring(i).trim();
                                if (ret.length() != 0) {
                                    return ret;
                                }
                            } else {
                                // else return the number, without parentheses
                                return number.toString();
                            }
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }

            return genreTagValue;
        }

        private void processImageFile(String path) {
            try {
                mBitmapOptions.outWidth = 0;
                mBitmapOptions.outHeight = 0;
                BitmapFactory.decodeFile(path, mBitmapOptions);
                mWidth = mBitmapOptions.outWidth;
                mHeight = mBitmapOptions.outHeight;
            } catch (Throwable th) {
                // ignore;
            }
        }

        public void setMimeType(String mimeType) {
            if ("audio/mp4".equals(mMimeType) &&
                    mimeType.startsWith("video")) {
                // for feature parity with Donut, we force m4a files to keep the
                // audio/mp4 mimetype, even if they are really "enhanced podcasts"
                // with a video track
                return;
            }
            mMimeType = mimeType;
            mFileType = MediaFile.getFileTypeForMimeType(mimeType);
        }

        /**
         * Formats the data into a values array suitable for use with the Media
         * Content Provider.
         *
=======
            }
        }
        
        public void setMimeType(String mimeType) {
            mMimeType = mimeType;
            mFileType = MediaFile.getFileTypeForMimeType(mimeType);
        }
        
        /**
         * Formats the data into a values array suitable for use with the Media
         * Content Provider.
         * 
>>>>>>> 54b6cfa... Initial Contribution
         * @return a map of values
         */
        private ContentValues toValues() {
            ContentValues map = new ContentValues();

            map.put(MediaStore.MediaColumns.DATA, mPath);
            map.put(MediaStore.MediaColumns.TITLE, mTitle);
            map.put(MediaStore.MediaColumns.DATE_MODIFIED, mLastModified);
            map.put(MediaStore.MediaColumns.SIZE, mFileSize);
            map.put(MediaStore.MediaColumns.MIME_TYPE, mMimeType);
<<<<<<< HEAD
            map.put(MediaStore.MediaColumns.IS_DRM, mIsDrm);

            String resolution = null;
            if (mWidth > 0 && mHeight > 0) {
                map.put(MediaStore.MediaColumns.WIDTH, mWidth);
                map.put(MediaStore.MediaColumns.HEIGHT, mHeight);
                resolution = mWidth + "x" + mHeight;
            }

            if (!mNoMedia) {
                if (MediaFile.isVideoFileType(mFileType)) {
                    map.put(Video.Media.ARTIST, (mArtist != null && mArtist.length() > 0
                            ? mArtist : MediaStore.UNKNOWN_STRING));
                    map.put(Video.Media.ALBUM, (mAlbum != null && mAlbum.length() > 0
                            ? mAlbum : MediaStore.UNKNOWN_STRING));
                    map.put(Video.Media.DURATION, mDuration);
                    if (resolution != null) {
                        map.put(Video.Media.RESOLUTION, resolution);
                    }
                } else if (MediaFile.isImageFileType(mFileType)) {
                    // FIXME - add DESCRIPTION
                } else if (MediaFile.isAudioFileType(mFileType)) {
                    map.put(Audio.Media.ARTIST, (mArtist != null && mArtist.length() > 0) ?
                            mArtist : MediaStore.UNKNOWN_STRING);
                    map.put(Audio.Media.ALBUM_ARTIST, (mAlbumArtist != null &&
                            mAlbumArtist.length() > 0) ? mAlbumArtist : null);
                    map.put(Audio.Media.ALBUM, (mAlbum != null && mAlbum.length() > 0) ?
                            mAlbum : MediaStore.UNKNOWN_STRING);
                    map.put(Audio.Media.COMPOSER, mComposer);
                    map.put(Audio.Media.GENRE, mGenre);
                    if (mYear != 0) {
                        map.put(Audio.Media.YEAR, mYear);
                    }
                    map.put(Audio.Media.TRACK, mTrack);
                    map.put(Audio.Media.DURATION, mDuration);
                    map.put(Audio.Media.COMPILATION, mCompilation);
                }
            }
            return map;
        }

        private Uri endFile(FileEntry entry, boolean ringtones, boolean notifications,
                boolean alarms, boolean music, boolean podcasts)
                throws RemoteException {
            // update database

            // use album artist if artist is missing
=======
            
            if (MediaFile.isVideoFileType(mFileType)) {
                map.put(Video.Media.ARTIST, (mArtist != null && mArtist.length() > 0 ? mArtist : MediaFile.UNKNOWN_STRING));
                map.put(Video.Media.ALBUM, (mAlbum != null && mAlbum.length() > 0 ? mAlbum : MediaFile.UNKNOWN_STRING));
                map.put(Video.Media.DURATION, mDuration);
                // FIXME - add RESOLUTION
            } else if (MediaFile.isImageFileType(mFileType)) {
                // FIXME - add DESCRIPTION
                // map.put(field, value);
            } else if (MediaFile.isAudioFileType(mFileType)) {
                map.put(Audio.Media.ARTIST, (mArtist != null && mArtist.length() > 0 ? mArtist : MediaFile.UNKNOWN_STRING));
                map.put(Audio.Media.ALBUM, (mAlbum != null && mAlbum.length() > 0 ? mAlbum : MediaFile.UNKNOWN_STRING));
                map.put(Audio.Media.COMPOSER, mComposer);
                if (mYear != 0) {
                    map.put(Audio.Media.YEAR, mYear);
                }
                map.put(Audio.Media.TRACK, mTrack);
                map.put(Audio.Media.DURATION, mDuration);
            }
            return map;
        }
    
        public Uri endFile(FileCacheEntry entry, boolean ringtones, boolean notifications, boolean alarms, boolean music) 
                throws RemoteException {
            // update database
            Uri tableUri;
            boolean isAudio = MediaFile.isAudioFileType(mFileType);
            boolean isVideo = MediaFile.isVideoFileType(mFileType);
            boolean isImage = MediaFile.isImageFileType(mFileType);
            if (isVideo) {
                tableUri = mVideoUri;
            } else if (isImage) {
                tableUri = mImagesUri;
            } else if (isAudio) {
                tableUri = mAudioUri;
            } else {
                // don't add file to database if not audio, video or image
                return null;
            }
            entry.mTableUri = tableUri;
            
             // use album artist if artist is missing
>>>>>>> 54b6cfa... Initial Contribution
            if (mArtist == null || mArtist.length() == 0) {
                mArtist = mAlbumArtist;
            }

            ContentValues values = toValues();
            String title = values.getAsString(MediaStore.MediaColumns.TITLE);
<<<<<<< HEAD
            if (title == null || TextUtils.isEmpty(title.trim())) {
                title = MediaFile.getFileTitle(values.getAsString(MediaStore.MediaColumns.DATA));
                values.put(MediaStore.MediaColumns.TITLE, title);
            }
            String album = values.getAsString(Audio.Media.ALBUM);
            if (MediaStore.UNKNOWN_STRING.equals(album)) {
                album = values.getAsString(MediaStore.MediaColumns.DATA);
                // extract last path segment before file name
                int lastSlash = album.lastIndexOf('/');
                if (lastSlash >= 0) {
                    int previousSlash = 0;
                    while (true) {
                        int idx = album.indexOf('/', previousSlash + 1);
                        if (idx < 0 || idx >= lastSlash) {
                            break;
                        }
                        previousSlash = idx;
                    }
                    if (previousSlash != 0) {
                        album = album.substring(previousSlash + 1, lastSlash);
                        values.put(Audio.Media.ALBUM, album);
                    }
                }
            }
            long rowId = entry.mRowId;
            if (MediaFile.isAudioFileType(mFileType) && (rowId == 0 || mMtpObjectHandle != 0)) {
                // Only set these for new entries. For existing entries, they
                // may have been modified later, and we want to keep the current
                // values so that custom ringtones still show up in the ringtone
                // picker.
=======
            if (TextUtils.isEmpty(title)) {
                title = values.getAsString(MediaStore.MediaColumns.DATA);
                // extract file name after last slash
                int lastSlash = title.lastIndexOf('/');
                if (lastSlash >= 0) {
                    lastSlash++;
                    if (lastSlash < title.length()) {
                        title = title.substring(lastSlash);
                    }
                }
                // truncate the file extension (if any)
                int lastDot = title.lastIndexOf('.');
                if (lastDot > 0) {
                    title = title.substring(0, lastDot);
                }
                values.put(MediaStore.MediaColumns.TITLE, title);
            }
            if (isAudio) {
>>>>>>> 54b6cfa... Initial Contribution
                values.put(Audio.Media.IS_RINGTONE, ringtones);
                values.put(Audio.Media.IS_NOTIFICATION, notifications);
                values.put(Audio.Media.IS_ALARM, alarms);
                values.put(Audio.Media.IS_MUSIC, music);
<<<<<<< HEAD
                values.put(Audio.Media.IS_PODCAST, podcasts);
            } else if (mFileType == MediaFile.FILE_TYPE_JPEG && !mNoMedia) {
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(entry.mPath);
                } catch (IOException ex) {
                    // exif is null
                }
                if (exif != null) {
                    float[] latlng = new float[2];
                    if (exif.getLatLong(latlng)) {
                        values.put(Images.Media.LATITUDE, latlng[0]);
                        values.put(Images.Media.LONGITUDE, latlng[1]);
                    }

                    long time = exif.getGpsDateTime();
                    if (time != -1) {
                        values.put(Images.Media.DATE_TAKEN, time);
                    } else {
                        // If no time zone information is available, we should consider using
                        // EXIF local time as taken time if the difference between file time
                        // and EXIF local time is not less than 1 Day, otherwise MediaProvider
                        // will use file time as taken time.
                        time = exif.getDateTime();
                        if (time != -1 && Math.abs(mLastModified * 1000 - time) >= 86400000) {
                            values.put(Images.Media.DATE_TAKEN, time);
                        }
                    }

                    int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, -1);
                    if (orientation != -1) {
                        // We only recognize a subset of orientation tag values.
                        int degree;
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                degree = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                degree = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                degree = 270;
                                break;
                            default:
                                degree = 0;
                                break;
                        }
                        values.put(Images.Media.ORIENTATION, degree);
                    }
                }
            }

            Uri tableUri = mFilesUri;
            MediaInserter inserter = mMediaInserter;
            if (!mNoMedia) {
                if (MediaFile.isVideoFileType(mFileType)) {
                    tableUri = mVideoUri;
                } else if (MediaFile.isImageFileType(mFileType)) {
                    tableUri = mImagesUri;
                } else if (MediaFile.isAudioFileType(mFileType)) {
                    tableUri = mAudioUri;
                }
            }
            Uri result = null;
            boolean needToSetSettings = false;
            if (rowId == 0) {
                if (mMtpObjectHandle != 0) {
                    values.put(MediaStore.MediaColumns.MEDIA_SCANNER_NEW_OBJECT_ID, mMtpObjectHandle);
                }
                if (tableUri == mFilesUri) {
                    int format = entry.mFormat;
                    if (format == 0) {
                        format = MediaFile.getFormatCode(entry.mPath, mMimeType);
                    }
                    values.put(Files.FileColumns.FORMAT, format);
                }
                // Setting a flag in order not to use bulk insert for the file related with
                // notifications, ringtones, and alarms, because the rowId of the inserted file is
                // needed.
                if (mWasEmptyPriorToScan) {
                    if (notifications && !mDefaultNotificationSet) {
                        if (TextUtils.isEmpty(mDefaultNotificationFilename) ||
                                doesPathHaveFilename(entry.mPath, mDefaultNotificationFilename)) {
                            needToSetSettings = true;
                        }
                    } else if (ringtones && !mDefaultRingtoneSet) {
                        if (TextUtils.isEmpty(mDefaultRingtoneFilename) ||
                                doesPathHaveFilename(entry.mPath, mDefaultRingtoneFilename)) {
                            needToSetSettings = true;
                        }
                    } else if (alarms && !mDefaultAlarmSet) {
                        if (TextUtils.isEmpty(mDefaultAlarmAlertFilename) ||
                                doesPathHaveFilename(entry.mPath, mDefaultAlarmAlertFilename)) {
                            needToSetSettings = true;
                        }
                    }
                }

                // New file, insert it.
                // Directories need to be inserted before the files they contain, so they
                // get priority when bulk inserting.
                // If the rowId of the inserted file is needed, it gets inserted immediately,
                // bypassing the bulk inserter.
                if (inserter == null || needToSetSettings) {
                    result = mMediaProvider.insert(tableUri, values);
                } else if (entry.mFormat == MtpConstants.FORMAT_ASSOCIATION) {
                    inserter.insertwithPriority(tableUri, values);
                } else {
                    inserter.insert(tableUri, values);
                }

=======
            } else if (isImage) {
                File parentFile = new File(entry.mPath).getParentFile();
                if (parentFile == null) {
                    return null;
                }
                String path = parentFile.toString().toLowerCase();
                String name = parentFile.getName().toLowerCase();
                
                values.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
                values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
            }
            
            Uri result = null;
            long rowId = entry.mRowId;
            if (rowId == 0) {
                // new file, insert it
                result = mMediaProvider.insert(tableUri, values);
>>>>>>> 54b6cfa... Initial Contribution
                if (result != null) {
                    rowId = ContentUris.parseId(result);
                    entry.mRowId = rowId;
                }
            } else {
                // updated file
                result = ContentUris.withAppendedId(tableUri, rowId);
<<<<<<< HEAD
                // path should never change, and we want to avoid replacing mixed cased paths
                // with squashed lower case paths
                values.remove(MediaStore.MediaColumns.DATA);

                int mediaType = 0;
                if (!MediaScanner.isNoMediaPath(entry.mPath)) {
                    int fileType = MediaFile.getFileTypeForMimeType(mMimeType);
                    if (MediaFile.isAudioFileType(fileType)) {
                        mediaType = FileColumns.MEDIA_TYPE_AUDIO;
                    } else if (MediaFile.isVideoFileType(fileType)) {
                        mediaType = FileColumns.MEDIA_TYPE_VIDEO;
                    } else if (MediaFile.isImageFileType(fileType)) {
                        mediaType = FileColumns.MEDIA_TYPE_IMAGE;
                    } else if (MediaFile.isPlayListFileType(fileType)) {
                        mediaType = FileColumns.MEDIA_TYPE_PLAYLIST;
                    }
                    values.put(FileColumns.MEDIA_TYPE, mediaType);
                }

                mMediaProvider.update(result, values, null, null);
            }

            if(needToSetSettings) {
                if (notifications) {
                    setSettingIfNotSet(Settings.System.NOTIFICATION_SOUND, tableUri, rowId);
                    mDefaultNotificationSet = true;
                } else if (ringtones) {
                    setSettingIfNotSet(Settings.System.RINGTONE, tableUri, rowId);
                    mDefaultRingtoneSet = true;
                } else if (alarms) {
                    setSettingIfNotSet(Settings.System.ALARM_ALERT, tableUri, rowId);
                    mDefaultAlarmSet = true;
                }
            }

            return result;
        }

=======
                mMediaProvider.update(result, values, null, null);
            }
            if (mProcessGenres && mGenre != null) {
                String genre = mGenre;
                Uri uri = mGenreCache.get(genre);
                if (uri == null) {
                    Cursor cursor = null;
                    try {
                        // see if the genre already exists
                        cursor = mMediaProvider.query(
                                mGenresUri,
                                GENRE_LOOKUP_PROJECTION, MediaStore.Audio.Genres.NAME + "=?",
                                        new String[] { genre }, null);
                        if (cursor == null || cursor.getCount() == 0) {
                            // genre does not exist, so create the genre in the genre table
                            values.clear();
                            values.put(MediaStore.Audio.Genres.NAME, genre);
                            uri = mMediaProvider.insert(mGenresUri, values);
                        } else {
                            // genre already exists, so compute its Uri
                            cursor.moveToNext();
                            uri = ContentUris.withAppendedId(mGenresUri, cursor.getLong(0));
                        }
                        if (uri != null) {
                            uri = Uri.withAppendedPath(uri, Genres.Members.CONTENT_DIRECTORY);
                            mGenreCache.put(genre, uri);
                        }
                    } finally {
                        // release the cursor if it exists
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
              
                if (uri != null) {
                    // add entry to audio_genre_map  
                    values.clear();
                    values.put(MediaStore.Audio.Genres.Members.AUDIO_ID, Long.valueOf(rowId));
                    mMediaProvider.insert(uri, values);
                }
            }
            
            if (notifications && !mDefaultNotificationSet) {
                if (TextUtils.isEmpty(mDefaultNotificationFilename) ||
                        doesPathHaveFilename(entry.mPath, mDefaultNotificationFilename)) {
                    setSettingIfNotSet(Settings.System.NOTIFICATION_SOUND, tableUri, rowId);
                    mDefaultNotificationSet = true;
                }
            } else if (ringtones && !mDefaultRingtoneSet) {
                if (TextUtils.isEmpty(mDefaultRingtoneFilename) ||
                        doesPathHaveFilename(entry.mPath, mDefaultRingtoneFilename)) {
                    setSettingIfNotSet(Settings.System.RINGTONE, tableUri, rowId);
                    mDefaultRingtoneSet = true;
                }
            }
            
            return result;
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        private boolean doesPathHaveFilename(String path, String filename) {
            int pathFilenameStart = path.lastIndexOf(File.separatorChar) + 1;
            int filenameLength = filename.length();
            return path.regionMatches(pathFilenameStart, filename, 0, filenameLength) &&
                    pathFilenameStart + filenameLength == path.length();
        }
<<<<<<< HEAD

        private void setSettingIfNotSet(String settingName, Uri uri, long rowId) {

            String existingSettingValue = Settings.System.getString(mContext.getContentResolver(),
                    settingName);

=======
        
        private void setSettingIfNotSet(String settingName, Uri uri, long rowId) {
            
            String existingSettingValue = Settings.System.getString(mContext.getContentResolver(),
                    settingName);
            
>>>>>>> 54b6cfa... Initial Contribution
            if (TextUtils.isEmpty(existingSettingValue)) {
                // Set the setting to the given URI
                Settings.System.putString(mContext.getContentResolver(), settingName,
                        ContentUris.withAppendedId(uri, rowId).toString());
            }
        }
<<<<<<< HEAD

        private int getFileTypeFromDrm(String path) {
            if (!isDrmEnabled()) {
                return 0;
            }

            int resultFileType = 0;

            if (mDrmManagerClient == null) {
                mDrmManagerClient = new DrmManagerClient(mContext);
            }

            if (mDrmManagerClient.canHandle(path, null)) {
                String drmMimetype = mDrmManagerClient.getOriginalMimeType(path);
                if (drmMimetype != null) {
                    mMimeType = drmMimetype;
                    resultFileType = MediaFile.getFileTypeForMimeType(drmMimetype);
                }
            }
            return resultFileType;
        }

    }; // end of anonymous MediaScannerClient instance

    private void prescan(String filePath, boolean prescanFiles) throws RemoteException {
        Cursor c = null;
        String where = null;
        String[] selectionArgs = null;

        if (mPlayLists == null) {
            mPlayLists = new ArrayList<FileEntry>();
        } else {
            mPlayLists.clear();
        }

        if (filePath != null) {
            // query for only one file
            where = MediaStore.Files.FileColumns._ID + ">?" +
                " AND " + Files.FileColumns.DATA + "=?";
            selectionArgs = new String[] { "", filePath };
        } else {
            where = MediaStore.Files.FileColumns._ID + ">?";
            selectionArgs = new String[] { "" };
        }

        // Tell the provider to not delete the file.
        // If the file is truly gone the delete is unnecessary, and we want to avoid
        // accidentally deleting files that are really there (this may happen if the
        // filesystem is mounted and unmounted while the scanner is running).
        Uri.Builder builder = mFilesUri.buildUpon();
        builder.appendQueryParameter(MediaStore.PARAM_DELETE_DATA, "false");
        MediaBulkDeleter deleter = new MediaBulkDeleter(mMediaProvider, builder.build());

        // Build the list of files from the content provider
        try {
            if (prescanFiles) {
                // First read existing files from the files table.
                // Because we'll be deleting entries for missing files as we go,
                // we need to query the database in small batches, to avoid problems
                // with CursorWindow positioning.
                long lastId = Long.MIN_VALUE;
                Uri limitUri = mFilesUri.buildUpon().appendQueryParameter("limit", "1000").build();
                mWasEmptyPriorToScan = true;

                while (true) {
                    selectionArgs[0] = "" + lastId;
                    if (c != null) {
                        c.close();
                        c = null;
                    }
                    c = mMediaProvider.query(limitUri, FILES_PRESCAN_PROJECTION,
                            where, selectionArgs, MediaStore.Files.FileColumns._ID, null);
                    if (c == null) {
                        break;
                    }

                    int num = c.getCount();

                    if (num == 0) {
                        break;
                    }
                    mWasEmptyPriorToScan = false;
                    while (c.moveToNext()) {
                        long rowId = c.getLong(FILES_PRESCAN_ID_COLUMN_INDEX);
                        String path = c.getString(FILES_PRESCAN_PATH_COLUMN_INDEX);
                        int format = c.getInt(FILES_PRESCAN_FORMAT_COLUMN_INDEX);
                        long lastModified = c.getLong(FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX);
                        lastId = rowId;

                        // Only consider entries with absolute path names.
                        // This allows storing URIs in the database without the
                        // media scanner removing them.
                        if (path != null && path.startsWith("/")) {
                            boolean exists = false;
                            try {
                                exists = Libcore.os.access(path, libcore.io.OsConstants.F_OK);
                            } catch (ErrnoException e1) {
                            }
                            if (!exists && !MtpConstants.isAbstractObject(format)) {
                                // do not delete missing playlists, since they may have been
                                // modified by the user.
                                // The user can delete them in the media player instead.
                                // instead, clear the path and lastModified fields in the row
                                MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
                                int fileType = (mediaFileType == null ? 0 : mediaFileType.fileType);

                                if (!MediaFile.isPlayListFileType(fileType)) {
                                    deleter.delete(rowId);
                                    if (path.toLowerCase(Locale.US).endsWith("/.nomedia")) {
                                        deleter.flush();
                                        String parent = new File(path).getParent();
                                        mMediaProvider.call(MediaStore.UNHIDE_CALL, parent, null);
                                    }
                                }
                            }
                        }
=======
        
    }; // end of anonymous MediaScannerClient instance
    
    private void prescan(String filePath) throws RemoteException {
        Cursor c = null;
        String where = null;
        String[] selectionArgs = null;
         
        if (mFileCache == null) {
            mFileCache = new HashMap<String, FileCacheEntry>();
        } else {
            mFileCache.clear();
        }
        if (mPlayLists == null) {
            mPlayLists = new ArrayList<FileCacheEntry>();
        } else {
            mPlayLists.clear();
        }
  
        // Build the list of files from the content provider
        try {
            // Read existing files from the audio table
            if (filePath != null) {
                where = MediaStore.Audio.Media.DATA + "=?";
                selectionArgs = new String[] { filePath };
            }
            c = mMediaProvider.query(mAudioUri, AUDIO_PROJECTION, where, selectionArgs, null);
 
            if (c != null) {
                try {
                    while (c.moveToNext()) {
                        long rowId = c.getLong(ID_AUDIO_COLUMN_INDEX);
                        String path = c.getString(PATH_AUDIO_COLUMN_INDEX);
                        long lastModified = c.getLong(DATE_MODIFIED_AUDIO_COLUMN_INDEX);
                        
                        String key = path;
                        if (mCaseInsensitivePaths) {
                            key = path.toLowerCase();
                        }
                        mFileCache.put(key, new FileCacheEntry(mAudioUri, rowId, path,
                                lastModified));
                    }
                } finally {
                    c.close();
                    c = null;
                }
            }

            // Read existing files from the video table
            if (filePath != null) {
                where = MediaStore.Video.Media.DATA + "=?";
            } else {
                where = null;
            }
            c = mMediaProvider.query(mVideoUri, VIDEO_PROJECTION, where, selectionArgs, null);
 
            if (c != null) {
                try {
                    while (c.moveToNext()) {
                        long rowId = c.getLong(ID_VIDEO_COLUMN_INDEX);
                        String path = c.getString(PATH_VIDEO_COLUMN_INDEX);
                        long lastModified = c.getLong(DATE_MODIFIED_VIDEO_COLUMN_INDEX);
                        
                        String key = path;
                        if (mCaseInsensitivePaths) {
                            key = path.toLowerCase();
                        }
                        mFileCache.put(key, new FileCacheEntry(mVideoUri, rowId, path,
                                lastModified));
                    }
                } finally {
                    c.close();
                    c = null;
                }
            }

            // Read existing files from the images table
            if (filePath != null) {
                where = MediaStore.Images.Media.DATA + "=?";
            } else {
                where = null;
            }
            mOriginalCount = 0;
            c = mMediaProvider.query(mImagesUri, IMAGES_PROJECTION, where, selectionArgs, null);
 
            if (c != null) {
                try {
                    mOriginalCount = c.getCount();
                    while (c.moveToNext()) {
                        long rowId = c.getLong(ID_IMAGES_COLUMN_INDEX);
                        String path = c.getString(PATH_IMAGES_COLUMN_INDEX);
                       long lastModified = c.getLong(DATE_MODIFIED_IMAGES_COLUMN_INDEX);
    
                        String key = path;
                        if (mCaseInsensitivePaths) {
                            key = path.toLowerCase();
                        }
                        mFileCache.put(key, new FileCacheEntry(mImagesUri, rowId, path,
                                lastModified));
                    }
                } finally {
                    c.close();
                    c = null;
                }
            }
            
            if (mProcessPlaylists) {
                // Read existing files from the playlists table
                if (filePath != null) {
                    where = MediaStore.Audio.Playlists.DATA + "=?";
                } else {
                    where = null;
                }
                c = mMediaProvider.query(mPlaylistsUri, PLAYLISTS_PROJECTION, where, selectionArgs, null);
     
                if (c != null) {
                    try {
                        while (c.moveToNext()) {
                            String path = c.getString(PATH_IMAGES_COLUMN_INDEX);
            
                            if (path != null && path.length() > 0) {
                                long rowId = c.getLong(ID_PLAYLISTS_COLUMN_INDEX);
                                long lastModified = c.getLong(DATE_MODIFIED_PLAYLISTS_COLUMN_INDEX);
    
                                String key = path;
                                if (mCaseInsensitivePaths) {
                                    key = path.toLowerCase();
                                }
                                mFileCache.put(key, new FileCacheEntry(mPlaylistsUri, rowId, path,
                                        lastModified));
                            }
                        }
                    } finally {
                        c.close();
                        c = null;
>>>>>>> 54b6cfa... Initial Contribution
                    }
                }
            }
        }
        finally {
            if (c != null) {
                c.close();
            }
<<<<<<< HEAD
            deleter.flush();
        }

        // compute original size of images
        mOriginalCount = 0;
        c = mMediaProvider.query(mImagesUri, ID_PROJECTION, null, null, null, null);
        if (c != null) {
            mOriginalCount = c.getCount();
            c.close();
        }
    }

    private boolean inScanDirectory(String path, String[] directories) {
        for (int i = 0; i < directories.length; i++) {
            String directory = directories[i];
            if (path.startsWith(directory)) {
=======
        }
    }
    
    private boolean inScanDirectory(String path, String[] directories) {
        for (int i = 0; i < directories.length; i++) {
            if (path.startsWith(directories[i])) {
>>>>>>> 54b6cfa... Initial Contribution
                return true;
            }
        }
        return false;
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    private void pruneDeadThumbnailFiles() {
        HashSet<String> existingFiles = new HashSet<String>();
        String directory = "/sdcard/DCIM/.thumbnails";
        String [] files = (new File(directory)).list();
        if (files == null)
            files = new String[0];
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        for (int i = 0; i < files.length; i++) {
            String fullPathString = directory + "/" + files[i];
            existingFiles.add(fullPathString);
        }
<<<<<<< HEAD

        try {
            Cursor c = mMediaProvider.query(
                    mThumbsUri,
                    new String [] { "_data" },
                    null,
                    null,
                    null, null);
=======
        
        try {
            Cursor c = mMediaProvider.query(
                    mThumbsUri, 
                    new String [] { "_data" }, 
                    null, 
                    null, 
                    null);
>>>>>>> 54b6cfa... Initial Contribution
            Log.v(TAG, "pruneDeadThumbnailFiles... " + c);
            if (c != null && c.moveToFirst()) {
                do {
                    String fullPathString = c.getString(0);
                    existingFiles.remove(fullPathString);
                } while (c.moveToNext());
            }
<<<<<<< HEAD

            for (String fileToDelete : existingFiles) {
                if (false)
=======
            
            for (String fileToDelete : existingFiles) {
                if (Config.LOGV)
>>>>>>> 54b6cfa... Initial Contribution
                    Log.v(TAG, "fileToDelete is " + fileToDelete);
                try {
                    (new File(fileToDelete)).delete();
                } catch (SecurityException ex) {
                }
            }
<<<<<<< HEAD

=======
            
>>>>>>> 54b6cfa... Initial Contribution
            Log.v(TAG, "/pruneDeadThumbnailFiles... " + c);
            if (c != null) {
                c.close();
            }
        } catch (RemoteException e) {
            // We will soon be killed...
        }
    }

<<<<<<< HEAD
    static class MediaBulkDeleter {
        StringBuilder whereClause = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<String>(100);
        IContentProvider mProvider;
        Uri mBaseUri;

        public MediaBulkDeleter(IContentProvider provider, Uri baseUri) {
            mProvider = provider;
            mBaseUri = baseUri;
        }

        public void delete(long id) throws RemoteException {
            if (whereClause.length() != 0) {
                whereClause.append(",");
            }
            whereClause.append("?");
            whereArgs.add("" + id);
            if (whereArgs.size() > 100) {
                flush();
            }
        }
        public void flush() throws RemoteException {
            int size = whereArgs.size();
            if (size > 0) {
                String [] foo = new String [size];
                foo = whereArgs.toArray(foo);
                int numrows = mProvider.delete(mBaseUri, MediaStore.MediaColumns._ID + " IN (" +
                        whereClause.toString() + ")", foo);
                //Log.i("@@@@@@@@@", "rows deleted: " + numrows);
                whereClause.setLength(0);
                whereArgs.clear();
            }
        }
    }

    private void postscan(String[] directories) throws RemoteException {

=======
    private void postscan(String[] directories) throws RemoteException {
        Iterator<FileCacheEntry> iterator = mFileCache.values().iterator();

        while (iterator.hasNext()) {
            FileCacheEntry entry = iterator.next();
            String path = entry.mPath;
            
            // remove database entries for files that no longer exist.
            boolean fileMissing = false;
            
            if (!entry.mSeenInFileSystem) {
                if (inScanDirectory(path, directories)) {
                    // we didn't see this file in the scan directory.
                    fileMissing = true;
                } else {
                    // the file is outside of our scan directory,
                    // so we need to check for file existence here.
                    File testFile = new File(path);
                    if (!testFile.exists()) {
                        fileMissing = true;
                    }
                }
            }
            
            if (fileMissing) {
                // do not delete missing playlists, since they may have been modified by the user.
                // the user can delete them in the media player instead.
                // instead, clear the path and lastModified fields in the row
                MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
                int fileType = (mediaFileType == null ? 0 : mediaFileType.fileType);

                if (MediaFile.isPlayListFileType(fileType)) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Audio.Playlists.DATA, "");
                    values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, 0);
                    mMediaProvider.update(ContentUris.withAppendedId(mPlaylistsUri, entry.mRowId), values, null, null);
                } else {
                    mMediaProvider.delete(ContentUris.withAppendedId(entry.mTableUri, entry.mRowId), null, null);
                    iterator.remove();
                }
            }
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        // handle playlists last, after we know what media files are on the storage.
        if (mProcessPlaylists) {
            processPlayLists();
        }
<<<<<<< HEAD

        if (mOriginalCount == 0 && mImagesUri.equals(Images.Media.getContentUri("external")))
            pruneDeadThumbnailFiles();

        // allow GC to clean up
        mPlayLists = null;
        mMediaProvider = null;
    }

    private void initialize(String volumeName) {
        mMediaProvider = mContext.getContentResolver().acquireProvider("media");

=======
        
        if (mOriginalCount == 0 && mImagesUri.equals(Images.Media.getContentUri("external")))
            pruneDeadThumbnailFiles();
        
        // allow GC to clean up
        mGenreCache = null;
        mPlayLists = null;
        mFileCache = null;
        mMediaProvider = null;
    }
    
    private void initialize(String volumeName) {
        mMediaProvider = mContext.getContentResolver().acquireProvider("media");
        
>>>>>>> 54b6cfa... Initial Contribution
        mAudioUri = Audio.Media.getContentUri(volumeName);
        mVideoUri = Video.Media.getContentUri(volumeName);
        mImagesUri = Images.Media.getContentUri(volumeName);
        mThumbsUri = Images.Thumbnails.getContentUri(volumeName);
<<<<<<< HEAD
        mFilesUri = Files.getContentUri(volumeName);
=======
>>>>>>> 54b6cfa... Initial Contribution

        if (!volumeName.equals("internal")) {
            // we only support playlists on external media
            mProcessPlaylists = true;
            mProcessGenres = true;
<<<<<<< HEAD
            mPlaylistsUri = Playlists.getContentUri(volumeName);

            mCaseInsensitivePaths = true;
        }
=======
            mGenreCache = new HashMap<String, Uri>();
            mGenresUri = Genres.getContentUri(volumeName);
            mPlaylistsUri = Playlists.getContentUri(volumeName);
            // assuming external storage is FAT (case insensitive), except on the simulator.
            if ( Process.supportsProcesses()) {
                mCaseInsensitivePaths = true;
            }
        }          
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void scanDirectories(String[] directories, String volumeName) {
        try {
            long start = System.currentTimeMillis();
<<<<<<< HEAD
            initialize(volumeName);
            prescan(null, true);
            long prescan = System.currentTimeMillis();

            if (ENABLE_BULK_INSERTS) {
                // create MediaInserter for bulk inserts
                mMediaInserter = new MediaInserter(mMediaProvider, 500);
            }

            for (int i = 0; i < directories.length; i++) {
                processDirectory(directories[i], mClient);
            }

            if (ENABLE_BULK_INSERTS) {
                // flush remaining inserts
                mMediaInserter.flushAll();
                mMediaInserter = null;
            }

            long scan = System.currentTimeMillis();
            postscan(directories);
            long end = System.currentTimeMillis();

            if (false) {
=======
            initialize(volumeName);    
            prescan(null);
            long prescan = System.currentTimeMillis();
            
            for (int i = 0; i < directories.length; i++) {
                processDirectory(directories[i], MediaFile.sFileExtensions, mClient);
            }
            long scan = System.currentTimeMillis();
            postscan(directories);
            long end = System.currentTimeMillis();
            
            if (Config.LOGD) {
>>>>>>> 54b6cfa... Initial Contribution
                Log.d(TAG, " prescan time: " + (prescan - start) + "ms\n");
                Log.d(TAG, "    scan time: " + (scan - prescan) + "ms\n");
                Log.d(TAG, "postscan time: " + (end - scan) + "ms\n");
                Log.d(TAG, "   total time: " + (end - start) + "ms\n");
            }
        } catch (SQLException e) {
            // this might happen if the SD card is removed while the media scanner is running
            Log.e(TAG, "SQLException in MediaScanner.scan()", e);
        } catch (UnsupportedOperationException e) {
            // this might happen if the SD card is removed while the media scanner is running
            Log.e(TAG, "UnsupportedOperationException in MediaScanner.scan()", e);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in MediaScanner.scan()", e);
        }
    }

    // this function is used to scan a single file
    public Uri scanSingleFile(String path, String volumeName, String mimeType) {
        try {
<<<<<<< HEAD
            initialize(volumeName);
            prescan(path, true);

            File file = new File(path);

            // lastModified is in milliseconds on Files.
            long lastModifiedSeconds = file.lastModified() / 1000;

            // always scan the file, so we can return the content://media Uri for existing files
            return mClient.doScanFile(path, mimeType, lastModifiedSeconds, file.length(),
                    false, true, false);
=======
            initialize(volumeName);        
            prescan(path);
    
            File file = new File(path);
            // always scan the file, so we can return the content://media Uri for existing files
            return mClient.doScanFile(path, mimeType, file.lastModified(), file.length(), true);
>>>>>>> 54b6cfa... Initial Contribution
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
            return null;
        }
    }

<<<<<<< HEAD
    private static boolean isNoMediaFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) return false;

        // special case certain file names
        // I use regionMatches() instead of substring() below
        // to avoid memory allocation
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash + 2 < path.length()) {
            // ignore those ._* files created by MacOS
            if (path.regionMatches(lastSlash + 1, "._", 0, 2)) {
                return true;
            }

            // ignore album art files created by Windows Media Player:
            // Folder.jpg, AlbumArtSmall.jpg, AlbumArt_{...}_Large.jpg
            // and AlbumArt_{...}_Small.jpg
            if (path.regionMatches(true, path.length() - 4, ".jpg", 0, 4)) {
                if (path.regionMatches(true, lastSlash + 1, "AlbumArt_{", 0, 10) ||
                        path.regionMatches(true, lastSlash + 1, "AlbumArt.", 0, 9)) {
                    return true;
                }
                int length = path.length() - lastSlash - 1;
                if ((length == 17 && path.regionMatches(
                        true, lastSlash + 1, "AlbumArtSmall", 0, 13)) ||
                        (length == 10
                         && path.regionMatches(true, lastSlash + 1, "Folder", 0, 6))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNoMediaPath(String path) {
        if (path == null) return false;

        // return true if file or any parent directory has name starting with a dot
        if (path.indexOf("/.") >= 0) return true;

        // now check to see if any parent directories have a ".nomedia" file
        // start from 1 so we don't bother checking in the root directory
        int offset = 1;
        while (offset >= 0) {
            int slashIndex = path.indexOf('/', offset);
            if (slashIndex > offset) {
                slashIndex++; // move past slash
                File file = new File(path.substring(0, slashIndex) + ".nomedia");
                if (file.exists()) {
                    // we have a .nomedia in one of the parent directories
                    return true;
                }
            }
            offset = slashIndex;
        }
        return isNoMediaFile(path);
    }

    public void scanMtpFile(String path, String volumeName, int objectHandle, int format) {
        initialize(volumeName);
        MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
        int fileType = (mediaFileType == null ? 0 : mediaFileType.fileType);
        File file = new File(path);
        long lastModifiedSeconds = file.lastModified() / 1000;

        if (!MediaFile.isAudioFileType(fileType) && !MediaFile.isVideoFileType(fileType) &&
            !MediaFile.isImageFileType(fileType) && !MediaFile.isPlayListFileType(fileType)) {

            // no need to use the media scanner, but we need to update last modified and file size
            ContentValues values = new ContentValues();
            values.put(Files.FileColumns.SIZE, file.length());
            values.put(Files.FileColumns.DATE_MODIFIED, lastModifiedSeconds);
            try {
                String[] whereArgs = new String[] {  Integer.toString(objectHandle) };
                mMediaProvider.update(Files.getMtpObjectsUri(volumeName), values, "_id=?",
                        whereArgs);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in scanMtpFile", e);
            }
            return;
        }

        mMtpObjectHandle = objectHandle;
        Cursor fileList = null;
        try {
            if (MediaFile.isPlayListFileType(fileType)) {
                // build file cache so we can look up tracks in the playlist
                prescan(null, true);

                FileEntry entry = makeEntryFor(path);
                if (entry != null) {
                    fileList = mMediaProvider.query(mFilesUri, FILES_PRESCAN_PROJECTION,
                            null, null, null, null);
                    processPlayList(entry, fileList);
                }
            } else {
                // MTP will create a file entry for us so we don't want to do it in prescan
                prescan(path, false);

                // always scan the file, so we can return the content://media Uri for existing files
                mClient.doScanFile(path, mediaFileType.mimeType, lastModifiedSeconds, file.length(),
                    (format == MtpConstants.FORMAT_ASSOCIATION), true, isNoMediaPath(path));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in MediaScanner.scanFile()", e);
        } finally {
            mMtpObjectHandle = 0;
            if (fileList != null) {
                fileList.close();
            }
        }
    }

    FileEntry makeEntryFor(String path) {
        String key = path;
        String where;
        String[] selectionArgs;
        if (mCaseInsensitivePaths) {
            // the 'like' makes it use the index, the 'lower()' makes it correct
            // when the path contains sqlite wildcard characters
            where = "_data LIKE ?1 AND lower(_data)=lower(?1)";
            selectionArgs = new String[] { path };
        } else {
            where = Files.FileColumns.DATA + "=?";
            selectionArgs = new String[] { path };
        }

        Cursor c = null;
        try {
            c = mMediaProvider.query(mFilesUri, FILES_PRESCAN_PROJECTION,
                    where, selectionArgs, null, null);
            if (c.moveToNext()) {
                long rowId = c.getLong(FILES_PRESCAN_ID_COLUMN_INDEX);
                int format = c.getInt(FILES_PRESCAN_FORMAT_COLUMN_INDEX);
                long lastModified = c.getLong(FILES_PRESCAN_DATE_MODIFIED_COLUMN_INDEX);
                return new FileEntry(rowId, path, lastModified, format);
            }
        } catch (RemoteException e) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    // returns the number of matching file/directory names, starting from the right
    private int matchPaths(String path1, String path2) {
        int result = 0;
        int end1 = path1.length();
        int end2 = path2.length();
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        while (end1 > 0 && end2 > 0) {
            int slash1 = path1.lastIndexOf('/', end1 - 1);
            int slash2 = path2.lastIndexOf('/', end2 - 1);
            int backSlash1 = path1.lastIndexOf('\\', end1 - 1);
            int backSlash2 = path2.lastIndexOf('\\', end2 - 1);
            int start1 = (slash1 > backSlash1 ? slash1 : backSlash1);
            int start2 = (slash2 > backSlash2 ? slash2 : backSlash2);
            if (start1 < 0) start1 = 0; else start1++;
            if (start2 < 0) start2 = 0; else start2++;
            int length = end1 - start1;
            if (end2 - start2 != length) break;
            if (path1.regionMatches(true, start1, path2, start2, length)) {
                result++;
                end1 = start1 - 1;
                end2 = start2 - 1;
            } else break;
        }
<<<<<<< HEAD

        return result;
    }

    private boolean matchEntries(long rowId, String data) {

        int len = mPlaylistEntries.size();
        boolean done = true;
        for (int i = 0; i < len; i++) {
            PlaylistEntry entry = mPlaylistEntries.get(i);
            if (entry.bestmatchlevel == Integer.MAX_VALUE) {
                continue; // this entry has been matched already
            }
            done = false;
            if (data.equalsIgnoreCase(entry.path)) {
                entry.bestmatchid = rowId;
                entry.bestmatchlevel = Integer.MAX_VALUE;
                continue; // no need for path matching
            }

            int matchLength = matchPaths(data, entry.path);
            if (matchLength > entry.bestmatchlevel) {
                entry.bestmatchid = rowId;
                entry.bestmatchlevel = matchLength;
            }
        }
        return done;
    }

    private void cachePlaylistEntry(String line, String playListDirectory) {
        PlaylistEntry entry = new PlaylistEntry();
        // watch for trailing whitespace
        int entryLength = line.length();
        while (entryLength > 0 && Character.isWhitespace(line.charAt(entryLength - 1))) entryLength--;
        // path should be longer than 3 characters.
        // avoid index out of bounds errors below by returning here.
        if (entryLength < 3) return;
        if (entryLength < line.length()) line = line.substring(0, entryLength);

        // does entry appear to be an absolute path?
        // look for Unix or DOS absolute paths
        char ch1 = line.charAt(0);
        boolean fullPath = (ch1 == '/' ||
                (Character.isLetter(ch1) && line.charAt(1) == ':' && line.charAt(2) == '\\'));
        // if we have a relative path, combine entry with playListDirectory
        if (!fullPath)
            line = playListDirectory + line;
        entry.path = line;
        //FIXME - should we look for "../" within the path?

        mPlaylistEntries.add(entry);
    }

    private void processCachedPlaylist(Cursor fileList, ContentValues values, Uri playlistUri) {
        fileList.moveToPosition(-1);
        while (fileList.moveToNext()) {
            long rowId = fileList.getLong(FILES_PRESCAN_ID_COLUMN_INDEX);
            String data = fileList.getString(FILES_PRESCAN_PATH_COLUMN_INDEX);
            if (matchEntries(rowId, data)) {
                break;
            }
        }

        int len = mPlaylistEntries.size();
        int index = 0;
        for (int i = 0; i < len; i++) {
            PlaylistEntry entry = mPlaylistEntries.get(i);
            if (entry.bestmatchlevel > 0) {
                try {
                    values.clear();
                    values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(index));
                    values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, Long.valueOf(entry.bestmatchid));
                    mMediaProvider.insert(playlistUri, values);
                    index++;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in MediaScanner.processCachedPlaylist()", e);
                    return;
                }
            }
        }
        mPlaylistEntries.clear();
    }

    private void processM3uPlayList(String path, String playListDirectory, Uri uri,
            ContentValues values, Cursor fileList) {
=======
               
        return result;
    }

    private boolean addPlayListEntry(String entry, String playListDirectory, 
            Uri uri, ContentValues values, int index) {
        
        // watch for trailing whitespace
        int entryLength = entry.length();
        while (entryLength > 0 && Character.isWhitespace(entry.charAt(entryLength - 1))) entryLength--;
        // path should be longer than 3 characters.
        // avoid index out of bounds errors below by returning here.
        if (entryLength < 3) return false;
        if (entryLength < entry.length()) entry = entry.substring(0, entryLength);

        // does entry appear to be an absolute path?
        // look for Unix or DOS absolute paths
        char ch1 = entry.charAt(0);
        boolean fullPath = (ch1 == '/' ||
                (Character.isLetter(ch1) && entry.charAt(1) == ':' && entry.charAt(2) == '\\'));
        // if we have a relative path, combine entry with playListDirectory
        if (!fullPath)
            entry = playListDirectory + entry;
            
        //FIXME - should we look for "../" within the path?
        
        // best matching MediaFile for the play list entry
        FileCacheEntry bestMatch = null;
        
        // number of rightmost file/directory names for bestMatch
        int bestMatchLength = 0;    
                
        Iterator<FileCacheEntry> iterator = mFileCache.values().iterator();
        while (iterator.hasNext()) {
            FileCacheEntry cacheEntry = iterator.next();
            String path = cacheEntry.mPath;
        
            if (path.equalsIgnoreCase(entry)) {
                bestMatch = cacheEntry;
                break;    // don't bother continuing search
            }
            
            int matchLength = matchPaths(path, entry);
            if (matchLength > bestMatchLength) {
                bestMatch = cacheEntry;
                bestMatchLength = matchLength;
            }
        }
        
        if (bestMatch == null) {
            return false;
        }
        
        try {
        // OK, now we need to add this to the database
            values.clear();
            values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(index));
            values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, Long.valueOf(bestMatch.mRowId));
            mMediaProvider.insert(uri, values);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in MediaScanner.addPlayListEntry()", e);
            return false;
        }

        return true;
    }
    
    private void processM3uPlayList(String path, String playListDirectory, Uri uri, ContentValues values) {
>>>>>>> 54b6cfa... Initial Contribution
        BufferedReader reader = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(f)), 8192);
                String line = reader.readLine();
<<<<<<< HEAD
                mPlaylistEntries.clear();
                while (line != null) {
                    // ignore comment lines, which begin with '#'
                    if (line.length() > 0 && line.charAt(0) != '#') {
                        cachePlaylistEntry(line, playListDirectory);
                    }
                    line = reader.readLine();
                }

                processCachedPlaylist(fileList, values, uri);
=======
                int index = 0;
                while (line != null) {
                    // ignore comment lines, which begin with '#'
                    if (line.length() > 0 && line.charAt(0) != '#') {
                        values.clear();
                        if (addPlayListEntry(line, playListDirectory, uri, values, index))
                            index++;
                    }
                    line = reader.readLine();
                }
>>>>>>> 54b6cfa... Initial Contribution
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException in MediaScanner.processM3uPlayList()", e);
            }
        }
    }

<<<<<<< HEAD
    private void processPlsPlayList(String path, String playListDirectory, Uri uri,
            ContentValues values, Cursor fileList) {
=======
    private void processPlsPlayList(String path, String playListDirectory, Uri uri, ContentValues values) {
>>>>>>> 54b6cfa... Initial Contribution
        BufferedReader reader = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(f)), 8192);
                String line = reader.readLine();
<<<<<<< HEAD
                mPlaylistEntries.clear();
=======
                int index = 0;
>>>>>>> 54b6cfa... Initial Contribution
                while (line != null) {
                    // ignore comment lines, which begin with '#'
                    if (line.startsWith("File")) {
                        int equals = line.indexOf('=');
                        if (equals > 0) {
<<<<<<< HEAD
                            cachePlaylistEntry(line.substring(equals + 1), playListDirectory);
=======
                            values.clear();
                            if (addPlayListEntry(line.substring(equals + 1), playListDirectory, uri, values, index))
                                index++;
>>>>>>> 54b6cfa... Initial Contribution
                        }
                    }
                    line = reader.readLine();
                }
<<<<<<< HEAD

                processCachedPlaylist(fileList, values, uri);
=======
>>>>>>> 54b6cfa... Initial Contribution
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException in MediaScanner.processPlsPlayList()", e);
            }
        }
    }

    class WplHandler implements ElementListener {

        final ContentHandler handler;
        String playListDirectory;
<<<<<<< HEAD

        public WplHandler(String playListDirectory, Uri uri, Cursor fileList) {
            this.playListDirectory = playListDirectory;

=======
        Uri uri;
        ContentValues values = new ContentValues();
        int index = 0;

        public WplHandler(String playListDirectory, Uri uri) {
            this.playListDirectory = playListDirectory;
            this.uri = uri;
            
>>>>>>> 54b6cfa... Initial Contribution
            RootElement root = new RootElement("smil");
            Element body = root.getChild("body");
            Element seq = body.getChild("seq");
            Element media = seq.getChild("media");
            media.setElementListener(this);

            this.handler = root.getContentHandler();
        }

<<<<<<< HEAD
        @Override
        public void start(Attributes attributes) {
            String path = attributes.getValue("", "src");
            if (path != null) {
                cachePlaylistEntry(path, playListDirectory);
            }
        }

       @Override
=======
        public void start(Attributes attributes) {
            String path = attributes.getValue("", "src");
            if (path != null) {
                values.clear();
                if (addPlayListEntry(path, playListDirectory, uri, values, index)) {
                    index++;
                }
            }
        }

>>>>>>> 54b6cfa... Initial Contribution
       public void end() {
       }

        ContentHandler getContentHandler() {
            return handler;
        }
    }

<<<<<<< HEAD
    private void processWplPlayList(String path, String playListDirectory, Uri uri,
            ContentValues values, Cursor fileList) {
=======
    private void processWplPlayList(String path, String playListDirectory, Uri uri) {
>>>>>>> 54b6cfa... Initial Contribution
        FileInputStream fis = null;
        try {
            File f = new File(path);
            if (f.exists()) {
                fis = new FileInputStream(f);

<<<<<<< HEAD
                mPlaylistEntries.clear();
                Xml.parse(fis, Xml.findEncodingByName("UTF-8"),
                        new WplHandler(playListDirectory, uri, fileList).getContentHandler());

                processCachedPlaylist(fileList, values, uri);
=======
                Xml.parse(fis, Xml.findEncodingByName("UTF-8"), new WplHandler(playListDirectory, uri).getContentHandler());
>>>>>>> 54b6cfa... Initial Contribution
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException in MediaScanner.processWplPlayList()", e);
            }
        }
    }
<<<<<<< HEAD

    private void processPlayList(FileEntry entry, Cursor fileList) throws RemoteException {
        String path = entry.mPath;
        ContentValues values = new ContentValues();
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash < 0) throw new IllegalArgumentException("bad path " + path);
        Uri uri, membersUri;
        long rowId = entry.mRowId;

        // make sure we have a name
        String name = values.getAsString(MediaStore.Audio.Playlists.NAME);
        if (name == null) {
            name = values.getAsString(MediaStore.MediaColumns.TITLE);
            if (name == null) {
                // extract name from file name
                int lastDot = path.lastIndexOf('.');
                name = (lastDot < 0 ? path.substring(lastSlash + 1)
                        : path.substring(lastSlash + 1, lastDot));
            }
        }

        values.put(MediaStore.Audio.Playlists.NAME, name);
        values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, entry.mLastModified);

        if (rowId == 0) {
            values.put(MediaStore.Audio.Playlists.DATA, path);
            uri = mMediaProvider.insert(mPlaylistsUri, values);
            rowId = ContentUris.parseId(uri);
            membersUri = Uri.withAppendedPath(uri, Playlists.Members.CONTENT_DIRECTORY);
        } else {
            uri = ContentUris.withAppendedId(mPlaylistsUri, rowId);
            mMediaProvider.update(uri, values, null, null);

            // delete members of existing playlist
            membersUri = Uri.withAppendedPath(uri, Playlists.Members.CONTENT_DIRECTORY);
            mMediaProvider.delete(membersUri, null, null);
        }

        String playListDirectory = path.substring(0, lastSlash + 1);
        MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
        int fileType = (mediaFileType == null ? 0 : mediaFileType.fileType);

        if (fileType == MediaFile.FILE_TYPE_M3U) {
            processM3uPlayList(path, playListDirectory, membersUri, values, fileList);
        } else if (fileType == MediaFile.FILE_TYPE_PLS) {
            processPlsPlayList(path, playListDirectory, membersUri, values, fileList);
        } else if (fileType == MediaFile.FILE_TYPE_WPL) {
            processWplPlayList(path, playListDirectory, membersUri, values, fileList);
        }
    }

    private void processPlayLists() throws RemoteException {
        Iterator<FileEntry> iterator = mPlayLists.iterator();
        Cursor fileList = null;
        try {
            // use the files uri and projection because we need the format column,
            // but restrict the query to just audio files
            fileList = mMediaProvider.query(mFilesUri, FILES_PRESCAN_PROJECTION,
                    "media_type=2", null, null, null);
            while (iterator.hasNext()) {
                FileEntry entry = iterator.next();
                // only process playlist files if they are new or have been modified since the last scan
                if (entry.mLastModifiedChanged) {
                    processPlayList(entry, fileList);
                }
            }
        } catch (RemoteException e1) {
        } finally {
            if (fileList != null) {
                fileList.close();
            }
        }
    }

    private native void processDirectory(String path, MediaScannerClient client);
    private native void processFile(String path, String mimeType, MediaScannerClient client);
    public native void setLocale(String locale);

    public native byte[] extractAlbumArt(FileDescriptor fd);

    private static native final void native_init();
    private native final void native_setup();
    private native final void native_finalize();

    /**
     * Releases resources associated with this MediaScanner object.
     * It is considered good practice to call this method when
     * one is done using the MediaScanner object. After this method
     * is called, the MediaScanner object can no longer be used.
     */
    public void release() {
        native_finalize();
    }

    @Override
    protected void finalize() {
        mContext.getContentResolver().releaseProvider(mMediaProvider);
        native_finalize();
=======
    
    private void processPlayLists() throws RemoteException {
        Iterator<FileCacheEntry> iterator = mPlayLists.iterator();
        while (iterator.hasNext()) {
            FileCacheEntry entry = iterator.next();
            String path = entry.mPath;  

            // only process playlist files if they are new or have been modified since the last scan
            if (entry.mLastModifiedChanged) {
                ContentValues values = new ContentValues();
                int lastSlash = path.lastIndexOf('/');
                if (lastSlash < 0) throw new IllegalArgumentException("bad path " + path);
                Uri uri, membersUri;
                long rowId = entry.mRowId;
                if (rowId == 0) {
                    // Create a new playlist
        
                    int lastDot = path.lastIndexOf('.');
                    String name = (lastDot < 0 ? path.substring(lastSlash + 1) : path.substring(lastSlash + 1, lastDot));
                    values.put(MediaStore.Audio.Playlists.NAME, name);
                    values.put(MediaStore.Audio.Playlists.DATA, path);
                    values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, entry.mLastModified);
                    uri = mMediaProvider.insert(mPlaylistsUri, values);
                    rowId = ContentUris.parseId(uri);
                    membersUri = Uri.withAppendedPath(uri, Playlists.Members.CONTENT_DIRECTORY);
                } else {
                    uri = ContentUris.withAppendedId(mPlaylistsUri, rowId);
                    
                    // update lastModified value of existing playlist
                    values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, entry.mLastModified);
                    mMediaProvider.update(uri, values, null, null);

                    // delete members of existing playlist
                    membersUri = Uri.withAppendedPath(uri, Playlists.Members.CONTENT_DIRECTORY);
                    mMediaProvider.delete(membersUri, null, null);
                }
               
                String playListDirectory = path.substring(0, lastSlash + 1);
                MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
                int fileType = (mediaFileType == null ? 0 : mediaFileType.fileType);

                if (fileType == MediaFile.FILE_TYPE_M3U)
                    processM3uPlayList(path, playListDirectory, membersUri, values);
                else if (fileType == MediaFile.FILE_TYPE_PLS)
                    processPlsPlayList(path, playListDirectory, membersUri, values);
                else if (fileType == MediaFile.FILE_TYPE_WPL)
                    processWplPlayList(path, playListDirectory, membersUri);
                    
                Cursor cursor = mMediaProvider.query(membersUri, PLAYLIST_MEMBERS_PROJECTION, null,
                        null, null);
                try {
                    if (cursor == null || cursor.getCount() == 0) {
                        Log.d(TAG, "playlist is empty - deleting");
                        mMediaProvider.delete(uri, null, null);
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }
            }
        }
    }
    
    private native void processDirectory(String path, String extensions, MediaScannerClient client);
    private native void processFile(String path, String mimeType, MediaScannerClient client);
    
    public native byte[] extractAlbumArt(FileDescriptor fd);

    private native final void native_setup();
    private native final void native_finalize();
    @Override
    protected void finalize() { 
        mContext.getContentResolver().releaseProvider(mMediaProvider);
        native_finalize(); 
>>>>>>> 54b6cfa... Initial Contribution
    }
}
