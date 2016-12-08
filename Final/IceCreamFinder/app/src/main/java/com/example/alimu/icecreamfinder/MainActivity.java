package com.example.alimu.icecreamfinder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    Button findBtn;
    private searchIceCream findIceCreamShop = new searchIceCream();
    String saveStr = "";
    String flavor_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findBtn = (Button) findViewById(R.id.findButton);
        findBtn.setVisibility(View.INVISIBLE);
        if (savedInstanceState != null) {
            System.out.println("flavor_val");
            saveStr = savedInstanceState.getString("saveStr");
            flavor_value = savedInstanceState.getString("flavor_value");
            TextView text = (TextView) findViewById(R.id.detailsText);
            if(saveStr.isEmpty()){

            } else {
                text.setText(saveStr);
            }
            findBtn.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) findViewById(R.id.img);
            switch(flavor_value){
                case "Death by chocolate":
                    imageView.setImageResource(R.drawable.chocolate);
                    break;
                case "Salted caramel":
                    imageView.setImageResource(R.drawable.caramel);
                    break;
                case "Cookies and Cream":
                    imageView.setImageResource(R.drawable.cookiescream);
                    break;
                default:
                    //imageView.setImageResource(R.drawable.chocolate);
                    break;
            }
        }
    }

    public void findDetails(View view) {
        EditText dessertText = (EditText) findViewById(R.id.dessertText);
        String dessert_name = dessertText.getText().toString();
        Spinner flavor_spinner = (Spinner) findViewById(R.id.spinner);
        flavor_value = String.valueOf(flavor_spinner.getSelectedItem());
        ImageView imageView = (ImageView) findViewById(R.id.img);

        System.out.println("flavor_val"+ flavor_value);
        if (dessert_name.matches("")) {
            Toast.makeText(this, "Please enter a treat name to continue", Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        switch(flavor_value){
            case "Death by chocolate":
                imageView.setImageResource(R.drawable.chocolate);
                break;
            case "Salted caramel":
                imageView.setImageResource(R.drawable.caramel);
                break;
            case "Cookies and Cream":
                imageView.setImageResource(R.drawable.cookiescream);
                break;
            default:
                //imageView.setImageResource(R.drawable.chocolate);
                break;
        }

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        boolean toggleVal = toggle.isChecked();
        String toggle_str="";
        if(toggleVal) {
            toggle_str = " cone ";
        } else {
            toggle_str = " cup ";
        }

        Switch dairy_switch = (Switch) findViewById(R.id.switch1);
        boolean dairy = dairy_switch.isChecked();
        String dairy_str="";
        if(dairy){
            dairy_str=" dairy free ";
        } else {
            dairy_str="";
        }

        RadioGroup job_type = (RadioGroup) findViewById(R.id.job_type_group);
        String type="";
        int type_seleceted = job_type.getCheckedRadioButtonId();
        switch(type_seleceted){
            case R.id.radioButton:
                type="ice cream";
                break;
            case R.id.radioButton2:
                type="frozen yogurt";
                break;
            case R.id.radioButton3:
                type="gelato";
                break;
            case -1:
            default:
                type="";
                break;
        }

        String chkbox_selected = "";
        CheckBox chkbox = (CheckBox) findViewById(R.id.checkBox3);
        boolean checked = chkbox.isChecked();
        if(checked){
            chkbox_selected+= "sprinkles";
        }

        CheckBox chkbox2 = (CheckBox) findViewById(R.id.checkBox5);
        boolean checked2 = chkbox2.isChecked();
        if(checked2){
            chkbox_selected+= " hot fudge ";
        }

        CheckBox chkbox3 = (CheckBox) findViewById(R.id.checkBox4);
        boolean checked3 = chkbox3.isChecked();
        if(checked3){
            chkbox_selected+= "nuts";
        }

        String toppingList = "";
        if(chkbox_selected.matches("")){
            toppingList=".";
        } else {
            toppingList = "with "+chkbox_selected+" topping(s).";
        }
        TextView text = (TextView) findViewById(R.id.detailsText);
        saveStr = "My "+dessert_name+" is a "+ flavor_value+" "+ dairy_str + type + toggle_str+ toppingList;

        text.setText("My "+dessert_name+" is a "+ flavor_value+" "+ dairy_str + type + toggle_str+ toppingList);

        //Button findBtn = (Button) findViewById(R.id.findButton);
        findBtn.setVisibility(View.VISIBLE);
    }

    public void findIceCream(View view) {
        Spinner flavorSpinner = (Spinner)findViewById(R.id.spinner);
        Integer flavor = flavorSpinner.getSelectedItemPosition();
        findIceCreamShop.seticecreamShop(flavor);
        String suggestedCoffeeShop = findIceCreamShop.geticecreamShop();
        String suggestedCoffeeShopURL = findIceCreamShop.geticecreamShopURL();
        System.out.println(suggestedCoffeeShop);
        System.out.println(suggestedCoffeeShopURL);

        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("icecreamShopName", suggestedCoffeeShop);
        intent.putExtra("icecreamShopURL", suggestedCoffeeShopURL);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
        saveState.putString("saveStr", saveStr);
        saveState.putString("flavor_value", flavor_value);
    }
}
