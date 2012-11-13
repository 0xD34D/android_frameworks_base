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
import com.android.internal.os.BatteryStatsImpl;
=======
import com.android.server.Watchdog;
>>>>>>> 54b6cfa... Initial Contribution

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.IApplicationThread;
import android.app.IInstrumentationWatcher;
import android.content.ComponentName;
<<<<<<< HEAD
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemClock;
import android.os.UserId;
import android.util.PrintWriterPrinter;
import android.util.TimeUtils;
=======
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
>>>>>>> 54b6cfa... Initial Contribution

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Full information about a particular process that
 * is currently running.
 */
<<<<<<< HEAD
class ProcessRecord {
    final BatteryStatsImpl.Uid.Proc batteryStats; // where to collect runtime statistics
    final ApplicationInfo info; // all about the first app in the process
    final boolean isolated;     // true if this is a special isolated process
    final int uid;              // uid of process; may be different from 'info' if isolated
    final int userId;           // user of process.
    final String processName;   // name of the process
    // List of packages running in the process
    final HashSet<String> pkgList = new HashSet<String>();
=======
class ProcessRecord implements Watchdog.PssRequestor {
    final BatteryStats.Uid.Proc batteryStats; // where to collect runtime statistics
    final ApplicationInfo info; // all about the first app in the process
    final String processName;   // name of the process
    String uniquePackage;       // Name of package if only one in the process
>>>>>>> 54b6cfa... Initial Contribution
    IApplicationThread thread;  // the actual proc...  may be null only if
                                // 'persistent' is true (in which case we
                                // are in the process of launching the app)
    int pid;                    // The process of this application; 0 if none
    boolean starting;           // True if the process is being started
<<<<<<< HEAD
    long lastActivityTime;      // For managing the LRU list
    long lruWeight;             // Weight for ordering in LRU list
=======
>>>>>>> 54b6cfa... Initial Contribution
    int maxAdj;                 // Maximum OOM adjustment for this process
    int hiddenAdj;              // If hidden, this is the adjustment to use
    int curRawAdj;              // Current OOM unlimited adjustment for this process
    int setRawAdj;              // Last set OOM unlimited adjustment for this process
<<<<<<< HEAD
    int nonStoppingAdj;         // Adjustment not counting any stopping activities
    int curAdj;                 // Current OOM adjustment for this process
    int setAdj;                 // Last set OOM adjustment for this process
    int curSchedGroup;          // Currently desired scheduling class
    int setSchedGroup;          // Last set to background scheduling class
    int trimMemoryLevel;        // Last selected memory trimming level
    int memImportance;          // Importance constant computed from curAdj
    boolean serviceb;           // Process currently is on the service B list
    boolean keeping;            // Actively running code so don't kill due to that?
    boolean setIsForeground;    // Running foreground UI when last set?
    boolean foregroundServices; // Running any services that are foreground?
    boolean foregroundActivities; // Running any activities that are foreground?
    boolean systemNoUi;         // This is a system process, but not currently showing UI.
    boolean hasShownUi;         // Has UI been shown in this process since it was started?
    boolean pendingUiClean;     // Want to clean up resources from showing UI?
    boolean hasAboveClient;     // Bound using BIND_ABOVE_CLIENT, so want to be lower
    boolean bad;                // True if disabled in the bad process list
    boolean killedBackground;   // True when proc has been killed due to too many bg
    String waitingToKill;       // Process is waiting to be killed when in the bg; reason
    IBinder forcingToForeground;// Token that is forcing this process to be foreground
    int adjSeq;                 // Sequence id for identifying oom_adj assignment cycles
    int lruSeq;                 // Sequence id for identifying LRU update cycles
    CompatibilityInfo compat;   // last used compatibility mode
    IBinder.DeathRecipient deathRecipient; // Who is watching for the death.
    ComponentName instrumentationClass;// class installed to instrument app
    ApplicationInfo instrumentationInfo; // the application being instrumented
=======
    int curAdj;                 // Current OOM adjustment for this process
    int setAdj;                 // Last set OOM adjustment for this process
    boolean isForeground;       // Is this app running the foreground UI?
    boolean setIsForeground;    // Running foreground UI when last set?
    boolean foregroundServices; // Running any services that are foreground?
    boolean bad;                // True if disabled in the bad process list
    IBinder forcingToForeground;// Token that is forcing this process to be foreground
    int adjSeq;                 // Sequence id for identifying repeated trav
    ComponentName instrumentationClass;// class installed to instrument app
>>>>>>> 54b6cfa... Initial Contribution
    String instrumentationProfileFile; // where to save profiling
    IInstrumentationWatcher instrumentationWatcher; // who is waiting
    Bundle instrumentationArguments;// as given to us
    ComponentName instrumentationResultClass;// copy of instrumentationClass
<<<<<<< HEAD
    boolean usingWrapper;       // Set to true when process was launched with a wrapper attached
    BroadcastRecord curReceiver;// receiver currently running in the app
    long lastWakeTime;          // How long proc held wake lock at last check
    long lastCpuTime;           // How long proc has run CPU at last check
    long curCpuTime;            // How long proc has run CPU most recently
    long lastRequestedGc;       // When we last asked the app to do a gc
    long lastLowMemory;         // When we last told the app that memory is low
    boolean reportLowMemory;    // Set to true when waiting to report low mem
    boolean empty;              // Is this an empty background process?
    boolean hidden;             // Is this a hidden process?
    int lastPss;                // Last pss size reported by app.
    String adjType;             // Debugging: primary thing impacting oom_adj.
    int adjTypeCode;            // Debugging: adj code to report to app.
    Object adjSource;           // Debugging: option dependent object.
    int adjSourceOom;           // Debugging: oom_adj of adjSource's process.
    Object adjTarget;           // Debugging: target component impacting oom_adj.
    
