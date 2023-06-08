package mx.linkom.caseta_los_cabos;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import mx.linkom.caseta_los_cabos.offline.Database.UrisContentProvider;
import mx.linkom.caseta_los_cabos.offline.Global_info;
import mx.linkom.caseta_los_cabos.offline.Servicios.subirFotos;

public class AccesoAutosActivity extends mx.linkom.caseta_los_cabos.Menu {
    Configuracion Conf;
    FirebaseStorage storage;
    StorageReference storageReference;

    LinearLayout rlPermitido, rlDenegado,rlVista;
    TextView  tvMensaje;
    TextView Nombre,Dire,Tipo,Comentarios,Placas;


    ArrayList<String> names;
    JSONArray ja1,ja2,ja3,ja4,ja5,ja6,ja7,ja8,ja9;
    Bitmap bitmap,bitmap2,bitmap3;
    ProgressDialog pd,pd2,pd3;
    int foto;
    String f1,f2,f3;

    LinearLayout espacio1,espacio2,espacio3,espacio4,espacio5,espacio6,espacio7,espacio8,espacio9,espacio10;
    LinearLayout registrar1,registrar2,registrar3,registrar4;
    Button reg1,reg2,reg3,reg4,btn_foto1,btn_foto2,btn_foto3;
    LinearLayout Foto1View,Foto2View,Foto3View;
    LinearLayout Foto1,Foto2,Foto3;
    ImageView view1,view2,view3;
    TextView nombre_foto1,nombre_foto2,nombre_foto3;
    Uri uri_img,uri_img2,uri_img3;
    TextView dato;

