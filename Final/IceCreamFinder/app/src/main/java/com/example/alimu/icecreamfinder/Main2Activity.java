package com.example.alimu.icecreamfinder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    private String suggestedicecreamShopURL;
    private String suggestedicecreamShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        suggestedicecreamShop = getIntent().getExtras().getString("icecreamShopName");
        suggestedicecreamShopURL = getIntent().getExtras().getString("icecreamShopURL");

        TextView text = (TextView) findViewById(R.id.icecreamDetails);
        text.setText("You should check out "+suggestedicecreamShop + " ice cream shop");

        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        View.OnClickListener onclick = new View.OnClickListener(){
            public void onClick(View view){
                loadWebSite(view);
            }
        };
        imageButton.setOnClickListener(onclick);
    }

    public void loadWebSite(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(suggestedicecreamShopURL));
        startActivity(intent);
    }
}
