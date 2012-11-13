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

package android.widget;

import android.annotation.Widget;
import android.content.Context;
<<<<<<< HEAD
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker.OnValueChangeListener;

import com.android.internal.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * A view for selecting the time of day, in either 24 hour or AM/PM mode. The
 * hour, each minute digit, and AM/PM (if applicable) can be conrolled by
 * vertical spinners. The hour can be entered by keyboard input. Entering in two
 * digit hours can be accomplished by hitting two digits within a timeout of
 * about a second (e.g. '1' then '2' to select 12). The minutes can be entered
 * by entering single digits. Under AM/PM mode, the user can hit 'a', 'A", 'p'
 * or 'P' to pick. For a dialog using this view, see
 * {@link android.app.TimePickerDialog}.
 *<p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide.
 * </p>
 */
@Widget
public class TimePicker extends FrameLayout {

    private static final boolean DEFAULT_ENABLED_STATE = true;

    private static final int HOURS_IN_HALF_DAY = 12;

    /**
     * A no-op callback used in the constructor to avoid null checks later in
     * the code.
=======
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.android.internal.R;
import com.android.internal.widget.NumberPicker;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * A view for selecting the time of day, in either 24 hour or AM/PM mode.
 *
 * The hour, each minute digit, and AM/PM (if applicable) can be conrolled by
 * vertical spinners.
 *
 * The hour can be entered by keyboard input.  Entering in two digit hours
 * can be accomplished by hitting two digits within a timeout of about a
 * second (e.g. '1' then '2' to select 12).
 *
 * The minutes can be entered by entering single digits.
 *
 * Under AM/PM mode, the user can hit 'a', 'A", 'p' or 'P' to pick.
 *
 * For a dialog using this view, see {@link android.app.TimePickerDialog}.
 */
@Widget
public class TimePicker extends FrameLayout {
    
    /**
     * A no-op callback used in the constructor to avoid null checks
     * later in the code.
>>>>>>> 54b6cfa... Initial Contribution
     */
    private static final OnTimeChangedListener NO_OP_CHANGE_LISTENER = new OnTimeChangedListener() {
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        }
    };
<<<<<<< HEAD

    // state
    private boolean mIs24HourView;

    private boolean mIsAm;

    // ui components
    private final NumberPicker mHourSpinner;

    private final NumberPicker mMinuteSpinner;

    private final NumberPicker mAmPmSpinner;

    private final EditText mHourSpinnerInput;

    private final EditText mMinuteSpinnerInput;

    private final EditText mAmPmSpinnerInput;

    private final TextView mDivider;

    // Note that the legacy implementation of the TimePicker is
    // using a button for toggling between AM/PM while the new
    // version uses a NumberPicker spinner. Therefore the code
    // accommodates these two cases to be backwards compatible.
    private final Button mAmPmButton;

    private final String[] mAmPmStrings;

    private boolean mIsEnabled = DEFAULT_ENABLED_STATE;

    // callbacks
    private OnTimeChangedListener mOnTimeChangedListener;

    private Calendar mTempCalendar;

    private Locale mCurrentLocale;

=======
    
    // state
    private int mCurrentHour = 0; // 0-23
    private int mCurrentMinute = 0; // 0-59
    private Boolean mIs24HourView = false;
    private boolean mIsAm;

    // ui components
    private final NumberPicker mHourPicker;
    private final NumberPicker mMinutePicker;
    private final Button mAmPmButton;
    private final String mAmText;
    private final String mPmText;
    
    // callbacks
    private OnTimeChangedListener mOnTimeChangedListener;

>>>>>>> 54b6cfa... Initial Contribution
    /**
     * The callback interface used to indicate the time has been adjusted.
     */
    public interface OnTimeChangedListener {

        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The current hour.
         * @param minute The current minute.
         */
        void onTimeChanged(TimePicker view, int hourOfDay, int minute);
    }

    public TimePicker(Context context) {
        this(context, null);
    }
<<<<<<< HEAD

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.timePickerStyle);
=======
    
    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

