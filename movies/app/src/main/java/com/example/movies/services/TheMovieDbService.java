package com.example.movies.services;

import com.example.movies.models.Movie;
import com.example.movies.models.ResponseMovies;
import com.example.movies.models.ResponseResults;
import com.example.movies.models.ReviewMovie;
import com.example.movies.models.VideoMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {

    @GET("movie/popular")
    Call<ResponseMovies> getMostPopular(@Query("api_key") String key);

    @GET("movie/top_rated")
    Call<ResponseMovies> getTopRated(@Query("api_key") String key);

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(@Path("movie_id") String id,@Query("api_key") String key);

    @GET("movie/{movie_id}/videos")
    Call<ResponseResults<VideoMovie>> getVideosMovie(@Path("movie_id") String id, @Query("api_key") String key);

    @GET("movie/{movie_id}/reviews")
    Call<ResponseResults<ReviewMovie>> getReviewsMovie(@Path("movie_id") String id, @Query("api_key") String key);
}
