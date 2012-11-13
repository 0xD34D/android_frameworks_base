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

package com.android.server.am;

<<<<<<< HEAD
import android.app.ActivityManager;
import android.content.IIntentSender;
import android.content.IIntentReceiver;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserId;
import android.util.Slog;
=======
import android.app.IActivityManager;
import android.app.IIntentSender;
import android.app.IIntentReceiver;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
>>>>>>> 54b6cfa... Initial Contribution

import java.io.PrintWriter;
import java.lang.ref.WeakReference;

class PendingIntentRecord extends IIntentSender.Stub {
    final ActivityManagerService owner;
    final Key key;
    final int uid;
    final WeakReference<PendingIntentRecord> ref;
    boolean sent = false;
    boolean canceled = false;

<<<<<<< HEAD
    String stringName;
    
    final static class Key {
        final int type;
        final String packageName;
        final ActivityRecord activity;
=======
    final static class Key {
        final int type;
        final String packageName;
        final HistoryRecord activity;
>>>>>>> 54b6cfa... Initial Contribution
        final String who;
        final int requestCode;
        final Intent requestIntent;
        final String requestResolvedType;
<<<<<<< HEAD
        final Bundle options;
        Intent[] allIntents;
        String[] allResolvedTypes;
=======
>>>>>>> 54b6cfa... Initial Contribution
        final int flags;
        final int hashCode;
        
