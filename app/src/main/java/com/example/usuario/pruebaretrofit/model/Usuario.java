package com.example.usuario.pruebaretrofit.model;

/**
 * Created by usuario on 30/11/2017.
 */
import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class Usuario implements Serializable
    {

        @SerializedName("nombre")
        @Expose
        private String nombre;
                @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("correo")
        @Expose
        private String correo;
        @SerializedName("puntFinal")
        @Expose
        private Integer puntFinal;
        private final static long serialVersionUID = 3122917227009467179L;

        public Usuario(String nombre, String password, String correo) {
            this.nombre = nombre;
            this.correo = correo;
            this.password = password;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getPuntFinal() {
            return puntFinal;
        }

        public void setPuntFinal(Integer puntFinal) {
            this.puntFinal = puntFinal;
        }

    }
