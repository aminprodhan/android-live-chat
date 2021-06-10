package amin_chats.share;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cursor on 3/9/2020.
 */

public class RideServiceInfo {

    public String getRate() {
        return rate;
    }
    @SerializedName("rider_id")
        String rider_id;

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getSource_address_txt() {
        return source_address_txt;
    }

    public void setSource_address_txt(String source_address_txt) {
        this.source_address_txt = source_address_txt;
    }

    public String getDestination_address_txt() {
        return destination_address_txt;
    }

    public void setDestination_address_txt(String destination_address_txt) {
        this.destination_address_txt = destination_address_txt;
    }
    @SerializedName("sa_txt")
        String source_address_txt;
    @SerializedName("da_txt")
        String destination_address_txt;

    public void setRate(String rate) {
        this.rate = rate;
    }
    @SerializedName("rate")
        String rate;
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    @SerializedName("distance")
        String distance;
    @SerializedName("msg")
        String msg;
    @SerializedName("status")
        String status;
    @SerializedName("list")
        List<RideServiceList> list;
    public String getLatitude_destination() {
        return latitude_destination;
    }
    public void setLatitude_destination(String latitude_destination)
    {
        this.latitude_destination = latitude_destination;
    }
    @SerializedName("latitude_destination")
    String latitude_destination;
    public String getLongitude_destination() {
        return longitude_destination;
    }

    public void setLongitude_destination(String longitude_destination)
    {
        this.longitude_destination = longitude_destination;
    }
    @SerializedName("longitude_destination")
    String longitude_destination;


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String lognititude) {
        this.longitude = lognititude;
    }
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longitude")
    String longitude;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RideServiceList> getList() {
        return list;
    }

    public void setList(List<RideServiceList> list) {
        this.list = list;
    }


}
