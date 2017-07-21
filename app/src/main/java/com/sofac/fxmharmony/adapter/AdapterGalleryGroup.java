package com.sofac.fxmharmony.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sofac.fxmharmony.R;

import java.util.ArrayList;

/**
 * Created by Maxim on 21.07.2017.
 */

public class AdapterGalleryGroup extends RecyclerView.Adapter<AdapterGalleryGroup.ViewHolder> {

    private ArrayList<String> data;

    public AdapterGalleryGroup(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_gallery_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext())
                    .load(data.get(position))
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
