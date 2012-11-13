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

package com.android.internal.telephony.gsm;

<<<<<<< HEAD
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.CellLocation;
import android.telephony.PhoneNumberUtils;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import com.android.internal.telephony.CallTracker;
import android.text.TextUtils;
import android.util.Log;

import static com.android.internal.telephony.CommandsInterface.CF_ACTION_DISABLE;
import static com.android.internal.telephony.CommandsInterface.CF_ACTION_ENABLE;
import static com.android.internal.telephony.CommandsInterface.CF_ACTION_ERASURE;
import static com.android.internal.telephony.CommandsInterface.CF_ACTION_REGISTRATION;
import static com.android.internal.telephony.CommandsInterface.CF_REASON_ALL;
import static com.android.internal.telephony.CommandsInterface.CF_REASON_ALL_CONDITIONAL;
import static com.android.internal.telephony.CommandsInterface.CF_REASON_NO_REPLY;
import static com.android.internal.telephony.CommandsInterface.CF_REASON_NOT_REACHABLE;
import static com.android.internal.telephony.CommandsInterface.CF_REASON_BUSY;
import static com.android.internal.telephony.CommandsInterface.CF_REASON_UNCONDITIONAL;
import static com.android.internal.telephony.CommandsInterface.SERVICE_CLASS_VOICE;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_BASEBAND_VERSION;

import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.PhoneSubInfo;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.ServiceStateTracker;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
=======
import android.content.*;
import android.database.SQLException;
import android.os.*;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import com.android.internal.telephony.*;
import com.android.internal.telephony.gsm.stk.Service;
import static com.android.internal.telephony.gsm.CommandsInterface.*;

import com.android.internal.telephony.test.SimulatedRadioControl;
import android.text.TextUtils;
import android.util.Log;
import static com.android.internal.telephony.TelephonyProperties.*;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.telephony.CellLocation;
import android.telephony.ServiceState;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * {@hide}
 */
public class GSMPhone extends PhoneBase {
    // NOTE that LOG_TAG here is "GSM", which means that log messages
    // from this file will go into the radio log rather than the main
    // log.  (Use "adb logcat -b radio" to see them.)
    static final String LOG_TAG = "GSM";
<<<<<<< HEAD
    private static final boolean LOCAL_DEBUG = true;
    private static final boolean VDBG = false; /* STOP SHIP if true */

    // Key used to read/write current ciphering state
    public static final String CIPHERING_KEY = "ciphering_key";
    // Key used to read/write voice mail number
    public static final String VM_NUMBER = "vm_number_key";
    // Key used to read/write the SIM IMSI used for storing the voice mail
    public static final String VM_SIM_IMSI = "vm_sim_imsi_key";

    // Instance Variables
    GsmCallTracker mCT;
    GsmServiceStateTracker mSST;
=======
    private static final boolean LOCAL_DEBUG = false;

    // Key used to read and write the saved network selection value
    public static final String NETWORK_SELECTION_KEY = "network_selection_key";
    // Key used to read/write "disable data connection on boot" pref (used for testing)
    public static final String DATA_DISABLED_ON_BOOT_KEY = "disabled_on_boot_key";
    // Key used to read/write current ciphering state
    public static final String CIPHERING_KEY = "ciphering_key";
    // Key used to read/write current CLIR setting
    public static final String CLIR_KEY = "clir_key";


    //***** Instance Variables

    CallTracker mCT;
    ServiceStateTracker mSST;
    CommandsInterface mCM;
    SMSDispatcher mSMS;
    DataConnectionTracker mDataConnection;
    SIMFileHandler mSIMFileHandler;
    SIMRecords mSIMRecords;
    GsmSimCard mSimCard;
    Service mStkService;
    MyHandler h;
>>>>>>> 54b6cfa... Initial Contribution
    ArrayList <GsmMmiCode> mPendingMMIs = new ArrayList<GsmMmiCode>();
    SimPhoneBookInterfaceManager mSimPhoneBookIntManager;
    SimSmsInterfaceManager mSimSmsIntManager;
    PhoneSubInfo mSubInfo;

<<<<<<< HEAD

=======
>>>>>>> 54b6cfa... Initial Contribution
    Registrant mPostDialHandler;

    /** List of Registrants to receive Supplementary Service Notifications. */
    RegistrantList mSsnRegistrants = new RegistrantList();

    Thread debugPortThread;
    ServerSocket debugSocket;

<<<<<<< HEAD
    private String mImei;
    private String mImeiSv;
    private String mVmNumber;


    // Constructors

