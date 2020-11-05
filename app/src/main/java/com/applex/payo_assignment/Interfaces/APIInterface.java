package com.applex.payo_assignment.Interfaces;

import com.applex.payo_assignment.Models.DataModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    String BASE_URL = "https://reqres.in/api/";

    @GET("users?")
    Call<DataModel> getUserList(@Query("page") String page);
}