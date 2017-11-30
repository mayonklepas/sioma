package com.meteorit.sioma;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gv;
    ArrayList<Homemodel> model=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        model.add(new Homemodel(R.mipmap.ic_assignment_ind_black_48dp,
                "PENDAFTARAN\n SIM"));
        model.add(new Homemodel(R.mipmap.ic_call_black_48dp,
                "CALL CENTER"));
        model.add(new Homemodel(R.mipmap.ic_mail_outline_black_48dp,
                "SMS CENTER"));
        model.add(new Homemodel(R.mipmap.ic_language_black_48dp,
                "WEB KORLANTAS"));
        model.add(new Homemodel(R.mipmap.ic_volume_up_black_48dp,
                "INFORMASI"));
        model.add(new Homemodel(R.mipmap.ic_account_circle_black_48dp,
                "PROFILE"));
        gv=(GridView) findViewById(R.id.gv);
        gv.setNumColumns(2);
        gv.setAdapter(new Homeadapter(model, this));
        selectitem();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        System.out.println(Config.iduser);
        if (Config.nohp.equals("")){
            AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("Informasi");
            adb.setMessage("Segera Lengkapi data anda di menu PROFILE");
            adb.setPositiveButton("Saya Mengerti", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            adb.show();
        }
        FirebaseMessaging.getInstance().subscribeToTopic("sioma-mataram-ntb");
    }


    private void selectitem(){
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent intent=new Intent(MainActivity.this,SimActivity.class);
                    startActivity(intent);
                }else if(i==1){
                    /*Intent intent=new Intent(MainActivity.this,SkckActivity.class);
                    startActivity(intent);*/
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                        Toast.makeText(MainActivity.this, "Tekan Call Center Sekali Lagi Untuk Memanggil", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+Config.nohp));
                    startActivity(callIntent);
                }else if(i==2){
                    Intent intent=new Intent(MainActivity.this,PengaduanActivity.class);
                    startActivity(intent);
                    /*Uri uri = Uri.parse("smsto:"+Config.infosms);
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(it);*/
                }else if(i==3){
                    /*Intent intent=new Intent(MainActivity.this,SurathilangActivity.class);
                    startActivity(intent);*/
                    Uri uri = Uri.parse(Config.infoweb);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else if(i==4){
                    Intent intent=new Intent(MainActivity.this,InboxActivity.class);
                    startActivity(intent);
                }else if(i==5){
                    Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("Informasi");
            adb.setMessage("Keluar dari aplikasi, anda tidak akan mendapatkan notifikasi, Minimize jika ingin terus mendapatkan notifikasi");
            adb.setPositiveButton("Tetap keluar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
            adb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            adb.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_call) {
             if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) !=
                     PackageManager.PERMISSION_GRANTED) {
                 ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                 Toast.makeText(this, "Tekan Call Center Sekali Lagi Untuk Memanggil", Toast.LENGTH_SHORT).show();
                 return true;
             }
             Intent callIntent = new Intent(Intent.ACTION_CALL);
             callIntent.setData(Uri.parse("tel:"+Config.nohp));
             startActivity(callIntent);
        } else if (id == R.id.nav_sms) {
             Uri uri = Uri.parse("smsto:"+Config.infosms);
             Intent it = new Intent(Intent.ACTION_SENDTO, uri);
             startActivity(it);
         } else if (id == R.id.nav_web) {
             Uri uri = Uri.parse(Config.infoweb);
             Intent intent = new Intent(Intent.ACTION_VIEW, uri);
             startActivity(intent);
        }else if (id == R.id.nav_profil){
             Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
             startActivity(intent);
         }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
