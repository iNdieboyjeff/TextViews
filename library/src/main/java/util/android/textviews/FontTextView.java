package util.android.textviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.LruCache;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.customfont);
        String fontFamily = null;
        final int n = a.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.customfont_android_fontFamily) {
                fontFamily = a.getString(attr);
            }
            a.recycle();
        }
        if (!isInEditMode()) {
            try {
                Typeface tf = sTypefaceCache.get(fontFamily);
                if(tf == null) {
                    Log.d(LOGTAG, "loading in font: " + fontFamily);
                    tf = Typeface.createFromAsset(getContext().getAssets(), fontFamily);
                    sTypefaceCache.put(fontFamily, tf);
                }
                setTypeface(tf);
            } catch (Exception ignored) {
            }
        }
    }
}