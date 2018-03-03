package com.example.w.maps.warehouse.model;

/**
 * Created by ganesh on 21/11/15.
 */

public interface FetchDataListener {
    public void onFetchComplete(MyList list);
    public void onFetchFailure(String msg);
}
