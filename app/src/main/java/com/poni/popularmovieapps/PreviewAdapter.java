package com.poni.popularmovieapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PreviewAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public PreviewAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_preview, parent, false);
        }
        img = (ImageView) convertView.findViewById(R.id.imagePreview);
        Picasso.with(this.context).load("https://image.tmdb.org/t/p/w500"+getItem(position)).
                into(img);
        return convertView;
    }
}
