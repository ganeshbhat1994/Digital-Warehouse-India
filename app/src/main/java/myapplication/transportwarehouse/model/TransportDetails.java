package myapplication.transportwarehouse.model;

/**
 * Created by anurag on 10/24/2015.
 */
public class TransportDetails {
    private int Location_id;
    private String address, phone_no, types, name, service;
    float rating,cost;

    public TransportDetails(int location_id, String phone_no, String address, String types, String name, String service,float rating,float cost) {
        Location_id = location_id;
        this.phone_no = phone_no;
        this.address = address;
        this.types = types;
        this.name = name;
        this.service = service;
        this.rating = rating;
        this.cost=cost;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getLocation_id() {
        return Location_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setLocation_id(int location_id) {
        Location_id = location_id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
