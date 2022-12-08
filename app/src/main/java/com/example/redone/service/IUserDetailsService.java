package com.example.redone.service;

import com.example.redone.models.UserDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IUserDetailsService {

    @GET("api/user/details/buscarPorUsername/{username}")
    public Call<UserDetail> findUsername(@Path("username") String username);

    @GET("api/user/details/buscarPorUsernamePassword/username={username}&pass={password}")
    public Call<UserDetail> findUsernamePass(@Path("username") String username,@Path("password") String password);
}
