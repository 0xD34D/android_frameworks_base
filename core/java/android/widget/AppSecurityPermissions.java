/*
**
** Copyright 2007, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/
package android.widget;

import com.android.internal.R;
<<<<<<< HEAD

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
=======
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
>>>>>>> 54b6cfa... Initial Contribution
import java.util.Set;

/**
 * This class contains the SecurityPermissions view implementation.
 * Initially the package's advanced or dangerous security permissions
 * are displayed under categorized
 * groups. Clicking on the additional permissions presents
 * extended information consisting of all groups and permissions.
 * To use this view define a LinearLayout or any ViewGroup and add this
 * view by instantiating AppSecurityPermissions and invoking getPermissionsView.
 * 
 * {@hide}
 */
public class AppSecurityPermissions  implements View.OnClickListener {

    private enum State {
        NO_PERMS,
        DANGEROUS_ONLY,
        NORMAL_ONLY,
        BOTH
    }

<<<<<<< HEAD
    private final static String TAG = "AppSecurityPermissions";
=======
    private final String TAG = "AppSecurityPermissions";
>>>>>>> 54b6cfa... Initial Contribution
    private boolean localLOGV = false;
    private Context mContext;
    private LayoutInflater mInflater;
    private PackageManager mPm;
    private LinearLayout mPermsView;
<<<<<<< HEAD
    private Map<String, String> mDangerousMap;
    private Map<String, String> mNormalMap;
    private List<PermissionInfo> mPermsList;
=======
    private HashMap<String, String> mDangerousMap;
    private HashMap<String, String> mNormalMap;
    private ArrayList<PermissionInfo> mPermsList;
>>>>>>> 54b6cfa... Initial Contribution
    private String mDefaultGrpLabel;
    private String mDefaultGrpName="DefaultGrp";
    private String mPermFormat;
    private Drawable mNormalIcon;
    private Drawable mDangerousIcon;
    private boolean mExpanded;
    private Drawable mShowMaxIcon;
    private Drawable mShowMinIcon;
    private View mShowMore;
    private TextView mShowMoreText;
    private ImageView mShowMoreIcon;
    private State mCurrentState;
    private LinearLayout mNonDangerousList;
    private LinearLayout mDangerousList;
<<<<<<< HEAD
    private HashMap<String, CharSequence> mGroupLabelCache;
    private View mNoPermsView;
    
    public AppSecurityPermissions(Context context, List<PermissionInfo> permList) {
        mContext = context;
        mPm = mContext.getPackageManager();
        mPermsList = permList;
    }
    
    public AppSecurityPermissions(Context context, String packageName) {
        mContext = context;
        mPm = mContext.getPackageManager();
        mPermsList = new ArrayList<PermissionInfo>();
        Set<PermissionInfo> permSet = new HashSet<PermissionInfo>();
        PackageInfo pkgInfo;
        try {
            pkgInfo = mPm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Could'nt retrieve permissions for package:"+packageName);
            return;
        }
        // Extract all user permissions
        if((pkgInfo.applicationInfo != null) && (pkgInfo.applicationInfo.uid != -1)) {
            getAllUsedPermissions(pkgInfo.applicationInfo.uid, permSet);
        }
        for(PermissionInfo tmpInfo : permSet) {
            mPermsList.add(tmpInfo);
        }
    }
    
    public AppSecurityPermissions(Context context, PackageParser.Package pkg) {
        mContext = context;
        mPm = mContext.getPackageManager();
        mPermsList = new ArrayList<PermissionInfo>();
        Set<PermissionInfo> permSet = new HashSet<PermissionInfo>();
        if(pkg == null) {
            return;
        }
        // Get requested permissions
        if (pkg.requestedPermissions != null) {
            ArrayList<String> strList = pkg.requestedPermissions;
            int size = strList.size();
            if (size > 0) {
                extractPerms(strList.toArray(new String[size]), permSet);
            }
        }
        // Get permissions related to  shared user if any
        if(pkg.mSharedUserId != null) {
            int sharedUid;
            try {
                sharedUid = mPm.getUidForSharedUser(pkg.mSharedUserId);
                getAllUsedPermissions(sharedUid, permSet);
            } catch (NameNotFoundException e) {
                Log.w(TAG, "Could'nt retrieve shared user id for:"+pkg.packageName);
            }
        }
        // Retrieve list of permissions
        for(PermissionInfo tmpInfo : permSet) {
            mPermsList.add(tmpInfo);
        }
    }
    
