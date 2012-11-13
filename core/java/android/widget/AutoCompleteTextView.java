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

import android.content.Context;
import android.content.res.TypedArray;
<<<<<<< HEAD
import android.database.DataSetObserver;
=======
>>>>>>> 54b6cfa... Initial Contribution
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
<<<<<<< HEAD
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
=======
import android.util.AttributeSet;
>>>>>>> 54b6cfa... Initial Contribution
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.view.WindowManager;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
=======
>>>>>>> 54b6cfa... Initial Contribution

import com.android.internal.R;


/**
 * <p>An editable text view that shows completion suggestions automatically
 * while the user is typing. The list of suggestions is displayed in a drop
 * down menu from which the user can choose an item to replace the content
 * of the edit box with.</p>
 *
 * <p>The drop down can be dismissed at any time by pressing the back key or,
 * if no item is selected in the drop down, by pressing the enter/dpad center
 * key.</p>
 *
 * <p>The list of suggestions is obtained from a data adapter and appears
 * only after a given number of characters defined by
 * {@link #getThreshold() the threshold}.</p>
 *
 * <p>The following code snippet shows how to create a text view which suggests
 * various countries names while the user is typing:</p>
 *
 * <pre class="prettyprint">
 * public class CountriesActivity extends Activity {
 *     protected void onCreate(Bundle icicle) {
 *         super.onCreate(icicle);
 *         setContentView(R.layout.countries);
 *
<<<<<<< HEAD
 *         ArrayAdapter&lt;String&gt; adapter = new ArrayAdapter&lt;String&gt;(this,
=======
 *         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
>>>>>>> 54b6cfa... Initial Contribution
 *                 android.R.layout.simple_dropdown_item_1line, COUNTRIES);
 *         AutoCompleteTextView textView = (AutoCompleteTextView)
 *                 findViewById(R.id.countries_list);
 *         textView.setAdapter(adapter);
 *     }
 *
 *     private static final String[] COUNTRIES = new String[] {
 *         "Belgium", "France", "Italy", "Germany", "Spain"
 *     };
 * }
 * </pre>
 *
<<<<<<< HEAD
 * <p>See the <a href="{@docRoot}guide/topics/ui/controls/text.html">Text Fields</a>
 * guide.</p>
 *
=======
>>>>>>> 54b6cfa... Initial Contribution
 * @attr ref android.R.styleable#AutoCompleteTextView_completionHint
 * @attr ref android.R.styleable#AutoCompleteTextView_completionThreshold
 * @attr ref android.R.styleable#AutoCompleteTextView_completionHintView
 * @attr ref android.R.styleable#AutoCompleteTextView_dropDownSelector
<<<<<<< HEAD
 * @attr ref android.R.styleable#AutoCompleteTextView_dropDownAnchor
 * @attr ref android.R.styleable#AutoCompleteTextView_dropDownWidth
 * @attr ref android.R.styleable#AutoCompleteTextView_dropDownHeight
 * @attr ref android.R.styleable#AutoCompleteTextView_dropDownVerticalOffset
 * @attr ref android.R.styleable#AutoCompleteTextView_dropDownHorizontalOffset
 */
public class AutoCompleteTextView extends EditText implements Filter.FilterListener {
    static final boolean DEBUG = false;
    static final String TAG = "AutoCompleteTextView";

    static final int EXPAND_MAX = 3;

    private CharSequence mHintText;
    private TextView mHintView;
=======
 */
public class AutoCompleteTextView extends EditText implements Filter.FilterListener {
    private static final int HINT_VIEW_ID = 0x17;

    private CharSequence mHintText;
>>>>>>> 54b6cfa... Initial Contribution
    private int mHintResource;

    private ListAdapter mAdapter;
    private Filter mFilter;
    private int mThreshold;

<<<<<<< HEAD
    private ListPopupWindow mPopup;
    private int mDropDownAnchorId;
=======
    private PopupWindow mPopup;
    private DropDownListView mDropDownList;

    private Drawable mDropDownListHighlight;
>>>>>>> 54b6cfa... Initial Contribution

    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;

<<<<<<< HEAD
    private boolean mDropDownDismissedOnCompletion = true;

    private int mLastKeyCode = KeyEvent.KEYCODE_UNKNOWN;
    private boolean mOpenBefore;

    private Validator mValidator = null;

    // Set to true when text is set directly and no filtering shall be performed
    private boolean mBlockCompletion;

    // When set, an update in the underlying adapter will update the result list popup.
    // Set to false when the list is hidden to prevent asynchronous updates to popup the list again.
    private boolean mPopupCanBeUpdated = true;

    private PassThroughClickListener mPassThroughClickListener;
    private PopupDataSetObserver mObserver;
=======
    private final DropDownItemClickListener mDropDownItemClickListener =
            new DropDownItemClickListener();

    private boolean mTextChanged;
>>>>>>> 54b6cfa... Initial Contribution

    public AutoCompleteTextView(Context context) {
        this(context, null);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.autoCompleteTextViewStyle);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

<<<<<<< HEAD
        mPopup = new ListPopupWindow(context, attrs,
                com.android.internal.R.attr.autoCompleteTextViewStyle);
        mPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopup.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
=======
        mPopup = new PopupWindow(context, attrs, com.android.internal.R.attr.autoCompleteTextViewStyle);
>>>>>>> 54b6cfa... Initial Contribution

        TypedArray a =
            context.obtainStyledAttributes(
                attrs, com.android.internal.R.styleable.AutoCompleteTextView, defStyle, 0);

        mThreshold = a.getInt(
                R.styleable.AutoCompleteTextView_completionThreshold, 2);

<<<<<<< HEAD
        mPopup.setListSelector(a.getDrawable(R.styleable.AutoCompleteTextView_dropDownSelector));
        mPopup.setVerticalOffset((int)
                a.getDimension(R.styleable.AutoCompleteTextView_dropDownVerticalOffset, 0.0f));
        mPopup.setHorizontalOffset((int)
                a.getDimension(R.styleable.AutoCompleteTextView_dropDownHorizontalOffset, 0.0f));
        
