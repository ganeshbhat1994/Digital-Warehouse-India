package myapplication.transportwarehouse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.w.maps.R;

import java.util.List;

import myapplication.transportwarehouse.model.WarehouseDetails;

/**
 * Created by ganesh on 29/9/15.
 */
public class ListviewAdapter extends BaseAdapter {

    List<WarehouseDetails> list;
    Context _context;
    ImageView imageView;


    public ListviewAdapter(List<WarehouseDetails> list, Context _context)
    {
        this.list = list;
        this._context = _context;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v=convertView;
        if(v==null)
        {
            LayoutInflater v1=(LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=v1.inflate(R.layout.item_transportlayout,null);
        }
        TextView txtName=(TextView) v.findViewById(R.id.txtName);
        TextView txtAddress=(TextView) v.findViewById(R.id.txtAddress);
        TextView txtNoRate=(TextView)v.findViewById(R.id.txtNoRate);
        RatingBar ratingBar=(RatingBar)v.findViewById(R.id.ratingBar);

        WarehouseDetails model=list.get(position);

        txtName.setText(model.getName());
        txtAddress.setText(model.getAddress());
        txtNoRate.setText("Viewers  "+ String.valueOf(model.getNoRaters()));
        ratingBar.setRating(Float.valueOf(String.valueOf(model.getRating())));
        ratingBar.setFocusable(false);
        return v;
    }
}
