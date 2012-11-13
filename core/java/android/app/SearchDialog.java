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

package android.app;

<<<<<<< HEAD

=======
>>>>>>> 54b6cfa... Initial Contribution
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
<<<<<<< HEAD
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Search dialog. This is controlled by the 
 * SearchManager and runs in the current foreground process.
=======
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.server.search.SearchableInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.WrapperListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-application-process implementation of Search Bar.  This is still controlled by the 
 * SearchManager, but it runs in the current activity's process to keep things lighter weight.
>>>>>>> 54b6cfa... Initial Contribution
 * 
 * @hide
 */
public class SearchDialog extends Dialog {

    // Debugging support
<<<<<<< HEAD
    private static final boolean DBG = false;
    private static final String LOG_TAG = "SearchDialog";

    private static final String INSTANCE_KEY_COMPONENT = "comp";
    private static final String INSTANCE_KEY_APPDATA = "data";
    private static final String INSTANCE_KEY_USER_QUERY = "uQry";

    // The string used for privateImeOptions to identify to the IME that it should not show
    // a microphone button since one already exists in the search dialog.
    private static final String IME_OPTION_NO_MICROPHONE = "nm";

    private static final int SEARCH_PLATE_LEFT_PADDING_NON_GLOBAL = 7;

    // views & widgets
    private TextView mBadgeLabel;
    private ImageView mAppIcon;
    private AutoCompleteTextView mSearchAutoComplete;
    private View mSearchPlate;
    private SearchView mSearchView;
    private Drawable mWorkingSpinner;
    private View mCloseSearch;

    // interaction with searchable application
    private SearchableInfo mSearchable;
    private ComponentName mLaunchComponent;
    private Bundle mAppSearchData;
    private Context mActivityContext;

    // For voice searching
    private final Intent mVoiceWebSearchIntent;
    private final Intent mVoiceAppSearchIntent;

    // The query entered by the user. This is not changed when selecting a suggestion
    // that modifies the contents of the text field. But if the user then edits
    // the suggestion, the resulting string is saved.
    private String mUserQuery;

    // Last known IME options value for the search edit text.
    private int mSearchAutoCompleteImeOptions;

    private BroadcastReceiver mConfChangeListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CONFIGURATION_CHANGED)) {
                onConfigurationChanged();
            }
        }
    };

    static int resolveDialogTheme(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(com.android.internal.R.attr.searchDialogTheme,
                outValue, true);
        return outValue.resourceId;
    }
=======
    final static String LOG_TAG = "SearchDialog";
    private static final int DBG_LOG_TIMING = 0;
    final static int DBG_JAM_THREADING = 0;

    // interaction with runtime
    IntentFilter mCloseDialogsFilter;
    IntentFilter mPackageFilter;
    private final Handler mHandler = new Handler(); // why isn't Dialog.mHandler shared?
    
    private static final String INSTANCE_KEY_COMPONENT = "comp";
    private static final String INSTANCE_KEY_APPDATA = "data";
    private static final String INSTANCE_KEY_GLOBALSEARCH = "glob";
    private static final String INSTANCE_KEY_DISPLAY_QUERY = "dQry";
    private static final String INSTANCE_KEY_DISPLAY_SEL_START = "sel1";
    private static final String INSTANCE_KEY_DISPLAY_SEL_END = "sel2";
    private static final String INSTANCE_KEY_USER_QUERY = "uQry";
    private static final String INSTANCE_KEY_SUGGESTION_QUERY = "sQry";
    private static final String INSTANCE_KEY_SELECTED_ELEMENT = "slEl";
    private static final int INSTANCE_SELECTED_BUTTON = -2;
    private static final int INSTANCE_SELECTED_QUERY = -1;

    // views & widgets
    private View mSearchBarLayout;
    private TextView mBadgeLabel;
    private LinearLayout mSearchEditLayout;
    private EditText mSearchTextField;
    private Button mGoButton;
    private ListView mSuggestionsList;

    private ViewTreeObserver mViewTreeObserver = null;
    
    // interaction with searchable application
    private ComponentName mLaunchComponent;
    private Bundle mAppSearchData;
    private boolean mGlobalSearchMode;
    private Context mActivityContext;

    // interaction with the search manager service
    private SearchableInfo mSearchable;
    
    // support for suggestions 
    private SuggestionsRunner mSuggestionsRunner;
    private String mUserQuery = null;
    private int mUserQuerySelStart;
    private int mUserQuerySelEnd;
    private boolean mNonUserQuery = false;
    private boolean mLeaveJammedQueryOnRefocus = false;
    private String mPreviousSuggestionQuery = null;
    private Context mProviderContext;
    private Animation mSuggestionsEntry;
    private Animation mSuggestionsExit;
    private boolean mSkipNextAnimate;
    private int mPresetSelection = -1;
    private String mSuggestionAction = null;
    private Uri mSuggestionData = null;
    private String mSuggestionQuery = null;
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * Constructor - fires it up and makes it look like the search UI.
     * 
     * @param context Application Context we can use for system acess
     */
