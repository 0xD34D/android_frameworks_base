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

import android.app.IServiceConnection;
<<<<<<< HEAD
import android.app.PendingIntent;
=======
>>>>>>> 54b6cfa... Initial Contribution

import java.io.PrintWriter;

/**
 * Description of a single binding to a service.
 */
class ConnectionRecord {
    final AppBindRecord binding;    // The application/service binding.
<<<<<<< HEAD
    final ActivityRecord activity;   // If non-null, the owning activity.
    final IServiceConnection conn;  // The client connection.
    final int flags;                // Binding options.
    final int clientLabel;          // String resource labeling this client.
    final PendingIntent clientIntent; // How to launch the client.
    String stringName;              // Caching of toString.
    boolean serviceDead;            // Well is it?
    
    void dump(PrintWriter pw, String prefix) {
        pw.println(prefix + "binding=" + binding);
        if (activity != null) {
            pw.println(prefix + "activity=" + activity);
        }
=======
    final HistoryRecord activity;   // If non-null, the owning activity.
    final IServiceConnection conn;  // The client connection.
    final int flags;                // Binding options.

    void dump(PrintWriter pw, String prefix) {
        pw.println(prefix + this);
        pw.println(prefix + "binding=" + binding);
        pw.println(prefix + "activity=" + activity);
>>>>>>> 54b6cfa... Initial Contribution
        pw.println(prefix + "conn=" + conn.asBinder()
                + " flags=0x" + Integer.toHexString(flags));
    }
    
<<<<<<< HEAD
    ConnectionRecord(AppBindRecord _binding, ActivityRecord _activity,
               IServiceConnection _conn, int _flags,
               int _clientLabel, PendingIntent _clientIntent) {
=======
    ConnectionRecord(AppBindRecord _binding, HistoryRecord _activity,
               IServiceConnection _conn, int _flags) {
>>>>>>> 54b6cfa... Initial Contribution
        binding = _binding;
        activity = _activity;
        conn = _conn;
        flags = _flags;
<<<<<<< HEAD
        clientLabel = _clientLabel;
        clientIntent = _clientIntent;
    }

    public String toString() {
        if (stringName != null) {
            return stringName;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("ConnectionRecord{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        if (serviceDead) {
            sb.append("DEAD ");
        }
        sb.append(binding.service.shortName);
        sb.append(":@");
        sb.append(Integer.toHexString(System.identityHashCode(conn.asBinder())));
        sb.append('}');
        return stringName = sb.toString();
=======
    }

    public String toString() {
        return "ConnectionRecord{"
            + Integer.toHexString(System.identityHashCode(this))
            + " " + binding.service.shortName
            + ":@" + Integer.toHexString(System.identityHashCode(conn.asBinder())) + "}";
>>>>>>> 54b6cfa... Initial Contribution
    }
}
