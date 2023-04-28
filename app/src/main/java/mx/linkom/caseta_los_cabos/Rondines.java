package mx.linkom.caseta_los_cabos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import mx.linkom.caseta_los_cabos.Controller.PagerControlador;
import mx.linkom.caseta_los_cabos.offline.Global_info;


public class Rondines  extends mx.linkom.caseta_los_cabos.Menu{

    TabLayout tablayout;
    TabItem tabRecibir,tabEstacionar,tabRecoger,tabEntrega;
    ViewPager viewPager;
    PagerControlador pagerAdapter;

    ImageView iconoInternet;
    boolean Offline = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rondines_lista);


        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        TabItem tabs1 = (TabItem) findViewById(R.id.tabUbicacion);
        TabItem tabs2 = (TabItem) findViewById(R.id.tabQr);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        iconoInternet = (ImageView)findViewById(R.id.iconoInternetRondines);

        if (Global_info.getINTERNET().equals("Si")){
            //Es online
            Offline = false;
            iconoInternet.setImageResource(R.drawable.ic_online);
        }else {
            //Es offline
            iconoInternet.setImageResource(R.drawable.ic_offline);
            Offline = true;
        }

        iconoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Offline){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Rondines.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOffline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Rondines.this);
                    alertDialogBuilder.setTitle(Global_info.getTituloAviso());
                    alertDialogBuilder
                            .setMessage(Global_info.getModoOnline())
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                }
            }
        });

        pagerAdapter=new PagerControlador(getSupportFragmentManager(),tablayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    pagerAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition()==1){
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
    }



    @Override
    public void onBackPressed(){
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }


}
