package com.poni.popularmovieapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.poni.popularmovieapps.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private ImageView img;

    public RecyclerViewHolder(final View view) {
        super(view);
        img = (ImageView) view.findViewById(R.id.imagePreview);
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }
}
