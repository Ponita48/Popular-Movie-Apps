package com.poni.popularmovieapps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.poni.popularmovieapps.R;
import com.poni.popularmovieapps.adapter.SearchAdapter;
import com.poni.popularmovieapps.api.APIService;
import com.poni.popularmovieapps.model.DiscoverModel;
import com.poni.popularmovieapps.model.Result;
import com.poni.popularmovieapps.model.Results;
import com.poni.popularmovieapps.model.SearchModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ProgressBar pb;
    EditText edit;
    Button btn;
    ListView lv;
    RadioGroup radio;

    List<Results> listKeyword = new ArrayList<>();
    List<Result> listTitle = new ArrayList<>();
    List<String> nama = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit = (EditText) findViewById(R.id.editSearch);
        lv = (ListView) findViewById(R.id.listSearch);
        pb = (ProgressBar) findViewById(R.id.pbSearch);
        radio = (RadioGroup) findViewById(R.id.select);
        btn = (Button) findViewById(R.id.searchButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int group = radio.getCheckedRadioButtonId();
                switch (group) {
                    case R.id.movTitle:
                        connectTitle();
                        break;
                    case R.id.movKey:
                        connectKeyword();
                        break;
                    default:
                        Toast.makeText(SearchActivity.this, "Please Select Search Method",
                                Toast.LENGTH_SHORT).show();
                }
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

    public void connectTitle() {
        pb.setVisibility(View.VISIBLE);
        lv.setVisibility(View.INVISIBLE);
        Call<DiscoverModel> call = APIService.service.getMovieSearch(edit.getText().toString());
        call.enqueue(new Callback<DiscoverModel>() {
            @Override
            public void onResponse(Call<DiscoverModel> call, Response<DiscoverModel> response) {
                if (response.isSuccessful()) {
                    DiscoverModel body = response.body();
                    listTitle.addAll(body.getResults());
                    SearchAdapter adapter = new SearchAdapter(SearchActivity.this, listTitle);
                    lv.setAdapter(adapter);
                    pb.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent in = new Intent(getApplicationContext(), DetailActivity.class);
                            in.putExtra("id", listTitle.get(position).getId());
                            startActivity(in);
                        }
                    });
                } else {
                    Toast.makeText(SearchActivity.this, "Connection Timed Out",
                            Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<DiscoverModel> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    public void connectKeyword() {
        pb.setVisibility(View.VISIBLE);
        lv.setVisibility(View.INVISIBLE);
        Call<SearchModel> call = APIService.service.getKeywordSearch(edit.getText().toString());
        call.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                if (response.isSuccessful()) {
                    SearchModel body = response.body();
                    listKeyword.addAll(body.getResults());
                    for (int i = 0; i < listKeyword.size(); i++) {
                        nama.add(listKeyword.get(i).getName());
                    }
                    ArrayAdapter adapter = new ArrayAdapter(SearchActivity.this, R.layout.list_simple, nama);
                    lv.setAdapter(adapter);
                    pb.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent in = new Intent(getApplicationContext(), DetailActivity.class);
                            in.putExtra("id", listKeyword.get(position).getId());
                            startActivity(in);
                        }
                    });
                } else {
                    Toast.makeText(SearchActivity.this, "Connection Timed Out",
                            Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
            }
        });
    }
}
