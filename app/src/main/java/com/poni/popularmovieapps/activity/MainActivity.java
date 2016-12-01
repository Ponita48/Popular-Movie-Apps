package com.poni.popularmovieapps.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.poni.popularmovieapps.adapter.PreviewAdapter;
import com.poni.popularmovieapps.R;
import com.poni.popularmovieapps.api.APIService;
import com.poni.popularmovieapps.model.DiscoverModel;
import com.poni.popularmovieapps.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private GridView gv;
    private ProgressBar pb;
    private TextView textMain;
    private Button refresh;
    private String sort = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        gv = (GridView) findViewById(R.id.previewLayout);
        textMain = (TextView) findViewById(R.id.textMain);
        refresh = (Button) findViewById(R.id.buttonRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(sort);
            }
        });
        connect(sort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                CharSequence colors[] = new CharSequence[] {
                        "Popularity Ascending",
                        "Popularity Descending",
                        "Release Date Ascending",
                        "Release Date Descending"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Sort By");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                sort = "popularity.asc";
                                break;
                            case 1:
                                sort = "popularity.desc";
                                break;
                            case 2:
                                sort = "release_date.asc";
                                break;
                            case 3:
                                sort = "release_date.desc";
                                break;
                            default:
                                sort = "";
                                break;
                        }
                        connect(sort);
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void connect(String sort) {
        pb.setVisibility(View.VISIBLE);
        gv.setVisibility(View.INVISIBLE);
        refresh.setVisibility(View.INVISIBLE);
        textMain.setVisibility(View.INVISIBLE);
        Call<DiscoverModel> call = APIService.service.getMovies(1, sort);
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
                    gv.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                    textMain.setVisibility(View.VISIBLE);
                    textMain.setText("Connection Timed Out");
                    refresh.setVisibility(View.VISIBLE);
                    refresh.setText("REFRESH");
                }
            }

            @Override
            public void onFailure(Call<DiscoverModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                textMain.setVisibility(View.VISIBLE);
                textMain.setText("Connection Failed");
                refresh.setVisibility(View.VISIBLE);
                refresh.setText("REFRESH");
            }
        });
    }
}
