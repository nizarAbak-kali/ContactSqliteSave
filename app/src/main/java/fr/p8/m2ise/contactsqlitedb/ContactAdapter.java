package fr.p8.m2ise.contactsqlitedb;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nizar on 12/10/2016.
 */

public class ContactAdapter extends BaseAdapter {
    private Context mContext;

    BitmapFactory bitmapFactory;
    ArrayList<Contact> listContat;
    public ContactAdapter(Context context, ArrayList<Contact> listC)
    {
        super();
        mContext=context;
        listContat = listC;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(listContat.get(i) == null)
            return null;


        Contact c = listContat.get(i);

        Bitmap img = BitmapFactory.decodeByteArray(c.getThumbnail(),0,c.getThumbnail().length);
        String name = c.getName();
        String phoneNumber = String.valueOf(c.getPhoneNumber());

        ImageView imageView  = (ImageView) view.findViewById(R.id.photo);
        TextView textViewname = (TextView) view.findViewById(R.id.name);

        imageView.setImageBitmap(img);
        textViewname.setText(name);
        return view;
    }

    @Override
    public int getCount() {
        return listContat.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
