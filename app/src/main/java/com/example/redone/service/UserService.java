package com.example.redone.service;

import com.example.redone.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

    @GET("api/user/buscarPorId/{id}")
    public Call<User>find(@Path("id") String id);


    @GET("api/user/buscarPorUsuarioContrasena/{username}/{password}")
    public Call<User>findUserAndPass(@Path("username") String username,@Path("password")String password);

    @GET("user/updateByUser/user={username}&newpass={pass}")
    public Call<Boolean> updateUserByUsuario(@Path("username") String username, @Path("pass")String pass);


}
