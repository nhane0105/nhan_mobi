package com.example.baitap4;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface định nghĩa các API call.
 * Base URL: https://jsonplaceholder.typicode.com
 */
public interface ApiService {

    @GET("posts")
    Call<List<Post>> getPosts();
}
