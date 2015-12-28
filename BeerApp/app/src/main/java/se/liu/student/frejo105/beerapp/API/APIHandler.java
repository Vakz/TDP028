package se.liu.student.frejo105.beerapp.API;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import se.liu.student.frejo105.beerapp.API.Serialization.RegisteredGson;
import se.liu.student.frejo105.beerapp.Model.Beer;

/**
 * Created by vakz on 12/27/15.
 */
public class APIHandler {

    public void getBeer(String id, final RequestCompleteCallback cb) {
        HttpClient.getBeer(id, new RequestCompleteCallback<Beer>() {
            @Override
            public void onSuccess(Beer result) {
                //Beer beer = RegisteredGson.getInstance().fromJson(result, Beer.class);
                cb.onSuccess(result);
            }

            @Override
            public void onFailure(String message) {
                cb.onFailure(message);
            }
        });
    }
}
