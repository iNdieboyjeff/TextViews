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

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.LruCache;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import java.io.IOException;
import java.io.InputStream;


public class TypefaceSpan extends MetricAffectingSpan {
    /** An <code>LruCache</code> for previously loaded typefaces. */
    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<String, Typeface>(12);

    private Typeface mTypeface;


    public TypefaceSpan(Context context, String typefaceName) {
        this(context, typefaceName, Typeface.NORMAL);
    }

    public TypefaceSpan(Context context, String typefaceName, int style) {
        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext()
                    .getAssets(), String.format("%s", getAssetPath(context.getApplicationContext(), typefaceName)));

            mTypeface = Typeface.create(mTypeface, style);

            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
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
    private String getAssetPath(Context context, String fontName) {
        AssetManager assetManager = context.getResources().getAssets();
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