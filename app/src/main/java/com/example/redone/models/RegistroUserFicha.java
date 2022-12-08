package com.example.redone.models;


public class RegistroUserFicha {

    private Integer id;
    private Integer idperfil;
    private Integer idusuario;
    private String fichas;
    private Integer total;
    private String estatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFichas() {
        return fichas;
    }

    public void setFichas(String fichas) {
        this.fichas = fichas;
    }

    public Integer getIdperfil() {
        return idperfil;
    }

    public void setIdperfil(Integer idperfil) {
        this.idperfil = idperfil;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Codigo{" +
                "id=" + id +
                ", estatus='" + estatus + '\'' +
                ", fichas='" + fichas + '\'' +
                ", idperfil=" + idperfil +
                ", idusuario=" + idusuario +
                ", total=" + total +
                '}';
    }
}