    // contains HistoryRecord objects
    final ArrayList<ActivityRecord> activities = new ArrayList<ActivityRecord>();
    // all ServiceRecord running in this process
    final HashSet<ServiceRecord> services = new HashSet<ServiceRecord>();
=======
    BroadcastRecord curReceiver;// receiver currently running in the app
    long lastRequestedGc;       // When we last asked the app to do a gc
    int lastPss;                // Last pss size reported by app.
    
    // contains HistoryRecord objects
    final ArrayList activities = new ArrayList();
    // all ServiceRecord running in this process
    final HashSet services = new HashSet();
>>>>>>> 54b6cfa... Initial Contribution
    // services that are currently executing code (need to remain foreground).
    final HashSet<ServiceRecord> executingServices
             = new HashSet<ServiceRecord>();
    // All ConnectionRecord this process holds
    final HashSet<ConnectionRecord> connections
            = new HashSet<ConnectionRecord>();  
    // all IIntentReceivers that are registered from this process.
    final HashSet<ReceiverList> receivers = new HashSet<ReceiverList>();
    // class (String) -> ContentProviderRecord
<<<<<<< HEAD
    final HashMap<String, ContentProviderRecord> pubProviders
            = new HashMap<String, ContentProviderRecord>(); 
    // All ContentProviderRecord process is using
    final ArrayList<ContentProviderConnection> conProviders
            = new ArrayList<ContentProviderConnection>();
=======
    final HashMap pubProviders = new HashMap(); 
    // All ContentProviderRecord process is using
    final HashSet conProviders = new HashSet(); 
>>>>>>> 54b6cfa... Initial Contribution
    
    boolean persistent;         // always keep this application running?
    boolean crashing;           // are we in the process of crashing?
    Dialog crashDialog;         // dialog being displayed due to crash.
    boolean notResponding;      // does the app have a not responding dialog?
    Dialog anrDialog;           // dialog being displayed due to app not resp.
    boolean removed;            // has app package been removed from device?
    boolean debugging;          // was app launched for debugging?
<<<<<<< HEAD
    boolean waitedForDebugger;  // has process show wait for debugger dialog?
    Dialog waitDialog;          // current wait for debugger dialog
    
