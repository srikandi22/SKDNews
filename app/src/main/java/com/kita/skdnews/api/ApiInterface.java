package com.kita.skdnews.api;

import com.kita.skdnews.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );

    @GET("top-headlines")
    Call<News> getNewsSearch(
            @Query("category") String category,
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("apiKey") String apiKey
    );
}
