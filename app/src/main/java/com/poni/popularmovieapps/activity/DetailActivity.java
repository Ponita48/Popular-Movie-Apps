package com.poni.popularmovieapps.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.poni.popularmovieapps.R;
import com.poni.popularmovieapps.api.APIService;
import com.poni.popularmovieapps.model.DetailModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView foto = (ImageView) findViewById(R.id.fotoDetail);
        final ImageView poster = (ImageView) findViewById(R.id.poster);
        final TextView judul = (TextView) findViewById(R.id.judulDetail);
        final TextView rilis = (TextView) findViewById(R.id.tglRilis);
        final TextView sinopsis = (TextView) findViewById(R.id.sinopsisDetail);
        final ProgressBar loading = (ProgressBar) findViewById(R.id.loadingDetail);

        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("id");
        loading.setVisibility(View.VISIBLE);
        Call<DetailModel> call = APIService.service.getDetail(id);
        call.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                if (response.isSuccessful()) {
                    DetailModel body = response.body();
                    Picasso.with(DetailActivity.this).load("https://image.tmdb.org/t/p/w500" +
                            body.getBackdropPath()).
                            into(poster);
                    Picasso.with(DetailActivity.this).load("https://image.tmdb.org/t/p/w500" +
                            body.getPosterPath()).
                            into(foto);
                    judul.setText(judul.getText() + " " + body.getTitle());
                    rilis.setText(rilis.getText() + " " + body.getReleaseDate());
                    sinopsis.setText(body.getOverview());
                    loading.setVisibility(View.GONE);
                    judul.setVisibility(View.VISIBLE);
                    rilis.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(DetailActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
