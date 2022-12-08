package com.example.redone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.redone.service.UserService;
import com.example.redone.utils.config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_resetear_contraseña extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtNewPass ;
    private EditText txtConfirmNewPass;
    private Button btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        txtNewPass = findViewById(R.id.txtnewPassChangePass);
        txtConfirmNewPass = findViewById(R.id.txtConfirmNewPassChangePass);
        btnChangePass = findViewById(R.id.btnResetPassword);
        txtUsername = findViewById(R.id.txtUsernameChangePass);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtNewPass.getText().length()>=6 &&  txtConfirmNewPass.getText().length()>=6 ) {
                    if (txtNewPass.getText().toString().equals(txtConfirmNewPass.getText().toString())) {
                        changePassword(txtUsername.getText().toString(), txtNewPass.getText().toString());
                    } else {
                        Toast.makeText(activity_resetear_contraseña.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity_resetear_contraseña.this,"La contraseña debe ser minima 6 caracteres", Toast.LENGTH_SHORT).show();;
                }
            }
        });
    }



    public void changePassword(String usuario,String pass){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        UserService userServices = retrofit.create(UserService.class);
        Call call = userServices.updateUserByUsuario(usuario,pass);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.body().toString().contains("true")){
                    Toast.makeText(activity_resetear_contraseña.this,"Datos actualizados con exito", Toast.LENGTH_SHORT).show();
                    txtUsername.setText("");
                    txtNewPass.setText("");
                    txtConfirmNewPass.setText("");
                }else{
                    Toast.makeText(activity_resetear_contraseña.this,"No se pudo verificar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }




}
