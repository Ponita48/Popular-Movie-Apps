package com.poni.popularmovieapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.poni.popularmovieapps.api.APIService;
import com.poni.popularmovieapps.model.DiscoverModel;
import com.poni.popularmovieapps.model.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private GridView gv;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        gv = (GridView) findViewById(R.id.previewLayout);
        Call<DiscoverModel> call = APIService.service.getMovies(1);
        call.enqueue(new Callback<DiscoverModel>() {
            @Override
            public void onResponse(Call<DiscoverModel> call, Response<DiscoverModel> response) {
                if (response.isSuccessful()) {
                    final List<Result> list = response.body().getResults();
                    PreviewAdapter adapter = new PreviewAdapter(MainActivity.this, list);
                    gv.setAdapter(adapter);
                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Integer movieId = list.get(position).getId();
                            Intent in = new Intent(MainActivity.this, DetailActivity.class);
                            in.putExtra("id", movieId);
                            startActivity(in);
                        }
                    });
                    pb.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DiscoverModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connecion Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
