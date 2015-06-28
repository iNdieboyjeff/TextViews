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


public class FontTextView extends TextView {

    private static final String LOGTAG = FontTextView.class.getSimpleName();
    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<String, Typeface>(12);

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

    public void init(Context context, AttributeSet attrs) {
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
            } catch (Exception e) {
            }
        }
    }
}