    /**
     * Utility to retrieve a view displaying a single permission.
     */
    public static View getPermissionItemView(Context context,
            CharSequence grpName, CharSequence description, boolean dangerous) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        Drawable icon = context.getResources().getDrawable(dangerous
                ? R.drawable.ic_bullet_key_permission : R.drawable.ic_text_dot);
        return getPermissionItemView(context, inflater, grpName,
                description, dangerous, icon);
    }
    
    private void getAllUsedPermissions(int sharedUid, Set<PermissionInfo> permSet) {
        String sharedPkgList[] = mPm.getPackagesForUid(sharedUid);
        if(sharedPkgList == null || (sharedPkgList.length == 0)) {
            return;
        }
        for(String sharedPkg : sharedPkgList) {
            getPermissionsForPackage(sharedPkg, permSet);
        }
    }
    
    private void getPermissionsForPackage(String packageName, 
            Set<PermissionInfo> permSet) {
        PackageInfo pkgInfo;
        try {
            pkgInfo = mPm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Could'nt retrieve permissions for package:"+packageName);
            return;
        }
        if ((pkgInfo != null) && (pkgInfo.requestedPermissions != null)) {
            extractPerms(pkgInfo.requestedPermissions, permSet);
        }
    }
    
    private void extractPerms(String strList[], Set<PermissionInfo> permSet) {
        if((strList == null) || (strList.length == 0)) {
            return;
        }
        for(String permName:strList) {
            try {
                PermissionInfo tmpPermInfo = mPm.getPermissionInfo(permName, 0);
                if(tmpPermInfo != null) {
                    permSet.add(tmpPermInfo);
                }
            } catch (NameNotFoundException e) {
                Log.i(TAG, "Ignoring unknown permission:"+permName);
            }
        }
    }
    
    public int getPermissionCount() {
        return mPermsList.size();
    }

    public View getPermissionsView() {
        
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
=======
    private HashMap<String, String> mGroupLabelCache;
    private View mNoPermsView;

    public AppSecurityPermissions(Context context) {
        this(context, null);
    }

    public AppSecurityPermissions(Context context, ArrayList<PermissionInfo> permList) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPm = context.getPackageManager();
        mPermsList = permList;
>>>>>>> 54b6cfa... Initial Contribution
        mPermsView = (LinearLayout) mInflater.inflate(R.layout.app_perms_summary, null);
        mShowMore = mPermsView.findViewById(R.id.show_more);
        mShowMoreIcon = (ImageView) mShowMore.findViewById(R.id.show_more_icon);
        mShowMoreText = (TextView) mShowMore.findViewById(R.id.show_more_text);
        mDangerousList = (LinearLayout) mPermsView.findViewById(R.id.dangerous_perms_list);
        mNonDangerousList = (LinearLayout) mPermsView.findViewById(R.id.non_dangerous_perms_list);
        mNoPermsView = mPermsView.findViewById(R.id.no_permissions);

        // Set up the LinearLayout that acts like a list item.
        mShowMore.setClickable(true);
        mShowMore.setOnClickListener(this);
        mShowMore.setFocusable(true);
<<<<<<< HEAD
=======
        mShowMore.setBackgroundResource(android.R.drawable.list_selector_background);
>>>>>>> 54b6cfa... Initial Contribution

        // Pick up from framework resources instead.
        mDefaultGrpLabel = mContext.getString(R.string.default_permission_group);
        mPermFormat = mContext.getString(R.string.permissions_format);
        mNormalIcon = mContext.getResources().getDrawable(R.drawable.ic_text_dot);
        mDangerousIcon = mContext.getResources().getDrawable(R.drawable.ic_bullet_key_permission);
<<<<<<< HEAD
        mShowMaxIcon = mContext.getResources().getDrawable(R.drawable.expander_close_holo_dark);
        mShowMinIcon = mContext.getResources().getDrawable(R.drawable.expander_open_holo_dark);
        
        // Set permissions view
        setPermissions(mPermsList);
=======
        mShowMaxIcon = mContext.getResources().getDrawable(R.drawable.expander_ic_maximized);
        mShowMinIcon = mContext.getResources().getDrawable(R.drawable.expander_ic_minimized);
    }

    public void setSecurityPermissionsView() {
        setPermissions(mPermsList);
    }

    public void setSecurityPermissionsView(Uri pkgURI) {
        final String archiveFilePath = pkgURI.getPath();
        PackageParser packageParser = new PackageParser(archiveFilePath);
        File sourceFile = new File(archiveFilePath);
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        PackageParser.Package pkgInfo = packageParser.parsePackage(sourceFile,
                archiveFilePath, metrics, 0);
        mPermsList = generatePermissionsInfo(pkgInfo.requestedPermissions);
        //For packages that havent been installed we need the application info object
        //to load the labels and other resources.
        setPermissions(mPermsList, pkgInfo.applicationInfo);
    }

    public void setSecurityPermissionsView(PackageInfo pInfo) {
        mPermsList = generatePermissionsInfo(pInfo.requestedPermissions);
        setPermissions(mPermsList);
    }

    public View getPermissionsView() {
>>>>>>> 54b6cfa... Initial Contribution
        return mPermsView;
    }

    /**
     * Canonicalizes the group description before it is displayed to the user.
     *
     * TODO check for internationalization issues remove trailing '.' in str1
     */
    private String canonicalizeGroupDesc(String groupDesc) {
        if ((groupDesc == null) || (groupDesc.length() == 0)) {
            return null;
        }
        // Both str1 and str2 are non-null and are non-zero in size.
        int len = groupDesc.length();
        if(groupDesc.charAt(len-1) == '.') {
            groupDesc = groupDesc.substring(0, len-1);
        }
        return groupDesc;
    }

    /**
     * Utility method that concatenates two strings defined by mPermFormat.
     * a null value is returned if both str1 and str2 are null, if one of the strings
     * is null the other non null value is returned without formatting
     * this is to placate initial error checks
     */
