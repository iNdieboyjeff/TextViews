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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import java.util.logging.Level;
import java.util.logging.Logger;


public class TypefaceSpan extends MetricAffectingSpan {

    private Typeface mTypeface;


    public TypefaceSpan(Context context, String typefaceName) {
        this(context, typefaceName, Typeface.NORMAL);
    }

    public TypefaceSpan(Context context, String typefaceName, int style) {
        try {
            mTypeface = Typeface.create(TypefaceCache.loadTypeface(context, typefaceName), style);
        } catch (Exception err) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.OFF, err.getMessage(), err);
            mTypeface = setTypefaceFromAttrs(typefaceName, 0, style);
        }
    }

    private Typeface setTypefaceFromAttrs(String familyName, int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        if (familyName != null) {
            tf = Typeface.create(familyName, styleIndex);
        }

        return tf;
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

}