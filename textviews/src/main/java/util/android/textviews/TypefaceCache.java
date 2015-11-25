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
import android.graphics.Typeface;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Maintain one typeface cache for all widgets.</p>
 *
 * @author Jeff Sutton
 * @since 1.2.1
 */
public class TypefaceCache {

    private static final String LOGTAG = TypefaceCache.class.getSimpleName();
    private static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);

    public static Typeface loadTypeface(Context context, String fontFamily) {
        Typeface tf = sTypefaceCache.get(fontFamily);
        if(tf == null) {
            Log.d(LOGTAG, "loading font: " + fontFamily);
            tf = Typeface.createFromAsset(context.getAssets(), getAssetPath(context, fontFamily));
            if (fontFamily.toLowerCase().endsWith("-bold")) {
                tf = Typeface.create(tf, Typeface.BOLD);
            } else if (fontFamily.toLowerCase().endsWith("-regular")) {
                tf = Typeface.create(tf, Typeface.NORMAL);
            } else if (fontFamily.toLowerCase().endsWith("-italic")) {
                tf = Typeface.create(tf, Typeface.ITALIC);
            } else if (fontFamily.toLowerCase().endsWith("-bolditalic")) {
                tf = Typeface.create(tf, Typeface.BOLD_ITALIC);
            }
            sTypefaceCache.put(fontFamily, tf);
        }
        return tf;
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
    private static String getAssetPath(Context context, String fontName) {
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
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fontName;
    }
}
