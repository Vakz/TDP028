package se.liu.student.frejo105.beerapp.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.api.model.Beer;
import se.liu.student.frejo105.beerapp.api.model.Pub;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import se.liu.student.frejo105.beerapp.api.model.Point;
import se.liu.student.frejo105.beerapp.api.serialization.RegisteredGson;
import se.liu.student.frejo105.beerapp.BeerApp;
import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.utility.Utility;

/**
 * Created by vakz on 2016-06-20.
 */
public class HttpClient {
    private static final String address = "http://vps.vakz.se:8888/";
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    public static void getBeer(int id, final HttpCallback<Beer> cb) {
        if (id < 0) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.invalid_id)));
            return;
        }
        RequestParams params = new RequestParams("id", id);
        makeCall(getCompleteUrl("beer"), params, Beer.class, cb);
    }

    public static void search(String query, final HttpCallback<ArrayList<Beer>> cb) {
        if (Utility.isNullEmptyOrWhitespace(query)) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.empty_searchword)));
            return;
        }
        RequestParams params = new RequestParams("query", query);
        makeArrayCall(getCompleteUrl("search"), params, Beer.class, cb);
    }

    public static void suggestion(Point location, ArrayList<Integer> filter, final HttpCallback<Beer> cb) {
        if (filter == null) {
            filter = new ArrayList<>();
        }
        RequestParams params = new RequestParams();
        params.put("lon", location.longitude);
        params.put("lat", location.latitude);
        params.put("filter", RegisteredGson.getInstance().toJson(filter));
        makeCall(getCompleteUrl("suggestion"), params, Beer.class, cb);
    }

    public static void menu(int id, final HttpCallback<ArrayList<Beer>> cb) {
        if (id < 0) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.invalid_id)));
            return;
        }
        RequestParams params = new RequestParams("id", id);
        makeArrayCall(getCompleteUrl("menu"), params, Beer.class, cb);
    }

    public static void pub(int id, final HttpCallback<Pub> cb) {
        if (id < 0) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.invalid_id)));
            return;
        }
        RequestParams params = new RequestParams("id", id);
        makeCall(getCompleteUrl("pub"), params, Pub.class, cb);
    }

    public static void getNearby(Point location, int distance, final HttpCallback<ArrayList<Pub>> cb) {
        if (distance < 0) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.invalid_distance)));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("lon", location.longitude);
        params.put("lat", location.latitude);
        params.put("distance", distance);
        makeArrayCall(getCompleteUrl("getNearby"), params, Pub.class, cb);
    }

    public static void pubsServing(Point location, int id, int distance, final HttpCallback<ArrayList<Pub>> cb) {
        if (id < 0) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.invalid_id)));
            return;
        }
        if (distance < 0) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.invalid_distance)));
        }
        RequestParams params = new RequestParams();
        params.put("lon", location.longitude);
        params.put("lat", location.latitude);
        params.put("distance", distance);
        params.put("id", id);
        makeArrayCall(getCompleteUrl("pubsServing"), params, Pub.class, cb);
    }

    public static void getClosest(Point location, final HttpCallback<Pub> cb) {
        RequestParams params = new RequestParams();
        params.put("lon", location.longitude);
        params.put("lat", location.longitude);
        makeCall(getCompleteUrl("getClosest"), params, Pub.class, cb);
    }

    public static void getImage(int id, File file, final HttpCallback<File> cb) {
        System.out.println("Getting an image");
        if (id < 0) {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.invalid_id)));
            return;
        }
        String url = String.format("%s/images/%s.jpg", address, id);
        httpClient.get(url, null, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                cb.onFailure(new HttpResponseException(statusCode, BeerApp.getContext().getResources().getString(R.string.image_fail)));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                cb.onSuccess(file);
            }
        });
    }



    private static <T> void makeArrayCall(String url, RequestParams params, final Type typeOfT, final HttpCallback<ArrayList<T>> cb) {
        httpClient.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                cb.onFailure(new HttpResponseException(statusCode, responseString));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<T> l = RegisteredGson.getInstance().fromJsonArray(responseString, typeOfT);
                cb.onSuccess(l);
            }
        });
    }

    private static <T> void makeCall(String url, RequestParams params, final Type typeOfT, final HttpCallback<T> cb) {
        httpClient.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                cb.onFailure(new HttpResponseException(statusCode, responseString));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                cb.onSuccess((T)RegisteredGson.getInstance().fromJson (responseString, typeOfT));
            }
        });
    }

    private static String getCompleteUrl(String endpoint) {
        return address + endpoint;
    }
}
