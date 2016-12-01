package com.poni.popularmovieapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poni.popularmovieapps.R;
import com.poni.popularmovieapps.activity.DetailActivity;
import com.poni.popularmovieapps.model.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerPreviewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    List<Result> results;
    Context context;
    LayoutInflater inflater;

    public RecyclerPreviewAdapter(Context context, List<Result> results) {
        this.context = context;
        this.results = results;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.layout_preview, null);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final Result temp = results.get(position);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w500" +
                temp.getPosterPath()).
                into(holder.getImg());
        holder.getImg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DetailActivity.class);
                in.putExtra("id", temp.getId());
                context.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.results.size();
    }
}
