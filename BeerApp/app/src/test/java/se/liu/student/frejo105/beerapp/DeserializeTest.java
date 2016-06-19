package se.liu.student.frejo105.beerapp;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.junit.Test;

import se.liu.student.frejo105.beerapp.API.Model.Beer;
import se.liu.student.frejo105.beerapp.API.Model.Point;
import se.liu.student.frejo105.beerapp.API.Model.Pub;
import se.liu.student.frejo105.beerapp.API.Serialization.RegisteredGson;

import static org.junit.Assert.assertEquals;

/**
 * Created by vakz on 2016-06-19.
 */
public class DeserializeTest {
    @Test
    public void deserializePoint() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        JsonElement obj = jp.parse("{\"lat\":58.4115954,\"lon\":15.602247000000034}");
        Point p = gson.fromJson(obj, Point.class);
        assertEquals(p.latitude, 58.4115954, 0.0);
        assertEquals(p.longitude, 15.602247000000034, 0.0);
    }

    @Test
    public void deserializeBeer() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();
        JsonElement obj = jp.parse("{\"id\":2,\"name\":\"Carlsberg Export\",\"brewery\":\"Carlsberg\",\"beer_type\":\"Pale Lager\",\"description\":\"descriptionÅÄÖåäö\"}");
        Beer b = gson.fromJson(obj, Beer.class);
        assertEquals(b.id, 2);
        assertEquals(b.name, "Carlsberg Export");
        assertEquals(b.brewery, "Carlsberg");
        assertEquals(b.beerType, "Pale Lager");
        assertEquals(b.description, "descriptionÅÄÖåäö");
    }

    @Test
    public void deserializePubNoDistance() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();

        JsonElement obj = jp.parse("{\"id\":2,\"name\":\"New York Legends\",\"description\":\"Lorem ipsum dolor sit amet\",\"serves\":[{\"id\":1,\"name\":\"Stella Artois\",\"brewery\":\"InBev\",\"beer_type\":\"Pale Lager\",\"description\":\"smak\"},{\"id\":2,\"name\":\"Carlsberg Export\",\"brewery\":\"Carlsberg\",\"beer_type\":\"Pale Lager\",\"description\":\"inslag\"},{\"id\":6,\"name\":\"Staropramen\",\"brewery\":\"Staropramen\",\"beer_type\":\"Pale Lager\",\"description\":\"sötma\"},{\"id\":8,\"name\":\"Staropramen Dark\",\"brewery\":\"Staropramen\",\"beer_type\":\"Dark Lager\",\"description\":\"Maltig.\"},{\"id\":9,\"name\":\"Guinness Extra Stout\",\"brewery\":\"Guinness\",\"beer_type\":\"Porter and Stout\",\"description\":\"Smakrik\"},{\"id\":12,\"name\":\"Budweiser\",\"brewery\":\"Anheuser-Busch\",\"beer_type\":\"Pale Lager\",\"description\":\"Fruktig.\"}],\"distance\":null,\"location\":{\"lat\":58.4117745,\"lon\":15.622129500000028}}");

        Pub p = gson.fromJson(obj, Pub.class);
        assertEquals(p.id, 2);
        assertEquals(p.name, "New York Legends");
        assertEquals(p.description, "Lorem ipsum dolor sit amet");

        Beer b = new Beer();
        for (int i = 0; i < p.serves.size(); ++i) {
            if (p.serves.get(i).id == 9) {
                b = p.serves.get(i);
                break;
            }
        }

        assertEquals(b.id, 9);
        assertEquals(b.brewery, "Guinness");
        assertEquals(b.name, "Guinness Extra Stout");
        assertEquals(p.location.longitude, 15.622129500000028, 0.0);
        assertEquals(p.distance, -1, 0.0);
    }

    @Test
    public void deserializePub() throws JSONException {
        RegisteredGson gson = RegisteredGson.getInstance();
        JsonParser jp = new JsonParser();

        JsonElement obj = jp.parse("{\"id\":2,\"name\":\"New York Legends\",\"description\":\"Lorem ipsum dolor sit amet\",\"serves\":[{\"id\":1,\"name\":\"Stella Artois\",\"brewery\":\"InBev\",\"beer_type\":\"Pale Lager\",\"description\":\"smak\"},{\"id\":2,\"name\":\"Carlsberg Export\",\"brewery\":\"Carlsberg\",\"beer_type\":\"Pale Lager\",\"description\":\"inslag\"},{\"id\":6,\"name\":\"Staropramen\",\"brewery\":\"Staropramen\",\"beer_type\":\"Pale Lager\",\"description\":\"sötma\"},{\"id\":8,\"name\":\"Staropramen Dark\",\"brewery\":\"Staropramen\",\"beer_type\":\"Dark Lager\",\"description\":\"Maltig.\"},{\"id\":9,\"name\":\"Guinness Extra Stout\",\"brewery\":\"Guinness\",\"beer_type\":\"Porter and Stout\",\"description\":\"Smakrik\"},{\"id\":12,\"name\":\"Budweiser\",\"brewery\":\"Anheuser-Busch\",\"beer_type\":\"Pale Lager\",\"description\":\"Fruktig.\"}],\"distance\":25687.23,\"location\":{\"lat\":58.4117745,\"lon\":15.622129500000028}}");

        Pub p = gson.fromJson(obj, Pub.class);
        assertEquals(p.id, 2);
        assertEquals(p.name, "New York Legends");
        assertEquals(p.description, "Lorem ipsum dolor sit amet");

        Beer b = new Beer();
        for (int i = 0; i < p.serves.size(); ++i) {
            if (p.serves.get(i).id == 9) {
                b = p.serves.get(i);
                break;
            }
        }

        assertEquals(b.id, 9);
        assertEquals(b.brewery, "Guinness");
        assertEquals(b.name, "Guinness Extra Stout");
        assertEquals(p.location.longitude, 15.622129500000028, 0.0);
        assertEquals(p.distance, 25687.23, 0.0);
    }
}
