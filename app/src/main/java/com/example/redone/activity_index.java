package com.example.redone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redone.models.RegistroUserFicha;
import com.example.redone.models.Codigos;
import com.example.redone.models.Hotspot;
import com.example.redone.models.HotspotApp;
import com.example.redone.models.User;
import com.example.redone.service.HotspotServiceApp;
import com.example.redone.service.HotspotService;
import com.example.redone.service.UserService;
import com.example.redone.utils.config;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_index extends AppCompatActivity {

    private TextView txtEdit_idperfil;
    private TextView txtEdit_nombreperfil;
    private TextView txtPropietarioCuenta;
    private TextView btnDetallesUser;
    private ImageView imageAddCode;

    //Controles para dialogo
    private EditText codigo_add ;
    private ImageView imgClose;
    private Button btnRegistrarCodigo;
    private Button btnReclamarCodigo;

    private TextView txtCongratulations;
    private TextView txtRegaloObtenido;

    public RegistroUserFicha fichaRegistrada= null;

            //Creamos variable bundle que nos recoge variable publicas de ota clase
    Bundle bundle;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_index);

        TableLayout tableLayout;
        //Inicializacion de variables
        tableLayout = findViewById(R.id.table2);
        txtEdit_idperfil = findViewById(R.id.idPerfil);
        txtEdit_nombreperfil = findViewById(R.id.nombrePerfil);
        btnDetallesUser = findViewById(R.id.btnDetailsAccount);
        txtPropietarioCuenta = findViewById(R.id.propietarioCuenta);
        imageAddCode = findViewById(R.id.imgAddCode);
        btnReclamarCodigo = findViewById(R.id.reclamarcodigo);
        txtCongratulations = findViewById(R.id.txtCongratulations);
        txtRegaloObtenido = findViewById(R.id.regaloObtenido);

        //Obtenemos los extras(variables que se han pasado a esta clase
        bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        btnDetallesUser.setText(bundle.getString("nombre").substring(0,1).toUpperCase());
        txtPropietarioCuenta.setText(bundle.getString("nombre"));

        llenarEtiquetas();

        txtCongratulations.setVisibility(View.GONE);
        txtRegaloObtenido.setVisibility(View.GONE);

        //Mostrando tabla de registros
        createHeader(tableLayout);
        findCodigos(new Long(id),tableLayout);

        tableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 1; i <= tableLayout.getChildCount() - 1; i++) {
                    TableRow tablerow = (TableRow) tableLayout.getChildAt(i);
                    TableRow rwD = findViewById(tablerow.getId());
                    rwD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView textoCeldaId = (TextView) rwD.getChildAt(0);
                            TextView textoCeldaPerfil = (TextView) rwD.getChildAt(1);
                            TextView textoCeldaEstatus = (TextView) rwD.getChildAt(3);
                            if (textoCeldaEstatus.getText().toString().toUpperCase().contains("DISPONI")) {
                                txtEdit_idperfil.setText(textoCeldaId.getText().toString());
                                txtEdit_nombreperfil.setText(textoCeldaPerfil.getText().toString());
                            } else {
                                Toast.makeText(activity_index.this, "Estatus: Bloqueado", Toast.LENGTH_SHORT).show();
                                txtEdit_idperfil.setText("");
                                txtEdit_nombreperfil.setText("");
                            }
                        }
                    });
                }
            }
        });

        imageAddCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(tableLayout);
            }
        });

        btnReclamarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRegaloCodigo(new Long(txtEdit_idperfil.getText().toString()));
            }
        });
    }

    //Creamos encabezados para la tabla
    private void createHeader(TableLayout table) {
        String[] header = {"PERFIL", "CODIGOS", "TOTAL", "ESTATUS"};
        int indexC = 0;
        TableRow rowHeader = new TableRow(getApplicationContext());
        while (indexC < header.length) {
            TextView txtCell = new TextView(getApplicationContext());
            txtCell.setText(header[indexC++]);
            txtCell.setTextColor(Color.parseColor("#FF000000"));
            txtCell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txtCell.setTypeface(null, Typeface.BOLD);
            rowHeader.addView(txtCell, newTableRowParams());
            Resources resource = getApplicationContext().getResources();
            rowHeader.setBackgroundColor(resource.getColor(R.color.blue));
        }
        table.addView(rowHeader);
    }

    //Parametros para las columnas
    private TableRow.LayoutParams newTableRowParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(10, 10, 10, 10);
        params.weight = 1;
        return params;
    }

    public List<RegistroUserFicha> findCodigos(Long idUsuario, TableLayout table) {
        List<RegistroUserFicha> lista = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        HotspotServiceApp hotspotServiceApp = retrofit.create(HotspotServiceApp.class);
        Call<Codigos> call = hotspotServiceApp.findCodigosByIdUser(idUsuario);
        call.enqueue(new Callback<Codigos>() {
            @Override
            public void onResponse(Call<Codigos> call, @NonNull Response<Codigos> response) {
                if (response.isSuccessful()) {
                    //Si la tabla ya contiene datos la vaciamos
                    int count = table.getChildCount();
                      for (int c = 1; c <= count-1; c++) {
                            View child = table.getChildAt(c);
                            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
                        }
                      //llenamos la tabla
                        for (int i = 0; i < response.body().getFichas().size(); i++) {
                        RegistroUserFicha codigo = response.body().getFichas().get(i);
                        lista.add(codigo);
                    }
                }
                getClientes(lista,table);
            }

            @Override
            public void onFailure(Call<Codigos> call, Throwable t) {
                System.out.println("in failure");
            }

        });
        return lista;
    }

    private void getClientes(List<RegistroUserFicha> lista_cod, TableLayout table) {
        ArrayList<String[]> rows = new ArrayList<>();
        if (lista_cod != null) {
            for (int x = 0; x < lista_cod.size(); x++) {
                RegistroUserFicha cod = lista_cod.get(x);
                //4 String porque es la cantidad de columnas
                String[] row = new String[4];
                row[0] = cod.getIdperfil().toString();
                row[1] = cod.getFichas();
                row[2] = cod.getTotal().toString();
                row[3] = cod.getEstatus().toUpperCase();

                rows.add(row);
            }
            llenemosTabla(table, rows);
        }
    }

    //metodo donde llenamos la tabla
    private void llenemosTabla(TableLayout table, ArrayList<String[]> datos) {
        //Verifiquemos si la tabla esta llena
        int total_fichas = 0;
        for (int i = 1; i <= datos.size(); i++) {
            TableRow rowContenido = new TableRow(getApplicationContext());
            //4 String porque es la cantidad de columnas
            for (int y = 0; y < 4; y++) {
                TextView txtCell = new TextView(getApplicationContext());
                txtCell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                String[] columns = datos.get(i - 1);
                String info = (y < columns.length) ? columns[y] : "";

                txtCell.setText(info);
                Resources resource = getApplicationContext().getResources();
                txtCell.setTextColor(resource.getColor(R.color.white));
                total_fichas = Integer.parseInt(columns[2]);
                if (total_fichas >= 5) {
                    rowContenido.setBackgroundColor(resource.getColor(R.color.green));
                } else {
                    rowContenido.setBackgroundColor(resource.getColor(R.color.red));
                }
                rowContenido.addView(txtCell, newTableRowParams());
                rowContenido.setId(Integer.parseInt(columns[0]));
            }
            table.addView(rowContenido);
        }
    }

    public void openDialog(TableLayout table){
        AlertDialog.Builder alerta;
        alerta = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        LayoutInflater inflater = getLayoutInflater();
        View views = inflater.inflate(R.layout.activity_register_code,null);
        codigo_add = views.findViewById(R.id.txtCodigoView);
        imgClose = views.findViewById(R.id.imgCloseView);
        btnRegistrarCodigo = views.findViewById(R.id.btnRegistrarCodigoView);

        alerta.setView(views);
        alerta.setCancelable(false);

        AlertDialog dialog = alerta.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnRegistrarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
                HotspotService hotspotService = retrofit.create(HotspotService.class);
                Call<Hotspot> call =  hotspotService.findHotspotById(codigo_add.getText().toString());
                call.enqueue(new Callback<Hotspot>() {
                    @Override
                    public void onResponse(Call<Hotspot> call, Response<Hotspot> response) {
                        if(response.isSuccessful()){
                            Hotspot ficha = response.body();
                            if(ficha != null) {
                                if(ficha.getDisponible()){
                                    goToRegisterCodes(new Long(id),codigo_add.getText().toString(),response.body().getIdperfil().longValue(),table);
                                }else{
                                    Toast.makeText(activity_index.this,"Ficha registrada anteriormente", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(activity_index.this,"Error no se encontro en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Hotspot> call, Throwable t) {
                        Toast.makeText(activity_index.this,"Codigo no existe en la base de datos....", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public void goToRegisterCodes(Long idusuario,String codigo,Long perfil,TableLayout table){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        HotspotServiceApp hotspotServiceApp = retrofit.create(HotspotServiceApp.class);
        Call<RegistroUserFicha> call = hotspotServiceApp.saveCodigo(idusuario,perfil,codigo);
        call.enqueue(new Callback<RegistroUserFicha>() {
            @Override
            public void onResponse(Call<RegistroUserFicha> call, Response<RegistroUserFicha> response) {
                if(response.isSuccessful()){
                        codigoUsado(codigo,false);
                        Toast.makeText(activity_index.this,"Codigo registrado con exito",Toast.LENGTH_SHORT).show();
                        findCodigos(new Long(id),table);
                        codigo_add.setText("");
                }
            }
            @Override
            public void onFailure(Call<RegistroUserFicha> call, Throwable t) {
                Toast.makeText(activity_index.this,"Error verifique su conexion a internet",Toast.LENGTH_SHORT).show();
            }
        });

    }

    //programando el boton de obtenerRegalo
    public void getRegaloCodigo(Long idPerfil){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        HotspotServiceApp  hotspotService = retrofit.create(HotspotServiceApp.class);
        Call<HotspotApp> call = hotspotService.findCodigoByIdPerfil(idPerfil);
        call.enqueue(new Callback<HotspotApp>() {
            @Override
            public void onResponse(Call<HotspotApp> call, Response<HotspotApp> response) {
                if(response.isSuccessful()){
                    HotspotApp hot = response.body();
                    if(hot.getDisponible()){
                        txtCongratulations.setVisibility(View.VISIBLE);
                        txtRegaloObtenido.setVisibility(View.VISIBLE);
                        txtRegaloObtenido.setText(hot.getFicha());
                        RegistroUserFicha registro = modificarRegistrosPorPerfil(new Long(id),idPerfil);
                        modificarFichasAppPorPerfil(txtRegaloObtenido.getText().toString(),false);

                    }else{
                        Toast.makeText(activity_index.this,"No hay fichas de regalo disponibles", Toast.LENGTH_SHORT).show();
                    }

                }else{
                   Toast.makeText(activity_index.this,"No se pudo obtener ficha de regalo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HotspotApp> call, Throwable t) {
                Toast.makeText(activity_index.this,"Error, no se pudo obtener codigo de regalo", Toast.LENGTH_SHORT).show();
                txtCongratulations.setVisibility(View.GONE);
                txtRegaloObtenido.setVisibility(View.GONE);
            }
        });
    }


    //Controlemos boton retroceso
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if(keyCode == event.KEYCODE_BACK){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setCancelable(false);
           builder.setMessage("¿Cerrar aplicacion?")
                   .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           Intent intent = new Intent(Intent.ACTION_MAIN);
                           intent.addCategory(Intent.CATEGORY_HOME);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                           returnLogin();
                       }
                   })
                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.dismiss();
                       }
                   });


           builder.show();
       }
       return super.onKeyDown(keyCode,event);
    }


    public void llenarEtiquetas(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        UserService userService = retrofit.create(UserService.class);
        Call<User> call = userService.find(String.valueOf(id));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                           txtPropietarioCuenta.setText(response.body().getNombre_completo());
                            btnDetallesUser.setText(response.body().getNombre_completo().substring(0,1).toUpperCase());
                        }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void returnLogin(){
        Intent intent = new Intent(activity_index.this,activity_login.class);
        startActivity(intent);
    }

   //Metodo para modificar estatus del codigo(Fichas usadas)
    private void codigoUsado(String codigo,boolean estatus){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        HotspotService hotspotService = retrofit.create(HotspotService.class);
        Call<Hotspot> call = hotspotService.modificarPorId(codigo,estatus);
        call.enqueue(new Callback<Hotspot>() {
            @Override
            public void onResponse(Call<Hotspot> call, Response<Hotspot> response) {
                if(response.isSuccessful()){
                    System.out.println(response.body().getDisponible());
                }
            }

            @Override
            public void onFailure(Call<Hotspot> call, Throwable t) {
                System.out.println("Error al modificar estatus");
            }
        });
    }

    private RegistroUserFicha modificarRegistrosPorPerfil(Long idUser, Long idPerfil){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        HotspotServiceApp hotspotServiceApp = retrofit.create(HotspotServiceApp.class);
        Call<RegistroUserFicha> call = hotspotServiceApp.modificarPorIdPerfil(idUser,idPerfil);
        call.enqueue(new Callback<RegistroUserFicha>() {
            @Override
            public void onResponse(Call<RegistroUserFicha> call, Response<RegistroUserFicha> response) {
                if(response.isSuccessful()){
                     fichaRegistrada = response.body();
                }
            }

            @Override
            public void onFailure(Call<RegistroUserFicha> call, Throwable t) {

            }
        });

        return fichaRegistrada;
    }

    //Mofiicacion de fichas que son para regalo en el app por id cambiarndo estatus
    private HotspotApp modificarFichasAppPorPerfil(String id,boolean estatus){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(new config().host).addConverterFactory(GsonConverterFactory.create()).build();
        HotspotServiceApp hotspotServiceApp = retrofit.create(HotspotServiceApp.class);
        Call<HotspotApp>call = hotspotServiceApp.updateEstatusPorFicha(id,estatus);
        call.enqueue(new Callback<HotspotApp>() {
            @Override
            public void onResponse(Call<HotspotApp> call, Response<HotspotApp> response) {
                if(response.isSuccessful()){
                    HotspotApp hotspotApp=response.body();
                }
            }

            @Override
            public void onFailure(Call<HotspotApp> call, Throwable t) {

            }
        });

        return null;
    }

   }