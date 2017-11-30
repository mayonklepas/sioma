package com.meteorit.sioma;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Minami on 05/11/2017.
 */

public class Homeadapter extends BaseAdapter {

    ArrayList<Homemodel> model=new ArrayList<>();
    Context ct;
    private static LayoutInflater lay;

    public Homeadapter(ArrayList<Homemodel> model, Context ct) {
        this.model = model;
        this.ct = ct;
        lay=(LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder{
        ImageView imgmain;
        TextView tjudul;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder h=new Holder();
        View rowview=lay.inflate(R.layout.maingridadapter,null);
        h.imgmain=(ImageView) rowview.findViewById(R.id.imgmain);
        h.tjudul=(TextView) rowview.findViewById(R.id.tjudul);
        if(i==2){
            h.tjudul.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        }else if(i==3){
            h.tjudul.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        }else{
            h.tjudul.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        }
        h.tjudul.setText(model.get(i).getJudul());
        h.imgmain.setImageResource(model.get(i).imgid);
        return rowview;
    }
}
