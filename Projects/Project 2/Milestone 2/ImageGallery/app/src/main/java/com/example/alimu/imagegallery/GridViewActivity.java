package com.example.alimu.imagegallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GridViewActivity extends MainActivity {
    public ArrayList<String> imageList = new ArrayList<String>();
    public ArrayList<String> dirList = new ArrayList<String>();
    public GridView gridview;
    public ImageAdapter imageAdapter;
    public String dir_name;
    public String call_click_image;
    LoadFilesAsync loadFilesAsync;

    public class LoadFilesAsync extends AsyncTask<Void, String, Void> {

        ImageAdapter asyncAdapter;
        String dir_name;

        public LoadFilesAsync(ImageAdapter adapter, String dir_name) {
            asyncAdapter = adapter;
            this.dir_name=dir_name;
        }
        @Override
        protected void onPreExecute() {
            asyncAdapter.clear();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("targetPath called"+dir_name);

            String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
            getAllImages(DCIMPath);

            String picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            getAllImages(picturesPath);

            System.out.println("pictures done "+dir_name);

            return null;
        }

        public void getAllImages(String path){
            File mainDir = new File(path);
            System.out.println("targetDirector called");

            if(mainDir.getName().equals(dir_name)){
                File[] files = mainDir.listFiles();
                for (File curFile : files) {
                    if (curFile.isDirectory()) {
                    } else {
                        System.out.println("files called");
                        imageAdapter.add(curFile.getAbsolutePath());
                    }
                }
            } else {
                File subDir = new File(path + "/"+dir_name+"/");
                if(subDir.exists()){
                    System.out.println("new files " + subDir);
                    File[] remainingFiles = subDir.listFiles();
                    for (File curFile : remainingFiles) {
                        if (curFile.isDirectory()) {

                        } else {
                            System.out.println("files called");
                            imageAdapter.add(curFile.getAbsolutePath());
                        }
                    }
                    System.out.println("targetDir done");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        /*
        call_click_image = getIntent().getExtras().getString("call_click_image");

        if(!call_click_image.isEmpty() && call_click_image.equals("call")){
            saveImage();
        }
        */

        LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View customActionBarView = inflater.inflate(
                R.layout.custom_action_bar, null);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setCustomView(customActionBarView);

        actionBar.setDisplayShowCustomEnabled(true);

        dir_name = getIntent().getExtras().getString("dir_name");

        gridview = (GridView) findViewById(R.id.imgGrid);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);
        System.out.println("setAdapter called"+dir_name);

        loadFilesAsync = new LoadFilesAsync(imageAdapter, dir_name);
        loadFilesAsync.execute();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), imageDisplayActivity.class);
                intent.putExtra("path", imageList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_speech:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, REQUEST_VOICE_SPEECH);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()  {
        super.onBackPressed();
        System.out.println("onBackPressed ");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("refresh", true);
        startActivityForResult(intent, 12);
        System.out.println("startActivityForResult ");
/*
            if(adapter!= null && list!=null) {

                int picIndex = dirList.indexOf("Pictures");

                System.out.println("onBackPressed Grid ok" + picIndex+ dirSizeList.get(picIndex));

                adapter = new CustomList(this, dirList, dirSizeList);
                list = (ListView) findViewById(R.id.list);

                //list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onResume()
    {
        System.out.println("on Resume grid ");
        super.onResume();
/*
        if(imageAdapter!= null && gridview!=null) {
            System.out.println("on Resume grid image adapter not null");
            imageAdapter.notifyDataSetChanged();
        }

        int picIndex = dirList.indexOf("Pictures");

        System.out.println("on Resume ok" + picIndex+ dirSizeList.get(picIndex));

        adapter.notifyDataSetChanged();

        if(adapter!= null && list!=null) {
            System.out.println("on Resume ok list not null");
            //adapter = new CustomList(MainActivity.this, dirList, dirSizeList);
            //list = (ListView) findViewById(R.id.list);
            //list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }*/
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("onActivityResult Grid called");

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            System.out.println("onActivityResult Grid reqcode");

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
            File f = new File(currentImagePath );
            System.out.println("REQUEST_TAKE_PHOTO ok"+currentImagePath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            imageAdapter.add(currentImagePath);
            gridview.invalidateViews();
            System.out.println("REQUEST_TAKE_PHOTO done");

            /*
            //if(data.getData()!=null) {

                //currentPicPath = data.getExtras().getString("current_pic_path");
                //System.out.println("onActivityResult Grid called " + currentPicPath);
            //}

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
            File f = new File(currentPicPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            */
            //imageAdapter.add(currentPicPath);
        }

        if (requestCode == REQUEST_VOICE_SPEECH && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(),voice_text,Toast.LENGTH_LONG).show();
            if(voice_text.equals("camera")){
                System.out.println("camera with speech called");
                Context c = this;
                View view = View.inflate(c, R.layout.activity_grid_view, null);
                clickPic(view);
            }
        }

    }

    public class ImageAdapter extends BaseAdapter
    {
        private Context cont;
        public ImageAdapter(Context c)
        {
            cont = c;
        }
        public int getCount()
        {
            return imageList.size();
        }
        public Object getItem(int position)
        {
            return null;
        }
        public long getItemId(int position)
        {
            return 0;
        }
        public void add(String path){
            imageList.add(path);
        }
        void clear() {
            imageList.clear();
        }
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;

            if (convertView == null)
            {
                imageView = new ImageView(cont);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            }
            else
            {
                imageView = (ImageView) convertView;
            }

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = false ;
            bmOptions.inSampleSize = 4;
            bmOptions.inPurgeable = true ;
            Bitmap bitmap = BitmapFactory.decodeFile(imageList.get(position), bmOptions);

            imageView.setImageBitmap(bitmap);

            return imageView;
        }
    }

    public void saveImage()
    {

    }
}
