package com.ridelimos.ridelimos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridelimos.ridelimos.R;
import com.ridelimos.ridelimos.models.Trip;

import java.util.List;

/**
 * Created by zebarahman on 6/10/17.
 */

public class HistoryAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context mContext;
    List<Trip> items;

    public HistoryAdapter(Context mContext, List<Trip> items) {
        this.mContext = mContext;
        this.items = items;
        inflater = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return items.size();
    }

    public Trip getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder {
        TextView txttitle;
        ImageView thumb;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listitem_history, null);
            holder.txttitle = (TextView) view.findViewById(R.id.txtdate);
            //holder.thumb = (ImageView) view.findViewById(R.id.video_thumbnail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        //holder.txttitle.setText(items.get(position).getName());

        return view;
    }
}



