package com.example.redone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.redone.models.Hotspot;
import com.example.redone.models.User;
import com.example.redone.models.UserDetail;
import com.example.redone.service.HotspotService;
import com.example.redone.service.IUserDetailsService;
import com.example.redone.utils.config;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class activity_registrar_persona extends AppCompatActivity {
    Button registrarCuenta;
    TextInputEditText txtNombre;
    TextInputEditText txtTelefono;
    TextInputEditText txtUsuario;
    TextInputEditText txtContraseña;
    TextInputEditText txtCodigoReferido;
    boolean bandera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_person);
        registrarCuenta = findViewById(R.id.btnRegistrardatos);
        txtNombre = findViewById(R.id.txtNombreCompleto);
        txtTelefono = findViewById(R.id.txtTelefonoContacto);
        txtUsuario = findViewById(R.id.txtUsuarioRegistro);
        txtContraseña = findViewById(R.id.txtContraseñaRegistro);
        txtCodigoReferido = findViewById(R.id.txtCodigoReferido);

        registrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Llenemos un modelo usuario
                User usuarioModelo = new User();
                UserDetail detalleUsuario = new UserDetail();
                if(!txtNombre.getText().equals("")){
                    if(!txtUsuario.getText().equals("")){
                        if(!txtContraseña.getText().equals("")){
                            if(txtContraseña.getText().length() >= 6){
                                usuarioModelo.setIdusuario(randomId());
                                usuarioModelo.setNombre_completo(txtNombre.getText().toString().trim());
                                usuarioModelo.setTelefono(txtTelefono.getText().toString().trim());
                                detalleUsuario.setIdusuario(usuarioModelo.getIdusuario());
                                detalleUsuario.setUsuario(txtUsuario.getText().toString().trim());
                                detalleUsuario.setPassword(txtContraseña.getText().toString().trim());
                                goToRegisterAccount(usuarioModelo,detalleUsuario);
                            }else{
                                Toast.makeText(activity_registrar_persona.this,"La contraseña de ser al menos 6 caracteres",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity_registrar_persona.this,"Contraseña requerida", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity_registrar_persona.this,"Usuario es requerido", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity_registrar_persona.this,"Nombre es requerido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToRegisterAccount(User modelo,UserDetail detalle){
        JSONObject js = new JSONObject();
        try {
            js.put("idusuario",modelo.getIdusuario());
            js.put("nombre_completo",modelo.getNombre_completo());
            js.put("fecha_creacion",modelo.getFecha_creacion());
            js.put("telefono",modelo.getTelefono());
        } catch (JSONException e) {
            System.out.println("Error al crear json usuario");
        }

        String url = new config().host+"/api/user/guardar";
        //Buscamos si el usuario aun no existe en la base de datos
        if(buscarDetalle(detalle) == false){
            // Make request for JSONObject
            JsonObjectRequest postRequest = new JsonObjectRequest(
                    Request.Method.POST, url, js,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response.toString());
                            Toast.makeText(activity_registrar_persona.this,"Tu cuenta ha sido creada con exito",Toast.LENGTH_SHORT).show();
                            if(json.getLong("idusuario")>0){
                                detalle.setIdusuario((int) json.getLong("idusuario"));
                                registrarDetalle(detalle);
                            }

                        }catch(JSONException e){
                            Toast.makeText(activity_registrar_persona.this,"Error verifique su conexion a internet",Toast.LENGTH_SHORT).show();
                        }
                    }, error -> Toast.makeText(activity_registrar_persona.this,"Erro al guardar usuario",Toast.LENGTH_SHORT).show()) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            //Ejecutamos el metodo
            Volley.newRequestQueue(this).add(postRequest);
        }else{
            Toast.makeText(activity_registrar_persona.this,"Nombre de usuario ya existe",Toast.LENGTH_SHORT).show();
        }



    }

    public boolean buscarDetalle(UserDetail detalle){
        bandera = false;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        IUserDetailsService userDetailService = retrofit.create(IUserDetailsService.class);
        retrofit2.Call<UserDetail> call =  userDetailService.findUsername(detalle.getUsuario());
        call.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, retrofit2.Response<UserDetail> response) {
                if(response.isSuccessful()){
                    if(response.body().getUsuario()!= null){
                        bandera = true;
                    }
                }
            }
            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
               // Toast.makeText(activity_registrar_persona.this,"Sin acceso a internet 1",Toast.LENGTH_SHORT).show();
            }
        });

        return bandera;
    }

    public void registrarDetalle(UserDetail detalle){
        JSONObject js = new JSONObject();
        bandera = false;
        try {
            js.put("idusuario",detalle.getIdusuario());
            js.put("usuario",detalle.getUsuario());
            js.put("password",detalle.getPassword());
        } catch (JSONException e) {
            System.out.println("Error al crear json detalles usuario");
        }

        String url = new config().host+"/api/user/details/guardar";

        // Make request for JSONObject
        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST, url, js,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response.toString());
                        limpiarFormulario();
                        bandera = true;
                    }catch(JSONException e){
                        //Toast.makeText(activity_registrar_persona.this,"Error verifique su conexion a internet",Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(activity_registrar_persona.this,"",Toast.LENGTH_SHORT).show()) {
               @Override
               public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    public int randomId(){
        int numero = (int) (Math.random() * 999 + 10);
        return numero;
    }

    public String randomCodigoReferido(){
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        int length = 6;
        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();
        if (length < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }



    public void limpiarFormulario(){
        txtNombre.setText("");
        txtTelefono.setText("");
        txtUsuario.setText("");
        txtContraseña.setText("");
        txtCodigoReferido.setText("");
    }

}