        private static final int ODD_PRIME_NUMBER = 37;
        
<<<<<<< HEAD
        Key(int _t, String _p, ActivityRecord _a, String _w,
                int _r, Intent[] _i, String[] _it, int _f, Bundle _o) {
=======
        Key(int _t, String _p, HistoryRecord _a, String _w,
                int _r, Intent _i, String _it, int _f) {
>>>>>>> 54b6cfa... Initial Contribution
            type = _t;
            packageName = _p;
            activity = _a;
            who = _w;
            requestCode = _r;
<<<<<<< HEAD
            requestIntent = _i != null ? _i[_i.length-1] : null;
            requestResolvedType = _it != null ? _it[_it.length-1] : null;
            allIntents = _i;
            allResolvedTypes = _it;
            flags = _f;
            options = _o;
=======
            requestIntent = _i;
            requestResolvedType = _it;
            flags = _f;
>>>>>>> 54b6cfa... Initial Contribution
            
            int hash = 23;
            hash = (ODD_PRIME_NUMBER*hash) + _f;
            hash = (ODD_PRIME_NUMBER*hash) + _r;
            if (_w != null) {
                hash = (ODD_PRIME_NUMBER*hash) + _w.hashCode();
            }
            if (_a != null) {
                hash = (ODD_PRIME_NUMBER*hash) + _a.hashCode();
            }
<<<<<<< HEAD
            if (requestIntent != null) {
                hash = (ODD_PRIME_NUMBER*hash) + requestIntent.filterHashCode();
            }
            if (requestResolvedType != null) {
                hash = (ODD_PRIME_NUMBER*hash) + requestResolvedType.hashCode();
=======
            if (_i != null) {
                hash = (ODD_PRIME_NUMBER*hash) + _i.filterHashCode();
            }
            if (_it != null) {
                hash = (ODD_PRIME_NUMBER*hash) + _it.hashCode();
>>>>>>> 54b6cfa... Initial Contribution
            }
            hash = (ODD_PRIME_NUMBER*hash) + _p.hashCode();
            hash = (ODD_PRIME_NUMBER*hash) + _t;
            hashCode = hash;
<<<<<<< HEAD
            //Slog.i(ActivityManagerService.TAG, this + " hashCode=0x"
=======
            //Log.i(ActivityManagerService.TAG, this + " hashCode=0x"
>>>>>>> 54b6cfa... Initial Contribution
            //        + Integer.toHexString(hashCode));
        }
        
        public boolean equals(Object otherObj) {
            if (otherObj == null) {
                return false;
            }
            try {
                Key other = (Key)otherObj;
                if (type != other.type) {
                    return false;
                }
                if (!packageName.equals(other.packageName)) {
                    return false;
                }
                if (activity != other.activity) {
                    return false;
                }
                if (who != other.who) {
                    if (who != null) {
                        if (!who.equals(other.who)) {
                            return false;
                        }
                    } else if (other.who != null) {
                        return false;
                    }
                }
                if (requestCode != other.requestCode) {
                    return false;
                }
                if (requestIntent != other.requestIntent) {
                    if (requestIntent != null) {
                        if (!requestIntent.filterEquals(other.requestIntent)) {
                            return false;
                        }
                    } else if (other.requestIntent != null) {
                        return false;
                    }
                }
                if (requestResolvedType != other.requestResolvedType) {
                    if (requestResolvedType != null) {
                        if (!requestResolvedType.equals(other.requestResolvedType)) {
                            return false;
                        }
                    } else if (other.requestResolvedType != null) {
                        return false;
                    }
                }
                if (flags != other.flags) {
                    return false;
                }
                return true;
            } catch (ClassCastException e) {
            }
            return false;
        }

        public int hashCode() {
            return hashCode;
        }
        
        public String toString() {
            return "Key{" + typeName() + " pkg=" + packageName
<<<<<<< HEAD
                + " intent="
                + (requestIntent != null
                        ? requestIntent.toShortString(false, true, false, false) : "<null>")
                + " flags=0x" + Integer.toHexString(flags) + "}";
=======
                + " intent=" + requestIntent + " flags=0x"
                + Integer.toHexString(flags) + "}";
>>>>>>> 54b6cfa... Initial Contribution
        }
        
        String typeName() {
            switch (type) {
<<<<<<< HEAD
                case ActivityManager.INTENT_SENDER_ACTIVITY:
                    return "startActivity";
                case ActivityManager.INTENT_SENDER_BROADCAST:
                    return "broadcastIntent";
                case ActivityManager.INTENT_SENDER_SERVICE:
                    return "startService";
                case ActivityManager.INTENT_SENDER_ACTIVITY_RESULT:
=======
                case IActivityManager.INTENT_SENDER_ACTIVITY:
                    return "startActivity";
                case IActivityManager.INTENT_SENDER_BROADCAST:
                    return "broadcastIntent";
                case IActivityManager.INTENT_SENDER_SERVICE:
                    return "startService";
                case IActivityManager.INTENT_SENDER_ACTIVITY_RESULT:
>>>>>>> 54b6cfa... Initial Contribution
                    return "activityResult";
            }
            return Integer.toString(type);
        }
    }
    
    PendingIntentRecord(ActivityManagerService _owner, Key _k, int _u) {
        owner = _owner;
        key = _k;
        uid = _u;
        ref = new WeakReference<PendingIntentRecord>(this);
    }

    public int send(int code, Intent intent, String resolvedType,
<<<<<<< HEAD
            IIntentReceiver finishedReceiver, String requiredPermission) {
        return sendInner(code, intent, resolvedType, finishedReceiver,
                requiredPermission, null, null, 0, 0, 0, null);
    }
    
    int sendInner(int code, Intent intent, String resolvedType,
            IIntentReceiver finishedReceiver, String requiredPermission,
            IBinder resultTo, String resultWho, int requestCode,
            int flagsMask, int flagsValues, Bundle options) {
=======
            IIntentReceiver finishedReceiver) {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized(owner) {
            if (!canceled) {
                sent = true;
                if ((key.flags&PendingIntent.FLAG_ONE_SHOT) != 0) {
                    owner.cancelIntentSenderLocked(this, true);
                    canceled = true;
                }
                Intent finalIntent = key.requestIntent != null
                        ? new Intent(key.requestIntent) : new Intent();
                if (intent != null) {
                    int changes = finalIntent.fillIn(intent, key.flags);
                    if ((changes&Intent.FILL_IN_DATA) == 0) {
                        resolvedType = key.requestResolvedType;
                    }
                } else {
                    resolvedType = key.requestResolvedType;
                }
<<<<<<< HEAD
                flagsMask &= ~Intent.IMMUTABLE_FLAGS;
                flagsValues &= flagsMask;
                finalIntent.setFlags((finalIntent.getFlags()&~flagsMask) | flagsValues);
=======
>>>>>>> 54b6cfa... Initial Contribution
                
                final long origId = Binder.clearCallingIdentity();
                
                boolean sendFinish = finishedReceiver != null;
                switch (key.type) {
<<<<<<< HEAD
                    case ActivityManager.INTENT_SENDER_ACTIVITY:
                        if (options == null) {
                            options = key.options;
                        } else if (key.options != null) {
                            Bundle opts = new Bundle(key.options);
                            opts.putAll(options);
                            options = opts;
                        }
                        try {
                            if (key.allIntents != null && key.allIntents.length > 1) {
                                Intent[] allIntents = new Intent[key.allIntents.length];
                                String[] allResolvedTypes = new String[key.allIntents.length];
                                System.arraycopy(key.allIntents, 0, allIntents, 0,
                                        key.allIntents.length);
                                if (key.allResolvedTypes != null) {
                                    System.arraycopy(key.allResolvedTypes, 0, allResolvedTypes, 0,
                                            key.allResolvedTypes.length);
                                }
                                allIntents[allIntents.length-1] = finalIntent;
                                allResolvedTypes[allResolvedTypes.length-1] = resolvedType;
                                owner.startActivitiesInPackage(uid, allIntents,
                                        allResolvedTypes, resultTo, options);
                            } else {
                                owner.startActivityInPackage(uid,
                                        finalIntent, resolvedType,
                                        resultTo, resultWho, requestCode, 0, options);
                            }
                        } catch (RuntimeException e) {
                            Slog.w(ActivityManagerService.TAG,
                                    "Unable to send startActivity intent", e);
                        }
                        break;
                    case ActivityManager.INTENT_SENDER_ACTIVITY_RESULT:
                        key.activity.stack.sendActivityResultLocked(-1, key.activity,
                                key.who, key.requestCode, code, finalIntent);
                        break;
                    case ActivityManager.INTENT_SENDER_BROADCAST:
                        try {
                            // If a completion callback has been requested, require
                            // that the broadcast be delivered synchronously
                            owner.broadcastIntentInPackage(key.packageName, uid,
                                    finalIntent, resolvedType,
                                    finishedReceiver, code, null, null,
                                requiredPermission, (finishedReceiver != null), false, UserId
                                        .getUserId(uid));
                            sendFinish = false;
                        } catch (RuntimeException e) {
                            Slog.w(ActivityManagerService.TAG,
                                    "Unable to send startActivity intent", e);
                        }
                        break;
                    case ActivityManager.INTENT_SENDER_SERVICE:
=======
                    case IActivityManager.INTENT_SENDER_ACTIVITY:
                        try {
                            owner.startActivityInPackage(uid,
                                    finalIntent, resolvedType,
                                    null, null, 0, false);
                        } catch (RuntimeException e) {
                            Log.w(ActivityManagerService.TAG,
                                    "Unable to send startActivity intent", e);
                        }
                        break;
                    case IActivityManager.INTENT_SENDER_ACTIVITY_RESULT:
                        owner.sendActivityResultLocked(-1, key.activity,
                                key.who, key.requestCode, code, finalIntent);
                        break;
                    case IActivityManager.INTENT_SENDER_BROADCAST:
                        try {
                            owner.broadcastIntentInPackage(key.packageName, uid,
                                    finalIntent, resolvedType,
                                    finishedReceiver, code, null, null, null,
                                    false, false);
                            sendFinish = false;
                        } catch (RuntimeException e) {
                            Log.w(ActivityManagerService.TAG,
                                    "Unable to send startActivity intent", e);
                        }
                        break;
                    case IActivityManager.INTENT_SENDER_SERVICE:
>>>>>>> 54b6cfa... Initial Contribution
                        try {
                            owner.startServiceInPackage(uid,
                                    finalIntent, resolvedType);
                        } catch (RuntimeException e) {
<<<<<<< HEAD
                            Slog.w(ActivityManagerService.TAG,
=======
                            Log.w(ActivityManagerService.TAG,
>>>>>>> 54b6cfa... Initial Contribution
                                    "Unable to send startService intent", e);
                        }
                        break;
                }
                
                if (sendFinish) {
                    try {
                        finishedReceiver.performReceive(new Intent(finalIntent), 0,
<<<<<<< HEAD
                                null, null, false, false);
=======
                                null, null, false);
>>>>>>> 54b6cfa... Initial Contribution
                    } catch (RemoteException e) {
                    }
                }
                
                Binder.restoreCallingIdentity(origId);
                
                return 0;
            }
        }
<<<<<<< HEAD
        return ActivityManager.START_CANCELED;
    }
    
    protected void finalize() throws Throwable {
        try {
            if (!canceled) {
                owner.mHandler.sendMessage(owner.mHandler.obtainMessage(
                        ActivityManagerService.FINALIZE_PENDING_INTENT_MSG, this));
            }
        } finally {
            super.finalize();
        }
    }

    public void completeFinalize() {
        synchronized(owner) {
            WeakReference<PendingIntentRecord> current =
                    owner.mIntentSenderRecords.get(key);
            if (current == ref) {
                owner.mIntentSenderRecords.remove(key);
            }
        }
    }
    
    void dump(PrintWriter pw, String prefix) {
        pw.print(prefix); pw.print("uid="); pw.print(uid);
                pw.print(" packageName="); pw.print(key.packageName);
                pw.print(" type="); pw.print(key.typeName());
                pw.print(" flags=0x"); pw.println(Integer.toHexString(key.flags));
        if (key.activity != null || key.who != null) {
            pw.print(prefix); pw.print("activity="); pw.print(key.activity);
                    pw.print(" who="); pw.println(key.who);
        }
        if (key.requestCode != 0 || key.requestResolvedType != null) {
            pw.print(prefix); pw.print("requestCode="); pw.print(key.requestCode);
                    pw.print(" requestResolvedType="); pw.println(key.requestResolvedType);
        }
        if (key.requestIntent != null) {
            pw.print(prefix); pw.print("requestIntent=");
                    pw.println(key.requestIntent.toShortString(false, true, true, true));
        }
        if (sent || canceled) {
            pw.print(prefix); pw.print("sent="); pw.print(sent);
                    pw.print(" canceled="); pw.println(canceled);
        }
    }

    public String toString() {
        if (stringName != null) {
            return stringName;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("PendingIntentRecord{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        sb.append(key.packageName);
        sb.append(' ');
        sb.append(key.typeName());
        sb.append('}');
        return stringName = sb.toString();
=======
        return -1;
    }
    
    protected void finalize() throws Throwable {
        if (!canceled) {
            synchronized(owner) {
                WeakReference<PendingIntentRecord> current =
                        owner.mIntentSenderRecords.get(key);
                if (current == ref) {
                    owner.mIntentSenderRecords.remove(key);
                }
            }
        }
    }

    void dump(PrintWriter pw, String prefix) {
        pw.println(prefix + "packageName=" + key.packageName
                + " type=" + key.typeName()
                + " flags=0x" + Integer.toHexString(key.flags));
        pw.println(prefix + "activity=" + key.activity + " who=" + key.who);
        pw.println(prefix + "requestCode=" + key.requestCode
                + " requestResolvedType=" + key.requestResolvedType);
        pw.println(prefix + "requestIntent=" + key.requestIntent);
        pw.println(prefix + "sent=" + sent + " canceled=" + canceled);
    }

    public String toString() {
        return "IntentSenderRecord{"
            + Integer.toHexString(System.identityHashCode(this))
            + " " + key.packageName + " " + key.typeName() + "}";
>>>>>>> 54b6cfa... Initial Contribution
    }
}
