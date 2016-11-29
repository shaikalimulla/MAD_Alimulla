package com.example.alimu.imagegallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class GridViewActivity extends MainActivity {
    public ArrayList<String> imageList = new ArrayList<String>();
    public ArrayList<String> dirList = new ArrayList<String>();
    public GridView gridview;
    public ImageAdapter imageAdapter;
    public String dir_name;
    public String call_click_image;
    LoadFilesAsync loadFilesAsync;
    ArrayList<Integer> positionsList = new ArrayList<>();
    public String SelectedImagePath;
    int selectCount = 0;

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
        System.out.println("GridActivity onCreate called");

        LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View customActionBarView = inflater.inflate(
                R.layout.custom_action_bar, null);

        TextView titleText = (TextView) customActionBarView.findViewById(R.id.title);

        dir_name = getIntent().getExtras().getString("dir_name");

        titleText.setText(dir_name);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)titleText.getLayoutParams();
        titleText.setLayoutParams(layoutParams);
        titleText.setGravity(Gravity.CENTER);

        System.out.println("dir_name "+dir_name+titleText.getText().toString());

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setCustomView(customActionBarView);

        actionBar.setDisplayShowCustomEnabled(true);

        gridview = (GridView) findViewById(R.id.imgGrid);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);
        gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridview.setMultiChoiceModeListener(new MultiChoiceModeListener());
        gridview.setDrawSelectorOnTop(true);
        gridview.setSelector(getResources().getDrawable(R.drawable.highlight_image));

        System.out.println("setAdapter called"+dir_name);

        loadFilesAsync = new LoadFilesAsync(imageAdapter, dir_name);
        loadFilesAsync.execute();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                System.out.println("onItemClick ");

                gridview.setSelector(new StateListDrawable());
                Intent intent = new Intent(getApplicationContext(), imageDisplayActivity.class);
                intent.putExtra("path", imageList.get(position));
                startActivity(intent);
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {
                System.out.println("onItemLongClick ");
                imageAdapter.setSelectedPosition(position);
                return true;
            }
        });
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

    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        String selecteditem;
        String dirName;
        boolean operationSelected = false;

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            System.out.println("MultiChoiceModeListener ");
            mode.setTitle("Select Items");
            mode.setSubtitle("1 item selected");
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.onselect_menu, menu);
            operationSelected = false;
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            System.out.println("onPrepareActionMode called ");
            return true;
        }

        public void sendBroadcastToGallery(String SelectedImagePath){
            System.out.println("sendBroadcastToGallery ");
            File f;
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
            if(SelectedImagePath == null) {
                f = new File(selecteditem);
            }
            else {
                f = new File(SelectedImagePath);
            }
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getApplicationContext().sendBroadcast(mediaScanIntent);
        }

        public void generateImages(File storageDir){
            String imageFileName = selecteditem.substring(selecteditem.lastIndexOf("/")+1, selecteditem.lastIndexOf(".jpg"));
            System.out.println("createNewFile "+ imageFileName);

            try {
                File srcImage = new File(selecteditem);
                File dstImage = File.createTempFile(imageFileName, ".jpg", storageDir);
                System.out.println("src "+ srcImage);
                System.out.println("dst "+ dstImage);
                FileInputStream inStream = new FileInputStream(srcImage);
                FileOutputStream outStream = new FileOutputStream(dstImage);
                FileChannel inChannel = inStream.getChannel();
                FileChannel outChannel = outStream.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                inStream.close();
                outStream.close();

                SelectedImagePath = dstImage.getAbsolutePath();
                System.out.println("SelectedImagePath "+ SelectedImagePath);
                sendBroadcastToGallery(SelectedImagePath);
            } catch (Exception e){
                System.out.println("Exception createNewFile");
            }
        }

        public void findImage(String path, String dirName, String operation){
            File mainDir = new File(path);
            if(mainDir.getName().equals(dirName)) {
                System.out.println("mainDir equals " + mainDir);
                if(operation.equals("delete")) {
                    File[] remainingFiles = mainDir.listFiles();
                    for (File curFile : remainingFiles) {
                        if(curFile.getAbsolutePath().equals(selecteditem)) {
                            System.out.println("mainDir curFile in pictures need to be removed"+curFile.getAbsolutePath());
                            System.out.println("mainDir curFile in pictures need to be removed"+selecteditem);
                            File file = new File(selecteditem);
                            file.delete();
                            sendBroadcastToGallery(null);
                        }
                    }
                } else if(operation.equals("add")){
                    generateImages(mainDir);
                }

            } else {
                File subDir = new File(path + "/"+dirName+"/");
                if(subDir.exists()){
                    System.out.println("subDir equals " + subDir);
                    if(operation.equals("delete")) {
                        File[] remainingFiles = subDir.listFiles();
                        for (File curFile : remainingFiles) {
                            if(curFile.getAbsolutePath().equals(selecteditem)) {
                                System.out.println("subDir curFile in pictures need to be removed"+curFile.getAbsolutePath());
                                System.out.println("subDir curFile in pictures need to be removed"+selecteditem);
                                File file = new File(selecteditem);
                                file.delete();
                                sendBroadcastToGallery(null);
                            }
                        }
                    } else if(operation.equals("add")){
                        generateImages(subDir);
                    }

                }
            }
        }

        public void removeImageFromPhone(String selecteditem2){
            String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
            String picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            System.out.println("enter removeImageFromPhone");
            File file = new File(selecteditem);
            dirName = file.getParent().substring(file.getParent().lastIndexOf(File.separator) + 1);
            System.out.println("last index dirName"+dirName);
            findImage(DCIMPath, dirName, "delete");
            findImage(picturesPath, dirName, "delete");
        }

        public void proceedDeletion(){
            System.out.println("positionsList size "+positionsList.size());
            Collections.sort(positionsList);
            for (int i = (positionsList.size() - 1); i >= 0; i--) {
                System.out.println("item loop vlue "+positionsList.get(i));
                if (positionsList.get(i)!=null) {
                    selecteditem = imageAdapter.getItem(positionsList.get(i));

                    System.out.println("item_delete "+selecteditem);

                    int firstPosition = gridview.getFirstVisiblePosition();
                    int lastPosition = gridview.getLastVisiblePosition();
                    int position = (Integer) positionsList.get(i);
                    if ((position < firstPosition) || (position > lastPosition)) {

                    } else{
                        View tv = (View) gridview.getChildAt(position - firstPosition);
                        tv.setBackgroundColor(Color.TRANSPARENT);
                        tv.invalidate();
                    }

                    imageAdapter.remove(selecteditem);
                    removeImageFromPhone(selecteditem);
                    System.out.println("deletion done");
                }
            }
            imageAdapter.notifyDataSetChanged();
            gridview.setDrawSelectorOnTop(false);
            gridview.invalidateViews();
            positionsList.clear();
        }

        public void requestPermission(){
            System.out.println("requestPermission ");
            AlertDialog.Builder alert = new AlertDialog.Builder(GridViewActivity.this);
            alert.setMessage("Are you sure you want to delete the file. Deleting from the app will permanently delete the image from the phone.");
            alert.setCancelable(true);

            alert.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            proceedDeletion();
                        }
                    });

            alert.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            destroyView();
                            //positionsList.clear();
                        }
                    });

            AlertDialog alertDailog = alert.create();
            alertDailog.show();
        }

        public void addSelectedImages(String dirName, String operation){
            System.out.println("addSelectedImages positionsList size "+positionsList.size());
            String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
            String picturesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            System.out.println("enter addSelectedImages");

            Collections.sort(positionsList);
            for (int i = (positionsList.size() - 1); i >= 0; i--) {
                System.out.println("item loop vlue "+positionsList.get(i));
                if (positionsList.get(i)!=null) {
                    selecteditem = imageAdapter.getItem(positionsList.get(i));

                    System.out.println("item_copy "+selecteditem);

                    int firstPosition = gridview.getFirstVisiblePosition();
                    int lastPosition = gridview.getLastVisiblePosition();
                    int position = (Integer) positionsList.get(i);
                    if ((position < firstPosition) || (position > lastPosition)) {

                    } else{
                        View tv = (View) gridview.getChildAt(position - firstPosition);
                        tv.setBackgroundColor(Color.TRANSPARENT);
                        tv.invalidate();
                    }

                    findImage(DCIMPath, dirName, "add");
                    findImage(picturesPath, dirName, "add");
                    File file = new File(selecteditem);
                    String currentDirName = file.getParent().substring(file.getParent().lastIndexOf(File.separator) + 1);
                    if(currentDirName.equals(dirName)){
                        imageAdapter.add(SelectedImagePath);
                    }
                    System.out.println("item_copy done");
                }
            }
            imageAdapter.notifyDataSetChanged();
            gridview.setDrawSelectorOnTop(false);
            gridview.invalidateViews();
            if(operation.equals("add")){
                positionsList.clear();
            } else if(operation.equals("cut")){

            }
        }

        public void cutSelectedImages(String dirName){
            addSelectedImages(dirName, "cut");
            proceedDeletion();
        }

        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_copy:
                    System.out.println("item_copy ");
                    operationSelected = true;

                    PopupMenu popup_copy = new PopupMenu(GridViewActivity.this, findViewById(R.id.item_copy));

                    for(int i = 0; i<MainActivity.dirList.size();i++){
                        System.out.println("PopupMenu copy "+MainActivity.dirList.get(i));
                        popup_copy.getMenu().add(Menu.NONE, i+1, Menu.NONE, MainActivity.dirList.get(i));
                    }

                    popup_copy.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            addSelectedImages(item.getTitle().toString(), "add");
                            Toast.makeText(GridViewActivity.this,"Selected pictures are copied into " + item.getTitle()+" directory.",Toast.LENGTH_SHORT).show();
                            mode.finish();
                            return true;
                        }
                    });

                    popup_copy.show();

                    return true;

                case R.id.item_delete:
                    System.out.println("item_delete "+positionsList.size());
                    operationSelected = true;
                    requestPermission();
                    mode.finish();
                    return true;

                case R.id.item_cut:
                    System.out.println("item_cut "+positionsList.size());
                    PopupMenu popup_cut = new PopupMenu(GridViewActivity.this, findViewById(R.id.item_copy));

                    for(int i = 0; i<MainActivity.dirList.size();i++){
                        System.out.println("PopupMenu cut "+MainActivity.dirList.get(i));
                        popup_cut.getMenu().add(Menu.NONE, i+1, Menu.NONE, MainActivity.dirList.get(i));
                    }

                    popup_cut.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            cutSelectedImages(item.getTitle().toString());
                            Toast.makeText(GridViewActivity.this,"Selected pictures are copied into " + item.getTitle()+" directory.",Toast.LENGTH_SHORT).show();
                            mode.finish();
                            return true;
                        }
                    });

                    popup_cut.show();
                    return true;

                default:
                    return false;
            }
        }

        public void destroyView(){
            for (int i = (positionsList.size() - 1); i >= 0; i--) {
                if (positionsList.get(i)!=null) {
                    selecteditem = imageAdapter.getItem(positionsList.get(i));
                    System.out.println("selecteditem "+selecteditem);

                    int firstPosition = gridview.getFirstVisiblePosition();
                    int lastPosition = gridview.getLastVisiblePosition();
                    int position = (Integer) positionsList.get(i);
                    if ((position < firstPosition) || (position > lastPosition)) {

                    } else{
                        View tv = (View) gridview.getChildAt(position - firstPosition);
                        tv.setBackgroundColor(Color.TRANSPARENT);
                        tv.invalidate();
                    }
                    System.out.println("deletion done");
                }
            }
            imageAdapter.notifyDataSetChanged();
            gridview.setDrawSelectorOnTop(false);
            gridview.invalidateViews();
            positionsList.clear();
        }

        public void onDestroyActionMode(ActionMode mode) {
            System.out.println("onDestroyActionMode called "+positionsList.size()+ "operationSelected:" + operationSelected);

            if(operationSelected){
                return;
            }
            destroyView();
        }


        public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                              boolean checked) {
            System.out.println("onItemCheckedStateChanged position: "+position);
            selectCount = gridview.getCheckedItemCount();
            System.out.println("onItemCheckedStateChanged "+selectCount);

            int firstPosition = gridview.getFirstVisiblePosition();
            int lastPosition = gridview.getLastVisiblePosition();
            gridview.setSelector(getResources().getDrawable(R.drawable.highlight_image));

            if(checked){
                System.out.println("checked " + (position - firstPosition));

                if ((position < firstPosition) || (position > lastPosition)) {

                } else {
                    View tv = (View) gridview.getChildAt(position - firstPosition);
                    tv.setBackgroundColor(Color.RED);
                    tv.invalidate();
                }

                positionsList.add(position);

            }else{
                System.out.println("not checked "+ (position - firstPosition));
                if ((position < firstPosition) || (position > lastPosition)) {

                } else {
                    View tv = (View) gridview.getChildAt(position - firstPosition);
                    tv.setBackgroundColor(Color.TRANSPARENT);
                    tv.invalidate();
                    gridview.setSelector(new StateListDrawable());
                }

                positionsList.remove((Integer)position);
            }
            switch (selectCount) {
                case 1:
                    mode.setSubtitle("1 item selected");
                    break;
                default:
                    mode.setSubtitle("" + selectCount + " items selected");
                    break;
            }
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
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        System.out.println("onCreateContextMenu ");
        menu.setHeaderTitle("Context Menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.edit_menu);
        String menuItemName = menuItems[menuItemIndex];
        Toast.makeText(getApplicationContext(),menuItemName,Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onResume()
    {
        System.out.println("on Resume grid ");
        super.onResume();
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
        } else if(requestCode == REQUEST_TAKE_PHOTO && resultCode != RESULT_OK){
            System.out.println("onActivityResult Grid called resultCode not ok ");
            File file = new File(currentImagePath );
            file.delete();
            sendBroadcastToGallery();
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

    public void sendBroadcastToGallery(){
        System.out.println("sendBroadcastToGallery ");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        File f = new File(currentImagePath);

        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }

    public class ImageAdapter extends BaseAdapter
    {
        private Context cont;
        private int selectedPosition = -1;
        public ImageAdapter(Context c)
        {
            cont = c;
        }
        public int getCount()
        {
            return imageList.size();
        }
        public String getItem(int position)
        {
            return imageList.get(position);
        }
        public long getItemId(int position)
        {
            return 0;
        }
        public void add(String path){
            imageList.add(path);
        }
        public void remove(String path){
            imageList.remove(path);
        }
        public void setSelectedPosition(int position) {
            selectedPosition = position;
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
}
