package util.android.textviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;


public class ExpandableTextView extends FontTextView {

    private static final String LOG_TAG = ExpandableTextView.class.getSimpleName();

    private static final int DEFAULT_TRIM = 4;
    private static final String EXPAND_TEXT = "See More";
    private static final String ELLIPSIS = "\u2026";
    private int expandTextColour;
    private CharSequence originalText;
    private CharSequence expansionText;
    private boolean trim = true;
    private int lineLength;
    private BaseAdapter adapter;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        this.lineLength = typedArray.getInt(R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM);
        setMaxLines(lineLength);
        ViewTreeObserver vto = this.getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener globalListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addEllipse();
            }
        };
        vto.addOnGlobalLayoutListener(globalListener);
        expandTextColour = typedArray.getColor(R.styleable.ExpandableTextView_expandColor, Color.BLACK);
        expansionText = typedArray.getText(R.styleable.ExpandableTextView_expandText);
        originalText = this.getText();
        typedArray.recycle();
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trim) {
                    setMaxLines(Integer.MAX_VALUE);
                    setText(originalText, false);
                } else {
                    setMaxLines(lineLength);
                    setText(originalText, true);
                }
                requestLayout();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                trim = !trim;
            }
        };
        this.setOnClickListener(clickListener);
    }

    @Override
    public TextUtils.TruncateAt getEllipsize() {
        return super.getEllipsize();
    }

    public void setText(CharSequence text, boolean replaceOriginal) {
        super.setText(text);
        if (replaceOriginal && (originalText == null || !originalText.equals(text))) {
            originalText = text;
            setMaxLines(lineLength);
            trim = true;
            addEllipse();
        }
    }

    private void addEllipse() {
        Layout l = getLayout();
        if (l != null) {
            int lines = l.getLineCount();
            if (lines > 0) {
                if (l.getEllipsisCount(lines - 1) > 0) {
                    //text has shortened
                    SpannableStringBuilder text = (SpannableStringBuilder)
                            Html.fromHtml(getText().subSequence(0,
                                    l.getLineEnd(lines - 1) - (EXPAND_TEXT.length() +
                                            ELLIPSIS.length() + 2)) +
                                    ELLIPSIS + "<font color=\"" + expandTextColour + "\">" +
                                    getExpansionText() + "</font>");
                    setText(text);
                }
            }
        }
    }

    public CharSequence getExpansionText() {
        if (expansionText == null) {
            return EXPAND_TEXT;
        } else {
            return expansionText;
        }
    }

    public void setExpansionText(CharSequence text) {
        this.expansionText = text;
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
