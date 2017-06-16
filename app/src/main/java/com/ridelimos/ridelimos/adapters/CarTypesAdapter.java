package com.ridelimos.ridelimos.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ridelimos.ridelimos.R;
import com.ridelimos.ridelimos.models.CarType;

import java.util.ArrayList;

/**
 * Created by zebarahman on 6/12/17.
 */

public class CarTypesAdapter extends RecyclerView.Adapter<CarTypebarViewHolder> {

    private ArrayList<CarType> itemList;
    private Context context;
    private View prevView;

    public CarTypesAdapter(Context context, ArrayList<CarType> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public CarType getItem(int position) {
        return itemList.get(position);
    }

    public void setItems(ArrayList<CarType> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public CarTypebarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartype_item_view, null);
        CarTypebarViewHolder rcv = new CarTypebarViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final CarTypebarViewHolder holder, final int position) {
        holder.title.setText(itemList.get(position).getTitle());

        try { Glide.with(context).load(itemList.get(position).getPicture()).into(holder.picture);
        } catch (Exception e) { e.printStackTrace(); }

        if(position==0){
            holder.view.setVisibility(View.VISIBLE);
            prevView=holder.view;
        }
        else
            holder.view.setVisibility(View.INVISIBLE);

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.view.setVisibility(View.VISIBLE);
                if(prevView!=null)
                prevView.setVisibility(View.INVISIBLE);
                prevView=holder.view;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


}