<<<<<<< HEAD
    public SearchDialog(Context context, SearchManager searchManager) {
        super(context, resolveDialogTheme(context));

        // Save voice intent for later queries/launching
        mVoiceWebSearchIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
        mVoiceWebSearchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mVoiceWebSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        mVoiceAppSearchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mVoiceAppSearchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * Create the search dialog and any resources that are used for the
     * entire lifetime of the dialog.
=======
    public SearchDialog(Context context) {
        super(context, com.android.internal.R.style.Theme_Translucent);
    }

    /**
     * We create the search dialog just once, and it stays around (hidden)
     * until activated by the user.
>>>>>>> 54b6cfa... Initial Contribution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window theWindow = getWindow();
<<<<<<< HEAD
        WindowManager.LayoutParams lp = theWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        // taking up the whole window (even when transparent) is less than ideal,
        // but necessary to show the popup window until the window manager supports
        // having windows anchored by their parent but not clipped by them.
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP | Gravity.FILL_HORIZONTAL;
        lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        theWindow.setAttributes(lp);

        // Touching outside of the search dialog will dismiss it
        setCanceledOnTouchOutside(true);        
    }

    /**
     * We recreate the dialog view each time it becomes visible so as to limit
     * the scope of any problems with the contained resources.
     */
    private void createContentView() {
        setContentView(com.android.internal.R.layout.search_bar);

        // get the view elements for local access
        SearchBar searchBar = (SearchBar) findViewById(com.android.internal.R.id.search_bar);
        searchBar.setSearchDialog(this);
        mSearchView = (SearchView) findViewById(com.android.internal.R.id.search_view);
        mSearchView.setIconified(false);
        mSearchView.setOnCloseListener(mOnCloseListener);
        mSearchView.setOnQueryTextListener(mOnQueryChangeListener);
        mSearchView.setOnSuggestionListener(mOnSuggestionSelectionListener);
        mSearchView.onActionViewExpanded();

        mCloseSearch = findViewById(com.android.internal.R.id.closeButton);
        mCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // TODO: Move the badge logic to SearchView or move the badge to search_bar.xml
        mBadgeLabel = (TextView) mSearchView.findViewById(com.android.internal.R.id.search_badge);
        mSearchAutoComplete = (AutoCompleteTextView)
                mSearchView.findViewById(com.android.internal.R.id.search_src_text);
        mAppIcon = (ImageView) findViewById(com.android.internal.R.id.search_app_icon);
        mSearchPlate = mSearchView.findViewById(com.android.internal.R.id.search_plate);
        mWorkingSpinner = getContext().getResources().
                getDrawable(com.android.internal.R.drawable.search_spinner);
        // TODO: Restore the spinner for slow suggestion lookups
        // mSearchAutoComplete.setCompoundDrawablesWithIntrinsicBounds(
        //        null, null, mWorkingSpinner, null);
        setWorking(false);

        // pre-hide all the extraneous elements
        mBadgeLabel.setVisibility(View.GONE);

        // Additional adjustments to make Dialog work for Search
        mSearchAutoCompleteImeOptions = mSearchAutoComplete.getImeOptions();
=======
        theWindow.requestFeature(Window.FEATURE_NO_TITLE);
        theWindow.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        theWindow.setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL);

        setContentView(com.android.internal.R.layout.search_bar);

        // Note:  theWindow.setBackgroundDrawable(null) does not work here - you get blackness
        theWindow.setBackgroundDrawableResource(android.R.color.transparent);

        theWindow.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = theWindow.getAttributes();
        lp.dimAmount = 0.5f;
        lp.setTitle("Search Dialog");
        theWindow.setAttributes(lp);

        // get the view elements for local access
        mSearchBarLayout = findViewById(com.android.internal.R.id.search_bar);
        mBadgeLabel = (TextView) findViewById(com.android.internal.R.id.search_badge);
        mSearchEditLayout = (LinearLayout)findViewById(com.android.internal.R.id.search_edit_frame);
        mSearchTextField = (EditText) findViewById(com.android.internal.R.id.search_src_text);
        mGoButton = (Button) findViewById(com.android.internal.R.id.search_go_btn);
        mSuggestionsList = (ListView) findViewById(com.android.internal.R.id.search_suggest_list);
        
        // attach listeners
        mSearchTextField.addTextChangedListener(mTextWatcher);
        mSearchTextField.setOnKeyListener(mTextKeyListener);
        mGoButton.setOnClickListener(mGoButtonClickListener);
        mGoButton.setOnKeyListener(mGoButtonKeyListener);
        mSuggestionsList.setOnItemClickListener(mSuggestionsListItemClickListener);
        mSuggestionsList.setOnKeyListener(mSuggestionsKeyListener);
        mSuggestionsList.setOnFocusChangeListener(mSuggestFocusListener);
        mSuggestionsList.setOnItemSelectedListener(mSuggestSelectedListener);

        // pre-hide all the extraneous elements
        mBadgeLabel.setVisibility(View.GONE);
        mSuggestionsList.setVisibility(View.GONE);

        // Additional adjustments to make Dialog work for Search

        // Touching outside of the search dialog will dismiss it 
        setCanceledOnTouchOutside(true);
        
        // Preload animations
        mSuggestionsEntry = AnimationUtils.loadAnimation(getContext(), 
                com.android.internal.R.anim.grow_fade_in);
        mSuggestionsExit = AnimationUtils.loadAnimation(getContext(), 
                com.android.internal.R.anim.fade_out);

        // Set up broadcast filters
        mCloseDialogsFilter = new
        IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mPackageFilter = new IntentFilter();
        mPackageFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        mPackageFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        mPackageFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        mPackageFilter.addDataScheme("package");
>>>>>>> 54b6cfa... Initial Contribution
    }

    /**
     * Set up the search dialog
     * 
<<<<<<< HEAD
     * @return true if search dialog launched, false if not
     */
    public boolean show(String initialQuery, boolean selectInitialQuery,
            ComponentName componentName, Bundle appSearchData) {
        boolean success = doShow(initialQuery, selectInitialQuery, componentName, appSearchData);
        if (success) {
            // Display the drop down as soon as possible instead of waiting for the rest of the
            // pending UI stuff to get done, so that things appear faster to the user.
            mSearchAutoComplete.showDropDownAfterLayout();
        }
        return success;
    }

    /**
     * Does the rest of the work required to show the search dialog. Called by
     * {@link #show(String, boolean, ComponentName, Bundle)} and
     *
     * @return true if search dialog showed, false if not
     */
    private boolean doShow(String initialQuery, boolean selectInitialQuery,
            ComponentName componentName, Bundle appSearchData) {
        // set up the searchable and show the dialog
        if (!show(componentName, appSearchData)) {
            return false;
        }

        // finally, load the user's initial text (which may trigger suggestions)
        setUserQuery(initialQuery);
        if (selectInitialQuery) {
            mSearchAutoComplete.selectAll();
        }

        return true;
    }

    /**
     * Sets up the search dialog and shows it.
     * 
     * @return <code>true</code> if search dialog launched
     */
    private boolean show(ComponentName componentName, Bundle appSearchData) {
        
        if (DBG) { 
            Log.d(LOG_TAG, "show(" + componentName + ", " 
                    + appSearchData + ")");
        }
        
        SearchManager searchManager = (SearchManager)
                mContext.getSystemService(Context.SEARCH_SERVICE);
        // Try to get the searchable info for the provided component.
        mSearchable = searchManager.getSearchableInfo(componentName);

        if (mSearchable == null) {
            return false;
        }

        mLaunchComponent = componentName;
        mAppSearchData = appSearchData;
        mActivityContext = mSearchable.getActivityContext(getContext());

        // show the dialog. this will call onStart().
        if (!isShowing()) {
            // Recreate the search bar view every time the dialog is shown, to get rid
            // of any bad state in the AutoCompleteTextView etc
            createContentView();
            mSearchView.setSearchableInfo(mSearchable);
            mSearchView.setAppSearchData(mAppSearchData);

            show();
        }
        updateUI();

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Register a listener for configuration change events.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        getContext().registerReceiver(mConfChangeListener, filter);
    }

    /**
     * The search dialog is being dismissed, so handle all of the local shutdown operations.
     * 
     * This function is designed to be idempotent so that dismiss() can be safely called at any time
     * (even if already closed) and more likely to really dump any memory.  No leaks!
     */
    @Override
    public void onStop() {
        super.onStop();

        getContext().unregisterReceiver(mConfChangeListener);

        // dump extra memory we're hanging on to
        mLaunchComponent = null;
        mAppSearchData = null;
        mSearchable = null;
        mUserQuery = null;
    }

    /**
     * Sets the search dialog to the 'working' state, which shows a working spinner in the
     * right hand size of the text field.
     * 
     * @param working true to show spinner, false to hide spinner
     */
    public void setWorking(boolean working) {
        mWorkingSpinner.setAlpha(working ? 255 : 0);
        mWorkingSpinner.setVisible(working, false);
        mWorkingSpinner.invalidateSelf();
    }

    /**
     * Save the minimal set of data necessary to recreate the search
     * 
     * @return A bundle with the state of the dialog, or {@code null} if the search
     *         dialog is not showing.
     */
    @Override
    public Bundle onSaveInstanceState() {
        if (!isShowing()) return null;

        Bundle bundle = new Bundle();

        // setup info so I can recreate this particular search       
        bundle.putParcelable(INSTANCE_KEY_COMPONENT, mLaunchComponent);
        bundle.putBundle(INSTANCE_KEY_APPDATA, mAppSearchData);
        bundle.putString(INSTANCE_KEY_USER_QUERY, mUserQuery);

=======
     * @param Returns true if search dialog launched, false if not
     */
    public boolean show(String initialQuery, boolean selectInitialQuery,
            ComponentName componentName, Bundle appSearchData, boolean globalSearch) {
        if (isShowing()) {
            // race condition - already showing but not handling events yet.
            // in this case, just discard the "show" request
            return true;
        }

        // Get searchable info from search manager and use to set up other elements of UI
        // Do this first so we can get out quickly if there's nothing to search
        ISearchManager sms;
        sms = ISearchManager.Stub.asInterface(ServiceManager.getService(Context.SEARCH_SERVICE));
        try {
            mSearchable = sms.getSearchableInfo(componentName, globalSearch);
        } catch (RemoteException e) {
            mSearchable = null;
        }
        if (mSearchable == null) {
            // unfortunately, we can't log here.  it would be logspam every time the user
            // clicks the "search" key on a non-search app
            return false;
        }
        
        // OK, we're going to show ourselves
        if (mSuggestionsList != null) {
            mSuggestionsList.setVisibility(View.GONE);      // prevent any flicker if was visible
        }
        super.show();

        setupSearchableInfo();
        
        // start the suggestions thread (which will mainly idle)
        mSuggestionsRunner = new SuggestionsRunner();
        new Thread(mSuggestionsRunner, "SearchSuggestions").start();

        mLaunchComponent = componentName;
        mAppSearchData = appSearchData;
        mGlobalSearchMode = globalSearch;

        // receive broadcasts
        getContext().registerReceiver(mBroadcastReceiver, mCloseDialogsFilter);
        getContext().registerReceiver(mBroadcastReceiver, mPackageFilter);
        
        mViewTreeObserver = mSearchBarLayout.getViewTreeObserver();
        mViewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);

        // finally, load the user's initial text (which may trigger suggestions)
        mNonUserQuery = false;
        if (initialQuery == null) {
            initialQuery = "";     // This forces the preload to happen, triggering suggestions
        }
        mSearchTextField.setText(initialQuery);
        if (selectInitialQuery) {
            mSearchTextField.selectAll();
        } else {
            mSearchTextField.setSelection(initialQuery.length());
        }
        return true;
    }

    /**
     * The default show() for this Dialog is not supported.
     */
    @Override
    public void show() {
        return;
    }

    /**
     * Dismiss the search dialog.
     * 
     * This function is designed to be idempotent so it can be safely called at any time
     * (even if already closed) and more likely to really dump any memory.  No leaks!
     */
    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
        setOnCancelListener(null);
        setOnDismissListener(null);
        
        // stop receiving broadcasts (throws exception if none registered)
        try {
            getContext().unregisterReceiver(mBroadcastReceiver);
        } catch (RuntimeException e) {
            // This is OK - it just means we didn't have any registered
        }
        
        // ignore layout notifications
        try {
            if (mViewTreeObserver != null) {
                mViewTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
            }
        } catch (RuntimeException e) {
            // This is OK - none registered or observer "dead"
        } 
        mViewTreeObserver = null;

        // dump extra memory we're hanging on to
        if (mSuggestionsRunner != null) {
            mSuggestionsRunner.cancelSuggestions();
            mSuggestionsRunner = null;
        }
        mLaunchComponent = null;
        mAppSearchData = null;
        mSearchable = null;
        mSuggestionAction = null;
        mSuggestionData = null;
        mSuggestionQuery = null;
        mActivityContext = null;
        mProviderContext = null;
        mPreviousSuggestionQuery = null;
        mUserQuery = null;
    }
    
    /**
     * Save the minimal set of data necessary to recreate the search
     * 
     * @return A bundle with the state of the dialog.
     */
    @Override
    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        
        // setup info so I can recreate this particular search       
        bundle.putParcelable(INSTANCE_KEY_COMPONENT, mLaunchComponent);
        bundle.putBundle(INSTANCE_KEY_APPDATA, mAppSearchData);
        bundle.putBoolean(INSTANCE_KEY_GLOBALSEARCH, mGlobalSearchMode);
        
        // UI state
        bundle.putString(INSTANCE_KEY_DISPLAY_QUERY, mSearchTextField.getText().toString());
        bundle.putInt(INSTANCE_KEY_DISPLAY_SEL_START, mSearchTextField.getSelectionStart());
        bundle.putInt(INSTANCE_KEY_DISPLAY_SEL_END, mSearchTextField.getSelectionEnd());
        bundle.putString(INSTANCE_KEY_USER_QUERY, mUserQuery);
        bundle.putString(INSTANCE_KEY_SUGGESTION_QUERY, mPreviousSuggestionQuery);
        
        int selectedElement = INSTANCE_SELECTED_QUERY;
        if (mGoButton.isFocused()) {
            selectedElement = INSTANCE_SELECTED_BUTTON;
        } else if ((mSuggestionsList.getVisibility() == View.VISIBLE) && 
                mSuggestionsList.isFocused()) {
            selectedElement = mSuggestionsList.getSelectedItemPosition();   // 0..n
        }
        bundle.putInt(INSTANCE_KEY_SELECTED_ELEMENT, selectedElement);
        
