package com.example.redone.models;

import java.util.Date;

public class User {

        private Integer idusuario;
        private String nombre_completo;
        private String fecha_creacion;
        private String telefono;

        public Integer getIdusuario() {
                return idusuario;
        }

        public void setIdusuario(Integer idusuario) {
                this.idusuario = idusuario;
        }

        public String getNombre_completo() {
                return nombre_completo;
        }

        public void setNombre_completo(String nombre_completo) {
                this.nombre_completo = nombre_completo;
        }

        public String getFecha_creacion() {
                return fecha_creacion;
        }

        public void setFecha_creacion(String fecha_creacion) {
                this.fecha_creacion = fecha_creacion;
        }

        public String getTelefono() {
                return telefono;
        }

        public void setTelefono(String telefono) {
                this.telefono = telefono;
        }
}
