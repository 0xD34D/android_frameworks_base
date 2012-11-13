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

import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
=======
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
>>>>>>> 54b6cfa... Initial Contribution
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Displays a list of all activities which can be performed
 * for a given intent. Launches when clicked.
 *
 */
public abstract class LauncherActivity extends ListActivity {
<<<<<<< HEAD
    Intent mIntent;
    PackageManager mPackageManager;
    IconResizer mIconResizer;
    
    /**
     * An item in the list
     */
    public static class ListItem {
        public ResolveInfo resolveInfo;
        public CharSequence label;
        public Drawable icon;
        public String packageName;
        public String className;
        public Bundle extras;
        
        ListItem(PackageManager pm, ResolveInfo resolveInfo, IconResizer resizer) {
            this.resolveInfo = resolveInfo;
            label = resolveInfo.loadLabel(pm);
            ComponentInfo ci = resolveInfo.activityInfo;
            if (ci == null) ci = resolveInfo.serviceInfo;
            if (label == null && ci != null) {
                label = resolveInfo.activityInfo.name;
            }
            
            if (resizer != null) {
                icon = resizer.createIconThumbnail(resolveInfo.loadIcon(pm));
            }
            packageName = ci.applicationInfo.packageName;
            className = ci.name;
        }

        public ListItem() {
        }
    }

    /**
=======
    
    /**
>>>>>>> 54b6cfa... Initial Contribution
     * Adapter which shows the set of activities that can be performed for a given intent.
     */
    private class ActivityAdapter extends BaseAdapter implements Filterable {
        private final Object lock = new Object();
<<<<<<< HEAD
        private ArrayList<ListItem> mOriginalValues;

        protected final IconResizer mIconResizer;
        protected final LayoutInflater mInflater;

        protected List<ListItem> mActivitiesList;

        private Filter mFilter;
        private final boolean mShowIcons;
        
        public ActivityAdapter(IconResizer resizer) {
            mIconResizer = resizer;
            mInflater = (LayoutInflater) LauncherActivity.this.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            mShowIcons = onEvaluateShowIcons();
            mActivitiesList = makeListItems();
        }

        public Intent intentForPosition(int position) {
            if (mActivitiesList == null) {
                return null;
            }

            Intent intent = new Intent(mIntent);
            ListItem item = mActivitiesList.get(position);
            intent.setClassName(item.packageName, item.className);
            if (item.extras != null) {
                intent.putExtras(item.extras);
            }
            return intent;
        }

        public ListItem itemForPosition(int position) {
=======
        private ArrayList<ResolveInfo> mOriginalValues;

        protected final Context mContext;
        protected final Intent mIntent;
        protected final LayoutInflater mInflater;

        protected List<ResolveInfo> mActivitiesList;

        private Filter mFilter;

        public ActivityAdapter(Context context, Intent intent) {
            mContext = context;
            mIntent = new Intent(intent);
            mIntent.setComponent(null);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            PackageManager pm = context.getPackageManager();
            mActivitiesList = pm.queryIntentActivities(intent, 0);
            if (mActivitiesList != null) {
                Collections.sort(mActivitiesList, new ResolveInfo.DisplayNameComparator(pm));
            }
        }

        public Intent intentForPosition(int position) {
>>>>>>> 54b6cfa... Initial Contribution
            if (mActivitiesList == null) {
                return null;
            }

<<<<<<< HEAD
            return mActivitiesList.get(position);
=======
            Intent intent = new Intent(mIntent);
            ActivityInfo ai = mActivitiesList.get(position).activityInfo;
            intent.setClassName(ai.applicationInfo.packageName, ai.name);
            return intent;
>>>>>>> 54b6cfa... Initial Contribution
        }

        public int getCount() {
            return mActivitiesList != null ? mActivitiesList.size() : 0;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = mInflater.inflate(
<<<<<<< HEAD
                        com.android.internal.R.layout.activity_list_item_2, parent, false);
=======
                        com.android.internal.R.layout.simple_list_item_1, parent, false);
>>>>>>> 54b6cfa... Initial Contribution
            } else {
                view = convertView;
            }
            bindView(view, mActivitiesList.get(position));
            return view;
        }

<<<<<<< HEAD
        private void bindView(View view, ListItem item) {
            TextView text = (TextView) view;
            text.setText(item.label);
            if (mShowIcons) {
                if (item.icon == null) {
                    item.icon = mIconResizer.createIconThumbnail(item.resolveInfo.loadIcon(getPackageManager()));
                }
                text.setCompoundDrawablesWithIntrinsicBounds(item.icon, null, null, null);
            }
=======
        private char getCandidateLetter(ResolveInfo info) {
            PackageManager pm = mContext.getPackageManager();
            CharSequence label = info.loadLabel(pm);

            if (label == null) {
                label = info.activityInfo.name;
            }

            return Character.toLowerCase(label.charAt(0));
        }

        private void bindView(View view, ResolveInfo info) {
            TextView text = (TextView) view.findViewById(com.android.internal.R.id.text1);

            PackageManager pm = mContext.getPackageManager();
            CharSequence label = info.loadLabel(pm);
            text.setText(label != null ? label : info.activityInfo.name);
>>>>>>> 54b6cfa... Initial Contribution
        }

        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ArrayFilter();
            }
            return mFilter;
        }
