package lk.software.app.foodorderingadminapp.services;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionApi {
    @GET("json")
    Call<JsonObject> getJson(@Query("origin") String origin,
                             @Query("destination") String destination,
                             @Query("alternatives") boolean alternatives,
                             @Query("key") String key);
}
