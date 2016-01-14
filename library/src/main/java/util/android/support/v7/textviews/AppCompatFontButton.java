/*
 *  Copyright (c) 2015 Jeff Sutton
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package util.android.support.v7.textviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import util.android.textviews.R;
import util.android.textviews.TypefaceCache;

/**
 * <p>A Button that allows you to set a custom Typeface</p>
 *
 * @author Jeff Sutton
 * @since 1.2.4
 */
public class AppCompatFontButton extends AppCompatButton {

    private static final String LOG_TAG = AppCompatFontButton.class.getSimpleName();

    public AppCompatFontButton(Context context) {
        super(context);
    }

    public AppCompatFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AppCompatFontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * <p>Initialises the view using the attributes set in XML from a layout file or a style/theme.</p>
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontButton);
        String fontFamily = null;
        final int n = a.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.FontButton_android_fontFamily) {
                fontFamily = a.getString(attr);
            }
        }
        a.recycle();
        if (!isInEditMode()) {
            try {
                setTypeface(TypefaceCache.loadTypeface(getContext(), fontFamily));
            } catch (Exception ignored) {
            }
        }
    }

}
