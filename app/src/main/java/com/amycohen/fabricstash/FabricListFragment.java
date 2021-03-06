package com.amycohen.fabricstash;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FabricListFragment  extends Fragment implements ValueEventListener {
    //implements TextWatcher

    @BindView(R.id.inventoryList) RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FabricListAdapter fabricListAdapter;

//    private StorageReference mStorageRef;
    private final String TAG = "DATABASE";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list, container, false);

        ButterKnife.bind(this, view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();
        DatabaseReference fabricInventoryList = FirebaseDatabase.getInstance().getReference("fabricInventory").child(uid);

//        DatabaseReference fabricInventoryList = FirebaseDatabase.getInstance().getReference("fabricInventory");
        fabricInventoryList.addValueEventListener(this);

//        mStorageRef = FirebaseStorage.getInstance().getReference("fabricPhotos");

        linearLayoutManager = new LinearLayoutManager(getActivity());
        fabricListAdapter = new FabricListAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(fabricListAdapter);


        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        List<Fabric> fabrics = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            fabrics.add(Fabric.fromSnapshot(snapshot));
        }

        fabricListAdapter.fabricsList = fabrics;
        fabricListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", databaseError.toException());
    }

    @OnClick(R.id.addToStash)
    public void addToStash() {
        Intent intent = new Intent(getActivity(), CreateNewItemActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.goToHome)
    public void returnHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}