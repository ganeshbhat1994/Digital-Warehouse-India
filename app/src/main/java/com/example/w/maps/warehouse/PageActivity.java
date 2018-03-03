package com.example.w.maps.warehouse;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w.maps.About;
import com.example.w.maps.Login;
import com.example.w.maps.MapsActivity;
import com.example.w.maps.NewReg;
import com.example.w.maps.OwnerLogin;
import com.example.w.maps.R;
import com.example.w.maps.Review;
import com.example.w.maps.StorageFacility;
import com.example.w.maps.warehouse.model.MyList;

import myapplication.transportwarehouse.Transportdetails;

public class PageActivity extends Activity {

    int temp;
    ActionBar actionBar;
    TextView textHeading;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    RatingBar rateBar2;
    TextView textAddress2,textAddress1,textAddress3,phone1,txtReview;
    TextView txtUid,type,capacity,availability,storage_type,rent,ownName,product_type1;
    String phone,OLocation,loginflag,EditTextValue,Trent,mapLocation;

    MyList list=null;
    ViewPager vp1;
    int position,a,a1;
    float b1;
    Intent intent;
    TextView txtWarning;
    private ProgressDialog dialog;
    WarehouseDetails model;
    View v;
    LayoutInflater inflater;
    MypageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2f6699"));
        actionBar.setBackgroundDrawable(colorDrawable);


        Intent intent=getIntent();

        Bundle b = getIntent().getExtras();
        list=b.getParcelable("list");
        if(intent.hasExtra("key1")==true) {
            temp = intent.getExtras().getInt("key1");
            position = checklist();
        }

        model=list.get(position);
        a=model.getuId();
        b1= (float) model.getRating();
        phone=model.getPhone();
        OLocation=(model.getWaddress2()).toString();
        mapLocation=(model.getWaddress2()+","+model.getWaddress1()).toString();
        Trent=String.valueOf(model.getRent());

        vp1 = (ViewPager) findViewById(R.id.viewpager);
        adapter=new MypageAdapter();
        vp1.setAdapter(adapter);

        vp1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                model=null;
                model=list.get(position);
                a=model.getuId();
                b1= (float) model.getRating();
                phone=model.getPhone();
                mapLocation=(model.getWaddress2()+","+model.getWaddress1()).toString();
                OLocation=(model.getWaddress2()).toString();
                Trent=String.valueOf(model.getRent());
               // Toast.makeText(getApplicationContext(),"Location "+Location,Toast.LENGTH_LONG).show();

