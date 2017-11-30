package com.meteorit.sioma;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimActivity extends AppCompatActivity {

    Spinner jenis_permohonan, golongan,berkacamata, cacat_fisik, sertifikat_mengemudi, jenis_kelamin,warga_negara;
    EditText no_sim,kode_bank, no_resi, tanggal_daftar, nama, asal_negara,
    no_pasport, tanggal_passport, no_kims, tanggal_kims, tinggi_badan, tempat_lahir,
    tanggal_lahir, pekerjaan, alamat_lengkap, rt_rw, kota, kode_pos, no_telp,
    nama_ayah, nama_ibu, no_ktp, ktp_keluaran, pendidikan_terakhir, alamat_darurat, no_hp_darurat, rt_rw_darurat,
    kode_pos_darurat, sidik_jari;
    ImageView foto;
    TextInputLayout no_sim_hint,asal_negara_hint,no_pasport_hint,tanggal_pasport_hint,no_kims_hint,tanggal_kims_hint;
    Button ambilfoto,kirim;
    String sjenis_permohonan,sgolongan,sberkacamata,scacat_fisik,ssertifikat_mengemudi,sjenis_kelamin,swarga_negara;
    int status=2;
    String keterangan="";
    Uri selectedimage;
    int resreq=1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim);
        jenis_permohonan=(Spinner) findViewById(R.id.jenis_permohonan);
        ArrayAdapter<String> jenis_permohonan_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"Baru","Perpanjang"});
        jenis_permohonan.setAdapter(jenis_permohonan_adapter);

        golongan=(Spinner) findViewById(R.id.golongan);
        ArrayAdapter<String> golongan_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"A","B","C"});
        golongan.setAdapter(golongan_adapter);

        berkacamata=(Spinner) findViewById(R.id.berkacamata);
        ArrayAdapter<String> berkacamata_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"Ya","Tidak"});
        berkacamata.setAdapter(berkacamata_adapter);

        cacat_fisik=(Spinner) findViewById(R.id.cacat_fisik);
        ArrayAdapter<String> cacat_fisik_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"Ya","Tidak"});
        cacat_fisik.setAdapter(cacat_fisik_adapter);

        sertifikat_mengemudi=(Spinner) findViewById(R.id.sertifikat_mengemudi);
        ArrayAdapter<String> sertifikat_mengemudi_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"Ya","Tidak"});
        sertifikat_mengemudi.setAdapter(sertifikat_mengemudi_adapter);

        jenis_kelamin=(Spinner) findViewById(R.id.jenis_kelamin);
        ArrayAdapter<String> jenis_kelamin_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"Laki-laki","Perempuan"});
        jenis_kelamin.setAdapter(jenis_kelamin_adapter);

        warga_negara=(Spinner) findViewById(R.id.warga_negara);
        ArrayAdapter<String> warga_negara_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"WNI","WNA"});
        warga_negara.setAdapter(warga_negara_adapter);

        ambilfoto=(Button) findViewById(R.id.ambilfoto);
        kirim=(Button) findViewById(R.id.kirim);
        foto=(ImageView) findViewById(R.id.foto);

        no_sim_hint=(TextInputLayout) findViewById(R.id.no_sim_hint);
        asal_negara_hint=(TextInputLayout) findViewById(R.id.asal_negara_hint);
        no_pasport_hint=(TextInputLayout) findViewById(R.id.no_pasport_hint);
        tanggal_pasport_hint=(TextInputLayout) findViewById(R.id.tanggal_pasport_hint);
        no_kims_hint=(TextInputLayout) findViewById(R.id.no_kims_hint);
        tanggal_kims_hint=(TextInputLayout) findViewById(R.id.tanggal_kims_hint);
        LinearLayout mainlay=(LinearLayout) findViewById(R.id.mainlay);
        mainlay.requestFocus();

        kode_bank=(EditText)findViewById(R.id.kode_bank);
        no_resi=(EditText)findViewById(R.id.no_resi);
        no_sim=(EditText)findViewById(R.id.no_sim);
        nama=(EditText)findViewById(R.id.nama);
        asal_negara=(EditText)findViewById(R.id.asal_negara);
        no_pasport=(EditText)findViewById(R.id.no_pasport);
        tanggal_passport=(EditText)findViewById(R.id.tanggal_pasport);
        no_kims=(EditText)findViewById(R.id.no_kims);
        tanggal_kims=(EditText)findViewById(R.id.tanggal_kims);
        tinggi_badan=(EditText)findViewById(R.id.tinggi_badan);
        tempat_lahir=(EditText)findViewById(R.id.tempat_lahir);
        tanggal_lahir=(EditText)findViewById(R.id.tanggal_lahir);
        pekerjaan=(EditText)findViewById(R.id.pekerjaan);
        alamat_lengkap=(EditText)findViewById(R.id.alamat_lengkap);
        rt_rw=(EditText)findViewById(R.id.rt_rw);
        kota=(EditText)findViewById(R.id.kota);
        kode_pos=(EditText)findViewById(R.id.kode_pos);
        no_telp=(EditText)findViewById(R.id.no_telp);
        nama_ayah=(EditText)findViewById(R.id.nama_ayah);
        nama_ibu=(EditText)findViewById(R.id.nama_ibu);
        no_ktp=(EditText)findViewById(R.id.no_ktp);
        ktp_keluaran=(EditText)findViewById(R.id.ktp_keluaran);
        pendidikan_terakhir=(EditText)findViewById(R.id.pendidikan_terakhir);
        alamat_darurat=(EditText)findViewById(R.id.alamat_darurat);
        no_hp_darurat=(EditText)findViewById(R.id.nohp_darurat);
        rt_rw_darurat=(EditText)findViewById(R.id.rt_rw_darurat);
        kode_pos_darurat=(EditText)findViewById(R.id.kode_pos_darurat);
        spinnercontrol();
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertsim();
                //String data=imagetostr();
            }
        });
        ambilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getfoto();
            }
        });

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

        tanggal_passport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd(tanggal_passport).show();
            }
        });

        tanggal_kims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd(tanggal_kims).show();
            }
        });
    }

    private void spinnercontrol(){
        jenis_permohonan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    no_sim_hint.setVisibility(View.GONE);
                    sjenis_permohonan="Baru";
                }else{
                    no_sim_hint.setVisibility(View.VISIBLE);
                    sjenis_permohonan="Perpanjang";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        warga_negara.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    asal_negara_hint.setVisibility(View.GONE);
                    no_pasport_hint.setVisibility(View.GONE);
                    tanggal_pasport_hint.setVisibility(View.GONE);
                    no_kims_hint.setVisibility(View.GONE);
                    tanggal_kims_hint.setVisibility(View.GONE);
                    swarga_negara="WNI";
                }else{
                    asal_negara_hint.setVisibility(View.VISIBLE);
                    no_pasport_hint.setVisibility(View.VISIBLE);
                    tanggal_pasport_hint.setVisibility(View.VISIBLE);
                    no_kims_hint.setVisibility(View.VISIBLE);
                    tanggal_kims_hint.setVisibility(View.VISIBLE);
                    swarga_negara="WNA";
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

        golongan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sgolongan="A";
                }else if (i == 1){
                    sgolongan="B";
                }else{
                    sgolongan="C";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        berkacamata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sberkacamata="Ya";
                }else{
                    sberkacamata="Tidak";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cacat_fisik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    scacat_fisik="Ya";
                }else{
                    scacat_fisik="Tidak";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sertifikat_mengemudi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    ssertifikat_mengemudi="Ya";
                }else{
                    ssertifikat_mengemudi="Tidak";
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 || resultCode==RESULT_OK){
            if(data==null || data.getData()==null){

            }else{
                Uri uri=data.getData();
                foto.setImageURI(uri);
            }
        }

        }


    public String imagetostr(){
        BitmapDrawable bmd=(BitmapDrawable) foto.getDrawable();
        Bitmap bmp=bmd.getBitmap();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,20,baos);
        byte[] imagebyte=baos.toByteArray();
        String imagecompress=Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return imagecompress;
    }

    private void insertsim(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Mengirim Data...");
        pd.setCancelable(false);
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url + "/rest/insertsim.php", new Response.Listener<String>() {
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
                map.put("iduser",Config.iduser);
                map.put("jenis_permohonan",sjenis_permohonan);
                map.put("golongan",sgolongan);
                map.put("no_sim",no_sim.getText().toString());
                map.put("kode_bank", kode_bank.getText().toString());
                map.put("no_resi",no_resi.getText().toString());
                map.put("nama",nama.getText().toString());
                map.put("jenis_kelamin",sjenis_kelamin);
                map.put("warga_negara",swarga_negara);
                map.put("asal_negara", asal_negara.getText().toString());
                map.put("no_pasport", no_pasport.getText().toString());
                map.put("tanggal_passport",tanggal_passport.getText().toString());
                map.put("no_kims", no_kims.getText().toString());
                map.put("tanggal_kims",tanggal_kims.getText().toString());
                map.put("tinggi_badan",tinggi_badan.getText().toString());
                map.put("tempat_lahir",tempat_lahir.getText().toString());
                map.put("tanggal_lahir",tanggal_lahir.getText().toString());
                map.put("pekerjaan",pekerjaan.getText().toString());
                map.put("alamat_lengkap",alamat_lengkap.getText().toString());
                map.put("rt_rw",rt_rw.getText().toString());
                map.put("kota",kota.getText().toString());
                map.put("kode_pos",kode_pos.getText().toString());
                map.put("no_telp",no_telp.getText().toString());
                map.put("nama_ayah",nama_ayah.getText().toString());
                map.put("nama_ibu",nama_ibu.getText().toString());
                map.put("no_ktp",no_ktp.getText().toString());
                map.put("ktp_keluaran",ktp_keluaran.getText().toString());
                map.put("pendidikan_terakhir",pendidikan_terakhir.getText().toString());
                map.put("berkacamata",sberkacamata);
                map.put("cacat_fisik",scacat_fisik);
                map.put("sertifikat_mengemudi",ssertifikat_mengemudi);
                map.put("alamat_darurat",alamat_darurat.getText().toString());
                map.put("no_hp_darurat",no_hp_darurat.getText().toString());
                map.put("rt_rw_darurat",rt_rw_darurat.getText().toString());
                map.put("kode_pos_darurat",kode_pos_darurat.getText().toString());
                map.put("datafoto", imagetostr());
                map.put("foto", "SIM-"+nama.getText().toString()+"-"+new SimpleDateFormat("yyyyMMddhms").format(new Date())+".jpg");
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
                    AlertDialog.Builder adb=new AlertDialog.Builder(SimActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage("Pendaftaran SIM Sukses, Terima Kasih");
                    adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }else{
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(SimActivity.this);
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




    public static class datepickerfragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{



        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c=Calendar.getInstance();
            int tahun=c.get(Calendar.YEAR);
            int bulan=c.get(Calendar.MONTH);
            int tanggal=c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),this,tahun,bulan,tanggal);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        }
    }



}
