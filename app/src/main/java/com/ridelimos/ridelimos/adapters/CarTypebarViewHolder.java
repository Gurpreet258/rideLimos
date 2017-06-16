package com.ridelimos.ridelimos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridelimos.ridelimos.R;

/**
 * Created by zebarahman on 6/12/17.
 */

public class CarTypebarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView title;
    public TextView subtitle;
    public ImageView picture;
    public View view;

    public CarTypebarViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.car_title);
        subtitle = (TextView) itemView.findViewById(R.id.car_subtitle);
        picture = (ImageView) itemView.findViewById(R.id.car_photo);
        view = (View) itemView.findViewById(R.id.viewHeader);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}