package se.liu.student.frejo105.beerapp.api;

import cz.msebera.android.httpclient.client.HttpResponseException;

public interface HttpCallback<T> {
    void onSuccess(T result);
    void onFailure(HttpResponseException hre);
}
