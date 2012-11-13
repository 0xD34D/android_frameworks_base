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
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ALPHA;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_ISO_COUNTRY;
import static com.android.internal.telephony.TelephonyProperties.PROPERTY_ICC_OPERATOR_NUMERIC;
import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;

import com.android.internal.telephony.AdnRecord;
import com.android.internal.telephony.AdnRecordCache;
import com.android.internal.telephony.AdnRecordLoader;
import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;
import com.android.internal.telephony.IccUtils;
import com.android.internal.telephony.IccVmFixedException;
import com.android.internal.telephony.IccVmNotSupportedException;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.IccRefreshResponse;

import java.util.ArrayList;

=======
import android.os.AsyncResult;
import android.os.RegistrantList;
import android.os.Registrant;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import java.util.ArrayList;

import static com.android.internal.telephony.TelephonyProperties.*;
import com.android.internal.telephony.SimCard;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * {@hide}
 */
<<<<<<< HEAD
public class SIMRecords extends IccRecords {
    protected static final String LOG_TAG = "GSM";

    private static final boolean CRASH_RIL = false;

    protected static final boolean DBG = true;

    // ***** Instance Variables

    VoiceMailConstants mVmConfig;


    SpnOverride mSpnOverride;

    // ***** Cached SIM State; cleared on channel close

    private String imsi;
    private boolean callForwardingEnabled;


    /**
     * States only used by getSpnFsm FSM
=======
public final class SIMRecords extends Handler implements SimConstants
{
    static final String LOG_TAG = "GSM";

    private static final boolean CRASH_RIL = false;

    private static final boolean DBG = true;

    //***** Instance Variables

    GSMPhone phone;
    RegistrantList recordsLoadedRegistrants = new RegistrantList();

    int recordsToLoad;  // number of pending load requests

    AdnRecordCache adnCache;

    VoiceMailConstants mVmConfig;
    
    //***** Cached SIM State; cleared on channel close

    boolean recordsRequested = false; // true if we've made requests for the sim records

    String imsi;
    String iccid;
    String msisdn = null;  // My mobile number
    String msisdnTag = null;
    String voiceMailNum = null;
    String voiceMailTag = null;
    String newVoiceMailNum = null;
    String newVoiceMailTag = null;
    boolean isVoiceMailFixed = false;
    int countVoiceMessages = 0;
    boolean callForwardingEnabled;
    int mncLength = 0;   // 0 is used to indicate that the value
                         // is not initialized
    int mailboxIndex = 0; // 0 is no mailbox dailing number associated

    /**
     * Sates only used by getSpnFsm FSM
>>>>>>> 54b6cfa... Initial Contribution
     */
    private Get_Spn_Fsm_State spnState;

    /** CPHS service information (See CPHS 4.2 B.3.1.1)
     *  It will be set in onSimReady if reading GET_CPHS_INFO successfully
     *  mCphsInfo[0] is CPHS Phase
     *  mCphsInfo[1] and mCphsInfo[2] is CPHS Service Table
     */
    private byte[] mCphsInfo = null;
<<<<<<< HEAD
    boolean mCspPlmnEnabled = true;
=======
>>>>>>> 54b6cfa... Initial Contribution

    byte[] efMWIS = null;
    byte[] efCPHS_MWI =null;
    byte[] mEfCff = null;
    byte[] mEfCfis = null;


<<<<<<< HEAD
=======
    String spn;
>>>>>>> 54b6cfa... Initial Contribution
    int spnDisplayCondition;
    // Numeric network codes listed in TS 51.011 EF[SPDI]
    ArrayList<String> spdiNetworks = null;

    String pnnHomeName = null;

<<<<<<< HEAD
    UsimServiceTable mUsimServiceTable;

    // ***** Constants
=======
    //***** Constants
>>>>>>> 54b6cfa... Initial Contribution

    // Bitmasks for SPN display rules.
    static final int SPN_RULE_SHOW_SPN  = 0x01;
    static final int SPN_RULE_SHOW_PLMN = 0x02;

    // From TS 51.011 EF[SPDI] section
<<<<<<< HEAD
    static final int TAG_SPDI = 0xA3;
=======
>>>>>>> 54b6cfa... Initial Contribution
    static final int TAG_SPDI_PLMN_LIST = 0x80;

    // Full Name IEI from TS 24.008
    static final int TAG_FULL_NETWORK_NAME = 0x43;

    // Short Name IEI from TS 24.008
    static final int TAG_SHORT_NETWORK_NAME = 0x45;

    // active CFF from CPHS 4.2 B.4.5
    static final int CFF_UNCONDITIONAL_ACTIVE = 0x0a;
    static final int CFF_UNCONDITIONAL_DEACTIVE = 0x05;
    static final int CFF_LINE1_MASK = 0x0f;
    static final int CFF_LINE1_RESET = 0xf0;

    // CPHS Service Table (See CPHS 4.2 B.3.1)
    private static final int CPHS_SST_MBN_MASK = 0x30;
    private static final int CPHS_SST_MBN_ENABLED = 0x30;

<<<<<<< HEAD
    // ***** Event Constants

    private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
    protected static final int EVENT_GET_IMSI_DONE = 3;
    protected static final int EVENT_GET_ICCID_DONE = 4;
=======
    //***** Event Constants

    private static final int EVENT_SIM_READY = 1;
    private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 2;
    private static final int EVENT_GET_IMSI_DONE = 3;
    private static final int EVENT_GET_ICCID_DONE = 4;
>>>>>>> 54b6cfa... Initial Contribution
    private static final int EVENT_GET_MBI_DONE = 5;
    private static final int EVENT_GET_MBDN_DONE = 6;
    private static final int EVENT_GET_MWIS_DONE = 7;
    private static final int EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE = 8;
<<<<<<< HEAD
    protected static final int EVENT_GET_AD_DONE = 9; // Admin data on SIM
    protected static final int EVENT_GET_MSISDN_DONE = 10;
=======
    private static final int EVENT_GET_AD_DONE = 9; // Admin data on SIM
    private static final int EVENT_GET_MSISDN_DONE = 10;
>>>>>>> 54b6cfa... Initial Contribution
    private static final int EVENT_GET_CPHS_MAILBOX_DONE = 11;
    private static final int EVENT_GET_SPN_DONE = 12;
    private static final int EVENT_GET_SPDI_DONE = 13;
    private static final int EVENT_UPDATE_DONE = 14;
    private static final int EVENT_GET_PNN_DONE = 15;
<<<<<<< HEAD
    protected static final int EVENT_GET_SST_DONE = 17;
=======
    private static final int EVENT_GET_SST_DONE = 17;
>>>>>>> 54b6cfa... Initial Contribution
    private static final int EVENT_GET_ALL_SMS_DONE = 18;
    private static final int EVENT_MARK_SMS_READ_DONE = 19;
    private static final int EVENT_SET_MBDN_DONE = 20;
    private static final int EVENT_SMS_ON_SIM = 21;
    private static final int EVENT_GET_SMS_DONE = 22;
    private static final int EVENT_GET_CFF_DONE = 24;
    private static final int EVENT_SET_CPHS_MAILBOX_DONE = 25;
    private static final int EVENT_GET_INFO_CPHS_DONE = 26;
    private static final int EVENT_SET_MSISDN_DONE = 30;
    private static final int EVENT_SIM_REFRESH = 31;
    private static final int EVENT_GET_CFIS_DONE = 32;
<<<<<<< HEAD
    private static final int EVENT_GET_CSP_CPHS_DONE = 33;

    // Lookup table for carriers known to produce SIMs which incorrectly indicate MNC length.

    private static final String[] MCCMNC_CODES_HAVING_3DIGITS_MNC = {
        "405025", "405026", "405027", "405028", "405029", "405030", "405031", "405032",
        "405033", "405034", "405035", "405036", "405037", "405038", "405039", "405040",
        "405041", "405042", "405043", "405044", "405045", "405046", "405047", "405750",
        "405751", "405752", "405753", "405754", "405755", "405756", "405799", "405800",
        "405801", "405802", "405803", "405804", "405805", "405806", "405807", "405808",
        "405809", "405810", "405811", "405812", "405813", "405814", "405815", "405816",
        "405817", "405818", "405819", "405820", "405821", "405822", "405823", "405824",
        "405825", "405826", "405827", "405828", "405829", "405830", "405831", "405832",
        "405833", "405834", "405835", "405836", "405837", "405838", "405839", "405840",
        "405841", "405842", "405843", "405844", "405845", "405846", "405847", "405848",
        "405849", "405850", "405851", "405852", "405853", "405875", "405876", "405877",
        "405878", "405879", "405880", "405881", "405882", "405883", "405884", "405885",
        "405886", "405908", "405909", "405910", "405911", "405912", "405913", "405914",
        "405915", "405916", "405917", "405918", "405919", "405920", "405921", "405922",
        "405923", "405924", "405925", "405926", "405927", "405928", "405929", "405930",
        "405931", "405932"
    };

    // ***** Constructor

