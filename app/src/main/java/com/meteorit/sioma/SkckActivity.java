package com.meteorit.sioma;

import android.*;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SkckActivity extends AppCompatActivity {

    EditText nama,tempat_lahir, tanggal_lahir, alamat, pekerjaan, no_ktp, no_paspor,
    keperluan;
    Spinner jenis_kelamin, kewarganegaraan, agama;
    TextInputLayout no_paspor_hint;
    String sjenis_kelamin,skewarganegaraan,sagama;
    int status=2;
    String keterangan="";
    Button ambilfoto,kirim;
    ImageView foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skck);
        jenis_kelamin=(Spinner) findViewById(R.id.jenis_kelamin);
        ArrayAdapter<String> jenis_kelamin_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"Laki-laki","Perempuan"});
        jenis_kelamin.setAdapter(jenis_kelamin_adapter);

        kewarganegaraan=(Spinner) findViewById(R.id.kewarganegaraan);
        ArrayAdapter<String> kewarganegaraan_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"WNI","WNA"});
        kewarganegaraan.setAdapter(kewarganegaraan_adapter);

        agama=(Spinner) findViewById(R.id.agama);
        ArrayAdapter<String> agama_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"ISLAM","KRISTEN","KATOLIK","HINDU","BUDHA","KONGHUCU"});
        agama.setAdapter(agama_adapter);
        no_paspor_hint=(TextInputLayout) findViewById(R.id.no_paspor_hint);
        nama=(EditText) findViewById(R.id.nama);
        tempat_lahir=(EditText) findViewById(R.id.tempat_lahir);
        tanggal_lahir=(EditText) findViewById(R.id.tanggal_lahir);
        alamat=(EditText) findViewById(R.id.alamat);
        pekerjaan=(EditText) findViewById(R.id.pekerjaan);
        no_ktp=(EditText) findViewById(R.id.no_ktp);
        no_paspor=(EditText) findViewById(R.id.no_paspor);
        keperluan=(EditText) findViewById(R.id.keperluan);
        foto=(ImageView) findViewById(R.id.foto);
        ambilfoto=(Button) findViewById(R.id.ambilfoto);
        kirim=(Button) findViewById(R.id.kirim);
        LinearLayout mainlay=(LinearLayout) findViewById(R.id.mainlay);
        mainlay.requestFocus();

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertskck();
            }
        });
        ambilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getfoto();
            }
        });

        spinnercontrol();
        datepickeract();


    }


    private DatePickerDialog dpd(final EditText ed){
        Calendar c=Calendar.getInstance();
        DatePickerDialog dpdin=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                ed.setText(i+"-"+i1+"-"+i2);
            }
        },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        return dpdin;
    }

    private void datepickeract(){
        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd(tanggal_lahir).show();
            }
        });
    }

    private void spinnercontrol(){

        kewarganegaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    no_paspor_hint.setVisibility(View.GONE);
                    skewarganegaraan="WNI";
                }else{
                    no_paspor_hint.setVisibility(View.VISIBLE);
                    skewarganegaraan="WNA";;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        jenis_kelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sjenis_kelamin="Laki-laki";
                }else{
                    sjenis_kelamin="Perempuan";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        agama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    sagama="ISLAM";
                }else if (i == 1){
                    sagama="KRISTEN";
                }else if(i == 2){
                    sagama="KATOLIK";
                }else if(i == 3){
                    sagama="HINDU";
                }else if(i == 4){
                    sagama="BUDHA";
                }else if(i == 5){
                    sagama="KONGHUCU";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




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

    private void insertskck(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Mengirim Data...");
        pd.setCancelable(false);
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url + "/rest/insertskck.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject jo=new JSONObject(response);
                    JSONArray jastatus=jo.getJSONArray("status");
                    JSONObject jostatus= jastatus.getJSONObject(0);
                    status=jostatus.getInt("kode");
                    keterangan=jostatus.getString("keterangan");
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
                map.put("nama",nama.getText().toString());
                map.put("jenis_kelamin",sjenis_kelamin);
                map.put("kewarganegaraan",skewarganegaraan);
                map.put("agama", sagama);
                map.put("tempat_lahir",tempat_lahir.getText().toString());
                map.put("tanggal_lahir",tanggal_lahir.getText().toString().replaceAll("[-.,/]","-"));
                map.put("alamat",alamat.getText().toString());
                map.put("pekerjaan",pekerjaan.getText().toString());
                map.put("no_ktp", no_ktp.getText().toString());
                map.put("no_paspor", no_paspor.getText().toString());
                map.put("keperluan",keperluan.getText().toString());
                map.put("datafoto",imagetostr());
                map.put("foto","SKCK-"+nama.getText().toString()+"-"+new SimpleDateFormat("yyyyMMddhms").format(new Date())+".jpg");
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
                    AlertDialog.Builder adb=new AlertDialog.Builder(SkckActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage("Permintaan SKCK Berhasil Dikirim, Terima Kasih");
                    adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }else{
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(SkckActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage(keterangan);
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
}
