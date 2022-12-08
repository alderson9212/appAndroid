package com.example.redone.service;


import com.example.redone.models.Hotspot;
import com.example.redone.models.PerfilConexion;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HotspotService {
    @GET("api/hotspot/buscarPorId/{codigo}")
    public Call<Hotspot> findHotspotById(@Path("codigo") String codigo);

    @GET("api/hotspot/modificarPorId/ficha={ficha}&status={status}")
    public Call<Hotspot> modificarPorId(@Path("ficha") String codigo,@Path("status") boolean status);


}
