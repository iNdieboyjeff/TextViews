/*
 *  Copyright (c) 2015-2017 Jeff Sutton
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

package util.android.textviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.CheckedTextView;
import android.widget.RemoteViews;


/**
 * <p>An extension to {@link CheckedTextView} that supports custom fonts.</p>
 * <p>
 * <p>Fonts should be located in the assets folder of your project.</p>
 * <p>
 * <p>The font for a text view can be set using the
 * {@link R.styleable#FontTextView_android_fontFamily android:fontFamily}
 * property, specifying the file name of the font you wish to use.</p>
 * <p>
 * <p>Typefaces are cached in a {@link LruCache}.</p>
 * <p>
 * <p><b>XML attributes</b></p>
 * <p>
 * <p>See {@link R.styleable#FontTextView FontTextView attributes},
 * {@link android.R.styleable#CheckedTextView CheckedTextView attributes}, {@link android.R.styleable#View View Attributes}
 *
 * @author Jeff Sutton
 * @version 1.0
 * @attr ref util.android.textviews.R.styleable#FontTextView_android_fontFamily</p>
 */
@RemoteViews.RemoteView
public class FontCheckedTextView extends android.support.v7.widget.AppCompatCheckedTextView {

    public FontCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * <p>Initialises the view using the attributes set in XML from a layout file or a style/theme.</p>
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
        String fontFamily = null;
        final int n = a.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.FontTextView_android_fontFamily) {
                fontFamily = a.getString(attr);
            }
        }
        a.recycle();
        if (!isInEditMode()) {
            try {
                setTypeface(TypefaceCache.loadTypeface(getContext(), fontFamily));
            } catch (Exception ignored) {}
        }
    }

    public FontCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    public FontCheckedTextView(Context context) {
        super(context);
    }

}