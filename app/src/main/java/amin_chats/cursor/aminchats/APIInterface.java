package amin_chats.cursor.aminchats;

import android.content.pm.ServiceInfo;
import org.json.JSONObject;
import java.util.List;
import amin_chats.share.LocationSearch;
import amin_chats.share.RideServiceInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("/support/android/user_login.php")
        Call<User> userLogin(@Body User user);
    @GET("/support/api_controller/getRideServiceList")
        Call<RideServiceInfo> getRideServiceList();
    @GET("/support/api_controller/getLocationListByLike")
        Call<LocationSearch> doGetLocationList(@Query("key") String key);



   /* @POST("/gateway/CreateOrUpdateBooking/1.0/createOrUpdateBooking/")
        Call<User> apiCall(@Body CreateBooking user);
    */
    /*@GET("api/users?")
    Call<MultipleResource> doGetListResources();
    @POST("/api/users")
    Call<User> createUser(@Body User user);
    @GET("/api/users?")
    Call<UserList> doGetUserList(@Query("page") String page);
    @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/
}
