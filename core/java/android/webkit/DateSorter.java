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

package android.webkit;

import android.content.Context;
<<<<<<< HEAD
import android.content.res.Resources;
=======
import android.util.Log;
>>>>>>> 54b6cfa... Initial Contribution

import java.util.Calendar;
import java.util.Date;

/**
 * Sorts dates into the following groups:
 *   Today
 *   Yesterday
<<<<<<< HEAD
 *   seven days ago
=======
 *   five days ago
>>>>>>> 54b6cfa... Initial Contribution
 *   one month ago
 *   older than a month ago
 */

public class DateSorter {

    private static final String LOGTAG = "webkit";

    /** must be >= 3 */
    public static final int DAY_COUNT = 5;

<<<<<<< HEAD
    private long [] mBins = new long[DAY_COUNT-1];
    private String [] mLabels = new String[DAY_COUNT];
    
    private static final int NUM_DAYS_AGO = 7;
=======
    private long [] mBins = new long[DAY_COUNT];
    private String [] mLabels = new String[DAY_COUNT];

    Date mDate = new Date();
    Calendar mCal = Calendar.getInstance();
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * @param context Application context
     */
    public DateSorter(Context context) {
<<<<<<< HEAD
        Resources resources = context.getResources();
=======
>>>>>>> 54b6cfa... Initial Contribution

        Calendar c = Calendar.getInstance();
        beginningOfDay(c);
        
        // Create the bins
        mBins[0] = c.getTimeInMillis(); // Today
<<<<<<< HEAD
        c.add(Calendar.DAY_OF_YEAR, -1);
        mBins[1] = c.getTimeInMillis();  // Yesterday
        c.add(Calendar.DAY_OF_YEAR, -(NUM_DAYS_AGO - 1));
        mBins[2] = c.getTimeInMillis();  // Five days ago
        c.add(Calendar.DAY_OF_YEAR, NUM_DAYS_AGO); // move back to today
        c.add(Calendar.MONTH, -1);
        mBins[3] = c.getTimeInMillis();  // One month ago
=======
        c.roll(Calendar.DAY_OF_YEAR, -1);
        mBins[1] = c.getTimeInMillis();  // Yesterday
        c.roll(Calendar.DAY_OF_YEAR, -4);
        mBins[2] = c.getTimeInMillis();  // Five days ago
        c.roll(Calendar.DAY_OF_YEAR, 5); // move back to today
        c.roll(Calendar.MONTH, -1);
        mBins[3] = c.getTimeInMillis();  // One month ago
        c.roll(Calendar.MONTH, -1);
        mBins[4] = c.getTimeInMillis();  // Over one month ago
>>>>>>> 54b6cfa... Initial Contribution

        // build labels
        mLabels[0] = context.getText(com.android.internal.R.string.today).toString();
        mLabels[1] = context.getText(com.android.internal.R.string.yesterday).toString();
<<<<<<< HEAD

        int resId = com.android.internal.R.plurals.last_num_days;
        String format = resources.getQuantityString(resId, NUM_DAYS_AGO);
        mLabels[2] = String.format(format, NUM_DAYS_AGO);

        mLabels[3] = context.getString(com.android.internal.R.string.last_month);
        mLabels[4] = context.getString(com.android.internal.R.string.older);
=======
        mLabels[2] = context.getString(com.android.internal.R.string.daysDurationPastPlural, 5);
        mLabels[3] = context.getText(com.android.internal.R.string.oneMonthDurationPast).toString();
        StringBuilder sb = new StringBuilder();
        sb.append(context.getText(com.android.internal.R.string.before)).append(" ");
        sb.append(context.getText(com.android.internal.R.string.oneMonthDurationPast));
        mLabels[4] = sb.toString();
                

>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @param time time since the Epoch in milliseconds, such as that
     * returned by Calendar.getTimeInMillis()
     * @return an index from 0 to (DAY_COUNT - 1) that identifies which
     * date bin this date belongs to
     */
    public int getIndex(long time) {
<<<<<<< HEAD
        int lastDay = DAY_COUNT - 1;
        for (int i = 0; i < lastDay; i++) {
            if (time > mBins[i]) return i;
        }
        return lastDay;
=======
        // Lame linear search
        for (int i = 0; i < DAY_COUNT; i++) {
            if (time > mBins[i]) return i;
        }
        return DAY_COUNT - 1;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @param index date bin index as returned by getIndex()
     * @return string label suitable for display to user
     */
    public String getLabel(int index) {
<<<<<<< HEAD
        if (index < 0 || index >= DAY_COUNT) return "";
=======
>>>>>>> 54b6cfa... Initial Contribution
        return mLabels[index];
    }


    /**
     * @param index date bin index as returned by getIndex()
     * @return date boundary at given index
     */
    public long getBoundary(int index) {
<<<<<<< HEAD
        int lastDay = DAY_COUNT - 1;
        // Error case
        if (index < 0 || index > lastDay) index = 0;
        // Since this provides a lower boundary on dates that will be included
        // in the given bin, provide the smallest value
        if (index == lastDay) return Long.MIN_VALUE;
=======
>>>>>>> 54b6cfa... Initial Contribution
        return mBins[index];
    }

    /**
     * Calcuate 12:00am by zeroing out hour, minute, second, millisecond
     */
<<<<<<< HEAD
    private void beginningOfDay(Calendar c) {
=======
    private Calendar beginningOfDay(Calendar c) {
>>>>>>> 54b6cfa... Initial Contribution
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
<<<<<<< HEAD
=======
        return c;
>>>>>>> 54b6cfa... Initial Contribution
    }
}
