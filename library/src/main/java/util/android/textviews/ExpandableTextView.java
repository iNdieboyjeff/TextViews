package util.android.textviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;


/**
 * Created by michael on 16/12/2014.
 */
public class ExpandableTextView extends FontTextView {

    private static final int DEFAULT_TRIM = 4;
    private static final String EXPAND_TEXT = "See More";
    private static final String ELLIPSIS = "\u2026";

    private String originalText;
    private boolean trim = true;
    private int lineLength;
    private ViewTreeObserver vto;
    private BaseAdapter adapter;
    public int expandTextColour;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.lineLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM);
        typedArray.recycle();
        setMaxLines(lineLength);

        vto = this.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(globalListener);

        expandTextColour = typedArray.getColor(R.styleable.ExpandableTextView_expandColor, Color.BLACK);

        this.setOnClickListener(clickListener);
    }

    @Override
    public TextUtils.TruncateAt getEllipsize() {
        return super.getEllipsize();
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            addEllipse();
        }
    };

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (trim) {
                setMaxLines(Integer.MAX_VALUE);
                setText(originalText);
            } else {
                setMaxLines(lineLength);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            trim = !trim;
        }
    };

    private void addEllipse() {
        Layout l = getLayout();
        if (l != null) {
            int lines = l.getLineCount();
            if (lines > 0) {
                if (l.getEllipsisCount(lines - 1) > 0) {
                    //text has shortened
                    Spanned text = Html.fromHtml(getText().subSequence(0, l.getLineEnd(lines - 1) - (EXPAND_TEXT.length() + ELLIPSIS.length() + 2)) + ELLIPSIS + "<font color=\"" + expandTextColour + "\">" + EXPAND_TEXT + "</font>");
                    setText(text);
                }
            }
        }
    }

    public void setText(String text, boolean replaceOriginal) {
        super.setText(text);
        if (replaceOriginal && (originalText == null || !originalText.equals(text))) {
            originalText = text;
            setMaxLines(lineLength);
            trim = true;
            addEllipse();
        }
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void setExpandTextColour(String color) {
        expandTextColour = Color.parseColor(color);
    }

    public void setExpandTextColour(int color) {
        expandTextColour = color;
    }

    public void setTrim(int trim) {
        lineLength = trim;
        setMaxLines(trim);
    }
}
