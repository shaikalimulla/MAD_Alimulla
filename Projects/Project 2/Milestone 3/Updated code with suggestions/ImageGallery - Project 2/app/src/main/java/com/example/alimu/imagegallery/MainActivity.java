package com.example.alimu.imagegallery;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
References:
1. https://developer.android.com/guide/topics/ui/layout/listview.html
2. https://developer.android.com/guide/topics/ui/layout/gridview.html
3. https://developer.android.com/training/camera/photobasics.html
 */

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

        LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View customActionBarView = inflater.inflate(
                R.layout.custom_action_bar, null);

        Context c = this;
        Activity host = (Activity) (c);
        hostName = host.getClass().getSimpleName();

        if(hostName.equals("MainActivity")){
            ImageButton imgBtn = (ImageButton) customActionBarView.findViewById(R.id.action_back);
            imgBtn.setVisibility(View.INVISIBLE);
        }

        TextView titleText = (TextView) customActionBarView.findViewById(R.id.title);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)titleText.getLayoutParams();
        titleText.setLayoutParams(layoutParams);
        titleText.setGravity(Gravity.CENTER);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setCustomView(customActionBarView);

        actionBar.setDisplayShowCustomEnabled(true);

        if (savedInstanceState != null) {
            currentImagePath = savedInstanceState.getString("currentImagePath");
        }

        String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        getFolderSize(DCIMPath);

        String picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        getFolderSize(picturesPath);

        adapter = new CustomList(MainActivity.this, dirList, dirSizeList);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            Intent intent = new Intent(getApplicationContext(), GridViewActivity.class);
            intent.putExtra("dir_name", dirList.get(position));
            startActivity(intent);
        }
        });

        clickImageButton = (ImageButton) findViewById(R.id.clickBtn);
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(cameraPermissionCheck!= PackageManager.PERMISSION_GRANTED || writePermissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
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
            case R.id.action_back:
                onBackPressed();
                return true;

            case R.id.action_speech:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, REQUEST_VOICE_SPEECH);

                return true;

            case R.id.action_info:
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("1. This app will get all the images present in different folders from the phone and displays folder structure with images count as list view in the home page. \n"+
                        "2. Tap on any folder from the list to load images present in it in the gridview. \n"+
                        "3. Select any image in the gridview to open in full screen mode in separate page.\n"+
                        "4. Tap on Camera icon at the top left screen to open Camera to click instant picture. \n" +
                        "5. Tap on mic button at top right to open Google voice which opens Camera when you say \" Camera \". \n"+
                        "6. All the pictures taken from this app through camera will be stored in Pictures folder by default.\n"+
                        "7. Use Back arrow icon present at top right of the screen to navigate back to previous page. \n"+
                        "8. Overflow icon at the top right have two options Info and Home. Tap on Info to view this page which summarizes app details. \n" +
                        "9. Tap on Home option to navigate back to home page from any screen. ");
                alert.setCancelable(true);

                alert.setNegativeButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDailog = alert.create();
                alertDailog.setCanceledOnTouchOutside(true);
                alertDailog.show();
                return true;

            case R.id.action_home:
                Intent home_intent = new Intent(this, MainActivity.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home_intent);
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

    public void goBack(View view)
    {
        onBackPressed();
    }

    public void clickPic(View view)
    {
        Context c = this;
        Activity host = (Activity) (c);
        hostName = host.getClass().getSimpleName();

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
        System.out.println("image created"+image.length()+ " "+image);

        currentImagePath = image.getAbsolutePath();
        System.out.println("image create exit"+currentImagePath);
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        saveState.putString("currentImagePath", currentImagePath);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("onActivityResult called");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            sendImageToGallery();
        } else if(requestCode == REQUEST_TAKE_PHOTO && resultCode != RESULT_OK){
            System.out.println("onActivityResult called resultCode not ok ");
            File file = new File(currentImagePath );
            file.delete();
            sendBroadcastToGallery();
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

    public void sendBroadcastToGallery(){
        System.out.println("sendBroadcastToGallery ");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        File f = new File(currentImagePath);

        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);
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