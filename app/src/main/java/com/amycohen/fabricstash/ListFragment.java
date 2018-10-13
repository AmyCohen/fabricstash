package com.amycohen.fabricstash;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amycohen.fabricstash.models.Fabric;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {
    //implements ValueEventListener
    //implements TextWatcher
    private List<Fabric> fabrics;

    @BindView(R.id.inventoryList) RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_fragment, container, false);

        ButterKnife.bind(this, view);



        linearLayoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new ListAdapter(fabrics);
        recyclerView.setAdapter(listAdapter);


        return view;
    }
}

/*
public class ErrandListFragment extends Fragment implements ValueEventListener {

 */