<<<<<<< HEAD
        
        /**
         * An array filters constrains the content of the array adapter with a prefix. Each
         * item that does not start with the supplied prefix is removed from the list.
=======

        /**
         * <p>An array filters constrains the content of the array adapter with a prefix. Each item that
         * does not start with the supplied prefix is removed from the list.</p>
>>>>>>> 54b6cfa... Initial Contribution
         */
        private class ArrayFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();

                if (mOriginalValues == null) {
                    synchronized (lock) {
<<<<<<< HEAD
                        mOriginalValues = new ArrayList<ListItem>(mActivitiesList);
=======
                        mOriginalValues = new ArrayList<ResolveInfo>(mActivitiesList);
>>>>>>> 54b6cfa... Initial Contribution
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
<<<<<<< HEAD
                        ArrayList<ListItem> list = new ArrayList<ListItem>(mOriginalValues);
=======
                        ArrayList<ResolveInfo> list = new ArrayList<ResolveInfo>(mOriginalValues);
>>>>>>> 54b6cfa... Initial Contribution
                        results.values = list;
                        results.count = list.size();
                    }
                } else {
<<<<<<< HEAD
                    final String prefixString = prefix.toString().toLowerCase();

                    ArrayList<ListItem> values = mOriginalValues;
                    int count = values.size();

                    ArrayList<ListItem> newValues = new ArrayList<ListItem>(count);

                    for (int i = 0; i < count; i++) {
                        ListItem item = values.get(i);

                        String[] words = item.label.toString().toLowerCase().split(" ");
=======
                    final PackageManager pm = mContext.getPackageManager();
                    final String prefixString = prefix.toString().toLowerCase();

                    ArrayList<ResolveInfo> values = mOriginalValues;
                    int count = values.size();

                    ArrayList<ResolveInfo> newValues = new ArrayList<ResolveInfo>(count);

                    for (int i = 0; i < count; i++) {
                        ResolveInfo value = values.get(i);

                        final CharSequence label = value.loadLabel(pm);
                        final CharSequence name = label != null ? label : value.activityInfo.name;

                        String[] words = name.toString().toLowerCase().split(" ");
>>>>>>> 54b6cfa... Initial Contribution
                        int wordCount = words.length;

                        for (int k = 0; k < wordCount; k++) {
                            final String word = words[k];

                            if (word.startsWith(prefixString)) {
<<<<<<< HEAD
                                newValues.add(item);
=======
                                newValues.add(value);
>>>>>>> 54b6cfa... Initial Contribution
                                break;
                            }
                        }
                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
<<<<<<< HEAD
                mActivitiesList = (List<ListItem>) results.values;
=======
                mActivitiesList = (List<ResolveInfo>) results.values;
>>>>>>> 54b6cfa... Initial Contribution
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }
<<<<<<< HEAD
        
    /**
     * Utility class to resize icons to match default icon size.  
     */
    public class IconResizer {
        // Code is borrowed from com.android.launcher.Utilities. 
        private int mIconWidth = -1;
        private int mIconHeight = -1;

        private final Rect mOldBounds = new Rect();
        private Canvas mCanvas = new Canvas();
        
        public IconResizer() {
            mCanvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG,
                    Paint.FILTER_BITMAP_FLAG));
            
            final Resources resources = LauncherActivity.this.getResources();
            mIconWidth = mIconHeight = (int) resources.getDimension(
                    android.R.dimen.app_icon_size);
        }

        /**
         * Returns a Drawable representing the thumbnail of the specified Drawable.
         * The size of the thumbnail is defined by the dimension
         * android.R.dimen.launcher_application_icon_size.
         *
         * This method is not thread-safe and should be invoked on the UI thread only.
         *
         * @param icon The icon to get a thumbnail of.
         *
         * @return A thumbnail for the specified icon or the icon itself if the
         *         thumbnail could not be created. 
         */
        public Drawable createIconThumbnail(Drawable icon) {
            int width = mIconWidth;
            int height = mIconHeight;

            final int iconWidth = icon.getIntrinsicWidth();
            final int iconHeight = icon.getIntrinsicHeight();

            if (icon instanceof PaintDrawable) {
                PaintDrawable painter = (PaintDrawable) icon;
                painter.setIntrinsicWidth(width);
                painter.setIntrinsicHeight(height);
            }

            if (width > 0 && height > 0) {
                if (width < iconWidth || height < iconHeight) {
                    final float ratio = (float) iconWidth / iconHeight;

                    if (iconWidth > iconHeight) {
                        height = (int) (width / ratio);
                    } else if (iconHeight > iconWidth) {
                        width = (int) (height * ratio);
                    }

                    final Bitmap.Config c = icon.getOpacity() != PixelFormat.OPAQUE ?
                                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                    final Bitmap thumb = Bitmap.createBitmap(mIconWidth, mIconHeight, c);
                    final Canvas canvas = mCanvas;
                    canvas.setBitmap(thumb);
                    // Copy the old bounds to restore them later
                    // If we were to do oldBounds = icon.getBounds(),
                    // the call to setBounds() that follows would
                    // change the same instance and we would lose the
                    // old bounds
                    mOldBounds.set(icon.getBounds());
                    final int x = (mIconWidth - width) / 2;
                    final int y = (mIconHeight - height) / 2;
                    icon.setBounds(x, y, x + width, y + height);
                    icon.draw(canvas);
                    icon.setBounds(mOldBounds);
                    icon = new BitmapDrawable(getResources(), thumb);
                    canvas.setBitmap(null);
                } else if (iconWidth < width && iconHeight < height) {
                    final Bitmap.Config c = Bitmap.Config.ARGB_8888;
                    final Bitmap thumb = Bitmap.createBitmap(mIconWidth, mIconHeight, c);
                    final Canvas canvas = mCanvas;
                    canvas.setBitmap(thumb);
                    mOldBounds.set(icon.getBounds());
                    final int x = (width - iconWidth) / 2;
                    final int y = (height - iconHeight) / 2;
                    icon.setBounds(x, y, x + iconWidth, y + iconHeight);
                    icon.draw(canvas);
                    icon.setBounds(mOldBounds);
                    icon = new BitmapDrawable(getResources(), thumb);
                    canvas.setBitmap(null);
                }
            }

            return icon;
        }
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        mPackageManager = getPackageManager();
    
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        onSetContentView();

        mIconResizer = new IconResizer();
        
        mIntent = new Intent(getTargetIntent());
        mIntent.setComponent(null);
        mAdapter = new ActivityAdapter(mIconResizer);

        setListAdapter(mAdapter);
        getListView().setTextFilterEnabled(true);

        updateAlertTitle();
        updateButtonText();

        setProgressBarIndeterminateVisibility(false);
    }

    private void updateAlertTitle() {
        TextView alertTitle = (TextView) findViewById(com.android.internal.R.id.alertTitle);
        if (alertTitle != null) {
            alertTitle.setText(getTitle());
        }
    }

    private void updateButtonText() {
        Button cancelButton = (Button) findViewById(com.android.internal.R.id.button1);
        if (cancelButton != null) {
            cancelButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        updateAlertTitle();
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        updateAlertTitle();
    }

    /**
     * Override to call setContentView() with your own content view to
     * customize the list layout.
     */
    protected void onSetContentView() {
        setContentView(com.android.internal.R.layout.activity_list);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = intentForPosition(position);
        startActivity(intent);
    }
    
    /**
     * Return the actual Intent for a specific position in our
     * {@link android.widget.ListView}.
     * @param position The item whose Intent to return
     */
    protected Intent intentForPosition(int position) {
        ActivityAdapter adapter = (ActivityAdapter) mAdapter;
        return adapter.intentForPosition(position);
    }
    
    /**
     * Return the {@link ListItem} for a specific position in our
     * {@link android.widget.ListView}.
     * @param position The item to return
     */
    protected ListItem itemForPosition(int position) {
        ActivityAdapter adapter = (ActivityAdapter) mAdapter;
        return adapter.itemForPosition(position);
    }
    
    /**
     * Get the base intent to use when running
     * {@link PackageManager#queryIntentActivities(Intent, int)}.
     */
    protected Intent getTargetIntent() {
        return new Intent();
    }

    /**
     * Perform query on package manager for list items.  The default
     * implementation queries for activities.
     */
    protected List<ResolveInfo> onQueryPackageManager(Intent queryIntent) {
        return mPackageManager.queryIntentActivities(queryIntent, /* no flags */ 0);
    }
    
    /**
     * Perform the query to determine which results to show and return a list of them.
     */
    public List<ListItem> makeListItems() {
        // Load all matching activities and sort correctly
        List<ResolveInfo> list = onQueryPackageManager(mIntent);
        Collections.sort(list, new ResolveInfo.DisplayNameComparator(mPackageManager));

        ArrayList<ListItem> result = new ArrayList<ListItem>(list.size());
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            ResolveInfo resolveInfo = list.get(i);
            result.add(new ListItem(mPackageManager, resolveInfo, null));
        }

        return result;
    }

    /**
     * Whether or not to show icons in the list
     * @hide keeping this private for now, since only Settings needs it
     * @return true to show icons beside the activity names, false otherwise
     */
    protected boolean onEvaluateShowIcons() {
        return true;
    }
=======
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAdapter = new ActivityAdapter(this, getTargetIntent());
        
        setListAdapter(mAdapter);
        getListView().setTextFilterEnabled(true);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = ((ActivityAdapter)mAdapter).intentForPosition(position);

        startActivity(intent);
    }
    
    protected abstract Intent getTargetIntent();
   
>>>>>>> 54b6cfa... Initial Contribution
}
