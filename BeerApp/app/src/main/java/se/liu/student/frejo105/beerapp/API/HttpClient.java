package se.liu.student.frejo105.beerapp.API;

import android.content.res.Resources;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.Serialization.RegisteredGson;
import se.liu.student.frejo105.beerapp.BeerApp;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.Model.Location;
import se.liu.student.frejo105.beerapp.Model.Pub;

import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.Utility.Utility;
public class HttpClient {
    private static final String address = "http://vakz.se:8888/";
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    public static void getBeer(String id, final RequestCompleteCallback<Beer> cb) {
        if (Utility.isNullEmptyOrWhitespace(id))
        {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.invalid_id)));
            return;
        }
        RequestParams params = new RequestParams("id", id);
        makeCall(getCompleteUrl("Beer"), params, Beer.class, cb);
    }

    public static void search(String searchword, final RequestCompleteCallback<ArrayList<Beer>> cb) {
        if (Utility.isNullEmptyOrWhitespace(searchword))
        {
            cb.onFailure(new HttpResponseException(400, BeerApp.getContext().getResources().getString(R.string.empty_searchword)));
            return;
        }
        RequestParams params = new RequestParams("searchword", searchword);
        makeArrayCall(getCompleteUrl("Search"), params, Beer.class, cb);
    }

    public static void suggestion(Location coordinates, int distance, SuggestionFilters filter, final RequestCompleteCallback<ArrayList<Pub>> cb) {
        RequestParams params = new RequestParams();
        params.put("lng", coordinates.longitude);
        params.put("lat", coordinates.latitude);
        if (!filter.excludeBeers.isEmpty()) {
            params.put("tried", RegisteredGson.getInstance().toJson(filter.excludeBeers));
        }
        if (!filter.excludeTypes.isEmpty()) {
            params.put("notoftype", RegisteredGson.getInstance().toJson(filter.excludeTypes));
        }
        if (!filter.excludeTypes.isEmpty()) {
            params.put("oftype", RegisteredGson.getInstance().toJson(filter.onlyIncludeTypes));
        }
        if (distance > 0) params.put("distance", distance);
        makeArrayCall(getCompleteUrl("Suggestion"), params, Pub.class, cb);
    }


    public static void getMenu(String id, final RequestCompleteCallback<Pub> cb) {
        if (Utility.isNullEmptyOrWhitespace(id))
        {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.invalid_id)));
            return;
        }
        RequestParams params = new RequestParams("id", id);
        makeCall(getCompleteUrl("Menu"), params, Pub.class, cb);
    }

    public static void getPub(String id, boolean incMenu, final RequestCompleteCallback<Pub> cb) {
        if (Utility.isNullEmptyOrWhitespace(id))
        {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.invalid_id)));
            return;
        }
        RequestParams params = new RequestParams("id", id);
        if (incMenu) params.put("incMenu", "true");
        makeCall(getCompleteUrl("Pub"), params, Pub.class, cb);
    }

    public static void getNearbyPubs(Location loc, int distance, final RequestCompleteCallback<ArrayList<Pub>> cb) {
        RequestParams params = new RequestParams();
        params.put("lng", loc.longitude);
        params.put("lat", loc.latitude);
        if(distance > 0) params.put("distance", distance);
        makeArrayCall(getCompleteUrl("Pubs"), params, Pub.class, cb);
    }

    public static void getPubsServing(Location loc, String id, int distance, final RequestCompleteCallback<ArrayList<Pub>> cb) {
        if (Utility.isNullEmptyOrWhitespace(id))
        {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.invalid_id)));
            return;
        }

        RequestParams params = new RequestParams();
        params.put("lng", loc.longitude);
        params.put("lat", loc.latitude);
        params.put("id", id);
        if(distance > 0) params.put("distance", distance);
        makeArrayCall(getCompleteUrl("PubsServing"), params, Pub.class, cb);
    }

    public static void getImage(String id, File file, final RequestCompleteCallback<File> cb) {
        if (Utility.isNullEmptyOrWhitespace(id))
        {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.invalid_id)));
            return;
        }
        String url = String.format("%s/%s.png", address, id);
        httpClient.get(url, null, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                cb.onFailure(new HttpResponseException(statusCode, Resources.getSystem().getString(R.string.image_fail)));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                 cb.onSuccess(file);
            }
        });
    }

    private static <T> void makeArrayCall(String url, RequestParams params, final Type typeOfT, final RequestCompleteCallback<ArrayList<T>> cb) {
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

    private static <T> void makeCall(String url, RequestParams params, final Type typeOfT, final RequestCompleteCallback<T> cb) {
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

    private static String getCompleteUrl(String subtarget) {
        return address + subtarget;
    }
}