                // Log.e("uid here is",Integer.toString(a));
                //Log.e("uid with name is",model.getOwnName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp1.setCurrentItem(position);


    }
    public void btnReviewClick(View v)
    {
        v=adapter.mCurrentView;

        Intent intent1=new Intent(getApplicationContext(),Review.class);

        intent1.putExtra("uid",a);
        intent1.putExtra("rating", b1);
        //
        //  Toast.makeText(getApplicationContext(),String.valueOf(a),Toast.LENGTH_SHORT).show();
        startActivity(intent1);
    }

    public void btnClickTransport(View v)

    {
        //  LayoutInflater inflater = getLayoutInflater();
        //   View dialoglayout = inflater.inflate(R.layout.dialog_layout, null);


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(getApplicationContext());
        //alert.setView(dialoglayout);

        alert.setMessage("Enter your current location");
        alert.setTitle(" Customer Location");
        alert.setView(edittext);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                // Editable YouEditTextValue = edittext.getText();
                //OR
                EditTextValue = edittext.getText().toString();


                Intent intent1 = new Intent(getApplicationContext(), Transportdetails.class);

                intent1.putExtra("Clocation", EditTextValue);
                intent1.putExtra("Olocation", OLocation);
                intent1.putExtra("rent", Trent);
                Toast.makeText(getApplicationContext(),Trent,Toast.LENGTH_SHORT).show();
                startActivity(intent1);
                //  Toast.makeText(getApplicationContext(), EditTextValue, Toast.LENGTH_LONG).show();
            }
        });

       /* alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });*/

        alert.show();

    }
    public void btnClickMap(View v)
    {

        Bundle bundle=new Bundle();
        bundle.putString("location",mapLocation);
        //Log.e("Location ",Location);

        Intent intent1=new Intent(getApplicationContext(),MapsActivity.class);
        intent1.putExtras(bundle);
        startActivity(intent1);
    }
    public void btnClickCall(View v)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phone, null));
        startActivity(intent);

    }


    public void btnClickSubmit(View v)
    {
        loginflag="submit";
        Bundle bundle = new Bundle();

        bundle.putString("wid", String.valueOf(a));
        bundle.putString("ophone", phone);
        bundle.putString("rent",Trent);
       // Toast.makeText(getApplicationContext(),phone,Toast.LENGTH_SHORT).show();
        Intent intent1=new Intent(getApplicationContext(),StorageFacility.class);
        intent1.putExtras(bundle);
        startActivity(intent1);
    }

    public class MypageAdapter extends PagerAdapter
    {
        View mCurrentView;
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==(View)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v= inflater.inflate(R.layout.pager_activity, null);

            textHeading=(TextView) v.findViewById(R.id.txtHeading);

            rateBar2=(RatingBar) v.findViewById(R.id.rateBar2);
            phone1=(TextView)v.findViewById(R.id.txtPhone1);
            ownName=(TextView)v.findViewById(R.id.txtOwnName);
            textAddress1=(TextView) v.findViewById(R.id.txtAddress1);
            textAddress2=(TextView) v.findViewById(R.id.txtAddress2);
            textAddress3=(TextView) v.findViewById(R.id.txtAddress3);
            type=(TextView) v.findViewById(R.id.txtType);
            capacity=(TextView) v.findViewById(R.id.txtCapacity1);
            availability=(TextView) v.findViewById(R.id.txtAvailability);
            storage_type=(TextView) v.findViewById(R.id.txtStorage_type1);
            rent=(TextView) v.findViewById(R.id.txtRent);
            txtWarning=(TextView) v.findViewById(R.id.txtWarning);
            txtReview=(TextView) v.findViewById(R.id.txtReview);
            txtUid=(TextView) v.findViewById(R.id.txtUid);

          /*  imageView1=(ImageView) v.findViewById(R.id.imgView1);
            imageView2=(ImageView) v.findViewById(R.id.imgView2);
            imageView3=(ImageView) v.findViewById(R.id.imgView3);
            imageView4=(ImageView) v.findViewById(R.id.imgView4);
            imageView5=(ImageView) v.findViewById(R.id.imgView5);*/

            product_type1=(TextView) v.findViewById(R.id.txtProduct_type1);

            model = list.get(position);


            txtUid.setText(String.valueOf(model.getuId()));
            textHeading.setText(""+model.getWname().toString());
            rateBar2.setRating(model.getRating());

            capacity.setText(String.valueOf(""+model.getWcapacity()) + " tons");
            availability.setText(String.valueOf(""+model.getWavailability())+" tons");
            type.setText(""+model.getType());
            rent.setText(String.valueOf(""+model.getRent())+" Rs");
            storage_type.setText(""+model.getStorage_type());
            phone1.setText(""+model.getPhone());
            ownName.setText(""+model.getOwnName());
            textAddress1.setText(""+model.getWaddress1());
            textAddress2.setText(""+model.getWaddress2());
            textAddress3.setText(""+model.getWaddress3());
            txtReview.setText(""+model.getNoRaters());

            a1=model.getuId();
/*
            if(a1<10)
            {
                imageView1.setImageResource(R.drawable.images1);
                imageView2.setImageResource(R.drawable.images2);
                imageView3.setImageResource(R.drawable.images3);
                imageView4.setImageResource(R.drawable.images4);
                imageView5.setImageResource(R.drawable.images5);
            }
            else if((a1>10)&&(a1<20))
            {
                imageView1.setImageResource(R.drawable.images6);
                imageView2.setImageResource(R.drawable.images7);
                imageView3.setImageResource(R.drawable.images8);
                imageView4.setImageResource(R.drawable.images9);
                imageView5.setImageResource(R.drawable.images10);
            }
            else if((a1>20)&&(a1<30))
            {
                imageView1.setImageResource(R.drawable.images11);
                imageView2.setImageResource(R.drawable.images12);
                imageView3.setImageResource(R.drawable.images13);
                imageView4.setImageResource(R.drawable.images14);
                imageView5.setImageResource(R.drawable.images15);
            }
            else if((a1>30)&&(a1<40))
            {
                imageView1.setImageResource(R.drawable.images16);
                imageView2.setImageResource(R.drawable.images17);
                imageView3.setImageResource(R.drawable.images18);
                imageView4.setImageResource(R.drawable.images19);
                imageView5.setImageResource(R.drawable.images20);
            }
            else {

                imageView1.setImageResource(R.drawable.images21);
                imageView2.setImageResource(R.drawable.images22);
                imageView3.setImageResource(R.drawable.images23);
                imageView4.setImageResource(R.drawable.images24);
                imageView5.setImageResource(R.drawable.images25);


            }*/
            if(model.getType().equals("PVT"))
            {
                txtWarning.setText("WARNING : PRIVATE WAREHOUSE");
            }

            ViewPager viewPager=(ViewPager)container;
            viewPager.addView(v, 0);

            return v;
        }
        @Override
        public void destroyItem(ViewGroup container,int position,Object object){
            ((ViewGroup)container).removeView((View)object);
            object=null;
        }
        @Override
        public void setPrimaryItem(ViewGroup container,int position,Object object)
        {
            mCurrentView=(View)object;
        }
    }


    private int checklist()
    {
        int i=0;
        for (WarehouseDetails w:list)
        {
             if(temp==w.getuId())
             {
                 return i;
             }
            i++;
        }
        return -1;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_page, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement


        switch (item.getItemId()) {
            case R.id.map: {
;               Bundle bundle=new Bundle();
                bundle.putString("location", mapLocation);
                //Log.e("Location ",Location);

                Intent intent1=new Intent(getApplicationContext(),MapsActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                return true;
            }
            case R.id.call: {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                return true;
                //finish();
            }
            case R.id.transport: {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(getApplicationContext());
                //alert.setView(dialoglayout);

                alert.setMessage("Enter your current location");
                alert.setTitle(" Customer Location");
                alert.setView(edittext);

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        // Editable YouEditTextValue = edittext.getText();
                        //OR
                        EditTextValue = edittext.getText().toString();


                        Intent intent1 = new Intent(getApplicationContext(), Transportdetails.class);

                        intent1.putExtra("Clocation", EditTextValue);
                        intent1.putExtra("Olocation", OLocation);
                        intent1.putExtra("rent", Trent);
                        Toast.makeText(getApplicationContext(), Trent, Toast.LENGTH_SHORT).show();
                        startActivity(intent1);
                        //  Toast.makeText(getApplicationContext(), EditTextValue, Toast.LENGTH_LONG).show();
                    }
                });
                alert.show();
                return true;
            }
            case R.id.reviews: {
                v=adapter.mCurrentView;

                Intent intent1=new Intent(getApplicationContext(),Review.class);

                intent1.putExtra("uid", a);
                intent1.putExtra("rating", b1);
                //
                //  Toast.makeText(getApplicationContext(),String.valueOf(a),Toast.LENGTH_SHORT).show();
                startActivity(intent1);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
