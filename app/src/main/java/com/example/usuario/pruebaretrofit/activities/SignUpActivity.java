package com.example.usuario.pruebaretrofit.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usuario.pruebaretrofit.R;
import com.example.usuario.pruebaretrofit.model.Usuario;
import com.example.usuario.pruebaretrofit.service.RestClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
Button sign;
String name;
String pass;
String passRep;
String email;
Usuario user;

EditText nombre;
EditText pass1;
EditText pass2;
EditText mail;
    private static final String URL_BASE = "http://10.193.222.188:8080/1O-survival/game/"; ///nuestra api virtual
    private Retrofit retrofit=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nombre = (EditText) findViewById(R.id.namee);
        pass1 = (EditText) findViewById(R.id.pass11);
        pass2 = (EditText) findViewById(R.id.pass22);
        mail = (EditText) findViewById(R.id.mail);
        sign= (Button) findViewById(R.id.signup);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nombre.getText().toString();
                pass = pass1.getText().toString();
                passRep = pass2.getText().toString();
                email = mail.getText().toString();
                if(pass.equals(passRep)) {


                    connectService();
                }
        else{
                Log.i("user","Falló");
                Toast.makeText(getApplicationContext(),"El campo 'Contraseña' y 'Repetir contraseña' deben de ser iguales",Toast.LENGTH_LONG).show();

            }
            }
        });

    }
    public void connectService(){
        if (this.retrofit == null) {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RestClient service = retrofit.create(RestClient.class);


            user = new Usuario(name, pass, email);
            Call<ResponseBody> call = service.signupUser(user);
            try {
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                     int i=   response.code();
                     String s = response.body().toString();
                     Log.i("user","INSERTADO");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("user","NO INSERTADO");

                    }
                });



            }
            catch (Exception e){
                throw e;
            }





    }
}
