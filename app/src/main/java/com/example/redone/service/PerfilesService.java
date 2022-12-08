package com.example.redone.service;


import com.example.redone.models.Codigos;
import com.example.redone.models.PerfilConexion;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PerfilesService {

    @GET("perfiles/findById/{id}")
    public Call<PerfilConexion> findPerfilById(@Path("id") int id);


}
