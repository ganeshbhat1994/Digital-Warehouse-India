package myapplication.transportwarehouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.w.maps.R;

/**
 * Created by anurag on 1/9/2016.
 */
public class Transport_Reg_Activity extends Activity implements View.OnClickListener {
    EditText tCompany,pass,licence,pno,area,district,state,pin;
    Button add_det;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_reg);

        tCompany = (EditText) findViewById(R.id.cname);
        pass = (EditText) findViewById(R.id.password);
        licence = (EditText) findViewById(R.id.licencenumber);
        pno = (EditText) findViewById(R.id.phonenumber);
        area = (EditText) findViewById(R.id.area);
        district = (EditText) findViewById(R.id.district);
        state = (EditText) findViewById(R.id.state);
        add_det = (Button) findViewById(R.id.submit);
        pin = (EditText) findViewById(R.id.pin);

        add_det.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("tCompany",tCompany.getText().toString());
        bundle.putString("pass",pass.getText().toString());
        bundle.putString("licence",licence.getText().toString());
        bundle.putString("pno",pno.getText().toString());
        bundle.putString("area",area.getText().toString());
        bundle.putString("district",district.getText().toString());
        bundle.putString("state",state.getText().toString());
        bundle.putString("pin",pin.getText().toString());
        Intent intent = new Intent(getApplicationContext(),Transp_add_details.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
