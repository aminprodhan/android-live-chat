package amin.background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import amin_chats.cursor.aminchats.ChatBoxAdapter;
import amin_chats.cursor.aminchats.Message;

public class BiddingBackgroundService extends Service {

    private Application signalApplication;
   // private Realm  realm;

    public static BiddingBackgroundService instance = null;

    public static boolean isInstanceCreated() {
        return instance == null ? false : true;
    }

    private final IBinder myBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public BiddingBackgroundService getService() {
            return BiddingBackgroundService.this;
        }
    }

    public void IsBendable() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        if (isInstanceCreated()) {
            return;
        }
        super.onCreate();
     //   realm = Realm.getDefaultInstance();

        signalApplication = (Application) getApplication();

        if (signalApplication.getSocket() == null)
            signalApplication.CHAT_SOCKET = signalApplication.getSocket();

        signalApplication.getSocket().on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        signalApplication.getSocket().on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        signalApplication.getSocket().on(Socket.EVENT_CONNECT, onConnect);

        //@formatter:off
        signalApplication.getSocket().on("message", message);
        //@formatter:on

      //  EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isInstanceCreated()) {
            return START_STICKY;
        }
        super.onStartCommand(intent, flags, startId);
        connectConnection();
        return START_STICKY;
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("new message is open,.....");

                    /*EventBus.getDefault().post(
                            new EventChangeChatServerStateEvent(EventChangeChatServerStateEvent.chatServerState.connectedToSocket)
                    );*/
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    /*EventBus.getDefault().post(
                            new EventChangeChatServerStateEvent(EventChangeChatServerStateEvent.chatServerState.disconnectedFromSocket)
                    );*/
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    /*EventBus.getDefault().post(
                            new EventChangeChatServerStateEvent(EventChangeChatServerStateEvent.chatServerState.flashConnectionIcon)
                    );*/
                }
            });
        }
    };
    private Emitter.Listener message = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            final JSONObject result = (JSONObject) args[0];
            new Handler(getMainLooper())
                    .post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    //System.out.println("data event = ");

                                    JSONObject data = (JSONObject) args[0];
                                    try {
                                        //extract data from fired event
                                        String nickname = data.getString("senderNickname");
                                        String message = data.getString("message");
                                        EventBus.getDefault().post(new MessageEvent(message));
                                        System.out.println("data event service = "+message);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                    );
        }
    };

    private void connectConnection() {
        instance = this;
        signalApplication.getSocket().connect();
    }

    private void disconnectConnection() {
        instance = null;
        signalApplication.getSocket().disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

      /*  if (!realm.isClosed())
            realm.close();
*/
        signalApplication.getSocket().off(Socket.EVENT_CONNECT, onConnect);
        signalApplication.getSocket().off(Socket.EVENT_DISCONNECT, onDisconnect);
        signalApplication.getSocket().off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        signalApplication.getSocket().off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        //EventBus.getDefault().unregister(this);

        //@formatter:off
        signalApplication.getSocket().off("message", message);
        //@formatter:on
        disconnectConnection();
    }

}