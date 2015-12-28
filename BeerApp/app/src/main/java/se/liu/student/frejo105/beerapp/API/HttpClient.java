package se.liu.student.frejo105.beerapp.API;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import se.liu.student.frejo105.beerapp.API.Serialization.RegisteredGson;
import se.liu.student.frejo105.beerapp.Model.Beer;

/**
 * Created by vakz on 12/27/15.
 */
public class HttpClient {
    private static final String address = "http://vakz.se:8888/";
    private static AsyncHttpClient httpClient = new AsyncHttpClient();

    public static void getBeer(String id, final RequestCompleteCallback cb) {
        httpClient.get(getCompleteUrl("Beer"), new RequestParams("id", id), new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Gson gson = new Gson();
                cb.onSuccess(RegisteredGson.getInstance().fromJson(response, Beer.class));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                cb.onFailure(responseString);
            }
        });
    }

    private static String getCompleteUrl(String subtarget) {
        return address + subtarget;
    }
}