<<<<<<< HEAD
        // initialization based on locale
        setCurrentLocale(Locale.getDefault());

        // process style attributes
        TypedArray attributesArray = context.obtainStyledAttributes(
                attrs, R.styleable.TimePicker, defStyle, 0);
        int layoutResourceId = attributesArray.getResourceId(
                R.styleable.TimePicker_internalLayout, R.layout.time_picker);
        attributesArray.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResourceId, this, true);

        // hour
        mHourSpinner = (NumberPicker) findViewById(R.id.hour);
        mHourSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                updateInputState();
                if (!is24HourView()) {
                    if ((oldVal == HOURS_IN_HALF_DAY - 1 && newVal == HOURS_IN_HALF_DAY)
                            || (oldVal == HOURS_IN_HALF_DAY && newVal == HOURS_IN_HALF_DAY - 1)) {
                        mIsAm = !mIsAm;
                        updateAmPmControl();
=======
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.time_picker,
            this, // we are the parent
            true);

        // hour
        mHourPicker = (NumberPicker) findViewById(R.id.hour);
        mHourPicker.setOnChangeListener(new NumberPicker.OnChangedListener() {
            public void onChanged(NumberPicker spinner, int oldVal, int newVal) {
                mCurrentHour = newVal;
                if (!mIs24HourView) {
                    // adjust from [1-12] to [0-11] internally, with the times
                    // written "12:xx" being the start of the half-day
                    if (mCurrentHour == 12) {
                        mCurrentHour = 0;
                    }
                    if (!mIsAm) {
                        // PM means 12 hours later than nominal
                        mCurrentHour += 12;
>>>>>>> 54b6cfa... Initial Contribution
                    }
                }
                onTimeChanged();
            }
        });
<<<<<<< HEAD
        mHourSpinnerInput = (EditText) mHourSpinner.findViewById(R.id.numberpicker_input);
        mHourSpinnerInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        // divider (only for the new widget style)
        mDivider = (TextView) findViewById(R.id.divider);
        if (mDivider != null) {
            mDivider.setText(R.string.time_picker_separator);
        }

        // minute
        mMinuteSpinner = (NumberPicker) findViewById(R.id.minute);
        mMinuteSpinner.setMinValue(0);
        mMinuteSpinner.setMaxValue(59);
        mMinuteSpinner.setOnLongPressUpdateInterval(100);
        mMinuteSpinner.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        mMinuteSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker spinner, int oldVal, int newVal) {
                updateInputState();
                int minValue = mMinuteSpinner.getMinValue();
                int maxValue = mMinuteSpinner.getMaxValue();
                if (oldVal == maxValue && newVal == minValue) {
                    int newHour = mHourSpinner.getValue() + 1;
                    if (!is24HourView() && newHour == HOURS_IN_HALF_DAY) {
                        mIsAm = !mIsAm;
                        updateAmPmControl();
                    }
                    mHourSpinner.setValue(newHour);
                } else if (oldVal == minValue && newVal == maxValue) {
                    int newHour = mHourSpinner.getValue() - 1;
                    if (!is24HourView() && newHour == HOURS_IN_HALF_DAY - 1) {
                        mIsAm = !mIsAm;
                        updateAmPmControl();
                    }
                    mHourSpinner.setValue(newHour);
                }
                onTimeChanged();
            }
        });
        mMinuteSpinnerInput = (EditText) mMinuteSpinner.findViewById(R.id.numberpicker_input);
        mMinuteSpinnerInput.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        /* Get the localized am/pm strings and use them in the spinner */
        mAmPmStrings = new DateFormatSymbols().getAmPmStrings();

        // am/pm
        View amPmView = findViewById(R.id.amPm);
        if (amPmView instanceof Button) {
            mAmPmSpinner = null;
            mAmPmSpinnerInput = null;
            mAmPmButton = (Button) amPmView;
            mAmPmButton.setOnClickListener(new OnClickListener() {
                public void onClick(View button) {
                    button.requestFocus();
                    mIsAm = !mIsAm;
                    updateAmPmControl();
                    onTimeChanged();
                }
            });
        } else {
            mAmPmButton = null;
            mAmPmSpinner = (NumberPicker) amPmView;
            mAmPmSpinner.setMinValue(0);
            mAmPmSpinner.setMaxValue(1);
            mAmPmSpinner.setDisplayedValues(mAmPmStrings);
            mAmPmSpinner.setOnValueChangedListener(new OnValueChangeListener() {
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    updateInputState();
                    picker.requestFocus();
                    mIsAm = !mIsAm;
                    updateAmPmControl();
                    onTimeChanged();
                }
            });
            mAmPmSpinnerInput = (EditText) mAmPmSpinner.findViewById(R.id.numberpicker_input);
            mAmPmSpinnerInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

        // update controls to initial state
        updateHourControl();
        updateAmPmControl();

        setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);

        // set to current time
        setCurrentHour(mTempCalendar.get(Calendar.HOUR_OF_DAY));
        setCurrentMinute(mTempCalendar.get(Calendar.MINUTE));

        if (!isEnabled()) {
            setEnabled(false);
        }

        // set the content descriptions
        setContentDescriptions();

        // If not explicitly specified this view is important for accessibility.
        if (getImportantForAccessibility() == IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mIsEnabled == enabled) {
            return;
        }
        super.setEnabled(enabled);
        mMinuteSpinner.setEnabled(enabled);
        if (mDivider != null) {
            mDivider.setEnabled(enabled);
        }
        mHourSpinner.setEnabled(enabled);
        if (mAmPmSpinner != null) {
            mAmPmSpinner.setEnabled(enabled);
        } else {
            mAmPmButton.setEnabled(enabled);
        }
        mIsEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setCurrentLocale(newConfig.locale);
    }

    /**
     * Sets the current locale.
     *
     * @param locale The current locale.
     */
    private void setCurrentLocale(Locale locale) {
        if (locale.equals(mCurrentLocale)) {
            return;
        }
        mCurrentLocale = locale;
        mTempCalendar = Calendar.getInstance(locale);
=======

        // digits of minute
        mMinutePicker = (NumberPicker) findViewById(R.id.minute);
        mMinutePicker.setRange(0, 59);
        mMinutePicker.setSpeed(100);
        mMinutePicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        mMinutePicker.setOnChangeListener(new NumberPicker.OnChangedListener() {
            public void onChanged(NumberPicker spinner, int oldVal, int newVal) {
                mCurrentMinute = newVal;
                onTimeChanged();
            }
        });

        // am/pm
        mAmPmButton = (Button) findViewById(R.id.amPm);

        // now that the hour/minute picker objects have been initialized, set
        // the hour range properly based on the 12/24 hour display mode.
        configurePickerRanges();

        // initialize to current time
        Calendar cal = Calendar.getInstance();
        setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);
        
        // by default we're not in 24 hour mode
        setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        setCurrentMinute(cal.get(Calendar.MINUTE));
        
        mIsAm = (mCurrentHour < 12);
        
        /* Get the localized am/pm strings and use them in the spinner */
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] dfsAmPm = dfs.getAmPmStrings();
        mAmText = dfsAmPm[Calendar.AM];
        mPmText = dfsAmPm[Calendar.PM];
        mAmPmButton.setText(mIsAm ? mAmText : mPmText);
        mAmPmButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                requestFocus();
                if (mIsAm) {
                    
                    // Currently AM switching to PM
                    if (mCurrentHour < 12) {
                        mCurrentHour += 12;
                    }                
                } else {
                    
                    // Currently PM switching to AM
                    if (mCurrentHour >= 12) {
                        mCurrentHour -= 12;
                    }
                }
                mIsAm = !mIsAm;
                mAmPmButton.setText(mIsAm ? mAmText : mPmText);
                onTimeChanged();
            }
        });
        
        if (!isEnabled()) {
            setEnabled(false);
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mMinutePicker.setEnabled(enabled);
        mHourPicker.setEnabled(enabled);
        mAmPmButton.setEnabled(enabled);
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Used to save / restore state of time picker
     */
    private static class SavedState extends BaseSavedState {

        private final int mHour;
<<<<<<< HEAD

=======
>>>>>>> 54b6cfa... Initial Contribution
        private final int mMinute;

        private SavedState(Parcelable superState, int hour, int minute) {
            super(superState);
            mHour = hour;
            mMinute = minute;
        }
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        private SavedState(Parcel in) {
            super(in);
            mHour = in.readInt();
            mMinute = in.readInt();
        }

        public int getHour() {
            return mHour;
        }

        public int getMinute() {
            return mMinute;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mHour);
            dest.writeInt(mMinute);
        }

<<<<<<< HEAD
        @SuppressWarnings({"unused", "hiding"})
        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
=======
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
>>>>>>> 54b6cfa... Initial Contribution
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
<<<<<<< HEAD
        return new SavedState(superState, getCurrentHour(), getCurrentMinute());
=======
        return new SavedState(superState, mCurrentHour, mCurrentMinute);
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentHour(ss.getHour());
        setCurrentMinute(ss.getMinute());
    }

    /**
     * Set the callback that indicates the time has been adjusted by the user.
<<<<<<< HEAD
     *
=======
>>>>>>> 54b6cfa... Initial Contribution
     * @param onTimeChangedListener the callback, should not be null.
     */
    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        mOnTimeChangedListener = onTimeChangedListener;
    }

    /**
<<<<<<< HEAD
     * @return The current hour in the range (0-23).
     */
    public Integer getCurrentHour() {
        int currentHour = mHourSpinner.getValue();
        if (is24HourView()) {
            return currentHour;
        } else if (mIsAm) {
            return currentHour % HOURS_IN_HALF_DAY;
        } else {
            return (currentHour % HOURS_IN_HALF_DAY) + HOURS_IN_HALF_DAY;
        }
=======
     * @return The current hour (0-23).
     */
    public Integer getCurrentHour() {
        return mCurrentHour;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Set the current hour.
     */
    public void setCurrentHour(Integer currentHour) {
<<<<<<< HEAD
        // why was Integer used in the first place?
        if (currentHour == null || currentHour == getCurrentHour()) {
            return;
        }
        if (!is24HourView()) {
            // convert [0,23] ordinal to wall clock display
            if (currentHour >= HOURS_IN_HALF_DAY) {
                mIsAm = false;
                if (currentHour > HOURS_IN_HALF_DAY) {
                    currentHour = currentHour - HOURS_IN_HALF_DAY;
                }
            } else {
                mIsAm = true;
                if (currentHour == 0) {
                    currentHour = HOURS_IN_HALF_DAY;
                }
            }
            updateAmPmControl();
        }
        mHourSpinner.setValue(currentHour);
        onTimeChanged();
=======
        this.mCurrentHour = currentHour;
        updateHourDisplay();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Set whether in 24 hour or AM/PM mode.
<<<<<<< HEAD
     *
     * @param is24HourView True = 24 hour mode. False = AM/PM.
     */
    public void setIs24HourView(Boolean is24HourView) {
        if (mIs24HourView == is24HourView) {
            return;
        }
        mIs24HourView = is24HourView;
        // cache the current hour since spinner range changes
        int currentHour = getCurrentHour();
        updateHourControl();
        // set value after spinner range is updated
        setCurrentHour(currentHour);
        updateAmPmControl();
=======
     * @param is24HourView True = 24 hour mode. False = AM/PM.
     */
    public void setIs24HourView(Boolean is24HourView) {
        if (mIs24HourView != is24HourView) {
            mIs24HourView = is24HourView;
            configurePickerRanges();
            updateHourDisplay();
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * @return true if this is in 24 hour view else false.
     */
    public boolean is24HourView() {
        return mIs24HourView;
    }
<<<<<<< HEAD

=======
    
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * @return The current minute.
     */
    public Integer getCurrentMinute() {
<<<<<<< HEAD
        return mMinuteSpinner.getValue();
=======
        return mCurrentMinute;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Set the current minute (0-59).
     */
    public void setCurrentMinute(Integer currentMinute) {
<<<<<<< HEAD
        if (currentMinute == getCurrentMinute()) {
            return;
        }
        mMinuteSpinner.setValue(currentMinute);
        onTimeChanged();
=======
        this.mCurrentMinute = currentMinute;
        updateMinuteDisplay();
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public int getBaseline() {
<<<<<<< HEAD
        return mHourSpinner.getBaseline();
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);

        int flags = DateUtils.FORMAT_SHOW_TIME;
        if (mIs24HourView) {
            flags |= DateUtils.FORMAT_24HOUR;
        } else {
            flags |= DateUtils.FORMAT_12HOUR;
        }
        mTempCalendar.set(Calendar.HOUR_OF_DAY, getCurrentHour());
        mTempCalendar.set(Calendar.MINUTE, getCurrentMinute());
        String selectedDateUtterance = DateUtils.formatDateTime(mContext,
                mTempCalendar.getTimeInMillis(), flags);
        event.getText().add(selectedDateUtterance);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(TimePicker.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(TimePicker.class.getName());
    }

    private void updateHourControl() {
        if (is24HourView()) {
            mHourSpinner.setMinValue(0);
            mHourSpinner.setMaxValue(23);
            mHourSpinner.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        } else {
            mHourSpinner.setMinValue(1);
            mHourSpinner.setMaxValue(12);
            mHourSpinner.setFormatter(null);
        }
    }

    private void updateAmPmControl() {
        if (is24HourView()) {
            if (mAmPmSpinner != null) {
                mAmPmSpinner.setVisibility(View.GONE);
            } else {
                mAmPmButton.setVisibility(View.GONE);
            }
        } else {
            int index = mIsAm ? Calendar.AM : Calendar.PM;
            if (mAmPmSpinner != null) {
                mAmPmSpinner.setValue(index);
                mAmPmSpinner.setVisibility(View.VISIBLE);
            } else {
                mAmPmButton.setText(mAmPmStrings[index]);
                mAmPmButton.setVisibility(View.VISIBLE);
            }
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
    }

    private void onTimeChanged() {
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
        if (mOnTimeChangedListener != null) {
            mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute());
        }
    }

    private void setContentDescriptions() {
        // Minute
        trySetContentDescription(mMinuteSpinner, R.id.increment,
                R.string.time_picker_increment_minute_button);
        trySetContentDescription(mMinuteSpinner, R.id.decrement,
                R.string.time_picker_decrement_minute_button);
        // Hour
        trySetContentDescription(mHourSpinner, R.id.increment,
                R.string.time_picker_increment_hour_button);
        trySetContentDescription(mHourSpinner, R.id.decrement,
                R.string.time_picker_decrement_hour_button);
        // AM/PM
        if (mAmPmSpinner != null) {
            trySetContentDescription(mAmPmSpinner, R.id.increment,
                    R.string.time_picker_increment_set_pm_button);
            trySetContentDescription(mAmPmSpinner, R.id.decrement,
                    R.string.time_picker_decrement_set_am_button);
        }
    }

    private void trySetContentDescription(View root, int viewId, int contDescResId) {
        View target = root.findViewById(viewId);
        if (target != null) {
            target.setContentDescription(mContext.getString(contDescResId));
        }
    }

    private void updateInputState() {
        // Make sure that if the user changes the value and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the value via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // value and having the IME up makes no sense.
        InputMethodManager inputMethodManager = InputMethodManager.peekInstance();
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mHourSpinnerInput)) {
                mHourSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mMinuteSpinnerInput)) {
                mMinuteSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mAmPmSpinnerInput)) {
                mAmPmSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }
}
=======
        return mHourPicker.getBaseline(); 
    }

    /**
     * Set the state of the spinners appropriate to the current hour.
     */
    private void updateHourDisplay() {
        int currentHour = mCurrentHour;
        if (!mIs24HourView) {
            // convert [0,23] ordinal to wall clock display
            if (currentHour > 12) currentHour -= 12;
            else if (currentHour == 0) currentHour = 12;
        }
        mHourPicker.setCurrent(currentHour);
        mIsAm = mCurrentHour < 12;
        mAmPmButton.setText(mIsAm ? mAmText : mPmText);
        onTimeChanged();
    }

    private void configurePickerRanges() {
        if (mIs24HourView) {
            mHourPicker.setRange(0, 23);
            mHourPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
            mAmPmButton.setVisibility(View.GONE);
        } else {
            mHourPicker.setRange(1, 12);
            mHourPicker.setFormatter(null);
            mAmPmButton.setVisibility(View.VISIBLE);
        }
    }

    private void onTimeChanged() {
        mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute());
    }

    /**
     * Set the state of the spinners appropriate to the current minute.
     */
    private void updateMinuteDisplay() {
        mMinutePicker.setCurrent(mCurrentMinute);
        mOnTimeChangedListener.onTimeChanged(this, getCurrentHour(), getCurrentMinute());
    }
}

>>>>>>> 54b6cfa... Initial Contribution
