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

package android.media;

import android.content.ContentResolver;
import android.content.Context;
<<<<<<< HEAD
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.RemoteException;
=======
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
>>>>>>> 54b6cfa... Initial Contribution
import android.provider.DrmStore;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

<<<<<<< HEAD
=======
import java.io.FileDescriptor;
>>>>>>> 54b6cfa... Initial Contribution
import java.io.IOException;

/**
 * Ringtone provides a quick method for playing a ringtone, notification, or
 * other similar types of sounds.
 * <p>
 * For ways of retrieving {@link Ringtone} objects or to show a ringtone
 * picker, see {@link RingtoneManager}.
 * 
 * @see RingtoneManager
 */
public class Ringtone {
<<<<<<< HEAD
    private static final String TAG = "Ringtone";
    private static final boolean LOGD = true;
=======
    private static String TAG = "Ringtone";
>>>>>>> 54b6cfa... Initial Contribution

    private static final String[] MEDIA_COLUMNS = new String[] {
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE
    };

    private static final String[] DRM_COLUMNS = new String[] {
        DrmStore.Audio._ID,
        DrmStore.Audio.DATA,
        DrmStore.Audio.TITLE
    };

<<<<<<< HEAD
    private final Context mContext;
    private final AudioManager mAudioManager;
    private final boolean mAllowRemote;
    private final IRingtonePlayer mRemotePlayer;
    private final Binder mRemoteToken;

    private MediaPlayer mLocalPlayer;

    private Uri mUri;
    private String mTitle;

    private int mStreamType = AudioManager.STREAM_RING;

