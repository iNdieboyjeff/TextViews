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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.lang.reflect.Field;


/**
 * <p>An extension to {@link TextView} that supports custom fonts.</p>
 * <p>
 * <p>Fonts should be located in the assets folder of your project.</p>
 * <p>
 * <p>The font for a text view can be set using the {@link util.android.textviews.R.styleable#FontTextView_android_fontFamily
 * android:fontFamily} property, specifying the file name of the font you wish to use.</p>
 * <p>
 * <p>Typefaces are cached in a {@link LruCache}.</p>
 * <p>
 * <p><b>XML attributes</b></p>
 * <p>
 * <p>See {@link util.android.textviews.R.styleable#FontTextView FontTextView attributes}, {@link
 * android.R.styleable#TextView TextView attributes}, {@link android.R.styleable#View View
 * Attributes}
 *
 * @author Jeff Sutton
 * @version 1.0
 * @attr ref util.android.textviews.R.styleable#FontTextView_android_fontFamily</p>
 */
@RemoteViews.RemoteView
public class FontTextView extends TextView {

    private static final String LOG_TAG = FontTextView.class.getSimpleName();

    // Enum for the "typeface" XML parameter.
    // TODO: How can we get this from the XML instead of hardcoding it here?
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;

    public void setJustify(boolean justify) {
        this.justify = justify;
    }

    private boolean justify = false;
    private boolean autoMax = false;
    private int mLineY;
    private int mViewWidth;

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * <p>Initialises the view using the attributes set in XML from a layout file or a
     * style/theme.</p>
     *
     * @param attrs AttributeSet
     */
    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
        String fontFamily = null;
        final int n = ta.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.FontTextView_android_fontFamily) {
                fontFamily = ta.getString(attr);
            } else if (attr == R.styleable.FontTextView_justify) {
                justify = ta.getBoolean(attr, false);
            } else if (attr == R.styleable.FontTextView_autoMaxLines) {
                autoMax = ta.getBoolean(attr, false);
            }
        }
        final int typefaceIndex = ta.getInt(R.styleable.FontTextView_android_typeface, -1);
        final int styleIndex = ta.getInt(R.styleable.FontTextView_android_textStyle, -1);
        ta.recycle();
        if (!isInEditMode() && fontFamily != null) {
            try {
                setTypeface(TypefaceCache.loadTypeface(getContext(), fontFamily));
            } catch (Exception eek) {
                Log.e(LOG_TAG, "Unable to load and apply typeface: " + fontFamily);
                setTypefaceFromAttrs(fontFamily, typefaceIndex, styleIndex);
            }
        }
    }

    private void setTypefaceFromAttrs(String familyName, int typefaceIndex, int styleIndex) {
        Typeface tf = null;
        if (familyName != null) {
            tf = Typeface.create(familyName, styleIndex);
            if (tf != null) {
                setTypeface(tf);
                return;
            }
        }
        switch (typefaceIndex) {
            case SANS:
                tf = Typeface.SANS_SERIF;
                Log.d(LOG_TAG, "Setting typeface to default sans-serif");
                break;

            case SERIF:
                tf = Typeface.SERIF;
                Log.d(LOG_TAG, "Setting typeface to default serif");
                break;

            case MONOSPACE:
                tf = Typeface.MONOSPACE;
                Log.d(LOG_TAG, "Setting typeface to default monospace");
                break;
        }

        setTypeface(tf, styleIndex);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontTextView(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public boolean isJustify() {
        return justify;
    }

    private float calculateTextHeight(Paint.FontMetrics fm) {
        return fm.bottom - fm.top;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (autoMax) {
            setVisibleMaxLines();
        }
        if (!justify) {
            super.onDraw(canvas);
            return;
        }

        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();

        canvas.translate(compoundPaddingLeft,
                compoundPaddingTop);
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.setTypeface(getTypeface());
        paint.setTextSize(getTextSize());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth() - (compoundPaddingLeft + compoundPaddingRight);

        String text = getText().toString() + "\n";
        mLineY = -4;
        mLineY += getTextSize() + 1;
        Layout layout = new DynamicLayout(text, paint, (int) (mViewWidth - getTextSize()), Layout.Alignment.ALIGN_NORMAL, 0, 0.1f, true);
        int mLines = getMaxLines();
        if (mLines == -1) {
            mLines = Integer.MAX_VALUE;
        }
        for (int i = 0; i < Math.min(mLines, layout.getLineCount()); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            String line;
            float width = DynamicLayout.getDesiredWidth(text, lineStart, lineEnd, paint);
            if (i == mLines - 1 && i < layout.getLineCount() && this.getEllipsize() ==
                    TextUtils
                            .TruncateAt.END) {
                line = text.substring(lineStart, lineEnd - 2)
                        + "\u2026";
                width = DynamicLayout.getDesiredWidth(line, 0, line.length(), getPaint());
                if (needScale(line)) {
                    drawScaledText(canvas, lineStart, line, width);
                } else {
                    canvas.drawText(line, 0, mLineY, paint);
                }
            } else if (i < mLines - 1) {
                line = text.substring(lineStart, lineEnd);
                if (needScale(line) && (i < layout.getLineCount() - 1)) {
                    drawScaledText(canvas, lineStart, line, width);
                } else {
                    canvas.drawText(line, 0, mLineY, paint);
                }
            }
            mLineY += getLineHeight();

        }
    }

    @Override
    public int getMaxLines() {
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getMaxLines();
        } else {
            try {
                Field mMaximumField = this.getClass().getDeclaredField("mMaximum");
                mMaximumField.setAccessible(true);
                return mMaximumField.getInt(this);
            } catch (Exception err) {
                err.printStackTrace();
                return -1;
            }
        }
    }

    public void setVisibleMaxLines() {
        new Runnable() {
            @Override
            public void run() {
                if (getText() != "") {
                    //calculate font height
                    float height = getLineHeight();
                    //calculate the no of lines that will fit in the text box based on this height
                    int heightOfTextView = getHeight();
                    int noLinesInTextView = (int) (heightOfTextView / height);
                    //set max lines to this
                    setMaxLines(noLinesInTextView);
                }
            }
        }.run();

    }

    private boolean needScale(String line) {
        return line.length() != 0 && line.charAt(line.length() - 1) != '\n';
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
        float x = 0;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, getPaint());
            float bw = DynamicLayout.getDesiredWidth(blanks, getPaint());
            x += bw;

            line = line.substring(3);
        }

        float d = (mViewWidth - lineWidth) / (line.length() - 1);
        if (d > 3f) {
            d = 3;
        }
        if (d < -0.0f) {
            line = line.substring(0, (int) (line.length() - (14.7 * Math.abs(d)))) + "\u2026";
            d = 0;
        }
        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = DynamicLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, mLineY, getPaint());
            x += cw + d;
        }
    }


    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

}