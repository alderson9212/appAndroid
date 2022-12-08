package com.example.redone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.redone.models.User;
import com.example.redone.models.UserDetail;
import com.example.redone.service.IUserDetailsService;
import com.example.redone.service.UserService;
import com.example.redone.utils.config;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_login extends AppCompatActivity {

    private EditText txtInputUser;
    private EditText txtInputPass;
    private Button submitLogin;

    private Button btnCrearCuenta;
    private Button btnCambiarContraseña;
    private ProgressBar progres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submitLogin = findViewById(R.id.btnLoginAccount);
        txtInputUser = findViewById(R.id.txtUserLogin);
        txtInputPass = findViewById(R.id.txtPasswordLogin);
        btnCrearCuenta = findViewById(R.id.btnCrearCuentaLogin);
        btnCambiarContraseña = findViewById(R.id.btnOlvidoContraseñaLogin);
        progres = findViewById(R.id.progres1);


        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progres.setVisibility(view.getVisibility());
                Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
                IUserDetailsService userServiceDetails = retrofit.create(IUserDetailsService.class);
                UserDetail u = new UserDetail();
                if(txtInputUser.getText().toString().length()>0 && txtInputPass.getText().toString().length() >0){
                    Call<UserDetail> call = userServiceDetails.findUsernamePass(txtInputUser.getText().toString(),txtInputPass.getText().toString());
                    call.enqueue(new Callback<UserDetail>() {
                        @Override
                        public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                               try {
                                if(response.isSuccessful()) {
                                    UserDetail u = response.body();
                                    if (u != null) {
                                       startTrue(u.getIdusuario());
                                    }
                                }else{
                                    progres.setVisibility(View.GONE);
                                    Toast.makeText(activity_login.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                if(u == null){
                                    progres.setProgress(3000);
                                    progres.setVisibility(View.GONE);
                                    Toast.makeText(activity_login.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                                }else{
                                    progres.setVisibility(View.GONE);
                                    Toast.makeText(activity_login.this,"Error 500 servidor caido",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<UserDetail> call, Throwable t) {
                            progres.setVisibility(view.GONE);
                            Toast.makeText(activity_login.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                        }
                    });
                }  else{
                    progres.setVisibility(view.GONE);
                    Toast.makeText(activity_login.this,"Usuario y contraseña requeridos",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta();
            }
        });

        btnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }



    private void crearCuenta(){
        Intent intent = new Intent(activity_login.this, activity_registrar_persona.class);
        startActivity(intent);
    }

    private void startTrue(int id){
        Intent intent = new Intent(activity_login.this, activity_index.class);
        intent.putExtra("id",id);
        intent.putExtra("nombre","sdasda");
        startActivity(intent);
        txtInputUser.setText("");
        txtInputPass.setText("");
    }

    private void resetPassword(){
        Intent intent = new Intent(activity_login.this,activity_resetear_contraseña.class);
        startActivity(intent);
    }


}
