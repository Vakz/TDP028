package se.liu.student.frejo105.beerapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.junit.Test;

import se.liu.student.frejo105.beerapp.API.Serialization.RegisteredGson;
import se.liu.student.frejo105.beerapp.Model.Pub;
import se.liu.student.frejo105.beerapp.API.Serialization.LocationDeserializer;
import se.liu.student.frejo105.beerapp.Model.Beer;
import se.liu.student.frejo105.beerapp.Model.BeerType;
import se.liu.student.frejo105.beerapp.Model.Brewery;
import se.liu.student.frejo105.beerapp.API.Serialization.BeerDeserializer;
import se.liu.student.frejo105.beerapp.Model.Location;
import se.liu.student.frejo105.beerapp.API.Serialization.PubDeserializer;

/**
 * Created by vakz on 12/27/15.
 */
public class DeserializeTest {

    @Test
    public void deserializeBeerType() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        JsonElement jsonObject = jp.parse("{\"_id\": \"567926ddcf1fd0dd120efd5b\", \"typeName\": \"Lager\"}").getAsJsonObject();
        BeerType bt = gson.fromJson(jsonObject, BeerType.class);
        assertEquals("567926ddcf1fd0dd120efd5b", bt._id);
        assertEquals("Lager", bt.typeName);
    }

    @Test
    public void deserializeBrewery() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        JsonElement jsonObject = jp.parse("{\"_id\": \"5679267f8e8f2a83128f5e27\", \"name\": \"Brewery\"}");
        Brewery brewery = gson.fromJson(jsonObject, Brewery.class);
        assertEquals("5679267f8e8f2a83128f5e27", brewery._id);
        assertEquals("Brewery", brewery.name);
    }

    @Test
    public void deserializeBeer() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        String brewery = "{\"_id\": \"5679267f8e8f2a83128f5e27\", \"name\": \"Brewery\"}";
        String beertype = "{\"_id\": \"567926ddcf1fd0dd120efd5b\", \"typeName\": \"Lager\"}";
        String beer = "{\"_id\": \"567929b63bf8eb42146e93d1\", \"name\": \"TestBeer\", \"desc\": \"TestDesc\", \"brewery\": %s, \"beerType\": %s}";
        String formatted = String.format(beer, brewery, beertype);
        JsonElement jsonObject = jp.parse(formatted).getAsJsonObject();
        Beer parsed = gson.fromJson(jsonObject, Beer.class);
        assertEquals("567929b63bf8eb42146e93d1", parsed._id);
        assertEquals("Lager", parsed.beerType.typeName);
        assertEquals("Brewery", parsed.brewery.name);
    }

    @Test
    public void deserializeLocation() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        JsonElement jsonObject = jp.parse("{\"type\": \"Point\", \"coordinates\": [15.2, 48.3342]}");
        Location loc = gson.fromJson(jsonObject, Location.class);
        assertEquals("Point", loc.type);
        assertEquals(15.2, loc.longitude, 0.00001);
        assertEquals(48.3342, loc.latitude, 0.00001);
    }

    @Test
    public void deserializeUnpopulatedPub() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        String location = "{\"type\": \"Point\", \"coordinates\": [15.2, 17.234]}";
        String pub = "{\"_id\": \"567bcb3eb2b6563f11353b15\" ,\"name\": \"TestPub\", \"serves\": [\"567929b63bf8eb42146e93d1\"], \"loc\": %s}";
        String formatted = String.format(pub, location);
        JsonElement jsonObject = jp.parse(formatted).getAsJsonObject();
        Pub p = gson.fromJson(jsonObject, Pub.class);
        assertEquals("TestPub", p.name);
        assertEquals("567929b63bf8eb42146e93d1", p.serves.get(0)._id);
        assertEquals(15.2, p.loc.longitude, 0.00001);
        assertEquals(17.234, p.loc.latitude, 0.00001);
    }

    @Test
    public void deserializePopulatedPub() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        String location = "{\"type\": \"Point\", \"coordinates\": [15.2, 17.234]}";
        String brewery = "{\"_id\": \"5679267f8e8f2a83128f5e27\", \"name\": \"Brewery\"}";
        String beertype = "{\"_id\": \"567926ddcf1fd0dd120efd5b\", \"typeName\": \"Lager\"}";
        String serves = "[{\"_id\": \"567929b63bf8eb42146e93d1\", \"name\": \"TestBeer\", \"desc\": \"TestDesc\", \"brewery\": %s, \"beerType\": %s}]";
        String formatted = String.format(serves, brewery, beertype);
        String pub = "{\"_id\": \"567bcb3eb2b6563f11353b15\" ,\"name\": \"TestPub\", \"serves\": %s, \"loc\": %s}";
        formatted = String.format(pub, formatted, location);
        JsonElement jsonObject = jp.parse(formatted).getAsJsonObject();
        Pub p = gson.fromJson(jsonObject, Pub.class);
        assertEquals("TestPub", p.name);
        assertEquals("567929b63bf8eb42146e93d1", p.serves.get(0)._id);
        assertEquals("Lager", p.serves.get(0).beerType.typeName);
        assertEquals("5679267f8e8f2a83128f5e27", p.serves.get(0).brewery._id);
        assertEquals(15.2, p.loc.longitude, 0.00001);
        assertEquals(17.234, p.loc.latitude, 0.00001);
    }
}
