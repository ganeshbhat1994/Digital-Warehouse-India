package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class About extends Activity {
 TextView about;
    ActionBar actionBar;
    public String aboutapp="Developed by:\nSIT Team,Tumkur\n\n\nTeam Head:\n\tVijay Kumar,Asst.Proffessor,SIT\n\n\nTechnical Team:\n\tRenuprasad B R\n\tShriganesh Bhat\n\tRavikiran D G" +
            "\n\tPranav Kamath\n\tYogendra J S\n\tManjesh\n\tAditya Raj\n\tAditya Banerjee\n\tAnurag Gupta\n\tRohith\n\n\n THANKS TO SIT for its support";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        about=(TextView)findViewById(R.id.tvabout);
        about.setText(aboutapp);
    }


}