    public
    GSMPhone (Context context, CommandsInterface ci, PhoneNotifier notifier) {
=======
    private int mReportedRadioResets;
    private int mReportedAttemptedConnects;
    private int mReportedSuccessfulConnects;

    private String mImei;
    private String mImeiSv;

    //***** Event Constants

    static final int EVENT_RADIO_AVAILABLE          = 1;
    /** Supplemnetary Service Notification received. */
    static final int EVENT_SSN                      = 2;
    static final int EVENT_SIM_RECORDS_LOADED       = 3;
    static final int EVENT_MMI_DONE                 = 4;
    static final int EVENT_RADIO_ON                 = 5;
    static final int EVENT_GET_BASEBAND_VERSION_DONE = 6;
    static final int EVENT_USSD                     = 7;
    static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 8;
    static final int EVENT_GET_IMEI_DONE            = 9;
    static final int EVENT_GET_IMEISV_DONE          = 10;
    static final int EVENT_GET_SIM_STATUS_DONE      = 11;
    static final int EVENT_SET_CALL_FORWARD_DONE    = 12;
    static final int EVENT_GET_CALL_FORWARD_DONE    = 13;
    static final int EVENT_CALL_RING                = 14;
    // Used to intercept the carriere selection calls so that 
    // we can save the values.
    static final int EVENT_SET_NETWORK_MANUAL_COMPLETE = 15;
    static final int EVENT_SET_NETWORK_AUTOMATIC_COMPLETE = 16;
    static final int EVENT_SET_CLIR_COMPLETE = 17;
    static final int EVENT_REGISTERED_TO_NETWORK = 18;

    //***** Constructors

    public
    GSMPhone (Context context, CommandsInterface ci, PhoneNotifier notifier)
    {
>>>>>>> 54b6cfa... Initial Contribution
        this(context,ci,notifier, false);
    }

    public
<<<<<<< HEAD
    GSMPhone (Context context, CommandsInterface ci, PhoneNotifier notifier, boolean unitTestMode) {
        super(notifier, context, ci, unitTestMode);
=======
    GSMPhone (Context context, CommandsInterface ci, PhoneNotifier notifier, boolean unitTestMode)
    {
        super(notifier, context, unitTestMode);

        h = new MyHandler();
        mCM = ci;
>>>>>>> 54b6cfa... Initial Contribution

        if (ci instanceof SimulatedRadioControl) {
            mSimulatedRadioControl = (SimulatedRadioControl) ci;
        }

<<<<<<< HEAD
        mCM.setPhoneType(Phone.PHONE_TYPE_GSM);
        mIccCard.set(UiccController.getInstance(this).getIccCard());
        mIccRecords = mIccCard.get().getIccRecords();
        mCT = new GsmCallTracker(this);
        mSST = new GsmServiceStateTracker (this);
        mSMS = new GsmSMSDispatcher(this, mSmsStorageMonitor, mSmsUsageMonitor);
        mDataConnectionTracker = new GsmDataConnectionTracker (this);
        if (!unitTestMode) {
            mSimPhoneBookIntManager = new SimPhoneBookInterfaceManager(this);
            mSimSmsIntManager = new SimSmsInterfaceManager(this, mSMS);
            mSubInfo = new PhoneSubInfo(this);
        }

        mCM.registerForAvailable(this, EVENT_RADIO_AVAILABLE, null);
        registerForSimRecordEvents();
        mCM.registerForOffOrNotAvailable(this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mCM.registerForOn(this, EVENT_RADIO_ON, null);
        mCM.setOnUSSD(this, EVENT_USSD, null);
        mCM.setOnSuppServiceNotification(this, EVENT_SSN, null);
        mSST.registerForNetworkAttached(this, EVENT_REGISTERED_TO_NETWORK, null);
=======
        mCT = new CallTracker(this);
        mSST = new ServiceStateTracker (this);
        mSMS = new SMSDispatcher(this);
        mSIMFileHandler = new SIMFileHandler(this);
        mSIMRecords = new SIMRecords(this);
        mDataConnection = new DataConnectionTracker (this);
        mSimCard = new GsmSimCard(this);
        if (!unitTestMode) {
            mSimPhoneBookIntManager = new SimPhoneBookInterfaceManager(this);
            mSimSmsIntManager = new SimSmsInterfaceManager(this);
            mSubInfo = new PhoneSubInfo(this);
        }
        mStkService = Service.getInstance(mCM, mSIMRecords, mContext,
                mSIMFileHandler, mSimCard);
                
        mCM.registerForAvailable(h, EVENT_RADIO_AVAILABLE, null);
        mSIMRecords.registerForRecordsLoaded(h, EVENT_SIM_RECORDS_LOADED, null);
        mCM.registerForOffOrNotAvailable(h, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, 
                                                    null);
        mCM.registerForOn(h, EVENT_RADIO_ON, null);
        mCM.setOnUSSD(h, EVENT_USSD, null);
        mCM.setOnSuppServiceNotification(h, EVENT_SSN, null);
        mCM.setOnCallRing(h, EVENT_CALL_RING, null);
        mSST.registerForNetworkAttach(h, EVENT_REGISTERED_TO_NETWORK, null);
>>>>>>> 54b6cfa... Initial Contribution

        if (false) {
            try {
                //debugSocket = new LocalServerSocket("com.android.internal.telephony.debug");
                debugSocket = new ServerSocket();
                debugSocket.setReuseAddress(true);
                debugSocket.bind (new InetSocketAddress("127.0.0.1", 6666));

                debugPortThread
                    = new Thread(
                        new Runnable() {
                            public void run() {
                                for(;;) {
                                    try {
                                        Socket sock;
                                        sock = debugSocket.accept();
                                        Log.i(LOG_TAG, "New connection; resetting radio");
                                        mCM.resetRadio(null);
                                        sock.close();
                                    } catch (IOException ex) {
<<<<<<< HEAD
                                        Log.w(LOG_TAG,
=======
                                        Log.w(LOG_TAG, 
>>>>>>> 54b6cfa... Initial Contribution
                                            "Exception accepting socket", ex);
                                    }
                                }
                            }
                        },
                        "GSMPhone debug");

                debugPortThread.start();

            } catch (IOException ex) {
                Log.w(LOG_TAG, "Failure to open com.android.internal.telephony.debug socket", ex);
            }
        }
<<<<<<< HEAD

        //Change the system property
        SystemProperties.set(TelephonyProperties.CURRENT_ACTIVE_PHONE,
                new Integer(Phone.PHONE_TYPE_GSM).toString());
    }

    @Override
    public void dispose() {
        synchronized(PhoneProxy.lockForRadioTechnologyChange) {
            super.dispose();

            //Unregister from all former registered events
            mCM.unregisterForAvailable(this); //EVENT_RADIO_AVAILABLE
            unregisterForSimRecordEvents();
            mCM.unregisterForOffOrNotAvailable(this); //EVENT_RADIO_OFF_OR_NOT_AVAILABLE
            mCM.unregisterForOn(this); //EVENT_RADIO_ON
            mSST.unregisterForNetworkAttached(this); //EVENT_REGISTERED_TO_NETWORK
            mCM.unSetOnUSSD(this);
            mCM.unSetOnSuppServiceNotification(this);

            mPendingMMIs.clear();

            //Force all referenced classes to unregister their former registered events
            mCT.dispose();
            mDataConnectionTracker.dispose();
            mSST.dispose();
            mSimPhoneBookIntManager.dispose();
            mSimSmsIntManager.dispose();
            mSubInfo.dispose();
        }
    }

    @Override
    public void removeReferences() {
        Log.d(LOG_TAG, "removeReferences");
        mSimulatedRadioControl = null;
        mSimPhoneBookIntManager = null;
        mSimSmsIntManager = null;
        mSubInfo = null;
        mCT = null;
        mSST = null;
        super.removeReferences();
    }

    protected void finalize() {
        if(LOCAL_DEBUG) Log.d(LOG_TAG, "GSMPhone finalized");
    }


    public ServiceState
    getServiceState() {
=======
    }
    
    //***** Overridden from Phone

    public ServiceState 
    getServiceState()
    {
>>>>>>> 54b6cfa... Initial Contribution
        return mSST.ss;
    }

    public CellLocation getCellLocation() {
        return mSST.cellLoc;
    }

<<<<<<< HEAD
    public Phone.State getState() {
        return mCT.state;
    }

    public String getPhoneName() {
        return "GSM";
    }

    public int getPhoneType() {
        return Phone.PHONE_TYPE_GSM;
    }

    public SignalStrength getSignalStrength() {
        return mSST.mSignalStrength;
    }

    public CallTracker getCallTracker() {
        return mCT;
    }

    public ServiceStateTracker getServiceStateTracker() {
        return mSST;
    }

    public List<? extends MmiCode>
    getPendingMmiCodes() {
        return mPendingMMIs;
    }

    public DataState getDataConnectionState(String apnType) {
        DataState ret = DataState.DISCONNECTED;

        if (mSST == null) {
            // Radio Technology Change is ongoning, dispose() and removeReferences() have
            // already been called

            ret = DataState.DISCONNECTED;
=======
    public Phone.State 
    getState()
    {
        return mCT.state;
    }

    public String
    getPhoneName()
    {
        return "GSM";
    }

    public String[] getActiveApnTypes() {
        return mDataConnection.getActiveApnTypes();
    }

    public String getActiveApn() {
        return mDataConnection.getActiveApnString();
    }

    public int
    getSignalStrengthASU()
    {
        return mSST.rssi == 99 ? -1 : mSST.rssi;
    }

    public boolean
    getMessageWaitingIndicator()
    {
        return mSIMRecords.getVoiceMessageWaiting();
    }

    public boolean
    getCallForwardingIndicator() {
        return mSIMRecords.getVoiceCallForwardingFlag();
    }

    public List<? extends MmiCode>
    getPendingMmiCodes()
    {
        return mPendingMMIs;
    }

    public DataState getDataConnectionState() {
        DataState ret = DataState.DISCONNECTED;

        if ((SystemProperties.get("adb.connected", "").length() > 0)
                && (SystemProperties.get("android.net.use-adb-networking", "")
                .length() > 0)) {
            // We're connected to an ADB host and we have USB networking
            // turned on. No matter what the radio state is,
            // we report data connected

            ret = DataState.CONNECTED;
>>>>>>> 54b6cfa... Initial Contribution
        } else if (mSST.getCurrentGprsState()
                != ServiceState.STATE_IN_SERVICE) {
            // If we're out of service, open TCP sockets may still work
            // but no data will flow
            ret = DataState.DISCONNECTED;
<<<<<<< HEAD
        } else if (mDataConnectionTracker.isApnTypeEnabled(apnType) == false ||
                mDataConnectionTracker.isApnTypeActive(apnType) == false) {
            //TODO: isApnTypeActive() is just checking whether ApnContext holds
            //      Dataconnection or not. Checking each ApnState below should
            //      provide the same state. Calling isApnTypeActive() can be removed.
            ret = DataState.DISCONNECTED;
        } else { /* mSST.gprsState == ServiceState.STATE_IN_SERVICE */
            switch (mDataConnectionTracker.getState(apnType)) {
                case FAILED:
                case IDLE:
                    ret = DataState.DISCONNECTED;
                break;

                case CONNECTED:
                case DISCONNECTING:
                    if ( mCT.state != Phone.State.IDLE
                            && !mSST.isConcurrentVoiceAndDataAllowed()) {
                        ret = DataState.SUSPENDED;
                    } else {
                        ret = DataState.CONNECTED;
                    }
                break;

                case INITING:
                case CONNECTING:
                case SCANNING:
                    ret = DataState.CONNECTING;
                break;
=======
        } else { /* mSST.gprsState == ServiceState.STATE_IN_SERVICE */
            switch (mDataConnection.state) {
            case FAILED:
            case IDLE:
                ret = DataState.DISCONNECTED;
            break;

            case CONNECTED:
                if ( mCT.state != Phone.State.IDLE
                        && !mSST.isConcurrentVoiceAndData())
                    ret = DataState.SUSPENDED;
                else
                    ret = DataState.CONNECTED;
            break;

            case INITING:
            case CONNECTING:
            case SCANNING:
                ret = DataState.CONNECTING;
            break;
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        return ret;
    }

    public DataActivityState getDataActivityState() {
        DataActivityState ret = DataActivityState.NONE;

        if (mSST.getCurrentGprsState() == ServiceState.STATE_IN_SERVICE) {
<<<<<<< HEAD
            switch (mDataConnectionTracker.getActivity()) {
                case DATAIN:
                    ret = DataActivityState.DATAIN;
                break;

                case DATAOUT:
                    ret = DataActivityState.DATAOUT;
                break;

                case DATAINANDOUT:
                    ret = DataActivityState.DATAINANDOUT;
                break;
=======
            switch (mDataConnection.activity) {

            case DATAIN:
                ret = DataActivityState.DATAIN;
            break;

            case DATAOUT:
                ret = DataActivityState.DATAOUT;
            break;

            case DATAINANDOUT:
                ret = DataActivityState.DATAINANDOUT;
            break;
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        return ret;
    }

    /**
<<<<<<< HEAD
     * Notify any interested party of a Phone state change {@link Phone.State}
=======
     * Notify any interested party of a Phone state change.
>>>>>>> 54b6cfa... Initial Contribution
     */
    /*package*/ void notifyPhoneStateChanged() {
        mNotifier.notifyPhoneState(this);
    }

    /**
<<<<<<< HEAD
     * Notify registrants of a change in the call state. This notifies changes in {@link Call.State}
     * Use this when changes in the precise call state are needed, else use notifyPhoneStateChanged.
     */
    /*package*/ void notifyPreciseCallStateChanged() {
        /* we'd love it if this was package-scoped*/
        super.notifyPreciseCallStateChangedP();
    }

    /*package*/ void
    notifyNewRingingConnection(Connection c) {
=======
     * Notifies registrants (ie, activities in the Phone app) about
     * changes to call state (including Phone and Connection changes).
     */
    /*package*/ void
    notifyCallStateChanged()
    {
        /* we'd love it if this was package-scoped*/
        super.notifyCallStateChangedP();
    }

    /*package*/ void
    notifyNewRingingConnection(Connection c)
    {
>>>>>>> 54b6cfa... Initial Contribution
        /* we'd love it if this was package-scoped*/
        super.notifyNewRingingConnectionP(c);
    }

<<<<<<< HEAD
    /*package*/ void
    notifyDisconnect(Connection cn) {
=======
    /**
     * Notifiy registrants of a RING event.
     */
    void notifyIncomingRing() {    
        AsyncResult ar = new AsyncResult(null, this, null);
        mIncomingRingRegistrants.notifyRegistrants(ar);
    }
    
    /*package*/ void
    notifyDisconnect(Connection cn)
    {
>>>>>>> 54b6cfa... Initial Contribution
        mDisconnectRegistrants.notifyResult(cn);
    }

    void notifyUnknownConnection() {
        mUnknownConnectionRegistrants.notifyResult(this);
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    void notifySuppServiceFailed(SuppService code) {
        mSuppServiceFailedRegistrants.notifyResult(code);
    }

    /*package*/ void
<<<<<<< HEAD
    notifyServiceStateChanged(ServiceState ss) {
=======
    notifyServiceStateChanged(ServiceState ss)
    {
>>>>>>> 54b6cfa... Initial Contribution
        super.notifyServiceStateChangedP(ss);
    }

    /*package*/
    void notifyLocationChanged() {
        mNotifier.notifyCellLocation(this);
    }

    /*package*/ void
<<<<<<< HEAD
    notifySignalStrength() {
        mNotifier.notifySignalStrength(this);
    }

    public void
=======
    notifySignalStrength()
    {
        mNotifier.notifySignalStrength(this);
    }

    /*package*/ void
    notifyDataConnection(String reason) {
        mNotifier.notifyDataConnection(this, reason);
    }

    /*package*/ void
    notifyDataConnectionFailed(String reason) {
        mNotifier.notifyDataConnectionFailed(this, reason);
    }

    /*package*/ void
    notifyDataActivity() {
        mNotifier.notifyDataActivity(this);
    }

    /*package*/ void
    updateMessageWaitingIndicator(boolean mwi)
    {
        // this also calls notifyMessageWaitingIndicator()
        mSIMRecords.setVoiceMessageWaiting(1, mwi ? -1 : 0);
    }

    /*package*/ void
    notifyMessageWaitingIndicator()
    {
        mNotifier.notifyMessageWaitingChanged(this);
    }

    /*package*/ void
>>>>>>> 54b6cfa... Initial Contribution
    notifyCallForwardingIndicator() {
        mNotifier.notifyCallForwardingChanged(this);
    }

<<<<<<< HEAD
    // override for allowing access from other classes of this package
    /**
     * {@inheritDoc}
     */
    public final void
    setSystemProperty(String property, String value) {
        super.setSystemProperty(property, value);
=======
    /**
     * Set a system property, unless we're in unit test mode
     */

    /*package*/ void
    setSystemProperty(String property, String value)
    {
        if(getUnitTestMode()) {
            return;
        }
        SystemProperties.set(property, value);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void registerForSuppServiceNotification(
            Handler h, int what, Object obj) {
        mSsnRegistrants.addUnique(h, what, obj);
        if (mSsnRegistrants.size() == 1) mCM.setSuppServiceNotifications(true, null);
    }

    public void unregisterForSuppServiceNotification(Handler h) {
        mSsnRegistrants.remove(h);
        if (mSsnRegistrants.size() == 0) mCM.setSuppServiceNotifications(false, null);
    }

<<<<<<< HEAD
    public void
    acceptCall() throws CallStateException {
        mCT.acceptCall();
    }

    public void
    rejectCall() throws CallStateException {
=======
    public void 
    acceptCall() throws CallStateException
    {
        mCT.acceptCall();
    }

    public void 
    rejectCall() throws CallStateException
    {
>>>>>>> 54b6cfa... Initial Contribution
        mCT.rejectCall();
    }

    public void
<<<<<<< HEAD
    switchHoldingAndActive() throws CallStateException {
        mCT.switchWaitingOrHoldingAndActive();
    }

    public boolean canConference() {
        return mCT.canConference();
    }

    public boolean canDial() {
        return mCT.canDial();
    }

    public void conference() throws CallStateException {
        mCT.conference();
    }

    public void clearDisconnected() {
        mCT.clearDisconnected();
    }

    public boolean canTransfer() {
        return mCT.canTransfer();
    }

    public void explicitCallTransfer() throws CallStateException {
        mCT.explicitCallTransfer();
    }

    public GsmCall
    getForegroundCall() {
        return mCT.foregroundCall;
    }

    public GsmCall
    getBackgroundCall() {
        return mCT.backgroundCall;
    }

    public GsmCall
    getRingingCall() {
=======
    switchHoldingAndActive() throws CallStateException
    {
        mCT.switchWaitingOrHoldingAndActive();
    }


    public boolean canConference()
    {
        return mCT.canConference();
    }

    public boolean canDial()
    {
        return mCT.canDial();
    }

    public void conference() throws CallStateException
    {
        mCT.conference();
    }

    public void clearDisconnected()
    {
    
        mCT.clearDisconnected();
    }

    public boolean canTransfer()
    {
        return mCT.canTransfer();
    }

    public void explicitCallTransfer() throws CallStateException
    {
        mCT.explicitCallTransfer();
    }

    public Call
    getForegroundCall()
    {
        return mCT.foregroundCall;
    }

    public Call 
    getBackgroundCall()
    {
        return mCT.backgroundCall;
    }

    public Call 
    getRingingCall()
    {
>>>>>>> 54b6cfa... Initial Contribution
        return mCT.ringingCall;
    }

    private boolean handleCallDeflectionIncallSupplementaryService(
            String dialString) throws CallStateException {
        if (dialString.length() > 1) {
            return false;
        }

<<<<<<< HEAD
        if (getRingingCall().getState() != GsmCall.State.IDLE) {
=======
        if (getRingingCall().getState() != Call.State.IDLE) {
>>>>>>> 54b6cfa... Initial Contribution
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 0: rejectCall");
            try {
                mCT.rejectCall();
            } catch (CallStateException e) {
                if (LOCAL_DEBUG) Log.d(LOG_TAG,
                    "reject failed", e);
                notifySuppServiceFailed(Phone.SuppService.REJECT);
            }
<<<<<<< HEAD
        } else if (getBackgroundCall().getState() != GsmCall.State.IDLE) {
=======
        } else if (getBackgroundCall().getState() != Call.State.IDLE) {
>>>>>>> 54b6cfa... Initial Contribution
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
                    "MmiCode 0: hangupWaitingOrBackground");
            mCT.hangupWaitingOrBackground();
        }

        return true;
    }

    private boolean handleCallWaitingIncallSupplementaryService(
            String dialString) throws CallStateException {
        int len = dialString.length();

        if (len > 2) {
            return false;
        }

<<<<<<< HEAD
        GsmCall call = (GsmCall) getForegroundCall();
=======
        GSMCall call = (GSMCall) getForegroundCall();
>>>>>>> 54b6cfa... Initial Contribution

        try {
            if (len > 1) {
                char ch = dialString.charAt(1);
                int callIndex = ch - '0';

<<<<<<< HEAD
                if (callIndex >= 1 && callIndex <= GsmCallTracker.MAX_CONNECTIONS) {
=======
                if (callIndex >= 1 && callIndex <= CallTracker.MAX_CONNECTIONS) {
>>>>>>> 54b6cfa... Initial Contribution
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
                            "MmiCode 1: hangupConnectionByIndex " +
                            callIndex);
                    mCT.hangupConnectionByIndex(call, callIndex);
                }
            } else {
<<<<<<< HEAD
                if (call.getState() != GsmCall.State.IDLE) {
=======
                if (call.getState() != Call.State.IDLE) {
>>>>>>> 54b6cfa... Initial Contribution
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
                            "MmiCode 1: hangup foreground");
                    //mCT.hangupForegroundResumeBackground();
                    mCT.hangup(call);
                } else {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
                            "MmiCode 1: switchWaitingOrHoldingAndActive");
                    mCT.switchWaitingOrHoldingAndActive();
                }
            }
        } catch (CallStateException e) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
                "hangup failed", e);
            notifySuppServiceFailed(Phone.SuppService.HANGUP);
        }

        return true;
    }

    private boolean handleCallHoldIncallSupplementaryService(String dialString)
            throws CallStateException {
        int len = dialString.length();

        if (len > 2) {
            return false;
        }

<<<<<<< HEAD
        GsmCall call = (GsmCall) getForegroundCall();
=======
        GSMCall call = (GSMCall) getForegroundCall();
>>>>>>> 54b6cfa... Initial Contribution

        if (len > 1) {
            try {
                char ch = dialString.charAt(1);
                int callIndex = ch - '0';
<<<<<<< HEAD
                GsmConnection conn = mCT.getConnectionByIndex(call, callIndex);

                // gsm index starts at 1, up to 5 connections in a call,
                if (conn != null && callIndex >= 1 && callIndex <= GsmCallTracker.MAX_CONNECTIONS) {
=======
                GSMConnection conn = mCT.getConnectionByIndex(call, callIndex);
                
                // gsm index starts at 1, up to 5 connections in a call,
                if (conn != null && callIndex >= 1 && callIndex <= CallTracker.MAX_CONNECTIONS) {
>>>>>>> 54b6cfa... Initial Contribution
                    if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 2: separate call "+
                            callIndex);
                    mCT.separate(conn);
                } else {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG, "separate: invalid call index "+
                            callIndex);
                    notifySuppServiceFailed(Phone.SuppService.SEPARATE);
                }
            } catch (CallStateException e) {
                if (LOCAL_DEBUG) Log.d(LOG_TAG,
                    "separate failed", e);
                notifySuppServiceFailed(Phone.SuppService.SEPARATE);
            }
        } else {
            try {
<<<<<<< HEAD
                if (getRingingCall().getState() != GsmCall.State.IDLE) {
=======
                if (getRingingCall().getState() != Call.State.IDLE) {
>>>>>>> 54b6cfa... Initial Contribution
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
                    "MmiCode 2: accept ringing call");
                    mCT.acceptCall();
                } else {
                    if (LOCAL_DEBUG) Log.d(LOG_TAG,
                    "MmiCode 2: switchWaitingOrHoldingAndActive");
                    mCT.switchWaitingOrHoldingAndActive();
                }
            } catch (CallStateException e) {
                if (LOCAL_DEBUG) Log.d(LOG_TAG,
                    "switch failed", e);
                notifySuppServiceFailed(Phone.SuppService.SWITCH);
            }
        }

        return true;
    }

    private boolean handleMultipartyIncallSupplementaryService(
            String dialString) throws CallStateException {
        if (dialString.length() > 1) {
            return false;
        }

        if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 3: merge calls");
        try {
            conference();
        } catch (CallStateException e) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
                "conference failed", e);
            notifySuppServiceFailed(Phone.SuppService.CONFERENCE);
        }
        return true;
    }

    private boolean handleEctIncallSupplementaryService(String dialString)
            throws CallStateException {

        int len = dialString.length();

        if (len != 1) {
            return false;
        }

        if (LOCAL_DEBUG) Log.d(LOG_TAG, "MmiCode 4: explicit call transfer");
        try {
            explicitCallTransfer();
        } catch (CallStateException e) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG,
                "transfer failed", e);
            notifySuppServiceFailed(Phone.SuppService.TRANSFER);
        }
        return true;
    }

    private boolean handleCcbsIncallSupplementaryService(String dialString)
            throws CallStateException {
        if (dialString.length() > 1) {
            return false;
        }

        Log.i(LOG_TAG, "MmiCode 5: CCBS not supported!");
        // Treat it as an "unknown" service.
        notifySuppServiceFailed(Phone.SuppService.UNKNOWN);
        return true;
    }

    public boolean handleInCallMmiCommands(String dialString)
            throws CallStateException {
        if (!isInCall()) {
            return false;
        }

        if (TextUtils.isEmpty(dialString)) {
            return false;
        }

        boolean result = false;
        char ch = dialString.charAt(0);
        switch (ch) {
            case '0':
                result = handleCallDeflectionIncallSupplementaryService(
                        dialString);
                break;
            case '1':
                result = handleCallWaitingIncallSupplementaryService(
                        dialString);
                break;
            case '2':
                result = handleCallHoldIncallSupplementaryService(dialString);
                break;
            case '3':
                result = handleMultipartyIncallSupplementaryService(dialString);
                break;
            case '4':
                result = handleEctIncallSupplementaryService(dialString);
                break;
            case '5':
                result = handleCcbsIncallSupplementaryService(dialString);
                break;
            default:
                break;
        }

        return result;
    }

    boolean isInCall() {
<<<<<<< HEAD
        GsmCall.State foregroundCallState = getForegroundCall().getState();
        GsmCall.State backgroundCallState = getBackgroundCall().getState();
        GsmCall.State ringingCallState = getRingingCall().getState();
=======
        Call.State foregroundCallState = getForegroundCall().getState();
        Call.State backgroundCallState = getBackgroundCall().getState();
        Call.State ringingCallState = getRingingCall().getState();
>>>>>>> 54b6cfa... Initial Contribution

       return (foregroundCallState.isAlive() ||
                backgroundCallState.isAlive() ||
                ringingCallState.isAlive());
    }

    public Connection
<<<<<<< HEAD
    dial(String dialString) throws CallStateException {
        return dial(dialString, null);
    }

    public Connection
    dial (String dialString, UUSInfo uusInfo) throws CallStateException {
=======
    dial (String dialString) throws CallStateException {
>>>>>>> 54b6cfa... Initial Contribution
        // Need to make sure dialString gets parsed properly
        String newDialString = PhoneNumberUtils.stripSeparators(dialString);

        // handle in-call MMI first if applicable
        if (handleInCallMmiCommands(newDialString)) {
            return null;
        }
<<<<<<< HEAD

        // Only look at the Network portion for mmi
        String networkPortion = PhoneNumberUtils.extractNetworkPortionAlt(newDialString);
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(networkPortion, this);
=======
        
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(newDialString, this);
>>>>>>> 54b6cfa... Initial Contribution
        if (LOCAL_DEBUG) Log.d(LOG_TAG,
                               "dialing w/ mmi '" + mmi + "'...");

        if (mmi == null) {
<<<<<<< HEAD
            return mCT.dial(newDialString, uusInfo);
        } else if (mmi.isTemporaryModeCLIR()) {
            return mCT.dial(mmi.dialingNumber, mmi.getCLIRMode(), uusInfo);
=======
            return mCT.dial(newDialString);
        } else if (mmi.isTemporaryModeCLIR()) {
            return mCT.dial(mmi.dialingNumber, mmi.getCLIRMode());
>>>>>>> 54b6cfa... Initial Contribution
        } else {
            mPendingMMIs.add(mmi);
            mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
            mmi.processCode();

            // FIXME should this return null or something else?
            return null;
        }
    }

    public boolean handlePinMmi(String dialString) {
        GsmMmiCode mmi = GsmMmiCode.newFromDialString(dialString, this);
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        if (mmi != null && mmi.isPinCommand()) {
            mPendingMMIs.add(mmi);
            mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
            mmi.processCode();
            return true;
        }
<<<<<<< HEAD

        return false;
=======
        
        return false;        
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void sendUssdResponse(String ussdMessge) {
        GsmMmiCode mmi = GsmMmiCode.newFromUssdUserInput(ussdMessge, this);
        mPendingMMIs.add(mmi);
        mMmiRegistrants.notifyRegistrants(new AsyncResult(null, mmi, null));
        mmi.sendUssd(ussdMessge);
    }
<<<<<<< HEAD

    public void
    sendDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
=======
    
    public void
    sendDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG, 
>>>>>>> 54b6cfa... Initial Contribution
                    "sendDtmf called with invalid character '" + c + "'");
        } else {
            if (mCT.state ==  Phone.State.OFFHOOK) {
                mCM.sendDtmf(c, null);
            }
        }
    }

    public void
    startDtmf(char c) {
        if (!PhoneNumberUtils.is12Key(c)) {
            Log.e(LOG_TAG,
                "startDtmf called with invalid character '" + c + "'");
        } else {
            mCM.startDtmf(c, null);
        }
    }

    public void
    stopDtmf() {
        mCM.stopDtmf(null);
    }

    public void
<<<<<<< HEAD
    sendBurstDtmf(String dtmfString) {
        Log.e(LOG_TAG, "[GSMPhone] sendBurstDtmf() is a CDMA method");
    }

    public void
=======
>>>>>>> 54b6cfa... Initial Contribution
    setRadioPower(boolean power) {
        mSST.setRadioPower(power);
    }

<<<<<<< HEAD
    private void storeVoiceMailNumber(String number) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(VM_NUMBER, number);
        editor.apply();
        setVmSimImsi(getSubscriberId());
    }

    public String getVoiceMailNumber() {
        // Read from the SIM. If its null, try reading from the shared preference area.
        String number = mIccRecords.getVoiceMailNumber();
        if (TextUtils.isEmpty(number)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            number = sp.getString(VM_NUMBER, null);
        }
        return number;
    }

    private String getVmSimImsi() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(VM_SIM_IMSI, null);
    }

    private void setVmSimImsi(String imsi) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(VM_SIM_IMSI, imsi);
        editor.apply();
=======

    public String getVoiceMailNumber() {
        return mSIMRecords.getVoiceMailNumber();
>>>>>>> 54b6cfa... Initial Contribution
    }

    public String getVoiceMailAlphaTag() {
        String ret;

<<<<<<< HEAD
        ret = mIccRecords.getVoiceMailAlphaTag();
=======
        ret = mSIMRecords.getVoiceMailAlphaTag();
>>>>>>> 54b6cfa... Initial Contribution

        if (ret == null || ret.length() == 0) {
            return mContext.getText(
                com.android.internal.R.string.defaultVoiceMailAlphaTag).toString();
        }

<<<<<<< HEAD
        return ret;
=======
        return ret;        
>>>>>>> 54b6cfa... Initial Contribution
    }

    public String getDeviceId() {
        return mImei;
    }

    public String getDeviceSvn() {
        return mImeiSv;
    }

<<<<<<< HEAD
    public String getImei() {
        return mImei;
    }

    public String getEsn() {
        Log.e(LOG_TAG, "[GSMPhone] getEsn() is a CDMA method");
        return "0";
    }

    public String getMeid() {
        Log.e(LOG_TAG, "[GSMPhone] getMeid() is a CDMA method");
        return "0";
    }

    public String getSubscriberId() {
        return mIccRecords.getIMSI();
    }

    public String getLine1Number() {
        return mIccRecords.getMsisdnNumber();
    }

    @Override
    public String getMsisdn() {
        return mIccRecords.getMsisdnNumber();
    }

    public String getLine1AlphaTag() {
        return mIccRecords.getMsisdnAlphaTag();
    }

    public void setLine1Number(String alphaTag, String number, Message onComplete) {
        mIccRecords.setMsisdnNumber(alphaTag, number, onComplete);
=======
    public String getSubscriberId() {
        return mSIMRecords.imsi;
    }

    public String getSimSerialNumber() {
        return mSIMRecords.iccid;
    }

    public String getLine1Number() {
        return mSIMRecords.getMsisdnNumber();
    }

    public String getLine1AlphaTag() {
        String ret;

        ret = mSIMRecords.getMsisdnAlphaTag();

        if (ret == null || ret.length() == 0) {
            return mContext.getText(
                    com.android.internal.R.string.defaultMsisdnAlphaTag).toString();
        }

        return ret;
    }

    public void setLine1Number(String alphaTag, String number, Message onComplete) {
        mSIMRecords.setMsisdnNumber(alphaTag, number, onComplete);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void setVoiceMailNumber(String alphaTag,
                            String voiceMailNumber,
                            Message onComplete) {
<<<<<<< HEAD

        Message resp;
        mVmNumber = voiceMailNumber;
        resp = obtainMessage(EVENT_SET_VM_NUMBER_DONE, 0, 0, onComplete);
        mIccRecords.setVoiceMailNumber(alphaTag, mVmNumber, resp);
    }

    private boolean isValidCommandInterfaceCFReason (int commandInterfaceCFReason) {
        switch (commandInterfaceCFReason) {
        case CF_REASON_UNCONDITIONAL:
        case CF_REASON_BUSY:
        case CF_REASON_NO_REPLY:
        case CF_REASON_NOT_REACHABLE:
        case CF_REASON_ALL:
        case CF_REASON_ALL_CONDITIONAL:
            return true;
        default:
            return false;
=======
        mSIMRecords.setVoiceMailNumber(alphaTag, voiceMailNumber, onComplete);
    }
    
    private boolean isValidCommandInterfaceCFReason (int commandInterfaceCFReason) {
        switch (commandInterfaceCFReason) {
            case CF_REASON_UNCONDITIONAL:
            case CF_REASON_BUSY:
            case CF_REASON_NO_REPLY:
            case CF_REASON_NOT_REACHABLE:
            case CF_REASON_ALL:
            case CF_REASON_ALL_CONDITIONAL:
                return true;
            default:
                return false;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    private boolean isValidCommandInterfaceCFAction (int commandInterfaceCFAction) {
        switch (commandInterfaceCFAction) {
<<<<<<< HEAD
        case CF_ACTION_DISABLE:
        case CF_ACTION_ENABLE:
        case CF_ACTION_REGISTRATION:
        case CF_ACTION_ERASURE:
            return true;
        default:
            return false;
        }
    }

    protected  boolean isCfEnable(int action) {
        return (action == CF_ACTION_ENABLE) || (action == CF_ACTION_REGISTRATION);
    }

    public void getCallForwardingOption(int commandInterfaceCFReason, Message onComplete) {
=======
            case CF_ACTION_DISABLE:
            case CF_ACTION_ENABLE:
            case CF_ACTION_REGISTRATION:
            case CF_ACTION_ERASURE:
                return true;
            default:
                return false;
        }
    }
    
    private boolean isCfEnable(int action) {
        return (action == CF_ACTION_ENABLE) || (action == CF_ACTION_REGISTRATION);
    }
    
    public void getCallForwardingOption(int commandInterfaceCFReason,
                                        Message onComplete) {
        
>>>>>>> 54b6cfa... Initial Contribution
        if (isValidCommandInterfaceCFReason(commandInterfaceCFReason)) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "requesting call forwarding query.");
            Message resp;
            if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
<<<<<<< HEAD
                resp = obtainMessage(EVENT_GET_CALL_FORWARD_DONE, onComplete);
=======
                resp = h.obtainMessage(EVENT_GET_CALL_FORWARD_DONE, onComplete);
>>>>>>> 54b6cfa... Initial Contribution
            } else {
                resp = onComplete;
            }
            mCM.queryCallForwardStatus(commandInterfaceCFReason,0,null,resp);
        }
    }

    public void setCallForwardingOption(int commandInterfaceCFAction,
<<<<<<< HEAD
            int commandInterfaceCFReason,
            String dialingNumber,
            int timerSeconds,
            Message onComplete) {
        if (    (isValidCommandInterfaceCFAction(commandInterfaceCFAction)) &&
                (isValidCommandInterfaceCFReason(commandInterfaceCFReason))) {

            Message resp;
            if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
                resp = obtainMessage(EVENT_SET_CALL_FORWARD_DONE,
=======
                                        int commandInterfaceCFReason,
                                        String dialingNumber,
                                        int timerSeconds,
                                        Message onComplete) {
            
        if ((isValidCommandInterfaceCFAction(commandInterfaceCFAction)) && 
            (isValidCommandInterfaceCFReason(commandInterfaceCFReason))) {
            
            Message resp;
            if (commandInterfaceCFReason == CF_REASON_UNCONDITIONAL) {
                resp = h.obtainMessage(EVENT_SET_CALL_FORWARD_DONE,
>>>>>>> 54b6cfa... Initial Contribution
                        isCfEnable(commandInterfaceCFAction) ? 1 : 0, 0, onComplete);
            } else {
                resp = onComplete;
            }
            mCM.setCallForward(commandInterfaceCFAction,
                    commandInterfaceCFReason,
                    CommandsInterface.SERVICE_CLASS_VOICE,
                    dialingNumber,
                    timerSeconds,
                    resp);
        }
    }
<<<<<<< HEAD

    public void getOutgoingCallerIdDisplay(Message onComplete) {
        mCM.getCLIR(onComplete);
    }

    public void setOutgoingCallerIdDisplay(int commandInterfaceCLIRMode,
                                           Message onComplete) {
        mCM.setCLIR(commandInterfaceCLIRMode,
                obtainMessage(EVENT_SET_CLIR_COMPLETE, commandInterfaceCLIRMode, 0, onComplete));
    }

    public void getCallWaiting(Message onComplete) {
        //As per 3GPP TS 24.083, section 1.6 UE doesn't need to send service
        //class parameter in call waiting interrogation  to network
        mCM.queryCallWaiting(CommandsInterface.SERVICE_CLASS_NONE, onComplete);
    }

    public void setCallWaiting(boolean enable, Message onComplete) {
        mCM.setCallWaiting(enable, CommandsInterface.SERVICE_CLASS_VOICE, onComplete);
    }

    public void
=======
    
    public void getOutgoingCallerIdDisplay(Message onComplete) {
        mCM.getCLIR(onComplete);
    }
    
    public void setOutgoingCallerIdDisplay(int commandInterfaceCLIRMode, 
                                           Message onComplete) {
        mCM.setCLIR(commandInterfaceCLIRMode,
                h.obtainMessage(EVENT_SET_CLIR_COMPLETE, commandInterfaceCLIRMode, 0, onComplete));
    }

    public void getCallWaiting(Message onComplete) {
        mCM.queryCallWaiting(CommandsInterface.SERVICE_CLASS_VOICE, onComplete);
    }
    
    public void setCallWaiting(boolean enable, Message onComplete) {
        mCM.setCallWaiting(enable, CommandsInterface.SERVICE_CLASS_VOICE, onComplete);
    }
    
    public boolean
    getSimRecordsLoaded() {
        return mSIMRecords.getRecordsLoaded();
    }

    public SimCard
    getSimCard() {
        return mSimCard;
    }

    public void 
>>>>>>> 54b6cfa... Initial Contribution
    getAvailableNetworks(Message response) {
        mCM.getAvailableNetworks(response);
    }

    /**
<<<<<<< HEAD
     * Small container class used to hold information relevant to
     * the carrier selection process. operatorNumeric can be ""
     * if we are looking for automatic selection. operatorAlphaLong is the
     * corresponding operator name.
=======
     * Small container class used to hold information relevant to 
     * the carrier selection process. operatorNumeric can be ""
     * if we are looking for automatic selection. 
>>>>>>> 54b6cfa... Initial Contribution
     */
    private static class NetworkSelectMessage {
        public Message message;
        public String operatorNumeric;
<<<<<<< HEAD
        public String operatorAlphaLong;
    }

    public void
    setNetworkSelectionModeAutomatic(Message response) {
        // wrap the response message in our own message along with
        // an empty string (to indicate automatic selection) for the
=======
    }
    
    public void 
    setNetworkSelectionModeAutomatic(Message response) {
        // wrap the response message in our own message along with
        // an empty string (to indicate automatic selection) for the 
>>>>>>> 54b6cfa... Initial Contribution
        // operator's id.
        NetworkSelectMessage nsm = new NetworkSelectMessage();
        nsm.message = response;
        nsm.operatorNumeric = "";
<<<<<<< HEAD
        nsm.operatorAlphaLong = "";

        // get the message
        Message msg = obtainMessage(EVENT_SET_NETWORK_AUTOMATIC_COMPLETE, nsm);
        if (LOCAL_DEBUG)
=======
        
        // get the message
        Message msg = h.obtainMessage(EVENT_SET_NETWORK_AUTOMATIC_COMPLETE, nsm);
        if (LOCAL_DEBUG) 
>>>>>>> 54b6cfa... Initial Contribution
            Log.d(LOG_TAG, "wrapping and sending message to connect automatically");

        mCM.setNetworkSelectionModeAutomatic(msg);
    }

<<<<<<< HEAD
    public void
    selectNetworkManually(OperatorInfo network,
            Message response) {
=======
    public void 
    selectNetworkManually(com.android.internal.telephony.gsm.NetworkInfo network,
                          Message response) {
>>>>>>> 54b6cfa... Initial Contribution
        // wrap the response message in our own message along with
        // the operator's id.
        NetworkSelectMessage nsm = new NetworkSelectMessage();
        nsm.message = response;
<<<<<<< HEAD
        nsm.operatorNumeric = network.getOperatorNumeric();
        nsm.operatorAlphaLong = network.getOperatorAlphaLong();

        // get the message
        Message msg = obtainMessage(EVENT_SET_NETWORK_MANUAL_COMPLETE, nsm);

        mCM.setNetworkSelectionModeManual(network.getOperatorNumeric(), msg);
=======
        nsm.operatorNumeric = network.operatorNumeric;
        
        // get the message
        Message msg = h.obtainMessage(EVENT_SET_NETWORK_MANUAL_COMPLETE, nsm);

        mCM.setNetworkSelectionModeManual(network.operatorNumeric, msg);
    }
    
    /**
     * Method to retrieve the saved operator id from the Shared Preferences
     */
    private String getSavedNetworkSelection() {
        // open the shared preferences and search with our key. 
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sp.getString(NETWORK_SELECTION_KEY, "");
    }

    /**
     * Method to restore the previously saved operator id, or reset to
     * automatic selection, all depending upon the value in the shared
     * preferences.
     */
    void restoreSavedNetworkSelection(Message response) {
        // retrieve the operator id
        String networkSelection = getSavedNetworkSelection();
        
        // set to auto if the id is empty, otherwise select the network.
        if (TextUtils.isEmpty(networkSelection)) {
            mCM.setNetworkSelectionModeAutomatic(response);
        } else {
            mCM.setNetworkSelectionModeManual(networkSelection, response);
        }
    }
    
    public void 
    setPreferredNetworkType(int networkType, Message response) {
        mCM.setPreferredNetworkType(networkType, response);
    }

    public void
    getPreferredNetworkType(Message response) {
        mCM.getPreferredNetworkType(response);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void
    getNeighboringCids(Message response) {
        mCM.getNeighboringCids(response);
    }
<<<<<<< HEAD

    public void setOnPostDialCharacter(Handler h, int what, Object obj) {
        mPostDialHandler = new Registrant(h, what, obj);
    }

    public void setMute(boolean muted) {
        mCT.setMute(muted);
    }

    public boolean getMute() {
        return mCT.getMute();
    }

    public void getDataCallList(Message response) {
        mCM.getDataCallList(response);
    }

    public void updateServiceLocation() {
        mSST.enableSingleLocationUpdate();
=======
    
    public void setOnPostDialCharacter(Handler h, int what, Object obj)
    {
        mPostDialHandler = new Registrant(h, what, obj);
    }


    public void setMute(boolean muted)
    {
        mCT.setMute(muted);
    }
    
    public boolean getMute()
    {
        return mCT.getMute();
    }


    public void invokeOemRilRequestRaw(byte[] data, Message response)
    {
        mCM.invokeOemRilRequestRaw(data, response);
    }

    public void invokeOemRilRequestStrings(String[] strings, Message response)
    {
        mCM.invokeOemRilRequestStrings(strings, response);
    }

    public void getPdpContextList(Message response) {
        mCM.getPDPContextList(response);
    }

    public List<PdpConnection> getCurrentPdpList () {
        return mDataConnection.getAllPdps();
    }

    public void updateServiceLocation(Message response) {
        mSST.getLacAndCid(response);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public void enableLocationUpdates() {
        mSST.enableLocationUpdates();
    }

    public void disableLocationUpdates() {
        mSST.disableLocationUpdates();
    }

<<<<<<< HEAD
    public boolean getDataRoamingEnabled() {
        return mDataConnectionTracker.getDataOnRoamingEnabled();
    }

    public void setDataRoamingEnabled(boolean enable) {
        mDataConnectionTracker.setDataOnRoamingEnabled(enable);
=======
    public void setBandMode(int bandMode, Message response) {
        mCM.setBandMode(bandMode, response);
    }

    public void queryAvailableBandMode(Message response) {
        mCM.queryAvailableBandMode(response);
    }

    public boolean getDataRoamingEnabled() {
        return mDataConnection.getDataOnRoamingEnabled();
    }

    public void setDataRoamingEnabled(boolean enable) {
        mDataConnection.setDataOnRoamingEnabled(enable);
    }

    public boolean enableDataConnectivity() {
        return mDataConnection.setDataEnabled(true);
    }

    public int enableApnType(String type) {
        return mDataConnection.enableApnType(type);
    }

    public int disableApnType(String type) {
        return mDataConnection.disableApnType(type);
    }

    public boolean disableDataConnectivity() {
        return mDataConnection.setDataEnabled(false);
    }

    public String getInterfaceName(String apnType) {
        return mDataConnection.getInterfaceName(apnType);
    }

    public String getIpAddress(String apnType) {
        return mDataConnection.getIpAddress(apnType);
    }

    public String getGateway(String apnType) {
        return mDataConnection.getGateway(apnType);
    }

    public String[] getDnsServers(String apnType) {
        return mDataConnection.getDnsServers(apnType);
    }

    /**
     * The only circumstances under which we report that data connectivity is not
     * possible are
     * <ul>
     * <li>Data roaming is disallowed and we are roaming.</li>
     * <li>The current data state is {@code DISCONNECTED} for a reason other than
     * having explicitly disabled connectivity. In other words, data is not available
     * because the phone is out of coverage or some like reason.</li>
     * </ul>
     * @return {@code true} if data connectivity is possible, {@code false} otherwise.
     */
    public boolean isDataConnectivityPossible() {
        // TODO: Currently checks if any GPRS connection is active. Should it only
        // check for "default"?
        boolean noData = mDataConnection.getDataEnabled() &&
            getDataConnectionState() == DataState.DISCONNECTED;
        return !noData && getSimCard().getState() == SimCard.State.READY &&
                getServiceState().getState() == ServiceState.STATE_IN_SERVICE &&
            (mDataConnection.getDataOnRoamingEnabled() || !getServiceState().getRoaming());
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Removes the given MMI from the pending list and notifies
     * registrants that it is complete.
     * @param mmi MMI that is done
     */
    /*package*/ void
<<<<<<< HEAD
    onMMIDone(GsmMmiCode mmi) {
        /* Only notify complete if it's on the pending list.
=======
    onMMIDone(GsmMmiCode mmi)
    {
        /* Only notify complete if it's on the pending list. 
>>>>>>> 54b6cfa... Initial Contribution
         * Otherwise, it's already been handled (eg, previously canceled).
         * The exception is cancellation of an incoming USSD-REQUEST, which is
         * not on the list.
         */
        if (mPendingMMIs.remove(mmi) || mmi.isUssdRequest()) {
            mMmiCompleteRegistrants.notifyRegistrants(
                new AsyncResult(null, mmi, null));
        }
    }


<<<<<<< HEAD
    private void
    onNetworkInitiatedUssd(GsmMmiCode mmi) {
=======
    private void 
    onNetworkInitiatedUssd(GsmMmiCode mmi)
    {
>>>>>>> 54b6cfa... Initial Contribution
        mMmiCompleteRegistrants.notifyRegistrants(
            new AsyncResult(null, mmi, null));
    }


    /** ussdMode is one of CommandsInterface.USSD_MODE_* */
    private void
<<<<<<< HEAD
    onIncomingUSSD (int ussdMode, String ussdMessage) {
        boolean isUssdError;
        boolean isUssdRequest;

        isUssdRequest
            = (ussdMode == CommandsInterface.USSD_MODE_REQUEST);

        isUssdError
            = (ussdMode != CommandsInterface.USSD_MODE_NOTIFY
                && ussdMode != CommandsInterface.USSD_MODE_REQUEST);

=======
    onIncomingUSSD (int ussdMode, String ussdMessage)
    {
        boolean isUssdError;
        boolean isUssdRequest;
        
        isUssdRequest 
            = (ussdMode == CommandsInterface.USSD_MODE_REQUEST);

        isUssdError 
            = (ussdMode != CommandsInterface.USSD_MODE_NOTIFY
                && ussdMode != CommandsInterface.USSD_MODE_REQUEST);
    
>>>>>>> 54b6cfa... Initial Contribution
        // See comments in GsmMmiCode.java
        // USSD requests aren't finished until one
        // of these two events happen
        GsmMmiCode found = null;
        for (int i = 0, s = mPendingMMIs.size() ; i < s; i++) {
            if(mPendingMMIs.get(i).isPendingUSSD()) {
                found = mPendingMMIs.get(i);
                break;
            }
        }

        if (found != null) {
            // Complete pending USSD

            if (isUssdError) {
                found.onUssdFinishedError();
            } else {
                found.onUssdFinished(ussdMessage, isUssdRequest);
            }
        } else { // pending USSD not found
            // The network may initiate its own USSD request

            // ignore everything that isnt a Notify or a Request
            // also, discard if there is no message to present
            if (!isUssdError && ussdMessage != null) {
                GsmMmiCode mmi;
<<<<<<< HEAD
                mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage,
=======
                mmi = GsmMmiCode.newNetworkInitiatedUssd(ussdMessage, 
>>>>>>> 54b6cfa... Initial Contribution
                                                   isUssdRequest,
                                                   GSMPhone.this);
                onNetworkInitiatedUssd(mmi);
            }
        }
    }

    /**
     * Make sure the network knows our preferred setting.
     */
<<<<<<< HEAD
    protected  void syncClirSetting() {
=======
    private void syncClirSetting() {
>>>>>>> 54b6cfa... Initial Contribution
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        int clirSetting = sp.getInt(CLIR_KEY, -1);
        if (clirSetting >= 0) {
            mCM.setCLIR(clirSetting, null);
        }
    }

<<<<<<< HEAD
    @Override
    public void handleMessage (Message msg) {
        AsyncResult ar;
        Message onComplete;

        switch (msg.what) {
            case EVENT_RADIO_AVAILABLE: {
                mCM.getBasebandVersion(
                        obtainMessage(EVENT_GET_BASEBAND_VERSION_DONE));

                mCM.getIMEI(obtainMessage(EVENT_GET_IMEI_DONE));
                mCM.getIMEISV(obtainMessage(EVENT_GET_IMEISV_DONE));
            }
            break;

            case EVENT_RADIO_ON:
            break;

            case EVENT_REGISTERED_TO_NETWORK:
                syncClirSetting();
                break;

            case EVENT_SIM_RECORDS_LOADED:
                updateCurrentCarrierInProvider();

                // Check if this is a different SIM than the previous one. If so unset the
                // voice mail number.
                String imsi = getVmSimImsi();
                String imsiFromSIM = getSubscriberId();
                if (imsi != null && imsiFromSIM != null && !imsiFromSIM.equals(imsi)) {
                    storeVoiceMailNumber(null);
                    setVmSimImsi(null);
                }

            break;

            case EVENT_GET_BASEBAND_VERSION_DONE:
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    break;
                }

                if (LOCAL_DEBUG) Log.d(LOG_TAG, "Baseband version: " + ar.result);
                setSystemProperty(PROPERTY_BASEBAND_VERSION, (String)ar.result);
            break;

            case EVENT_GET_IMEI_DONE:
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    break;
                }

                mImei = (String)ar.result;
            break;

            case EVENT_GET_IMEISV_DONE:
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    break;
                }

                mImeiSv = (String)ar.result;
            break;

            case EVENT_USSD:
                ar = (AsyncResult)msg.obj;

                String[] ussdResult = (String[]) ar.result;

                if (ussdResult.length > 1) {
                    try {
                        onIncomingUSSD(Integer.parseInt(ussdResult[0]), ussdResult[1]);
                    } catch (NumberFormatException e) {
                        Log.w(LOG_TAG, "error parsing USSD");
                    }
                }
            break;

            case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                // Some MMI requests (eg USSD) are not completed
                // within the course of a CommandsInterface request
                // If the radio shuts off or resets while one of these
                // is pending, we need to clean up.

                for (int i = 0, s = mPendingMMIs.size() ; i < s; i++) {
                    if (mPendingMMIs.get(i).isPendingUSSD()) {
                        mPendingMMIs.get(i).onUssdFinishedError();
                    }
                }
            break;

            case EVENT_SSN:
                ar = (AsyncResult)msg.obj;
                SuppServiceNotification not = (SuppServiceNotification) ar.result;
                mSsnRegistrants.notifyRegistrants(ar);
            break;

            case EVENT_SET_CALL_FORWARD_DONE:
                ar = (AsyncResult)msg.obj;
                if (ar.exception == null) {
                    mIccRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
                }
                onComplete = (Message) ar.userObj;
                if (onComplete != null) {
                    AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                    onComplete.sendToTarget();
                }
                break;

            case EVENT_SET_VM_NUMBER_DONE:
                ar = (AsyncResult)msg.obj;
                if (IccVmNotSupportedException.class.isInstance(ar.exception)) {
                    storeVoiceMailNumber(mVmNumber);
                    ar.exception = null;
                }
                onComplete = (Message) ar.userObj;
                if (onComplete != null) {
                    AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                    onComplete.sendToTarget();
                }
                break;


            case EVENT_GET_CALL_FORWARD_DONE:
                ar = (AsyncResult)msg.obj;
                if (ar.exception == null) {
                    handleCfuQueryResult((CallForwardInfo[])ar.result);
                }
                onComplete = (Message) ar.userObj;
                if (onComplete != null) {
                    AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                    onComplete.sendToTarget();
                }
                break;

            case EVENT_NEW_ICC_SMS:
                ar = (AsyncResult)msg.obj;
                mSMS.dispatchMessage((SmsMessage)ar.result);
                break;

            case EVENT_SET_NETWORK_AUTOMATIC:
                ar = (AsyncResult)msg.obj;
                setNetworkSelectionModeAutomatic((Message)ar.result);
                break;

            case EVENT_ICC_RECORD_EVENTS:
                ar = (AsyncResult)msg.obj;
                processIccRecordEvents((Integer)ar.result);
                break;

            // handle the select network completion callbacks.
            case EVENT_SET_NETWORK_MANUAL_COMPLETE:
            case EVENT_SET_NETWORK_AUTOMATIC_COMPLETE:
                handleSetSelectNetwork((AsyncResult) msg.obj);
                break;

            case EVENT_SET_CLIR_COMPLETE:
                ar = (AsyncResult)msg.obj;
                if (ar.exception == null) {
                    saveClirSetting(msg.arg1);
                }
                onComplete = (Message) ar.userObj;
                if (onComplete != null) {
                    AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                    onComplete.sendToTarget();
                }
                break;

             default:
                 super.handleMessage(msg);
        }
    }

    private void processIccRecordEvents(int eventCode) {
        switch (eventCode) {
            case SIMRecords.EVENT_CFI:
                notifyCallForwardingIndicator();
                break;
            case SIMRecords.EVENT_MWI:
                notifyMessageWaitingIndicator();
                break;
        }
    }

   /**
     * Sets the "current" field in the telephony provider according to the SIM's operator
     *
     * @return true for success; false otherwise.
     */
    boolean updateCurrentCarrierInProvider() {
        if (mIccRecords != null) {
            try {
                Uri uri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
                ContentValues map = new ContentValues();
                map.put(Telephony.Carriers.NUMERIC, mIccRecords.getOperatorNumeric());
                mContext.getContentResolver().insert(uri, map);
                return true;
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Can't store current operator", e);
            }
        }
        return false;
    }

=======
    //***** Inner Classes

    class MyHandler extends Handler
    {
        MyHandler()
        {
        }

        MyHandler(Looper l)
        {
            super(l);
        }

        public void
        handleMessage (Message msg) 
        {
            AsyncResult ar;
            Message onComplete;

            switch (msg.what) {
                case EVENT_RADIO_AVAILABLE: {
                    mCM.getBasebandVersion(
                            obtainMessage(EVENT_GET_BASEBAND_VERSION_DONE));

                    mCM.getIMEI(obtainMessage(EVENT_GET_IMEI_DONE));
                    mCM.getIMEISV(obtainMessage(EVENT_GET_IMEISV_DONE));
                }
                break;

                case EVENT_RADIO_ON:
                break;

                case EVENT_REGISTERED_TO_NETWORK:
                    syncClirSetting();
                    break;

                case EVENT_SIM_RECORDS_LOADED:
                    mSIMRecords.getSIMOperatorNumeric();

                    try {
                        //set the current field the telephony provider according to
                        //the SIM's operator
                        Uri uri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
                        ContentValues map = new ContentValues();
                        map.put(Telephony.Carriers.NUMERIC, mSIMRecords.getSIMOperatorNumeric());
                        mContext.getContentResolver().insert(uri, map);
                    } catch (SQLException e) {
                        Log.e(LOG_TAG, "Can't store current operator", e);
                    }

                break;

                case EVENT_GET_BASEBAND_VERSION_DONE:
                    ar = (AsyncResult)msg.obj;

                    if (ar.exception != null) {
                        break;
                    }

                    if (LOCAL_DEBUG) Log.d(LOG_TAG, "Baseband version: " + ar.result);
                    setSystemProperty(PROPERTY_BASEBAND_VERSION, (String)ar.result);
                break;

                case EVENT_GET_IMEI_DONE:
                    ar = (AsyncResult)msg.obj;

                    if (ar.exception != null) {
                        break;
                    }

                    mImei = (String)ar.result;
                break;

                case EVENT_GET_IMEISV_DONE:
                    ar = (AsyncResult)msg.obj;

                    if (ar.exception != null) {
                        break;
                    }
                    
                    mImeiSv = (String)ar.result;
                break;


                case EVENT_USSD:
                    ar = (AsyncResult)msg.obj;

                    String[] ussdResult = (String[]) ar.result;

                    if (ussdResult.length > 1) {
                        try {
                            onIncomingUSSD(Integer.parseInt(ussdResult[0]), ussdResult[1]);
                        } catch (NumberFormatException e) {
                            Log.w(LOG_TAG, "error parsing USSD");
                        }
                    }
                break;

                case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:                
                    // Some MMI requests (eg USSD) are not completed
                    // within the course of a CommandsInterface request
                    // If the radio shuts off or resets while one of these
                    // is pending, we need to clean up.

                    for (int i = 0, s = mPendingMMIs.size() ; i < s; i++) {
                        if (mPendingMMIs.get(i).isPendingUSSD()) {
                            mPendingMMIs.get(i).onUssdFinishedError();
                        }                            
                    }
                break;
                
                case EVENT_SSN:
                    ar = (AsyncResult)msg.obj;
                    SuppServiceNotification not = (SuppServiceNotification) ar.result;
                    mSsnRegistrants.notifyRegistrants(ar);
                break;

                case EVENT_SET_CALL_FORWARD_DONE:
                    ar = (AsyncResult)msg.obj;
                    if (ar.exception == null) {
                        mSIMRecords.setVoiceCallForwardingFlag(1, msg.arg1 == 1);
                    }
                    onComplete = (Message) ar.userObj;
                    if (onComplete != null) {
                        AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                        onComplete.sendToTarget();
                    }
                    break;

                case EVENT_GET_CALL_FORWARD_DONE:
                    ar = (AsyncResult)msg.obj;
                    if (ar.exception == null) {
                        handleCfuQueryResult((CallForwardInfo[])ar.result);
                    }
                    onComplete = (Message) ar.userObj;
                    if (onComplete != null) {
                        AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                        onComplete.sendToTarget();
                    }
                    break;
                    
                case EVENT_CALL_RING:
                    ar = (AsyncResult)msg.obj;
                    if (ar.exception == null) {
                        notifyIncomingRing();
                    }
                    break;
                    
                // handle the select network completion callbacks.    
                case EVENT_SET_NETWORK_MANUAL_COMPLETE:
                case EVENT_SET_NETWORK_AUTOMATIC_COMPLETE:
                    handleSetSelectNetwork((AsyncResult) msg.obj);
                    break;

                case EVENT_SET_CLIR_COMPLETE:
                    ar = (AsyncResult)msg.obj;
                    if (ar.exception == null) {
                        saveClirSetting(msg.arg1);
                    }
                    onComplete = (Message) ar.userObj;
                    if (onComplete != null) {
                        AsyncResult.forMessage(onComplete, ar.result, ar.exception);
                        onComplete.sendToTarget();
                    }
                    break;
            }
        }
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * Used to track the settings upon completion of the network change.
     */
    private void handleSetSelectNetwork(AsyncResult ar) {
<<<<<<< HEAD
        // look for our wrapper within the asyncresult, skip the rest if it
        // is null.
=======
        // look for our wrapper within the asyncresult, skip the rest if it 
        // is null. 
>>>>>>> 54b6cfa... Initial Contribution
        if (!(ar.userObj instanceof NetworkSelectMessage)) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "unexpected result from user object.");
            return;
        }
<<<<<<< HEAD

        NetworkSelectMessage nsm = (NetworkSelectMessage) ar.userObj;

        // found the object, now we send off the message we had originally
        // attached to the request.
=======
        
        NetworkSelectMessage nsm = (NetworkSelectMessage) ar.userObj;
        
        // found the object, now we send off the message we had originally
        // attached to the request. 
>>>>>>> 54b6cfa... Initial Contribution
        if (nsm.message != null) {
            if (LOCAL_DEBUG) Log.d(LOG_TAG, "sending original message to recipient");
            AsyncResult.forMessage(nsm.message, ar.result, ar.exception);
            nsm.message.sendToTarget();
        }
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        // open the shared preferences editor, and write the value.
        // nsm.operatorNumeric is "" if we're in automatic.selection.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NETWORK_SELECTION_KEY, nsm.operatorNumeric);
<<<<<<< HEAD
        editor.putString(NETWORK_SELECTION_NAME_KEY, nsm.operatorAlphaLong);

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        // commit and log the result.
        if (! editor.commit()) {
            Log.e(LOG_TAG, "failed to commit network selection preference");
        }

    }

    /**
     * Saves CLIR setting so that we can re-apply it as necessary
     * (in case the RIL resets it across reboots).
     */
<<<<<<< HEAD
    public void saveClirSetting(int commandInterfaceCLIRMode) {
=======
    /* package */ void saveClirSetting(int commandInterfaceCLIRMode) {
>>>>>>> 54b6cfa... Initial Contribution
        // open the shared preferences editor, and write the value.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(CLIR_KEY, commandInterfaceCLIRMode);
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        // commit and log the result.
        if (! editor.commit()) {
            Log.e(LOG_TAG, "failed to commit CLIR preference");
        }
<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
    }

    private void handleCfuQueryResult(CallForwardInfo[] infos) {
        if (infos == null || infos.length == 0) {
            // Assume the default is not active
            // Set unconditional CFF in SIM to false
<<<<<<< HEAD
            mIccRecords.setVoiceCallForwardingFlag(1, false);
        } else {
            for (int i = 0, s = infos.length; i < s; i++) {
                if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                    mIccRecords.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
=======
            mSIMRecords.setVoiceCallForwardingFlag(1, false);
        } else {
            for (int i = 0, s = infos.length; i < s; i++) {
                if ((infos[i].serviceClass & SERVICE_CLASS_VOICE) != 0) {
                    mSIMRecords.setVoiceCallForwardingFlag(1, (infos[i].status == 1));
>>>>>>> 54b6cfa... Initial Contribution
                    // should only have the one
                    break;
                }
            }
        }
    }
<<<<<<< HEAD

    /**
     * Retrieves the PhoneSubInfo of the GSMPhone
     */
    public PhoneSubInfo getPhoneSubInfo(){
        return mSubInfo;
    }

    /**
     * Retrieves the IccSmsInterfaceManager of the GSMPhone
     */
    public IccSmsInterfaceManager getIccSmsInterfaceManager(){
        return mSimSmsIntManager;
    }

    /**
     * Retrieves the IccPhoneBookInterfaceManager of the GSMPhone
     */
    public IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager(){
        return mSimPhoneBookIntManager;
    }

    /**
     * Activate or deactivate cell broadcast SMS.
     *
     * @param activate 0 = activate, 1 = deactivate
     * @param response Callback message is empty on completion
     */
    public void activateCellBroadcastSms(int activate, Message response) {
        Log.e(LOG_TAG, "[GSMPhone] activateCellBroadcastSms() is obsolete; use SmsManager");
        response.sendToTarget();
    }

    /**
     * Query the current configuration of cdma cell broadcast SMS.
     *
     * @param response Callback message is empty on completion
     */
    public void getCellBroadcastSmsConfig(Message response) {
        Log.e(LOG_TAG, "[GSMPhone] getCellBroadcastSmsConfig() is obsolete; use SmsManager");
        response.sendToTarget();
    }

    /**
     * Configure cdma cell broadcast SMS.
     *
     * @param response Callback message is empty on completion
     */
    public void setCellBroadcastSmsConfig(int[] configValuesArray, Message response) {
        Log.e(LOG_TAG, "[GSMPhone] setCellBroadcastSmsConfig() is obsolete; use SmsManager");
        response.sendToTarget();
    }

    public boolean isCspPlmnEnabled() {
        return mIccRecords.isCspPlmnEnabled();
    }

    private void registerForSimRecordEvents() {
        mIccRecords.registerForNetworkSelectionModeAutomatic(
                this, EVENT_SET_NETWORK_AUTOMATIC, null);
        mIccRecords.registerForNewSms(this, EVENT_NEW_ICC_SMS, null);
        mIccRecords.registerForRecordsEvents(this, EVENT_ICC_RECORD_EVENTS, null);
        mIccRecords.registerForRecordsLoaded(this, EVENT_SIM_RECORDS_LOADED, null);
    }

    private void unregisterForSimRecordEvents() {
        mIccRecords.unregisterForNetworkSelectionModeAutomatic(this);
        mIccRecords.unregisterForNewSms(this);
        mIccRecords.unregisterForRecordsEvents(this);
        mIccRecords.unregisterForRecordsLoaded(this);
    }

    @Override
    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("GSMPhone extends:");
        super.dump(fd, pw, args);
        pw.println(" mCT=" + mCT);
        pw.println(" mSST=" + mSST);
        pw.println(" mPendingMMIs=" + mPendingMMIs);
        pw.println(" mSimPhoneBookIntManager=" + mSimPhoneBookIntManager);
        pw.println(" mSimSmsIntManager=" + mSimSmsIntManager);
        pw.println(" mSubInfo=" + mSubInfo);
        if (VDBG) pw.println(" mImei=" + mImei);
        if (VDBG) pw.println(" mImeiSv=" + mImeiSv);
        pw.println(" mVmNumber=" + mVmNumber);
=======
    /**
     * simulateDataConnection
     *
     * simulates various data connection states. This messes with
     * DataConnectionTracker's internal states, but doesn't actually change
     * the underlying radio connection states.
     * 
     * @param state Phone.DataState enum.
     */
    public void simulateDataConnection(Phone.DataState state) {
        DataConnectionTracker.State dcState;

        switch (state) {
            case CONNECTED:
                dcState = DataConnectionTracker.State.CONNECTED;
                break;
            case SUSPENDED:
                dcState = DataConnectionTracker.State.CONNECTED;
                break;
            case DISCONNECTED:
                dcState = DataConnectionTracker.State.FAILED;
                break;
            default:
                dcState = DataConnectionTracker.State.CONNECTING;
                break;
        }

        mDataConnection.setState(dcState);
        notifyDataConnection(null);
>>>>>>> 54b6cfa... Initial Contribution
    }
}
