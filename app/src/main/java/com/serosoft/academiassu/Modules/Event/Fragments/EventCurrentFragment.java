package com.serosoft.academiassu.Modules.Event.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cipolat.superstateview.SuperStateView;
import com.serosoft.academiassu.Modules.Event.Adapters.EventAdapter;
import com.serosoft.academiassu.Modules.Event.Models.Events_Dto;
import com.serosoft.academiassu.R;
import com.serosoft.academiassu.Utils.ProjectUtils;

import java.util.ArrayList;
import java.util.Collections;

public class EventCurrentFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private SuperStateView superStateView;

    ArrayList<Events_Dto> eventList;
    EventAdapter eventAdapter;
    LinearLayoutManager linearLayoutManager;

    private final String TAG = EventCurrentFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.event_list_fragment, container, false);
        ProjectUtils.showLog(TAG,"onCreateView start");

        mContext = getActivity();

        Initialize(v);

        Bundle bundle = getArguments();
        if(bundle != null){
            eventList = new ArrayList<>();
            eventList = (ArrayList<Events_Dto>) bundle.getSerializable("Current");
            ProjectUtils.showLog(TAG,""+eventList.size());
        }

        if(eventList != null && eventList.size() > 0){
            checkEmpty(false);

            Collections.sort(eventList, Events_Dto.sortMostCurrent);

            eventAdapter = new EventAdapter(mContext,eventList);
            recyclerView.setAdapter(eventAdapter);
            eventAdapter.notifyDataSetChanged();
        }
        else{

            checkEmpty(true);
        }

        return v;
    }

    private void checkEmpty(boolean is_empty)
    {
        if(is_empty)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            superStateView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            superStateView.setVisibility(View.INVISIBLE);
        }
    }

    private void Initialize(View v) {

        recyclerView = v.findViewById(R.id.recyclerView);
        superStateView = v.findViewById(R.id.superStateView);

        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }
}
