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

package com.example.sample2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.sample2.model.Broadcast;

import util.android.textviews.ExpandableTextView;
import util.android.textviews.FontTextView;

/**
 * Created by jeff on 20/11/2015.
 */
public class BroadcastHolder extends RecyclerView.ViewHolder{

    private RecyclerView.Adapter mAdapter;
    private FontTextView mTitle;
    private ExpandableTextView mSynopsis;


    public BroadcastHolder(View itemView, RecyclerView.Adapter adapter) {
        super(itemView);
        mAdapter = adapter;
        mTitle = (FontTextView) itemView.findViewById(R.id.title);
        mSynopsis = (ExpandableTextView) itemView.findViewById(R.id.synopsis);
    }

    public void bindBroadcast(final Broadcast broadcast, int position) {
        mTitle.setText(broadcast.getProgramme().getDisplayTitles().getTitle());
        mSynopsis.setText(broadcast.getProgramme().getShortSynopsis() + "\nhfdsfuihewkjfksdhfkhsdfhisuehf\nhfourhfgiurehgiheighrehgiuh\ngoiherogthreuhgiurehh\noifgdfgfdgaegregfgdfgfdgrrgghir", true);
        mSynopsis.setContracted(!broadcast.expanded);
        mSynopsis.setOnViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcast.expanded = !mSynopsis.getContracted();
            }
        });

    }
}
