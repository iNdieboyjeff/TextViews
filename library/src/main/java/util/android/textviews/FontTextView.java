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

package util.android.textviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


/**
 * <p>An extension to {@link TextView} that supports custom fonts.</p>
 *
 * <p>Fonts should be located in the assets folder of your project.</p>
 *
 * <p>The font for a text view can be set using the android:fontFamily property, specifying the
 * file name of the font you wish to use.</p>
 *
 * <p>Typefaces are cached in a {@link LruCache}.</p>
 *
 * @author Jeff Sutton
 * @version 1.0
 */
public class FontTextView extends TextView {

    private static final String LOGTAG = FontTextView.class.getSimpleName();
    private static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextView(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
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
                Typeface tf = sTypefaceCache.get(fontFamily);
                if(tf == null) {
                    Log.d(LOGTAG, "loading in font: " + fontFamily);
                    tf = Typeface.createFromAsset(getContext().getAssets(), getAssetPath(fontFamily));
                    sTypefaceCache.put(fontFamily, tf);
                }
                setTypeface(tf);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * <p>Look to see if an font exists in the assets folder.</p>
     *
     * <p>If a file cannot be found, this method will also check if a file with .tff or .otf
     * appended to the name exists.  This allows you to specify just the font name in your code,
     * rather than the full filename.</p>
     *
     * @param fontName The name of the font file you want to locate
     * @return String representing the actual name of the font file
     */
    private String getAssetPath(String fontName) {
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;
        try {
           inputStream = assetManager.open(fontName);
        } catch (IOException ex) {
            try {
                inputStream = assetManager.open(fontName + ".ttf");
                fontName += ".ttf";
            } catch (IOException e) {
                try {
                    inputStream = assetManager.open(fontName + ".otf");
                    fontName += ".otf";
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fontName;
    }
}