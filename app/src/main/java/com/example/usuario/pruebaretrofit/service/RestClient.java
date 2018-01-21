package com.example.usuario.pruebaretrofit.service;

import com.example.usuario.pruebaretrofit.model.ListaUsuariosResponse;
import com.example.usuario.pruebaretrofit.model.Usuario2;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
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

}
