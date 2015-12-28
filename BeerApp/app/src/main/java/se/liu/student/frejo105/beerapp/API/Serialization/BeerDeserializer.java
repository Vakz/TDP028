package se.liu.student.frejo105.beerapp.API.Serialization;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.Model.BeerType;
import se.liu.student.frejo105.beerapp.Model.Brewery;

/**
 * Deserializer built from example at http://www.javacreed.com/gson-deserialiser-example/
 */
public class BeerDeserializer implements JsonDeserializer<Beer> {

    @Override
    public Beer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        Beer beer = new Beer();
        beer._id = jsonObject.get("_id").getAsString();
        beer.desc = jsonObject.get("desc").getAsString();
        beer.name = jsonObject.get("name").getAsString();
        Gson gson = new Gson();
        beer.beerType = gson.fromJson(jsonObject.get("beerType"), BeerType.class);
        beer.brewery = gson.fromJson(jsonObject.get("brewery"), Brewery.class);
        return beer;
    }
}
