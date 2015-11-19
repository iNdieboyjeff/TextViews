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

package com.example.textviewsample;

import android.graphics.Color;
import android.graphics.Typeface;
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

    private TextView sourceCodeText;

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
        TypefaceSpan codeSpan = new TypefaceSpan(getActivity(), "SourceCodePro-Regular");
        TypefaceSpan codeSpan2 = new TypefaceSpan(getActivity(), "SourceCodePro-Bold", Typeface.BOLD);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.MAGENTA);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.BLUE);

        SpannableString title = new SpannableString(getString(R.string.code_sample));
        title.setSpan(codeSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(codeSpan2, 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(colorSpan, 4, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setSpan(colorSpan2, 19, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        sourceCodeText.setText(title);
    }

}
