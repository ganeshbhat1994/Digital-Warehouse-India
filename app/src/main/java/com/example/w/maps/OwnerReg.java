package com.example.w.maps;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OwnerReg extends Activity {
    //private ProgressDialog pDialog;
    EditText etusername;
    EditText etpassword;
    // EditText etusertype;
    EditText etArea;
    EditText etDistrict;
    EditText etState;
    EditText etPIN;
    ActionBar actionBar;
   // String iscold;
    EditText etphone;
    Button submit;

    public String wusername,wpassword,wArea,wDistrict,wState,wPIN,wPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_reg);
        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);
        etusername = (EditText) findViewById(R.id.username);
        etpassword = (EditText) findViewById(R.id.password);
        etArea = (EditText) findViewById(R.id.area);
        etDistrict = (EditText) findViewById(R.id.district);
        etState = (EditText) findViewById(R.id.state);
        etPIN = (EditText) findViewById(R.id.PIN);
        etphone = (EditText) findViewById(R.id.phonenumber);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wusername = etusername.getText().toString();
                wpassword = etpassword.getText().toString();
                wArea= etArea.getText().toString();
                wDistrict = etDistrict.getText().toString();
                wState = etState.getText().toString();
                wPIN= etPIN.getText().toString();
                wPhone = etphone.getText().toString();

                if(wusername.length()==0){
                    Toast.makeText(OwnerReg.this,"Please enter the username",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wpassword.length()==0){
                    Toast.makeText(OwnerReg.this,"Please enter the password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wArea.length()==0){
                    Toast.makeText(OwnerReg.this,"Please enter the Area",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wDistrict.length()==0){
                    Toast.makeText(OwnerReg.this,"Please enter the District",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wState.length()==0){
                    Toast.makeText(OwnerReg.this,"Please enter the State",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wPIN.length()==0){
                    Toast.makeText(OwnerReg.this,"Please enter the PIN",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(wPhone.length()==0){
                    Toast.makeText(OwnerReg.this,"Please enter the Phone number",Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("pArea",wArea);
                bundle.putString("pusername",wusername);
                bundle.putString("ppassword",wpassword);
                bundle.putString("pDistrict",wDistrict);
                bundle.putString("pState",wState);
                bundle.putString("pPIN",wPIN);
                bundle.putString("pPhone",wPhone);

                Intent i=new Intent(getApplicationContext(),DetailsWarehouse.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }


}
