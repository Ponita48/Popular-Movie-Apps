package com.poni.popularmovieapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poni.popularmovieapps.R;
import com.poni.popularmovieapps.model.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    List<Result> list;
    Context context;
    LayoutInflater inflater;

    public SearchAdapter(Context context, List<Result> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Result getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_search_title, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Picasso.with(this.context).load("https://image.tmdb.org/t/p/w500" +
                getItem(position).getPosterPath()).
                into(vh.img);
        vh.text.setText(getItem(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView text;

        public ViewHolder(View v) {
            this.img = (ImageView) v.findViewById(R.id.imgSearch);
            this.text = (TextView) v.findViewById(R.id.titleSearch);
        }
    }
}
