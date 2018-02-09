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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.device.yearclass.DeviceInfo;
import com.facebook.device.yearclass.YearClass;

import java.util.Locale;

import util.android.textviews.ExpandableTextView;
import util.android.textviews.FontTextView;
import util.android.textviews.ShimmerTextView;
import util.android.textviews.TypefaceSpan;
import util.android.textviews.shimmer.Shimmer;
import util.android.util.DisplayUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    CheckBox ckJustify;
    CheckBox ckEllipsizeWords;
    CheckBox ckAutoMax;
    FontTextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ckJustify = view.findViewById(R.id.checkBox);
        ckEllipsizeWords = view.findViewById(R.id.checkBox2);
        ckAutoMax = view.findViewById(R.id.checkBox3);
        textView = view.findViewById(R.id.normal);

        ckJustify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                textView.setJustify(b);
            }
        });

        ckEllipsizeWords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                textView.setWordEllipsize(b);
            }
        });

        ckAutoMax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                textView.setAutoMax(b);
            }
        });

        textView.setOnLinkClickListener(new FontTextView.OnLinkClickListener() {
            @Override
            public void onLinkClick(View textView, String link, int type) {
                Toast.makeText(getContext(), "Link clicked: " + link, Toast.LENGTH_SHORT).show();
            }
        });

        setText();

    }
    private void setText() {
        TypefaceSpan span = new TypefaceSpan(getContext(), "Audiowide-Regular");
        TypefaceSpan span2 = new TypefaceSpan(getContext(), "NotoSerifDisplay-Regular");

        SpannableString title = new SpannableString(getString(R.string.hipsum));
        title.setSpan(span, 0, 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        title.setSpan(span2, 197, 262, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(title, true);
    }

    public MainActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
