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

package com.android.internal.view.menu;


<<<<<<< HEAD
import com.android.internal.view.menu.MenuBuilder.ItemInvoker;

=======
>>>>>>> 54b6cfa... Initial Contribution
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
<<<<<<< HEAD
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
=======
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.internal.view.menu.MenuBuilder.ItemInvoker;
>>>>>>> 54b6cfa... Initial Contribution

/**
 * The expanded menu view is a list-like menu with all of the available menu items.  It is opened
 * by the user clicking no the 'More' button on the icon menu view.
 */
public final class ExpandedMenuView extends ListView implements ItemInvoker, MenuView, OnItemClickListener {
    private MenuBuilder mMenu;

    /** Default animations for this menu */
    private int mAnimations;
    
    /**
     * Instantiates the ExpandedMenuView that is linked with the provided MenuBuilder.
     * @param menu The model for the menu which this MenuView will display
     */
    public ExpandedMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.MenuView, 0, 0);
        mAnimations = a.getResourceId(com.android.internal.R.styleable.MenuView_windowAnimationStyle, 0);
        a.recycle();

        setOnItemClickListener(this);
    }

<<<<<<< HEAD
    public void initialize(MenuBuilder menu) {
        mMenu = menu;
=======
    public void initialize(MenuBuilder menu, int menuType) {
        mMenu = menu;

        setAdapter(menu.new MenuAdapter(menuType));
    }

    public void updateChildren(boolean cleared) {
        ListAdapter adapter = getAdapter();
        // Tell adapter of the change, it will notify the mListView
        if (adapter != null) {
            if (cleared) {
                ((BaseAdapter)adapter).notifyDataSetInvalidated();
            }
            else {
                ((BaseAdapter)adapter).notifyDataSetChanged();
            }
        }
>>>>>>> 54b6cfa... Initial Contribution
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        
        // Clear the cached bitmaps of children
        setChildrenDrawingCacheEnabled(false);
    }

    public boolean invokeItem(MenuItemImpl item) {
        return mMenu.performItemAction(item, 0);
    }

    public void onItemClick(AdapterView parent, View v, int position, long id) {
        invokeItem((MenuItemImpl) getAdapter().getItem(position));
    }

    public int getWindowAnimations() {
        return mAnimations;
    }
    
}