    String shortStringName;     // caching of toShortString() result.
    String stringName;          // caching of toString() result.
    
=======
    int persistentActivities;   // number of activities that are persistent
    boolean waitedForDebugger;  // has process show wait for debugger dialog?
    Dialog waitDialog;          // current wait for debugger dialog
    
>>>>>>> 54b6cfa... Initial Contribution
    // These reports are generated & stored when an app gets into an error condition.
    // They will be "null" when all is OK.
    ActivityManager.ProcessErrorStateInfo crashingReport;
    ActivityManager.ProcessErrorStateInfo notRespondingReport;

<<<<<<< HEAD
    // Who will be notified of the error. This is usually an activity in the
    // app that installed the package.
    ComponentName errorReportReceiver;

    void dump(PrintWriter pw, String prefix) {
        final long now = SystemClock.uptimeMillis();

        pw.print(prefix); pw.print("user #"); pw.print(userId);
                pw.print(" uid="); pw.print(info.uid);
        if (uid != info.uid) {
            pw.print(" ISOLATED uid="); pw.print(uid);
        }
        pw.println();
        if (info.className != null) {
            pw.print(prefix); pw.print("class="); pw.println(info.className);
        }
        if (info.manageSpaceActivityName != null) {
            pw.print(prefix); pw.print("manageSpaceActivityName=");
            pw.println(info.manageSpaceActivityName);
        }
        pw.print(prefix); pw.print("dir="); pw.print(info.sourceDir);
                pw.print(" publicDir="); pw.print(info.publicSourceDir);
                pw.print(" data="); pw.println(info.dataDir);
        pw.print(prefix); pw.print("packageList="); pw.println(pkgList);
        pw.print(prefix); pw.print("compat="); pw.println(compat);
        if (instrumentationClass != null || instrumentationProfileFile != null
                || instrumentationArguments != null) {
            pw.print(prefix); pw.print("instrumentationClass=");
                    pw.print(instrumentationClass);
                    pw.print(" instrumentationProfileFile=");
                    pw.println(instrumentationProfileFile);
            pw.print(prefix); pw.print("instrumentationArguments=");
                    pw.println(instrumentationArguments);
            pw.print(prefix); pw.print("instrumentationInfo=");
                    pw.println(instrumentationInfo);
            if (instrumentationInfo != null) {
                instrumentationInfo.dump(new PrintWriterPrinter(pw), prefix + "  ");
            }
        }
        pw.print(prefix); pw.print("thread="); pw.print(thread);
                pw.print(" curReceiver="); pw.println(curReceiver);
        pw.print(prefix); pw.print("pid="); pw.print(pid); pw.print(" starting=");
                pw.print(starting); pw.print(" lastPss="); pw.println(lastPss);
        pw.print(prefix); pw.print("lastActivityTime=");
                TimeUtils.formatDuration(lastActivityTime, now, pw);
                pw.print(" lruWeight="); pw.print(lruWeight);
                pw.print(" serviceb="); pw.print(serviceb);
                pw.print(" keeping="); pw.print(keeping);
                pw.print(" hidden="); pw.print(hidden);
                pw.print(" empty="); pw.println(empty);
        pw.print(prefix); pw.print("oom: max="); pw.print(maxAdj);
                pw.print(" hidden="); pw.print(hiddenAdj);
                pw.print(" curRaw="); pw.print(curRawAdj);
                pw.print(" setRaw="); pw.print(setRawAdj);
                pw.print(" nonStopping="); pw.print(nonStoppingAdj);
                pw.print(" cur="); pw.print(curAdj);
                pw.print(" set="); pw.println(setAdj);
        pw.print(prefix); pw.print("curSchedGroup="); pw.print(curSchedGroup);
                pw.print(" setSchedGroup="); pw.print(setSchedGroup);
                pw.print(" systemNoUi="); pw.print(systemNoUi);
                pw.print(" trimMemoryLevel="); pw.println(trimMemoryLevel);
        pw.print(prefix); pw.print("hasShownUi="); pw.print(hasShownUi);
                pw.print(" pendingUiClean="); pw.print(pendingUiClean);
                pw.print(" hasAboveClient="); pw.println(hasAboveClient);
        pw.print(prefix); pw.print("setIsForeground="); pw.print(setIsForeground);
                pw.print(" foregroundServices="); pw.print(foregroundServices);
                pw.print(" forcingToForeground="); pw.println(forcingToForeground);
        pw.print(prefix); pw.print("persistent="); pw.print(persistent);
                pw.print(" removed="); pw.println(removed);
        pw.print(prefix); pw.print("adjSeq="); pw.print(adjSeq);
                pw.print(" lruSeq="); pw.println(lruSeq);
        if (!keeping) {
            long wtime;
            synchronized (batteryStats.getBatteryStats()) {
                wtime = batteryStats.getBatteryStats().getProcessWakeTime(info.uid,
                        pid, SystemClock.elapsedRealtime());
            }
            long timeUsed = wtime - lastWakeTime;
            pw.print(prefix); pw.print("lastWakeTime="); pw.print(lastWakeTime);
                    pw.print(" time used=");
                    TimeUtils.formatDuration(timeUsed, pw); pw.println("");
            pw.print(prefix); pw.print("lastCpuTime="); pw.print(lastCpuTime);
                    pw.print(" time used=");
                    TimeUtils.formatDuration(curCpuTime-lastCpuTime, pw); pw.println("");
        }
        pw.print(prefix); pw.print("lastRequestedGc=");
                TimeUtils.formatDuration(lastRequestedGc, now, pw);
                pw.print(" lastLowMemory=");
                TimeUtils.formatDuration(lastLowMemory, now, pw);
                pw.print(" reportLowMemory="); pw.println(reportLowMemory);
        if (killedBackground || waitingToKill != null) {
            pw.print(prefix); pw.print("killedBackground="); pw.print(killedBackground);
                    pw.print(" waitingToKill="); pw.println(waitingToKill);
        }
        if (debugging || crashing || crashDialog != null || notResponding
                || anrDialog != null || bad) {
            pw.print(prefix); pw.print("debugging="); pw.print(debugging);
                    pw.print(" crashing="); pw.print(crashing);
                    pw.print(" "); pw.print(crashDialog);
                    pw.print(" notResponding="); pw.print(notResponding);
                    pw.print(" " ); pw.print(anrDialog);
                    pw.print(" bad="); pw.print(bad);

                    // crashing or notResponding is always set before errorReportReceiver
                    if (errorReportReceiver != null) {
                        pw.print(" errorReportReceiver=");
                        pw.print(errorReportReceiver.flattenToShortString());
                    }
                    pw.println();
        }
        if (activities.size() > 0) {
            pw.print(prefix); pw.println("Activities:");
            for (int i=0; i<activities.size(); i++) {
                pw.print(prefix); pw.print("  - "); pw.println(activities.get(i));
            }
        }
        if (services.size() > 0) {
            pw.print(prefix); pw.println("Services:");
            for (ServiceRecord sr : services) {
                pw.print(prefix); pw.print("  - "); pw.println(sr);
            }
        }
        if (executingServices.size() > 0) {
            pw.print(prefix); pw.println("Executing Services:");
            for (ServiceRecord sr : executingServices) {
                pw.print(prefix); pw.print("  - "); pw.println(sr);
            }
        }
        if (connections.size() > 0) {
            pw.print(prefix); pw.println("Connections:");
            for (ConnectionRecord cr : connections) {
                pw.print(prefix); pw.print("  - "); pw.println(cr);
            }
        }
        if (pubProviders.size() > 0) {
            pw.print(prefix); pw.println("Published Providers:");
            for (HashMap.Entry<String, ContentProviderRecord> ent : pubProviders.entrySet()) {
                pw.print(prefix); pw.print("  - "); pw.println(ent.getKey());
                pw.print(prefix); pw.print("    -> "); pw.println(ent.getValue());
            }
        }
        if (conProviders.size() > 0) {
            pw.print(prefix); pw.println("Connected Providers:");
            for (int i=0; i<conProviders.size(); i++) {
                pw.print(prefix); pw.print("  - "); pw.println(conProviders.get(i).toShortString());
            }
        }
        if (receivers.size() > 0) {
            pw.print(prefix); pw.println("Receivers:");
            for (ReceiverList rl : receivers) {
                pw.print(prefix); pw.print("  - "); pw.println(rl);
            }
        }
    }
    
