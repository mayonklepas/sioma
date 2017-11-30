package com.meteorit.sioma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    SignInButton bmasuk;
    FirebaseAuth fauth;
    GoogleApiClient gapiClient;
    int RC = 0;
    int jumlah=0;
    int status=2;
    String keterangan="",infosms="",infocall="",infoweb="";
    int statuslogin=2;
    String keteranganlogin="";
    SharedPreferences sps;
    String ciduser,calamat,cnohp,cnama,cemail;
    String token="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bmasuk=(SignInButton) findViewById(R.id.bmasuk);
        sps=getSharedPreferences("userlog", Context.MODE_PRIVATE);
        fauth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gapiClient=new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        bmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getuserinfo();
            }
        });
        autologin(sps.getString("email",""));

    }


    private void autologin(final String email){
        token= String.valueOf(FirebaseInstanceId.getInstance().getToken());
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Memproses Data...");
        pd.setCancelable(false);
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url+"/rest/getuser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo=new JSONObject(response);
                    JSONArray jastatus=jo.getJSONArray("status");
                    JSONObject jostatus=jastatus.getJSONObject(0);
                    statuslogin=jostatus.getInt("kode");
                    keteranganlogin=jostatus.getString("keterangan");

                    JSONArray jainfo=jo.getJSONArray("info");
                    JSONObject joinfo=jainfo.getJSONObject(0);
                    infosms=joinfo.getString("sms");
                    infocall=joinfo.getString("call");
                    infoweb=joinfo.getString("web");

                    System.out.println("Status : "+jostatus);

                    if(statuslogin==0){
                        JSONArray jadata=jo.getJSONArray("data");
                        JSONObject jodata=jadata.getJSONObject(0);
                        jumlah=jodata.getInt("jumlah");
                        cnama=jodata.getString("nama");
                        cnohp=jodata.getString("nohp");
                        cemail=jodata.getString("email");
                        calamat=jodata.getString("alamat");
                        ciduser=jodata.getString("iduser");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder adb=new AlertDialog.Builder(LoginActivity.this);
                adb.setTitle("Informasi");
                adb.setMessage(error.getMessage());
                adb.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params=new HashMap();
                params.put("key","bk201!@#");
                params.put("email",email);
                params.put("firebasetoken", token);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(1000*15,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);
        rq.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(statuslogin==0){
                    if(jumlah==1){
                        Config.email=email;
                        Config.iduser=ciduser;
                        Config.alamat=calamat;
                        Config.nohp=cnohp;
                        Config.nama=cnama;
                        Config.infocall=infocall;
                        Config.infosms=infosms;
                        Config.infoweb=infoweb;
                        pd.dismiss();
                        Intent i=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Email Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    pd.dismiss();
                    AlertDialog.Builder adb=new AlertDialog.Builder(LoginActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage("Silahkan Klik Button Masuk / Signin");
                    adb.show();
                }


            }
        });
    }

    private void getuserinfo(){
        Intent loginintent=Auth.GoogleSignInApi.getSignInIntent(gapiClient);
        startActivityForResult(loginintent,RC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC){
            GoogleSignInResult signInResult=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(signInResult.isSuccess()){
                GoogleSignInAccount signInAccount=signInResult.getSignInAccount();
                cekuser(signInAccount.getId(),signInAccount.getDisplayName(),
                        signInAccount.getEmail(),"","");
            }

        }
    }


    private void insertuser(final String iduser, final String nama, final String email, final String nohp, final String foto){
        token= String.valueOf(FirebaseInstanceId.getInstance().getToken());
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url+"/rest/insertuser.php", new Response.Listener<String>() {
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
                AlertDialog.Builder adb=new AlertDialog.Builder(LoginActivity.this);
                adb.setTitle("Informasi");
                adb.setMessage(error.getMessage());
                adb.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params=new HashMap();
                params.put("key","bk201!@#");
                params.put("iduser",iduser);
                params.put("nama",nama);
                params.put("email",email);
                params.put("nohp",nohp);
                params.put("foto",foto);
                params.put("firebasetoken", token);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(1000*15,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);
        rq.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(status==0){
                    Config.iduser=iduser;
                    Config.nama=nama;
                    Config.email=email;
                    Config.nohp=nohp;
                    Intent i=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    AlertDialog.Builder adb=new AlertDialog.Builder(LoginActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage(keterangan);
                    adb.show();
                }

            }
        });
    }


   private void cekuser(final String iduser, final String nama, final String email, final String nohp, final String foto){
        token= String.valueOf(FirebaseInstanceId.getInstance().getToken());
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Memproses Data...");
        pd.setCancelable(false);
        pd.show();
        RequestQueue rq= Volley.newRequestQueue(this);
        StringRequest sr=new StringRequest(Request.Method.POST, Config.url+"/rest/getuser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //System.out.println(response);
                    JSONObject jo=new JSONObject(response);
                    JSONArray jastatus=jo.getJSONArray("status");
                    JSONObject jostatus=jastatus.getJSONObject(0);
                    statuslogin=jostatus.getInt("kode");
                    keteranganlogin=jostatus.getString("keterangan");

                    JSONArray jainfo=jo.getJSONArray("info");
                    JSONObject joinfo=jainfo.getJSONObject(0);
                    infosms=joinfo.getString("sms");
                    infocall=joinfo.getString("call");
                    infoweb=joinfo.getString("web");

                    if(statuslogin==0){
                        JSONArray jadata=jo.getJSONArray("data");
                        JSONObject jodata=jadata.getJSONObject(0);
                        jumlah=jodata.getInt("jumlah");
                        cnama=jodata.getString("nama");
                        cnohp=jodata.getString("nohp");
                        cemail=jodata.getString("email");
                        calamat=jodata.getString("alamat");
                        ciduser=jodata.getString("iduser");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder adb=new AlertDialog.Builder(LoginActivity.this);
                adb.setTitle("Informasi");
                adb.setMessage(error.getMessage());
                adb.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map params=new HashMap();
                params.put("key","bk201!@#");
                params.put("email",email);
                params.put("firebasetoken", token);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(1000*15,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);
        rq.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(statuslogin==0){
                    if(jumlah==1){
                        SharedPreferences.Editor spsedit=sps.edit();
                        spsedit.putString("email",email);
                        spsedit.commit();
                        Config.email=email;
                        Config.iduser=ciduser;
                        Config.alamat=calamat;
                        Config.nohp=cnohp;
                        Config.nama=cnama;
                        Config.infocall=infocall;
                        Config.infosms=infosms;
                        Config.infoweb=infoweb;
                        pd.dismiss();
                        Intent i=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        SharedPreferences.Editor spsedit=sps.edit();
                        spsedit.putString("email",email);
                        spsedit.commit();
                        insertuser(iduser,nama,email,nohp,foto);
                        pd.dismiss();
                    }
                }else{
                    AlertDialog.Builder adb=new AlertDialog.Builder(LoginActivity.this);
                    adb.setTitle("Informasi");
                    adb.setMessage(keterangan);
                    adb.show();
                }


            }
        });

    }




}
