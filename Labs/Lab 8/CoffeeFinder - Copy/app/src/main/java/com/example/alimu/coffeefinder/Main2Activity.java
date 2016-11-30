package com.example.alimu.coffeefinder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private String suggestedCoffeeShopURL;
    private String suggestedCoffeeShop;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        suggestedCoffeeShop = getIntent().getExtras().getString("coffeeShopName");
        suggestedCoffeeShopURL = getIntent().getExtras().getString("coffeeShopURL");

        TextView text = (TextView) findViewById(R.id.coffeeDetails);
        text.setText("This is your suggested coffee shop "+suggestedCoffeeShop);

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
        intent.setData(Uri.parse(suggestedCoffeeShopURL));
        startActivity(intent);
    }

}
