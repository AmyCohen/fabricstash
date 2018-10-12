package com.amycohen.fabricstash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amycohen.fabricstash.models.Fabric;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private FragmentManager fragmentManager;



    List<Fabric> fabricsList;

    public ListAdapter (List<Fabric> fabricsList) {
        this.fabricsList = fabricsList;
    }

    @Override
    public int getItemCount() {
        return this.fabricsList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.frabic_item, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Fabric fabric = fabricsList.get(i);
        //fill in the info that will be in the view
//        holder.fabricType.setText(fabric.fabricType);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Fabric mFabric;
        public View view;
//        public TextView
//        public TextView
//        public TextView
//        public TextView
//        public TextView
        
        public MyViewHolder(View view) {
            super(view);
            this.view = view;

//            this.name = view.findViewById(R.id.name);
            
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(v.getContext(), FabricDetailActivity.class);
//            mFabric.fillIntent(intent);
//
//            v.getContext().startActivity(intent);
        }
    }
    
    //Add a click event to take you to a detail page
}
