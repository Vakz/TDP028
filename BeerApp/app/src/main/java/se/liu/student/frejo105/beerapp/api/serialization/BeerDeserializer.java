package se.liu.student.frejo105.beerapp.api.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.liu.student.frejo105.beerapp.api.model.Beer;

/**
 * Created by vakz on 2016-06-19.
 */
public class BeerDeserializer implements JsonDeserializer<Beer> {
    @Override
    public Beer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Beer b = new Beer();
        try {
            JsonObject obj = json.getAsJsonObject();
            b.name = obj.get("name").getAsString();
            b.id = obj.get("id").getAsInt();
            b.description = obj.get("description").getAsString();
            b.brewery = obj.get("brewery").getAsString();
            b.beerType = obj.get("beer_type").getAsString();

            return b;
        }
        catch(Exception e) {
            throw new JsonParseException("Unable to parse beer");
        }
    }
}
