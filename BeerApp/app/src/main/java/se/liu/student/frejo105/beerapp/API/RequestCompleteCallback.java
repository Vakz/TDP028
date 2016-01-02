package se.liu.student.frejo105.beerapp.API;

import org.json.JSONObject;

import cz.msebera.android.httpclient.client.HttpResponseException;

/**
 * Created by vakz on 12/28/15.
 */
public interface RequestCompleteCallback<T> {
    void onSuccess(T result);
    void onFailure(HttpResponseException hre);
}
