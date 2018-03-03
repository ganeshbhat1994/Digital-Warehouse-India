package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import myapplication.transportwarehouse.Transport_Reg_Activity;

public class NewReg extends Activity {
    Button obutton;
    Button cbutton;
    Button transportbutton;
    //boolean hasLoggedIn;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reg);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);

        transportbutton = (Button) findViewById(R.id.transportbutton);
        transportbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Transport_Reg_Activity.class);
                startActivity(intent);
            }
        });

        obutton=(Button)findViewById(R.id.ownerbutton);
        cbutton=(Button)findViewById(R.id.customerbutton);
        obutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),OwnerReg.class);
                startActivity(i);


            }
        });
        cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(),Reg.class);
                startActivity(i);


            }
        });
    }



}
