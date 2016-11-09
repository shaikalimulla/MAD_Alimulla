package com.example.alimu.lab7;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void findDetails(View view) {
        EditText user = (EditText) findViewById(R.id.userNameText);
        String user_name = user.getText().toString();
        Spinner degree_spinner = (Spinner) findViewById(R.id.spinner);
        String degree_value = String.valueOf(degree_spinner.getSelectedItem());

        if (user_name.matches("")) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.authorizeToggleButton);
        boolean authorize = toggle.isChecked();
        String authorize_str="";
        if(authorize) {
            authorize_str = " need authorization";
        } else {
            authorize_str = " does not need authorization";
        }

        Switch visa_switch = (Switch) findViewById(R.id.switch1);
        boolean visa = visa_switch.isChecked();
        String visa_str="";
        if(visa){
            visa_str="require";
        } else {
            visa_str="does not require";
        }

        ImageView imageView = (ImageView) findViewById(R.id.img);
        RadioGroup job_type = (RadioGroup) findViewById(R.id.job_type_group);
        String type="";
        int type_seleceted = job_type.getCheckedRadioButtonId();
        switch(type_seleceted){
            case R.id.radioButton:
                type="in computer science";
                imageView.setImageResource(R.drawable.cs);
                break;
            case R.id.radioButton2:
                type="in electrical engineering";
                imageView.setImageResource(R.drawable.ee);
                break;
            case R.id.radioButton3:
                type="in mechanical engineering";
                imageView.setImageResource(R.drawable.mech);
                break;
            case R.id.radioButton4:
                type="in civil engineering";
                imageView.setImageResource(R.drawable.civil);
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
            chkbox_selected+= " FullTime";
        }

        CheckBox chkbox2 = (CheckBox) findViewById(R.id.checkBox5);
        boolean checked2 = chkbox2.isChecked();
        if(checked2){
            chkbox_selected+= " PartTime";
        }

        CheckBox chkbox3 = (CheckBox) findViewById(R.id.checkBox4);
        boolean checked3 = chkbox3.isChecked();
        if(checked3){
            chkbox_selected+= " Intern";
        }

        CheckBox chkbox4 = (CheckBox) findViewById(R.id.checkBox2);
        boolean checked4 = chkbox4.isChecked();
        if(checked4){
            chkbox_selected+= " Co-op";
        }
        String positionList = "";
        if(chkbox_selected.matches("")){
            positionList=".";
        } else {
            positionList = " and interested in"+chkbox_selected+" position(s).";
        }
        TextView text = (TextView) findViewById(R.id.detailsText);
        text.setText(user_name+" has a "+ degree_value +" degree "+type+ authorize_str+" and "+visa_str+" visa to work"+positionList);

    }
}