    ProcessRecord(BatteryStatsImpl.Uid.Proc _batteryStats, IApplicationThread _thread,
            ApplicationInfo _info, String _processName, int _uid) {
        batteryStats = _batteryStats;
        info = _info;
        isolated = _info.uid != _uid;
        uid = _uid;
        userId = UserId.getUserId(_uid);
        processName = _processName;
        pkgList.add(_info.packageName);
        thread = _thread;
        maxAdj = ProcessList.HIDDEN_APP_MAX_ADJ;
        hiddenAdj = ProcessList.HIDDEN_APP_MIN_ADJ;
=======
    void dump(PrintWriter pw, String prefix) {
        pw.println(prefix + this);
        pw.println(prefix + "class=" + info.className);
        pw.println(prefix+"manageSpaceActivityName="+info.manageSpaceActivityName);
        pw.println(prefix + "dir=" + info.sourceDir + " publicDir=" + info.publicSourceDir 
              + " data=" + info.dataDir);
        pw.println(prefix + "uniquePackage=" + uniquePackage);
        pw.println(prefix + "instrumentationClass=" + instrumentationClass
              + " instrumentationProfileFile=" + instrumentationProfileFile);
        pw.println(prefix + "instrumentationArguments=" + instrumentationArguments);
        pw.println(prefix + "thread=" + thread + " curReceiver=" + curReceiver);
        pw.println(prefix + "pid=" + pid + " starting=" + starting
                + " lastPss=" + lastPss);
        pw.println(prefix + "maxAdj=" + maxAdj + " hiddenAdj=" + hiddenAdj
                + " curRawAdj=" + curRawAdj + " setRawAdj=" + setRawAdj
                + " curAdj=" + curAdj + " setAdj=" + setAdj);
        pw.println(prefix + "isForeground=" + isForeground
                + " setIsForeground=" + setIsForeground
                + " foregroundServices=" + foregroundServices
                + " forcingToForeground=" + forcingToForeground);
        pw.println(prefix + "persistent=" + persistent + " removed=" + removed
                + " persistentActivities=" + persistentActivities);
        pw.println(prefix + "debugging=" + debugging
                + " crashing=" + crashing + " " + crashDialog
                + " notResponding=" + notResponding + " " + anrDialog
                + " bad=" + bad);
        pw.println(prefix + "activities=" + activities);
        pw.println(prefix + "services=" + services);
        pw.println(prefix + "executingServices=" + executingServices);
        pw.println(prefix + "connections=" + connections);
        pw.println(prefix + "pubProviders=" + pubProviders);
        pw.println(prefix + "conProviders=" + conProviders);
        pw.println(prefix + "receivers=" + receivers);
    }
    
    ProcessRecord(BatteryStats.Uid.Proc _batteryStats, IApplicationThread _thread,
            ApplicationInfo _info, String _processName) {
        batteryStats = _batteryStats;
        info = _info;
        processName = _processName;
        uniquePackage = _info.packageName;
        thread = _thread;
        maxAdj = ActivityManagerService.EMPTY_APP_ADJ;
        hiddenAdj = ActivityManagerService.HIDDEN_APP_MIN_ADJ;
>>>>>>> 54b6cfa... Initial Contribution
        curRawAdj = setRawAdj = -100;
        curAdj = setAdj = -100;
        persistent = false;
        removed = false;
<<<<<<< HEAD
    }

    public void setPid(int _pid) {
        pid = _pid;
        shortStringName = null;
        stringName = null;
    }
    
=======
        persistentActivities = 0;
    }

>>>>>>> 54b6cfa... Initial Contribution
    /**
     * This method returns true if any of the activities within the process record are interesting
     * to the user. See HistoryRecord.isInterestingToUserLocked()
     */
    public boolean isInterestingToUserLocked() {
        final int size = activities.size();
        for (int i = 0 ; i < size ; i++) {
<<<<<<< HEAD
            ActivityRecord r = activities.get(i);
=======
            HistoryRecord r = (HistoryRecord) activities.get(i);
>>>>>>> 54b6cfa... Initial Contribution
            if (r.isInterestingToUserLocked()) {
                return true;
            }
        }
        return false;
    }
    
