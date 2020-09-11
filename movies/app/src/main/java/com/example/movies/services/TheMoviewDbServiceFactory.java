package com.example.movies.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheMoviewDbServiceFactory {

    static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static TheMovieDbService create() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(TheMovieDbService.class);
    }
}