        // Get the anchor's id now, but the view won't be ready, so wait to actually get the
        // view and store it in mDropDownAnchorView lazily in getDropDownAnchorView later.
        // Defaults to NO_ID, in which case the getDropDownAnchorView method will simply return
        // this TextView, as a default anchoring point.
        mDropDownAnchorId = a.getResourceId(R.styleable.AutoCompleteTextView_dropDownAnchor,
                View.NO_ID);
        
        // For dropdown width, the developer can specify a specific width, or MATCH_PARENT
        // (for full screen width) or WRAP_CONTENT (to match the width of the anchored view).
        mPopup.setWidth(a.getLayoutDimension(
                R.styleable.AutoCompleteTextView_dropDownWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mPopup.setHeight(a.getLayoutDimension(
                R.styleable.AutoCompleteTextView_dropDownHeight,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mHintResource = a.getResourceId(R.styleable.AutoCompleteTextView_completionHintView,
                R.layout.simple_dropdown_hint);
        
        mPopup.setOnItemClickListener(new DropDownItemClickListener());
        setCompletionHint(a.getText(R.styleable.AutoCompleteTextView_completionHint));

        // Always turn on the auto complete input type flag, since it
        // makes no sense to use this widget without it.
        int inputType = getInputType();
        if ((inputType&EditorInfo.TYPE_MASK_CLASS)
                == EditorInfo.TYPE_CLASS_TEXT) {
            inputType |= EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE;
            setRawInputType(inputType);
        }
=======
        mHintText = a.getText(R.styleable.AutoCompleteTextView_completionHint);

        mDropDownListHighlight = a.getDrawable(
                R.styleable.AutoCompleteTextView_dropDownSelector);

        mHintResource = a.getResourceId(R.styleable.AutoCompleteTextView_completionHintView,
                R.layout.simple_dropdown_hint);
>>>>>>> 54b6cfa... Initial Contribution

        a.recycle();

        setFocusable(true);
<<<<<<< HEAD

        addTextChangedListener(new MyWatcher());
        
        mPassThroughClickListener = new PassThroughClickListener();
        super.setOnClickListener(mPassThroughClickListener);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        mPassThroughClickListener.mWrapped = listener;
    }

    /**
     * Private hook into the on click event, dispatched from {@link PassThroughClickListener}
     */
    private void onClickImpl() {
        // If the dropdown is showing, bring the keyboard to the front
        // when the user touches the text field.
        if (isPopupShowing()) {
            ensureImeVisible(true);
        }
=======
    }

    /**
     * Sets this to be single line; a separate method so
     * MultiAutoCompleteTextView can skip this.
     */
    /* package */ void finishInit() {
        setSingleLine();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * <p>Sets the optional hint text that is displayed at the bottom of the
     * the matching list.  This can be used as a cue to the user on how to
     * best use the list, or to provide extra information.</p>
     *
     * @param hint the text to be displayed to the user
     *
<<<<<<< HEAD
     * @see #getCompletionHint()
     *
=======
>>>>>>> 54b6cfa... Initial Contribution
     * @attr ref android.R.styleable#AutoCompleteTextView_completionHint
     */
    public void setCompletionHint(CharSequence hint) {
        mHintText = hint;
<<<<<<< HEAD
        if (hint != null) {
            if (mHintView == null) {
                final TextView hintView = (TextView) LayoutInflater.from(getContext()).inflate(
                        mHintResource, null).findViewById(com.android.internal.R.id.text1);
                hintView.setText(mHintText);
                mHintView = hintView;
                mPopup.setPromptView(hintView);
            } else {
                mHintView.setText(hint);
            }
        } else {
            mPopup.setPromptView(null);
            mHintView = null;
        }
    }

    /**
     * Gets the optional hint text displayed at the bottom of the the matching list.
     *
     * @return The hint text, if any
     *
     * @see #setCompletionHint(CharSequence)
     *
     * @attr ref android.R.styleable#AutoCompleteTextView_completionHint
     */
    public CharSequence getCompletionHint() {
        return mHintText;
    }

    /**
     * <p>Returns the current width for the auto-complete drop down list. This can
     * be a fixed width, or {@link ViewGroup.LayoutParams#MATCH_PARENT} to fill the screen, or
     * {@link ViewGroup.LayoutParams#WRAP_CONTENT} to fit the width of its anchor view.</p>
     * 
     * @return the width for the drop down list
     * 
     * @attr ref android.R.styleable#AutoCompleteTextView_dropDownWidth
     */
    public int getDropDownWidth() {
        return mPopup.getWidth();
    }
    
    /**
     * <p>Sets the current width for the auto-complete drop down list. This can
     * be a fixed width, or {@link ViewGroup.LayoutParams#MATCH_PARENT} to fill the screen, or
     * {@link ViewGroup.LayoutParams#WRAP_CONTENT} to fit the width of its anchor view.</p>
     * 
     * @param width the width to use
     * 
     * @attr ref android.R.styleable#AutoCompleteTextView_dropDownWidth
     */
    public void setDropDownWidth(int width) {
        mPopup.setWidth(width);
    }

    /**
     * <p>Returns the current height for the auto-complete drop down list. This can
     * be a fixed height, or {@link ViewGroup.LayoutParams#MATCH_PARENT} to fill
     * the screen, or {@link ViewGroup.LayoutParams#WRAP_CONTENT} to fit the height
     * of the drop down's content.</p>
     *
     * @return the height for the drop down list
     *
     * @attr ref android.R.styleable#AutoCompleteTextView_dropDownHeight
     */
    public int getDropDownHeight() {
        return mPopup.getHeight();
    }

    /**
     * <p>Sets the current height for the auto-complete drop down list. This can
     * be a fixed height, or {@link ViewGroup.LayoutParams#MATCH_PARENT} to fill
     * the screen, or {@link ViewGroup.LayoutParams#WRAP_CONTENT} to fit the height
     * of the drop down's content.</p>
     *
     * @param height the height to use
     *
     * @attr ref android.R.styleable#AutoCompleteTextView_dropDownHeight
     */
    public void setDropDownHeight(int height) {
        mPopup.setHeight(height);
    }
    
    /**
     * <p>Returns the id for the view that the auto-complete drop down list is anchored to.</p>
     *  
     * @return the view's id, or {@link View#NO_ID} if none specified
     * 
     * @attr ref android.R.styleable#AutoCompleteTextView_dropDownAnchor
     */
    public int getDropDownAnchor() {
        return mDropDownAnchorId;
    }
    
    /**
     * <p>Sets the view to which the auto-complete drop down list should anchor. The view
     * corresponding to this id will not be loaded until the next time it is needed to avoid
     * loading a view which is not yet instantiated.</p>
     * 
     * @param id the id to anchor the drop down list view to
     * 
     * @attr ref android.R.styleable#AutoCompleteTextView_dropDownAnchor 
     */
    public void setDropDownAnchor(int id) {
        mDropDownAnchorId = id;
        mPopup.setAnchorView(null);
    }
    
    /**
     * <p>Gets the background of the auto-complete drop-down list.</p>
     * 
     * @return the background drawable
     * 
     * @attr ref android.R.styleable#PopupWindow_popupBackground
     */
    public Drawable getDropDownBackground() {
        return mPopup.getBackground();
    }
    
    /**
     * <p>Sets the background of the auto-complete drop-down list.</p>
     * 
     * @param d the drawable to set as the background
     * 
     * @attr ref android.R.styleable#PopupWindow_popupBackground
     */
    public void setDropDownBackgroundDrawable(Drawable d) {
        mPopup.setBackgroundDrawable(d);
    }
    
    /**
     * <p>Sets the background of the auto-complete drop-down list.</p>
     * 
     * @param id the id of the drawable to set as the background
     * 
     * @attr ref android.R.styleable#PopupWindow_popupBackground
     */
    public void setDropDownBackgroundResource(int id) {
        mPopup.setBackgroundDrawable(getResources().getDrawable(id));
    }
    
    /**
     * <p>Sets the vertical offset used for the auto-complete drop-down list.</p>
     * 
     * @param offset the vertical offset
     */
    public void setDropDownVerticalOffset(int offset) {
        mPopup.setVerticalOffset(offset);
    }
    
    /**
     * <p>Gets the vertical offset used for the auto-complete drop-down list.</p>
     * 
     * @return the vertical offset
     */
    public int getDropDownVerticalOffset() {
        return mPopup.getVerticalOffset();
    }
    
    /**
     * <p>Sets the horizontal offset used for the auto-complete drop-down list.</p>
     * 
     * @param offset the horizontal offset
     */
    public void setDropDownHorizontalOffset(int offset) {
        mPopup.setHorizontalOffset(offset);
    }
    
    /**
     * <p>Gets the horizontal offset used for the auto-complete drop-down list.</p>
     * 
     * @return the horizontal offset
     */
    public int getDropDownHorizontalOffset() {
        return mPopup.getHorizontalOffset();
    }

     /**
     * <p>Sets the animation style of the auto-complete drop-down list.</p>
     *
     * <p>If the drop-down is showing, calling this method will take effect only
     * the next time the drop-down is shown.</p>
     *
     * @param animationStyle animation style to use when the drop-down appears
     *      and disappears.  Set to -1 for the default animation, 0 for no
     *      animation, or a resource identifier for an explicit animation.
     *
     * @hide Pending API council approval
     */
    public void setDropDownAnimationStyle(int animationStyle) {
        mPopup.setAnimationStyle(animationStyle);
    }

    /**
     * <p>Returns the animation style that is used when the drop-down list appears and disappears
     * </p>
     *
     * @return the animation style that is used when the drop-down list appears and disappears
     *
     * @hide Pending API council approval
     */
    public int getDropDownAnimationStyle() {
        return mPopup.getAnimationStyle();
    }

    /**
     * @return Whether the drop-down is visible as long as there is {@link #enoughToFilter()}
     *
     * @hide Pending API council approval
     */
    public boolean isDropDownAlwaysVisible() {
        return mPopup.isDropDownAlwaysVisible();
    }

    /**
     * Sets whether the drop-down should remain visible as long as there is there is
     * {@link #enoughToFilter()}.  This is useful if an unknown number of results are expected
     * to show up in the adapter sometime in the future.
     *
     * The drop-down will occupy the entire screen below {@link #getDropDownAnchor} regardless
     * of the size or content of the list.  {@link #getDropDownBackground()} will fill any space
     * that is not used by the list.
     *
     * @param dropDownAlwaysVisible Whether to keep the drop-down visible.
     *
     * @hide Pending API council approval
     */
    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        mPopup.setDropDownAlwaysVisible(dropDownAlwaysVisible);
    }
   
    /**
     * Checks whether the drop-down is dismissed when a suggestion is clicked.
     * 
     * @hide Pending API council approval
     */
    public boolean isDropDownDismissedOnCompletion() {
        return mDropDownDismissedOnCompletion;
    }

    /**
     * Sets whether the drop-down is dismissed when a suggestion is clicked. This is 
     * true by default.
     * 
     * @param dropDownDismissedOnCompletion Whether to dismiss the drop-down.
     * 
     * @hide Pending API council approval
     */
    public void setDropDownDismissedOnCompletion(boolean dropDownDismissedOnCompletion) {
        mDropDownDismissedOnCompletion = dropDownDismissedOnCompletion;
    }
 
    /**
=======
    }

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * <p>Returns the number of characters the user must type before the drop
     * down list is shown.</p>
     *
     * @return the minimum number of characters to type to show the drop down
     *
     * @see #setThreshold(int)
<<<<<<< HEAD
     *
     * @attr ref android.R.styleable#AutoCompleteTextView_completionThreshold
=======
>>>>>>> 54b6cfa... Initial Contribution
     */
    public int getThreshold() {
        return mThreshold;
    }

    /**
     * <p>Specifies the minimum number of characters the user has to type in the
     * edit box before the drop down list is shown.</p>
     *
     * <p>When <code>threshold</code> is less than or equals 0, a threshold of
     * 1 is applied.</p>
     *
     * @param threshold the number of characters to type before the drop down
     *                  is shown
     *
     * @see #getThreshold()
     *
     * @attr ref android.R.styleable#AutoCompleteTextView_completionThreshold
     */
    public void setThreshold(int threshold) {
        if (threshold <= 0) {
            threshold = 1;
        }

        mThreshold = threshold;
    }

    /**
     * <p>Sets the listener that will be notified when the user clicks an item
     * in the drop down list.</p>
     *
     * @param l the item click listener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        mItemClickListener = l;
    }

    /**
     * <p>Sets the listener that will be notified when the user selects an item
     * in the drop down list.</p>
     *
     * @param l the item selected listener
     */
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener l) {
        mItemSelectedListener = l;
    }

    /**
     * <p>Returns the listener that is notified whenever the user clicks an item
     * in the drop down list.</p>
     *
     * @return the item click listener
<<<<<<< HEAD
     *
     * @deprecated Use {@link #getOnItemClickListener()} intead
     */
    @Deprecated
=======
     */
>>>>>>> 54b6cfa... Initial Contribution
    public AdapterView.OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    /**
     * <p>Returns the listener that is notified whenever the user selects an
     * item in the drop down list.</p>
     *
     * @return the item selected listener
<<<<<<< HEAD
     *
     * @deprecated Use {@link #getOnItemSelectedListener()} intead
     */
    @Deprecated
=======
     */
>>>>>>> 54b6cfa... Initial Contribution
    public AdapterView.OnItemSelectedListener getItemSelectedListener() {
        return mItemSelectedListener;
    }

    /**
<<<<<<< HEAD
     * <p>Returns the listener that is notified whenever the user clicks an item
     * in the drop down list.</p>
     *
     * @return the item click listener
     */
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return mItemClickListener;
    }

    /**
     * <p>Returns the listener that is notified whenever the user selects an
     * item in the drop down list.</p>
     *
     * @return the item selected listener
     */
    public AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
        return mItemSelectedListener;
    }

