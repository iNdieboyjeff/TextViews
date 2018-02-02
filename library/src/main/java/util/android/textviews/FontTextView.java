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
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>An extension to {@link android.support.v7.widget.AppCompatTextView} that supports custom fonts.</p>
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

    public static final int LINK_TYPE_NONE = 0;
    public static final int LINK_TYPE_WEB = 1;
    public static final int LINK_TYPE_HASHTAG = 1<<1;
    public static final int LINK_TYPE_SCREENNAME = 1<<2;
    public static final int LINK_TYPE_EMAIL = 1<<3;
    public static final int LINK_TYPE_ALL = LINK_TYPE_WEB | LINK_TYPE_HASHTAG | LINK_TYPE_SCREENNAME | LINK_TYPE_EMAIL;

    private static final String LOG_TAG = FontTextView.class.getSimpleName();
    private static final boolean DEBUG = true;

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

    private Pattern hyperlinkPattern = Patterns.WEB_URL;
    private Pattern emailPattern = Patterns.EMAIL_ADDRESS;
    private Pattern hashtagPattern = Pattern.compile("(#\\w+)");
    private Pattern screenNamePattern = Pattern.compile("(@\\w+)");

    private int linkType = LINK_TYPE_NONE;
    private int linkTextColor = Color.BLUE;
    private boolean linkUnderline = false;
    private boolean hitLink = false;

    private List<Hyperlink> links = new ArrayList<>();
    private OnLinkClickListener listener;

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
            } else if (attr == R.styleable.FontTextView_linkTextColor) {
                setLinkColor(ta.getColor(attr, linkTextColor));
            } else if (attr == R.styleable.FontTextView_linkTextUnderline) {
                setLinkUnderline(ta.getBoolean(attr, false));
            } else if (attr == R.styleable.FontTextView_linkType) {
                setLinkType(ta.getInt(attr, LINK_TYPE_NONE));
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

        if (linkType != LINK_TYPE_NONE) {
            setLinkText(getText().toString());
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
                LOG("Setting typeface to default serif");
                break;

            case MONOSPACE:
                tf = Typeface.MONOSPACE;
                LOG("Setting typeface to default monospace");
                break;

            case SANS:
            default:
                tf = Typeface.SANS_SERIF;
                LOG("Setting typeface to default sans-serif");
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
     * Convenience log method used during debugging
     *
     * @param message
     */
    private static void LOG(String message) {
        if (DEBUG || BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message);
        }
    }

    public void setOnLinkClickListener(OnLinkClickListener listener) {
        this.listener = listener;
    }

    public boolean isDetectingLinks() {
        return linkType != LINK_TYPE_NONE;
    }

    public void setLinkType(int type) {
        linkType = type;
    }

    public void setLinkColor(int color) {
        linkTextColor = color;
    }

    public void setLinkUnderline(boolean underline) {
        linkUnderline = underline;
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

    /**
     * Paint the justified text onto the canvas
     *
     * @param canvas
     */
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

    /**
     * Paint non-justified text on to the canvas.  If wordEllipsize is set, and Ellipsize is set to
     * END then this will ellisize the text on word boundaries, rather than in the middle of words
     *
     * @param canvas
     */
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

    @Override
    public void setAllCaps(boolean allCaps) {
        mAllCaps = allCaps;
        super.setAllCaps(allCaps);
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
            lineBuilder.append(word + " ");
        }

        line = lineBuilder.toString();

        float yLine = getPaddingTop() + mFirstLineTextHeight + (lineNum - 1) * getLineHeight();
        canvas.drawText(line, getPaddingLeft(), yLine, getPaint());
    }

    private void flushLine(Canvas canvas, int lineNum, String line) {
        if (lineNum == 1) setFirstLineTextHeight(line);

        if (lineNum > getMaxLines()) {
            return;
        }
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

    /**
     * Flush a line to the canvas.  Word spacing is uniform so that the line is NOT justified.
     * If needed an ellipsis is added.  This is done on word boundaries rather than in the middle
     * of words.
     *
     * @param canvas
     * @param lineNum
     * @param line
     */
    private void flushLineRagged(Canvas canvas, int lineNum, String line) {
        if (lineNum == 1) setFirstLineTextHeight(line);

        if (lineNum > getMaxLines()) {
            return;
        }

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

    /**
     * Flush a word to the canvas.  Here the space between words is calculated so that words are
     * evenly spaced and the whole line becomes justified.
     *
     * @param canvas
     * @param yLine
     * @param word
     */
    private void flushWord(Canvas canvas, float yLine, String word) {
        float xStart = getPaddingLeft();
        float wordWidth = getPaint().measureText(word);
        float spacingWidth = (getDrawableWidth() - wordWidth) / (word.length() - 1);

        for (int i = 0; i < word.length(); i++) {
            canvas.drawText(word, i, i + 1, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word, i, i + 1) + spacingWidth;
        }
    }

    /**
     * Flush a word to the canvas.  Here the space between words is simply the space needed to
     * contain a space.
     *
     * @param canvas
     * @param yLine
     * @param word
     */
    private void flushWordRagged(Canvas canvas, float yLine, String word) {
        float xStart = getPaddingLeft();
        float spacingWidth = getPaint().measureText(" ");

        for (int i = 0; i < word.length(); i++) {
            canvas.drawText(word, i, i + 1, xStart, yLine, getPaint());
            xStart += getPaint().measureText(word, i, i + 1) + spacingWidth;
        }
    }

    /**
     * Add the ellipsis to a line by trimming trailing spaces, and removing any full-stop from
     * the end of the line.
     *
     * @param line
     * @return
     */
    private String getEllipsizedLine(String line) {
        line = line.trim();
        if (line.endsWith(".")) {
            line = line.substring(0, line.length()-1) + ELLIPSIS;
        } else {
            line += ELLIPSIS;
        }
        return line;
    }

    public void setLinkText(String text) {
        LOG("setLinkText()");
        links.clear();

        if (containsLinkType(LINK_TYPE_WEB)) {
            gatherLinks(text, hyperlinkPattern, LINK_TYPE_WEB);
        }
        if (containsLinkType(LINK_TYPE_HASHTAG)) {
            gatherLinks(text, hashtagPattern, LINK_TYPE_HASHTAG);
        }
        if (containsLinkType(LINK_TYPE_SCREENNAME)) {
            gatherLinks(text, screenNamePattern, LINK_TYPE_SCREENNAME);
        }
        if (containsLinkType(LINK_TYPE_EMAIL)) {
            gatherLinks(text, emailPattern, LINK_TYPE_EMAIL);
        }

        SpannableString linkableText = new SpannableString(text);

        if (!links.isEmpty()) {
            for (Hyperlink linkSpec : links) {
                linkableText.setSpan(linkSpec.span, linkSpec.start, linkSpec.end, 0);
            }

            MovementMethod m = getMovementMethod();
            if ((m == null) || !(m instanceof LinkMovementMethod)) {
                if (getLinksClickable()) {
                    setMovementMethod(LocalLinkMovementMethod.getInstance());
                }
            }
        }

        setText(linkableText);
    }

    private boolean containsLinkType(int type) {
        boolean containsType = (linkType & type) == type;
        LOG("Check link type " + type + ": " + containsType);
        return containsType;
    }

    private void gatherLinks(String s, Pattern pattern, int type) {
        Matcher m = pattern.matcher(s);

        while (m.find()) {
            int start = m.start();
            int end = m.end();

            Hyperlink link = new Hyperlink();

            link.type = type;
            link.textSpan = s.subSequence(start, end);
            link.color = linkTextColor;
            link.underline = linkUnderline;
            link.span = new LinkSpan(link.textSpan.toString(), link.type, link.color, link.underline);
            link.start = start;
            link.end = end;

            links.add(link);
        }
    }

    @Override
    public boolean hasFocusable() {
        return !isDetectingLinks() && super.hasFocusable();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hitLink = false;
        boolean res = super.onTouchEvent(event);

        if (isDetectingLinks())
            return hitLink;

        return res;
    }

    public interface OnLinkClickListener {
        void onLinkClick(View textView, String link, int type);
    }

    private class Hyperlink {
        int type;
        CharSequence textSpan;
        LinkSpan span;
        int start;
        int end;
        int color;
        boolean underline;
    }

    private class LinkSpan extends ClickableSpan {

        private String mLinkText;
        private int mType;
        private int mColor;
        private boolean mUnderline;

        public LinkSpan(String linkText, int type, int color, boolean underline) {
            mLinkText = linkText;
            mType = type;
            mColor = color;
            mUnderline = underline;
        }

        @Override
        public void onClick(View textView) {
            if (listener != null) {
                listener.onLinkClick(textView, mLinkText, mType);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.bgColor = Color.TRANSPARENT;
            ds.setColor(mColor);
            ds.setUnderlineText(mUnderline);
        }
    }

    private static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;

        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LocalLinkMovementMethod();

            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[link.length - 1].onClick(widget);
                    }

                    if (widget instanceof FontTextView) {
                        ((FontTextView) widget).hitLink = true;
                    }
                    return true;
                }
                else {
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }
            return Touch.onTouchEvent(widget, buffer, event);
        }
    }
}