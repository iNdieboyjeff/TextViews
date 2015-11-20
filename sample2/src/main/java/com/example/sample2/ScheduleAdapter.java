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
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.sample2.model.Broadcast;

import java.util.List;

/**
 * Created by jeff on 20/11/2015.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<BroadcastHolder> {

    private final List<Broadcast> mBroadcasts;

    public ScheduleAdapter(List<Broadcast> mBroadcasts) {
        this.mBroadcasts = mBroadcasts;
    }

    @Override
    public BroadcastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BroadcastHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false), this);
    }

    @Override
    public void onBindViewHolder(BroadcastHolder holder, int position) {
        Broadcast broadcast = mBroadcasts.get(position);
        holder.bindBroadcast(broadcast, position);
    }

    @Override
    public int getItemCount() {
        return mBroadcasts.size();
    }
}
