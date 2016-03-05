package se.liu.student.frejo105.beerapp.API.Serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.Model.Location;
import se.liu.student.frejo105.beerapp.Model.Pub;

/**
 * Created by vakz on 12/27/15.
 */
public class PubDeserializer implements JsonDeserializer<Pub> {

    @Override
    public Pub deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Pub pub = new Pub();
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonObject jsonObject = json.getAsJsonObject();
        pub._id = jsonObject.get("_id").getAsString();
        pub.name = jsonObject.get("name").getAsString();
        pub.desc = jsonObject.get("desc").getAsString();
        pub.loc = gson.fromJson(jsonObject.get("loc"), Location.class);
        JsonArray jsonServes = jsonObject.get("serves").getAsJsonArray();
        pub.serves = new ArrayList<>();
        for (int i = 0; i < jsonServes.size(); ++i) {
            JsonElement e = jsonServes.get(i);
            Beer beer = new Beer();
            // Testing if we received a unpopulated pub
            try {
                beer._id = e.getAsString();
            }
            catch(Exception je) {
                // Received a populated pub
                beer = gson.fromJson(e, Beer.class);
            }
            pub.serves.add(beer);
        }
        return pub;
    }
}
