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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
<<<<<<< HEAD
import android.view.ViewGroup;
=======
>>>>>>> 54b6cfa... Initial Contribution
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * The item view for each item in the ListView-based MenuViews.
 */
public class ListMenuItemView extends LinearLayout implements MenuView.ItemView {
<<<<<<< HEAD
    private static final String TAG = "ListMenuItemView";
=======
>>>>>>> 54b6cfa... Initial Contribution
    private MenuItemImpl mItemData; 
    
    private ImageView mIconView;
    private RadioButton mRadioButton;
    private TextView mTitleView;
    private CheckBox mCheckBox;
    private TextView mShortcutView;
    
    private Drawable mBackground;
    private int mTextAppearance;
    private Context mTextAppearanceContext;
<<<<<<< HEAD
    private boolean mPreserveIconSpacing;
    
    private int mMenuType;
    
    private LayoutInflater mInflater;

    private boolean mForceShowIcon;

=======
    
    private int mMenuType;
    
>>>>>>> 54b6cfa... Initial Contribution
    public ListMenuItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    
        TypedArray a =
            context.obtainStyledAttributes(
                attrs, com.android.internal.R.styleable.MenuView, defStyle, 0);
        
        mBackground = a.getDrawable(com.android.internal.R.styleable.MenuView_itemBackground);
        mTextAppearance = a.getResourceId(com.android.internal.R.styleable.
                                          MenuView_itemTextAppearance, -1);
<<<<<<< HEAD
        mPreserveIconSpacing = a.getBoolean(
                com.android.internal.R.styleable.MenuView_preserveIconSpacing, false);
=======
>>>>>>> 54b6cfa... Initial Contribution
        mTextAppearanceContext = context;
        
        a.recycle();
    }

    public ListMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        setBackgroundDrawable(mBackground);
        
        mTitleView = (TextView) findViewById(com.android.internal.R.id.title);
        if (mTextAppearance != -1) {
            mTitleView.setTextAppearance(mTextAppearanceContext,
                                         mTextAppearance);
        }
        
        mShortcutView = (TextView) findViewById(com.android.internal.R.id.shortcut);
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        mItemData = itemData;
        mMenuType = menuType;

        setVisibility(itemData.isVisible() ? View.VISIBLE : View.GONE);
        
        setTitle(itemData.getTitleForItemView(this));
        setCheckable(itemData.isCheckable());
        setShortcut(itemData.shouldShowShortcut(), itemData.getShortcut());
        setIcon(itemData.getIcon());
        setEnabled(itemData.isEnabled());
    }

