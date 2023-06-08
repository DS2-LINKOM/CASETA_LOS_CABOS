package mx.linkom.caseta_los_cabos;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;

import java.util.ArrayList;

import mx.linkom.caseta_los_cabos.adaptadores.ModuloClassGrid;
import mx.linkom.caseta_los_cabos.adaptadores.adaptador_Modulo;
import mx.linkom.caseta_los_cabos.offline.Global_info;


public class CorrespondenciaActivity extends mx.linkom.caseta_los_cabos.Menu {

    // Buutton

    Button Recepcion, Entrega;
    private Configuracion Conf;
    JSONArray ja1;

    private GridView gridList;

    /*ImageView iconoInternet;
    boolean Offline = false;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correspondencia);

        Conf = new Configuracion(this);
        gridList = (GridView)findViewById(R.id.gridList);

        /*iconoInternet = (ImageView) findViewById(R.id.iconoInternetCorrespondencia);

        if (Global_info.getINTERNET().equals("Si")){
            iconoInternet.setImageResource(R.drawable.ic_online);
            Offline = false;
        }else {
            iconoInternet.setImageResource(R.drawable.ic_offline);
            Offline = true;
        }*/

        llenado();

        /*iconoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Offline){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CorrespondenciaActivity.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CorrespondenciaActivity.this);
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

    }




    public void llenado(){
        ArrayList<ModuloClassGrid> lista = new ArrayList<ModuloClassGrid>();

                lista.add(new ModuloClassGrid(R.drawable.ic_baseline_how_to_vote_24,"Recepción","#FF4081"));

                lista.add(new ModuloClassGrid(R.drawable.ic_baseline_how_to_reg_24,"Entrega","#FF4081"));






        gridList.setAdapter(new adaptador_Modulo(this, R.layout.activity_modulo_lista, lista){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    ImageView add = (ImageView) view.findViewById(R.id.imageView);
                    if (add != null)
                        add.setImageResource(((ModuloClassGrid) entrada).getImagen());

                    final TextView title = (TextView) view.findViewById(R.id.title);
                    if (title != null)
                        title.setText(((ModuloClassGrid) entrada).getTitle());

                    final LinearLayout line = (LinearLayout) view.findViewById(R.id.line);
                    if (line != null)
                        line.setBackgroundColor(Color.parseColor(((ModuloClassGrid) entrada).getColorCode()));

                    gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                            if(position==0) {
                                Intent i = new Intent(getApplicationContext(), RecepcionActivity.class);
                                startActivity(i);
                                finish();
                            }else if(position==1){
                                Intent i = new Intent(getApplicationContext(), EntregaFolio.class);
                                startActivity(i);
                                finish();
                            }

                        }
                    });

                }
            }

        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ReportesActivity.class);
        startActivity(intent);
        finish();

    }

}
