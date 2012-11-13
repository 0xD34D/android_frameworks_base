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

package android.text.style;

<<<<<<< HEAD
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Browser;
import android.text.ParcelableSpan;
import android.text.TextUtils;
import android.view.View;

public class URLSpan extends ClickableSpan implements ParcelableSpan {

    private final String mURL;
=======
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.view.View;

public class URLSpan extends ClickableSpan {

    private String mURL;
>>>>>>> 54b6cfa... Initial Contribution

    public URLSpan(String url) {
        mURL = url;
    }

<<<<<<< HEAD
    public URLSpan(Parcel src) {
        mURL = src.readString();
    }
    
    public int getSpanTypeId() {
        return TextUtils.URL_SPAN;
    }
    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mURL);
    }

=======
>>>>>>> 54b6cfa... Initial Contribution
    public String getURL() {
        return mURL;
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
<<<<<<< HEAD
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        context.startActivity(intent);
=======
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        widget.getContext().startActivity(intent);
>>>>>>> 54b6cfa... Initial Contribution
    }
}
