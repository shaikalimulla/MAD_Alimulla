package com.example.alimu.coffeefinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private searchCoffeeShop myCoffeeShop = new searchCoffeeShop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        View.OnClickListener onclick = new View.OnClickListener(){
            public void onClick(View view){
                findCoffee(view);
            }
        };
        button.setOnClickListener(onclick);
    }

    public void findCoffee(View view){
        Spinner crowdSpinner = (Spinner)findViewById(R.id.spinner);
        Integer crowd = crowdSpinner.getSelectedItemPosition();
        myCoffeeShop.setCoffeeShop(crowd);
        String suggestedCoffeeShop = myCoffeeShop.getCoffeeShop();
        String suggestedCoffeeShopURL = myCoffeeShop.getCoffeeShopURL();
        System.out.println(suggestedCoffeeShop);
        System.out.println(suggestedCoffeeShopURL);

        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("coffeeShopName", suggestedCoffeeShop);
        intent.putExtra("coffeeShopURL", suggestedCoffeeShopURL);
        startActivity(intent);
    }
}
