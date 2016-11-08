package com.example.alimu.imagegallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alimu on 11/4/2016.
 */

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> dirList = new ArrayList<String>();
    public ArrayList<Integer> dirSizeList = new ArrayList<Integer>();

    public CustomList(Activity context, ArrayList<String> dirList , ArrayList<Integer> dirSizeList) {
        super(context, R.layout.list_row_view, dirList);
        this.context = context;
        this.dirList = dirList;
        this.dirSizeList = dirSizeList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_row_view, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView folderSize = (TextView) rowView.findViewById(R.id.folder_size);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(dirList.get(position));
        folderSize.setText(String.valueOf(dirSizeList.get(position)));
        System.out.println("check " + dirSizeList.get(position));

        imageView.setImageResource(R.drawable.pattern);
        return rowView;
    }
}
