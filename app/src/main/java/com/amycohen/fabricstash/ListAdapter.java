package com.amycohen.fabricstash;

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

    public List<Fabric> fabricsList;
    private FragmentManager fragmentManager;

    public ListAdapter(){}
    public ListAdapter (List<Fabric> fabricsList) {
        this.fabricsList = fabricsList;
    }

    public ListAdapter(Fabric fabrics) {
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_adapter);
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.frabic_item, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        
        public View view;
//        public TextView
//        public TextView
//        public TextView
//        public TextView
//        public TextView
        
        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            
        }
    }
    
    //Add a click event to take you to a detail page
}
