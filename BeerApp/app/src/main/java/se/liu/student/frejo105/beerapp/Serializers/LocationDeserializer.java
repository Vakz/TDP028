package se.liu.student.frejo105.beerapp.Serializers;

/**
 * Created by vakz on 12/27/15.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.liu.student.frejo105.beerapp.Model.Location;

/**
 * Deserializer built from example at http://www.javacreed.com/gson-deserialiser-example/
 */
public class LocationDeserializer implements JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Location loc = new Location();
        JsonObject jsonObject = json.getAsJsonObject();
        loc.type = jsonObject.get("type").getAsString();
        JsonArray coordinates = jsonObject.get("coordinates").getAsJsonArray();
        double[] coords = { coordinates.get(0).getAsDouble(), coordinates.get(1).getAsDouble() };
        loc.coordinates = coords;
        return loc;
    }
}