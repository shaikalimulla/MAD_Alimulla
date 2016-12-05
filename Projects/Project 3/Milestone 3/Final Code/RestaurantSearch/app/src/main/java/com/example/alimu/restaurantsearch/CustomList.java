package com.example.alimu.restaurantsearch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by alimu on 11/4/2016.
 */

public class CustomList extends ArrayAdapter<String> implements LoadImage.Listener{

    private final Activity context;
    private ArrayList<String> nameList = new ArrayList<String>();
    private ArrayList<String> vicinityList = new ArrayList<String>();
    private ArrayList<String> distanceList = new ArrayList<String>();
    private ArrayList<String> availabilityList = new ArrayList<String>();
    private ArrayList<String> iconList = new ArrayList<String>();
    private ImageView imageView;

    public CustomList(Activity context, ArrayList<String> nameList , ArrayList<String> vicinityList, ArrayList<String> distanceList, ArrayList<String> availabilityList, ArrayList<String> iconList) {
        super(context, R.layout.list_row_view, nameList);
        this.context = context;
        this.nameList = nameList;
        this.vicinityList = vicinityList;
        this.distanceList = distanceList;
        this.availabilityList = availabilityList;
        this.iconList = iconList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_row_view, null, true);
        TextView placeName = (TextView) rowView.findViewById(R.id.placeName);
        TextView address = (TextView) rowView.findViewById(R.id.address);
        TextView distance = (TextView) rowView.findViewById(R.id.distance);
        TextView availability = (TextView) rowView.findViewById(R.id.availability);

        imageView = (ImageView) rowView.findViewById(R.id.img);

        placeName.setText(Html.fromHtml(nameList.get(position)));
        placeName.setMovementMethod(new ScrollingMovementMethod());
        address.setText(String.valueOf(vicinityList.get(position)));
        distance.setText(distanceList.get(position)+" Miles");
        System.out.println("check " + nameList.get(position));
        if(availabilityList.get(position).equals("true")){
            availability.setText("Open Now");
        }

        if(availabilityList.get(position).equals("false")){
            availability.setText("Closed Now");
            availability.setTextColor(Color.DKGRAY);
        }

        String ImageUrl = iconList.get(position);
        LoadImage load = new LoadImage(this);
        load.execute(ImageUrl);
        return rowView;
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onError() {

    }
}