>>>>>>> 54b6cfa... Initial Contribution
        return bundle;
    }

    /**
     * Restore the state of the dialog from a previously saved bundle.
<<<<<<< HEAD
     * 
=======
     *
>>>>>>> 54b6cfa... Initial Contribution
     * @param savedInstanceState The state of the dialog previously saved by
     *     {@link #onSaveInstanceState()}.
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
<<<<<<< HEAD
        if (savedInstanceState == null) return;

        ComponentName launchComponent = savedInstanceState.getParcelable(INSTANCE_KEY_COMPONENT);
        Bundle appSearchData = savedInstanceState.getBundle(INSTANCE_KEY_APPDATA);
        String userQuery = savedInstanceState.getString(INSTANCE_KEY_USER_QUERY);

        // show the dialog.
        if (!doShow(userQuery, false, launchComponent, appSearchData)) {
            // for some reason, we couldn't re-instantiate
            return;
        }
    }

    /**
     * Called after resources have changed, e.g. after screen rotation or locale change.
     */
    public void onConfigurationChanged() {
        if (mSearchable != null && isShowing()) {
            // Redraw (resources may have changed)
            updateSearchAppIcon();
            updateSearchBadge();
            if (isLandscapeMode(getContext())) {
                mSearchAutoComplete.ensureImeVisible(true);
            }
        }
    }

    static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Update the UI according to the info in the current value of {@link #mSearchable}.
     */
    private void updateUI() {
        if (mSearchable != null) {
            mDecor.setVisibility(View.VISIBLE);
            updateSearchAutoComplete();
            updateSearchAppIcon();
            updateSearchBadge();
            
            // In order to properly configure the input method (if one is being used), we
            // need to let it know if we'll be providing suggestions.  Although it would be
            // difficult/expensive to know if every last detail has been configured properly, we 
            // can at least see if a suggestions provider has been configured, and use that
            // as our trigger.
            int inputType = mSearchable.getInputType();
            // We only touch this if the input type is set up for text (which it almost certainly
            // should be, in the case of search!)
            if ((inputType & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT) {
                // The existence of a suggestions authority is the proxy for "suggestions 
                // are available here"
                inputType &= ~InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
                if (mSearchable.getSuggestAuthority() != null) {
                    inputType |= InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
                }
            }
            mSearchAutoComplete.setInputType(inputType);
            mSearchAutoCompleteImeOptions = mSearchable.getImeOptions();
            mSearchAutoComplete.setImeOptions(mSearchAutoCompleteImeOptions);
            
            // If the search dialog is going to show a voice search button, then don't let
            // the soft keyboard display a microphone button if it would have otherwise.
            if (mSearchable.getVoiceSearchEnabled()) {
                mSearchAutoComplete.setPrivateImeOptions(IME_OPTION_NO_MICROPHONE);
            } else {
                mSearchAutoComplete.setPrivateImeOptions(null);
            }
        }
    }
    
    /**
     * Updates the auto-complete text view.
     */
    private void updateSearchAutoComplete() {
        // we dismiss the entire dialog instead
        mSearchAutoComplete.setDropDownDismissedOnCompletion(false);
        mSearchAutoComplete.setForceIgnoreOutsideTouch(false);
    }
    
    private void updateSearchAppIcon() {
        PackageManager pm = getContext().getPackageManager();
        Drawable icon;
        try {
            ActivityInfo info = pm.getActivityInfo(mLaunchComponent, 0);
            icon = pm.getApplicationIcon(info.applicationInfo);
            if (DBG)
                Log.d(LOG_TAG, "Using app-specific icon");
        } catch (NameNotFoundException e) {
            icon = pm.getDefaultActivityIcon();
            Log.w(LOG_TAG, mLaunchComponent + " not found, using generic app icon");
        }
        mAppIcon.setImageDrawable(icon);
        mAppIcon.setVisibility(View.VISIBLE);
        mSearchPlate.setPadding(SEARCH_PLATE_LEFT_PADDING_NON_GLOBAL, mSearchPlate.getPaddingTop(), mSearchPlate.getPaddingRight(), mSearchPlate.getPaddingBottom());
    }

    /**
     * Setup the search "Badge" if requested by mode flags.
=======
        // Get the launch info
        ComponentName launchComponent = savedInstanceState.getParcelable(INSTANCE_KEY_COMPONENT);
        Bundle appSearchData = savedInstanceState.getBundle(INSTANCE_KEY_APPDATA);
        boolean globalSearch = savedInstanceState.getBoolean(INSTANCE_KEY_GLOBALSEARCH);
        
        // get the UI state
        String displayQuery = savedInstanceState.getString(INSTANCE_KEY_DISPLAY_QUERY);
        int querySelStart = savedInstanceState.getInt(INSTANCE_KEY_DISPLAY_SEL_START, -1);
        int querySelEnd = savedInstanceState.getInt(INSTANCE_KEY_DISPLAY_SEL_END, -1);
        String userQuery = savedInstanceState.getString(INSTANCE_KEY_USER_QUERY);
        int selectedElement = savedInstanceState.getInt(INSTANCE_KEY_SELECTED_ELEMENT);
        String suggestionQuery = savedInstanceState.getString(INSTANCE_KEY_SUGGESTION_QUERY);
        
        // show the dialog.  skip any show/hide animation, we want to go fast.
        // send the text that actually generates the suggestions here;  we'll replace the display
        // text as necessary in a moment.
        if (!show(suggestionQuery, false, launchComponent, appSearchData, globalSearch)) {
            // for some reason, we couldn't re-instantiate
            return;
        }
        mSkipNextAnimate = true;
        
        mNonUserQuery = true;
        mSearchTextField.setText(displayQuery);
        mNonUserQuery = false;
        
        // clean up the selection state
        switch (selectedElement) {
        case INSTANCE_SELECTED_BUTTON:
            mGoButton.setEnabled(true);
            mGoButton.setFocusable(true);
            mGoButton.requestFocus();
            break;
        case INSTANCE_SELECTED_QUERY:
            if (querySelStart >= 0 && querySelEnd >= 0) {
                mSearchTextField.requestFocus();
                mSearchTextField.setSelection(querySelStart, querySelEnd);
            }
            break;
        default:
            // defer selecting a list element until suggestion list appears
            mPresetSelection = selectedElement;
            break;
        }
    }
    
    /**
     * Hook for updating layout on a rotation
     * 
     */
    public void onConfigurationChanged(Configuration newConfig) {
        if (isShowing()) {
            // Redraw (resources may have changed)
            updateSearchButton();
            updateSearchBadge();
            updateQueryHint();
        } 
    }

    /**
     * Use SearchableInfo record (from search manager service) to preconfigure the UI in various
     * ways.
     */
    private void setupSearchableInfo() {
        if (mSearchable != null) {
            mActivityContext = mSearchable.getActivityContext(getContext());
            mProviderContext = mSearchable.getProviderContext(getContext(), mActivityContext);
            
            updateSearchButton();
            updateSearchBadge();
            updateQueryHint();
        }
    }

    /**
     * The list of installed packages has just changed.  This means that our current context
     * may no longer be valid.  This would only happen if a package is installed/removed exactly
     * when the search bar is open.  So for now we're just going to close the search
     * bar.  
     * 
     * Anything fancier would require some checks to see if the user's context was still valid.
     * Which would be messier.
     */
    public void onPackageListChange() {
        cancel();
    }
    
    /**
     * Update the text in the search button
     */
    private void updateSearchButton() {
        int textId = mSearchable.getSearchButtonText();
        if (textId == 0) {
            textId = com.android.internal.R.string.search_go;
        }
        String goText = mActivityContext.getResources().getString(textId);
        mGoButton.setText(goText);
    }
    
    /**
     * Setup the search "Badge" if request by mode flags.
>>>>>>> 54b6cfa... Initial Contribution
     */
    private void updateSearchBadge() {
        // assume both hidden
        int visibility = View.GONE;
        Drawable icon = null;
<<<<<<< HEAD
        CharSequence text = null;

        // optionally show one or the other.
        if (mSearchable.useBadgeIcon()) {
            icon = mActivityContext.getResources().getDrawable(mSearchable.getIconId());
            visibility = View.VISIBLE;
            if (DBG) Log.d(LOG_TAG, "Using badge icon: " + mSearchable.getIconId());
        } else if (mSearchable.useBadgeLabel()) {
            text = mActivityContext.getResources().getText(mSearchable.getLabelId()).toString();
            visibility = View.VISIBLE;
            if (DBG) Log.d(LOG_TAG, "Using badge label: " + mSearchable.getLabelId());
        }

=======
        String text = null;
        
        // optionally show one or the other.
        if (mSearchable.mBadgeIcon) {
            icon = mActivityContext.getResources().getDrawable(mSearchable.getIconId());
            visibility = View.VISIBLE;
        } else if (mSearchable.mBadgeLabel) {
            text = mActivityContext.getResources().getText(mSearchable.getLabelId()).toString();
            visibility = View.VISIBLE;
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        mBadgeLabel.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        mBadgeLabel.setText(text);
        mBadgeLabel.setVisibility(visibility);
    }

<<<<<<< HEAD
    /*
=======
    /**
     * Update the hint in the query text field.
     */
    private void updateQueryHint() {
        if (isShowing()) {
            String hint = null;
            if (mSearchable != null) {
                int hintId = mSearchable.getHintId();
                if (hintId != 0) {
                    hint = mActivityContext.getString(hintId);
                }
            }
            mSearchTextField.setHint(hint);
        }
    }

    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Listeners of various types
     */

    /**
<<<<<<< HEAD
     * {@link Dialog#onTouchEvent(MotionEvent)} will cancel the dialog only when the
     * touch is outside the window. But the window includes space for the drop-down,
     * so we also cancel on taps outside the search bar when the drop-down is not showing.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // cancel if the drop-down is not showing and the touch event was outside the search plate
        if (!mSearchAutoComplete.isPopupShowing() && isOutOfBounds(mSearchPlate, event)) {
            if (DBG) Log.d(LOG_TAG, "Pop-up not showing and outside of search plate.");
            cancel();
            return true;
        }
        // Let Dialog handle events outside the window while the pop-up is showing.
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(View v, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(mContext).getScaledWindowTouchSlop();
        return (x < -slop) || (y < -slop)
                || (x > (v.getWidth()+slop))
                || (y > (v.getHeight()+slop));
    }

    @Override
    public void hide() {
        if (!isShowing()) return;

        // We made sure the IME was displayed, so also make sure it is closed
        // when we go away.
        InputMethodManager imm = (InputMethodManager)getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(
                    getWindow().getDecorView().getWindowToken(), 0);
        }

        super.hide();
    }
    
    /**
     * Launch a search for the text in the query text field.
     */
    public void launchQuerySearch() {
        launchQuerySearch(KeyEvent.KEYCODE_UNKNOWN, null);
    }

    /**
     * Launch a search for the text in the query text field.
     *
     * @param actionKey The key code of the action key that was pressed,
     *        or {@link KeyEvent#KEYCODE_UNKNOWN} if none.
     * @param actionMsg The message for the action key that was pressed,
     *        or <code>null</code> if none.
     */
    protected void launchQuerySearch(int actionKey, String actionMsg) {
        String query = mSearchAutoComplete.getText().toString();
        String action = Intent.ACTION_SEARCH;
        Intent intent = createIntent(action, null, null, query, actionKey, actionMsg);
        launchIntent(intent);
    }

    /**
     * Launches an intent, including any special intent handling.
     */
    private void launchIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        Log.d(LOG_TAG, "launching " + intent);
        try {
            // If the intent was created from a suggestion, it will always have an explicit
            // component here.
            getContext().startActivity(intent);
            // If the search switches to a different activity,
            // SearchDialogWrapper#performActivityResuming
            // will handle hiding the dialog when the next activity starts, but for
            // real in-app search, we still need to dismiss the dialog.
            dismiss();
        } catch (RuntimeException ex) {
            Log.e(LOG_TAG, "Failed launch activity: " + intent, ex);
        }
    }

    /**
     * Sets the list item selection in the AutoCompleteTextView's ListView.
     */
    public void setListSelection(int index) {
        mSearchAutoComplete.setListSelection(index);
    }

    /**
     * Constructs an intent from the given information and the search dialog state.
     * 
     * @param action Intent action.
     * @param data Intent data, or <code>null</code>.
     * @param extraData Data for {@link SearchManager#EXTRA_DATA_KEY} or <code>null</code>.
     * @param query Intent query, or <code>null</code>.
     * @param actionKey The key code of the action key that was pressed,
     *        or {@link KeyEvent#KEYCODE_UNKNOWN} if none.
     * @param actionMsg The message for the action key that was pressed,
     *        or <code>null</code> if none.
     * @param mode The search mode, one of the acceptable values for
     *             {@link SearchManager#SEARCH_MODE}, or {@code null}.
     * @return The intent.
     */
    private Intent createIntent(String action, Uri data, String extraData, String query,
            int actionKey, String actionMsg) {
        // Now build the Intent
        Intent intent = new Intent(action);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // We need CLEAR_TOP to avoid reusing an old task that has other activities
        // on top of the one we want. We don't want to do this in in-app search though,
        // as it can be destructive to the activity stack.
        if (data != null) {
            intent.setData(data);
        }
        intent.putExtra(SearchManager.USER_QUERY, mUserQuery);
        if (query != null) {
            intent.putExtra(SearchManager.QUERY, query);
        }
        if (extraData != null) {
            intent.putExtra(SearchManager.EXTRA_DATA_KEY, extraData);
        }
        if (mAppSearchData != null) {
            intent.putExtra(SearchManager.APP_DATA, mAppSearchData);
        }
        if (actionKey != KeyEvent.KEYCODE_UNKNOWN) {
            intent.putExtra(SearchManager.ACTION_KEY, actionKey);
            intent.putExtra(SearchManager.ACTION_MSG, actionMsg);
        }
        intent.setComponent(mSearchable.getSearchActivity());
        return intent;
    }

    /**
     * The root element in the search bar layout. This is a custom view just to override
     * the handling of the back button.
     */
    public static class SearchBar extends LinearLayout {

        private SearchDialog mSearchDialog;

        public SearchBar(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SearchBar(Context context) {
            super(context);
        }

        public void setSearchDialog(SearchDialog searchDialog) {
            mSearchDialog = searchDialog;
        }

        /**
         * Don't allow action modes in a SearchBar, it looks silly.
         */
        @Override
        public ActionMode startActionModeForChild(View child, ActionMode.Callback callback) {
            return null;
        }
    }

    private boolean isEmpty(AutoCompleteTextView actv) {
        return TextUtils.getTrimmedLength(actv.getText()) == 0;
    }

    @Override
    public void onBackPressed() {
        // If the input method is covering the search dialog completely,
        // e.g. in landscape mode with no hard keyboard, dismiss just the input method
        InputMethodManager imm = (InputMethodManager)getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isFullscreenMode() &&
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0)) {
            return;
        }
        // Close search dialog
        cancel();
    }

    private boolean onClosePressed() {
        // Dismiss the dialog if close button is pressed when there's no query text
        if (isEmpty(mSearchAutoComplete)) {
            dismiss();
            return true;
        }

        return false;
    }

    private final SearchView.OnCloseListener mOnCloseListener = new SearchView.OnCloseListener() {

        public boolean onClose() {
            return onClosePressed();
        }
    };

    private final SearchView.OnQueryTextListener mOnQueryChangeListener =
            new SearchView.OnQueryTextListener() {

        public boolean onQueryTextSubmit(String query) {
            dismiss();
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private final SearchView.OnSuggestionListener mOnSuggestionSelectionListener =
            new SearchView.OnSuggestionListener() {

        public boolean onSuggestionSelect(int position) {
            return false;
        }

        public boolean onSuggestionClick(int position) {
            dismiss();
            return false;
        }
    };

    /**
     * Sets the text in the query box, updating the suggestions.
     */
    private void setUserQuery(String query) {
        if (query == null) {
            query = "";
        }
        mUserQuery = query;
        mSearchAutoComplete.setText(query);
        mSearchAutoComplete.setSelection(query.length());
=======
     * Dialog's OnKeyListener implements various search-specific functionality
     *
     * @param keyCode This is the keycode of the typed key, and is the same value as
     * found in the KeyEvent parameter.
     * @param event The complete event record for the typed key
     *
     * @return Return true if the event was handled here, or false if not.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            cancel();
            return true;
        case KeyEvent.KEYCODE_SEARCH:
            if (TextUtils.getTrimmedLength(mSearchTextField.getText()) != 0) {
                launchQuerySearch(KeyEvent.KEYCODE_UNKNOWN, null);
            } else {
                cancel();
            }
            return true;
        default:
            SearchableInfo.ActionKeyInfo actionKey = mSearchable.findActionKey(keyCode);
            if ((actionKey != null) && (actionKey.mQueryActionMsg != null)) {
                launchQuerySearch(keyCode, actionKey.mQueryActionMsg);
                return true;
            }
            break;
        }
        return false;
    }

    /**
     * Callback to watch the textedit field for empty/non-empty
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int
                before, int after) { }

        public void onTextChanged(CharSequence s, int start,
                int before, int after) {
            if (DBG_LOG_TIMING == 1) {
                dbgLogTiming("onTextChanged()");
            }
            updateWidgetState();
            // Only do suggestions if actually typed by user
            if (!mNonUserQuery) {
                updateSuggestions();
                mUserQuery = mSearchTextField.getText().toString();
                mUserQuerySelStart = mSearchTextField.getSelectionStart();
                mUserQuerySelEnd = mSearchTextField.getSelectionEnd();
            }
        }

        public void afterTextChanged(Editable s) { }
    };

    /**
     * Enable/Disable the cancel button based on edit text state (any text?)
     */
    private void updateWidgetState() {
        // enable the button if we have one or more non-space characters
        boolean enabled =
            TextUtils.getTrimmedLength(mSearchTextField.getText()) != 0;

        mGoButton.setEnabled(enabled);
        mGoButton.setFocusable(enabled);
    }

    /**
     * In response to a change in the query text, update the suggestions
     */
    private void updateSuggestions() {
        final String queryText = mSearchTextField.getText().toString();
        mPreviousSuggestionQuery = queryText;
        if (DBG_LOG_TIMING == 1) {
            dbgLogTiming("updateSuggestions()");
        }
        
        mSuggestionsRunner.requestSuggestions(mSearchable, queryText);
        
        // For debugging purposes, put in a lot of strings (really fast typist)
        if (DBG_JAM_THREADING > 0) {
            for (int ii = 1; ii < DBG_JAM_THREADING; ++ii) {
                final String jamQuery = queryText + ii;
                mSuggestionsRunner.requestSuggestions(mSearchable, jamQuery);
            }
            // one final (correct) string for cleanup
            mSuggestionsRunner.requestSuggestions(mSearchable, queryText);
        }
    }

    /**
     * This class defines a queued message structure for processing user keystrokes, and a
     * thread that allows the suggestions to be gathered out-of-band, and allows us to skip
     * over multiple keystrokes if the typist is faster than the content provider.
     */
    private class SuggestionsRunner implements Runnable {

        private class Request {
            final SearchableInfo mSearchableInfo;     // query will set these
            final String mQueryText;
            final boolean cancelRequest;              // cancellation will set this
            
            // simple constructors
            Request(final SearchableInfo searchable, final String queryText) {
                mSearchableInfo = searchable;
                mQueryText = queryText;
                cancelRequest = false;
            }
            
            Request() {
                mSearchableInfo = null;
                mQueryText = null;
                cancelRequest = true;
            }
        }
        
        private final LinkedBlockingQueue<Request> mSuggestionsQueue = 
                                                            new LinkedBlockingQueue<Request>();

        /**
         * Queue up a suggestions request (non-blocking - can safely call from UI thread)
         */
        public void requestSuggestions(final SearchableInfo searchable, final String queryText) {
            Request request = new Request(searchable, queryText);
            try {
                mSuggestionsQueue.put(request);
            } catch (InterruptedException e) {
                // discard the request.
            }
        }
        
        /**
         * Cancel blocking suggestions, discard any results, and shut down the thread.
         * (non-blocking - can safely call from UI thread)
         */
        private void cancelSuggestions() {
            Request request = new Request();
            try {
                mSuggestionsQueue.put(request);
            } catch (InterruptedException e) {
                // discard the request.
                // TODO can we do better here?
            }
        }
        
        /**
         * This runnable implements the logic for decoupling keystrokes from suggestions.  
         * The logic isn't quite obvious here, so I'll try to describe it.
         * 
         * Normally we simply sleep waiting for a keystroke.  When a keystroke arrives,
         * we immediately dispatch a request to gather suggestions.  
         * 
         * But this can take a while, so by the time it comes back, more keystrokes may have 
         * arrived.  If anything happened while we were gathering the suggestion, we discard its 
         * results, and then use the most recent keystroke to start the next suggestions request.
         * 
         * Any request containing cancelRequest == true will cause the thread to immediately
         * terminate.
         */
        public void run() {            
            // outer blocking loop simply waits for a suggestion
            while (true) {
                try {
                    Request request = mSuggestionsQueue.take();
                    if (request.cancelRequest) {
                        return;
                    }
                    
                    // since we were idle, what we're really interested is the final element
                    // in the queue.  So keep pulling until we get the last element.
                    // TODO Could we just do some sort of takeHead() here?
                    while (! mSuggestionsQueue.isEmpty()) {
                        request = mSuggestionsQueue.take();
                        if (request.cancelRequest) {
                            return;
                        }
                    }
                    final Request useRequest = request;
                    
                    // now process the final element (unless it's a cancel - that can be discarded)
                    
                    if (useRequest.mSearchableInfo != null) {
                        
                        // go get the cursor.  this is what takes time.
                        final Cursor c = getSuggestions(useRequest.mSearchableInfo, 
                                useRequest.mQueryText);
                        
                        // We now have a suggestions result.  But, if any new requests have arrived,
                        // we're going to discard them - we don't want to waste time displaying 
                        // out-of-date results, we just want to get going on the next set.
                        // Note, null cursor is a valid result (no suggestions).  This logic also
                        // supports the need to discard the results *and* stop the thread if a kill 
                        // request arrives during a query.
                        if (mSuggestionsQueue.size() > 0) {
                            if (c != null) {
                                c.close();
                            }
                        } else {
                            mHandler.post(new Runnable() {
                                public void run() {
                                    updateSuggestionsWithCursor(c, useRequest.mSearchableInfo);
                                } 
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    // loop back for more
                }
                // At this point the queue may contain zero-to-many new requests;  We simply 
                // loop back to handle them (or, block until new requests arrive)
            }
        }
    }
        
    /**
     * Back in the UI thread, handle incoming cursors
     */
    private final static String[] ONE_LINE_FROM =       {SearchManager.SUGGEST_COLUMN_TEXT_1 };
    private final static String[] ONE_LINE_ICONS_FROM = {SearchManager.SUGGEST_COLUMN_TEXT_1,
                                                         SearchManager.SUGGEST_COLUMN_ICON_1,
                                                         SearchManager.SUGGEST_COLUMN_ICON_2};
    private final static String[] TWO_LINE_FROM =       {SearchManager.SUGGEST_COLUMN_TEXT_1,
                                                         SearchManager.SUGGEST_COLUMN_TEXT_2 };
    private final static String[] TWO_LINE_ICONS_FROM = {SearchManager.SUGGEST_COLUMN_TEXT_1,
                                                         SearchManager.SUGGEST_COLUMN_TEXT_2,
                                                         SearchManager.SUGGEST_COLUMN_ICON_1,
                                                         SearchManager.SUGGEST_COLUMN_ICON_2 };
    
    private final static int[] ONE_LINE_TO =       {com.android.internal.R.id.text1};
    private final static int[] ONE_LINE_ICONS_TO = {com.android.internal.R.id.text1,
                                                    com.android.internal.R.id.icon1, 
                                                    com.android.internal.R.id.icon2};
    private final static int[] TWO_LINE_TO =       {com.android.internal.R.id.text1, 
                                                    com.android.internal.R.id.text2};
    private final static int[] TWO_LINE_ICONS_TO = {com.android.internal.R.id.text1, 
                                                    com.android.internal.R.id.text2,
                                                    com.android.internal.R.id.icon1, 
                                                    com.android.internal.R.id.icon2};
    
    /**
     * A new cursor (with suggestions) is ready for use.  Update the UI.
     */
    void updateSuggestionsWithCursor(Cursor c, final SearchableInfo searchable) {
        ListAdapter adapter = null;
        
        // first, check for various conditions that disqualify this cursor
        if ((c == null) || (c.getCount() == 0)) {
            // no cursor, or cursor with no data
        } else if ((searchable != mSearchable) || !isShowing()) {
            // race condition (suggestions arrived after conditions changed)
        } else {
            // check cursor before trying to create list views from it
            int colId = c.getColumnIndex("_id");
            int col1 = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
            int col2 = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2);
            int colIc1 = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1);
            int colIc2 = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_2);
            
            boolean minimal = (colId >= 0) && (col1 >= 0);
            boolean hasIcons = (colIc1 >= 0) && (colIc2 >= 0);
            boolean has2Lines = col2 >= 0;

            if (minimal) {
                int layout;
                String[] from;
                int[] to;
                
                if (hasIcons) {
                    if (has2Lines) {
                        layout = com.android.internal.R.layout.search_dropdown_item_icons_2line;
                        from = TWO_LINE_ICONS_FROM;
                        to = TWO_LINE_ICONS_TO;
                    } else {
                        layout = com.android.internal.R.layout.search_dropdown_item_icons_1line;
                        from = ONE_LINE_ICONS_FROM;
                        to = ONE_LINE_ICONS_TO;
                    }
                } else {
                    if (has2Lines) {
                        layout = com.android.internal.R.layout.search_dropdown_item_2line;
                        from = TWO_LINE_FROM;
                        to = TWO_LINE_TO;
                    } else {
                        layout = com.android.internal.R.layout.search_dropdown_item_1line;
                        from = ONE_LINE_FROM;
                        to = ONE_LINE_TO;
                    }
                }
                try {
                    if (DBG_LOG_TIMING == 1) {
                        dbgLogTiming("updateSuggestions(3)");
                    }
                    adapter = new SuggestionsCursorAdapter(getContext(), layout, c, from, to, 
                            mProviderContext);
                    if (DBG_LOG_TIMING == 1) {
                        dbgLogTiming("updateSuggestions(4)");
                    }
                } catch (RuntimeException e) {
                    Log.e(LOG_TAG, "Exception while creating SuggestionsCursorAdapter", e);
                }
            }
            
            // Provide some help for developers instead of just silently discarding
            if ((colIc1 >= 0) != (colIc2 >= 0)) {
                Log.w(LOG_TAG, "Suggestion icon column(s) discarded, must be 0 or 2 columns.");
            } else if (adapter == null) {
                Log.w(LOG_TAG, "Suggestions cursor discarded due to missing required columns.");
            }
        }
        
        // if we have a cursor but we're not using it (e.g. disqualified), close it now
        if ((c != null) && (adapter == null)) {
            c.close();
            c = null;
        }
        
        // we only made an adapter if there were 1+ suggestions.  Now, based on the existence
        // of the adapter, we'll also show/hide the list.
        discardListCursor(mSuggestionsList);
        if (adapter == null) {
            showSuggestions(false, !mSkipNextAnimate);
        } else {
            layoutSuggestionsList();
            showSuggestions(true, !mSkipNextAnimate);
        }
        mSkipNextAnimate = false;
        if (DBG_LOG_TIMING == 1) {
            dbgLogTiming("updateSuggestions(5)");
        }
        mSuggestionsList.setAdapter(adapter);
        // now that we have an adapter, we can actually adjust the selection & scroll positions
        if (mPresetSelection >= 0) {
            boolean bTouchMode = mSuggestionsList.isInTouchMode();
            mSuggestionsList.setSelection(mPresetSelection);
            mPresetSelection = -1;
        }
        if (DBG_LOG_TIMING == 1) {
            dbgLogTiming("updateSuggestions(6)");
        }
    }
    
    /**
     * Utility for showing & hiding the suggestions list.  This is also responsible for triggering
     * animation, if any, at the right time.
     * 
     * @param visible If true, show the suggestions, if false, hide them.
     * @param animate If true, use animation.  If false, "just do it."
     */
    private void showSuggestions(boolean visible, boolean animate) {
        if (visible) {
            if (animate && (mSuggestionsList.getVisibility() != View.VISIBLE)) {
                mSuggestionsList.startAnimation(mSuggestionsEntry);
            }
            mSuggestionsList.setVisibility(View.VISIBLE);
        } else {
            if (animate && (mSuggestionsList.getVisibility() != View.GONE)) {
                mSuggestionsList.startAnimation(mSuggestionsExit);
            }
            mSuggestionsList.setVisibility(View.GONE);
        }
    }
    
    /**
     * This helper class supports the suggestions list by allowing 3rd party (e.g. app) resources
     * to be used in suggestions
     */
    private static class SuggestionsCursorAdapter extends SimpleCursorAdapter {
        
        private Resources mProviderResources;
        
        public SuggestionsCursorAdapter(Context context, int layout, Cursor c,
                String[] from, int[] to, Context providerContext) {
            super(context, layout, c, from, to);
            mProviderResources = providerContext.getResources();
        }
        
        /**
         * Overriding this allows us to affect the way that an icon is loaded.  Specifically,
         * we can be more controlling about the resource path (and allow icons to come from other
         * packages).
         *
         * @param v ImageView to receive an image
         * @param value the value retrieved from the cursor
         */
        @Override
        public void setViewImage(ImageView v, String value) {
            int resID;
            Drawable img = null;

            try {
                resID = Integer.parseInt(value);
                if (resID != 0) {
                    img = mProviderResources.getDrawable(resID);
                }
            } catch (NumberFormatException nfe) {
                // img = null;
            } catch (NotFoundException e2) {
                // img = null;
            }
            
            // finally, set the image to whatever we've gotten
            v.setImageDrawable(img);
        }
        
        /**
         * This method is overridden purely to provide a bit of protection against
         * flaky content providers.
         */
        @Override 
        /**
         * @see android.widget.ListAdapter#getView(int, View, ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                return super.getView(position, convertView, parent);
            } catch (RuntimeException e) {
                Log.w(LOG_TAG, "Search Suggestions cursor returned exception " + e.toString());
                // what can I return here?
                View v = newView(mContext, mCursor, parent);
                if (v != null) {
                    TextView tv = (TextView) v.findViewById(com.android.internal.R.id.text1);
                    tv.setText(e.toString());
                }
                return v;
            }
        }
    }
    
    /**
     * Cleanly close the cursor being used by a ListView.  Do this before replacing the adapter
     * or before closing the ListView.
     */
    private void discardListCursor(ListView list) {
        CursorAdapter ca = getSuggestionsAdapter(list);
        if (ca != null) {
            Cursor c = ca.getCursor();
            if (c != null) {
                ca.changeCursor(null);
            }
        }
    }
    
    /**
     * Safely retrieve the suggestions cursor adapter from the ListView
     * 
     * @param adapterView The ListView containing our adapter
     * @result The CursorAdapter that we installed, or null if not set
     */
    private static CursorAdapter getSuggestionsAdapter(AdapterView<?> adapterView) {
        CursorAdapter result = null;
        if (adapterView != null) {
            Object ad = adapterView.getAdapter();
            if (ad instanceof CursorAdapter) {
                result = (CursorAdapter) ad;
            } else if (ad instanceof WrapperListAdapter) {
                result = (CursorAdapter) ((WrapperListAdapter)ad).getWrappedAdapter();
            }
        }
        return result;
    }

    /**
     * Get the query cursor for the search suggestions.
     * 
     * @param query The search text entered (so far)
     * @return Returns a cursor with suggestions, or null if no suggestions 
     */
    private Cursor getSuggestions(final SearchableInfo searchable, final String query) {
        Cursor cursor = null;
        if (searchable.getSuggestAuthority() != null) {
            try {
                StringBuilder uriStr = new StringBuilder("content://");
                uriStr.append(searchable.getSuggestAuthority());

                // if content path provided, insert it now
                final String contentPath = searchable.getSuggestPath();
                if (contentPath != null) {
                    uriStr.append('/');
                    uriStr.append(contentPath);
                }

                // append standard suggestion query path 
                uriStr.append('/' + SearchManager.SUGGEST_URI_PATH_QUERY);

                // inject query, either as selection args or inline
                String[] selArgs = null;
                if (searchable.getSuggestSelection() != null) {    // if selection provided, use it
                    selArgs = new String[] {query};
                } else {
                    uriStr.append('/');                             // no sel, use REST pattern
                    uriStr.append(Uri.encode(query));
                }

                // finally, make the query
                if (DBG_LOG_TIMING == 1) {
                    dbgLogTiming("getSuggestions(1)");
                }
                cursor = getContext().getContentResolver().query(
                                                        Uri.parse(uriStr.toString()), null, 
                                                        searchable.getSuggestSelection(), selArgs,
                                                        null);
                if (DBG_LOG_TIMING == 1) {
                    dbgLogTiming("getSuggestions(2)");
                }
            } catch (RuntimeException e) {
                Log.w(LOG_TAG, "Search Suggestions query returned exception " + e.toString());
                cursor = null;
            }
        }
        
        return cursor;
    }

    /**
     * React to typing in the GO button by refocusing to EditText.  Continue typing the query.
     */
    View.OnKeyListener mGoButtonKeyListener = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // also guard against possible race conditions (late arrival after dismiss)
            if (mSearchable != null) {
                return refocusingKeyListener(v, keyCode, event);
            }
            return false;
        }
    };

    /**
     * React to a click in the GO button by launching a search.
     */
    View.OnClickListener mGoButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // also guard against possible race conditions (late arrival after dismiss)
            if (mSearchable != null) {
                launchQuerySearch(KeyEvent.KEYCODE_UNKNOWN, null);
            }
        }
    };

    /**
     * React to the user typing "enter" or other hardwired keys while typing in the search box.
     * This handles these special keys while the edit box has focus.
     */
    View.OnKeyListener mTextKeyListener = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // also guard against possible race conditions (late arrival after dismiss)
            if (mSearchable != null && 
                    TextUtils.getTrimmedLength(mSearchTextField.getText()) > 0) {
                if (DBG_LOG_TIMING == 1) {
                    dbgLogTiming("doTextKey()");
                }
                switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        v.cancelLongPress();
                        launchQuerySearch(KeyEvent.KEYCODE_UNKNOWN, null);                    
                        return true;
                    }
                    break;
                default:
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        SearchableInfo.ActionKeyInfo actionKey = mSearchable.findActionKey(keyCode);
                        if ((actionKey != null) && (actionKey.mQueryActionMsg != null)) {
                            launchQuerySearch(keyCode, actionKey.mQueryActionMsg);
                            return true;
                        }
                    }
                    break;
                }
            }
            return false;
        }
    };

    /**
     * React to the user typing while the suggestions are focused.  First, check for action
     * keys.  If not handled, try refocusing regular characters into the EditText.  In this case,
     * replace the query text (start typing fresh text).
     */
    View.OnKeyListener mSuggestionsKeyListener = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            boolean handled = false;
            // also guard against possible race conditions (late arrival after dismiss)
            if (mSearchable != null) {
                handled = doSuggestionsKey(v, keyCode, event);
                if (!handled) {
                    handled = refocusingKeyListener(v, keyCode, event);
                }
            }
            return handled;
        }
    };
    
    /**
     * Per UI design, we're going to "steer" any typed keystrokes back into the EditText
     * box, even if the user has navigated the focus to the dropdown or to the GO button.
     * 
     * @param v The view into which the keystroke was typed
     * @param keyCode keyCode of entered key
     * @param event Full KeyEvent record of entered key
     */
    private boolean refocusingKeyListener(View v, int keyCode, KeyEvent event) {
        boolean handled = false;

        if (!event.isSystem() && 
                (keyCode != KeyEvent.KEYCODE_DPAD_UP) &&
                (keyCode != KeyEvent.KEYCODE_DPAD_DOWN) &&
                (keyCode != KeyEvent.KEYCODE_DPAD_LEFT) &&
                (keyCode != KeyEvent.KEYCODE_DPAD_RIGHT) &&
                (keyCode != KeyEvent.KEYCODE_DPAD_CENTER)) {
            // restore focus and give key to EditText ...
            // but don't replace the user's query
            mLeaveJammedQueryOnRefocus = true;
            if (mSearchTextField.requestFocus()) {
                handled = mSearchTextField.dispatchKeyEvent(event);
            }
            mLeaveJammedQueryOnRefocus = false;
        }
        return handled;
    }
    
    /**
     * Update query text based on transitions in and out of suggestions list.
     */
    OnFocusChangeListener mSuggestFocusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            // also guard against possible race conditions (late arrival after dismiss)
            if (mSearchable == null) {
                return;
            }
            // Update query text based on navigation in to/out of the suggestions list
            if (hasFocus) {
                // Entering the list view - record selection point from user's query
                mUserQuery = mSearchTextField.getText().toString();
                mUserQuerySelStart = mSearchTextField.getSelectionStart();
                mUserQuerySelEnd = mSearchTextField.getSelectionEnd();
                // then update the query to match the entered selection
                jamSuggestionQuery(true, mSuggestionsList, 
                                    mSuggestionsList.getSelectedItemPosition());
            } else {
                // Exiting the list view
                
                if (mSuggestionsList.getSelectedItemPosition() < 0) {
                    // Direct exit - Leave new suggestion in place (do nothing)
                } else {
                    // Navigation exit - restore user's query text
                    if (!mLeaveJammedQueryOnRefocus) {
                        jamSuggestionQuery(false, null, -1);
                    }
                }
            }

        }
    };
    
    /**
     * Update query text based on movement of selection in/out of suggestion list
     */
    OnItemSelectedListener mSuggestSelectedListener = new OnItemSelectedListener() {
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            // Update query text while user navigates through suggestions list
            // also guard against possible race conditions (late arrival after dismiss)
            if (mSearchable != null && position >= 0 && mSuggestionsList.isFocused()) {
                jamSuggestionQuery(true, parent, position);
            }
        }

        // No action needed on this callback
        public void onNothingSelected(AdapterView parent) { }        
    };

    /**
     * This is the listener for the ACTION_CLOSE_SYSTEM_DIALOGS intent.  It's an indication that
     * we should close ourselves immediately, in order to allow a higher-priority UI to take over
     * (e.g. phone call received).
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                cancel();
            } else if (Intent.ACTION_PACKAGE_ADDED.equals(action)
                    || Intent.ACTION_PACKAGE_REMOVED.equals(action)
                    || Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
                onPackageListChange();
            }
        }
    };

    /**
     * UI-thread handling of dialog dismiss.  Called by mBroadcastReceiver.onReceive().
     *
     * TODO: This is a really heavyweight solution for something that should be so simple.
     * For example, we already have a handler, in our superclass, why aren't we sharing that?
     * I think we need to investigate simplifying this entire methodology, or perhaps boosting
     * it up into the Dialog class.
     */
    private static final int MESSAGE_DISMISS = 0;
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_DISMISS) {
                dismiss();
            }
        }
    };
    
    /**
     * Listener for layout changes in the main layout.  I use this to dynamically clean up 
     * the layout of the dropdown and make it "pixel perfect."
     */
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener 
        = new ViewTreeObserver.OnGlobalLayoutListener() {
        
        // It's very important that layoutSuggestionsList() does not reset
        // the values more than once, or this becomes an infinite loop.
        public void onGlobalLayout() {
            layoutSuggestionsList();
        }
    };

    /**
     * Various ways to launch searches
     */

    /**
     * React to the user clicking the "GO" button.  Hide the UI and launch a search.
     *
     * @param actionKey Pass a keycode if the launch was triggered by an action key.  Pass
     * KeyEvent.KEYCODE_UNKNOWN for no actionKey code.
     * @param actionMsg Pass the suggestion-provided message if the launch was triggered by an
     * action key.  Pass null for no actionKey message.
     */
    private void launchQuerySearch(int actionKey, final String actionMsg)  {
        final String query = mSearchTextField.getText().toString();
        final Bundle appData = mAppSearchData;
        final SearchableInfo si = mSearchable;      // cache briefly (dismiss() nulls it)
        dismiss();
        sendLaunchIntent(Intent.ACTION_SEARCH, null, query, appData, actionKey, actionMsg, si);
    }

    /**
     * React to the user typing an action key while in the suggestions list
     */
    private boolean doSuggestionsKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (DBG_LOG_TIMING == 1) {
                dbgLogTiming("doSuggestionsKey()");
            }
            
            // First, check for enter or search (both of which we'll treat as a "click")
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
                AdapterView<?> av = (AdapterView<?>) v;
                int position = av.getSelectedItemPosition();
                return launchSuggestion(av, position);
            }
            
            // Next, check for left/right moves while we'll manually grab and shift focus
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                // give focus to text editor
                // but don't restore the user's original query
                mLeaveJammedQueryOnRefocus = true;
                if (mSearchTextField.requestFocus()) {
                    mLeaveJammedQueryOnRefocus = false;
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        mSearchTextField.setSelection(0);
                    } else {
                        mSearchTextField.setSelection(mSearchTextField.length());
                    }
                    return true;
                }
                mLeaveJammedQueryOnRefocus = false;
            }
            
            // Next, check for an "action key"
            SearchableInfo.ActionKeyInfo actionKey = mSearchable.findActionKey(keyCode);
            if ((actionKey != null) && 
                    ((actionKey.mSuggestActionMsg != null) || 
                     (actionKey.mSuggestActionMsgColumn != null))) {
                //   launch suggestion using action key column
                ListView lv = (ListView) v;
                int position = lv.getSelectedItemPosition();
                if (position >= 0) {
                    CursorAdapter ca = getSuggestionsAdapter(lv);
                    Cursor c = ca.getCursor();
                    if (c.moveToPosition(position)) {
                        final String actionMsg = getActionKeyMessage(c, actionKey);
                        if (actionMsg != null && (actionMsg.length() > 0)) {
                            // shut down search bar and launch the activity
                            // cache everything we need because dismiss releases mems
                            setupSuggestionIntent(c, mSearchable);
                            final String query = mSearchTextField.getText().toString();
                            final Bundle appData =  mAppSearchData;
                            SearchableInfo si = mSearchable;
                            String suggestionAction = mSuggestionAction;
                            Uri suggestionData = mSuggestionData;
                            String suggestionQuery = mSuggestionQuery;
                            dismiss();
                            sendLaunchIntent(suggestionAction, suggestionData,
                                    suggestionQuery, appData,
                                             keyCode, actionMsg, si);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }    

    /**
     * Set or reset the user query to follow the selections in the suggestions
     * 
     * @param jamQuery True means to set the query, false means to reset it to the user's choice
     */
    private void jamSuggestionQuery(boolean jamQuery, AdapterView<?> parent, int position) {
        mNonUserQuery = true;       // disables any suggestions processing
        if (jamQuery) {
            CursorAdapter ca = getSuggestionsAdapter(parent);
            Cursor c = ca.getCursor();
            if (c.moveToPosition(position)) {
                setupSuggestionIntent(c, mSearchable);
                String jamText = null;

                // Simple heuristic for selecting text with which to rewrite the query.
                if (mSuggestionQuery != null) {
                    jamText = mSuggestionQuery;
                } else if (mSearchable.mQueryRewriteFromData && (mSuggestionData != null)) {
                    jamText = mSuggestionData.toString();
                } else if (mSearchable.mQueryRewriteFromText) {
                    try {
                        int column = c.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
                        jamText = c.getString(column);
                    } catch (RuntimeException e) {
                        // no work here, jamText is null
                    }
                }
                if (jamText != null) {
                    mSearchTextField.setText(jamText);
                    mSearchTextField.selectAll();
                }
            }
        } else {
            // reset user query
            mSearchTextField.setText(mUserQuery);
            try {
                mSearchTextField.setSelection(mUserQuerySelStart, mUserQuerySelEnd);
            } catch (IndexOutOfBoundsException e) {
                // In case of error, just select all
                Log.e(LOG_TAG, "Caught IndexOutOfBoundsException while setting selection.  " +
                        "start=" + mUserQuerySelStart + " end=" + mUserQuerySelEnd +
                        " text=\"" + mUserQuery + "\"");
                mSearchTextField.selectAll();
            }
        }
        mNonUserQuery = false;
    }

    /**
     * Assemble a search intent and send it.
     *
     * @param action The intent to send, typically Intent.ACTION_SEARCH
     * @param data The data for the intent
     * @param query The user text entered (so far)
     * @param appData The app data bundle (if supplied)
     * @param actionKey If the intent was triggered by an action key, e.g. KEYCODE_CALL, it will
     * be sent here.  Pass KeyEvent.KEYCODE_UNKNOWN for no actionKey code.
     * @param actionMsg If the intent was triggered by an action key, e.g. KEYCODE_CALL, the
     * corresponding tag message will be sent here.  Pass null for no actionKey message.
     * @param si Reference to the current SearchableInfo.  Passed here so it can be used even after
     * we've called dismiss(), which attempts to null mSearchable.
     */
    private void sendLaunchIntent(final String action, final Uri data, final String query,
            final Bundle appData, int actionKey, final String actionMsg, final SearchableInfo si) {
        Intent launcher = new Intent(action);

        if (query != null) {
            launcher.putExtra(SearchManager.QUERY, query);
        }

        if (data != null) {
            launcher.setData(data);
        }

        if (appData != null) {
            launcher.putExtra(SearchManager.APP_DATA, appData);
        }

        // add launch info (action key, etc.)
        if (actionKey != KeyEvent.KEYCODE_UNKNOWN) {
            launcher.putExtra(SearchManager.ACTION_KEY, actionKey);
            launcher.putExtra(SearchManager.ACTION_MSG, actionMsg);
        }

        // attempt to enforce security requirement (no 3rd-party intents)
        launcher.setComponent(si.mSearchActivity);

        getContext().startActivity(launcher);
    }

    /**
     * Handler for clicks in the suggestions list
     */
    private OnItemClickListener mSuggestionsListItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // this guard protects against possible race conditions (late arrival of click)
            if (mSearchable != null) {
                launchSuggestion(parent, position);
            }
        }
    };
    
    /**
     * Shared code for launching a query from a suggestion.
     * 
     * @param av The AdapterView (really a ListView) containing the suggestions
     * @param position The suggestion we'll be launching from
     * 
     * @return Returns true if a successful launch, false if could not (e.g. bad position)
     */
    private boolean launchSuggestion(AdapterView<?> av, int position) {
        CursorAdapter ca = getSuggestionsAdapter(av);
        Cursor c = ca.getCursor();
        if ((c != null) && c.moveToPosition(position)) {
            setupSuggestionIntent(c, mSearchable);
            
            final Bundle appData =  mAppSearchData;
            SearchableInfo si = mSearchable;
            String suggestionAction = mSuggestionAction;
            Uri suggestionData = mSuggestionData;
            String suggestionQuery = mSuggestionQuery;
            dismiss();
            sendLaunchIntent(suggestionAction, suggestionData, suggestionQuery, appData,
                                KeyEvent.KEYCODE_UNKNOWN, null, si);
            return true;
        }
        return false;
    }
    
    /**
     * Manually adjust suggestions list into its perfectly-tweaked position.
     * 
     * NOTE:  This MUST not adjust the parameters if they are already set correctly,
     * or you create an infinite loop via the ViewTreeObserver.OnGlobalLayoutListener callback. 
     */
    private void layoutSuggestionsList() {
        final int FUDGE_SUGG_X = 1;
        final int FUDGE_SUGG_WIDTH = 2;
        
        int[] itemLoc = new int[2];
        mSearchTextField.getLocationOnScreen(itemLoc);
        int x,width;
        x = itemLoc[0] + FUDGE_SUGG_X;
        width = mSearchTextField.getMeasuredWidth() + FUDGE_SUGG_WIDTH;
        
        // now set params and relayout
        ViewGroup.MarginLayoutParams lp;
        lp = (ViewGroup.MarginLayoutParams) mSuggestionsList.getLayoutParams();
        boolean changing = (lp.width != width) || (lp.leftMargin != x);
        if (changing) {
            lp.leftMargin = x;
            lp.width = width;
            mSuggestionsList.setLayoutParams(lp);
        }
    }
    
    /**
     * When a particular suggestion has been selected, perform the various lookups required
     * to use the suggestion.  This includes checking the cursor for suggestion-specific data,
     * and/or falling back to the XML for defaults;  It also creates REST style Uri data when
     * the suggestion includes a data id.
     * 
     * NOTE:  Return values are in member variables mSuggestionAction & mSuggestionData.
     * 
     * @param c The suggestions cursor, moved to the row of the user's selection
     * @param si The searchable activity's info record
     */
    void setupSuggestionIntent(Cursor c, SearchableInfo si) {
        try {
            // use specific action if supplied, or default action if supplied, or fixed default
            mSuggestionAction = null;
            int mColumn = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_ACTION);
            if (mColumn >= 0) {
                final String action = c.getString(mColumn);
                if (action != null) {
                    mSuggestionAction = action;
                }
            }
            if (mSuggestionAction == null) {
                mSuggestionAction = si.getSuggestIntentAction();
            }
            if (mSuggestionAction == null) {
                mSuggestionAction = Intent.ACTION_SEARCH;
            }
            
            // use specific data if supplied, or default data if supplied
            String data = null;
            mColumn = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA);
            if (mColumn >= 0) {
                final String rowData = c.getString(mColumn);
                if (rowData != null) {
                    data = rowData;
                }
            }
            if (data == null) {
                data = si.getSuggestIntentData();
            }
            
            // then, if an ID was provided, append it.
            if (data != null) {
                mColumn = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
                if (mColumn >= 0) {
                    final String id = c.getString(mColumn);
                    if (id != null) {
                        data = data + "/" + Uri.encode(id);
                    }
                }
            }
            mSuggestionData = (data == null) ? null : Uri.parse(data);
            
            mSuggestionQuery = null;
            mColumn = c.getColumnIndex(SearchManager.SUGGEST_COLUMN_QUERY);
            if (mColumn >= 0) {
                final String query = c.getString(mColumn);
                if (query != null) {
                    mSuggestionQuery = query;
                }
            }
        } catch (RuntimeException e ) {
            int rowNum;
            try {                       // be really paranoid now
                rowNum = c.getPosition();
            } catch (RuntimeException e2 ) {
                rowNum = -1;
            }
            Log.w(LOG_TAG, "Search Suggestions cursor at row " + rowNum + 
                            " returned exception" + e.toString());
        }
    }
    
    /**
     * For a given suggestion and a given cursor row, get the action message.  If not provided
     * by the specific row/column, also check for a single definition (for the action key).
     * 
     * @param c The cursor providing suggestions
     * @param actionKey The actionkey record being examined
     * 
     * @return Returns a string, or null if no action key message for this suggestion
     */
    private String getActionKeyMessage(Cursor c, final SearchableInfo.ActionKeyInfo actionKey) {
        String result = null;
        // check first in the cursor data, for a suggestion-specific message
        final String column = actionKey.mSuggestActionMsgColumn;
        if (column != null) {
            try {
                int colId = c.getColumnIndexOrThrow(column);
                result = c.getString(colId);
            } catch (RuntimeException e) {
                // OK - result is already null
            }
        }
        // If the cursor didn't give us a message, see if there's a single message defined
        // for the actionkey (for all suggestions)
        if (result == null) {
            result = actionKey.mSuggestActionMsg;
        }
        return result;
    }

    /**
     * Debugging Support
     */

    /**
     * For debugging only, sample the millisecond clock and log it.
     * Uses AtomicLong so we can use in multiple threads
     */
    private AtomicLong mLastLogTime = new AtomicLong(SystemClock.uptimeMillis());
    private void dbgLogTiming(final String caller) {
        long millis = SystemClock.uptimeMillis();
        long oldTime = mLastLogTime.getAndSet(millis);
        long delta = millis - oldTime;
        final String report = millis + " (+" + delta + ") ticks for Search keystroke in " + caller;
        Log.d(LOG_TAG,report);
>>>>>>> 54b6cfa... Initial Contribution
    }
}
