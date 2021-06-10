package amin_chats.cursor.aminchats;

public class Message {

    private String nickname;
    private String message ;
    //user rate
    private String rate ;

    public String getDriver_rate() {
        return driver_rate;
    }

    public void setDriver_rate(String driver_rate) {
        this.driver_rate = driver_rate;
    }

    private String driver_rate ;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    public  Message(){

    }
    public Message(String rate){
        this.rate=rate;
    }
    public Message(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}