<<<<<<< HEAD
    public void setForceShowIcon(boolean forceShow) {
        mPreserveIconSpacing = mForceShowIcon = forceShow;
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    public void setTitle(CharSequence title) {
        if (title != null) {
            mTitleView.setText(title);
            
            if (mTitleView.getVisibility() != VISIBLE) mTitleView.setVisibility(VISIBLE);
        } else {
            if (mTitleView.getVisibility() != GONE) mTitleView.setVisibility(GONE);
        }
    }
    
    public MenuItemImpl getItemData() {
        return mItemData;
    }

    public void setCheckable(boolean checkable) {
<<<<<<< HEAD
=======
        
>>>>>>> 54b6cfa... Initial Contribution
        if (!checkable && mRadioButton == null && mCheckBox == null) {
            return;
        }
        
<<<<<<< HEAD
=======
        if (mRadioButton == null) {
            insertRadioButton();
        }
        if (mCheckBox == null) {
            insertCheckBox();
        }
        
>>>>>>> 54b6cfa... Initial Contribution
        // Depending on whether its exclusive check or not, the checkbox or
        // radio button will be the one in use (and the other will be otherCompoundButton)
        final CompoundButton compoundButton;
        final CompoundButton otherCompoundButton; 

        if (mItemData.isExclusiveCheckable()) {
<<<<<<< HEAD
            if (mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = mRadioButton;
            otherCompoundButton = mCheckBox;
        } else {
            if (mCheckBox == null) {
                insertCheckBox();
            }
=======
            compoundButton = mRadioButton;
            otherCompoundButton = mCheckBox;
        } else {
>>>>>>> 54b6cfa... Initial Contribution
            compoundButton = mCheckBox;
            otherCompoundButton = mRadioButton;
        }
        
        if (checkable) {
            compoundButton.setChecked(mItemData.isChecked());
            
            final int newVisibility = checkable ? VISIBLE : GONE;
            if (compoundButton.getVisibility() != newVisibility) {
                compoundButton.setVisibility(newVisibility);
            }
            
            // Make sure the other compound button isn't visible
<<<<<<< HEAD
            if (otherCompoundButton != null && otherCompoundButton.getVisibility() != GONE) {
                otherCompoundButton.setVisibility(GONE);
            }
        } else {
            if (mCheckBox != null) mCheckBox.setVisibility(GONE);
            if (mRadioButton != null) mRadioButton.setVisibility(GONE);
=======
            if (otherCompoundButton.getVisibility() != GONE) {
                otherCompoundButton.setVisibility(GONE);
            }
        } else {
            mCheckBox.setVisibility(GONE);
            mRadioButton.setVisibility(GONE);
>>>>>>> 54b6cfa... Initial Contribution
        }
    }
    
    public void setChecked(boolean checked) {
        CompoundButton compoundButton;
        
        if (mItemData.isExclusiveCheckable()) {
            if (mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = mRadioButton;
        } else {
            if (mCheckBox == null) {
                insertCheckBox();
            }
            compoundButton = mCheckBox;
        }
        
        compoundButton.setChecked(checked);
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
<<<<<<< HEAD
        final int newVisibility = (showShortcut && mItemData.shouldShowShortcut())
                ? VISIBLE : GONE;

        if (newVisibility == VISIBLE) {
            mShortcutView.setText(mItemData.getShortcutLabel());
        }

=======
        mShortcutView.setText(mItemData.getShortcutLabel());

        final int newVisibility = showShortcut ? VISIBLE : GONE;
>>>>>>> 54b6cfa... Initial Contribution
        if (mShortcutView.getVisibility() != newVisibility) {
            mShortcutView.setVisibility(newVisibility);
        }
    }
    
    public void setIcon(Drawable icon) {
<<<<<<< HEAD
        final boolean showIcon = mItemData.shouldShowIcon() || mForceShowIcon;
        if (!showIcon && !mPreserveIconSpacing) {
            return;
        }
        
        if (mIconView == null && icon == null && !mPreserveIconSpacing) {
=======
        
        if (!mItemData.shouldShowIcon(mMenuType)) {
            return;
        }
        
        if (mIconView == null && icon == null) {
>>>>>>> 54b6cfa... Initial Contribution
            return;
        }
        
        if (mIconView == null) {
            insertIconView();
        }
        
<<<<<<< HEAD
        if (icon != null || mPreserveIconSpacing) {
            mIconView.setImageDrawable(showIcon ? icon : null);
=======
        if (icon != null) {
            mIconView.setImageDrawable(icon);
>>>>>>> 54b6cfa... Initial Contribution

            if (mIconView.getVisibility() != VISIBLE) {
                mIconView.setVisibility(VISIBLE);
            }
        } else {
            mIconView.setVisibility(GONE);
        }
    }
    
<<<<<<< HEAD
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIconView != null && mPreserveIconSpacing) {
            // Enforce minimum icon spacing
            ViewGroup.LayoutParams lp = getLayoutParams();
            LayoutParams iconLp = (LayoutParams) mIconView.getLayoutParams();
            if (lp.height > 0 && iconLp.width <= 0) {
                iconLp.width = lp.height;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void insertIconView() {
        LayoutInflater inflater = getInflater();
=======
    private void insertIconView() {
        LayoutInflater inflater = mItemData.getLayoutInflater(mMenuType);
>>>>>>> 54b6cfa... Initial Contribution
        mIconView = (ImageView) inflater.inflate(com.android.internal.R.layout.list_menu_item_icon,
                this, false);
        addView(mIconView, 0);
    }
    
    private void insertRadioButton() {
<<<<<<< HEAD
        LayoutInflater inflater = getInflater();
        mRadioButton =
                (RadioButton) inflater.inflate(com.android.internal.R.layout.list_menu_item_radio,
                this, false);
        addView(mRadioButton);
    }
    
    private void insertCheckBox() {
        LayoutInflater inflater = getInflater();
        mCheckBox =
                (CheckBox) inflater.inflate(com.android.internal.R.layout.list_menu_item_checkbox,
                this, false);
        addView(mCheckBox);
=======
        LayoutInflater inflater = mItemData.getLayoutInflater(mMenuType);
        mRadioButton =
                (RadioButton) inflater.inflate(com.android.internal.R.layout.list_menu_item_radio,
                this, false);
        addView(mRadioButton, 0);
    }
    
    private void insertCheckBox() {
        LayoutInflater inflater = mItemData.getLayoutInflater(mMenuType);
        mCheckBox =
                (CheckBox) inflater.inflate(com.android.internal.R.layout.list_menu_item_checkbox,
                this, false);
        addView(mCheckBox, 0);
>>>>>>> 54b6cfa... Initial Contribution
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public boolean showsIcon() {
<<<<<<< HEAD
        return mForceShowIcon;
    }
    
    private LayoutInflater getInflater() {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(mContext);
        }
        return mInflater;
    }
=======
        return false;
    }
    
>>>>>>> 54b6cfa... Initial Contribution
}
