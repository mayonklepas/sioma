package com.meteorit.sioma;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Minami on 13/11/2017.
 */

public class Inboxadapter extends RecyclerView.Adapter<Inboxadapter.Holder> {

    ArrayList<Inboxmodel> lsadapter=new ArrayList<>();
    Context ct;

    public Inboxadapter(ArrayList<Inboxmodel> lsadapter, Context ct) {
        this.lsadapter = lsadapter;
        this.ct = ct;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.infoadapter,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String string = "20170412";
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(lsadapter.get(position).getTanggal());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat displayFormat = new SimpleDateFormat("d MMMM , yyyy", Locale.ENGLISH);
        holder.nama.setText("SATKORLANTAS MATARAM");
        holder.pesan.setText(lsadapter.get(position).getPesan());
        holder.tanggal.setText(displayFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return lsadapter.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView nama,pesan,tanggal;
        public Holder(View itemView) {
            super(itemView);
            nama=(TextView) itemView.findViewById(R.id.nama);
            pesan=(TextView) itemView.findViewById(R.id.pesan);
            tanggal=(TextView) itemView.findViewById(R.id.tanggal);
        }
    }
}
