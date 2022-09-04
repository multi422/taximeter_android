package ir.mashhadict.taximeter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi     {

    @GET("location/")
    Call<List<Post>> getLocations();

    @POST("location/")
    Call<Post> createLocation(@Body Post post);

    @POST("driver/")
    Call<Driver> createDriver(@Header("Content-Type") String content_type, @Body Driver driver);

    @GET("driver/")
    Call<List<Driver>> getDriver();

    @GET("vrate/")
    Call<List<Rate>> getRate();

    @GET("journey/")
    Call<List<Journey>> getJourney();

    @POST("journey/")
    Call<Journey> createJourney(@Header("Content-Type") String content_type, @Body Journey journey);

}
