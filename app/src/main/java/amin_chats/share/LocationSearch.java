package amin_chats.share;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cursor on 3/5/2020.
 */

public class LocationSearch {

    String id;
    String latitude;
    @SerializedName("list")
    @Expose
    List<Location> list;
    public String getLongnitute() {
        return longnitute;
    }

    public void setLongnitute(String longnitute) {
        this.longnitute = longnitute;
    }

    public String getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
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

    public String getAddress() {
        return address;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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
    String longnitute;
    String countryName;
    String cityName;
    String postalCode;
    String knownName;
    String stateName;
    String datetime;
    String address;


   public LocationSearch(String id,String address){
       this.id=id;
       this.address=address;
   }

    public LocationSearch(String id, String latitude, String longnitute,
                          String countryName, String cityName, String postalCode,
                          String knownName, String stateName, String datetime, String address) {
        this.id = id;
        this.latitude = latitude;
        this.longnitute = longnitute;
        this.countryName = countryName;
        this.cityName = cityName;
        this.postalCode = postalCode;
        this.knownName = knownName;
        this.stateName = stateName;
        this.datetime = datetime;
        this.address = address;
    }


    @SerializedName("key")
        String search_key;

    public List<Location> getList() {
        return list;
    }

    public void setList(List<Location> list) {
        this.list = list;
    }


    public void setSearch_key(String search_key) {
        this.search_key = search_key;
    }

    public LocationSearch(String search_key){
        this.search_key=search_key;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongnitude(String longnitude) {
        this.longnitute = longnitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
