package com.meteorit.sioma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class IzinkeramaianActivity extends AppCompatActivity {

    EditText nama, alamat, penanggung_jawab, kegiatan, lokasi, rute,
    waktu, jumlah_peserta, koordinator_lapangan, alat_peraga, tujuan;
    int status=2;
    String keterangan="";
    Button kirim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_keramaian);
        nama=(EditText) findViewById(R.id.nama);
        penanggung_jawab=(EditText) findViewById(R.id.penanggung_jawab);
        kegiatan=(EditText) findViewById(R.id.kegiatan);
        alamat=(EditText) findViewById(R.id.alamat);
        lokasi=(EditText) findViewById(R.id.lokasi);
        rute=(EditText) findViewById(R.id.rute);
        waktu=(EditText) findViewById(R.id.waktu);
        jumlah_peserta=(EditText) findViewById(R.id.jumlah_peserta);
        koordinator_lapangan=(EditText) findViewById(R.id.koordinator_lapangan);
        alat_peraga=(EditText) findViewById(R.id.alat_peraga);
        tujuan=(EditText) findViewById(R.id.tujuan);

        kirim=(Button) findViewById(R.id.kirim);

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertizin();
            }
        });



    }


    private void insertizin(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Mengirim Data...");
        pd.setCancelable(false);
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url + "/rest/insertizin-keramaian.php", new Response.Listener<String>() {
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
                map.put("alamat",alamat.getText().toString());
                map.put("penanggung_jawab",penanggung_jawab.getText().toString());
                map.put("kegiatan",kegiatan.getText().toString());
                map.put("lokasi",lokasi.getText().toString());
                map.put("rute", rute.getText().toString());
                map.put("waktu",waktu.getText().toString());
                map.put("jumlah_peserta",jumlah_peserta.getText().toString());
                map.put("koordinator_lapangan", koordinator_lapangan.getText().toString());
                map.put("alat_peraga", alat_peraga.getText().toString());
                map.put("tujuan",tujuan.getText().toString());
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
                    AlertDialog.Builder adb=new AlertDialog.Builder(IzinkeramaianActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage("Permintaan Izin Keramaian Berhasil Dikirim, Terima Kasih");
                    adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }else{
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(IzinkeramaianActivity.this);
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
