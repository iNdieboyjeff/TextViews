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
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import util.android.textviews.R;
import util.android.textviews.TypefaceCache;

/**
 * <p>An extension to {@link AppCompatTextView} that supports custom fonts.</p>
 *
 * <p>Fonts should be located in the assets folder of your project.</p>
 *
 * <p>The font for a text view can be set using the android:fontFamily property, specifying the
 * file name of the font you wish to use.</p>
 *
 * <p>Typefaces are cached in a {@link TypefaceCache}.</p>
 *
 * <p><b>XML attributes</b></p>
 *
 * <p>See {@link util.android.textviews.R.styleable#FontTextView FontTextView attributes},
 * {@link android.support.v7.appcompat.R.styleable.AppCompatTextView AppCompatTextView attributes},
 * {@link android.R.styleable#View View attributes}
 *
 * @attr ref util.android.textviews.R.styleable#FontTextView_android_fontFamily</p>
 *
 * @author Jeff Sutton
 * @since 1.2.4
 */
public class AppCompatFontTextView extends AppCompatTextView {

    private static final String LOG_TAG = AppCompatFontTextView.class.getSimpleName();

    public AppCompatFontTextView(Context context) {
        super(context);
    }

    public AppCompatFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AppCompatFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * <p>Initialises the view using the attributes set in XML from a layout file or a style/theme.</p>
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AppCompatFontTextView);
        String fontFamily = null;
        final int n = a.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.AppCompatFontTextView_android_fontFamily) {
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
