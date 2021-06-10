package amin.ride.share;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Created by cursor on 4/3/2020.
 */
public class RideInfo {
    @SerializedName("status")
    String status;
    @SerializedName("msg")
    String msg;
    @SerializedName("error_msg")
    String error_msg;
    @SerializedName("data")
    @Expose
    List<RideInfoDetails> details;
    @SerializedName("user_type")
    String u_type;
    public String getU_type()
    {
        return u_type;
    }
    public void setU_type(String u_type)
    {
        this.u_type = u_type;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getError_msg() {
        return error_msg;
    }
    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
    public List<RideInfoDetails> getDetails() {
        return details;
    }
    public void setDetails(List<RideInfoDetails> details) {
        this.details = details;
    }

}