    public SIMRecords(IccCard card, Context c, CommandsInterface ci) {
        super(card, c, ci);

        adnCache = new AdnRecordCache(mFh);

        mVmConfig = new VoiceMailConstants();
        mSpnOverride = new SpnOverride();
=======

    //***** Constructor

    SIMRecords(GSMPhone phone)
    {
        this.phone = phone;

        adnCache = new AdnRecordCache(phone);

        mVmConfig = new VoiceMailConstants();
>>>>>>> 54b6cfa... Initial Contribution

        recordsRequested = false;  // No load request is made till SIM ready

        // recordsToLoad is set to 0 because no requests are made yet
        recordsToLoad = 0;

<<<<<<< HEAD
        mCi.registerForOffOrNotAvailable(
                        this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        mCi.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
        mCi.registerForIccRefresh(this, EVENT_SIM_REFRESH, null);

        // Start off by setting empty state
        onRadioOffOrNotAvailable();

    }

    @Override
    public void dispose() {
        if (DBG) log("Disposing SIMRecords " + this);
        //Unregister for all events
        mCi.unregisterForOffOrNotAvailable( this);
        mCi.unregisterForIccRefresh(this);
        mCi.unSetOnSmsOnSim(this);
        super.dispose();
    }

    protected void finalize() {
        if(DBG) log("finalized");
    }

    protected void onRadioOffOrNotAvailable() {
=======

        phone.mCM.registerForSIMReady(this, EVENT_SIM_READY, null);
        phone.mCM.registerForOffOrNotAvailable(
                        this, EVENT_RADIO_OFF_OR_NOT_AVAILABLE, null);
        phone.mCM.setOnSmsOnSim(this, EVENT_SMS_ON_SIM, null);
        phone.mCM.setOnSimRefresh(this, EVENT_SIM_REFRESH, null);

        // Start off by setting empty state
        onRadioOffOrNotAvailable();        

    }

    AdnRecordCache getAdnCache() {
        return adnCache;
    }

    private void onRadioOffOrNotAvailable()
    {
>>>>>>> 54b6cfa... Initial Contribution
        imsi = null;
        msisdn = null;
        voiceMailNum = null;
        countVoiceMessages = 0;
<<<<<<< HEAD
        mncLength = UNINITIALIZED;
        iccid = null;
        // -1 means no EF_SPN found; treat accordingly.
        spnDisplayCondition = -1;
        efMWIS = null;
        efCPHS_MWI = null;
=======
        mncLength = 0;
        iccid = null;
        spn = null;
        // -1 means no EF_SPN found; treat accordingly.
        spnDisplayCondition = -1;
        efMWIS = null;
        efCPHS_MWI = null; 
        spn = null;
>>>>>>> 54b6cfa... Initial Contribution
        spdiNetworks = null;
        pnnHomeName = null;

        adnCache.reset();

<<<<<<< HEAD
        log("SIMRecords: onRadioOffOrNotAvailable set 'gsm.sim.operator.numeric' to operator=null");
        SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, null);
        SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, null);
        SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY, null);
=======
        phone.setSystemProperty(PROPERTY_LINE1_VOICE_MAIL_WAITING, null);
        phone.setSystemProperty(PROPERTY_SIM_OPERATOR_NUMERIC, null);
        phone.setSystemProperty(PROPERTY_SIM_OPERATOR_ALPHA, null);
        phone.setSystemProperty(PROPERTY_SIM_OPERATOR_ISO_COUNTRY, null);
>>>>>>> 54b6cfa... Initial Contribution

        // recordsRequested is set to false indicating that the SIM
        // read requests made so far are not valid. This is set to
        // true only when fresh set of read requests are made.
        recordsRequested = false;
    }


    //***** Public Methods
