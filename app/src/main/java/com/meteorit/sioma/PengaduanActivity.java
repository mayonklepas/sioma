package com.meteorit.sioma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PengaduanActivity extends AppCompatActivity {

    EditText keterangan;
    Button kirim,ambilfoto;
    int status=2;
    String sketerangan="";
    ImageView foto;
    double lati=0,longi=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);
        keterangan=(EditText) findViewById(R.id.keterangan);
        foto=(ImageView) findViewById(R.id.foto);
        kirim=(Button) findViewById(R.id.kirim);
        ambilfoto=(Button) findViewById(R.id.ambilfoto);
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             insertpengaduan();
            }
        });
        ambilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getfoto();
            }
        });
        koordinatlisten();
    }

    private void getfoto() {
        /*if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},1);
                return;
            }
            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,1);

        }*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 || resultCode==RESULT_OK && data != null && data.getData() != null){
            Uri uri=data.getData();
            foto.setImageURI(uri);
        }

    }

    public String imagetostr(){
        BitmapDrawable bmd=(BitmapDrawable) foto.getDrawable();
        Bitmap bmp=bmd.getBitmap();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,20,baos);
        byte[] imagebyte=baos.toByteArray();
        String imagecompress= Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return imagecompress;
    }

    private void insertpengaduan(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Mengirim Data...");
        pd.setCancelable(false);
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url + "/rest/insertpengaduan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                    System.out.println(response);
                    JSONObject jo=new JSONObject(response);
                    JSONArray jastatus=jo.getJSONArray("status");
                    JSONObject jostatus= jastatus.getJSONObject(0);
                    status=jostatus.getInt("kode");
                    sketerangan=jostatus.getString("keterangan");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<>();
                map.put("key","bk201!@#");
                map.put("keterangan",keterangan.getText().toString());
                map.put("id_user",Config.iduser);
                map.put("latitude",String.valueOf(lati));
                map.put("longitude",String.valueOf(longi));
                map.put("datafoto",imagetostr());
                map.put("foto","Pengaduan-"+Config.email+"-"+new SimpleDateFormat("yyyyMMddhms").format(new Date())+".jpg");
                return map;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(1000*15,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);
        rq.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(status==0){
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(PengaduanActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage("Pengaduan Anda Berhasil Dikirim, Terima Kasih");
                    adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }else{
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(PengaduanActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage(sketerangan);
                    adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }
            }
        });
    }



    private void koordinatlisten() {
        LocationManager locma = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PengaduanActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            Toast.makeText(this, "Mohon Untuk Mengaktifkan GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationListener locis = new LocationListener () {
            @Override
            public void onLocationChanged(Location location) {
                longi = location.getLongitude();
                lati = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        Criteria ct = new Criteria();
        locma.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locis);
        //}
    }



}
