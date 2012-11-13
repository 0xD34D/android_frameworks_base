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
import android.content.IIntentReceiver;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.PrintWriterPrinter;
import android.util.Printer;
=======
import android.app.IIntentReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
>>>>>>> 54b6cfa... Initial Contribution

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A receiver object that has registered for one or more broadcasts.
 * The ArrayList holds BroadcastFilter objects.
 */
class ReceiverList extends ArrayList<BroadcastFilter>
        implements IBinder.DeathRecipient {
    final ActivityManagerService owner;
    public final IIntentReceiver receiver;
    public final ProcessRecord app;
    public final int pid;
    public final int uid;
    BroadcastRecord curBroadcast = null;
    boolean linkedToDeath = false;

<<<<<<< HEAD
    String stringName;
    
=======
>>>>>>> 54b6cfa... Initial Contribution
    ReceiverList(ActivityManagerService _owner, ProcessRecord _app,
            int _pid, int _uid, IIntentReceiver _receiver) {
        owner = _owner;
        receiver = _receiver;
        app = _app;
        pid = _pid;
        uid = _uid;
    }

    // Want object identity, not the array identity we are inheriting.
    public boolean equals(Object o) {
        return this == o;
    }
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    public void binderDied() {
        linkedToDeath = false;
        owner.unregisterReceiver(receiver);
    }
    
    void dumpLocal(PrintWriter pw, String prefix) {
<<<<<<< HEAD
        pw.print(prefix); pw.print("app="); pw.print(app);
            pw.print(" pid="); pw.print(pid); pw.print(" uid="); pw.println(uid);
        if (curBroadcast != null || linkedToDeath) {
            pw.print(prefix); pw.print("curBroadcast="); pw.print(curBroadcast);
                pw.print(" linkedToDeath="); pw.println(linkedToDeath);
        }
    }
    
    void dump(PrintWriter pw, String prefix) {
        Printer pr = new PrintWriterPrinter(pw);
=======
        pw.println(prefix + "receiver=IBinder "
                + Integer.toHexString(System.identityHashCode(receiver.asBinder())));
        pw.println(prefix + "app=" + app + " pid=" + pid + " uid=" + uid);
        pw.println(prefix + "curBroadcast=" + curBroadcast
                + " linkedToDeath=" + linkedToDeath);
    }
    
    void dump(PrintWriter pw, String prefix) {
        pw.println(prefix + this);
>>>>>>> 54b6cfa... Initial Contribution
        dumpLocal(pw, prefix);
        String p2 = prefix + "  ";
        final int N = size();
        for (int i=0; i<N; i++) {
            BroadcastFilter bf = get(i);
<<<<<<< HEAD
            pw.print(prefix); pw.print("Filter #"); pw.print(i);
                    pw.print(": BroadcastFilter{");
                    pw.print(Integer.toHexString(System.identityHashCode(bf)));
                    pw.println('}');
            bf.dumpInReceiverList(pw, pr, p2);
=======
            pw.println(prefix + "Filter #" + i + ": " + bf);
            bf.dump(pw, p2);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }
    
    public String toString() {
<<<<<<< HEAD
        if (stringName != null) {
            return stringName;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("ReceiverList{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        sb.append(pid);
        sb.append(' ');
        sb.append((app != null ? app.processName : "(unknown name)"));
        sb.append('/');
        sb.append(uid);
        sb.append((receiver.asBinder() instanceof Binder) ? " local:" : " remote:");
        sb.append(Integer.toHexString(System.identityHashCode(receiver.asBinder())));
        sb.append('}');
        return stringName = sb.toString();
=======
        return "ReceiverList{"
            + Integer.toHexString(System.identityHashCode(this))
            + " " + pid + " " + (app != null ? app.processName : "(unknown name)")
            + "/" + uid + " client "
            + Integer.toHexString(System.identityHashCode(receiver.asBinder()))
            + "}";
>>>>>>> 54b6cfa... Initial Contribution
    }
}
