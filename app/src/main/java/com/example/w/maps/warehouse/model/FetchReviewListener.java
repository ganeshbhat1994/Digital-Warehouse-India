package com.example.w.maps.warehouse.model;

/**
 * Created by ganesh on 25/12/15.
 */
public interface FetchReviewListener {
    public void onFetchComplete(ReviewData[] data);
    public void onFetchFailure(String msg);
}
