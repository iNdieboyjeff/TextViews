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


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sample2.model.BBCSchedule;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExampleFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExampleFragment1 extends Fragment implements Callback {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static final String CHANNEL_NAME = "channel_name";
    private static final String SCHEDULE_LINK = "schedule_link";

    private String mChannelName;
    private String mScheduleLink;

    private BBCSchedule mSchedule;

    private RecyclerView scheduleList;

    public ExampleFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExampleFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static ExampleFragment1 newInstance(String param1, String param2) {
        ExampleFragment1 fragment = new ExampleFragment1();
        Bundle args = new Bundle();
        args.putString(CHANNEL_NAME, param1);
        args.putString(SCHEDULE_LINK, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChannelName = getArguments().getString(CHANNEL_NAME);
            mScheduleLink = getArguments().getString(SCHEDULE_LINK);
        }

        Request request = new Request.Builder().url(mScheduleLink).build();
        client.newCall(request).enqueue(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_example_fragment1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scheduleList = (RecyclerView) view.findViewById(R.id.scheduleView);

        scheduleList.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onFailure(Request request, IOException e) {

    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
            final String body = response.body().string();
            mSchedule = gson.fromJson(body, BBCSchedule.class);
            scheduleList.post(new Runnable() {
                @Override
                public void run() {
                    scheduleList.setAdapter(new ScheduleAdapter(mSchedule.getSchedule().getDay().getBroadcasts()));
                }
            });

        }
    }
}
