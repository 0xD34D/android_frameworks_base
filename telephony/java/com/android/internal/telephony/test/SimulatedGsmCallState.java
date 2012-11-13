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

package com.android.internal.telephony.test;

<<<<<<< HEAD
import android.os.Looper;
=======
>>>>>>> 54b6cfa... Initial Contribution
import android.os.Message;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import com.android.internal.telephony.ATParseEx;
<<<<<<< HEAD
import com.android.internal.telephony.DriverCall;
=======
import com.android.internal.telephony.gsm.DriverCall;
>>>>>>> 54b6cfa... Initial Contribution
import java.util.List;
import java.util.ArrayList;

import android.util.Log;

<<<<<<< HEAD
class CallInfo {
=======
class CallInfo
{
>>>>>>> 54b6cfa... Initial Contribution
    enum State {
        ACTIVE(0),
        HOLDING(1),
        DIALING(2),    // MO call only
        ALERTING(3),   // MO call only
        INCOMING(4),   // MT call only
        WAITING(5);    // MT call only

        State (int value) {this.value = value;}
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        private final int value;
        public int value() {return value;};
    };

    boolean isMT;
    State state;
    boolean isMpty;
    String number;
    int TOA;

<<<<<<< HEAD
    CallInfo (boolean isMT, State state, boolean isMpty, String number) {
=======
    CallInfo (boolean isMT, State state, boolean isMpty, String number)
    {
>>>>>>> 54b6cfa... Initial Contribution
        this.isMT = isMT;
        this.state = state;
        this.isMpty = isMpty;
        this.number = number;

        if (number.length() > 0 && number.charAt(0) == '+') {
            TOA = PhoneNumberUtils.TOA_International;
        } else {
            TOA = PhoneNumberUtils.TOA_Unknown;
        }
    }

    static CallInfo
<<<<<<< HEAD
    createOutgoingCall(String number) {
=======
    createOutgoingCall(String number)
    {
>>>>>>> 54b6cfa... Initial Contribution
        return new CallInfo (false, State.DIALING, false, number);
    }

    static CallInfo
<<<<<<< HEAD
    createIncomingCall(String number) {
=======
    createIncomingCall(String number)
    {
>>>>>>> 54b6cfa... Initial Contribution
        return new CallInfo (true, State.INCOMING, false, number);
    }

    String
<<<<<<< HEAD
    toCLCCLine(int index) {
        return
=======
    toCLCCLine(int index)
    {
        return 
>>>>>>> 54b6cfa... Initial Contribution
            "+CLCC: "
            + index + "," + (isMT ? "1" : "0") +","
            + state.value() + ",0," + (isMpty ? "1" : "0")
            + ",\"" + number + "\"," + TOA;
    }

    DriverCall
<<<<<<< HEAD
    toDriverCall(int index) {
        DriverCall ret;
=======
    toDriverCall(int index)
    {
        DriverCall ret; 
>>>>>>> 54b6cfa... Initial Contribution

        ret = new DriverCall();

        ret.index = index;
        ret.isMT = isMT;

        try {
            ret.state = DriverCall.stateFromCLCC(state.value());
        } catch (ATParseEx ex) {
            throw new RuntimeException("should never happen", ex);
        }

        ret.isMpty = isMpty;
        ret.number = number;
        ret.TOA = TOA;
        ret.isVoice = true;
        ret.als = 0;

        return ret;
    }


    boolean
<<<<<<< HEAD
    isActiveOrHeld() {
=======
    isActiveOrHeld()
    {
>>>>>>> 54b6cfa... Initial Contribution
        return state == State.ACTIVE || state == State.HOLDING;
    }

    boolean
<<<<<<< HEAD
    isConnecting() {
=======
    isConnecting()
    {
>>>>>>> 54b6cfa... Initial Contribution
        return state == State.DIALING || state == State.ALERTING;
    }

    boolean
<<<<<<< HEAD
    isRinging() {
=======
    isRinging()
    {
>>>>>>> 54b6cfa... Initial Contribution
        return state == State.INCOMING || state == State.WAITING;
    }

}

<<<<<<< HEAD
class InvalidStateEx extends Exception {
    InvalidStateEx() {
=======
class InvalidStateEx extends Exception
{
    InvalidStateEx()
    {
>>>>>>> 54b6cfa... Initial Contribution

    }
}


<<<<<<< HEAD
class SimulatedGsmCallState extends Handler {
=======
class SimulatedGsmCallState extends Handler
{
>>>>>>> 54b6cfa... Initial Contribution
    //***** Instance Variables

    CallInfo calls[] = new CallInfo[MAX_CALLS];

    private boolean autoProgressConnecting = true;
    private boolean nextDialFailImmediately;
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution

    //***** Event Constants

    static final int EVENT_PROGRESS_CALL_STATE = 1;

    //***** Constants

    static final int MAX_CALLS = 7;
    /** number of msec between dialing -> alerting and alerting->active */
    static final int CONNECTING_PAUSE_MSEC = 5 * 100;


    //***** Overridden from Handler

<<<<<<< HEAD
    public SimulatedGsmCallState(Looper looper) {
        super(looper);
    }

    public void
    handleMessage(Message msg) {
=======
    public void
    handleMessage(Message msg)
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized(this) { switch (msg.what) {
            // PLEASE REMEMBER
            // calls may have hung up by the time delayed events happen

            case EVENT_PROGRESS_CALL_STATE:
                progressConnectingCallState();
            break;
        }}
    }

    //***** Public Methods

<<<<<<< HEAD
    /**
     * Start the simulated phone ringing
     * true if succeeded, false if failed
     */
    public boolean
    triggerRing(String number) {
=======
    /** 
     * Start the simulated phone ringing 
     * true if succeeded, false if failed
     */
    public boolean
    triggerRing(String number)
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (this) {
            int empty = -1;
            boolean isCallWaiting = false;

            // ensure there aren't already calls INCOMING or WAITING
            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

                if (call == null && empty < 0) {
                    empty = i;
<<<<<<< HEAD
                } else if (call != null
=======
                } else if (call != null 
>>>>>>> 54b6cfa... Initial Contribution
                    && (call.state == CallInfo.State.INCOMING
                        || call.state == CallInfo.State.WAITING)
                ) {
                    Log.w("ModelInterpreter",
                        "triggerRing failed; phone already ringing");
                    return false;
                } else if (call != null) {
                    isCallWaiting = true;
                }
            }

            if (empty < 0 ) {
                Log.w("ModelInterpreter", "triggerRing failed; all full");
                return false;
            }

            calls[empty] = CallInfo.createIncomingCall(
                PhoneNumberUtils.extractNetworkPortion(number));

            if (isCallWaiting) {
                calls[empty].state = CallInfo.State.WAITING;
            }
<<<<<<< HEAD

=======
            
>>>>>>> 54b6cfa... Initial Contribution
        }
        return true;
    }

    /** If a call is DIALING or ALERTING, progress it to the next state */
    public void
<<<<<<< HEAD
    progressConnectingCallState() {
=======
    progressConnectingCallState()
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (this)  {
            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

                if (call != null && call.state == CallInfo.State.DIALING) {
                    call.state = CallInfo.State.ALERTING;

                    if (autoProgressConnecting) {
                        sendMessageDelayed(
<<<<<<< HEAD
                                obtainMessage(EVENT_PROGRESS_CALL_STATE, call),
                                CONNECTING_PAUSE_MSEC);
                    }
                    break;
                } else if (call != null
=======
                                obtainMessage(EVENT_PROGRESS_CALL_STATE, call), 
                                CONNECTING_PAUSE_MSEC);
                    }
                    break;
                } else if (call != null 
>>>>>>> 54b6cfa... Initial Contribution
                        && call.state == CallInfo.State.ALERTING
                ) {
                    call.state = CallInfo.State.ACTIVE;
                    break;
                }
            }
        }
    }

    /** If a call is DIALING or ALERTING, progress it all the way to ACTIVE */
    public void
<<<<<<< HEAD
    progressConnectingToActive() {
=======
    progressConnectingToActive()
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (this)  {
            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

<<<<<<< HEAD
                if (call != null && (call.state == CallInfo.State.DIALING
=======
                if (call != null && (call.state == CallInfo.State.DIALING 
>>>>>>> 54b6cfa... Initial Contribution
                    || call.state == CallInfo.State.ALERTING)
                ) {
                    call.state = CallInfo.State.ACTIVE;
                    break;
                }
            }
        }
    }

    /** automatically progress mobile originated calls to ACTIVE.
     *  default to true
     */
    public void
<<<<<<< HEAD
    setAutoProgressConnectingCall(boolean b) {
        autoProgressConnecting = b;
    }

    public void
    setNextDialFailImmediately(boolean b) {
        nextDialFailImmediately = b;
    }

    /**
=======
    setAutoProgressConnectingCall(boolean b)
    {        
        autoProgressConnecting = b;
    }
    
    public void
    setNextDialFailImmediately(boolean b)
    {
        nextDialFailImmediately = b;
    }

    /** 
>>>>>>> 54b6cfa... Initial Contribution
     * hangup ringing, dialing, or active calls
     * returns true if call was hung up, false if not
     */
    public boolean
<<<<<<< HEAD
    triggerHangupForeground() {
=======
    triggerHangupForeground()
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (this) {
            boolean found;

            found = false;

            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

<<<<<<< HEAD
                if (call != null
=======
                if (call != null 
>>>>>>> 54b6cfa... Initial Contribution
                    && (call.state == CallInfo.State.INCOMING
                        || call.state == CallInfo.State.WAITING)
                ) {
                    calls[i] = null;
                    found = true;
                }
            }

            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

<<<<<<< HEAD
                if (call != null
=======
                if (call != null 
>>>>>>> 54b6cfa... Initial Contribution
                    && (call.state == CallInfo.State.DIALING
                        || call.state == CallInfo.State.ACTIVE
                        || call.state == CallInfo.State.ALERTING)
                ) {
                    calls[i] = null;
                    found = true;
                }
            }
            return found;
        }
    }

<<<<<<< HEAD
    /**
=======
    /** 
>>>>>>> 54b6cfa... Initial Contribution
     * hangup holding calls
     * returns true if call was hung up, false if not
     */
    public boolean
<<<<<<< HEAD
    triggerHangupBackground() {
=======
    triggerHangupBackground()
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (this) {
            boolean found = false;

            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

                if (call != null && call.state == CallInfo.State.HOLDING) {
                    calls[i] = null;
                    found = true;
                }
            }

            return found;
        }
    }

<<<<<<< HEAD
    /**
=======
    /** 
>>>>>>> 54b6cfa... Initial Contribution
     * hangup all
     * returns true if call was hung up, false if not
     */
    public boolean
<<<<<<< HEAD
    triggerHangupAll() {
=======
    triggerHangupAll()
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized(this) {
            boolean found = false;

            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

                if (calls[i] != null) {
                    found = true;
                }

                calls[i] = null;
            }

            return found;
        }
    }

    public boolean
<<<<<<< HEAD
    onAnswer() {
=======
    onAnswer()
    {
>>>>>>> 54b6cfa... Initial Contribution
        synchronized (this) {
            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo call = calls[i];

<<<<<<< HEAD
                if (call != null
=======
                if (call != null 
>>>>>>> 54b6cfa... Initial Contribution
                    && (call.state == CallInfo.State.INCOMING
                        || call.state == CallInfo.State.WAITING)
                ) {
                    return switchActiveAndHeldOrWaiting();
                }
            }
        }

        return false;
    }

    public boolean
<<<<<<< HEAD
    onHangup() {
=======
    onHangup()
    {
>>>>>>> 54b6cfa... Initial Contribution
        boolean found = false;

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo call = calls[i];

            if (call != null && call.state != CallInfo.State.WAITING) {
                calls[i] = null;
                found = true;
            }
        }

        return found;
    }

    public boolean
<<<<<<< HEAD
    onChld(char c0, char c1) {
=======
    onChld(char c0, char c1)
    {
>>>>>>> 54b6cfa... Initial Contribution
        boolean ret;
        int callIndex = 0;

        if (c1 != 0) {
            callIndex = c1 - '1';

            if (callIndex < 0 || callIndex >= calls.length) {
                return false;
            }
        }
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        switch (c0) {
            case '0':
                ret = releaseHeldOrUDUB();
            break;
            case '1':
                if (c1 <= 0) {
                    ret = releaseActiveAcceptHeldOrWaiting();
                } else {
                    if (calls[callIndex] == null) {
                        ret = false;
                    } else {
                        calls[callIndex] = null;
                        ret = true;
                    }
                }
            break;
            case '2':
                if (c1 <= 0) {
                    ret = switchActiveAndHeldOrWaiting();
                } else {
                    ret = separateCall(callIndex);
                }
            break;
            case '3':
                ret = conference();
            break;
            case '4':
                ret = explicitCallTransfer();
            break;
            case '5':
                if (true) { //just so javac doesnt complain about break
                    //CCBS not impled
                    ret = false;
                }
            break;
            default:
                ret = false;

        }

        return ret;
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    public boolean
    releaseHeldOrUDUB() {
        boolean found = false;

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null && c.isRinging()) {
                found = true;
                calls[i] = null;
                break;
            }
        }

        if (!found) {
            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo c = calls[i];

                if (c != null && c.state == CallInfo.State.HOLDING) {
                    found = true;
                    calls[i] = null;
                    // don't stop...there may be more than one
                }
            }
        }

        return true;
    }


    public boolean
<<<<<<< HEAD
    releaseActiveAcceptHeldOrWaiting() {
=======
    releaseActiveAcceptHeldOrWaiting()
    {
>>>>>>> 54b6cfa... Initial Contribution
        boolean foundHeld = false;
        boolean foundActive = false;

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null && c.state == CallInfo.State.ACTIVE) {
                calls[i] = null;
                foundActive = true;
            }
        }

        if (!foundActive) {
            // FIXME this may not actually be how most basebands react
            // CHLD=1 may not hang up dialing/alerting calls
            for (int i = 0 ; i < calls.length ; i++) {
                CallInfo c = calls[i];

<<<<<<< HEAD
                if (c != null
                        && (c.state == CallInfo.State.DIALING
=======
                if (c != null 
                        && (c.state == CallInfo.State.DIALING 
>>>>>>> 54b6cfa... Initial Contribution
                            || c.state == CallInfo.State.ALERTING)
                ) {
                    calls[i] = null;
                    foundActive = true;
                }
            }
        }

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null && c.state == CallInfo.State.HOLDING) {
                c.state = CallInfo.State.ACTIVE;
                foundHeld = true;
            }
        }

        if (foundHeld) {
            return true;
        }

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null && c.isRinging()) {
                c.state = CallInfo.State.ACTIVE;
                return true;
            }
        }

        return true;
    }

    public boolean
<<<<<<< HEAD
    switchActiveAndHeldOrWaiting() {
        boolean hasHeld = false;

=======
    switchActiveAndHeldOrWaiting()
    {
        boolean hasHeld = false;
        
>>>>>>> 54b6cfa... Initial Contribution
        // first, are there held calls?
        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null && c.state == CallInfo.State.HOLDING) {
                hasHeld = true;
                break;
            }
        }

        // Now, switch
        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null) {
                if (c.state == CallInfo.State.ACTIVE) {
                    c.state = CallInfo.State.HOLDING;
                } else if (c.state == CallInfo.State.HOLDING) {
                    c.state = CallInfo.State.ACTIVE;
                } else if (!hasHeld && c.isRinging())  {
                    c.state = CallInfo.State.ACTIVE;
                }
            }
        }

        return true;
    }


