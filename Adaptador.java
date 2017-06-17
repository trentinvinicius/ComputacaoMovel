package com.example.vinicius.trabalhofinal;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.util.CircularIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 07.06.17.
 */
public class Adaptador extends BaseAdapter {
    private Context context;
    private ArrayList<String> nomes = new ArrayList<String>();
    private ArrayList<byte[]> fotos = new ArrayList<byte[]>();

    public Adaptador(Context context, ArrayList<String> nomes, ArrayList<byte[]> fotos){
        this.context = context;
        this.nomes = nomes;
        this.fotos = fotos;
    }

    @Override
    public int getCount() {
        return nomes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View gv;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){

            gv = new View(context);
            gv = inflater.inflate(R.layout.gridviewlayout, null);
            TextView tv = (TextView)gv.findViewById(R.id.gvText);
            CircleImageView iv = (CircleImageView) gv.findViewById(R.id.gvImage);
            tv.setText(this.nomes.get(i));
            Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/stylusbt.ttf");
            tv.setTypeface(type);
            tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            try{
                Bitmap foto = BitmapFactory.decodeByteArray(fotos.get(i), 0, fotos.get(i).length);
                iv.setImageBitmap(foto);
            }catch(Exception e){
                iv.setImageResource(R.drawable.imgdefaut);
            }
            iv.setAdjustViewBounds(false);
        } else {
            gv = (View) convertView;
        }

        return gv;

    }

}
