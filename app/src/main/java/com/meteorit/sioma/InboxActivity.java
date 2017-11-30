package com.meteorit.sioma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InboxActivity extends AppCompatActivity {

    RecyclerView rv;
    ProgressBar pb;
    ArrayList<Inboxmodel> lsadapter=new ArrayList<>();
    Inboxadapter adapter;
    RecyclerView.LayoutManager rvman;
    int status=2;
    String keterangan="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        rv=(RecyclerView) findViewById(R.id.rv);
        pb=(ProgressBar) findViewById(R.id.pb);
        rvman=new LinearLayoutManager(this);
        rv.setLayoutManager(rvman);
        adapter=new Inboxadapter(lsadapter,this);
        rv.setAdapter(adapter);
        loaddata();
    }

    private void loaddata(){
        pb.setVisibility(View.VISIBLE);
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url+"/rest/getpesan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject jo=new JSONObject(response);
                    JSONArray jastatus=jo.getJSONArray("status");
                    JSONObject jostatus=jastatus.getJSONObject(0);
                    status=jostatus.getInt("kode");
                    keterangan=jostatus.getString("keterangan");
                    if(status==0){
                        JSONArray jadata=jo.getJSONArray("data");
                        for (int i = 0; i < jadata.length(); i++) {
                            JSONObject jodata=jadata.getJSONObject(i);
                            lsadapter.add(new Inboxmodel(
                                    jodata.getString("id_pesan"),
                                    jodata.getString("tanggal"),
                                    jodata.getString("isi")
                            ));
                        }
                    }
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
                map.put("id_user",Config.iduser);
                return map;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(15*1000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);
        rq.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(status==0){
                    pb.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }else{
                    pb.setVisibility(View.GONE);
                    Toast.makeText(InboxActivity.this, keterangan, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
