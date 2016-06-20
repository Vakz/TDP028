package se.liu.student.frejo105.beerapp.api;

import cz.msebera.android.httpclient.client.HttpResponseException;

/**
 * Created by vakz on 2016-06-20.
 */
public interface HttpCallback<T> {
    void onSuccess(T result);
    void onFailure(HttpResponseException hre);
}
