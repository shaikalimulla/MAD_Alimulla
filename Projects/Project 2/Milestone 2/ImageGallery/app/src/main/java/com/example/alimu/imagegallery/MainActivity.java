package com.example.alimu.imagegallery;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> dirList = new ArrayList<String>();
    public ArrayList<Integer> dirSizeList = new ArrayList<Integer>();
    public ImageButton clickImageButton;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_VOICE_SPEECH = 2;
    public String currentImagePath;
    ListView list;
    public CustomList adapter;
    public String hostName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("onCreate called");
/*
        final ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


         ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayShowHomeEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);


        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

*/

        LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View customActionBarView = inflater.inflate(
                R.layout.custom_action_bar, null);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setCustomView(customActionBarView);

        actionBar.setDisplayShowCustomEnabled(true);

        String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        getFolderSize(DCIMPath);

        String picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        getFolderSize(picturesPath);

        System.out.println("dirList " + dirList);
        System.out.println("dirSizeList " + dirSizeList);
        adapter = new CustomList(MainActivity.this, dirList, dirSizeList);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            //Toast.makeText(MainActivity.this, "You Clicked at " +dirList.get(position), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), GridViewActivity.class);
            intent.putExtra("dir_name", dirList.get(position));
            startActivity(intent);
        }
        });

        clickImageButton = (ImageButton) findViewById(R.id.clickBtn);
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);

    }

    @Override
    public void onResume()
    {
        System.out.println("on Resume ");
        super.onResume();
        /*int picIndex = dirList.indexOf("Pictures");

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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                clickImageButton.setEnabled(true);
            }
        }
    }

    public void clickPic(View view)
    {
        /*Intent intent = new Intent(getApplicationContext(), GridViewActivity.class);
        intent.putExtra("call_click_image", "call");
        startActivity(intent);*/
        System.out.println("click picture called");
        Context c = this;
        System.out.println("click picture context"+ c);
        Activity host = (Activity) (c);
        hostName = host.getClass().getSimpleName();
        System.out.println("click picture host "+ hostName);

        Intent clickPicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
        if (clickPicIntent.resolveActivity(getPackageManager()) != null)
        {
            File image = null;
            try
            {
                image = generateImage();
            }
            catch (IOException ex)
            {
            }

            if (image != null)
            {
                System.out.println("image not null");
                clickPicIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                startActivityForResult(clickPicIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File generateImage() throws IOException
    {
        System.out.println("image create called");
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format( new Date());
        String imageFileName = "JPEG_" + time + "_" ;
        System.out.println("imageFileName created");
        File saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        System.out.println("saveDir created");
        File image = File.createTempFile(imageFileName, ".jpg", saveDir);
        System.out.println("image created");
        // Save a file: path for use with ACTION_VIEW intents
        currentImagePath = image.getAbsolutePath();
        System.out.println("image create exit"+currentImagePath);
        return image;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("onActivityResult called");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            sendImageToGallery();
        }

        if (requestCode == 12 && resultCode == RESULT_OK) {
            adapter = new CustomList(MainActivity.this, dirList, dirSizeList);
            list=(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);
        }

        if (requestCode == REQUEST_VOICE_SPEECH && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(),voice_text,Toast.LENGTH_LONG).show();
            if(voice_text.equals("camera")){
                System.out.println("camera with speech called");
                Context c = this;
                View view = View.inflate(c, R.layout.activity_main, null);
                clickPic(view);
            }
        }

    }

    public void sendImageToGallery(){
        System.out.println("REQUEST_TAKE_PHOTO ok");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        File f = new File(currentImagePath );
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        System.out.println("REQUEST_TAKE_PHOTO before" +dirSizeList.size() + dirSizeList);
        int picIndex = dirList.indexOf("Pictures");
        dirSizeList.set(picIndex, dirSizeList.get(picIndex) + 1);
        System.out.println("REQUEST_TAKE_PHOTO ok" + picIndex+ dirSizeList.size()+dirSizeList);
        adapter.notifyDataSetChanged();
        System.out.println("value changed ok");
    }

    public void getFolderSize(String path){
        int numOfFiles = 0;
        ArrayList<String> localDirList = new ArrayList<String>();
        ArrayList<Integer> localDirSizeList = new ArrayList<Integer>();

        File mainDir = new File(path);
        System.out.println("files " + mainDir);
        localDirList.add(mainDir.getName());
        File[] files = mainDir.listFiles();

        if(mainDir.isDirectory()){
            if(files == null){
                return;
            }
        }

        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("old files " + file.getName());
                localDirList.add(file.getName());
            } else {
                numOfFiles++;
            }
        }

        System.out.println("count files" + numOfFiles);

        localDirSizeList.add(numOfFiles);
        for(int i =1; i<localDirList.size();i++){
            File targetDir = new File(path + "/"+localDirList.get(i)+"/");
            System.out.println("new files " + targetDir);
            File[] remainingFiles = targetDir.listFiles();
            System.out.println("count files" + remainingFiles.length);
            localDirSizeList.add(remainingFiles.length);
        }

        dirList.addAll(localDirList);
        dirSizeList.addAll(localDirSizeList);
    }
}

