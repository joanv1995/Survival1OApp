package com.example.usuario.pruebaretrofit.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.usuario.pruebaretrofit.R;
//import com.example.usuario.pruebaretrofit.adapter.PlayerListAdapter;
import com.example.usuario.pruebaretrofit.model.Usuario;
import com.example.usuario.pruebaretrofit.service.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InterfazUsuarioActivity extends AppCompatActivity {
    private static final String URL_BASE = "http://10.193.222.188:8080/1O-survival/game/"; ///nuestra api virtual
    private Retrofit retrofit=null;
    private RecyclerView playerList = null;
    //private PlayerListAdapter pla = null;
    private List<Usuario> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intefaz_usuario_activity);
        playerList = (RecyclerView)findViewById(R.id.lista);
        playerList.setHasFixedSize(true);
        playerList.setLayoutManager(new LinearLayoutManager(this));
       // playerList.setAdapter(pla);
       // connectReposService();

    }
    /*public void connectReposService () {
        if (this.retrofit == null) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RestClient service = retrofit.create(RestClient.class);

        Call<List<Usuario>> call = service.getListaUsuarios();
        try {

            Callback<List<Usuario>> cb = new Callback<List<Usuario>>() {
                             @Override
                             public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                                 players = response.body();
                                // pla = new PlayerListAdapter(players, R.layout.list_item_repo_layout, getApplicationContext());
                                // playerList.setAdapter(pla);

                                 Log.d("players", "nombre de jugadors: " + players.size());
                             }

                             @Override
                             public void onFailure(Call<List<Usuario>> call, Throwable t) {
                                 Log.e("players", t.toString());
                             }
                         };
            Log.i("players", "abans enqueue");
            call.enqueue(cb);

        }
        catch (Exception er){
           Log.e("error", er.getMessage());
        }
    }*/
}
