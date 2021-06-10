package amin_chats.cursor.aminchats;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anupamchugh on 09/01/17.
 */

public class User {

    @SerializedName("mobile_no")
        public String mobile_no;
    @SerializedName("pass")
        public String pass;
    @SerializedName("status")
        public String status;
    @SerializedName("msg")
        public String msg;
    @SerializedName("user_id")
        public String user_id;
    @SerializedName("img")
        public String img;
    @SerializedName("uname")
        public String uname;
    public User(String mobile_no, String pass)
    {
        this.mobile_no = mobile_no;
        this.pass = pass;
    }
    public User(String mobile_no, String status, String user_id, String img, String uname, String msg)
    {
        this.mobile_no = mobile_no;
       /// this.uid = uid;
        this.status = status;
        this.msg = msg;
        this.user_id = user_id;
        this.img = img;
        this.uname = uname;
    }
}
