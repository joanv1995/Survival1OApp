package com.example.usuario.pruebaretrofit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.activities.Mapa.MapaActivity;
import com.example.usuario.pruebaretrofit.model.Usuario;
import com.example.usuario.pruebaretrofit.service.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText userName;
    String playerName;
    EditText password;
    String pass;
    Button login;
    Button signup;
    Retrofit retrofit;
    Usuario usuarioCargado;
    Button button;
    private static final String URL_BASE = "http://10.193.222.188:8080/1O-survival/game/"; ///nuestra api virtual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        userName = (EditText) findViewById(R.id.usuarioname);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(it);
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setTypeface(null, Typeface.NORMAL);
                userName.setTextColor(Color.parseColor("#000000"));//negro
                userName.setText("");

            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password.setTypeface(null, Typeface.NORMAL);
                password.setTextColor(Color.parseColor("#000000"));
                password.setText("");
                password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
            }
        });
        login.setBackgroundResource(R.drawable.buttonstatelogin);
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               playerName = userName.getText().toString();
               pass = password.getText().toString();
               usuarioCargado = new Usuario("Joan Valverde","admin","joanv1995@gmail.com");
               //connectApiService();
               Intent in = new Intent(LoginActivity.this,PerfilActivity.class);
               in.putExtra("jugador",usuarioCargado);

               startActivity(in);

           }
       });

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapa = new Intent(LoginActivity.this, MapaActivity.class);
                startActivity(mapa);
            }
        });

    }



    public void connectApiService() {
        if (this.retrofit == null) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RestClient service = retrofit.create(RestClient.class);

        Call<Usuario> call = service.loginUser(playerName,pass);
        try {

            Callback<Usuario> cb = new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    usuarioCargado = response.body();

                    //if(!p.getPassword().equals(pass))
                    //Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                    //else
                    EntryUserInterface();



                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    //Log.e("players", t.toString());
                }
            };
            Log.i("players", "abans enqueue");
            call.enqueue(cb);

        } catch (Exception er) {
            Log.e("error", er.getMessage());
        }
    }
        public void EntryUserInterface(){
            if(usuarioCargado!=null)
            {
                Toast.makeText(this,"Usuario "+usuarioCargado.getNombre()+"ha sido logueado correctamente", Toast.LENGTH_SHORT).show();

              //  Intent in = new Intent(this, PerfilActivity.class);
               //in.putExtra("jugador",usuarioCargado);

              //startActivity(in);
            }
            else{
                Toast.makeText(this,"Login incorrecto", Toast.LENGTH_SHORT).show();


            }

    }


}

 class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }
        public char charAt(int index) {
            return '*'; // This is the important part
        }
        public int length() {
            return mSource.length(); // Return default
        }
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}

