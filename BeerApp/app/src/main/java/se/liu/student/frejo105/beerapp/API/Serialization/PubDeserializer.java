package se.liu.student.frejo105.beerapp.API.Serialization;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import se.liu.student.frejo105.beerapp.API.Model.Beer;
import se.liu.student.frejo105.beerapp.API.Model.Point;
import se.liu.student.frejo105.beerapp.API.Model.Pub;

/**
 * Created by vakz on 2016-06-19.
 */
public class PubDeserializer implements JsonDeserializer<Pub> {

    @Override
    public Pub deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Pub p = new Pub();
        try {
            RegisteredGson gson = RegisteredGson.getInstance();
            JsonObject jsonObject = json.getAsJsonObject();
            p.id = jsonObject.get("id").getAsInt();
            p.name = jsonObject.get("name").getAsString();
            p.description = jsonObject.get("description").getAsString();
            p.location = gson.fromJson(jsonObject.get("location"), Point.class);

            try {
                p.distance = jsonObject.get("distance").getAsDouble();
            }
            catch (Exception _) {
                p.distance = -1;
            }

            JsonArray serves = jsonObject.get("serves").getAsJsonArray();
            p.serves = new ArrayList<>();
            for (int i = 0; i < serves.size(); ++i) {
                Beer beer = gson.fromJson(serves.get(i), Beer.class);
                p.serves.add(beer);
            }

            return p;
        }
        catch(Exception _) {
            throw new JsonParseException("Unable to parse pub");
        }

    }

}
