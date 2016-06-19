package se.liu.student.frejo105.beerapp.API.Serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;

import se.liu.student.frejo105.beerapp.API.Model.Beer;
import se.liu.student.frejo105.beerapp.API.Model.Point;
import se.liu.student.frejo105.beerapp.API.Model.Pub;

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
        gs.registerTypeAdapter(Point.class, new PointDeserializer());
        gs.registerTypeAdapter(Beer.class, new BeerDeserializer());
        gs.registerTypeAdapter(Pub.class, new PubDeserializer());
        gson = gs.create();
    }

    public <T> T fromJson(JsonElement jsonElement, Type typeOfT) {
        return gson.fromJson(jsonElement, typeOfT);
    }

    public <T> T fromJson(String string, Type typeOfT) {
        return gson.fromJson(string, typeOfT);
    }

    public <T> ArrayList<T> fromJsonArray(String string, Type typeOfT) {
        JsonParser jp = new JsonParser();
        JsonElement e = jp.parse(string);
        JsonArray array = e.getAsJsonArray();
        ArrayList<T> items = new ArrayList<>();
        for (int i = 0; i < array.size(); ++i) {
            items.add((T) gson.fromJson(array.get(i), typeOfT));
        }
        return items;
    }

    public String toJson(Object o) {
        return gson.toJson(o);
    }
}
