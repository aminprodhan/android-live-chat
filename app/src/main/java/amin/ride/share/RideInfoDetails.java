package amin.ride.share;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cursor on 4/3/2020.
 */

public class RideInfoDetails
{
    @SerializedName("id")
    String id;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("driver_id")
    String driver_id;
    @SerializedName("pickup_id")
    String pickup_id;
    @SerializedName("destination_id")
    String destination_id;
    @SerializedName("rent_per_km")
    String rent_per_km;
    @SerializedName("km")
    String km;
    @SerializedName("default_rent")
    String default_rent;
    @SerializedName("status")
    String status;
    @SerializedName("date_time")
    String date_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPickup_id() {
        return pickup_id;
    }

    public void setPickup_id(String pickup_id) {
        this.pickup_id = pickup_id;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public String getRent_per_km() {
        return rent_per_km;
    }

    public void setRent_per_km(String rent_per_km) {
        this.rent_per_km = rent_per_km;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getDefault_rent() {
        return default_rent;
    }

    public void setDefault_rent(String default_rent) {
        this.default_rent = default_rent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }


}
