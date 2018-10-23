package com.amycohen.fabricstash;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amycohen.fabricstash.models.Fabric;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FabricListAdapter extends RecyclerView.Adapter<FabricListAdapter.MyViewHolder> {

    public List<Fabric> fabricsList;

    public FabricListAdapter() {
        fabricsList = new ArrayList<>();
        Log.d("COUNT", String.valueOf(fabricsList.size()));
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
        holder.bind(fabricsList.get(i));

    }

    @Override
    public int getItemCount() {
        return fabricsList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        TextView fabricName;
        TextView fabricCompany;
        TextView fabricType;
//        ImageView fabricImage;
        Fabric mFabric;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mView = itemView;
            mView.setOnClickListener(this);

//            fabricImage = itemView.findViewById(R.id.fabricPhoto);
            fabricName = itemView.findViewById(R.id.fabricName);
            fabricCompany = itemView.findViewById(R.id.fabricCompany);
            fabricType = itemView.findViewById(R.id.fabricType);
        }

        public void bind (Fabric fabric) {
            this.mFabric = fabric;
            fabricName.setText(fabric.fabricName);
            fabricCompany.setText(fabric.company);
            fabricType.setText(fabric.fabricType);

//            Ion.with(fabricImage)
//                    .error(R.drawable.placeholder)
//                    .load(fabric.imageUrl);
        }

    //Add a click event to take you to a detail page
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(v.getContext(), FabricDetailActivity.class);
//            mFabric.fillIntent(intent);
//
//            v.getContext().startActivity(intent);
        }
    }

}
