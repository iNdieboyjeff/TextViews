/*
 *  Copyright (c) 2015-2018 Jeff Sutton
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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
 * <p>See {@link util.android.textviews.R.styleable#FontTextView FontTextView attributes}
 *
 * @author Jeff Sutton
 * @version 2.1
 * @attr ref util.android.textviews.R.styleable#FontTextView_android_fontFamily</p>
 */
@RemoteViews.RemoteView
public class FontTextView extends android.support.v7.widget.AppCompatTextView {

    private static final String LOG_TAG = FontTextView.class.getSimpleName();

    private static final CharSequence ELLIPSIS = "\u2026";


    // Enum for the "typeface" XML parameter.
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;
    private boolean justify = false;
    private boolean autoMax = false;
    private boolean mAllCaps = false;
    private boolean mWordEllipsize = false;
    private int mFirstLineTextHeight = 0;
    private Rect mLineBounds = new Rect();

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
            } else if (attr == R.styleable.FontTextView_android_textAllCaps) {
                mAllCaps = ta.getBoolean(R.styleable.FontTextView_android_textAllCaps, false);
            } else if (attr == R.styleable.FontTextView_wordEllipsize) {
                mWordEllipsize = ta.getBoolean(R.styleable.FontTextView_wordEllipsize, false);
            }
        }
        final int typefaceIndex = ta.getInt(R.styleable.FontTextView_android_typeface, -1);
        final int styleIndex = ta.getInt(R.styleable.FontTextView_android_textStyle, -1);

        ta.recycle();
        if (!isInEditMode() && fontFamily != null) {
            try {
                setTypeface(TypefaceCache.loadTypeface(getContext(), fontFamily));
            } catch (Exception eek) {
                setTypefaceFromAttrs(fontFamily, typefaceIndex, styleIndex);
            }
        }
    }

    private void setTypefaceFromAttrs(String familyName, int typefaceIndex, int styleIndex) {
        Typeface tf;
        if (familyName != null) {
            tf = Typeface.create(familyName, styleIndex);
            if (tf != null) {
                setTypeface(tf);
                return;
            }
        }
        switch (typefaceIndex) {

            case SERIF:
                tf = Typeface.SERIF;
                Log.d(LOG_TAG, "Setting typeface to default serif");
                break;

            case MONOSPACE:
                tf = Typeface.MONOSPACE;
                Log.d(LOG_TAG, "Setting typeface to default monospace");
                break;

            case SANS:
            default:
                tf = Typeface.SANS_SERIF;
                Log.d(LOG_TAG, "Setting typeface to default sans-serif");
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

    /**
     * Should the text be justified?
     *
     * @return boolean Justified text on or off
     */
    public boolean isJustify() {
        return justify;
    }

    public void setJustify(boolean justify) {
        this.justify = justify;
    }

    @Override
    public void setAllCaps(boolean allCaps) {
        mAllCaps = allCaps;
        super.setAllCaps(allCaps);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void switchText(int resId) {
        switchText(getResources().getString(resId));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void switchText(final String newText) {
        animate().alpha(0).setDuration(150).withEndAction(new Runnable() {
            @Override public void run() {
                setText(newText);
                animate().alpha(1).setDuration(150).start();
            }
        }).start();
    }

    @Override
    public boolean onPreDraw() {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.setTypeface(getTypeface());
        paint.setTextSize(getTextSize());
        paint.drawableState = getDrawableState();

        return super.onPreDraw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (autoMax) {
            setVisibleMaxLines();
        }
        if (mWordEllipsize && !justify) {
            onDrawRagged(canvas);
        } else if (justify) {
            onDrawJustified(canvas);
        } else {
            super.onDraw(canvas);
        }
    }


    private void onDrawJustified(Canvas canvas) {
        // Manipulations to mTextPaint found in super.onDraw()...
        getPaint().setColor(getCurrentTextColor());
        getPaint().drawableState = getDrawableState();

        // The actual String that contains all the words we will draw on screen
        String fullText = mAllCaps ? getText().toString().toUpperCase() : getText().toString();

        float lineSpacing = getLineHeight();
        float drawableWidth = getDrawableWidth();

        // Variables we need to traverse our fullText and build our lines
        int lineNum = 1;
        int lineStartIndex = 0;
        int lastWordEnd = 0;
        int currWordEnd = 0;

        if (fullText.indexOf(' ', 0) == -1) flushWord(canvas, getPaddingTop() + lineSpacing, fullText);
        else {
            while (currWordEnd >= 0) {
                lastWordEnd = currWordEnd + 1;
                currWordEnd = fullText.indexOf(' ', lastWordEnd);

                if (currWordEnd != -1) {
                    getPaint().getTextBounds(fullText, lineStartIndex, currWordEnd, mLineBounds);

                    if (mLineBounds.width() >= drawableWidth) {
                        flushLine(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd));
                        lineStartIndex = lastWordEnd;
                        lineNum++;
                    }

                } else {
                    getPaint().getTextBounds(fullText, lineStartIndex, fullText.length(), mLineBounds);

                    if (mLineBounds.width() >= drawableWidth) {
                        flushLine(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd));
                        rawFlushLine(canvas, ++lineNum, fullText.substring(lastWordEnd));
                    } else {
                        if (lineNum == 1) {
                            rawFlushLine(canvas, lineNum, fullText);
                        } else {
                            rawFlushLine(canvas, lineNum, fullText.substring(lineStartIndex));
                        }
                    }
                }

            }
        }
    }

    private void onDrawRagged(Canvas canvas) {
        // Manipulations to mTextPaint found in super.onDraw()...
        getPaint().setColor(getCurrentTextColor());
        getPaint().drawableState = getDrawableState();

        // The actual String that contains all the words we will draw on screen
        String fullText = mAllCaps ? getText().toString().toUpperCase() : getText().toString();

        float lineSpacing = getLineHeight();
        float drawableWidth = getDrawableWidth();

        // Variables we need to traverse our fullText and build our lines
        int lineNum = 1;
        int lineStartIndex = 0;
        int lastWordEnd = 0;
        int currWordEnd = 0;

        if (fullText.indexOf(' ', 0) == -1) flushWordRagged(canvas, getPaddingTop() + lineSpacing, fullText);
        else {
            while (currWordEnd >= 0) {
                lastWordEnd = currWordEnd + 1;
                currWordEnd = fullText.indexOf(' ', lastWordEnd);

                if (currWordEnd != -1) {
                    getPaint().getTextBounds(fullText, lineStartIndex, currWordEnd, mLineBounds);

                    if (mLineBounds.width() >= drawableWidth) {
                        flushLineRagged(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd));
                        lineStartIndex = lastWordEnd;
                        lineNum++;
                    }

                } else {
                    getPaint().getTextBounds(fullText, lineStartIndex, fullText.length(), mLineBounds);

                    if (mLineBounds.width() >= drawableWidth) {
                        flushLineRagged(canvas, lineNum, fullText.substring(lineStartIndex, lastWordEnd));
                        rawFlushLine(canvas, ++lineNum, fullText.substring(lastWordEnd));
                    } else {
                        if (lineNum == 1) {
                            rawFlushLine(canvas, lineNum, fullText);
                        } else {
                            rawFlushLine(canvas, lineNum, fullText.substring(lineStartIndex));
                        }
                    }
                }

            }
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
                return -1;
            }
        }
    }

    /**
     * Based on the size of the TextView calculate how many lines can be displayed and set
     * this as the maxLines for the view.
     */
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

    private float getDrawableWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private void setFirstLineTextHeight(String firstLine) {
        getPaint().getTextBounds(firstLine, 0, firstLine.length(), mLineBounds);
        mFirstLineTextHeight = mLineBounds.height();
    }

    private void rawFlushLine(Canvas canvas, int lineNum, String line) {
        if (lineNum == 1) setFirstLineTextHeight(line);

        String[] words = line.split("\\s+");

        if (getEllipsize() == TextUtils.TruncateAt.END && lineNum == getMaxLines()) {
            float unmodifiedWidth = getPaint().measureText(line + ELLIPSIS);
            if (getDrawableWidth() < unmodifiedWidth) {
                line = getEllipsizedLine(line);
                words = line.split("\\s+");
            } else {
                words[words.length - 1] = ELLIPSIS.toString();
            }
        }

        StringBuilder lineBuilder = new StringBuilder();

        for (String word : words) {
            lineBuilder.append(word);
        }

        line = lineBuilder.toString();

        float yLine = getPaddingTop() + mFirstLineTextHeight + (lineNum - 1) * getLineHeight();
        canvas.drawText(line, getPaddingLeft(), yLine, getPaint());
    }

    private void flushLine(Canvas canvas, int lineNum, String line) {
        if (lineNum == 1) setFirstLineTextHeight(line);

        float yLine = getPaddingTop() + mFirstLineTextHeight + (lineNum - 1) * getLineHeight();

        String[] words = line.split("\\s+");

        if (getEllipsize() == TextUtils.TruncateAt.END && lineNum == getMaxLines()) {
            float unmodifiedWidth = getPaint().measureText(line + ELLIPSIS);
            if (getDrawableWidth() > unmodifiedWidth) {
                line = getEllipsizedLine(line);
                words = line.split("\\s+");
            } else {
                words[words.length - 1] = ELLIPSIS.toString();
            }
        }

        StringBuilder lineBuilder = new StringBuilder();

        for (String word : words) {
            lineBuilder.append(word);
        }

        float xStart = getPaddingLeft();
        float wordWidth = getPaint().measureText(lineBuilder.toString());
        float spacingWidth = (getDrawableWidth() - wordWidth) / (words.length - 1);

        for (String word : words) {
            canvas.drawText(word, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word) + spacingWidth;
        }
    }

    private void flushLineRagged(Canvas canvas, int lineNum, String line) {
        if (lineNum == 1) setFirstLineTextHeight(line);

        float yLine = getPaddingTop() + mFirstLineTextHeight + (lineNum - 1) * getLineHeight();

        String[] words = line.split("\\s+");

        if (getEllipsize() == TextUtils.TruncateAt.END && lineNum == getMaxLines()) {
            float unmodifiedWidth = getPaint().measureText(line + ELLIPSIS);
            if (getDrawableWidth() > unmodifiedWidth) {
                line = getEllipsizedLine(line);
                words = line.split("\\s+");
            } else {
                words[words.length - 1] = ELLIPSIS.toString();
            }
        }

        StringBuilder lineBuilder = new StringBuilder();

        for (String word : words) {
            lineBuilder.append(word);
        }

        float xStart = getPaddingLeft();
        float spacingWidth = getPaint().measureText(" ");

        for (String word : words) {
            canvas.drawText(word, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word) + spacingWidth;
        }
    }

    private void flushWord(Canvas canvas, float yLine, String word) {
        float xStart = getPaddingLeft();
        float wordWidth = getPaint().measureText(word);
        float spacingWidth = (getDrawableWidth() - wordWidth) / (word.length() - 1);

        for (int i = 0; i < word.length(); i++) {
            canvas.drawText(word, i, i + 1, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word, i, i + 1) + spacingWidth;
        }
    }

    private void flushWordRagged(Canvas canvas, float yLine, String word) {
        float xStart = getPaddingLeft();
        float spacingWidth = getPaint().measureText(" ");

        for (int i = 0; i < word.length(); i++) {
            canvas.drawText(word, i, i + 1, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word, i, i + 1) + spacingWidth;
        }
    }

    private String getEllipsizedLine(String line) {
        line = line.trim();
        if (line.endsWith(".")) {
            line = line.substring(0, line.length()-1) + ELLIPSIS;
        }
        return line;
    }

}