    /** {@hide} */
    public Ringtone(Context context, boolean allowRemote) {
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAllowRemote = allowRemote;
        mRemotePlayer = allowRemote ? mAudioManager.getRingtonePlayer() : null;
        mRemoteToken = allowRemote ? new Binder() : null;
=======
    private MediaPlayer mAudio;

    private Uri mUri;
    private String mTitle;
    private FileDescriptor mFileDescriptor;
    private AssetFileDescriptor mAssetFileDescriptor;

    private int mStreamType = AudioManager.STREAM_RING;

    private Context mContext;

    Ringtone(Context context) {
        mContext = context;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Sets the stream type where this ringtone will be played.
     * 
     * @param streamType The stream, see {@link AudioManager}.
     */
    public void setStreamType(int streamType) {
        mStreamType = streamType;
<<<<<<< HEAD

        // The stream type has to be set before the media player is prepared.
        // Re-initialize it.
        setUri(mUri);
=======
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Gets the stream type where this ringtone will be played.
     * 
     * @return The stream type, see {@link AudioManager}.
     */
    public int getStreamType() {
        return mStreamType;
    }

    /**
     * Returns a human-presentable title for ringtone. Looks in media and DRM
     * content providers. If not in either, uses the filename
     * 
     * @param context A context used for querying. 
     */
    public String getTitle(Context context) {
        if (mTitle != null) return mTitle;
        return mTitle = getTitle(context, mUri, true);
    }

    private static String getTitle(Context context, Uri uri, boolean followSettingsUri) {
        Cursor cursor = null;
        ContentResolver res = context.getContentResolver();
        
        String title = null;

        if (uri != null) {
            String authority = uri.getAuthority();

            if (Settings.AUTHORITY.equals(authority)) {
                if (followSettingsUri) {
                    Uri actualUri = RingtoneManager.getActualDefaultRingtoneUri(context,
                            RingtoneManager.getDefaultType(uri));
                    String actualTitle = getTitle(context, actualUri, false);
                    title = context
                            .getString(com.android.internal.R.string.ringtone_default_with_actual,
                                    actualTitle);
                }
            } else {
<<<<<<< HEAD
                try {
                    if (DrmStore.AUTHORITY.equals(authority)) {
                        cursor = res.query(uri, DRM_COLUMNS, null, null, null);
                    } else if (MediaStore.AUTHORITY.equals(authority)) {
                        cursor = res.query(uri, MEDIA_COLUMNS, null, null, null);
                    }
                } catch (SecurityException e) {
                    // missing cursor is handled below
                }

                try {
                    if (cursor != null && cursor.getCount() == 1) {
                        cursor.moveToFirst();
                        return cursor.getString(2);
                    } else {
                        title = uri.getLastPathSegment();
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
=======
                
                if (DrmStore.AUTHORITY.equals(authority)) {
                    cursor = res.query(uri, DRM_COLUMNS, null, null, null);
                } else if (MediaStore.AUTHORITY.equals(authority)) {
                    cursor = res.query(uri, MEDIA_COLUMNS, null, null, null);
                }
                
                if (cursor != null && cursor.getCount() == 1) {
                    cursor.moveToFirst();
                    return cursor.getString(2);
                } else {
                    title = uri.getLastPathSegment();
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        }

        if (title == null) {
            title = context.getString(com.android.internal.R.string.ringtone_unknown);
            
            if (title == null) {
                title = "";
            }
        }
        
        return title;
    }
<<<<<<< HEAD

    /**
     * Set {@link Uri} to be used for ringtone playback. Attempts to open
     * locally, otherwise will delegate playback to remote
     * {@link IRingtonePlayer}.
     *
     * @hide
     */
    public void setUri(Uri uri) {
        destroyLocalPlayer();

        mUri = uri;
        if (mUri == null) {
            return;
        }

        // TODO: detect READ_EXTERNAL and specific content provider case, instead of relying on throwing

        // try opening uri locally before delegating to remote player
        mLocalPlayer = new MediaPlayer();
        try {
            mLocalPlayer.setDataSource(mContext, mUri);
            mLocalPlayer.setAudioStreamType(mStreamType);
            mLocalPlayer.prepare();

        } catch (SecurityException e) {
            destroyLocalPlayer();
            if (!mAllowRemote) {
                Log.w(TAG, "Remote playback not allowed: " + e);
            }
        } catch (IOException e) {
            destroyLocalPlayer();
            if (!mAllowRemote) {
                Log.w(TAG, "Remote playback not allowed: " + e);
            }
        }

        if (LOGD) {
            if (mLocalPlayer != null) {
                Log.d(TAG, "Successfully created local player");
            } else {
                Log.d(TAG, "Problem opening; delegating to remote player");
            }
        }
    }

    /** {@hide} */
    public Uri getUri() {
        return mUri;
    }

=======
    
    private void openMediaPlayer() throws IOException {
        mAudio = new MediaPlayer();
        if (mUri != null) {
            mAudio.setDataSource(mContext, mUri);
        } else if (mFileDescriptor != null) {
            mAudio.setDataSource(mFileDescriptor);
        } else if (mAssetFileDescriptor != null) {
            mAudio.setDataSource(mAssetFileDescriptor.getFileDescriptor(),
                    mAssetFileDescriptor.getStartOffset(),
                    mAssetFileDescriptor.getLength());
        } else {
            throw new IOException("No data source set.");
        }
        mAudio.setAudioStreamType(mStreamType);
        mAudio.prepare();
    }

    void open(FileDescriptor fd) throws IOException {
        mFileDescriptor = fd;
        openMediaPlayer();
    }

    void open(AssetFileDescriptor fd) throws IOException {
        mAssetFileDescriptor = fd;
        openMediaPlayer();
    }

    void open(Uri uri) throws IOException {
        mUri = uri;
        openMediaPlayer();
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Plays the ringtone.
     */
    public void play() {
<<<<<<< HEAD
        if (mLocalPlayer != null) {
            // do not play ringtones if stream volume is 0
            // (typically because ringer mode is silent).
            if (mAudioManager.getStreamVolume(mStreamType) != 0) {
                mLocalPlayer.start();
            }
        } else if (mAllowRemote) {
            try {
                mRemotePlayer.play(mRemoteToken, mUri, mStreamType);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem playing ringtone: " + e);
            }
        } else {
            Log.w(TAG, "Neither local nor remote playback available");
=======
        if (mAudio == null) {
            try {
                openMediaPlayer();
            } catch (Exception ex) {
                Log.e(TAG, "play() caught ", ex);
                mAudio = null;
            }
        }
        if (mAudio != null) {
            mAudio.start();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Stops a playing ringtone.
     */
    public void stop() {
<<<<<<< HEAD
        if (mLocalPlayer != null) {
            destroyLocalPlayer();
        } else if (mAllowRemote) {
            try {
                mRemotePlayer.stop(mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem stopping ringtone: " + e);
            }
        }
    }

    private void destroyLocalPlayer() {
        if (mLocalPlayer != null) {
            mLocalPlayer.reset();
            mLocalPlayer.release();
            mLocalPlayer = null;
=======
        if (mAudio != null) {
            mAudio.reset();
            mAudio.release();
            mAudio = null;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
     * Whether this ringtone is currently playing.
     * 
     * @return True if playing, false otherwise.
     */
    public boolean isPlaying() {
<<<<<<< HEAD
        if (mLocalPlayer != null) {
            return mLocalPlayer.isPlaying();
        } else if (mAllowRemote) {
            try {
                return mRemotePlayer.isPlaying(mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem checking ringtone: " + e);
                return false;
            }
        } else {
            Log.w(TAG, "Neither local nor remote playback available");
            return false;
        }
=======
        return mAudio != null && mAudio.isPlaying();
>>>>>>> 54b6cfa... Initial Contribution
    }

    void setTitle(String title) {
        mTitle = title;
    }
}
