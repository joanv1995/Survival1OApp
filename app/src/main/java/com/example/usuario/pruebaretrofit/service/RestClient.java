package com.example.usuario.pruebaretrofit.service;

import com.example.usuario.pruebaretrofit.model.Usuario;

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

    @GET("listaUsuarios")
    Call<List<Usuario>> getListaUsuarios();
    @GET("player/{nomPlayer}/{password}")
    Call<Usuario> loginUser(@Path("nomPlayer") String nomPlayer, @Path("password") String password);
    @POST("newUser")
    Call<Integer> signupUser(@Body Usuario u);

}
