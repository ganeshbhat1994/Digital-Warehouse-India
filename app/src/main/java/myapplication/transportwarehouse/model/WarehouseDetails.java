package myapplication.transportwarehouse.model;

/**
 * Created by ganesh on 29/9/15.
 */
public class WarehouseDetails {

    private int rating;
    private String name;
    private String address;

    private int namId;
    private int noRaters;


    public WarehouseDetails(String name,String address, int imageNo,int rating, int noRaters,int namId)
    {
        this.rating = rating;
        this.name = name;
        this.address = address;
        this.noRaters = noRaters;
        this.namId=namId;

    }
    public int getNamId() {
        return namId;
    }

    public void setNamId(int namId) {
        this.namId = namId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRating() {
        return rating;
    }
    public void setRate(int rating) {
        this.rating = rating;
    }
    public int getNoRaters() {
        return noRaters;
    }
    public void setNoRaters(int noRaters) {
        this.noRaters = noRaters;
    }


}
