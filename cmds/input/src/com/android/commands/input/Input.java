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

package com.android.commands.input;

<<<<<<< HEAD
import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
=======
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * Command that sends key events to the device, either by their keycode, or by
 * desired character output.
 */

public class Input {
<<<<<<< HEAD
    private static final String TAG = "Input";
=======
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Command-line entry point.
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        (new Input()).run(args);
    }

    private void run(String[] args) {
        if (args.length < 1) {
            showUsage();
            return;
        }

        String command = args[0];

<<<<<<< HEAD
        try {
            if (command.equals("text")) {
                if (args.length == 2) {
                    sendText(args[1]);
                    return;
                }
            } else if (command.equals("keyevent")) {
                if (args.length == 2) {
                    int keyCode = KeyEvent.keyCodeFromString(args[1]);
                    if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                        keyCode = KeyEvent.keyCodeFromString("KEYCODE_" + args[1]);
                    }
                    sendKeyEvent(keyCode);
                    return;
                }
            } else if (command.equals("tap")) {
                if (args.length == 3) {
                    sendTap(Float.parseFloat(args[1]), Float.parseFloat(args[2]));
                    return;
                }
            } else if (command.equals("swipe")) {
                if (args.length == 5) {
                    sendSwipe(Float.parseFloat(args[1]), Float.parseFloat(args[2]),
                            Float.parseFloat(args[3]), Float.parseFloat(args[4]));
                    return;
                }
            } else {
                System.err.println("Error: Unknown command: " + command);
                showUsage();
                return;
            }
        } catch (NumberFormatException ex) {
        }
        System.err.println("Error: Invalid arguments for command: " + command);
        showUsage();
=======
        if (command.equals("text")) {
            sendText(args[1]);
        } else if (command.equals("keyevent")) {
            sendKeyEvent(args[1]);
        } else if (command.equals("motionevent")) {
            System.err.println("Error: motionevent not yet supported.");
            return;
        }
        else {
            System.err.println("Error: Unknown command: " + command);
            showUsage();
            return;
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Convert the characters of string text into key event's and send to
     * device.
     *
     * @param text is a string of characters you want to input to the device.
     */
<<<<<<< HEAD
=======

>>>>>>> 54b6cfa... Initial Contribution
    private void sendText(String text) {

        StringBuffer buff = new StringBuffer(text);

        boolean escapeFlag = false;
        for (int i=0; i<buff.length(); i++) {
            if (escapeFlag) {
                escapeFlag = false;
                if (buff.charAt(i) == 's') {
                    buff.setCharAt(i, ' ');
                    buff.deleteCharAt(--i);
                }
            } 
            if (buff.charAt(i) == '%') {
                escapeFlag = true;
            }
        }

        char[] chars = buff.toString().toCharArray();

<<<<<<< HEAD
        KeyCharacterMap kcm = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
        KeyEvent[] events = kcm.getEvents(chars);
        for(int i = 0; i < events.length; i++) {
            injectKeyEvent(events[i]);
        }
    }

    private void sendKeyEvent(int keyCode) {
        long now = SystemClock.uptimeMillis();
        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD));
        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD));
    }

    private void sendTap(float x, float y) {
        long now = SystemClock.uptimeMillis();
        injectPointerEvent(MotionEvent.obtain(now, now, MotionEvent.ACTION_DOWN, x, y, 0));
        injectPointerEvent(MotionEvent.obtain(now, now, MotionEvent.ACTION_UP, x, y, 0));
    }

    private void sendSwipe(float x1, float y1, float x2, float y2) {
        final int NUM_STEPS = 11;
        long now = SystemClock.uptimeMillis();
        injectPointerEvent(MotionEvent.obtain(now, now, MotionEvent.ACTION_DOWN, x1, y1, 0));
        for (int i = 1; i < NUM_STEPS; i++) {
            float alpha = (float)i / NUM_STEPS;
            injectPointerEvent(MotionEvent.obtain(now, now, MotionEvent.ACTION_MOVE,
                    lerp(x1, x2, alpha), lerp(y1, y2, alpha), 0));
        }
        injectPointerEvent(MotionEvent.obtain(now, now, MotionEvent.ACTION_UP, x2, y2, 0));
    }

    private void injectKeyEvent(KeyEvent event) {
        Log.i(TAG, "InjectKeyEvent: " + event);
        InputManager.getInstance().injectInputEvent(event,
                InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

    private void injectPointerEvent(MotionEvent event) {
        event.setSource(InputDevice.SOURCE_TOUCHSCREEN);
        Log.i("Input", "InjectPointerEvent: " + event);
        InputManager.getInstance().injectInputEvent(event,
                InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

    private static final float lerp(float a, float b, float alpha) {
        return (b - a) * alpha + a;
    }

    private void showUsage() {
        System.err.println("usage: input ...");
        System.err.println("       input text <string>");
        System.err.println("       input keyevent <key code number or name>");
        System.err.println("       input tap <x> <y>");
        System.err.println("       input swipe <x1> <y1> <x2> <y2>");
=======
        KeyCharacterMap mKeyCharacterMap = KeyCharacterMap.
            load(KeyCharacterMap.BUILT_IN_KEYBOARD);

        KeyEvent[] events = mKeyCharacterMap.getEvents(chars);

        for(int i = 0; i < events.length; i++) {
            KeyEvent event = events[i];
            Log.i("SendKeyEvent", Integer.toString(event.getKeyCode()));
            try {
                (IWindowManager.Stub
                    .asInterface(ServiceManager.getService("window")))
                    .injectKeyEvent(event, true);
            } catch (RemoteException e) {
                Log.i("Input", "DeadOjbectException");
            }
        }
    }

    /**
     * Send a single key event.
     *
     * @param event is a string representing the keycode of the key event you
     * want to execute.
     */
    private void sendKeyEvent(String event) {
        int eventCode = Integer.parseInt(event);
        long now = SystemClock.uptimeMillis();
        Log.i("SendKeyEvent", event);
        try {
            KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, eventCode, 0);
            KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, eventCode, 0);
            (IWindowManager.Stub
                .asInterface(ServiceManager.getService("window")))
                .injectKeyEvent(down, true);
            (IWindowManager.Stub
                .asInterface(ServiceManager.getService("window")))
                .injectKeyEvent(up, true);
        } catch (RemoteException e) {
            Log.i("Input", "DeadOjbectException");
        }
    }

    private void sendMotionEvent(long downTime, int action, float x, float y, 
            float pressure, float size) {
    }

    private void showUsage() {
        System.err.println("usage: input [text|keyevent]");
        System.err.println("       input text <string>");
        System.err.println("       input keyevent <event_code>");
>>>>>>> 54b6cfa... Initial Contribution
    }
}
