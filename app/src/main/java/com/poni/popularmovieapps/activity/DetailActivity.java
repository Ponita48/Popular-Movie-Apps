package com.poni.popularmovieapps.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.poni.popularmovieapps.R;
import com.poni.popularmovieapps.api.APIService;
import com.poni.popularmovieapps.model.CurrencyModel;
import com.poni.popularmovieapps.model.DetailModel;
import com.poni.popularmovieapps.model.Rates;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    
    String curr = "USD";
    static double budgetVal;
    TextView ganti;

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
        final TextView rating = (TextView) findViewById(R.id.rating);
        final TextView budget = (TextView) findViewById(R.id.budget);
        final ProgressBar loading = (ProgressBar) findViewById(R.id.loadingDetail);
        ganti = budget;

        Bundle bundle = getIntent().getExtras();
        Integer id = bundle.getInt("id");
        loading.setVisibility(View.VISIBLE);
        Call<DetailModel> call = APIService.service.getDetail(id);
        call.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                if (response.isSuccessful()) {
                    DetailModel body = response.body();
                    DetailActivity.setBudgetVal(body.getBudget());
                    budgetVal /= 1000000;
                    if (curr.equals("IDR")) {
                        toIDR(budgetVal, budget);
                    }
                    Picasso.with(DetailActivity.this).load("https://image.tmdb.org/t/p/w500" +
                            body.getBackdropPath()).
                            into(poster);
                    Picasso.with(DetailActivity.this).load("https://image.tmdb.org/t/p/w500" +
                            body.getPosterPath()).
                            into(foto);
                    judul.setText(judul.getText() + " " + body.getTitle());
                    rilis.setText(rilis.getText() + " " + body.getReleaseDate());
                    rating.setText(rating.getText() + " " + body.getVoteAverage());
                    budget.setText(getString(R.string.budget_def) + " $" + budgetVal + " M");
                    sinopsis.setText(body.getOverview());
                    loading.setVisibility(View.GONE);
                    budget.setVisibility(View.VISIBLE);
                    judul.setVisibility(View.VISIBLE);
                    rilis.setVisibility(View.VISIBLE);
                    rating.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(DetailActivity.this, "Connection Timed Out",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_curr:
                CharSequence currency[] = new CharSequence[] {"USD", "IDR"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Set Currency");
                builder.setItems(currency, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                curr = "USD";
                                ganti.setText(getString(R.string.budget_def) + " $" + budgetVal +
                                        " M");
                                break;
                            case 1:
                                curr = "IDR";
                                toIDR(DetailActivity.getBudgetVal(), ganti);
                                break;
                            default:
                                curr = "USD";
                                ganti.setText(getString(R.string.budget_def) + " $" + budgetVal +
                                        " M");
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void toIDR(final double budgetVal, final TextView budget) {
        Call<CurrencyModel> call = APIService.curr.getCurrency();
        call.enqueue(new Callback<CurrencyModel>() {
            @Override
            public void onResponse(Call<CurrencyModel> call, Response<CurrencyModel> response) {
                if (response.isSuccessful()) {
                    double hasil = budgetVal * response.body().getRates().getIDR();
                    hasil /= 1000000;
                    budget.setText(getString(R.string.budget_def) + " Rp" + hasil + " Trilliun");
                    budget.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(DetailActivity.this, "Connection Timed Out",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CurrencyModel> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static double getBudgetVal() {
        return budgetVal;
    }

    public static void setBudgetVal(double budgetVal) {
        DetailActivity.budgetVal = budgetVal;
    }
}
