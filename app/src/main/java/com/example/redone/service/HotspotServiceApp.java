package com.example.redone.service;

import com.example.redone.models.RegistroUserFicha;
import com.example.redone.models.Codigos;
import com.example.redone.models.HotspotApp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HotspotServiceApp {

    @GET("api/hotspot/user/{idUser}")
    public Call<Codigos> findCodigosByIdUser(@Path("idUser") Long idusuario);

    @GET("api/hotspot/app/buscarPorIdPerfil/{idPerfil}")
    public Call<HotspotApp> findCodigoByIdPerfil(@Path("idPerfil") Long idPerfil);

    @GET("api/hotspot/app/modificarPorId/id={ficha}&estatus={estatus}")
    public Call<HotspotApp>updateEstatusPorFicha(@Path("ficha")String ficha,
                                                 @Path("estatus")boolean estatus);

    @GET("api/hotspot/user/guardar/idUser={idUser}&idPerfil={idPerfil}&ficha={ficha}")
    public Call<RegistroUserFicha> saveCodigo(@Path("idUser") Long iduser,
                                              @Path("idPerfil") Long idperfil,
                                              @Path("ficha") String codigo);

    @GET("api/hotspot/user/modificar/idUser={idUser}&idPerfil={idPerfil}")
    public Call<RegistroUserFicha>modificarPorIdPerfil(@Path("idUser") Long idUser,
                                                       @Path("idPerfil") Long idPerfil);

    @GET("api/hotspot/user/buscarPorIdUserIdPerfil/idUser={idUser}&idPerfil={idPerfil}")
    public Call<RegistroUserFicha>buscarPorIdUsuarioIdPerfil(@Path("idUser")Long idUser,
                                                             @Path("idPerfil") Long idPerfil);


}
