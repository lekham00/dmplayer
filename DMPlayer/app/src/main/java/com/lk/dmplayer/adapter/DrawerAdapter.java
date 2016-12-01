package com.lk.dmplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lk.dmplayer.R;
import com.lk.dmplayer.models.DrawerItem;

import java.util.ArrayList;

/**
 * Created by Le Kham on 10/23/2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private ArrayList<DrawerItem> drawerItems;

    public DrawerAdapter(ArrayList<DrawerItem> drawerItems) {
        this.drawerItems = drawerItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView textViewTitle = (TextView) holder.itemView.findViewById(R.id.textViewDrawerItemTitle);
        ImageView imageViewIcon = (ImageView) holder.itemView.findViewById(R.id.imageViewDrawerIcon);
        FrameLayout frameLayout = (FrameLayout) holder.itemView.findViewById(R.id.item_divider);
        frameLayout.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
        textViewTitle.setText(drawerItems.get(position).getTitle());
        imageViewIcon.setImageDrawable(drawerItems.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
