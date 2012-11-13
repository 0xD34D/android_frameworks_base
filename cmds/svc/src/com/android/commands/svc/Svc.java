/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.commands.svc;

<<<<<<< HEAD
=======
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IInstrumentationWatcher;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.os.Bundle;
import android.os.ServiceManager;
import android.view.IWindowManager;

import java.util.Iterator;
import java.util.Set;

>>>>>>> 54b6cfa... Initial Contribution
public class Svc {

    public static abstract class Command {
        private String mName;

        public Command(String name) {
            mName = name;
        }

        public String name() {
            return mName;
        }

        public abstract String shortHelp();         // should fit on one short line
        public abstract String longHelp();          // take as much space as you need, 75 col max
        public abstract void run(String[] args);    // run the command
    }

    public static void main(String[] args) {
<<<<<<< HEAD
=======
        if (true) {
            for (String a: args) {
                System.err.print(a + " ");
            }
            System.err.println();
        }

>>>>>>> 54b6cfa... Initial Contribution
        if (args.length >= 1) {
            Command c = lookupCommand(args[0]);
            if (c != null) {
                c.run(args);
                return;
            }
        }
        COMMAND_HELP.run(args);
    }

<<<<<<< HEAD
    private static Command lookupCommand(String name) {
=======
    private static final Command lookupCommand(String name) {
>>>>>>> 54b6cfa... Initial Contribution
        final int N = COMMANDS.length;
        for (int i=0; i<N; i++) {
            Command c = COMMANDS[i];
            if (c.name().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public static final Command COMMAND_HELP = new Command("help") {
        public String shortHelp() {
            return "Show information about the subcommands";
        }
        public String longHelp() {
            return shortHelp();
        }
        public void run(String[] args) {
            if (args.length == 2) {
                Command c = lookupCommand(args[1]);
                if (c != null) {
                    System.err.println(c.longHelp());
                    return;
                }
            }

            System.err.println("Available commands:");
            final int N = COMMANDS.length;
            int maxlen = 0;
            for (int i=0; i<N; i++) {
                Command c = COMMANDS[i];
                int len = c.name().length();
                if (maxlen < len) {
                    maxlen = len;
                }
            }
            String format = "    %-" + maxlen + "s    %s";
            for (int i=0; i<N; i++) {
                Command c = COMMANDS[i];
                System.err.println(String.format(format, c.name(), c.shortHelp()));
            }
        }
    };

    public static final Command[] COMMANDS = new Command[] {
<<<<<<< HEAD
            COMMAND_HELP,
            new PowerCommand(),
            new DataCommand(),
            new WifiCommand(),
            new UsbCommand()
=======
        COMMAND_HELP,
        new PowerCommand(),
>>>>>>> 54b6cfa... Initial Contribution
    };
}
