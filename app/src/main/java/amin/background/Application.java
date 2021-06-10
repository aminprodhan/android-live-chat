package amin.background;

/**
 * Created by cursor on 3/30/2020.
 */
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Application extends android.app.Application {

    public static final boolean DEBUG = true;
    public static Application application;

    private static Context context;
    private Socket socket;

    public static String    packageName;
    public static Resources resources;
    public static Socket    CHAT_SOCKET;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //@formatter:off
        resources   = this.getResources();
        context     = getApplicationContext();
        packageName = getPackageName();
        //@formatter:on
        //startService(new Intent(this, BiddingBackgroundService.class));

        socketConnect();
        /*try {
            CHAT_SOCKET = IO.socket(ClientSettings.getChatAddress(), opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("SOCKET.IO ", e.getMessage());
        }*/

    }
    public void socketConnect(){
        IO.Options opts = new IO.Options();
      //  opts.forceNew = true;
        //opts.reconnection = true;

        try {
            CHAT_SOCKET = IO.socket("http://192.168.0.21:5000",opts);
            CHAT_SOCKET.connect();
         //   CHAT_SOCKET.emit("join", "amin");
            /// System.out.println("soket id = "+CHAT_SOCKET.id());
        } catch (URISyntaxException e)
        {
            Log.e("SOCKET.IO ", e.getMessage());
            e.printStackTrace();
        }

    }
    public static Context getContext() {
        return context;
    }

    public Socket getSocket() {
        return CHAT_SOCKET;
    }
}
