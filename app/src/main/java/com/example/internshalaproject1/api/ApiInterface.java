package com.example.internshalaproject1.api;

import static com.example.internshalaproject1.api.ApiUtilities.API_KEY;

import com.example.internshalaproject1.model.ImageModel;
import com.example.internshalaproject1.model.SearchModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/photos")
    Call<List<ImageModel>> getImages(
                  @Query("page") int page,
                          @Query("per_page") int perPage
    );

    @Headers("Authorization: Client-ID "+API_KEY)
    @GET("/search/photos")
    Call<SearchModel> searchImages(
            @Query("query") String qquery
    );

}
