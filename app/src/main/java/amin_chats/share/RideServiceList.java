package amin_chats.share;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cursor on 3/9/2020.
 */

public class RideServiceList {

    @SerializedName("id")
        String id;
    @SerializedName("name")
        String name;
    @SerializedName("status")
        String status;
    @SerializedName("img")
        String img;
    @SerializedName("rate_per_km")
        String rate_per_km;
    @SerializedName("order_col")
        String order_col;
    @SerializedName("base_rate")
        String base_rate;
    public String getBase_rate() {
        return base_rate;
    }

    public void setBase_rate(String base_rate) {
        this.base_rate = base_rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRate_per_km() {
        return rate_per_km;
    }

    public void setRate_per_km(String rate_per_km) {
        this.rate_per_km = rate_per_km;
    }

    public String getOrder_col() {
        return order_col;
    }

    public void setOrder_col(String order_col) {
        this.order_col = order_col;
    }


}
