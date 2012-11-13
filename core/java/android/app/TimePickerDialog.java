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

package android.app;

<<<<<<< HEAD
import com.android.internal.R;

=======
>>>>>>> 54b6cfa... Initial Contribution
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.pim.DateFormat;
>>>>>>> 54b6cfa... Initial Contribution
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

<<<<<<< HEAD
/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 *
 * <p>See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.</p>
 */
public class TimePickerDialog extends AlertDialog
        implements OnClickListener, OnTimeChangedListener {
=======
import com.android.internal.R;

import java.util.Calendar;

/**
 * A dialog that prompts the user for the time of day using a {@link TimePicker}.
 */
public class TimePickerDialog extends AlertDialog implements OnClickListener, 
        OnTimeChangedListener {
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute The minute that was set.
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";
<<<<<<< HEAD

    private final TimePicker mTimePicker;
    private final OnTimeSetListener mCallback;

=======
    
    private final TimePicker mTimePicker;
    private final OnTimeSetListener mCallback;
    private final Calendar mCalendar;
    private final java.text.DateFormat mDateFormat;
    
>>>>>>> 54b6cfa... Initial Contribution
    int mInitialHourOfDay;
    int mInitialMinute;
    boolean mIs24HourView;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public TimePickerDialog(Context context,
            OnTimeSetListener callBack,
            int hourOfDay, int minute, boolean is24HourView) {
<<<<<<< HEAD
        this(context, 0, callBack, hourOfDay, minute, is24HourView);
=======
        this(context, com.android.internal.R.style.Theme_Dialog_Alert,
                callBack, hourOfDay, minute, is24HourView);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public TimePickerDialog(Context context,
            int theme,
            OnTimeSetListener callBack,
            int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme);
        mCallback = callBack;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mIs24HourView = is24HourView;

<<<<<<< HEAD
        setIcon(0);
        setTitle(R.string.time_picker_dialog_title);

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, themeContext.getText(R.string.date_time_done), this);

        LayoutInflater inflater =
                (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
=======
        mDateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();
        updateTitle(mInitialHourOfDay, mInitialMinute);
        
        setButton(context.getText(R.string.date_time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
        setIcon(R.drawable.ic_dialog_time);
        
        LayoutInflater inflater = 
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
>>>>>>> 54b6cfa... Initial Contribution
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        setView(view);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // initialize state
<<<<<<< HEAD
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setOnTimeChangedListener(this);
    }

    public void onClick(DialogInterface dialog, int which) {
        tryNotifyTimeSet();
    }

    public void updateTime(int hourOfDay, int minutOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minutOfHour);
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        /* do nothing */
    }

    private void tryNotifyTimeSet() {
        if (mCallback != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
=======
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setOnTimeChangedListener(this);
    }
    
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTimePicker.clearFocus();
            mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(), 
>>>>>>> 54b6cfa... Initial Contribution
                    mTimePicker.getCurrentMinute());
        }
    }

<<<<<<< HEAD
    @Override
    protected void onStop() {
        tryNotifyTimeSet();
        super.onStop();
    }

=======
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        updateTitle(hourOfDay, minute);
    }
    
    public void updateTime(int hourOfDay, int minutOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minutOfHour);
    }

    private void updateTitle(int hour, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        setTitle(mDateFormat.format(mCalendar.getTime()));
    }
    
>>>>>>> 54b6cfa... Initial Contribution
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
<<<<<<< HEAD
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
=======
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setOnTimeChangedListener(this);
        updateTitle(hour, minute);
>>>>>>> 54b6cfa... Initial Contribution
    }
}
