package com.example.retrofit;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderAPI {

    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId, // Integer and not int because Integer in nullable :)
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    //Another way to create query params without defining them hee in the API interface
    @GET("posts")
    Call<List<Post>> getPosts(
            @QueryMap Map<String, String> params
    );

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);

    @POST("posts")
    Call<Post> createPost(@Body Post post);

}