    public boolean
<<<<<<< HEAD
    separateCall(int index) {
=======
    separateCall(int index)
    {
>>>>>>> 54b6cfa... Initial Contribution
        try {
            CallInfo c;

            c = calls[index];

            if (c == null || c.isConnecting() || countActiveLines() != 1) {
                return false;
            }

            c.state = CallInfo.State.ACTIVE;
            c.isMpty = false;

            for (int i = 0 ; i < calls.length ; i++) {
                int countHeld=0, lastHeld=0;

                if (i != index) {
                    CallInfo cb = calls[i];

                    if (cb != null && cb.state == CallInfo.State.ACTIVE) {
                        cb.state = CallInfo.State.HOLDING;
                        countHeld++;
                        lastHeld = i;
                    }
                }

                if (countHeld == 1) {
                    // if there's only one left, clear the MPTY flag
                    calls[lastHeld].isMpty = false;
                }
            }

            return true;
<<<<<<< HEAD
        } catch (InvalidStateEx ex) {
=======
        } catch (InvalidStateEx ex) { 
>>>>>>> 54b6cfa... Initial Contribution
            return false;
        }
    }



    public boolean
<<<<<<< HEAD
    conference() {
=======
    conference() 
    {
>>>>>>> 54b6cfa... Initial Contribution
        int countCalls = 0;

        // if there's connecting calls, we can't do this yet
        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null) {
                countCalls++;
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
                if (c.isConnecting()) {
                    return false;
                }
            }
        }
        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null) {
                c.state = CallInfo.State.ACTIVE;
                if (countCalls > 0) {
<<<<<<< HEAD
                    c.isMpty = true;
=======
                    c.isMpty = true; 
>>>>>>> 54b6cfa... Initial Contribution
                }
            }
        }

