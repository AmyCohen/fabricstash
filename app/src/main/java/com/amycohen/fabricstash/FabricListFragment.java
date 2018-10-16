package com.amycohen.fabricstash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amycohen.fabricstash.models.Fabric;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FabricListFragment  extends Fragment implements ValueEventListener {
    //implements TextWatcher
    private final String TAG = "DATABASE";
    private List<Fabric> fabrics;

    @BindView(R.id.inventoryList)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference fabricInventoryList = FirebaseDatabase.getInstance().getReference("fabricInventory");
        fabricInventoryList.addValueEventListener(this);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new ListAdapter(fabrics);
        recyclerView.setAdapter(listAdapter);


        return view;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        String value = dataSnapshot.getValue(String.class);
        Log.d(TAG, "Value is: " + value);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", databaseError.toException());
    }
}