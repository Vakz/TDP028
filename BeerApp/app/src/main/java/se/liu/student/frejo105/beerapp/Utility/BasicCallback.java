package se.liu.student.frejo105.beerapp.Utility;

/**
 * Created by vakz on 2016-01-07.
 */
public interface BasicCallback {
    void onSuccess();

    void onFailure(Exception error);
}