<<<<<<< HEAD
    private String formatPermissions(String groupDesc, CharSequence permDesc) {
        if(groupDesc == null) {
            if(permDesc == null) {
                return null;
            }
            return permDesc.toString();
=======
    private String formatPermissions(String groupDesc, String permDesc) {
        if(groupDesc == null) {
            return permDesc;
>>>>>>> 54b6cfa... Initial Contribution
        }
        groupDesc = canonicalizeGroupDesc(groupDesc);
        if(permDesc == null) {
            return groupDesc;
        }
<<<<<<< HEAD
        // groupDesc and permDesc are non null
        return String.format(mPermFormat, groupDesc, permDesc.toString());
    }

    private CharSequence getGroupLabel(String grpName) {
=======
        return String.format(mPermFormat, groupDesc, permDesc);
    }

    /**
     * Utility method that concatenates two strings defined by mPermFormat.
     */
    private String formatPermissions(String groupDesc, CharSequence permDesc) {
        groupDesc = canonicalizeGroupDesc(groupDesc);
        if(permDesc == null) {
            return groupDesc;
        }
        // Format only if str1 and str2 are not null.
        return formatPermissions(groupDesc, permDesc.toString());
    }

    private ArrayList<PermissionInfo> generatePermissionsInfo(String[] strList) {
        ArrayList<PermissionInfo> permInfoList = new ArrayList<PermissionInfo>();
        if(strList == null) {
            return permInfoList;
        }
        PermissionInfo tmpPermInfo = null;
        for(int i = 0; i < strList.length; i++) {
            try {
                tmpPermInfo = mPm.getPermissionInfo(strList[i], 0);
                permInfoList.add(tmpPermInfo);
            } catch (NameNotFoundException e) {
                Log.i(TAG, "Ignoring unknown permisison:"+strList[i]);
                continue;
            }
        }
        return permInfoList;
    }

    private ArrayList<PermissionInfo> generatePermissionsInfo(ArrayList<String> strList) {
        ArrayList<PermissionInfo> permInfoList = new ArrayList<PermissionInfo>();
        if(strList != null) {
            PermissionInfo tmpPermInfo = null;
            for(String permName:strList) {
                try {
                    tmpPermInfo = mPm.getPermissionInfo(permName, 0);
                    permInfoList.add(tmpPermInfo);
                } catch (NameNotFoundException e) {
                    Log.i(TAG, "Ignoring unknown permisison:"+permName);
                    continue;
                }
            }
        }
        return permInfoList;
    }

    private String getGroupLabel(String grpName) {
>>>>>>> 54b6cfa... Initial Contribution
        if (grpName == null) {
            //return default label
            return mDefaultGrpLabel;
        }
<<<<<<< HEAD
        CharSequence cachedLabel = mGroupLabelCache.get(grpName);
        if (cachedLabel != null) {
            return cachedLabel;
        }
=======
        String cachedLabel = mGroupLabelCache.get(grpName);
        if (cachedLabel != null) {
            return cachedLabel;
        }

>>>>>>> 54b6cfa... Initial Contribution
        PermissionGroupInfo pgi;
        try {
            pgi = mPm.getPermissionGroupInfo(grpName, 0);
        } catch (NameNotFoundException e) {
            Log.i(TAG, "Invalid group name:" + grpName);
            return null;
        }
<<<<<<< HEAD
        CharSequence label = pgi.loadLabel(mPm).toString();
=======
        String label = pgi.loadLabel(mPm).toString();
>>>>>>> 54b6cfa... Initial Contribution
        mGroupLabelCache.put(grpName, label);
        return label;
    }

    /**
     * Utility method that displays permissions from a map containing group name and
     * list of permission descriptions.
     */
    private void displayPermissions(boolean dangerous) {
<<<<<<< HEAD
        Map<String, String> permInfoMap = dangerous ? mDangerousMap : mNormalMap;
=======
        HashMap<String, String> permInfoMap = dangerous ? mDangerousMap : mNormalMap;
>>>>>>> 54b6cfa... Initial Contribution
        LinearLayout permListView = dangerous ? mDangerousList : mNonDangerousList;
        permListView.removeAllViews();

        Set<String> permInfoStrSet = permInfoMap.keySet();
        for (String loopPermGrpInfoStr : permInfoStrSet) {
<<<<<<< HEAD
            CharSequence grpLabel = getGroupLabel(loopPermGrpInfoStr);
=======
            String grpLabel = getGroupLabel(loopPermGrpInfoStr);
>>>>>>> 54b6cfa... Initial Contribution
            //guaranteed that grpLabel wont be null since permissions without groups
            //will belong to the default group
            if(localLOGV) Log.i(TAG, "Adding view group:" + grpLabel + ", desc:"
                    + permInfoMap.get(loopPermGrpInfoStr));
            permListView.addView(getPermissionItemView(grpLabel,
                    permInfoMap.get(loopPermGrpInfoStr), dangerous));
        }
    }

    private void displayNoPermissions() {
        mNoPermsView.setVisibility(View.VISIBLE);
    }

<<<<<<< HEAD
    private View getPermissionItemView(CharSequence grpName, CharSequence permList,
            boolean dangerous) {
        return getPermissionItemView(mContext, mInflater, grpName, permList,
                dangerous, dangerous ? mDangerousIcon : mNormalIcon);
    }

    private static View getPermissionItemView(Context context, LayoutInflater inflater,
            CharSequence grpName, CharSequence permList, boolean dangerous, Drawable icon) {
        View permView = inflater.inflate(R.layout.app_permission_item, null);

        TextView permGrpView = (TextView) permView.findViewById(R.id.permission_group);
        TextView permDescView = (TextView) permView.findViewById(R.id.permission_list);
=======
    private View getPermissionItemView(String grpName, String permList,
            boolean dangerous) {
        View permView = mInflater.inflate(R.layout.app_permission_item, null);
        Drawable icon = dangerous ? mDangerousIcon : mNormalIcon;
        int grpColor = dangerous ? R.color.perms_dangerous_grp_color :
            R.color.perms_normal_grp_color;
        int permColor = dangerous ? R.color.perms_dangerous_perm_color :
            R.color.perms_normal_perm_color;

        TextView permGrpView = (TextView) permView.findViewById(R.id.permission_group);
        TextView permDescView = (TextView) permView.findViewById(R.id.permission_list);
        permGrpView.setTextColor(mContext.getResources().getColor(grpColor));
        permDescView.setTextColor(mContext.getResources().getColor(permColor));
>>>>>>> 54b6cfa... Initial Contribution

        ImageView imgView = (ImageView)permView.findViewById(R.id.perm_icon);
        imgView.setImageDrawable(icon);
        if(grpName != null) {
            permGrpView.setText(grpName);
            permDescView.setText(permList);
        } else {
            permGrpView.setText(permList);
            permDescView.setVisibility(View.GONE);
        }
        return permView;
    }

    private void showPermissions() {

        switch(mCurrentState) {
        case NO_PERMS:
            displayNoPermissions();
            break;

        case DANGEROUS_ONLY:
            displayPermissions(true);
            break;

        case NORMAL_ONLY:
            displayPermissions(false);
            break;

        case BOTH:
            displayPermissions(true);
            if (mExpanded) {
                displayPermissions(false);
                mShowMoreIcon.setImageDrawable(mShowMaxIcon);
                mShowMoreText.setText(R.string.perms_hide);
                mNonDangerousList.setVisibility(View.VISIBLE);
            } else {
                mShowMoreIcon.setImageDrawable(mShowMinIcon);
                mShowMoreText.setText(R.string.perms_show_all);
                mNonDangerousList.setVisibility(View.GONE);
            }
            mShowMore.setVisibility(View.VISIBLE);
            break;
        }
    }
    
    private boolean isDisplayablePermission(PermissionInfo pInfo) {
        if(pInfo.protectionLevel == PermissionInfo.PROTECTION_DANGEROUS ||
                pInfo.protectionLevel == PermissionInfo.PROTECTION_NORMAL) {
            return true;
        }
        return false;
    }
<<<<<<< HEAD
    
    /*
     * Utility method that aggregates all permission descriptions categorized by group
     * Say group1 has perm11, perm12, perm13, the group description will be
     * perm11_Desc, perm12_Desc, perm13_Desc
     */
    private void aggregateGroupDescs(
            Map<String, List<PermissionInfo> > map, Map<String, String> retMap) {
        if(map == null) {
            return;
        }
        if(retMap == null) {
           return;
        }
        Set<String> grpNames = map.keySet();
        Iterator<String> grpNamesIter = grpNames.iterator();
        while(grpNamesIter.hasNext()) {
            String grpDesc = null;
            String grpNameKey = grpNamesIter.next();
            List<PermissionInfo> grpPermsList = map.get(grpNameKey);
            if(grpPermsList == null) {
                continue;
            }
            for(PermissionInfo permInfo: grpPermsList) {
                CharSequence permDesc = permInfo.loadLabel(mPm);
                grpDesc = formatPermissions(grpDesc, permDesc);
            }
            // Insert grpDesc into map
            if(grpDesc != null) {
                if(localLOGV) Log.i(TAG, "Group:"+grpNameKey+" description:"+grpDesc.toString());
                retMap.put(grpNameKey, grpDesc.toString());
            }
        }
    }
    
    private static class PermissionInfoComparator implements Comparator<PermissionInfo> {
        private PackageManager mPm;
        private final Collator sCollator = Collator.getInstance();
        PermissionInfoComparator(PackageManager pm) {
            mPm = pm;
        }
        public final int compare(PermissionInfo a, PermissionInfo b) {
            CharSequence sa = a.loadLabel(mPm);
            CharSequence sb = b.loadLabel(mPm);
            return sCollator.compare(sa, sb);
        }
    }
    
    private void setPermissions(List<PermissionInfo> permList) {
        mGroupLabelCache = new HashMap<String, CharSequence>();
        //add the default label so that uncategorized permissions can go here
        mGroupLabelCache.put(mDefaultGrpName, mDefaultGrpLabel);
        
        // Map containing group names and a list of permissions under that group
        // categorized as dangerous
        mDangerousMap = new HashMap<String, String>();
        // Map containing group names and a list of permissions under that group
        // categorized as normal
        mNormalMap = new HashMap<String, String>();
        
        // Additional structures needed to ensure that permissions are unique under 
        // each group
        Map<String, List<PermissionInfo>> dangerousMap = 
            new HashMap<String,  List<PermissionInfo>>();
        Map<String, List<PermissionInfo> > normalMap = 
            new HashMap<String,  List<PermissionInfo>>();
        PermissionInfoComparator permComparator = new PermissionInfoComparator(mPm);
        
        if (permList != null) {
            // First pass to group permissions
            for (PermissionInfo pInfo : permList) {
                if(localLOGV) Log.i(TAG, "Processing permission:"+pInfo.name);
                if(!isDisplayablePermission(pInfo)) {
                    if(localLOGV) Log.i(TAG, "Permission:"+pInfo.name+" is not displayable");
                    continue;
                }
                Map<String, List<PermissionInfo> > permInfoMap =
                    (pInfo.protectionLevel == PermissionInfo.PROTECTION_DANGEROUS) ?
                            dangerousMap : normalMap;
                String grpName = (pInfo.group == null) ? mDefaultGrpName : pInfo.group;
                if(localLOGV) Log.i(TAG, "Permission:"+pInfo.name+" belongs to group:"+grpName);
                List<PermissionInfo> grpPermsList = permInfoMap.get(grpName);
                if(grpPermsList == null) {
                    grpPermsList = new ArrayList<PermissionInfo>();
                    permInfoMap.put(grpName, grpPermsList);
                    grpPermsList.add(pInfo);
                } else {
                    int idx = Collections.binarySearch(grpPermsList, pInfo, permComparator);
                    if(localLOGV) Log.i(TAG, "idx="+idx+", list.size="+grpPermsList.size());
                    if (idx < 0) {
                        idx = -idx-1;
                        grpPermsList.add(idx, pInfo);
                    }
                }
            }
            // Second pass to actually form the descriptions
            // Look at dangerous permissions first
            aggregateGroupDescs(dangerousMap, mDangerousMap);
            aggregateGroupDescs(normalMap, mNormalMap);
=======

    private void setPermissions(ArrayList<PermissionInfo> permList) {
        setPermissions(permList, null);    
    }
    
    private void setPermissions(ArrayList<PermissionInfo> permList, ApplicationInfo appInfo) {
        mDangerousMap = new HashMap<String, String>();
        mNormalMap = new HashMap<String, String>();
        mGroupLabelCache = new HashMap<String, String>();
        //add the default label so that uncategorized permissions can go here
        mGroupLabelCache.put(mDefaultGrpName, mDefaultGrpLabel);
        if (permList != null) {
            for (PermissionInfo pInfo : permList) {
                if(!isDisplayablePermission(pInfo)) {
                    continue;
                }
                String grpName = (pInfo.group == null) ? mDefaultGrpName : pInfo.group;
                HashMap<String, String> permInfoMap =
                    (pInfo.protectionLevel == PermissionInfo.PROTECTION_DANGEROUS) ?
                            mDangerousMap : mNormalMap;
                // Check to make sure we have a label for the group
                if (getGroupLabel(grpName) == null) {
                    continue;
                }
                CharSequence permDesc = pInfo.loadLabel(mPm);
                String grpDesc = permInfoMap.get(grpName);
                permInfoMap.put(grpName, formatPermissions(grpDesc, permDesc));
                if(localLOGV) Log.i(TAG, pInfo.name + "    :  " + permDesc+"    :    " + grpName);
            }
>>>>>>> 54b6cfa... Initial Contribution
        }

        mCurrentState = State.NO_PERMS;
        if(mDangerousMap.size() > 0) {
            mCurrentState = (mNormalMap.size() > 0) ? State.BOTH : State.DANGEROUS_ONLY;
        } else if(mNormalMap.size() > 0) {
            mCurrentState = State.NORMAL_ONLY;
        }
        if(localLOGV) Log.i(TAG, "mCurrentState=" + mCurrentState);
        showPermissions();
    }

    public void onClick(View v) {
        if(localLOGV) Log.i(TAG, "mExpanded="+mExpanded);
        mExpanded = !mExpanded;
        showPermissions();
    }
}