<<<<<<< HEAD

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIMSI() {
        return imsi;
    }

    public String getMsisdnNumber() {
        return msisdn;
    }

    @Override
    public UsimServiceTable getUsimServiceTable() {
        return mUsimServiceTable;
=======
    public void registerForRecordsLoaded(Handler h, int what, Object obj)
    {
        Registrant r = new Registrant(h, what, obj);
        recordsLoadedRegistrants.add(r);

        if (recordsToLoad == 0 && recordsRequested == true) {
            r.notifyRegistrant(new AsyncResult(null, null, null));
        }
    }

    /** Returns null if SIM is not yet ready */
    public String getIMSI()
    {
        return imsi;
    }

    public String getMsisdnNumber()
    {
        return msisdn;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Set subscriber number to SIM record
     *
     * The subscriber number is stored in EF_MSISDN (TS 51.011)
     *
     * When the operation is complete, onComplete will be sent to its handler
     *
     * @param alphaTag alpha-tagging of the dailing nubmer (up to 10 characters)
     * @param number dailing nubmer (up to 20 digits)
     *        if the number starts with '+', then set to international TOA
     * @param onComplete
     *        onComplete.obj will be an AsyncResult
     *        ((AsyncResult)onComplete.obj).exception == null on success
     *        ((AsyncResult)onComplete.obj).exception != null on fail
     */
    public void setMsisdnNumber(String alphaTag, String number,
            Message onComplete) {

        msisdn = number;
        msisdnTag = alphaTag;

<<<<<<< HEAD
        if(DBG) log("Set MSISDN: " + msisdnTag + " " + /*msisdn*/ "xxxxxxx");
=======
        if(DBG) log("Set MSISDN: " + msisdnTag +" " + msisdn);
>>>>>>> 54b6cfa... Initial Contribution


        AdnRecord adn = new AdnRecord(msisdnTag, msisdn);

<<<<<<< HEAD
        new AdnRecordLoader(mFh).updateEF(adn, EF_MSISDN, EF_EXT1, 1, null,
=======
        new AdnRecordLoader(phone).updateEF(adn, EF_MSISDN, EF_EXT1, 1, null,
>>>>>>> 54b6cfa... Initial Contribution
                obtainMessage(EVENT_SET_MSISDN_DONE, onComplete));
    }

    public String getMsisdnAlphaTag() {
        return msisdnTag;
    }

<<<<<<< HEAD
    public String getVoiceMailNumber() {
=======
    public String getVoiceMailNumber()
    {
>>>>>>> 54b6cfa... Initial Contribution
        return voiceMailNum;
    }

    /**
<<<<<<< HEAD
=======
     * Return Service Provider Name stored in SIM
     * @return null if SIM is not yet ready
     */
    public String getServiceProvideName()
    {
        return spn;
    }

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Set voice mail number to SIM record
     *
     * The voice mail number can be stored either in EF_MBDN (TS 51.011) or
     * EF_MAILBOX_CPHS (CPHS 4.2)
     *
     * If EF_MBDN is available, store the voice mail number to EF_MBDN
<<<<<<< HEAD
     *
=======
     * 
>>>>>>> 54b6cfa... Initial Contribution
     * If EF_MAILBOX_CPHS is enabled, store the voice mail number to EF_CHPS
     *
     * So the voice mail number will be stored in both EFs if both are available
     *
     * Return error only if both EF_MBDN and EF_MAILBOX_CPHS fail.
     *
     * When the operation is complete, onComplete will be sent to its handler
     *
     * @param alphaTag alpha-tagging of the dailing nubmer (upto 10 characters)
     * @param voiceNumber dailing nubmer (upto 20 digits)
     *        if the number is start with '+', then set to international TOA
     * @param onComplete
     *        onComplete.obj will be an AsyncResult
     *        ((AsyncResult)onComplete.obj).exception == null on success
     *        ((AsyncResult)onComplete.obj).exception != null on fail
     */
    public void setVoiceMailNumber(String alphaTag, String voiceNumber,
            Message onComplete) {
        if (isVoiceMailFixed) {
            AsyncResult.forMessage((onComplete)).exception =
<<<<<<< HEAD
                    new IccVmFixedException("Voicemail number is fixed by operator");
=======
                    new SimException("Voicemail number is fixed by operator");
>>>>>>> 54b6cfa... Initial Contribution
            onComplete.sendToTarget();
            return;
        }

        newVoiceMailNum = voiceNumber;
        newVoiceMailTag = alphaTag;

        AdnRecord adn = new AdnRecord(newVoiceMailTag, newVoiceMailNum);

        if (mailboxIndex != 0 && mailboxIndex != 0xff) {

<<<<<<< HEAD
            new AdnRecordLoader(mFh).updateEF(adn, EF_MBDN, EF_EXT6,
=======
            new AdnRecordLoader(phone).updateEF(adn, EF_MBDN, EF_EXT6,
>>>>>>> 54b6cfa... Initial Contribution
                    mailboxIndex, null,
                    obtainMessage(EVENT_SET_MBDN_DONE, onComplete));

        } else if (isCphsMailboxEnabled()) {

<<<<<<< HEAD
            new AdnRecordLoader(mFh).updateEF(adn, EF_MAILBOX_CPHS,
=======
            new AdnRecordLoader(phone).updateEF(adn, EF_MAILBOX_CPHS,
>>>>>>> 54b6cfa... Initial Contribution
                    EF_EXT1, 1, null,
                    obtainMessage(EVENT_SET_CPHS_MAILBOX_DONE, onComplete));

        } else {
            AsyncResult.forMessage((onComplete)).exception =
<<<<<<< HEAD
                    new IccVmNotSupportedException("Update SIM voice mailbox error");
=======
                    new SimException("Update SIM voice mailbox error");
>>>>>>> 54b6cfa... Initial Contribution
            onComplete.sendToTarget();
        }
    }

    public String getVoiceMailAlphaTag()
    {
        return voiceMailTag;
    }

    /**
     * Sets the SIM voice message waiting indicator records
     * @param line GSM Subscriber Profile Number, one-based. Only '1' is supported
     * @param countWaiting The number of messages waiting, if known. Use
<<<<<<< HEAD
     *                     -1 to indicate that an unknown number of
     *                      messages are waiting
     */
    public void
    setVoiceMessageWaiting(int line, int countWaiting) {
=======
     *                     -1 to indicate that an unknown number of 
     *                      messages are waiting
     */
    public void
    setVoiceMessageWaiting(int line, int countWaiting)
    {
>>>>>>> 54b6cfa... Initial Contribution
        if (line != 1) {
            // only profile 1 is supported
            return;
        }

        // range check
        if (countWaiting < 0) {
            countWaiting = -1;
        } else if (countWaiting > 0xff) {
            // TS 23.040 9.2.3.24.2
            // "The value 255 shall be taken to mean 255 or greater"
            countWaiting = 0xff;
        }

        countVoiceMessages = countWaiting;

<<<<<<< HEAD
        mRecordsEventsRegistrants.notifyResult(EVENT_MWI);
=======
        phone.setSystemProperty(PROPERTY_LINE1_VOICE_MAIL_WAITING, 
                                (countVoiceMessages != 0) ? "true" : "false");

        phone.notifyMessageWaitingIndicator();
>>>>>>> 54b6cfa... Initial Contribution

        try {
            if (efMWIS != null) {
                // TS 51.011 10.3.45

                // lsb of byte 0 is 'voicemail' status
<<<<<<< HEAD
                efMWIS[0] = (byte)((efMWIS[0] & 0xfe)
=======
                efMWIS[0] = (byte)((efMWIS[0] & 0xfe) 
>>>>>>> 54b6cfa... Initial Contribution
                                    | (countVoiceMessages == 0 ? 0 : 1));

                // byte 1 is the number of voice messages waiting
                if (countWaiting < 0) {
                    // The spec does not define what this should be
                    // if we don't know the count
                    efMWIS[1] = 0;
                } else {
                    efMWIS[1] = (byte) countWaiting;
                }

<<<<<<< HEAD
                mFh.updateEFLinearFixed(
                    EF_MWIS, 1, efMWIS, null,
                    obtainMessage (EVENT_UPDATE_DONE, EF_MWIS));
            }

            if (efCPHS_MWI != null) {
                    // Refer CPHS4_2.WW6 B4.2.3
                efCPHS_MWI[0] = (byte)((efCPHS_MWI[0] & 0xf0)
                            | (countVoiceMessages == 0 ? 0x5 : 0xa));

                mFh.updateEFTransparent(
=======
                phone.mSIMFileHandler.updateEFLinearFixed(
                    EF_MWIS, 1, efMWIS, null,
                    obtainMessage (EVENT_UPDATE_DONE, EF_MWIS));
            } 

            if (efCPHS_MWI != null) {
                    // Refer CPHS4_2.WW6 B4.2.3
                efCPHS_MWI[0] = (byte)((efCPHS_MWI[0] & 0xf0) 
                            | (countVoiceMessages == 0 ? 0x5 : 0xa));

                phone.mSIMFileHandler.updateEFTransparent(
>>>>>>> 54b6cfa... Initial Contribution
                    EF_VOICE_MAIL_INDICATOR_CPHS, efCPHS_MWI,
                    obtainMessage (EVENT_UPDATE_DONE, EF_VOICE_MAIL_INDICATOR_CPHS));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
<<<<<<< HEAD
            logw("Error saving voice mail state to SIM. Probably malformed SIM record", ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
=======
            Log.w(LOG_TAG,
                "Error saving voice mail state to SIM. Probably malformed SIM record", ex);
        }
    }

    /** @return  true if there are messages waiting, false otherwise. */
    public boolean getVoiceMessageWaiting()
    {
        return countVoiceMessages != 0;
    }

    /**
     * Returns number of voice messages waiting, if available
     * If not available (eg, on an older CPHS SIM) -1 is returned if 
     * getVoiceMessageWaiting() is true
     */
    public int getCountVoiceMessages()
    {
        return countVoiceMessages;
    }

>>>>>>> 54b6cfa... Initial Contribution
    public boolean getVoiceCallForwardingFlag() {
        return callForwardingEnabled;
    }

<<<<<<< HEAD
    /**
     * {@inheritDoc}
     */
    @Override
=======
>>>>>>> 54b6cfa... Initial Contribution
    public void setVoiceCallForwardingFlag(int line, boolean enable) {

        if (line != 1) return; // only line 1 is supported

        callForwardingEnabled = enable;

<<<<<<< HEAD
        mRecordsEventsRegistrants.notifyResult(EVENT_CFI);
=======
        phone.setSystemProperty(PROPERTY_LINE1_VOICE_CALL_FORWARDING,
                (callForwardingEnabled ? "true" : "false"));

        phone.notifyCallForwardingIndicator();
>>>>>>> 54b6cfa... Initial Contribution

        try {
            if (mEfCfis != null) {
                // lsb is of byte 1 is voice status
                if (enable) {
                    mEfCfis[1] |= 1;
                } else {
                    mEfCfis[1] &= 0xfe;
                }

                // TODO: Should really update other fields in EF_CFIS, eg,
                // dialing number.  We don't read or use it right now.

<<<<<<< HEAD
                mFh.updateEFLinearFixed(
=======
                phone.mSIMFileHandler.updateEFLinearFixed(
>>>>>>> 54b6cfa... Initial Contribution
                        EF_CFIS, 1, mEfCfis, null,
                        obtainMessage (EVENT_UPDATE_DONE, EF_CFIS));
            }

            if (mEfCff != null) {
                if (enable) {
                    mEfCff[0] = (byte) ((mEfCff[0] & CFF_LINE1_RESET)
                            | CFF_UNCONDITIONAL_ACTIVE);
                } else {
                    mEfCff[0] = (byte) ((mEfCff[0] & CFF_LINE1_RESET)
                            | CFF_UNCONDITIONAL_DEACTIVE);
                }

<<<<<<< HEAD
                mFh.updateEFTransparent(
=======
                phone.mSIMFileHandler.updateEFTransparent(
>>>>>>> 54b6cfa... Initial Contribution
                        EF_CFF_CPHS, mEfCff,
                        obtainMessage (EVENT_UPDATE_DONE, EF_CFF_CPHS));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
<<<<<<< HEAD
            logw("Error saving call fowarding flag to SIM. "
=======
            Log.w(LOG_TAG,
                    "Error saving call fowarding flag to SIM. "
>>>>>>> 54b6cfa... Initial Contribution
                            + "Probably malformed SIM record", ex);

        }
    }

    /**
     * Called by STK Service when REFRESH is received.
     * @param fileChanged indicates whether any files changed
     * @param fileList if non-null, a list of EF files that changed
     */
    public void onRefresh(boolean fileChanged, int[] fileList) {
        if (fileChanged) {
            // A future optimization would be to inspect fileList and
            // only reload those files that we care about.  For now,
            // just re-fetch all SIM records that we cache.
            fetchSimRecords();
        }
    }

<<<<<<< HEAD
    /**
     * {@inheritDoc}
     */
    @Override
    public String getOperatorNumeric() {
        if (imsi == null) {
            log("getOperatorNumeric: IMSI == null");
            return null;
        }
        if (mncLength == UNINITIALIZED || mncLength == UNKNOWN) {
            log("getSIMOperatorNumeric: bad mncLength");
            return null;
        }

        // Length = length of MCC + length of MNC
        // length of mcc = 3 (TS 23.003 Section 2.2)
        return imsi.substring(0, 3 + mncLength);
    }

    // ***** Overridden from Handler
    public void handleMessage(Message msg) {
=======
    /** Returns the 5 or 6 digit MCC/MNC of the operator that
     *  provided the SIM card. Returns null of SIM is not yet ready
     */
    String getSIMOperatorNumeric()
    {
        if (imsi == null) {
            return null;
        }

        if (mncLength != 0) {
            // Length = length of MCC + length of MNC
            // length of mcc = 3 (TS 23.003 Section 2.2)
            return imsi.substring(0, 3 + mncLength);
        }

        // Guess the MNC length based on the MCC if we don't
        // have a valid value in ef[ad]

        int mcc;

        mcc = Integer.parseInt(imsi.substring(0,3));

        return imsi.substring(0, 3 + MccTable.smallestDigitsMccForMnc(mcc));
    }

    boolean getRecordsLoaded()
    {
        if (recordsToLoad == 0 && recordsRequested == true) {
            return true;
        } else {
            return false;
        }
    }

    //***** Overridden from Handler
    public void handleMessage(Message msg)
    {
>>>>>>> 54b6cfa... Initial Contribution
        AsyncResult ar;
        AdnRecord adn;

        byte data[];

        boolean isRecordLoadResponse = false;

<<<<<<< HEAD
        if (mDestroyed) {
            loge("Received message " + msg + "[" + msg.what + "] " +
                    " while being destroyed. Ignoring.");
            return;
        }

        try { switch (msg.what) {
            case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                onRadioOffOrNotAvailable();
            break;
=======
        try { switch (msg.what) {
            case EVENT_SIM_READY:
                onSimReady();
            break;

            case EVENT_RADIO_OFF_OR_NOT_AVAILABLE:
                onRadioOffOrNotAvailable();
            break;  
>>>>>>> 54b6cfa... Initial Contribution

            /* IO events */
            case EVENT_GET_IMSI_DONE:
                isRecordLoadResponse = true;
<<<<<<< HEAD

                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    loge("Exception querying IMSI, Exception:" + ar.exception);
                    break;
                }

=======
            
                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    Log.e(LOG_TAG, "Exception querying IMSI", ar.exception);
                    break;
                } 
                
>>>>>>> 54b6cfa... Initial Contribution
                imsi = (String) ar.result;

                // IMSI (MCC+MNC+MSIN) is at least 6 digits, but not more
                // than 15 (and usually 15).
                if (imsi != null && (imsi.length() < 6 || imsi.length() > 15)) {
<<<<<<< HEAD
                    loge("invalid IMSI " + imsi);
                    imsi = null;
                }

                log("IMSI: " + /* imsi.substring(0, 6) +*/ "xxxxxxx");

                if (((mncLength == UNKNOWN) || (mncLength == 2)) &&
                        ((imsi != null) && (imsi.length() >= 6))) {
                    String mccmncCode = imsi.substring(0, 6);
                    for (String mccmnc : MCCMNC_CODES_HAVING_3DIGITS_MNC) {
                        if (mccmnc.equals(mccmncCode)) {
                            mncLength = 3;
                            break;
                        }
                    }
                }

                if (mncLength == UNKNOWN) {
                    // the SIM has told us all it knows, but it didn't know the mnc length.
                    // guess using the mcc
                    try {
                        int mcc = Integer.parseInt(imsi.substring(0,3));
                        mncLength = MccTable.smallestDigitsMccForMnc(mcc);
                    } catch (NumberFormatException e) {
                        mncLength = UNKNOWN;
                        loge("Corrupt IMSI!");
                    }
                }

                if (mncLength != UNKNOWN && mncLength != UNINITIALIZED) {
                    // finally have both the imsi and the mncLength and can parse the imsi properly
                    MccTable.updateMccMncConfiguration(mContext, imsi.substring(0, 3 + mncLength));
                }
                mParentCard.broadcastIccStateChangedIntent(
                        IccCard.INTENT_VALUE_ICC_IMSI, null);
=======
                    Log.e(LOG_TAG, "invalid IMSI " + imsi);
                    imsi = null;
                }
                
                Log.d(LOG_TAG, "IMSI: " + imsi.substring(0, 6) + "xxxxxxxxx");
                phone.mSimCard.updateImsiConfiguration(imsi);
                phone.mSimCard.broadcastSimStateChangedIntent(
                        SimCard.INTENT_VALUE_SIM_IMSI, null);
>>>>>>> 54b6cfa... Initial Contribution
            break;

            case EVENT_GET_MBI_DONE:
                boolean isValidMbdn;
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[]) ar.result;

                isValidMbdn = false;
                if (ar.exception == null) {
                    // Refer TS 51.011 Section 10.3.44 for content details
<<<<<<< HEAD
                    log("EF_MBI: " + IccUtils.bytesToHexString(data));
=======
                    Log.d(LOG_TAG, "EF_MBI: " +
                            SimUtils.bytesToHexString(data));
>>>>>>> 54b6cfa... Initial Contribution

                    // Voice mail record number stored first
                    mailboxIndex = (int)data[0] & 0xff;

                    // check if dailing numbe id valid
                    if (mailboxIndex != 0 && mailboxIndex != 0xff) {
<<<<<<< HEAD
                        log("Got valid mailbox number for MBDN");
=======
                        Log.d(LOG_TAG, "Got valid mailbox number for MBDN");
>>>>>>> 54b6cfa... Initial Contribution
                        isValidMbdn = true;
                    }
                }

                // one more record to load
                recordsToLoad += 1;

                if (isValidMbdn) {
                    // Note: MBDN was not included in NUM_OF_SIM_RECORDS_LOADED
<<<<<<< HEAD
                    new AdnRecordLoader(mFh).loadFromEF(EF_MBDN, EF_EXT6,
=======
                    new AdnRecordLoader(phone).loadFromEF(EF_MBDN, EF_EXT6,
>>>>>>> 54b6cfa... Initial Contribution
                            mailboxIndex, obtainMessage(EVENT_GET_MBDN_DONE));
                } else {
                    // If this EF not present, try mailbox as in CPHS standard
                    // CPHS (CPHS4_2.WW6) is a european standard.
<<<<<<< HEAD
                    new AdnRecordLoader(mFh).loadFromEF(EF_MAILBOX_CPHS,
=======
                    new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS,
>>>>>>> 54b6cfa... Initial Contribution
                            EF_EXT1, 1,
                            obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
                }

                break;
            case EVENT_GET_CPHS_MAILBOX_DONE:
            case EVENT_GET_MBDN_DONE:
<<<<<<< HEAD
                //Resetting the voice mail number and voice mail tag to null
                //as these should be updated from the data read from EF_MBDN.
                //If they are not reset, incase of invalid data/exception these
                //variables are retaining their previous values and are
                //causing invalid voice mailbox info display to user.
                voiceMailNum = null;
                voiceMailTag = null;
=======
>>>>>>> 54b6cfa... Initial Contribution
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
<<<<<<< HEAD

                    log("Invalid or missing EF"
=======
                
                    Log.d(LOG_TAG, "Invalid or missing EF" 
>>>>>>> 54b6cfa... Initial Contribution
                        + ((msg.what == EVENT_GET_CPHS_MAILBOX_DONE) ? "[MAILBOX]" : "[MBDN]"));

                    // Bug #645770 fall back to CPHS
                    // FIXME should use SST to decide

                    if (msg.what == EVENT_GET_MBDN_DONE) {
<<<<<<< HEAD
                        //load CPHS on fail...
                        // FIXME right now, only load line1's CPHS voice mail entry

                        recordsToLoad += 1;
                        new AdnRecordLoader(mFh).loadFromEF(
                                EF_MAILBOX_CPHS, EF_EXT1, 1,
=======
                        //load CPHS on fail... 
                        // FIXME right now, only load line1's CPHS voice mail entry

                        recordsToLoad += 1;
                        new AdnRecordLoader(phone).loadFromEF(
                                EF_MAILBOX_CPHS, EF_EXT1, 1, 
>>>>>>> 54b6cfa... Initial Contribution
                                obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
                    }
                    break;
                }

                adn = (AdnRecord)ar.result;

<<<<<<< HEAD
                log("VM: " + adn +
                        ((msg.what == EVENT_GET_CPHS_MAILBOX_DONE) ? " EF[MAILBOX]" : " EF[MBDN]"));
=======
                Log.d(LOG_TAG, "VM: " + adn + ((msg.what == EVENT_GET_CPHS_MAILBOX_DONE) ? " EF[MAILBOX]" : " EF[MBDN]"));
>>>>>>> 54b6cfa... Initial Contribution

                if (adn.isEmpty() && msg.what == EVENT_GET_MBDN_DONE) {
                    // Bug #645770 fall back to CPHS
                    // FIXME should use SST to decide
                    // FIXME right now, only load line1's CPHS voice mail entry
                    recordsToLoad += 1;
<<<<<<< HEAD
                    new AdnRecordLoader(mFh).loadFromEF(
                            EF_MAILBOX_CPHS, EF_EXT1, 1,
=======
                    new AdnRecordLoader(phone).loadFromEF(
                            EF_MAILBOX_CPHS, EF_EXT1, 1, 
>>>>>>> 54b6cfa... Initial Contribution
                            obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));

                    break;
                }

                voiceMailNum = adn.getNumber();
                voiceMailTag = adn.getAlphaTag();
            break;

            case EVENT_GET_MSISDN_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
<<<<<<< HEAD
                    log("Invalid or missing EF[MSISDN]");
=======
                    Log.d(LOG_TAG, "Invalid or missing EF[MSISDN]");
>>>>>>> 54b6cfa... Initial Contribution
                    break;
                }

                adn = (AdnRecord)ar.result;

                msisdn = adn.getNumber();
                msisdnTag = adn.getAlphaTag();

<<<<<<< HEAD
                log("MSISDN: " + /*msisdn*/ "xxxxxxx");
=======
                Log.d(LOG_TAG, "MSISDN: " + msisdn);
>>>>>>> 54b6cfa... Initial Contribution
            break;

            case EVENT_SET_MSISDN_DONE:
                isRecordLoadResponse = false;
                ar = (AsyncResult)msg.obj;

                if (ar.userObj != null) {
                    AsyncResult.forMessage(((Message) ar.userObj)).exception
                            = ar.exception;
                    ((Message) ar.userObj).sendToTarget();
                }
                break;

            case EVENT_GET_MWIS_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

<<<<<<< HEAD
                log("EF_MWIS: " + IccUtils.bytesToHexString(data));
=======
                Log.d(LOG_TAG, "EF_MWIS: " +
                   SimUtils.bytesToHexString(data));
>>>>>>> 54b6cfa... Initial Contribution

                efMWIS = data;

                if ((data[0] & 0xff) == 0xff) {
<<<<<<< HEAD
                    log("Uninitialized record MWIS");
=======
                    Log.d(LOG_TAG, "SIMRecords: Uninitialized record MWIS");
>>>>>>> 54b6cfa... Initial Contribution
                    break;
                }

                // Refer TS 51.011 Section 10.3.45 for the content description
                boolean voiceMailWaiting = ((data[0] & 0x01) != 0);
                countVoiceMessages = data[1] & 0xff;

                if (voiceMailWaiting && countVoiceMessages == 0) {
<<<<<<< HEAD
                    // Unknown count = -1
                    countVoiceMessages = -1;
                }

                mRecordsEventsRegistrants.notifyResult(EVENT_MWI);
=======
                    // Unknown count = -1 
                    countVoiceMessages = -1;
                }

                phone.setSystemProperty(PROPERTY_LINE1_VOICE_MAIL_WAITING, 
                                        voiceMailWaiting ? "true" : "false");
                phone.notifyMessageWaitingIndicator();
>>>>>>> 54b6cfa... Initial Contribution
            break;

            case EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

                efCPHS_MWI = data;

                // Use this data if the EF[MWIS] exists and
                // has been loaded

                if (efMWIS == null) {
                    int indicator = (int)(data[0] & 0xf);

                    // Refer CPHS4_2.WW6 B4.2.3
                    if (indicator == 0xA) {
                        // Unknown count = -1
                        countVoiceMessages = -1;
                    } else if (indicator == 0x5) {
                        countVoiceMessages = 0;
                    }

<<<<<<< HEAD
                    mRecordsEventsRegistrants.notifyResult(EVENT_MWI);
=======
                    phone.setSystemProperty(PROPERTY_LINE1_VOICE_MAIL_WAITING, 
                                            countVoiceMessages != 0 
                                                ? "true" : "false");
                    phone.notifyMessageWaitingIndicator();
>>>>>>> 54b6cfa... Initial Contribution
                }
            break;

            case EVENT_GET_ICCID_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;
<<<<<<< HEAD

                if (ar.exception != null) {
                    break;
                }

                iccid = IccUtils.bcdToString(data, 0, data.length);

                log("iccid: " + iccid);
=======
                
                if (ar.exception != null) {
                    break;
                }                

                iccid = SimUtils.bcdToString(data, 0, data.length);
            
                Log.d(LOG_TAG, "iccid: " + iccid);
>>>>>>> 54b6cfa... Initial Contribution

            break;


            case EVENT_GET_AD_DONE:
<<<<<<< HEAD
                try {
                    isRecordLoadResponse = true;

                    ar = (AsyncResult)msg.obj;
                    data = (byte[])ar.result;

                    if (ar.exception != null) {
                        break;
                    }

                    log("EF_AD: " + IccUtils.bytesToHexString(data));

                    if (data.length < 3) {
                        log("Corrupt AD data on SIM");
                        break;
                    }

                    if (data.length == 3) {
                        log("MNC length not present in EF_AD");
                        break;
                    }

                    mncLength = (int)data[3] & 0xf;

                    if (mncLength == 0xf) {
                        mncLength = UNKNOWN;
                    }
                } finally {
                    if (((mncLength == UNINITIALIZED) || (mncLength == UNKNOWN) ||
                            (mncLength == 2)) && ((imsi != null) && (imsi.length() >= 6))) {
                        String mccmncCode = imsi.substring(0, 6);
                        for (String mccmnc : MCCMNC_CODES_HAVING_3DIGITS_MNC) {
                            if (mccmnc.equals(mccmncCode)) {
                                mncLength = 3;
                                break;
                            }
                        }
                    }

                    if (mncLength == UNKNOWN || mncLength == UNINITIALIZED) {
                        if (imsi != null) {
                            try {
                                int mcc = Integer.parseInt(imsi.substring(0,3));

                                mncLength = MccTable.smallestDigitsMccForMnc(mcc);
                            } catch (NumberFormatException e) {
                                mncLength = UNKNOWN;
                                loge("Corrupt IMSI!");
                            }
                        } else {
                            // Indicate we got this info, but it didn't contain the length.
                            mncLength = UNKNOWN;

                            log("MNC length not present in EF_AD");
                        }
                    }
                    if (imsi != null && mncLength != UNKNOWN) {
                        // finally have both imsi and the length of the mnc and can parse
                        // the imsi properly
                        MccTable.updateMccMncConfiguration(mContext,
                                imsi.substring(0, 3 + mncLength));
                    }
                }
=======
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

                Log.d(LOG_TAG, "EF_AD: " +
                    SimUtils.bytesToHexString(data));

                if (data.length < 3) {
                    Log.d(LOG_TAG, "SIMRecords: Corrupt AD data on SIM");
                    break;
                }

                if (data.length == 3) {
                    Log.d(LOG_TAG, "SIMRecords: MNC length not present in EF_AD");
                    break;
                }

                mncLength = (int)data[3] & 0xf;

                if (mncLength == 0xf) {
                    // Resetting mncLength to 0 to indicate that it is not
                    // initialised
                    mncLength = 0;

                    Log.d(LOG_TAG, "SIMRecords: MNC length not present in EF_AD");
                    break;
                }

>>>>>>> 54b6cfa... Initial Contribution
            break;

            case EVENT_GET_SPN_DONE:
                isRecordLoadResponse = true;
                ar = (AsyncResult) msg.obj;
                getSpnFsm(false, ar);
            break;

            case EVENT_GET_CFF_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult) msg.obj;
                data = (byte[]) ar.result;

                if (ar.exception != null) {
                    break;
                }

<<<<<<< HEAD
                log("EF_CFF_CPHS: " + IccUtils.bytesToHexString(data));
=======
                Log.d(LOG_TAG, "EF_CFF_CPHS: " +
                        SimUtils.bytesToHexString(data));
>>>>>>> 54b6cfa... Initial Contribution
                mEfCff = data;

                if (mEfCfis == null) {
                    callForwardingEnabled =
                        ((data[0] & CFF_LINE1_MASK) == CFF_UNCONDITIONAL_ACTIVE);

<<<<<<< HEAD
                    mRecordsEventsRegistrants.notifyResult(EVENT_CFI);
=======
                    phone.setSystemProperty(PROPERTY_LINE1_VOICE_CALL_FORWARDING,
                            (callForwardingEnabled ? "true" : "false"));

                    phone.notifyCallForwardingIndicator();
>>>>>>> 54b6cfa... Initial Contribution
                }
                break;

            case EVENT_GET_SPDI_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

<<<<<<< HEAD
                parseEfSpdi(data);
=======
                parseEfSpdi(data);                                
>>>>>>> 54b6cfa... Initial Contribution
            break;

            case EVENT_UPDATE_DONE:
                ar = (AsyncResult)msg.obj;
                if (ar.exception != null) {
<<<<<<< HEAD
                    logw("update failed. ", ar.exception);
=======
                    Log.i(LOG_TAG, "SIMRecords update failed", ar.exception);
>>>>>>> 54b6cfa... Initial Contribution
                }
            break;

            case EVENT_GET_PNN_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

                SimTlv tlv = new SimTlv(data, 0, data.length);

                for ( ; tlv.isValidObject() ; tlv.nextObject()) {
                    if (tlv.getTag() == TAG_FULL_NETWORK_NAME) {
<<<<<<< HEAD
                        pnnHomeName
                            = IccUtils.networkNameToString(
=======
                        pnnHomeName 
                            = SimUtils.networkNameToString(
>>>>>>> 54b6cfa... Initial Contribution
                                tlv.getData(), 0, tlv.getData().length);
                        break;
                    }
                }
            break;

            case EVENT_GET_ALL_SMS_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                if (ar.exception != null)
                    break;

                handleSmses((ArrayList) ar.result);
                break;

            case EVENT_MARK_SMS_READ_DONE:
                Log.i("ENF", "marked read: sms " + msg.arg1);
                break;


            case EVENT_SMS_ON_SIM:
                isRecordLoadResponse = false;

                ar = (AsyncResult)msg.obj;

                int[] index = (int[])ar.result;

                if (ar.exception != null || index.length != 1) {
<<<<<<< HEAD
                    loge("Error on SMS_ON_SIM with exp "
                            + ar.exception + " length " + index.length);
                } else {
                    log("READ EF_SMS RECORD index=" + index[0]);
                    mFh.loadEFLinearFixed(EF_SMS,index[0],
                            obtainMessage(EVENT_GET_SMS_DONE));
=======
                    Log.e(LOG_TAG, "[SIMRecords] Error on SMS_ON_SIM with exp "
                            + ar.exception + " length " + index.length);
                } else {
                    Log.d(LOG_TAG, "READ EF_SMS RECORD index=" + index[0]);
                    phone.mSIMFileHandler.loadEFLinearFixed(EF_SMS,index[0],obtainMessage(EVENT_GET_SMS_DONE));
>>>>>>> 54b6cfa... Initial Contribution
                }
                break;

            case EVENT_GET_SMS_DONE:
                isRecordLoadResponse = false;
                ar = (AsyncResult)msg.obj;
                if (ar.exception == null) {
                    handleSms((byte[])ar.result);
                } else {
<<<<<<< HEAD
                    loge("Error on GET_SMS with exp " + ar.exception);
=======
                    Log.e(LOG_TAG, "[SIMRecords] Error on GET_SMS with exp "
                            + ar.exception);
>>>>>>> 54b6cfa... Initial Contribution
                }
                break;
            case EVENT_GET_SST_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

<<<<<<< HEAD
                mUsimServiceTable = new UsimServiceTable(data);
                if (DBG) log("SST: " + mUsimServiceTable);
                break;
=======
                //Log.d(LOG_TAG, "SST: " + SimUtils.bytesToHexString(data));
            break;
>>>>>>> 54b6cfa... Initial Contribution

            case EVENT_GET_INFO_CPHS_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    break;
                }

                mCphsInfo = (byte[])ar.result;

<<<<<<< HEAD
                if (DBG) log("iCPHS: " + IccUtils.bytesToHexString(mCphsInfo));
=======
                if (DBG) log("iCPHS: " + SimUtils.bytesToHexString(mCphsInfo));
>>>>>>> 54b6cfa... Initial Contribution
            break;

            case EVENT_SET_MBDN_DONE:
                isRecordLoadResponse = false;
                ar = (AsyncResult)msg.obj;

                if (ar.exception == null) {
                    voiceMailNum = newVoiceMailNum;
                    voiceMailTag = newVoiceMailTag;
                }

                if (isCphsMailboxEnabled()) {
                    adn = new AdnRecord(voiceMailTag, voiceMailNum);
                    Message onCphsCompleted = (Message) ar.userObj;

                    /* write to cphs mailbox whenever it is available but
                    * we only need notify caller once if both updating are
                    * successful.
                    *
                    * so if set_mbdn successful, notify caller here and set
                    * onCphsCompleted to null
                    */
                    if (ar.exception == null && ar.userObj != null) {
                        AsyncResult.forMessage(((Message) ar.userObj)).exception
                                = null;
                        ((Message) ar.userObj).sendToTarget();

                        if (DBG) log("Callback with MBDN successful.");

                        onCphsCompleted = null;
                    }

<<<<<<< HEAD
                    new AdnRecordLoader(mFh).
=======
                    new AdnRecordLoader(phone).
>>>>>>> 54b6cfa... Initial Contribution
                            updateEF(adn, EF_MAILBOX_CPHS, EF_EXT1, 1, null,
                            obtainMessage(EVENT_SET_CPHS_MAILBOX_DONE,
                                    onCphsCompleted));
                } else {
                    if (ar.userObj != null) {
                        AsyncResult.forMessage(((Message) ar.userObj)).exception
                                = ar.exception;
                        ((Message) ar.userObj).sendToTarget();
                    }
                }
                break;
            case EVENT_SET_CPHS_MAILBOX_DONE:
                isRecordLoadResponse = false;
                ar = (AsyncResult)msg.obj;
                if(ar.exception == null) {
                    voiceMailNum = newVoiceMailNum;
                    voiceMailTag = newVoiceMailTag;
                } else {
                    if (DBG) log("Set CPHS MailBox with exception: "
                            + ar.exception);
                }
                if (ar.userObj != null) {
                    if (DBG) log("Callback with CPHS MB successful.");
                    AsyncResult.forMessage(((Message) ar.userObj)).exception
                            = ar.exception;
                    ((Message) ar.userObj).sendToTarget();
                }
                break;
            case EVENT_SIM_REFRESH:
                isRecordLoadResponse = false;
                ar = (AsyncResult)msg.obj;
<<<<<<< HEAD
                if (DBG) log("Sim REFRESH with exception: " + ar.exception);
                if (ar.exception == null) {
                    handleSimRefresh((IccRefreshResponse)ar.result);
=======
                if (ar.exception == null) {
                    handleSimRefresh((int[])(ar.result));
>>>>>>> 54b6cfa... Initial Contribution
                }
                break;
            case EVENT_GET_CFIS_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data = (byte[])ar.result;

                if (ar.exception != null) {
                    break;
                }

<<<<<<< HEAD
                log("EF_CFIS: " + IccUtils.bytesToHexString(data));

                mEfCfis = data;

                // Refer TS 51.011 Section 10.3.46 for the content description
                callForwardingEnabled = ((data[1] & 0x01) != 0);

                mRecordsEventsRegistrants.notifyResult(EVENT_CFI);
                break;

            case EVENT_GET_CSP_CPHS_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;

                if (ar.exception != null) {
                    loge("Exception in fetching EF_CSP data " + ar.exception);
                    break;
                }

                data = (byte[])ar.result;

                log("EF_CSP: " + IccUtils.bytesToHexString(data));
                handleEfCspData(data);
                break;

            default:
                super.handleMessage(msg);   // IccRecords handles generic record load responses

        }}catch (RuntimeException exc) {
            // I don't want these exceptions to be fatal
            logw("Exception parsing SIM record", exc);
        } finally {
=======
                Log.d(LOG_TAG, "EF_CFIS: " +
                   SimUtils.bytesToHexString(data));

                mEfCfis = data;
                
                // Refer TS 51.011 Section 10.3.46 for the content description
                callForwardingEnabled = ((data[1] & 0x01) != 0);

                phone.setSystemProperty(PROPERTY_LINE1_VOICE_CALL_FORWARDING,
                        (callForwardingEnabled ? "true" : "false"));

                phone.notifyCallForwardingIndicator();
                break;

        }}catch (RuntimeException exc) {
            // I don't want these exceptions to be fatal
            Log.w(LOG_TAG, "Exception parsing SIM record", exc);
        } finally {        
>>>>>>> 54b6cfa... Initial Contribution
            // Count up record load responses even if they are fails
            if (isRecordLoadResponse) {
                onRecordLoaded();
            }
        }
    }

    private void handleFileUpdate(int efid) {
        switch(efid) {
            case EF_MBDN:
                recordsToLoad++;
<<<<<<< HEAD
                new AdnRecordLoader(mFh).loadFromEF(EF_MBDN, EF_EXT6,
                        mailboxIndex, obtainMessage(EVENT_GET_MBDN_DONE));
                break;
            case EF_MAILBOX_CPHS:
                recordsToLoad++;
                new AdnRecordLoader(mFh).loadFromEF(EF_MAILBOX_CPHS, EF_EXT1,
                        1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));
                break;
            case EF_CSP_CPHS:
                recordsToLoad++;
                log("[CSP] SIM Refresh for EF_CSP_CPHS");
                mFh.loadEFTransparent(EF_CSP_CPHS,
                        obtainMessage(EVENT_GET_CSP_CPHS_DONE));
=======
                new AdnRecordLoader(phone).loadFromEF(EF_MBDN, EF_EXT6,
                        mailboxIndex, obtainMessage(EVENT_GET_MBDN_DONE));            
                break;
            case EF_MAILBOX_CPHS:
                recordsToLoad++;
                new AdnRecordLoader(phone).loadFromEF(EF_MAILBOX_CPHS, EF_EXT1,
                        1, obtainMessage(EVENT_GET_CPHS_MAILBOX_DONE));          
>>>>>>> 54b6cfa... Initial Contribution
                break;
            default:
                // For now, fetch all records if this is not a
                // voicemail number.
                // TODO: Handle other cases, instead of fetching all.
                adnCache.reset();
                fetchSimRecords();
                break;
        }
    }

<<<<<<< HEAD
    private void handleSimRefresh(IccRefreshResponse refreshResponse){
        if (refreshResponse == null) {
            if (DBG) log("handleSimRefresh received without input");
            return;
        }

        if (refreshResponse.aid != null &&
                !refreshResponse.aid.equals(mParentCard.getAid())) {
            // This is for different app. Ignore.
            return;
        }

        switch (refreshResponse.refreshResult) {
            case IccRefreshResponse.REFRESH_RESULT_FILE_UPDATE:
                if (DBG) log("handleSimRefresh with SIM_FILE_UPDATED");
                handleFileUpdate(refreshResponse.efId);
                break;
            case IccRefreshResponse.REFRESH_RESULT_INIT:
                if (DBG) log("handleSimRefresh with SIM_REFRESH_INIT");
                // need to reload all files (that we care about)
                adnCache.reset();
                fetchSimRecords();
                break;
            case IccRefreshResponse.REFRESH_RESULT_RESET:
                if (DBG) log("handleSimRefresh with SIM_REFRESH_RESET");
                mCi.setRadioPower(false, null);
=======
    private void handleSimRefresh(int[] result) {
        if (result == null || result.length == 0) {
            return;
        }
        
        switch ((result[0])) {
            case CommandsInterface.SIM_REFRESH_FILE_UPDATED:
                // result[1] contains the EFID of the updated file.
                int efid = result[1];
                handleFileUpdate(efid);
                break;
            case CommandsInterface.SIM_REFRESH_INIT:
                // need to reload all files (that we care about)
                fetchSimRecords();
                break;
            case CommandsInterface.SIM_REFRESH_RESET:
                phone.mCM.setRadioPower(false, null);
>>>>>>> 54b6cfa... Initial Contribution
                /* Note: no need to call setRadioPower(true).  Assuming the desired
                * radio power state is still ON (as tracked by ServiceStateTracker),
                * ServiceStateTracker will call setRadioPower when it receives the
                * RADIO_STATE_CHANGED notification for the power off.  And if the
                * desired power state has changed in the interim, we don't want to
                * override it with an unconditional power on.
                */
                break;
            default:
                // unknown refresh operation
<<<<<<< HEAD
                if (DBG) log("handleSimRefresh with unknown operation");
=======
>>>>>>> 54b6cfa... Initial Contribution
                break;
        }
    }

<<<<<<< HEAD
    /**
     * Dispatch 3GPP format message. Overridden for CDMA/LTE phones by
     * {@link com.android.internal.telephony.cdma.CdmaLteUiccRecords}
     * to send messages to the secondary 3GPP format SMS dispatcher.
     */
    protected int dispatchGsmMessage(SmsMessageBase message) {
        mNewSmsRegistrants.notifyResult(message);
        return 0;
    }

    private void handleSms(byte[] ba) {
=======
    private void handleSms(byte[] ba)
    {
>>>>>>> 54b6cfa... Initial Contribution
        if (ba[0] != 0)
            Log.d("ENF", "status : " + ba[0]);

        // 3GPP TS 51.011 v5.0.0 (20011-12)  10.5.3
        // 3 == "received by MS from network; message to be read"
        if (ba[0] == 3) {
            int n = ba.length;

            // Note: Data may include trailing FF's.  That's OK; message
            // should still parse correctly.
<<<<<<< HEAD
            byte[] pdu = new byte[n - 1];
            System.arraycopy(ba, 1, pdu, 0, n - 1);
            SmsMessage message = SmsMessage.createFromPdu(pdu);

            dispatchGsmMessage(message);
=======
            byte[] nba = new byte[n - 1];
            System.arraycopy(ba, 1, nba, 0, n - 1);

            String pdu = SimUtils.bytesToHexString(nba);
            // XXX first line is bogus
            SmsMessage message = SmsMessage.newFromCMT(
                                new String[] { "", pdu });

            Log.i("ENF", "message from " +
                message.getOriginatingAddress());
            Log.i("ENF", "message text " +
                message.getMessageBody());

            phone.mSMS.dispatchMessage(message);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }


    private void handleSmses(ArrayList messages) {
        int count = messages.size();

        for (int i = 0; i < count; i++) {
            byte[] ba = (byte[]) messages.get(i);

            if (ba[0] != 0)
                Log.i("ENF", "status " + i + ": " + ba[0]);

            // 3GPP TS 51.011 v5.0.0 (20011-12)  10.5.3
            // 3 == "received by MS from network; message to be read"

            if (ba[0] == 3) {
                int n = ba.length;

                // Note: Data may include trailing FF's.  That's OK; message
                // should still parse correctly.
<<<<<<< HEAD
                byte[] pdu = new byte[n - 1];
                System.arraycopy(ba, 1, pdu, 0, n - 1);
                SmsMessage message = SmsMessage.createFromPdu(pdu);

                dispatchGsmMessage(message);
=======
                byte[] nba = new byte[n - 1];
                System.arraycopy(ba, 1, nba, 0, n - 1);

                String pdu = SimUtils.bytesToHexString(nba);
                // XXX first line is bogus
                SmsMessage message = SmsMessage.newFromCMT(
                        new String[] { "", pdu });

                Log.i("ENF", "message from " +
                    message.getOriginatingAddress());
                Log.i("ENF", "message text " +
                    message.getMessageBody());

                phone.mSMS.dispatchMessage(message);
>>>>>>> 54b6cfa... Initial Contribution

                // 3GPP TS 51.011 v5.0.0 (20011-12)  10.5.3
                // 1 == "received by MS from network; message read"

                ba[0] = 1;

                if (false) { // XXX writing seems to crash RdoServD
<<<<<<< HEAD
                    mFh.updateEFLinearFixed(EF_SMS,
                            i, ba, null, obtainMessage(EVENT_MARK_SMS_READ_DONE, i));
=======
                    phone.mSIMFileHandler.updateEFLinearFixed(EF_SMS, i, ba, null,
                                    obtainMessage(EVENT_MARK_SMS_READ_DONE, i));
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        }
    }

<<<<<<< HEAD
    protected void onRecordLoaded() {
        // One record loaded successfully or failed, In either case
        // we need to update the recordsToLoad count
        recordsToLoad -= 1;
        if (DBG) log("onRecordLoaded " + recordsToLoad + " requested: " + recordsRequested);
=======

    //***** Private Methods

    private void onRecordLoaded()
    {
        // One record loaded successfully or failed, In either case
        // we need to update the recordsToLoad count
        recordsToLoad -= 1;
>>>>>>> 54b6cfa... Initial Contribution

        if (recordsToLoad == 0 && recordsRequested == true) {
            onAllRecordsLoaded();
        } else if (recordsToLoad < 0) {
<<<<<<< HEAD
            loge("recordsToLoad <0, programmer error suspected");
            recordsToLoad = 0;
        }
    }

    protected void onAllRecordsLoaded() {
        String operator = getOperatorNumeric();

        // Some fields require more than one SIM record to set

        log("SIMRecords: onAllRecordsLoaded set 'gsm.sim.operator.numeric' to operator='" +
                operator + "'");
        SystemProperties.set(PROPERTY_ICC_OPERATOR_NUMERIC, operator);

        if (imsi != null) {
            SystemProperties.set(PROPERTY_ICC_OPERATOR_ISO_COUNTRY,
                    MccTable.countryCodeForMcc(Integer.parseInt(imsi.substring(0,3))));
        }
        else {
            loge("onAllRecordsLoaded: imsi is NULL!");
        }

        setVoiceMailByCountry(operator);
        setSpnFromConfig(operator);

        recordsLoadedRegistrants.notifyRegistrants(
            new AsyncResult(null, null, null));
        mParentCard.broadcastIccStateChangedIntent(
                IccCard.INTENT_VALUE_ICC_LOADED, null);
    }

    //***** Private methods

    private void setSpnFromConfig(String carrier) {
        if (mSpnOverride.containsCarrier(carrier)) {
            spn = mSpnOverride.getSpn(carrier);
        }
    }


=======
            Log.e(LOG_TAG, "SIMRecords: recordsToLoad <0, programmer error suspected");
            recordsToLoad = 0;
        }
    }
    
    private void onAllRecordsLoaded()
    {
        Log.d(LOG_TAG, "SIMRecords: record load complete");

        // Some fields require more than one SIM record to set

        phone.setSystemProperty(PROPERTY_SIM_OPERATOR_NUMERIC, 
                                getSIMOperatorNumeric());

        if (imsi != null) {
            phone.setSystemProperty(PROPERTY_SIM_OPERATOR_ISO_COUNTRY,
                                        MccTable.countryCodeForMcc(
                                            Integer.parseInt(imsi.substring(0,3))));
        }
        else {
            Log.e("SIM", "[SIMRecords] onAllRecordsLoaded: imsi is NULL!");
        }

        setVoiceMailByCountry(getSIMOperatorNumeric());

        recordsLoadedRegistrants.notifyRegistrants(
            new AsyncResult(null, null, null));
        phone.mSimCard.broadcastSimStateChangedIntent(
                SimCard.INTENT_VALUE_SIM_LOADED, null);
    }

>>>>>>> 54b6cfa... Initial Contribution
    private void setVoiceMailByCountry (String spn) {
        if (mVmConfig.containsCarrier(spn)) {
            isVoiceMailFixed = true;
            voiceMailNum = mVmConfig.getVoiceMailNumber(spn);
            voiceMailTag = mVmConfig.getVoiceMailTag(spn);
        }
    }

<<<<<<< HEAD
    @Override
    public void onReady() {
        /* broadcast intent SIM_READY here so that we can make sure
          READY is sent before IMSI ready
        */
        mParentCard.broadcastIccStateChangedIntent(
                IccCard.INTENT_VALUE_ICC_READY, null);
=======
    private void onSimReady() {
        /* broadcast intent SIM_READY here so that we can make sure
          READY is sent before IMSI ready
        */
        phone.mSimCard.broadcastSimStateChangedIntent(
                SimCard.INTENT_VALUE_SIM_READY, null);
>>>>>>> 54b6cfa... Initial Contribution

        fetchSimRecords();
    }

<<<<<<< HEAD
    protected void fetchSimRecords() {
        recordsRequested = true;

        if (DBG) log("fetchSimRecords " + recordsToLoad);

        mCi.getIMSIForApp(mParentCard.getAid(), obtainMessage(EVENT_GET_IMSI_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_ICCID, obtainMessage(EVENT_GET_ICCID_DONE));
=======
    private void fetchSimRecords() {
        recordsRequested = true;

	Log.v(LOG_TAG, "SIMRecords:fetchSimRecords " + recordsToLoad);

        phone.mCM.getIMSI(obtainMessage(EVENT_GET_IMSI_DONE));
        recordsToLoad++;

        phone.mSIMFileHandler.loadEFTransparent(EF_ICCID, 
                            obtainMessage(EVENT_GET_ICCID_DONE));
>>>>>>> 54b6cfa... Initial Contribution
        recordsToLoad++;

        // FIXME should examine EF[MSISDN]'s capability configuration
        // to determine which is the voice/data/fax line
<<<<<<< HEAD
        new AdnRecordLoader(mFh).loadFromEF(EF_MSISDN, EF_EXT1, 1,
=======
        new AdnRecordLoader(phone).loadFromEF(EF_MSISDN, EF_EXT1, 1,
>>>>>>> 54b6cfa... Initial Contribution
                    obtainMessage(EVENT_GET_MSISDN_DONE));
        recordsToLoad++;

        // Record number is subscriber profile
<<<<<<< HEAD
        mFh.loadEFLinearFixed(EF_MBI, 1, obtainMessage(EVENT_GET_MBI_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_AD, obtainMessage(EVENT_GET_AD_DONE));
        recordsToLoad++;

        // Record number is subscriber profile
        mFh.loadEFLinearFixed(EF_MWIS, 1, obtainMessage(EVENT_GET_MWIS_DONE));
=======
        phone.mSIMFileHandler.loadEFLinearFixed(EF_MBI, 1, 
                        obtainMessage(EVENT_GET_MBI_DONE));
        recordsToLoad++;

        phone.mSIMFileHandler.loadEFTransparent(EF_AD,  
                        obtainMessage(EVENT_GET_AD_DONE));
        recordsToLoad++;

        // Record number is subscriber profile
        phone.mSIMFileHandler.loadEFLinearFixed(EF_MWIS, 1, 
                        obtainMessage(EVENT_GET_MWIS_DONE));
>>>>>>> 54b6cfa... Initial Contribution
        recordsToLoad++;


        // Also load CPHS-style voice mail indicator, which stores
        // the same info as EF[MWIS]. If both exist, both are updated
        // but the EF[MWIS] data is preferred
        // Please note this must be loaded after EF[MWIS]
<<<<<<< HEAD
        mFh.loadEFTransparent(
                EF_VOICE_MAIL_INDICATOR_CPHS,
=======
        phone.mSIMFileHandler.loadEFTransparent(
                EF_VOICE_MAIL_INDICATOR_CPHS, 
>>>>>>> 54b6cfa... Initial Contribution
                obtainMessage(EVENT_GET_VOICE_MAIL_INDICATOR_CPHS_DONE));
        recordsToLoad++;

        // Same goes for Call Forward Status indicator: fetch both
        // EF[CFIS] and CPHS-EF, with EF[CFIS] preferred.
<<<<<<< HEAD
        mFh.loadEFLinearFixed(EF_CFIS, 1, obtainMessage(EVENT_GET_CFIS_DONE));
        recordsToLoad++;
        mFh.loadEFTransparent(EF_CFF_CPHS, obtainMessage(EVENT_GET_CFF_DONE));
=======
        phone.mSIMFileHandler.loadEFLinearFixed(EF_CFIS, 1, obtainMessage(EVENT_GET_CFIS_DONE));
        recordsToLoad++;
        phone.mSIMFileHandler.loadEFTransparent(EF_CFF_CPHS,
                obtainMessage(EVENT_GET_CFF_DONE));
>>>>>>> 54b6cfa... Initial Contribution
        recordsToLoad++;


        getSpnFsm(true, null);

<<<<<<< HEAD
        mFh.loadEFTransparent(EF_SPDI, obtainMessage(EVENT_GET_SPDI_DONE));
        recordsToLoad++;

        mFh.loadEFLinearFixed(EF_PNN, 1, obtainMessage(EVENT_GET_PNN_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_SST, obtainMessage(EVENT_GET_SST_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_CSP_CPHS,obtainMessage(EVENT_GET_CSP_CPHS_DONE));
=======
        phone.mSIMFileHandler.loadEFTransparent(EF_SPDI, 
            obtainMessage(EVENT_GET_SPDI_DONE));
        recordsToLoad++;

        phone.mSIMFileHandler.loadEFLinearFixed(EF_PNN, 1,
            obtainMessage(EVENT_GET_PNN_DONE));
        recordsToLoad++;

        phone.mSIMFileHandler.loadEFTransparent(EF_SST,
            obtainMessage(EVENT_GET_SST_DONE));
        recordsToLoad++;

        phone.mSIMFileHandler.loadEFTransparent(EF_INFO_CPHS,
                obtainMessage(EVENT_GET_INFO_CPHS_DONE));
>>>>>>> 54b6cfa... Initial Contribution
        recordsToLoad++;

        // XXX should seek instead of examining them all
        if (false) { // XXX
<<<<<<< HEAD
            mFh.loadEFLinearFixedAll(EF_SMS, obtainMessage(EVENT_GET_ALL_SMS_DONE));
=======
            phone.mSIMFileHandler.loadEFLinearFixedAll(EF_SMS,
                obtainMessage(EVENT_GET_ALL_SMS_DONE));
>>>>>>> 54b6cfa... Initial Contribution
            recordsToLoad++;
        }

        if (CRASH_RIL) {
<<<<<<< HEAD
            String sms = "0107912160130310f20404d0110041007030208054832b0120"
                         + "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
                         + "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
                         + "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
                         + "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"
                         + "ffffffffffffffffffffffffffffff";
            byte[] ba = IccUtils.hexStringToBytes(sms);

            mFh.updateEFLinearFixed(EF_SMS, 1, ba, null,
                            obtainMessage(EVENT_MARK_SMS_READ_DONE, 1));
        }
        if (DBG) log("fetchSimRecords " + recordsToLoad + " requested: " + recordsRequested);
=======
            String sms = "0107912160130310f20404d0110041007030208054832b0120ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
            byte[] ba = SimUtils.hexStringToBytes(sms);

            phone.mSIMFileHandler.updateEFLinearFixed(EF_SMS, 1, ba, null,
                            obtainMessage(EVENT_MARK_SMS_READ_DONE, 1));
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Returns the SpnDisplayRule based on settings on the SIM and the
     * specified plmn (currently-registered PLMN).  See TS 22.101 Annex A
     * and TS 51.011 10.3.11 for details.
     *
     * If the SPN is not found on the SIM, the rule is always PLMN_ONLY.
     */
<<<<<<< HEAD
    @Override
    public int getDisplayRule(String plmn) {
=======
    int getDisplayRule(String plmn) {
>>>>>>> 54b6cfa... Initial Contribution
        int rule;
        if (spn == null || spnDisplayCondition == -1) {
            // EF_SPN was not found on the SIM, or not yet loaded.  Just show ONS.
            rule = SPN_RULE_SHOW_PLMN;
        } else if (isOnMatchingPlmn(plmn)) {
            rule = SPN_RULE_SHOW_SPN;
            if ((spnDisplayCondition & 0x01) == 0x01) {
                // ONS required when registered to HPLMN or PLMN in EF_SPDI
                rule |= SPN_RULE_SHOW_PLMN;
            }
        } else {
            rule = SPN_RULE_SHOW_PLMN;
            if ((spnDisplayCondition & 0x02) == 0x00) {
                // SPN required if not registered to HPLMN or PLMN in EF_SPDI
                rule |= SPN_RULE_SHOW_SPN;
            }
        }
        return rule;
    }

    /**
     * Checks if plmn is HPLMN or on the spdiNetworks list.
     */
    private boolean isOnMatchingPlmn(String plmn) {
        if (plmn == null) return false;

<<<<<<< HEAD
        if (plmn.equals(getOperatorNumeric())) {
=======
        if (plmn.equals(getSIMOperatorNumeric())) {
>>>>>>> 54b6cfa... Initial Contribution
            return true;
        }

        if (spdiNetworks != null) {
            for (String spdiNet : spdiNetworks) {
                if (plmn.equals(spdiNet)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * States of Get SPN Finite State Machine which only used by getSpnFsm()
     */
    private enum Get_Spn_Fsm_State {
        IDLE,               // No initialized
        INIT,               // Start FSM
        READ_SPN_3GPP,      // Load EF_SPN firstly
        READ_SPN_CPHS,      // Load EF_SPN_CPHS secondly
        READ_SPN_SHORT_CPHS // Load EF_SPN_SHORT_CPHS last
    }

    /**
     * Finite State Machine to load Service Provider Name , which can be stored
     * in either EF_SPN (3GPP), EF_SPN_CPHS, or EF_SPN_SHORT_CPHS (CPHS4.2)
     *
     * After starting, FSM will search SPN EFs in order and stop after finding
     * the first valid SPN
     *
<<<<<<< HEAD
     * If the FSM gets restart while waiting for one of
     * SPN EFs results (i.e. a SIM refresh occurs after issuing
     * read EF_CPHS_SPN), it will re-initialize only after
     * receiving and discarding the unfinished SPN EF result.
     *
=======
>>>>>>> 54b6cfa... Initial Contribution
     * @param start set true only for initialize loading
     * @param ar the AsyncResult from loadEFTransparent
     *        ar.exception holds exception in error
     *        ar.result is byte[] for data in success
     */
    private void getSpnFsm(boolean start, AsyncResult ar) {
        byte[] data;

        if (start) {
<<<<<<< HEAD
            // Check previous state to see if there is outstanding
            // SPN read
            if(spnState == Get_Spn_Fsm_State.READ_SPN_3GPP ||
               spnState == Get_Spn_Fsm_State.READ_SPN_CPHS ||
               spnState == Get_Spn_Fsm_State.READ_SPN_SHORT_CPHS ||
               spnState == Get_Spn_Fsm_State.INIT) {
                // Set INIT then return so the INIT code
                // will run when the outstanding read done.
                spnState = Get_Spn_Fsm_State.INIT;
                return;
            } else {
                spnState = Get_Spn_Fsm_State.INIT;
            }
=======
            spnState = Get_Spn_Fsm_State.INIT;
>>>>>>> 54b6cfa... Initial Contribution
        }

        switch(spnState){
            case INIT:
                spn = null;

<<<<<<< HEAD
                mFh.loadEFTransparent(EF_SPN,
=======
                phone.mSIMFileHandler.loadEFTransparent( EF_SPN,
>>>>>>> 54b6cfa... Initial Contribution
                        obtainMessage(EVENT_GET_SPN_DONE));
                recordsToLoad++;

                spnState = Get_Spn_Fsm_State.READ_SPN_3GPP;
                break;
            case READ_SPN_3GPP:
                if (ar != null && ar.exception == null) {
                    data = (byte[]) ar.result;
                    spnDisplayCondition = 0xff & data[0];
<<<<<<< HEAD
                    spn = IccUtils.adnStringFieldToString(data, 1, data.length - 1);

                    if (DBG) log("Load EF_SPN: " + spn
                            + " spnDisplayCondition: " + spnDisplayCondition);
                    SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);

                    spnState = Get_Spn_Fsm_State.IDLE;
                } else {
                    mFh.loadEFTransparent( EF_SPN_CPHS,
=======
                    spn = SimUtils.adnStringFieldToString(data, 1, data.length - 1);

                    if (DBG) log("Load EF_SPN: " + spn
                            + " spnDisplayCondition: " + spnDisplayCondition);
                    phone.setSystemProperty(PROPERTY_SIM_OPERATOR_ALPHA, spn);

                    spnState = Get_Spn_Fsm_State.IDLE;
                } else {
                    phone.mSIMFileHandler.loadEFTransparent( EF_SPN_CPHS,
>>>>>>> 54b6cfa... Initial Contribution
                            obtainMessage(EVENT_GET_SPN_DONE));
                    recordsToLoad++;

                    spnState = Get_Spn_Fsm_State.READ_SPN_CPHS;
<<<<<<< HEAD

=======
                    
>>>>>>> 54b6cfa... Initial Contribution
                    // See TS 51.011 10.3.11.  Basically, default to
                    // show PLMN always, and SPN also if roaming.
                    spnDisplayCondition = -1;
                }
                break;
            case READ_SPN_CPHS:
                if (ar != null && ar.exception == null) {
                    data = (byte[]) ar.result;
<<<<<<< HEAD
                    spn = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1 );

                    if (DBG) log("Load EF_SPN_CPHS: " + spn);
                    SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);

                    spnState = Get_Spn_Fsm_State.IDLE;
                } else {
                    mFh.loadEFTransparent(
                            EF_SPN_SHORT_CPHS, obtainMessage(EVENT_GET_SPN_DONE));
=======
                    spn = SimUtils.adnStringFieldToString(
                            data, 0, data.length - 1 );

                    if (DBG) log("Load EF_SPN_CPHS: " + spn);
                    phone.setSystemProperty(PROPERTY_SIM_OPERATOR_ALPHA, spn);

                    spnState = Get_Spn_Fsm_State.IDLE;
                } else {
                    phone.mSIMFileHandler.loadEFTransparent( EF_SPN_SHORT_CPHS,
                            obtainMessage(EVENT_GET_SPN_DONE));
>>>>>>> 54b6cfa... Initial Contribution
                    recordsToLoad++;

                    spnState = Get_Spn_Fsm_State.READ_SPN_SHORT_CPHS;
                }
                break;
            case READ_SPN_SHORT_CPHS:
                if (ar != null && ar.exception == null) {
                    data = (byte[]) ar.result;
<<<<<<< HEAD
                    spn = IccUtils.adnStringFieldToString(
                            data, 0, data.length - 1);

                    if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
                    SystemProperties.set(PROPERTY_ICC_OPERATOR_ALPHA, spn);
=======
                    spn = SimUtils.adnStringFieldToString(
                            data, 0, data.length - 1);

                    if (DBG) log("Load EF_SPN_SHORT_CPHS: " + spn);
                    phone.setSystemProperty(PROPERTY_SIM_OPERATOR_ALPHA, spn);
>>>>>>> 54b6cfa... Initial Contribution
                }else {
                    if (DBG) log("No SPN loaded in either CHPS or 3GPP");
                }

                spnState = Get_Spn_Fsm_State.IDLE;
                break;
            default:
                spnState = Get_Spn_Fsm_State.IDLE;
        }
    }

    /**
     * Parse TS 51.011 EF[SPDI] record
     * This record contains the list of numeric network IDs that
     * are treated specially when determining SPN display
     */
    private void
<<<<<<< HEAD
    parseEfSpdi(byte[] data) {
=======
    parseEfSpdi(byte[] data)
    {
>>>>>>> 54b6cfa... Initial Contribution
        SimTlv tlv = new SimTlv(data, 0, data.length);

        byte[] plmnEntries = null;

<<<<<<< HEAD
        for ( ; tlv.isValidObject() ; tlv.nextObject()) {
            // Skip SPDI tag, if existant
            if (tlv.getTag() == TAG_SPDI) {
              tlv = new SimTlv(tlv.getData(), 0, tlv.getData().length);
            }
            // There should only be one TAG_SPDI_PLMN_LIST
=======
        // There should only be one TAG_SPDI_PLMN_LIST
        for ( ; tlv.isValidObject() ; tlv.nextObject()) {
>>>>>>> 54b6cfa... Initial Contribution
            if (tlv.getTag() == TAG_SPDI_PLMN_LIST) {
                plmnEntries = tlv.getData();
                break;
            }
        }

        if (plmnEntries == null) {
            return;
        }

        spdiNetworks = new ArrayList<String>(plmnEntries.length / 3);

        for (int i = 0 ; i + 2 < plmnEntries.length ; i += 3) {
<<<<<<< HEAD
            String plmnCode;
            plmnCode = IccUtils.bcdToString(plmnEntries, i, 3);
=======
            String plmnCode;        
            plmnCode = SimUtils.bcdToString(plmnEntries, i, 3);
>>>>>>> 54b6cfa... Initial Contribution

            // Valid operator codes are 5 or 6 digits
            if (plmnCode.length() >= 5) {
                log("EF_SPDI network: " + plmnCode);
                spdiNetworks.add(plmnCode);
            }
        }
    }

    /**
     * check to see if Mailbox Number is allocated and activated in CPHS SST
     */
    private boolean isCphsMailboxEnabled() {
        if (mCphsInfo == null)  return false;
        return ((mCphsInfo[1] & CPHS_SST_MBN_MASK) == CPHS_SST_MBN_ENABLED );
    }

<<<<<<< HEAD
    protected void log(String s) {
        Log.d(LOG_TAG, "[SIMRecords] " + s);
    }

    protected void loge(String s) {
        Log.e(LOG_TAG, "[SIMRecords] " + s);
    }

    protected void logw(String s, Throwable tr) {
        Log.w(LOG_TAG, "[SIMRecords] " + s, tr);
    }

    protected void logv(String s) {
        Log.v(LOG_TAG, "[SIMRecords] " + s);
    }

    /**
     * Return true if "Restriction of menu options for manual PLMN selection"
     * bit is set or EF_CSP data is unavailable, return false otherwise.
     */
    public boolean isCspPlmnEnabled() {
        return mCspPlmnEnabled;
    }

    /**
     * Parse EF_CSP data and check if
     * "Restriction of menu options for manual PLMN selection" is
     * Enabled/Disabled
     *
     * @param data EF_CSP hex data.
     */
    private void handleEfCspData(byte[] data) {
        // As per spec CPHS4_2.WW6, CPHS B.4.7.1, EF_CSP contains CPHS defined
        // 18 bytes (i.e 9 service groups info) and additional data specific to
        // operator. The valueAddedServicesGroup is not part of standard
        // services. This is operator specific and can be programmed any where.
        // Normally this is programmed as 10th service after the standard
        // services.
        int usedCspGroups = data.length / 2;
        // This is the "Servive Group Number" of "Value Added Services Group".
        byte valueAddedServicesGroup = (byte)0xC0;

        mCspPlmnEnabled = true;
        for (int i = 0; i < usedCspGroups; i++) {
             if (data[2 * i] == valueAddedServicesGroup) {
                 log("[CSP] found ValueAddedServicesGroup, value " + data[(2 * i) + 1]);
                 if ((data[(2 * i) + 1] & 0x80) == 0x80) {
                     // Bit 8 is for
                     // "Restriction of menu options for manual PLMN selection".
                     // Operator Selection menu should be enabled.
                     mCspPlmnEnabled = true;
                 } else {
                     mCspPlmnEnabled = false;
                     // Operator Selection menu should be disabled.
                     // Operator Selection Mode should be set to Automatic.
                     log("[CSP] Set Automatic Network Selection");
                     mNetworkSelectionModeAutomaticRegistrants.notifyRegistrants();
                 }
                 return;
             }
        }

        log("[CSP] Value Added Service Group (0xC0), not found!");
    }
=======
    private void log(String s) {
        Log.d(LOG_TAG, "[SIMRecords] " + s);
    }
>>>>>>> 54b6cfa... Initial Contribution
}
