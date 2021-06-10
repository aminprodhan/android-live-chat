package amin.api;

import amin.bidding.RecycleBiddingMsg;
import amin.ride.share.RideInfo;
import amin_chats.cursor.aminchats.User;
import amin_chats.share.RideServiceInfo;
import amin_chats.share.RideServiceList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by cursor on 4/3/2020.
 */

public interface APIRideInfo {
    @Headers("Content-Type: application/json")
    @POST("/support/app/ride_info/getRideContinueInfo")
    Call<RideInfo> getRideContinueInfo(@Body RideInfo info);

    @Headers("Content-Type: application/json")
    @POST("/support/app/ride_info/getCalulatedRent")
    Call<RideServiceInfo> getCalulatedRent(@Body RideServiceInfo info);

    @Headers("Content-Type: application/json")
    @POST("/api/searchDriver")
    Call<RideServiceInfo> searchDriver(@Body RideServiceInfo info);

    @Headers("Content-Type: application/json")
    @POST("/api/ride_accept")
    Call<RecycleBiddingMsg> ride_accept(@Body RecycleBiddingMsg info);


}
