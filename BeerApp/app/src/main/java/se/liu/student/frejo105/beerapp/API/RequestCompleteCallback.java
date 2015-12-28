package se.liu.student.frejo105.beerapp.API;

import org.json.JSONObject;

/**
 * Created by vakz on 12/28/15.
 */
public interface RequestCompleteCallback<T> {
    public void onSuccess(T result);
    public void onFailure(String message);
}
