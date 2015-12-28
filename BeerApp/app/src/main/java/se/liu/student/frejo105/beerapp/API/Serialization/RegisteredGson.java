package se.liu.student.frejo105.beerapp.API.Serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.lang.reflect.Type;

import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.Model.Location;
import se.liu.student.frejo105.beerapp.Model.Pub;

/**
 * Created by vakz on 12/28/15.
 */
public class RegisteredGson {
    private static Gson gson;
    private static RegisteredGson ourInstance = null;

    public static RegisteredGson getInstance() {
        if (ourInstance == null) ourInstance = new RegisteredGson();
        return ourInstance;
    }

    private RegisteredGson() {
        GsonBuilder gs = new GsonBuilder();
        gs.registerTypeAdapter(Beer.class, new BeerDeserializer());
        gs.registerTypeAdapter(Location.class, new LocationDeserializer());
        gs.registerTypeAdapter(Pub.class, new PubDeserializer());
        gson = gs.create();
    }

    public <T> T fromJson(JsonElement jsonElement, Type typeOfT) {
        return gson.fromJson(jsonElement, typeOfT);
    }

    public <T> T fromJson(String string, Type typeOfT) {
        return gson.fromJson(string, typeOfT);
    }
}
