package util.android.textviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;

/**
 * <p>A {@link FontTextView} that can start off displaying a snippet of text with a "Read More"
 * label at the end.  When clicked the View expands to show it's complete text.</p>
 *
 * <p>The colour and text of the "Read More" label is customisable.</p>
 *
 * <p>The number of lines displayed when in the contracted state is also customisable.</p>
 *
 * <p>This view is able to save and restore it's state following things like screen rotation.</p>
 *
 * @author Jeff Sutton
 */
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
                trim = !trim;
                setContracted(trim);
            }
        };
        this.setOnClickListener(clickListener);
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

    public void setContracted(boolean state) {
        Log.d(LOG_TAG, "Is view contracted: " + state);
        this.trim = state;
        if (!trim) {
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

    }

    public CharSequence getExpansionText() {
        if (expansionText == null) {
            return EXPAND_TEXT;
        } else {
            return expansionText;
        }
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

    public void setExpansionText(CharSequence text) {
        this.expansionText = text;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void setExpandTextColour(String color) {
        expandTextColour = Color.parseColor(color);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.trim = this.trim;
        ss.lines = this.lineLength;
        ss.expandColor = this.expandTextColour;
        ss.expandText = this.expansionText != null ? this.expansionText.toString() : null;
        ss.text = this.originalText != null ? this.originalText.toString() : null;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setExpandTextColour(ss.expandColor);
        setExpansionText(ss.expandText);
        setText(ss.text, true);
        setTrim(ss.lines);
        setContracted(ss.trim);
    }

    public void setExpandTextColour(int color) {
        expandTextColour = color;
    }

    public void setTrim(int trim) {
        lineLength = trim;
        setMaxLines(trim);
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        int expandColor;
        String expandText;
        int lines;
        boolean trim;
        String text;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.expandColor = in.readInt();
            this.lines = in.readInt();
            this.trim = in.readByte() != 0;
            this.expandText = in.readString();
            this.text = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.expandColor);
            out.writeInt(this.lines);
            out.writeByte((byte) (this.trim ? 1 : 0));
            out.writeString(this.expandText);
            out.writeString(this.text);
        }
    }
}