    /**
=======
>>>>>>> 54b6cfa... Initial Contribution
     * <p>Returns a filterable list adapter used for auto completion.</p>
     *
     * @return a data adapter used for auto completion
     */
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * <p>Changes the list of data used for auto completion. The provided list
     * must be a filterable list adapter.</p>
<<<<<<< HEAD
     * 
     * <p>The caller is still responsible for managing any resources used by the adapter.
     * Notably, when the AutoCompleteTextView is closed or released, the adapter is not notified.
     * A common case is the use of {@link android.widget.CursorAdapter}, which
     * contains a {@link android.database.Cursor} that must be closed.  This can be done
     * automatically (see 
     * {@link android.app.Activity#startManagingCursor(android.database.Cursor) 
     * startManagingCursor()}),
     * or by manually closing the cursor when the AutoCompleteTextView is dismissed.</p>
=======
>>>>>>> 54b6cfa... Initial Contribution
     *
     * @param adapter the adapter holding the auto completion data
     *
     * @see #getAdapter()
     * @see android.widget.Filterable
     * @see android.widget.ListAdapter
     */
    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
<<<<<<< HEAD
        if (mObserver == null) {
            mObserver = new PopupDataSetObserver();
        } else if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
=======
>>>>>>> 54b6cfa... Initial Contribution
        mAdapter = adapter;
        if (mAdapter != null) {
            //noinspection unchecked
            mFilter = ((Filterable) mAdapter).getFilter();
<<<<<<< HEAD
            adapter.registerDataSetObserver(mObserver);
=======
>>>>>>> 54b6cfa... Initial Contribution
        } else {
            mFilter = null;
        }

<<<<<<< HEAD
        mPopup.setAdapter(mAdapter);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()
                && !mPopup.isDropDownAlwaysVisible()) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    dismissDropDown();
                    return true;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
=======
        if (mDropDownList != null) {
            mDropDownList.setAdapter(mAdapter);
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
<<<<<<< HEAD
        boolean consumed = mPopup.onKeyUp(keyCode, event);
        if (consumed) {
            switch (keyCode) {
            // if the list accepts the key events and the key event
            // was a click, the text view gets the selected item
            // from the drop down as its content
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_TAB:
                if (event.hasNoModifiers()) {
                    performCompletion();
                }
                return true;
            }
        }

        if (isPopupShowing() && keyCode == KeyEvent.KEYCODE_TAB && event.hasNoModifiers()) {
            performCompletion();
            return true;
        }

=======
        if (isPopupShowing()) {
            boolean consumed = mDropDownList.onKeyUp(keyCode, event);
            if (consumed) {
                switch (keyCode) {
                    // if the list accepts the key events and the key event
                    // was a click, the text view gets the selected item
                    // from the drop down as its content
                    case KeyEvent.KEYCODE_ENTER:
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                        performCompletion();
                        return true;
                }
            }
        }
>>>>>>> 54b6cfa... Initial Contribution
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
<<<<<<< HEAD
        if (mPopup.onKeyDown(keyCode, event)) {
            return true;
        }
        
        if (!isPopupShowing()) {
            switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (event.hasNoModifiers()) {
                    performValidation();
                }
            }
        }

