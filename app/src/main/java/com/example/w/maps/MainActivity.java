package com.example.w.maps;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.w.maps.warehouse.FetchDataTask;
import com.example.w.maps.warehouse.PageActivity;
import com.example.w.maps.warehouse.adapters.ListviewAdapter;
import com.example.w.maps.warehouse.model.FetchDataListener;
import com.example.w.maps.warehouse.model.MyList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchDataListener ,SearchView.OnQueryTextListener {

    private ProgressDialog dialog;
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerTogle;
    Button btnSearch;
    MyList list;
    List<String> slist;
    List<String> sslist;
    ListView listView;
    ListviewAdapter adapter;
    ActionBar actionBar;
    Spinner spinner1;
    EditText txtProduct;
    CheckBox chkCold;
    CheckBox chkMost;
    CheckBox chkRating,chkAcc;
    String districtvalue,productValue;
    int ttt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout1);
        setSupportActionBar(toolbar);

        productValue="Select the product";

        drawerTogle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerSlide(View drawerView,float slideOffset)
            {

                super.onDrawerSlide(drawerView, slideOffset);
                //mtoolbar.setAlpha(1-slideoffset/2);
            }

        };
        drawerLayout.setDrawerListener(drawerTogle);
        drawerLayout.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                drawerTogle.syncState();
            }
        });

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        districtvalue=(mSharedPreference.getString("district","nulll"));
        chkCold = (CheckBox) findViewById(R.id.chkCold);
        chkMost = (CheckBox) findViewById(R.id.chkMost);
        chkRating = (CheckBox) findViewById(R.id.chkRating);
        chkAcc = (CheckBox) findViewById(R.id.chkAccredated);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        txtProduct=(EditText)findViewById(R.id.txtProduct);

        String sss[]=getResources().getStringArray(R.array.product_list);
        slist=new LinkedList<>(Arrays.asList(sss));
        sslist=new LinkedList<>(Arrays.asList(sss));

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,slist);
        spinner1.setAdapter(adapter);
        spinner1.setFocusable(true);


        txtProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString() != null) {
                    slist.clear();
                    for (String ss : sslist) {
                        if (((ss.toLowerCase())).trim().startsWith(((s.toString()).toLowerCase()).trim())) {
                            slist.add(ss);
                        }

                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productValue=spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        list=new MyList();

        initView( 1,0,0,0,0,"default","0");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_search_view, menu);

        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView= (android.support.v7.widget.SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("Search by Area/District");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;



    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            spinner1.performClick();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement


            switch (item.getItemId()) {
                case R.id.about:
                    Intent intentabout=new Intent(getApplicationContext(),About.class);
                    startActivity(intentabout);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);


            }


    }

    private void initView(int keyCost,int keyMost,int keyRate,int keyCold,int keyAcc,String productValue,String keySearch) {
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading please wait...");
        String url = "http://warehouse.netii.net/data.php";
        FetchDataTask task = new FetchDataTask(this,keyCost,keyMost, keyRate,keyCold,keyAcc,productValue,keySearch);
        task.execute(url);

    }

    @Override
    public void onFetchComplete(MyList data) {
        this.list=data;
        String msg;

        if(dialog != null)  dialog.dismiss();
        adapter=new ListviewAdapter(list,getApplicationContext());
        listView=(ListView)findViewById(R.id.mainList);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);

        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), PageActivity.class);
                int temp = (list.get(position)).getuId();
                i.putExtra("key1", temp);
                i.putExtra("list", (Parcelable) list);
                startActivity(i);
            }
        });

    }

    @Override
    public void onFetchFailure(String msg) {
        // dismiss the progress dialog
        if(dialog != null)  dialog.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void btnClick(View v)
    {
        int keyCost=0,keyDist=0,keyMost=0,keyRate=0,keyCold=0,keyAcc=0;

        if(chkMost.isChecked()==true)
        {
            keyMost=1;
        }
        else
            keyMost=0;
        if(chkRating.isChecked()==true)
        {
            keyRate=1;
        }
        else
            keyRate=0;
        if(chkCold.isChecked()==true)
        {
            keyCold=1;
        }
        else
            keyCold=0;
        if(chkAcc.isChecked()==true)
        {
            keyAcc=1;
        }
        else
            keyAcc=0;
        productValue=(spinner1.getSelectedItem()).toString();

        if(productValue.equals("Select the product"))
        {
            productValue="default";
        }
        initView(0,keyMost,keyRate,keyCold,keyAcc,productValue,"0");



    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String str=query;
        initView(0,0,0,0,0,"default",str);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}

