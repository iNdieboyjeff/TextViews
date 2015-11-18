package com.example.textviewsample;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import util.android.textviews.TypefaceSpan;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    TextView sourceCodeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sourceCodeText = (TextView) view.findViewById(R.id.textView7);

    }

    public MainActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        styleSourceCode();
    }

    private void styleSourceCode() {
        TypefaceSpan codeSpan = new TypefaceSpan(getActivity(), "SourceCodePro-Regular.ttf");
        TypefaceSpan codeSpan2 = new TypefaceSpan(getActivity(), "SourceCodePro-Bold.ttf");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.MAGENTA);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.GREEN);

        SpannableString title = new SpannableString("if (hello_world == true) {}");
        title.setSpan(codeSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(colorSpan, 4, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        title.setSpan(colorSpan2, 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(codeSpan2, 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        sourceCodeText.setText(title);
    }

}