        if (isPopupShowing() && keyCode == KeyEvent.KEYCODE_TAB && event.hasNoModifiers()) {
            return true;
        }

        mLastKeyCode = keyCode;
        boolean handled = super.onKeyDown(keyCode, event);
        mLastKeyCode = KeyEvent.KEYCODE_UNKNOWN;

        if (handled && isPopupShowing()) {
            clearListSelection();
=======
        // when the drop down is shown, we drive it directly
        if (isPopupShowing()) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismissDropDown();
                return true;

            // the key events are forwarded to the list in the drop down view
            // note that ListView handles space but we don't want that to happen
            } else if (keyCode != KeyEvent.KEYCODE_SPACE) {
                boolean consumed = mDropDownList.onKeyDown(keyCode, event);

                if (consumed) {
                    switch (keyCode) {
                        // avoid passing the focus from the text view to the
                        // next component
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_DPAD_UP:
                            return true;
                    }
                } else{
                    int index = mDropDownList.getSelectedItemPosition();
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_UP:
                            if (index == 0) {
                                return true;
                            }
                            break;
                        // when the selection is at the bottom, we block the
                        // event to avoid going to the next focusable widget
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            Adapter adapter = mDropDownList.getAdapter();
                            if (index == adapter.getCount() - 1) {
                                return true;
                            }
                            break;
                    }
                }
            }
        } else {
            switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                performValidation();
            }
        }

        // when text is changed, inserted or deleted, we attempt to show
        // the drop down
        boolean openBefore = isPopupShowing();
        mTextChanged = false;

        boolean handled = super.onKeyDown(keyCode, event);

        // if the list was open before the keystroke, but closed afterwards,
        // then something in the keystroke processing (an input filter perhaps)
        // called performCompletion() and we shouldn't do any more processing.
        if (openBefore && !isPopupShowing()) {
            return handled;
        }

        if (mTextChanged) { // would have been set in onTextChanged()
            // the drop down is shown only when a minimum number of characters
            // was typed in the text view
            if (enoughToFilter()) {
                if (mFilter != null) {
                    performFiltering(getText(), keyCode);
                }
            } else {
                // drop down is automatically dismissed when enough characters
                // are deleted from the text view
                dismissDropDown();
                if (mFilter != null) {
                    mFilter.filter(null);
                }
            }
            return true;
>>>>>>> 54b6cfa... Initial Contribution
        }

        return handled;
    }

    /**
     * Returns <code>true</code> if the amount of text in the field meets
     * or exceeds the {@link #getThreshold} requirement.  You can override
     * this to impose a different standard for when filtering will be
     * triggered.
     */
    public boolean enoughToFilter() {
<<<<<<< HEAD
        if (DEBUG) Log.v(TAG, "Enough to filter: len=" + getText().length()
                + " threshold=" + mThreshold);
        return getText().length() >= mThreshold;
    }

    /**
     * This is used to watch for edits to the text view.  Note that we call
     * to methods on the auto complete text view class so that we can access
     * private vars without going through thunks.
     */
    private class MyWatcher implements TextWatcher {
        public void afterTextChanged(Editable s) {
            doAfterTextChanged();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            doBeforeTextChanged();
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    void doBeforeTextChanged() {
        if (mBlockCompletion) return;

        // when text is changed, inserted or deleted, we attempt to show
        // the drop down
        mOpenBefore = isPopupShowing();
        if (DEBUG) Log.v(TAG, "before text changed: open=" + mOpenBefore);
    }

    void doAfterTextChanged() {
        if (mBlockCompletion) return;

        // if the list was open before the keystroke, but closed afterwards,
        // then something in the keystroke processing (an input filter perhaps)
        // called performCompletion() and we shouldn't do any more processing.
        if (DEBUG) Log.v(TAG, "after text changed: openBefore=" + mOpenBefore
                + " open=" + isPopupShowing());
        if (mOpenBefore && !isPopupShowing()) {
            return;
        }

        // the drop down is shown only when a minimum number of characters
        // was typed in the text view
        if (enoughToFilter()) {
            if (mFilter != null) {
                mPopupCanBeUpdated = true;
                performFiltering(getText(), mLastKeyCode);
            }
        } else {
            // drop down is automatically dismissed when enough characters
            // are deleted from the text view
            if (!mPopup.isDropDownAlwaysVisible()) {
                dismissDropDown();
            }
            if (mFilter != null) {
                mFilter.filter(null);
            }
        }
=======
        return getText().length() >= mThreshold;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before,
                                 int after) {
        super.onTextChanged(text, start, before, after);
        mTextChanged = true;
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * <p>Indicates whether the popup menu is showing.</p>
     *
     * @return true if the popup menu is showing, false otherwise
     */
    public boolean isPopupShowing() {
        return mPopup.isShowing();
    }

    /**
     * <p>Converts the selected item from the drop down list into a sequence
     * of character that can be used in the edit box.</p>
     *
     * @param selectedItem the item selected by the user for completion
     *
     * @return a sequence of characters representing the selected suggestion
     */
    protected CharSequence convertSelectionToString(Object selectedItem) {
        return mFilter.convertResultToString(selectedItem);
    }
<<<<<<< HEAD
    
    /**
     * <p>Clear the list selection.  This may only be temporary, as user input will often bring 
     * it back.
     */
    public void clearListSelection() {
        mPopup.clearListSelection();
    }
    
    /**
     * Set the position of the dropdown view selection.
     * 
     * @param position The position to move the selector to.
     */
    public void setListSelection(int position) {
        mPopup.setSelection(position);
    }

    /**
     * Get the position of the dropdown view selection, if there is one.  Returns 
     * {@link ListView#INVALID_POSITION ListView.INVALID_POSITION} if there is no dropdown or if
     * there is no selection.
     * 
     * @return the position of the current selection, if there is one, or 
     * {@link ListView#INVALID_POSITION ListView.INVALID_POSITION} if not.
     * 
     * @see ListView#getSelectedItemPosition()
     */
    public int getListSelection() {
        return mPopup.getSelectedItemPosition();
    }
=======
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * <p>Starts filtering the content of the drop down list. The filtering
     * pattern is the content of the edit box. Subclasses should override this
     * method to filter with a different pattern, for instance a substring of
     * <code>text</code>.</p>
     *
     * @param text the filtering pattern
<<<<<<< HEAD
     * @param keyCode the last character inserted in the edit box; beware that
     * this will be null when text is being added through a soft input method.
=======
     * @param keyCode the last character inserted in the edit box
>>>>>>> 54b6cfa... Initial Contribution
     */
    @SuppressWarnings({ "UnusedDeclaration" })
    protected void performFiltering(CharSequence text, int keyCode) {
        mFilter.filter(text, this);
    }

    /**
     * <p>Performs the text completion by converting the selected item from
     * the drop down list into a string, replacing the text box's content with
     * this string and finally dismissing the drop down menu.</p>
     */
    public void performCompletion() {
        performCompletion(null, -1, -1);
    }

<<<<<<< HEAD
    @Override
    public void onCommitCompletion(CompletionInfo completion) {
        if (isPopupShowing()) {
            mPopup.performItemClick(completion.getPosition());
        }
    }

    private void performCompletion(View selectedView, int position, long id) {
        if (isPopupShowing()) {
            Object selectedItem;
            if (position < 0) {
                selectedItem = mPopup.getSelectedItem();
            } else {
                selectedItem = mAdapter.getItem(position);
            }
            if (selectedItem == null) {
                Log.w(TAG, "performCompletion: no selected item");
                return;
            }

            mBlockCompletion = true;
            replaceText(convertSelectionToString(selectedItem));
            mBlockCompletion = false;

            if (mItemClickListener != null) {
                final ListPopupWindow list = mPopup;

                if (selectedView == null || position < 0) {
=======
    private void performCompletion(View selectedView, int position, long id) {
        if (isPopupShowing()) {
            Object selectedItem;
            if (position == -1) {
                selectedItem = mDropDownList.getSelectedItem();
            } else {
                selectedItem = mAdapter.getItem(position);
            }
            replaceText(convertSelectionToString(selectedItem));

            if (mItemClickListener != null) {
                final DropDownListView list = mDropDownList;

                if (selectedView == null || position == -1) {
>>>>>>> 54b6cfa... Initial Contribution
                    selectedView = list.getSelectedView();
                    position = list.getSelectedItemPosition();
                    id = list.getSelectedItemId();
                }
<<<<<<< HEAD
                mItemClickListener.onItemClick(list.getListView(), selectedView, position, id);
            }
        }

        if (mDropDownDismissedOnCompletion && !mPopup.isDropDownAlwaysVisible()) {
            dismissDropDown();
        }
    }
    
    /**
     * Identifies whether the view is currently performing a text completion, so subclasses
     * can decide whether to respond to text changed events.
     */
    public boolean isPerformingCompletion() {
        return mBlockCompletion;
    }

    /**
     * Like {@link #setText(CharSequence)}, except that it can disable filtering.
     *
     * @param filter If <code>false</code>, no filtering will be performed
     *        as a result of this call.
     * 
     * @hide Pending API council approval.
     */
    public void setText(CharSequence text, boolean filter) {
        if (filter) {
            setText(text);
        } else {
            mBlockCompletion = true;
            setText(text);
            mBlockCompletion = false;
        }
=======
                mItemClickListener.onItemClick(list, selectedView, position, id);
            }
        }

        dismissDropDown();
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * <p>Performs the text completion by replacing the current text by the
     * selected item. Subclasses should override this method to avoid replacing
     * the whole content of the edit box.</p>
     *
     * @param text the selected suggestion in the drop down list
     */
    protected void replaceText(CharSequence text) {
<<<<<<< HEAD
        clearComposingText();

=======
>>>>>>> 54b6cfa... Initial Contribution
        setText(text);
        // make sure we keep the caret at the end of the text view
        Editable spannable = getText();
        Selection.setSelection(spannable, spannable.length());
    }

<<<<<<< HEAD
    /** {@inheritDoc} */
    public void onFilterComplete(int count) {
        updateDropDownForFilter(count);
    }

    private void updateDropDownForFilter(int count) {
        // Not attached to window, don't update drop-down
        if (getWindowVisibility() == View.GONE) return;

        /*
         * This checks enoughToFilter() again because filtering requests
=======
    public void onFilterComplete(int count) {
        /*
         * This checks enoughToFilter() again because filtering requests    
>>>>>>> 54b6cfa... Initial Contribution
         * are asynchronous, so the result may come back after enough text
         * has since been deleted to make it no longer appropriate
         * to filter.
         */

<<<<<<< HEAD
        final boolean dropDownAlwaysVisible = mPopup.isDropDownAlwaysVisible();
        final boolean enoughToFilter = enoughToFilter();
        if ((count > 0 || dropDownAlwaysVisible) && enoughToFilter) {
            if (hasFocus() && hasWindowFocus() && mPopupCanBeUpdated) {
                showDropDown();
            }
        } else if (!dropDownAlwaysVisible && isPopupShowing()) {
            dismissDropDown();
            // When the filter text is changed, the first update from the adapter may show an empty
            // count (when the query is being performed on the network). Future updates when some
            // content has been retrieved should still be able to update the list.
            mPopupCanBeUpdated = true;
=======
        if (count > 0 && enoughToFilter()) {
            if (hasFocus() && hasWindowFocus()) {
                showDropDown();
            }
        } else {
            dismissDropDown();
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
<<<<<<< HEAD
        if (!hasWindowFocus && !mPopup.isDropDownAlwaysVisible()) {
=======
        performValidation();
        if (!hasWindowFocus) {
>>>>>>> 54b6cfa... Initial Contribution
            dismissDropDown();
        }
    }

    @Override
<<<<<<< HEAD
    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
        switch (hint) {
            case INVISIBLE:
                if (!mPopup.isDropDownAlwaysVisible()) {
                    dismissDropDown();
                }
                break;
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        // Perform validation if the view is losing focus.
        if (!focused) {
            performValidation();
        }
        if (!focused && !mPopup.isDropDownAlwaysVisible()) {
=======
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        performValidation();
        if (!focused) {
>>>>>>> 54b6cfa... Initial Contribution
            dismissDropDown();
        }
    }

<<<<<<< HEAD
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        dismissDropDown();
        super.onDetachedFromWindow();
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    /**
     * <p>Closes the drop down if present on screen.</p>
     */
    public void dismissDropDown() {
<<<<<<< HEAD
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            imm.displayCompletions(this, null);
        }
        mPopup.dismiss();
        mPopupCanBeUpdated = false;
    }

    @Override
    protected boolean setFrame(final int l, int t, final int r, int b) {
        boolean result = super.setFrame(l, t, r, b);

        if (isPopupShowing()) {
            showDropDown();
        }
=======
        mPopup.dismiss();
        if (mDropDownList != null) {
            // start next time with no selection
            mDropDownList.hideSelector();
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);

        mPopup.update(this, getMeasuredWidth(), -1);
>>>>>>> 54b6cfa... Initial Contribution

        return result;
    }

    /**
<<<<<<< HEAD
     * Issues a runnable to show the dropdown as soon as possible.
     *
     * @hide internal used only by SearchDialog
     */
    public void showDropDownAfterLayout() {
        mPopup.postShow();
    }
    
    /**
     * Ensures that the drop down is not obscuring the IME.
     * @param visible whether the ime should be in front. If false, the ime is pushed to
     * the background.
     * @hide internal used only here and SearchDialog
     */
    public void ensureImeVisible(boolean visible) {
        mPopup.setInputMethodMode(visible
                ? ListPopupWindow.INPUT_METHOD_NEEDED : ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
        if (mPopup.isDropDownAlwaysVisible() || (mFilter != null && enoughToFilter())) {
            showDropDown();
=======
     * <p>Displays the drop down on screen.</p>
     */
    public void showDropDown() {
        int height = buildDropDown();
        if (mPopup.isShowing()) {
            mPopup.update(this, getMeasuredWidth() - mPaddingLeft - mPaddingRight, height);
        } else {
            mPopup.setHeight(height);
            mPopup.setWidth(getMeasuredWidth() - mPaddingLeft - mPaddingRight);
            mPopup.showAsDropDown(this);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * @hide internal used only here and SearchDialog
     */
    public boolean isInputMethodNotNeeded() {
        return mPopup.getInputMethodMode() == ListPopupWindow.INPUT_METHOD_NOT_NEEDED;
    }

    /**
     * <p>Displays the drop down on screen.</p>
     */
    public void showDropDown() {
        buildImeCompletions();

        if (mPopup.getAnchorView() == null) {
            if (mDropDownAnchorId != View.NO_ID) {
                mPopup.setAnchorView(getRootView().findViewById(mDropDownAnchorId));
            } else {
                mPopup.setAnchorView(this);
            }
        }
        if (!isPopupShowing()) {
            // Make sure the list does not obscure the IME when shown for the first time.
            mPopup.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
            mPopup.setListItemExpandMax(EXPAND_MAX);
        }
        mPopup.show();
        mPopup.getListView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
    }

    /**
     * Forces outside touches to be ignored. Normally if {@link #isDropDownAlwaysVisible()} is
     * false, we allow outside touch to dismiss the dropdown. If this is set to true, then we
     * ignore outside touch even when the drop down is not set to always visible.
     * 
     * @hide used only by SearchDialog
     */
    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        mPopup.setForceIgnoreOutsideTouch(forceIgnoreOutsideTouch);
    }

    private void buildImeCompletions() {
        final ListAdapter adapter = mAdapter;
        if (adapter != null) {
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null) {
                final int count = Math.min(adapter.getCount(), 20);
                CompletionInfo[] completions = new CompletionInfo[count];
                int realCount = 0;

                for (int i = 0; i < count; i++) {
                    if (adapter.isEnabled(i)) {
                        Object item = adapter.getItem(i);
                        long id = adapter.getItemId(i);
                        completions[realCount] = new CompletionInfo(id, realCount,
                                convertSelectionToString(item));
                        realCount++;
                    }
                }
                
                if (realCount != count) {
                    CompletionInfo[] tmp = new CompletionInfo[realCount];
                    System.arraycopy(completions, 0, tmp, 0, realCount);
                    completions = tmp;
                }

                imm.displayCompletions(this, completions);
            }
=======
     * <p>Builds the popup window's content and returns the height the popup
     * should have. Returns -1 when the content already exists.</p>
     *
     * @return the content's height or -1 if content already exists
     */
    private int buildDropDown() {
        ViewGroup dropDownView;
        int otherHeights = 0;

        if (mDropDownList == null) {
            Context context = getContext();

            mDropDownList = new DropDownListView(context);
            mDropDownList.setSelector(mDropDownListHighlight);
            mDropDownList.setAdapter(mAdapter);
            mDropDownList.setVerticalFadingEdgeEnabled(true);
            mDropDownList.setOnItemClickListener(mDropDownItemClickListener);

            if (mItemSelectedListener != null) {
                mDropDownList.setOnItemSelectedListener(mItemSelectedListener);
            }

            dropDownView = mDropDownList;

            View hintView = getHintView(context);
            if (hintView != null) {
                // if an hint has been specified, we accomodate more space for it and
                // add a text view in the drop down menu, at the bottom of the list
                LinearLayout hintContainer = new LinearLayout(context);
                hintContainer.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, 0, 1.0f
                );
                hintContainer.addView(dropDownView, hintParams);
                hintContainer.addView(hintView);

                // measure the hint's height to find how much more vertical space
                // we need to add to the drop down's height
                int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.AT_MOST);
                int heightSpec = MeasureSpec.UNSPECIFIED;
                hintView.measure(widthSpec, heightSpec);

                hintParams = (LinearLayout.LayoutParams) hintView.getLayoutParams();
                otherHeights = hintView.getMeasuredHeight() + hintParams.topMargin
                        + hintParams.bottomMargin;

                dropDownView = hintContainer;
            }

            mPopup.setContentView(dropDownView);
        } else {
            dropDownView = (ViewGroup) mPopup.getContentView();
            final View view = dropDownView.findViewById(HINT_VIEW_ID);
            if (view != null) {
                LinearLayout.LayoutParams hintParams =
                        (LinearLayout.LayoutParams) view.getLayoutParams();
                otherHeights = view.getMeasuredHeight() + hintParams.topMargin
                        + hintParams.bottomMargin;
            }
        }

        // Max height available on the screen for a popup anchored to us
        final int maxHeight = mPopup.getMaxAvailableHeight(this);
        otherHeights += dropDownView.getPaddingTop() + dropDownView.getPaddingBottom();

        return mDropDownList.measureHeightOfChildren(MeasureSpec.UNSPECIFIED,
                0, ListView.NO_POSITION, maxHeight - otherHeights, 2) + otherHeights;
    }

    private View getHintView(Context context) {
        if (mHintText != null && mHintText.length() > 0) {
            final TextView hintView = (TextView) LayoutInflater.from(context).inflate(
                    mHintResource, null).findViewById(com.android.internal.R.id.text1);
            hintView.setText(mHintText);
            hintView.setId(HINT_VIEW_ID);
            return hintView;
        } else {
            return null;
        }
    }

    private class DropDownItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            performCompletion(v, position, id);
        }
    }

    /**
     * <p>Wrapper class for a ListView. This wrapper hijacks the focus to
     * make sure the list uses the appropriate drawables and states when
     * displayed on screen within a drop down. The focus is never actually
     * passed to the drop down; the list only looks focused.</p>
     */
    private static class DropDownListView extends ListView {
        /**
         * <p>Creates a new list view wrapper.</p>
         *
         * @param context this view's context
         */
        public DropDownListView(Context context) {
            super(context, null, com.android.internal.R.attr.dropDownListViewStyle);
        }

        /**
         * <p>Avoids jarring scrolling effect by ensuring that list elements
         * made of a text view fit on a single line.</p>
         *
         * @param position the item index in the list to get a view for
         * @return the view for the specified item
         */
        @Override
        protected View obtainView(int position) {
            View view = super.obtainView(position);

            if (view instanceof TextView) {
                ((TextView) view).setHorizontallyScrolling(true);
            }

            return view;
        }

        /**
         * <p>Returns the top padding of the currently selected view.</p>
         *
         * @return the height of the top padding for the selection
         */
        public int getSelectionPaddingTop() {
            return mSelectionTopPadding;
        }

        /**
         * <p>Returns the bottom padding of the currently selected view.</p>
         *
         * @return the height of the bottom padding for the selection
         */
        public int getSelectionPaddingBottom() {
            return mSelectionBottomPadding;
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always
         */
        @Override
        public boolean hasWindowFocus() {
            return true;
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always
         */
        @Override
        public boolean isFocused() {
            return true;
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always
         */
        @Override
        public boolean hasFocus() {
            return true;
>>>>>>> 54b6cfa... Initial Contribution
        }
    }

    /**
<<<<<<< HEAD
     * Sets the validator used to perform text validation.
     *
     * @param validator The validator used to validate the text entered in this widget.
     *
     * @see #getValidator()
     * @see #performValidation()
     */
=======
     * This interface is used to make sure that the text entered in this TextView complies to
     * a certain format.  Since there is no foolproof way to prevent the user from leaving
     * this View with an incorrect value in it, all we can do is try to fix it ourselves
     * when this happens.
     */
    static public interface Validator {
        /**
         * @return true if the text currently in the text editor is valid.
         */
        boolean isValid(CharSequence text);

        /**
         * @param invalidText a string that doesn't pass validation:
         * isValid(invalidText) returns false
         * @return a string based on invalidText such as invoking isValid() on it returns true.
         */
        CharSequence fixText(CharSequence invalidText);
    }

    private Validator mValidator = null;
    
>>>>>>> 54b6cfa... Initial Contribution
    public void setValidator(Validator validator) {
        mValidator = validator;
    }

    /**
     * Returns the Validator set with {@link #setValidator},
     * or <code>null</code> if it was not set.
<<<<<<< HEAD
     *
     * @see #setValidator(android.widget.AutoCompleteTextView.Validator)
     * @see #performValidation()
=======
>>>>>>> 54b6cfa... Initial Contribution
     */
    public Validator getValidator() {
        return mValidator;
    }
<<<<<<< HEAD

    /**
     * If a validator was set on this view and the current string is not valid,
     * ask the validator to fix it.
     *
     * @see #getValidator()
     * @see #setValidator(android.widget.AutoCompleteTextView.Validator)
=======
    
    /**
     * If a validator was set on this view and the current string is not valid,
     * ask the validator to fix it. 
>>>>>>> 54b6cfa... Initial Contribution
     */
    public void performValidation() {
        if (mValidator == null) return;

        CharSequence text = getText();
<<<<<<< HEAD

=======
        
>>>>>>> 54b6cfa... Initial Contribution
        if (!TextUtils.isEmpty(text) && !mValidator.isValid(text)) {
            setText(mValidator.fixText(text));
        }
    }

    /**
     * Returns the Filter obtained from {@link Filterable#getFilter},
     * or <code>null</code> if {@link #setAdapter} was not called with
     * a Filterable.
     */
    protected Filter getFilter() {
        return mFilter;
    }
<<<<<<< HEAD

    private class DropDownItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            performCompletion(v, position, id);
        }
    }

    /**
     * This interface is used to make sure that the text entered in this TextView complies to
     * a certain format.  Since there is no foolproof way to prevent the user from leaving
     * this View with an incorrect value in it, all we can do is try to fix it ourselves
     * when this happens.
     */
    public interface Validator {
        /**
         * Validates the specified text.
         *
         * @return true If the text currently in the text editor is valid.
         *
         * @see #fixText(CharSequence)
         */
        boolean isValid(CharSequence text);

        /**
         * Corrects the specified text to make it valid.
         *
         * @param invalidText A string that doesn't pass validation: isValid(invalidText)
         *        returns false
         *
         * @return A string based on invalidText such as invoking isValid() on it returns true.
         *
         * @see #isValid(CharSequence)
         */
        CharSequence fixText(CharSequence invalidText);
    }
    
    /**
     * Allows us a private hook into the on click event without preventing users from setting
     * their own click listener.
     */
    private class PassThroughClickListener implements OnClickListener {

        private View.OnClickListener mWrapped;

        /** {@inheritDoc} */
        public void onClick(View v) {
            onClickImpl();

            if (mWrapped != null) mWrapped.onClick(v);
        }
    }

    private class PopupDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            if (mAdapter != null) {
                // If the popup is not showing already, showing it will cause
                // the list of data set observers attached to the adapter to
                // change. We can't do it from here, because we are in the middle
                // of iterating through the list of observers.
                post(new Runnable() {
                    public void run() {
                        final ListAdapter adapter = mAdapter;
                        if (adapter != null) {
                            // This will re-layout, thus resetting mDataChanged, so that the
                            // listView click listener stays responsive
                            updateDropDownForFilter(adapter.getCount());
                        }
                    }
                });
            }
        }
    }
=======
>>>>>>> 54b6cfa... Initial Contribution
}