    /*ImageView iconoInternet;
    boolean Offline = false;*/
    String nombreImagen1, nombreImagen2, nombreImagen3, rutaImagen1, rutaImagen2, rutaImagen3;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoaccesos);

        Conf = new Configuracion(this);
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        names = new ArrayList<String>();

        reg1 = (Button) findViewById(R.id.reg1);
        reg2 = (Button) findViewById(R.id.reg2);
        reg3 = (Button) findViewById(R.id.reg3);
        reg4 = (Button) findViewById(R.id.reg4);
        btn_foto1 = (Button) findViewById(R.id.btn_foto1);
        btn_foto2 = (Button) findViewById(R.id.btn_foto2);
        btn_foto3 = (Button) findViewById(R.id.btn_foto3);

        nombre_foto1 = (TextView) findViewById(R.id.nombre_foto1);
        nombre_foto2 = (TextView) findViewById(R.id.nombre_foto2);
        nombre_foto3 = (TextView) findViewById(R.id.nombre_foto3);

        view1 = (ImageView) findViewById(R.id.view1);
        view2 = (ImageView) findViewById(R.id.view2);
        view3 = (ImageView) findViewById(R.id.view3);

        espacio1 = (LinearLayout) findViewById(R.id.espacio1);
        espacio2 = (LinearLayout) findViewById(R.id.espacio2);
        espacio3 = (LinearLayout) findViewById(R.id.espacio3);
        espacio4 = (LinearLayout) findViewById(R.id.espacio4);
        espacio5 = (LinearLayout) findViewById(R.id.espacio5);
        espacio6 = (LinearLayout) findViewById(R.id.espacio6);
        espacio7 = (LinearLayout) findViewById(R.id.espacio7);
        espacio8 = (LinearLayout) findViewById(R.id.espacio8);
        espacio9 = (LinearLayout) findViewById(R.id.espacio9);
        espacio10 = (LinearLayout) findViewById(R.id.espacio10);
        registrar1 = (LinearLayout) findViewById(R.id.registrar1);
        registrar2 = (LinearLayout) findViewById(R.id.registrar2);
        registrar3 = (LinearLayout) findViewById(R.id.registrar3);
        registrar4 = (LinearLayout) findViewById(R.id.registrar4);
        Foto1View = (LinearLayout) findViewById(R.id.Foto1View);
        Foto2View = (LinearLayout) findViewById(R.id.Foto2View);
        Foto3View = (LinearLayout) findViewById(R.id.Foto3View);
        Foto1 = (LinearLayout) findViewById(R.id.Foto1);
        Foto2 = (LinearLayout) findViewById(R.id.Foto2);
        Foto3 = (LinearLayout) findViewById(R.id.Foto3);


        Comentarios = (TextView)findViewById(R.id.setComentarios);
        Nombre = (TextView)findViewById(R.id.setNombre);
        Tipo = (TextView)findViewById(R.id.setTipo);
        Dire = (TextView)findViewById(R.id.setDire);
        Placas = (TextView) findViewById(R.id.setPlacas);
        tvMensaje = (TextView) findViewById(R.id.setMensaje);

        rlVista = (LinearLayout) findViewById(R.id.rlVista);
        rlPermitido = (LinearLayout) findViewById(R.id.rlPermitido);
        rlDenegado = (LinearLayout) findViewById(R.id.rlDenegado);


        dato = (TextView) findViewById(R.id.placas_texto);

        /*iconoInternet = (ImageView) findViewById(R.id.iconoInternetAutoAccesos);

        if (Global_info.getINTERNET().equals("Si")){
            iconoInternet.setImageResource(R.drawable.ic_online);
            Offline = false;
        }else {
            iconoInternet.setImageResource(R.drawable.ic_offline);
            Offline = true;
        }

        iconoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Offline){
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesoAutosActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AccesoAutosActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOnline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }
            }
        });*/

        //SI ES ACEPTADO O DENEGAODO
        if(Conf.getST().equals("Aceptado")){
            rlVista.setVisibility(View.VISIBLE);
            rlPermitido.setVisibility(View.GONE);
            rlDenegado.setVisibility(View.GONE);

            menu();

            /*if (Offline){
                menuOffline();
            }else {
                menu();
            }*/
        }else if(Conf.getST().equals("Denegado")){
            rlDenegado.setVisibility(View.VISIBLE);
            rlVista.setVisibility(View.GONE);
            rlPermitido.setVisibility(View.GONE);
            tvMensaje.setText("QR Inexistente");
        }

        pd= new ProgressDialog(this);
        pd.setMessage("Registrando...");

        pd2= new ProgressDialog(this);
        pd2.setMessage("Subiendo Imagen 2...");

        pd3= new ProgressDialog(this);
        pd3.setMessage("Subiendo Imagen 3...");



        reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }
        });

        reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }
        });

        reg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }
        });

        reg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }
        });

        btn_foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=1;
                imgFoto();
            }
        });

        btn_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=2;
                imgFoto2();
            }
        });

        btn_foto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto=3;
                imgFoto3();
            }
        });
        Placas.setFilters(new InputFilter[] { filter,new InputFilter.AllCaps() {
        } });

        if(Conf.getTipoReg().equals("Nada")){
            dato.setText("Placas:");
         }
    }


    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isSpaceChar(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    //ALETORIO
    Random primero = new Random();
    int prime= primero.nextInt(9);

    String [] segundo = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun = (int) Math.round(Math.random() * 25 ) ;

    Random tercero = new Random();
    int tercer= tercero.nextInt(9);

    String [] cuarto = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart = (int) Math.round(Math.random() * 25 ) ;

    String numero_aletorio=prime+segundo[numRandonsegun]+tercer+cuarto[numRandoncuart];

    //ALETORIO2

    Random primero2 = new Random();
    int prime2= primero2.nextInt(9);

    String [] segundo2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun2 = (int) Math.round(Math.random() * 25 ) ;

    Random tercero2 = new Random();
    int tercer2= tercero2.nextInt(9);

    String [] cuarto2 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart2 = (int) Math.round(Math.random() * 25 ) ;

    String numero_aletorio2=prime2+segundo2[numRandonsegun2]+tercer2+cuarto2[numRandoncuart2];

//ALETORIO3

    Random primero3 = new Random();
    int prime3= primero3.nextInt(9);

    String [] segundo3= {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun3 = (int) Math.round(Math.random() * 25 ) ;

    Random tercero3 = new Random();
    int tercer3= tercero3.nextInt(9);

    String [] cuarto3 = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart3 = (int) Math.round(Math.random() * 25 ) ;

    String numero_aletorio3=prime3+segundo3[numRandonsegun3]+tercer3+cuarto3[numRandoncuart3];


    Calendar fecha = Calendar.getInstance();
    int anio = fecha.get(Calendar.YEAR);
    int mes = fecha.get(Calendar.MONTH) + 1;
    int dia = fecha.get(Calendar.DAY_OF_MONTH);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void menuOffline() {
        Log.e("info", "Menu offline");
        try {
            Cursor cursoAppCaseta = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APP_CASETA, null, null, null);

            ja5 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()){
                ja5.put(cursoAppCaseta.getString(0));
                ja5.put(cursoAppCaseta.getString(1));
                ja5.put(cursoAppCaseta.getString(2));
                ja5.put(cursoAppCaseta.getString(3));
                ja5.put(cursoAppCaseta.getString(4));
                ja5.put(cursoAppCaseta.getString(5));
                ja5.put(cursoAppCaseta.getString(6));
                ja5.put(cursoAppCaseta.getString(7));
                ja5.put(cursoAppCaseta.getString(8));
                ja5.put(cursoAppCaseta.getString(9));
                ja5.put(cursoAppCaseta.getString(10));
                ja5.put(cursoAppCaseta.getString(11));
                ja5.put(cursoAppCaseta.getString(12));

                submenuOffline(ja5.getString(0));

            }
            cursoAppCaseta.close();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }

    }

    public void menu() {
        String URL = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/menu.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                response = response.replace("][", ",");
                if (response.length() > 0) {
                    try {
                        ja5 = new JSONArray(response);
                        submenu(ja5.getString(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_residencial", Conf.getResid());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void submenuOffline(final String id_app) {
        Log.e("info", "Sub menu offline");
        try {
            Cursor cursoAppCaseta = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_APPCASETAIMA, null, null, null, null);

            ja6 = new JSONArray();

            if (cursoAppCaseta.moveToFirst()){
                ja6.put(cursoAppCaseta.getString(0));
                ja6.put(cursoAppCaseta.getString(1));
                ja6.put(cursoAppCaseta.getString(2));
                ja6.put(cursoAppCaseta.getString(3));
                ja6.put(cursoAppCaseta.getString(4));
                ja6.put(cursoAppCaseta.getString(5));
                ja6.put(cursoAppCaseta.getString(6));
                ja6.put(cursoAppCaseta.getString(7));
                ja6.put(cursoAppCaseta.getString(8));
                ja6.put(cursoAppCaseta.getString(9));
                ja6.put(cursoAppCaseta.getString(10));

                imagenes();
                AutosOffline();
            }else {
                int $arreglo[]={0};
                try {
                    ja6 = new JSONArray($arreglo);
                    imagenes();
                    AutosOffline();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursoAppCaseta.close();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public void submenu(final String id_app) {
        String URL = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/menu_2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                if(response.equals("error")){
                    int $arreglo[]={0};
                    try {
                        ja6 = new JSONArray($arreglo);
                        imagenes();
                        Autos();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja6 = new JSONArray(response);
                            imagenes();
                            Autos();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_app", id_app.trim());
                params.put("id_residencial", Conf.getResid());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void imagenes(){
//        try {
//
//            if(ja6.getString(0).equals("0") || ja6.getString(3).equals("0")) {

                registrar1.setVisibility(View.VISIBLE);
                espacio1.setVisibility(View.VISIBLE);

                Foto1.setVisibility(View.GONE);
                espacio2.setVisibility(View.GONE);
                Foto1View.setVisibility(View.GONE);
                espacio3.setVisibility(View.GONE);
                registrar2.setVisibility(View.GONE);
                espacio4.setVisibility(View.GONE);
                Foto2.setVisibility(View.GONE);
                espacio5.setVisibility(View.GONE);
                Foto2View.setVisibility(View.GONE);
                espacio6.setVisibility(View.GONE);
                registrar3.setVisibility(View.GONE);
                espacio7.setVisibility(View.GONE);
                Foto3.setVisibility(View.GONE);
                espacio8.setVisibility(View.GONE);
                Foto3View.setVisibility(View.GONE);
                espacio9.setVisibility(View.GONE);
                registrar4.setVisibility(View.GONE);
                espacio10.setVisibility(View.GONE);


//            }else if(ja6.getString(3).equals("1")){
//
//                registrar1.setVisibility(View.GONE);
//                espacio1.setVisibility(View.GONE);
//
//                Foto1.setVisibility(View.VISIBLE);
//                espacio2.setVisibility(View.VISIBLE);
//                nombre_foto1.setVisibility(View.VISIBLE);
//                nombre_foto1.setText(ja6.getString(4)+":");
//
//                Foto1View.setVisibility(View.GONE);
//                espacio3.setVisibility(View.GONE);
//                registrar2.setVisibility(View.GONE);
//                espacio4.setVisibility(View.GONE);
//                Foto2.setVisibility(View.GONE);
//                espacio5.setVisibility(View.GONE);
//                Foto2View.setVisibility(View.GONE);
//                espacio6.setVisibility(View.GONE);
//                registrar3.setVisibility(View.GONE);
//                espacio7.setVisibility(View.GONE);
//                Foto3.setVisibility(View.GONE);
//                espacio8.setVisibility(View.GONE);
//                Foto3View.setVisibility(View.GONE);
//                espacio9.setVisibility(View.GONE);
//                registrar4.setVisibility(View.GONE);
//                espacio10.setVisibility(View.GONE);
//
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    //FOTOS

    public void imgFoto(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);
        nombreImagen1 = "app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
        File foto = new File(getApplication().getExternalFilesDir(null),nombreImagen1);
        rutaImagen1 = foto.getAbsolutePath();
        uri_img= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
        intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img);
        startActivityForResult( intentCaptura, 0);
    }

    public void imgFoto2(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);
        nombreImagen2 = "app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
        File foto = new File(getApplication().getExternalFilesDir(null),nombreImagen2);
        rutaImagen2 = foto.getAbsolutePath();
        uri_img2= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
        intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img2);
        startActivityForResult( intentCaptura, 1);
    }

    public void imgFoto3(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);
        nombreImagen3 = "app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png";
        File foto = new File(getApplication().getExternalFilesDir(null),"accesos3.png");
        rutaImagen3 = foto.getAbsolutePath();
        uri_img3= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
        intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img3);
        startActivityForResult( intentCaptura, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {


                Bitmap bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen1);


                Foto1View.setVisibility(View.VISIBLE);

                view1.setVisibility(View.VISIBLE);
                view1.setImageBitmap(bitmap);
                espacio3.setVisibility(View.VISIBLE);


                try {
                    if(ja6.getString(3).equals("1") && ja6.getString(5).equals("0") && ja6.getString(7).equals("0")) {
                        registrar2.setVisibility(View.VISIBLE);
                        reg2.setVisibility(View.VISIBLE);
                        espacio4.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                    }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")){
                        registrar2.setVisibility(View.GONE);
                        Foto2.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                        espacio6.setVisibility(View.VISIBLE);
                        nombre_foto2.setVisibility(View.VISIBLE);
                        nombre_foto2.setText(ja6.getString(6)+":");
                    }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")) {
                        registrar2.setVisibility(View.GONE);
                        Foto2.setVisibility(View.VISIBLE);
                        espacio5.setVisibility(View.VISIBLE);
                        espacio6.setVisibility(View.VISIBLE);
                        nombre_foto2.setVisibility(View.VISIBLE);
                        nombre_foto2.setText(ja6.getString(6)+":");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            if (requestCode == 1) {


                Bitmap bitmap2 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen2);



                Foto2View.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view2.setImageBitmap(bitmap2);
                espacio6.setVisibility(View.VISIBLE);



                try {
                    if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")) {
                        registrar3.setVisibility(View.VISIBLE);
                        reg3.setVisibility(View.VISIBLE);
                        espacio7.setVisibility(View.VISIBLE);
                    }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")){
                        registrar3.setVisibility(View.GONE);
                        espacio7.setVisibility(View.VISIBLE);
                        espacio8.setVisibility(View.VISIBLE);
                        Foto3.setVisibility(View.VISIBLE);
                        espacio9.setVisibility(View.VISIBLE);
                        nombre_foto3.setVisibility(View.VISIBLE);
                        nombre_foto3.setText(ja6.getString(8)+":");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (requestCode == 2) {


                Bitmap bitmap3 = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen3);


                Foto3View.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view3.setImageBitmap(bitmap3);
                espacio10.setVisibility(View.VISIBLE);



                try {
                    if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")){
                        registrar4.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void AutosOffline(){
        Log.e("info", "Autos offline");

        try {
            String qr =  Conf.getQR();
            String id_residencial = Conf.getResid().trim();

            String[] parametros = {qr, id_residencial};
            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_AUTO, null, null, parametros, null);

            if (cursor.moveToFirst()){
                ja7 = new JSONArray();

                do {
                    ja7.put(cursor.getString(0));
                    ja7.put(cursor.getString(1));
                    ja7.put(cursor.getString(2));
                    ja7.put(cursor.getString(3));
                    ja7.put(cursor.getString(4));
                    ja7.put(cursor.getString(5));
                    ja7.put(cursor.getString(6));
                    ja7.put(cursor.getString(7));
                    ja7.put(cursor.getString(8));
                    ja7.put(cursor.getString(9));
                    ja7.put(cursor.getString(10));
                    ja7.put(cursor.getString(11));
                    ja7.put(cursor.getString(12));
                    ja7.put(cursor.getString(13));

                    UsuarioOffline(ja7.getString(2));

                }while (cursor.moveToNext());
            }

            cursor.close();
        }catch (Exception ex){
            Log.e("Exception ", ex.toString());
        }
    }


    public void Autos(){

        String url = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/auto1.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja7 = new JSONArray(response);
                        Usuario(ja7.getString(2));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", "Id: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("QR", Conf.getQR());
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void UsuarioOffline(final String IdUsu){ //DATOS USUARIO

        Log.e("info", "Usuario offline");

        try {
            String id_residencial = Conf.getResid().trim();
            String id = IdUsu.trim();

            String parametros[] ={id, id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_USUARIO, null, "dts_accesso_autos", parametros, null);

            if (cursor.moveToFirst()){
                ja2 = new JSONArray();

                ja2.put(cursor.getString(0));
                ja2.put(cursor.getString(1));
                ja2.put(cursor.getString(2));
                ja2.put(cursor.getString(3));
                ja2.put(cursor.getString(4));
                ja2.put(cursor.getString(5));
                ja2.put(cursor.getString(6));

                dtlLugarOffline(ja2.getString(0));
            }
            cursor.close();
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }

    }


    public void Usuario(final String IdUsu){ //DATOS USUARIO

        String URL = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/vst_php2.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja2 = new JSONArray(response);
                        dtlLugar(ja2.getString(0));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG","Error: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IdUsu", IdUsu.trim());
                params.put("id_residencial", Conf.getResid().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void dtlLugarOffline(final String idUsuario){

        Log.e("info", "dtl Lugar offline");

        try {
            String id_residencial = Conf.getResid().trim();
            String id = idUsuario.trim();

            String parametros[] ={id_residencial, id};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_LUGAR, null, "dtl_lugar_usuario", parametros, null);

            if (cursor.moveToFirst()){
                ja3 = new JSONArray();
                ja3.put(cursor.getString(0));

                cajonesOffline();
            }
            cursor.close();
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }

    }

    public void dtlLugar(final String idUsuario){
        String URLResidencial = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/vst_php3.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLResidencial, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.equals("error")) {
                    sincasa();
                } else {

                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja3 = new JSONArray(response);
                            cajones();
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", idUsuario.trim());
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void cajonesOffline(){
        Log.e("info", "cajones offline");

        try {
            String id_residencial = Conf.getResid().trim();
            String id = "";

            String parametros[] ={id_residencial, id};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_CAJONES, null, "cajones", parametros, null);

            if (cursor.moveToFirst()){
                ja9 = new JSONArray();
                ja9.put(cursor.getString(0));

                salidasOffline();
            }else {
                int $arreglo[]={0};
                try {
                    ja9 = new JSONArray($arreglo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                salidasOffline();
            }
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }
    }

    public void cajones(){
        String URLResidencial = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/auto5.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLResidencial, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.equals("error")) {
                    int $arreglo[]={0};
                    try {
                        ja9 = new JSONArray($arreglo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    salidas();

                } else {

                    response = response.replace("][", ",");
                    if (response.length() > 0) {
                        try {
                            ja9 = new JSONArray(response);
                            Log.e("TAG", "LKMST: " + ja9);


                            salidas();
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("usuario", ja2.getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void salidasOffline(){
        Log.e("info", "salidas offline");
        try {
            String id_usuario = ja7.getString(2);
            String id_auto  = ja7.getString(0);
            String id_residencial = Conf.getResid().trim();

            String parametros[] = {id_residencial, id_usuario, id_auto};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS_AUTOS, null, "auto2", parametros, null);

            if (cursor.moveToFirst()){
                ja8 = new JSONArray();
                ja8.put(cursor.getString(0));
                ja8.put(cursor.getString(1));
                ja8.put(cursor.getString(2));
                ja8.put(cursor.getString(3));
                ja8.put(cursor.getString(4));
                ja8.put(cursor.getString(5));
                ja8.put(cursor.getString(6));
                ja8.put(cursor.getString(7));
                ja8.put(cursor.getString(8));
                ja8.put(cursor.getString(9));
                ja8.put(cursor.getString(10));
                ja8.put(cursor.getString(11));

                ValidarQR();
            }else {
                int $arreglo[]={0};
                ja8 = new JSONArray($arreglo);
                ValidarQR();
            }

        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }

    }

    public void salidas(){

        String url = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/auto2.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


                        @Override
            public void onResponse(String response) {

                            try {
                    if (response.trim().equals("error")){

                        int $arreglo[]={0};
                        ja8 = new JSONArray($arreglo);
                        ValidarQR();

                    }else{
                        response = response.replace("][",",");
                        ja8 = new JSONArray(response);
                        ValidarQR();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", "Id: " + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id_residencial", Conf.getResid().trim());
                try {
                    params.put("id_usuario", ja7.getString(2));
                    params.put("id_auto", ja7.getString(0));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public void sincasa(){

        rlVista.setVisibility(View.GONE);
        rlPermitido.setVisibility(View.GONE);
        rlDenegado.setVisibility(View.VISIBLE);
        tvMensaje.setText(" No tiene asignada una unidad privativa.");

    }


    public void ValidarQR(){

        try {
            if(ja8.getString(0).equals("0")){

                rlVista.setVisibility(View.GONE);
                rlDenegado.setVisibility(View.GONE);
                rlPermitido.setVisibility(View.VISIBLE);

                String cajon="";
                if(ja9.getString(0).equals("0")){
                    cajon="Ninguno,";
                }else{
                    for (int i = 0; i < ja9.length(); i += 1) {
                        cajon+=ja9.getString(i + 0)+",";
                    }
                }

                Nombre.setText(ja2.getString(1) + " " + ja2.getString(2) + " " + ja2.getString(3));
                Tipo.setText("Auto");
                Dire.setText(ja3.getString(0));
                Placas.setText(ja7.getString(4));
                Comentarios.setText(cajon.substring(0, cajon.length() - 1));
            }else{
                if(ja8.getString(11).equals("1")){
                    rlVista.setVisibility(View.GONE);
                    rlPermitido.setVisibility(View.GONE);
                    rlDenegado.setVisibility(View.VISIBLE);
                    tvMensaje.setText("Esté auto se encuentra dentro del complejo");
                }else if(ja8.getString(11).equals("2")){

                    String cajon="";
                    if(ja9.getString(0).equals("0")){
                        cajon="Ninguno,";
                    }else{
                        for (int i = 0; i < ja9.length(); i += 1) {
                            cajon+=ja9.getString(i + 0)+",";
                        }
                    }

                    rlVista.setVisibility(View.GONE);
                    rlDenegado.setVisibility(View.GONE);
                    rlPermitido.setVisibility(View.VISIBLE);

                    Nombre.setText(ja2.getString(1) + " " + ja2.getString(2) + " " + ja2.getString(3));
                    Tipo.setText("Auto");
                    Dire.setText(ja3.getString(0));
                    Placas.setText(ja7.getString(4));
                    Comentarios.setText(cajon.substring(0, cajon.length() - 1));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void Validacion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoAutosActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea realizar la entrada ?")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface dialog, int id) {
                        pd.show();
                        Registrar();

                        /*if (Offline){
                            RegistrarOffline();
                        }else {
                            pd.show();
                            Registrar();
                        }*/
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                        startActivity(i);
                        finish();

                    }
                }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RegistrarOffline(){

        if(Placas.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else if(Placas.getText().toString().equals(" ")){
            Toast.makeText(getApplicationContext(),"Campo de placas ", Toast.LENGTH_SHORT).show();
        }else if( Placas.getText().toString().equals("N/A")){
            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else{

            try {
                LocalDateTime hoy = LocalDateTime.now();

                int year = hoy.getYear();
                int month = hoy.getMonthValue();
                int day = hoy.getDayOfMonth();
                int hour = hoy.getHour();
                int minute = hoy.getMinute();
                int second =hoy.getSecond();

                String fecha = "";

                //Poner el cero cuando el mes o dia es menor a 10
                if (day < 10 || month < 10){
                    if (month < 10 && day >= 10){
                        fecha = year+"-0"+month+"-"+day;
                    } else if (month >= 10 && day < 10){
                        fecha = year+"-"+month+"-0"+day;
                    }else if (month < 10 && day < 10){
                        fecha = year+"-0"+month+"-0"+day;
                    }
                }else {
                    fecha = year+"-"+month+"-"+day;
                }

                String hora = "";

                if (hour < 10 || minute < 10){
                    if (hour < 10 && minute >=10){
                        hora = "0"+hour+":"+minute;
                    }else if (hour >= 10 && minute < 10){
                        hora = hour+":0"+minute;
                    }else if (hour < 10 && minute < 10){
                        hora = "0"+hour+":0"+minute;
                    }
                }else {
                    hora = hour+":"+minute;
                }

                String segundo = "00";

                if (second < 10){
                    segundo = "0"+second;
                }else {
                    segundo = ""+second;
                }



                ContentValues values = new ContentValues();
                values.put("id_residencial", Conf.getResid().trim());
                values.put("id_usuario", ja7.getString(2));
                values.put("id_auto", ja7.getString(0));
                values.put("entrada_real", fecha+" "+hora+":"+segundo);
                values.put("guardia_de_entrada", Conf.getUsu().trim());
                values.put("salida_real", "0000-00-00 00:00:00");
                values.put("guardia_de_salida", 0);
                values.put("foto1", "");
                values.put("foto2", "");
                values.put("foto3", "");
                values.put("estatus", 1);
                values.put("sqliteEstatus", 1);

                Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_DTL_ENTRADAS_SALIDAS_AUTOS,values);

                String idUri = uri.getLastPathSegment();

                int insertar = Integer.parseInt(idUri);

                if (insertar != -1){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoAutosActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Entrada de auto exitosa en modo offline")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();



                                }
                            }).create().show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoAutosActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Entrada de auto no exitosa en modo offline")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getApplicationContext(),"Entrada de Auto No Registrada", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }

            }catch (Exception ex){
                Log.e("Exception", ex.toString());
            }

        }
    }

    public void Registrar(){

        if(Placas.getText().toString().equals("")){
            pd.dismiss();

            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else if(Placas.getText().toString().equals(" ")){
            pd.dismiss();

            Toast.makeText(getApplicationContext(),"Campo de placas ", Toast.LENGTH_SHORT).show();
        }else if( Placas.getText().toString().equals("N/A")){
            pd.dismiss();

            Toast.makeText(getApplicationContext(),"Campo de placas", Toast.LENGTH_SHORT).show();
        }else{

            String URL = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/auto3.php?bd_name=" + Conf.getBd() + "&bd_user=" + Conf.getBdUsu() + "&bd_pwd=" + Conf.getBdCon();

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response){


                    if(response.equals("error")){

                        pd.dismiss();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoAutosActivity.this);
                        alertDialogBuilder.setTitle("Alerta");
                        alertDialogBuilder
                                .setMessage("Entrada de Auto No Exitosa")
                                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getApplicationContext(),"Entrada de Auto No Registrada", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), EscaneoVisitaActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }).create().show();


                    }else {

                        if (foto == 1){
                            ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/caseta/"+nombreImagen1.trim(), rutaImagen1);
                            Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                        }else if (foto == 2){
                            ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/caseta/"+nombreImagen1.trim(), rutaImagen1);
                            Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                            ContentValues val_img2 =  ValuesImagen(nombreImagen2, Conf.getPin()+"/caseta/"+nombreImagen2.trim(), rutaImagen2);
                            Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);
                        }else if (foto == 3){
                            ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/caseta/"+nombreImagen1.trim(), rutaImagen1);
                            Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

                            ContentValues val_img2 =  ValuesImagen(nombreImagen2, Conf.getPin()+"/caseta/"+nombreImagen2.trim(), rutaImagen2);
                            Uri uri2 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img2);

                            ContentValues val_img3 =  ValuesImagen(nombreImagen3, Conf.getPin()+"/caseta/"+nombreImagen3.trim(), rutaImagen3);
                            Uri uri3 = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img3);
                        }

//                        try {
//                            if(ja6.getString(0).equals("0") || ja6.getString(3).equals("0")) {
                                Terminar();
//                            }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("0") && ja6.getString(7).equals("0")){
//                                upload1();
//                                Terminar();
//                            }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")){
//                                upload1();
//                                upload2();
//                                Terminar();
//                            }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")){
//                                upload1();
//                                upload2();
//                                upload3();
//                                Terminar();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }


                    }


                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();

                    Log.e("TAG","Error: " + error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

//                    try {
//                        if(ja6.getString(0).equals("0") || ja6.getString(3).equals("0")) {
                            f1="";
                            f2="";
                            f3="";
//                        }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("0") && ja6.getString(7).equals("0")){
//                            f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
//                            f2="";
//                            f3="";
//                        }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("0")){
//                            f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
//                            f2="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
//                            f3="";
//                        }else if(ja6.getString(3).equals("1") && ja6.getString(5).equals("1") && ja6.getString(7).equals("1")){
//                            f1="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png";
//                            f2="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png";
//                            f3="app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png";
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    Map<String, String> params = new HashMap<>();

                        params.put("id_residencial", Conf.getResid().trim());
                    try {
                        params.put("id_usuario", ja7.getString(2));
                        params.put("id_auto", ja7.getString(0));

                        params.put("usuario",ja2.getString(1).trim() + " " + ja2.getString(2).trim() + " " + ja2.getString(3).trim());
                        params.put("token", ja2.getString(5).trim());
                        params.put("correo",ja2.getString(6).trim());
                        params.put("nom_residencial",Conf.getNomResi().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                        params.put("guardia", Conf.getUsu().trim());
                        params.put("foto1", nombreImagen1);
                        params.put("foto2", nombreImagen2);
                        params.put("foto3", nombreImagen3);




                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }



//    public void upload1(){
//
//        StorageReference mountainImagesRef = null;
//        mountainImagesRef = storageReference.child(Conf.getPin()+"/caseta/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio+".png");
//
//
//        UploadTask uploadTask = mountainImagesRef.putFile(uri_img);
//
//
//        // Listen for state changes, errors, and completion of the upload.
//        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                pd.show(); // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                //System.out.println("Upload is " + progress + "% done");
//                // Toast.makeText(getApplicationContext(),"Cargando Imagen INE " + progress + "%", Toast.LENGTH_SHORT).show();
//
//            }
//        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(AccesoAutosActivity.this,"Fallado",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                pd.dismiss();
//
//            }
//        });
//    }
//
//    public void upload2(){
//
//
//        StorageReference mountainImagesRef2 = null;
//        mountainImagesRef2 = storageReference.child(Conf.getPin()+"/caseta/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio2+".png");
//
//
//        final UploadTask uploadTask = mountainImagesRef2.putFile(uri_img2);
//
//        // Listen for state changes, errors, and completion of the upload.
//        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                //System.out.println("Upload is " + progress + "% done");
//                //Toast.makeText(getApplicationContext(),"Cargando Imagen PLACA " + progress + "%", Toast.LENGTH_SHORT).show();
//                pd2.show();
//            }
//        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(AccesoAutosActivity.this,"Fallado",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                pd2.dismiss();
//            }
//        });
//
//
//    }
//
//    public void upload3(){
//
//        StorageReference mountainImagesRef3 = null;
//        mountainImagesRef3 = storageReference.child(Conf.getPin()+"/caseta/app"+anio+mes+dia+Placas.getText().toString()+"-"+numero_aletorio3+".png");
//
//
//
//        UploadTask uploadTask = mountainImagesRef3.putFile(uri_img3);
//
//        // Listen for state changes, errors, and completion of the upload.
//        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                //System.out.println("Upload is " + progress + "% done");
//                //Toast.makeText(getApplicationContext(),"Cargando Imagen PLACA " + progress + "%", Toast.LENGTH_SHORT).show();
//                pd3.show();
//            }
//        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(AccesoAutosActivity.this,"Fallado",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                pd3.dismiss();
//
//            }
//        });
//
//
//    }


    public void Terminar() {


        pd.dismiss();


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccesoAutosActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Entrada de Auto Exitosa")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Solo ejecutar si el servicio no se esta ejecutando
                        if (!servicioFotos()){
                            Intent cargarFotos = new Intent(AccesoAutosActivity.this, subirFotos.class);
                            startService(cargarFotos);
                        }

                        Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                        startActivity(i);
                        finish();



                    }
                }).create().show();



    }


    public ContentValues ValuesImagen(String nombre, String rutaFirebase, String rutaDispositivo){
        ContentValues values = new ContentValues();
        values.put("titulo", nombre);
        values.put("direccionFirebase", rutaFirebase);
        values.put("rutaDispositivo", rutaDispositivo);
        return values;
    }

    //Método para saber si es que el servicio ya se esta ejecutando
    public boolean servicioFotos(){
        //Obtiene los servicios que se estan ejecutando
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        //Se recorren todos los servicios obtnidos para saber si el servicio creado ya se esta ejecutando
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(subirFotos.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
        startActivity(intent);
        finish();
    }
}