    public void stopFreezingAllLocked() {
        int i = activities.size();
        while (i > 0) {
            i--;
<<<<<<< HEAD
            activities.get(i).stopFreezingScreenLocked(true);
        }
    }
    
    public void unlinkDeathRecipient() {
        if (deathRecipient != null && thread != null) {
            thread.asBinder().unlinkToDeath(deathRecipient, 0);
        }
        deathRecipient = null;
    }

    void updateHasAboveClientLocked() {
        hasAboveClient = false;
        if (connections.size() > 0) {
            for (ConnectionRecord cr : connections) {
                if ((cr.flags&Context.BIND_ABOVE_CLIENT) != 0) {
                    hasAboveClient = true;
                    break;
                }
            }
        }
    }

    public String toShortString() {
        if (shortStringName != null) {
            return shortStringName;
        }
        StringBuilder sb = new StringBuilder(128);
        toShortString(sb);
        return shortStringName = sb.toString();
    }
    
    void toShortString(StringBuilder sb) {
        sb.append(pid);
        sb.append(':');
        sb.append(processName);
        sb.append('/');
        if (info.uid < Process.FIRST_APPLICATION_UID) {
            sb.append(uid);
        } else {
            sb.append('u');
            sb.append(userId);
            sb.append('a');
            sb.append(info.uid%Process.FIRST_APPLICATION_UID);
            if (uid != info.uid) {
                sb.append('i');
                sb.append(UserId.getAppId(uid) - Process.FIRST_ISOLATED_UID);
=======
            ((HistoryRecord)activities.get(i)).stopFreezingScreenLocked(true);
        }
    }
    
    public void requestPss() {
        IApplicationThread localThread = thread;
        if (localThread != null) {
            try {
                localThread.requestPss();
            } catch (RemoteException e) {
>>>>>>> 54b6cfa... Initial Contribution
            }
        }
    }
    
    public String toString() {
<<<<<<< HEAD
        if (stringName != null) {
            return stringName;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append("ProcessRecord{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        toShortString(sb);
        sb.append('}');
        return stringName = sb.toString();
    }
    
    /*
     *  Return true if package has been added false if not
     */
    public boolean addPackage(String pkg) {
        if (!pkgList.contains(pkg)) {
            pkgList.add(pkg);
            return true;
        }
        return false;
    }
    
    /*
     *  Delete all packages from list except the package indicated in info
     */
    public void resetPackageList() {
        pkgList.clear();
        pkgList.add(info.packageName);
    }
    
    public String[] getPackageList() {
        int size = pkgList.size();
        if (size == 0) {
            return null;
        }
        String list[] = new String[size];
        pkgList.toArray(list);
        return list;
=======
        return "ProcessRecord{"
            + Integer.toHexString(System.identityHashCode(this))
            + " " + pid + ":" + processName + "/" + info.uid + "}";
>>>>>>> 54b6cfa... Initial Contribution
    }
}
