package com.meteorit.sioma;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SurathilangActivity extends AppCompatActivity {


    EditText nama,ttl, pekerjaan, alamat,
    tanggal_hilang, jam_hilang, keterangan_hilang;
    Spinner agama,jenis_kelamin, warga_negara;
    String swarga_negara,sjenis_kelamin,sagama;
    int status=2;
    String keterangan="";
    Button kirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surathilang);
        jenis_kelamin=(Spinner) findViewById(R.id.jenis_kelamin);
        ArrayAdapter<String> jenis_kelamin_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"Laki-laki","Perempuan"});
        jenis_kelamin.setAdapter(jenis_kelamin_adapter);

        warga_negara=(Spinner) findViewById(R.id.warga_negara);
        ArrayAdapter<String> kewarganegaraan_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"WNI","WNA"});
        warga_negara.setAdapter(kewarganegaraan_adapter);

        agama=(Spinner) findViewById(R.id.agama);
        ArrayAdapter<String> agama_adapter=new ArrayAdapter<String>
                (this,R.layout.support_simple_spinner_dropdown_item,new String[]{"ISLAM","KRISTEN","KATOLIK","HINDU","BUDHA","KONGHUCU"});
        agama.setAdapter(agama_adapter);
        nama=(EditText) findViewById(R.id.nama);
        ttl=(EditText) findViewById(R.id.ttl);
        pekerjaan=(EditText) findViewById(R.id.pekerjaan);
        alamat=(EditText) findViewById(R.id.alamat);
        tanggal_hilang=(EditText) findViewById(R.id.tanggal_hilang);
        jam_hilang=(EditText) findViewById(R.id.jam_hilang);
        keterangan_hilang=(EditText) findViewById(R.id.keterangan_hilang);
        LinearLayout mainlay=(LinearLayout) findViewById(R.id.mainlay);
        mainlay.requestFocus();
        spinnercontrol();
        datepickeract();
        kirim=(Button) findViewById(R.id.kirim);
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertsuratkehilangan();
            }
        });


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

    private TimePickerDialog tpd(final EditText ed){
        Calendar c=Calendar.getInstance();
        TimePickerDialog tpdin=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                ed.setText(i+":"+i1);
            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
        return tpdin;
    }

    private void datepickeract(){
        tanggal_hilang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd(tanggal_hilang).show();
            }
        });

        jam_hilang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpd(jam_hilang).show();
            }
        });
    }

    private void spinnercontrol(){

        warga_negara.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    swarga_negara="WNI";
                }else{
                    swarga_negara="WNA";;
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

    private void insertsuratkehilangan(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Mengirim Data...");
        pd.setCancelable(false);
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url + "/rest/insertsurat-kehilangan.php", new Response.Listener<String>() {
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
                map.put("warga_negara",swarga_negara);
                map.put("agama", sagama);
                map.put("ttl",ttl.getText().toString());
                map.put("alamat",alamat.getText().toString());
                map.put("pekerjaan",pekerjaan.getText().toString());
                map.put("tanggal_hilang", tanggal_hilang.getText().toString());
                map.put("jam_hilang", jam_hilang.getText().toString());
                map.put("keterangan_hilang",keterangan_hilang.getText().toString());
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
                    AlertDialog.Builder adb=new AlertDialog.Builder(SurathilangActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage("Laporan Kehilangan Berhasill Dikirim, Terima Kasih");
                    adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }else{
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(SurathilangActivity.this);
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
