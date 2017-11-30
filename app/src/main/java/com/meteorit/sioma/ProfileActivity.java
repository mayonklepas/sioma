package com.meteorit.sioma;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView email;
    EditText nama,nohp,alamat;
    Button update;
    int status;
    String keterangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nama=(EditText) findViewById(R.id.nama);
        email=(TextView) findViewById(R.id.email);
        nohp=(EditText) findViewById(R.id.nohp);
        alamat=(EditText) findViewById(R.id.alamat);
        update=(Button) findViewById(R.id.update);
        email.setText(Config.email);
        nama.setText(Config.nama);
        nohp.setText(Config.nohp);
        alamat.setText(Config.alamat);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertuser(nama.getText().toString(),nohp.getText().toString(),alamat.getText().toString());
            }
        });

    }


    private void insertuser(final String nama,final String nohp,final String alamat){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Memproses Data...");
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url+"/rest/updateuser.php",
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo=new JSONObject(response);
                    JSONArray jastatus=jo.getJSONArray("status");
                    JSONObject jostatus=jastatus.getJSONObject(0);
                    status=jostatus.getInt("kode");
                    keterangan=jostatus.getString("keterangan");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder adb=new AlertDialog.Builder(ProfileActivity.this);
                adb.setTitle("Informasi");
                adb.setMessage(error.getMessage());
                adb.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params=new HashMap();
                params.put("key","bk201!@#");
                params.put("nama",nama);
                params.put("nohp",nohp);
                params.put("alamat",alamat);
                params.put("email",Config.email);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(1000*15,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);
        rq.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(status==0){
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(ProfileActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage("Profile Berhasil Diperbaharui");
                    adb.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }else{
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(ProfileActivity.this);
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
