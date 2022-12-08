package com.example.redone.models;

public class HotspotApp {

    private String ficha;
    private Boolean disponible;
    private String fecha_creacion;
    private Integer idperfil;


    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }

    public Integer getIdperfil() {
        return idperfil;
    }

    public void setIdperfil(Integer idperfil) {
        this.idperfil = idperfil;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Hotspot{" +
                "ficha='" + ficha + '\'' +
                ", idperfil=" + idperfil +
                ", fecha_creacion='" + fecha_creacion + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