        return true;
    }

    public boolean
<<<<<<< HEAD
    explicitCallTransfer() {
=======
    explicitCallTransfer()
    {
>>>>>>> 54b6cfa... Initial Contribution
        int countCalls = 0;

        // if there's connecting calls, we can't do this yet
        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null) {
                countCalls++;

                if (c.isConnecting()) {
                    return false;
                }
            }
        }

        // disconnect the subscriber from both calls
        return triggerHangupAll();
    }

    public boolean
<<<<<<< HEAD
    onDial(String address) {
=======
    onDial(String address)
    {
>>>>>>> 54b6cfa... Initial Contribution
        CallInfo call;
        int freeSlot = -1;

        Log.d("GSM", "SC> dial '" + address + "'");
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
        if (nextDialFailImmediately) {
            nextDialFailImmediately = false;

            Log.d("GSM", "SC< dial fail (per request)");
<<<<<<< HEAD
            return false;
=======
            return false;            
>>>>>>> 54b6cfa... Initial Contribution
        }

        String phNum = PhoneNumberUtils.extractNetworkPortion(address);

        if (phNum.length() == 0) {
            Log.d("GSM", "SC< dial fail (invalid ph num)");
            return false;
        }

        // Ignore setting up GPRS
        if (phNum.startsWith("*99") && phNum.endsWith("#")) {
            Log.d("GSM", "SC< dial ignored (gprs)");
            return true;
        }

        // There can be at most 1 active "line" when we initiate
        // a new call
        try {
            if (countActiveLines() > 1) {
                Log.d("GSM", "SC< dial fail (invalid call state)");
                return false;
            }
        } catch (InvalidStateEx ex) {
            Log.d("GSM", "SC< dial fail (invalid call state)");
            return false;
        }

        for (int i = 0 ; i < calls.length ; i++) {
            if (freeSlot < 0 && calls[i] == null) {
                freeSlot = i;
            }
<<<<<<< HEAD

            if (calls[i] != null && !calls[i].isActiveOrHeld()) {
                // Can't make outgoing calls when there is a ringing or
=======
            
            if (calls[i] != null && !calls[i].isActiveOrHeld()) {
                // Can't make outgoing calls when there is a ringing or 
>>>>>>> 54b6cfa... Initial Contribution
                // connecting outgoing call
                Log.d("GSM", "SC< dial fail (invalid call state)");
                return false;
            } else if (calls[i] != null && calls[i].state == CallInfo.State.ACTIVE) {
                // All active calls behome held
<<<<<<< HEAD
                calls[i].state = CallInfo.State.HOLDING;
=======
                calls[i].state = CallInfo.State.HOLDING;            
>>>>>>> 54b6cfa... Initial Contribution
            }
        }

        if (freeSlot < 0) {
            Log.d("GSM", "SC< dial fail (invalid call state)");
            return false;
        }

        calls[freeSlot] = CallInfo.createOutgoingCall(phNum);

        if (autoProgressConnecting) {
            sendMessageDelayed(
<<<<<<< HEAD
                    obtainMessage(EVENT_PROGRESS_CALL_STATE, calls[freeSlot]),
=======
                    obtainMessage(EVENT_PROGRESS_CALL_STATE, calls[freeSlot]), 
>>>>>>> 54b6cfa... Initial Contribution
                    CONNECTING_PAUSE_MSEC);
        }

        Log.d("GSM", "SC< dial (slot = " + freeSlot + ")");

        return true;
    }

    public List<DriverCall>
<<<<<<< HEAD
    getDriverCalls() {
=======
    getDriverCalls()
    {
>>>>>>> 54b6cfa... Initial Contribution
        ArrayList<DriverCall> ret = new ArrayList<DriverCall>(calls.length);

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null) {
                DriverCall dc;

                dc = c.toDriverCall(i + 1);
                ret.add(dc);
            }
        }

        Log.d("GSM", "SC< getDriverCalls " + ret);

        return ret;
    }

    public List<String>
<<<<<<< HEAD
    getClccLines() {
=======
    getClccLines()
    {
>>>>>>> 54b6cfa... Initial Contribution
        ArrayList<String> ret = new ArrayList<String>(calls.length);

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo c = calls[i];

            if (c != null) {
                ret.add((c.toCLCCLine(i + 1)));
            }
        }

        return ret;
    }

    private int
<<<<<<< HEAD
    countActiveLines() throws InvalidStateEx {
=======
    countActiveLines() throws InvalidStateEx
    {
>>>>>>> 54b6cfa... Initial Contribution
        boolean hasMpty = false;
        boolean hasHeld = false;
        boolean hasActive = false;
        boolean hasConnecting = false;
        boolean hasRinging = false;
        boolean mptyIsHeld = false;

        for (int i = 0 ; i < calls.length ; i++) {
            CallInfo call = calls[i];

            if (call != null) {
                if (!hasMpty && call.isMpty) {
                    mptyIsHeld = call.state == CallInfo.State.HOLDING;
<<<<<<< HEAD
                } else if (call.isMpty && mptyIsHeld
=======
                } else if (call.isMpty && mptyIsHeld 
>>>>>>> 54b6cfa... Initial Contribution
                    && call.state == CallInfo.State.ACTIVE
                ) {
                    Log.e("ModelInterpreter", "Invalid state");
                    throw new InvalidStateEx();
                } else if (!call.isMpty && hasMpty && mptyIsHeld
                    && call.state == CallInfo.State.HOLDING
                ) {
                    Log.e("ModelInterpreter", "Invalid state");
                    throw new InvalidStateEx();
                }

                hasMpty |= call.isMpty;
                hasHeld |= call.state == CallInfo.State.HOLDING;
                hasActive |= call.state == CallInfo.State.ACTIVE;
                hasConnecting |= call.isConnecting();
                hasRinging |= call.isRinging();
            }
        }

        int ret = 0;

        if (hasHeld) ret++;
        if (hasActive) ret++;
        if (hasConnecting) ret++;
        if (hasRinging) ret++;

        return ret;
    }

}
