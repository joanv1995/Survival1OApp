package com.example.usuario.pruebaretrofit.service;

import com.example.usuario.pruebaretrofit.model.Ranking2;
import com.example.usuario.pruebaretrofit.model.Usuario2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by usuario on 01/12/2017.
 */

public interface RestClient {

    @GET("{userName}/listaUsuarios")
    Call<List<Usuario2>> getListaUsuarios(@Path("userName") String userName);
    @GET("player/{nomPlayer}")
    Call<Usuario2> loginUser(@Body Usuario2 u);
    @POST("isUser")
    Call<Usuario2> esUser(@Body Usuario2 u);
    @POST("newUser")
    Call<Usuario2> signupUser(@Body Usuario2 u);
    @GET("{nombre}/ranking")
    Call<List<Ranking2>> infoUser (@Path("nombre") String nombre);




}
