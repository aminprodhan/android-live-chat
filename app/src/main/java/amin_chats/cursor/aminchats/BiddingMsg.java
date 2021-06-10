package amin_chats.cursor.aminchats;

/**
 * Created by cursor on 3/1/2020.
 */

public class BiddingMsg {

    String user_rate;
    String driver_rate;
    String uid;
    String driver_id;

    public BiddingMsg(String user_rate, String driver_rate, String uid, String driver_id)
    {
        this.user_rate = user_rate;
        this.driver_rate = driver_rate;
        this.uid = uid;
        this.driver_id = driver_id;
    }
    public String getUser_rate() {
        return user_rate;
    }

    public void setUser_rate(String user_rate) {
        this.user_rate = user_rate;
    }

    public String getDriver_rate() {
        return driver_rate;
    }

    public void setDriver_rate(String driver_rate) {
        this.driver_rate = driver_rate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }


}
