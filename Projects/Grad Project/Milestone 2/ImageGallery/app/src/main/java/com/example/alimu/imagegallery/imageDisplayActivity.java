package com.example.alimu.imagegallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class imageDisplayActivity extends AppCompatActivity {
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View customActionBarView = inflater.inflate(
                R.layout.custom_action_bar, null);

        TextView titleText = (TextView) customActionBarView.findViewById(R.id.title);
        ImageButton imgBtn = (ImageButton) customActionBarView.findViewById(R.id.clickBtn);
        imgBtn.setVisibility(View.INVISIBLE);
        path = getIntent().getExtras().getString("path");

        File file = new File(path);
        String dirName = file.getParent().substring(file.getParent().lastIndexOf(File.separator) + 1);

        titleText.setText(dirName);
        System.out.println("dirName "+dirName+titleText.getText().toString());

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

        ImageView imageView = (ImageView) findViewById(R.id.imgDisplay);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 4;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);

        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("onBackPressed image display activity ");
        /*
        Intent intent = new Intent(getApplicationContext(), GridViewActivity.class);
        intent.putExtra("refresh", true);
        startActivityForResult(intent, 4);
        System.out.println("startActivityForResult ");
        */
    }
}
