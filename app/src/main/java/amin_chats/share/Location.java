package amin_chats.share;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cursor on 3/5/2020.
 */

public class Location {

    @SerializedName("id")
    String id;
    @SerializedName("address")
    String address;
    @SerializedName("latitude")
    String latitude;
    @SerializedName("longnitute")
    String longnitute;
    @SerializedName("countryName")
    String countryName;
    @SerializedName("cityName")
    String cityName;
    @SerializedName("postalCode")
    String postalCode;
    @SerializedName("knownName")
    String knownName;
    @SerializedName("stateName")
    String stateName;
    @SerializedName("datetime")
    String datetime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongnitute() {
        return longnitute;
    }

    public void setLongnitute(String longnitute) {
        this.longnitute = longnitute;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


}
