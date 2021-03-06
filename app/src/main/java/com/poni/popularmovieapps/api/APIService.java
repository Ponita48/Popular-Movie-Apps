package com.poni.popularmovieapps.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poni.popularmovieapps.model.CurrencyModel;
import com.poni.popularmovieapps.model.DetailModel;
import com.poni.popularmovieapps.model.DiscoverModel;
import com.poni.popularmovieapps.model.SearchModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("discover/movie?api_key=69292009f880792c30f93ceb1ae0e166")
    Call<DiscoverModel> getMovies(@Query("page") int page, @Query("sort_by") String sort);

    @GET("movie/{id}?api_key=69292009f880792c30f93ceb1ae0e166")
    Call<DetailModel> getDetail(@Path("id") Integer id);

    @GET("search/keyword?api_key=69292009f880792c30f93ceb1ae0e166")
    Call<SearchModel> getKeywordSearch(@Query("query") String query);

    @GET("search/movie?api_key=69292009f880792c30f93ceb1ae0e166")
    Call<DiscoverModel> getMovieSearch(@Query("query") String query);

    //Currency
    @GET("latest?base=USD&symbols=IDR")
    Call<CurrencyModel> getCurrency();

    Gson gson = new GsonBuilder().create();

    String baseUrl = "https://api.themoviedb.org/3/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    Retrofit currency = new Retrofit.Builder()
            .baseUrl("http://api.fixer.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    APIService curr = APIService.currency.create(APIService.class);

    APIService service = APIService.retrofit.create(APIService.class);
}
