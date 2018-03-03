package myapplication.transportwarehouse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.w.maps.R;

import java.util.List;

import myapplication.transportwarehouse.model.TransportDetails;

/**
 * Created by anurag on 10/24/2015.
 */
public class TransportAdapter extends BaseAdapter {
    List<TransportDetails> list = null;
    Context context;

    public TransportAdapter(List<TransportDetails> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
        //Log.e("COUNT is ",""+list.size());
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
        {
            LayoutInflater v1=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=v1.inflate(R.layout.transport_list_layout,null);
        }

        TextView name = (TextView)v.findViewById(R.id.Trans_name);
        RatingBar ratingBar = (RatingBar)v.findViewById(R.id.ratingBar);
        TextView cost = (TextView)v.findViewById(R.id.TCost);

        TransportDetails transportDetails = list.get(position);

        cost.setText(String.valueOf(transportDetails.getCost()));
        name.setText(transportDetails.getName());
        ratingBar.setRating(Float.valueOf(String.valueOf(transportDetails.getRating())));
        return v;
    }
}
