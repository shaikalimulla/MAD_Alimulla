package com.example.alimu.imagegallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)titleText.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        titleText.setLayoutParams(layoutParams);
        titleText.setGravity(Gravity.CENTER);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.action_speech);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_back:
                onBackPressed();
                return true;

            case R.id.action_info:
                AlertDialog.Builder alert = new AlertDialog.Builder(imageDisplayActivity.this);
                alert.setMessage("1. This app will get all the images present in different folders from the phone and displays folder structure with images count as list view in the home page. \n"+
                        "2. Tap on any folder from the list to load images present in it in the gridview. \n"+
                        "3. Select any image in the gridview to open in full screen mode in separate page.\n"+
                        "4. Tap on Camera icon at the top left screen to open Camera to click instant picture. \n" +
                        "5. Tap on mic button at top right to open Google voice which opens Camera when you say \" Camera \". \n"+
                        "6. All the pictures taken from this app through camera will be stored in Pictures folder by default.\n"+
                        "7. Use long press gesture on image to perform operations like Copy, Cut and Delete. You can select multiple images.\n"+
                        "8. Use Back arrow icon present at top right of the screen to navigate back to previous page. \n"+
                        "9. Overflow icon at the top right have two options Info and Home. Tap on Info to view this page which summarizes app details. \n" +
                        "10. Tap on Home option to navigate back to home page from any screen. ");
                alert.setCancelable(true);

                alert.setNegativeButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDailog = alert.create();
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

    public void goBack(View view)
    {
        onBackPressed();
    }

}
