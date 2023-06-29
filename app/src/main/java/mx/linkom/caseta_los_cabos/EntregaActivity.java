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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import mx.linkom.caseta_los_cabos.detectPlaca.DetectarPlaca;
import mx.linkom.caseta_los_cabos.offline.Database.UrisContentProvider;
import mx.linkom.caseta_los_cabos.offline.Global_info;
import mx.linkom.caseta_los_cabos.offline.Servicios.subirFotos;

public class EntregaActivity extends mx.linkom.caseta_los_cabos.Menu {

    TextView setNumero,setComent,setPara;
    ImageView foto_recep,viewFoto;
    Button foto,btnRegistrar;
    LinearLayout View,espacio,espacio2,BtnReg,rlVista,rlPermitido;
    ProgressDialog pd,pd2;
    JSONArray ja1,ja2;
    private Configuracion Conf;
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;
    String fotos;
    Uri uri_img;

    /*ImageView iconoInternet;
    boolean Offline = false;*/
    TextView txtFoto;

    String rutaImagen1="", nombreImagen1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega);

        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        Conf = new Configuracion(this);

        setPara = (TextView) findViewById(R.id.setPara);
        setNumero = (TextView) findViewById(R.id.setNumero);
        setComent = (TextView) findViewById(R.id.setComent);
        foto_recep = (ImageView) findViewById(R.id.foto_recep);
        viewFoto = (ImageView) findViewById(R.id.viewFoto);
        foto = (Button) findViewById(R.id.foto);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        rlVista = (LinearLayout) findViewById(R.id.rlVista);
        rlPermitido = (LinearLayout) findViewById(R.id.rlPermitido);
        View = (LinearLayout) findViewById(R.id.View);
        espacio = (LinearLayout) findViewById(R.id.espacio);
        espacio2 = (LinearLayout) findViewById(R.id.espacio2);
        BtnReg = (LinearLayout) findViewById(R.id.BtnReg);

        /*iconoInternet = (ImageView) findViewById(R.id.iconoInternetEntrega);*/
        //resp_foto = (TextView) findViewById(R.id.resp_foto_entrega);
        txtFoto = (TextView) findViewById(R.id.txtFotoEntrega);

        txtFoto.setText(Global_info.getTexto1Imagenes());

        /*if (Global_info.getINTERNET().equals("Si")){
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
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EntregaActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(EntregaActivity.this);
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

        pd= new ProgressDialog(this);
        pd.setMessage("Registrando...");

        pd2 = new ProgressDialog(this);
        pd2.setMessage("Subiendo Imagen.");

        check();

        /*if (Offline){
            checkOffline();
        }else {
            check();
        }*/

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFotoOffline();
                /*if (Offline){
                    imgFotoOffline();
                }else {
                    imgFoto();
                }*/
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion();
            }});

    }




    //ALETORIO
    Random primero = new Random();
    int prime= primero.nextInt(9);

    String[] segundo = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandonsegun = (int) Math.round(Math.random() * 25 ) ;

    Random tercero = new Random();
    int tercer= tercero.nextInt(9);

    String[] cuarto = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m","n","o","p","q","r","s","t","u","v","w", "x","y","z" };
    int numRandoncuart = (int) Math.round(Math.random() * 25 ) ;

    String numero_aletorio=prime+segundo[numRandonsegun]+tercer+cuarto[numRandoncuart];

    public void checkOffline() {

        try {
            String id_residencial = Conf.getResid().trim();
            String fol = Conf.getPlacas();

            String parametros[] = {fol, id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_CORRESPONDENCIA, null, "Online", parametros, null);

            if (cursor.moveToFirst()){
                ja1 = new JSONArray();
                ja1.put(cursor.getString(0));
                ja1.put(cursor.getString(1));
                ja1.put(cursor.getString(2));
                ja1.put(cursor.getString(3));
                ja1.put(cursor.getString(4));
                ja1.put(cursor.getString(5));
                ja1.put(cursor.getString(6));
                ja1.put(cursor.getString(7));
                ja1.put(cursor.getString(8));
                ja1.put(cursor.getString(9));
                ja1.put(cursor.getString(10));
                ja1.put(cursor.getString(11));
                ja1.put(cursor.getString(12));
                ja1.put(cursor.getString(13));
                ja1.put(cursor.getString(14));
                ja1.put(cursor.getString(15));
                ja1.put(cursor.getString(16));
                ja1.put(cursor.getString(17));
                ja1.put(cursor.getString(18));
                ja1.put(cursor.getString(19));

                check2Offline();
            }else{

            }
        }catch (Exception ex){
            Log.e("Exception", ex.toString());
        }
    }

    public void check() {
        Log.e("Correspondencia", "Método check");
        String url = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/correspondencia_5.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Correspondencia", response);
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja1 = new JSONArray(response);
                        check2();
                    } catch (JSONException e) {

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
                params.put("Folio", Conf.getPlacas());
                params.put("id_residencial", Conf.getResid().trim());

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void check2Offline() {

        try {
            String id_usuario = ja1.getString(2);
            String id_residencial = Conf.getResid().trim();

            String parametros[] = {id_usuario, id_residencial};

            Cursor cursor = getContentResolver().query(UrisContentProvider.URI_CONTENIDO_DTL_LUGAR_USUARIO, null, null, parametros, null);

            if (cursor.moveToFirst()){
                ja2 = new JSONArray();
                ja2.put(cursor.getString(0));
                ja2.put(cursor.getString(1));

                ValidarQR();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void check2() {
        Log.e("Correspondencia", "Método check2");

        String url = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/correspondencia_6.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Correspondencia", response);
                response = response.replace("][",",");
                if (response.length()>0){
                    try {
                        ja2 = new JSONArray(response);
                        ValidarQR();
                    } catch (JSONException e) {

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
                try {
                    params.put("id_usuario", ja1.getString(2));
                    params.put("id_residencial", Conf.getResid().trim());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    public void ValidarQR(){
        Log.e("Correspondencia", "Método validar QR");
        try {
            rlVista.setVisibility(View.GONE);
            rlPermitido.setVisibility(View.VISIBLE);

            setNumero.setText(ja1.getString(0));

            /*if (!ja1.getString(13).isEmpty()){
                setNumero.setText(ja1.getString(0)+"-"+ja1.getString(13));
            }else{
                setNumero.setText(ja1.getString(0));
            }*/

            Log.e("Correspondencia", ja2.getString(0));
            Log.e("Correspondencia", ja1.getString(6));
            setPara.setText(ja2.getString(0));
            setComent.setText(ja1.getString(6));

            storageReference.child(Conf.getPin()+"/correspondencia/"+ja1.getString(7))
                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override

                        public void onSuccess(Uri uri) {

                            Glide.with(EntregaActivity.this)
                                    .load(uri)
                                    .error(R.drawable.log)
                                    .centerInside()
                                    .into(foto_recep);

                            txtFoto.setVisibility(android.view.View.GONE);
                            foto_recep.setVisibility(android.view.View.VISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("TAG","Error123: " + exception);

                            txtFoto.setText(Global_info.getTexto2Imagenes());

                        }
                    });

            /*if (!Offline){
                storageReference.child(Conf.getPin()+"/correspondencia/"+ja1.getString(7))
                        .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override

                            public void onSuccess(Uri uri) {

                                Glide.with(EntregaActivity.this)
                                        .load(uri)
                                        .error(R.drawable.log)
                                        .centerInside()
                                        .into(foto_recep);

                                txtFoto.setVisibility(android.view.View.GONE);
                                foto_recep.setVisibility(android.view.View.VISIBLE);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("TAG","Error123: " + exception);

                                txtFoto.setText(Global_info.getTexto2Imagenes());

                            }
                        });
            }*/






        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //IMAGEN FOTO

    public void imgFotoOffline(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                nombreImagen1 = "app"+ja1.getString(2)+"-"+numero_aletorio+".png";
                foto= new File(getApplication().getExternalFilesDir(null),nombreImagen1);
                rutaImagen1 = foto.getAbsolutePath();
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {

                uri_img= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img);
                startActivityForResult(intentCaptura, 0);
            }
        }
    }

    public void imgFoto(){
        Intent intentCaptura = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCaptura.addFlags(intentCaptura.FLAG_GRANT_READ_URI_PERMISSION);

        if (intentCaptura.resolveActivity(getPackageManager()) != null) {

            File foto=null;
            try {
                foto= new File(getApplication().getExternalFilesDir(null),"entrega.png");
            } catch (Exception ex) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Error al capturar la foto")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create().show();
            }
            if (foto != null) {

                uri_img= FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName()+".provider",foto);
                intentCaptura.putExtra(MediaStore.EXTRA_OUTPUT,uri_img);
                startActivityForResult(intentCaptura, 0);
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {


                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/"+nombreImagen1);


                bitmap = DetectarPlaca.fechaHoraFoto(bitmap);

                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(rutaImagen1);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // compress and save as JPEG
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*if (Offline){
                    try {
                        bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/app"+ja1.getString(2)+"-"+numero_aletorio+".png");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    bitmap = BitmapFactory.decodeFile(getApplicationContext().getExternalFilesDir(null) + "/entrega.png");
                }*/

                View.setVisibility(android.view.View.VISIBLE);
                viewFoto.setVisibility(android.view.View.VISIBLE);
                viewFoto.setImageBitmap(bitmap);
                espacio.setVisibility(android.view.View.VISIBLE);
                espacio2.setVisibility(android.view.View.VISIBLE);
                BtnReg.setVisibility(android.view.View.VISIBLE);
                btnRegistrar.setVisibility(android.view.View.VISIBLE);

            }
        }
    }


    public void Validacion() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("¿ Desea Entregar Paquete ?")
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
                        Intent i = new Intent(getApplicationContext(), CorrespondenciaActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RegistrarOffline(){

        try {
            int actualizar = 0;

            //Registrar fotos en SQLite
            ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/correspondencia/"+nombreImagen1.trim(), rutaImagen1);
            Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);

            //Obtener fecha
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
            String segundo = "0";

            if (second < 10){
                segundo = "0"+second;
            }else {
                segundo = ""+second;
            }

            if (hour < 10 || minute < 10){
                if (hour < 10 && minute >=10){
                    hora = "0"+hour+":"+minute+":"+segundo;
                }else if (hour >= 10 && minute < 10){
                    hora = hour+":0"+minute+":"+segundo;
                }else if (hour < 10 && minute < 10){
                    hora = "0"+hour+":0"+minute+":"+segundo;
                }
            }else {
                hora = hour+":"+minute+":"+segundo;
            }


            String fecha_entrega = fecha + " " + hora;

            String status = "";
            if (ja1.getString(19) != null){
                status = ja1.getString(19).trim();
            }

            System.out.println("Status sqlite: " + ja1.getString(19));

            System.out.println("ID : " + ja1.getString(0));

            ContentValues values = new ContentValues();
            values.put("foto", nombreImagen1);
            values.put("fecha_entrega", fecha_entrega);
            values.put("token",ja2.getString(1));
            values.put("nombre_r", Conf.getNomResi().trim());
            values.put("estatus", 1);
            if (status.equals("0")){
                values.put("sqliteEstatus", 2);
            }else{
                values.put("sqliteEstatus", 1);
            }

            actualizar = getContentResolver().update(UrisContentProvider.URI_CONTENIDO_CORRESPONDENCIA, values, "id = "+ ja1.getString(0), null);

            if (actualizar != -1){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Entrega exitosa en modo offline")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), CorrespondenciaActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();


            }else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
                alertDialogBuilder.setTitle("Alerta");
                alertDialogBuilder
                        .setMessage("Entrega no exitosa en modo offline")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).create().show();
            }

        }catch (Exception ex){
            Log.e("exception reg", ex.toString());
        }


    }

    public ContentValues ValuesImagen(String nombre, String rutaFirebase, String rutaDispositivo){
        ContentValues values = new ContentValues();
        values.put("titulo", nombre);
        values.put("direccionFirebase", rutaFirebase);
        values.put("rutaDispositivo", rutaDispositivo);
        return values;
    }

    public void Registrar(){

        String url = "https://communitycabo.sist.com.mx/plataforma/casetaV2/controlador/LOS_CABOS/correspondencia_7.php?bd_name="+Conf.getBd()+"&bd_user="+Conf.getBdUsu()+"&bd_pwd="+Conf.getBdCon();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response){


                if(response.equals("error")){
                    pd.dismiss();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
                    alertDialogBuilder.setTitle("Alerta");
                    alertDialogBuilder
                            .setMessage("Entrega No Exitosa")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), EntradasSalidasActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }).create().show();
                }else {

                    if (Global_info.getCantidadFotosEnEsperaEnSegundoPlano(EntregaActivity.this) >= Global_info.getLimiteFotosSegundoPlano()){
                        upload1();
                    }else {
                        //Registrar fotos en SQLite
                        ContentValues val_img1 =  ValuesImagen(nombreImagen1, Conf.getPin()+"/correspondencia/"+nombreImagen1.trim(), rutaImagen1);
                        Uri uri = getContentResolver().insert(UrisContentProvider.URI_CONTENIDO_FOTOS_OFFLINE, val_img1);
                    }


                    pd.dismiss();
                    terminar();

                    //upload1();

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

                Map<String, String> params = new HashMap<>();
                params.put("Folio",Conf.getPlacas());
                params.put("Foto",nombreImagen1 );
                params.put("id_residencial", Conf.getResid().trim());
                try {
                    params.put("token", ja2.getString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("nom_residencial",Conf.getNomResi().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);


    }



    public void upload1() {

        StorageReference mountainImagesRef = null;
        mountainImagesRef = storageReference.child(Conf.getPin() + "/correspondencia/" + nombreImagen1);

        Uri uri  = Uri.fromFile(new File(rutaImagen1));
        UploadTask uploadTask = mountainImagesRef.putFile(uri);

        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                pd2.show(); // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //System.out.println("Upload is " + progress + "% done");
                // Toast.makeText(getApplicationContext(),"Cargando Imagen INE " + progress + "%", Toast.LENGTH_SHORT).show();

            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(AccesoActivity.this,"Pausado",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(EntregaActivity.this, "Fallado", Toast.LENGTH_SHORT).show();
                pd2.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                eliminarFotoDirectorioLocal(nombreImagen1);
                pd2.dismiss();

            }
        });
    }

    public void eliminarFotoDirectorioLocal(String nombreFoto){
        String tempfilepath ="";
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            tempfilepath = externalFilesDir.getAbsolutePath();
            try {
                File grTempFiles = new File(tempfilepath);
                if (grTempFiles.exists()) {
                    File[] files = grTempFiles.listFiles();
                    if (grTempFiles.isDirectory() && files != null) {
                        int numofFiles = files.length;

                        for (int i = 0; i < numofFiles; i++) {
                            try {
                                File path = new File(files[i].getAbsolutePath());
                                if (!path.isDirectory() && path.getName().equals(nombreFoto)) {
                                    path.delete();
                                }
                            }catch (Exception e){
                                Log.e("EliminarFoto", e.toString());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ErrorFile", "deleteDirectory: Failed to onCreate directory  " + tempfilepath + " for an unknown reason.");

            }

        }else {
        }
    }

    public void terminar() {
        if (Global_info.getCantidadFotosEnEsperaEnSegundoPlano(EntregaActivity.this) > 0){
            if (!servicioFotos()){
                Intent cargarFotos = new Intent(EntregaActivity.this, subirFotos.class);
                startService(cargarFotos);
            }
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntregaActivity.this);
        alertDialogBuilder.setTitle("Alerta");
        alertDialogBuilder
                .setMessage("Entrega Exitosa")
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*if (!Offline){
                            //Solo ejecutar si el servicio no se esta ejecutando
                            if (!servicioFotos()){
                                Intent cargarFotos = new Intent(EntregaActivity.this, subirFotos.class);
                                startService(cargarFotos);
                            }
                        }*/

                        Intent i = new Intent(getApplicationContext(), CorrespondenciaActivity.class);
                        startActivity(i);
                        finish();
                    }
                }).create().show();
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
        Intent intent = new Intent(getApplicationContext(), CorrespondenciaActivity.class);
        startActivity(intent);
        finish();

    }

}
