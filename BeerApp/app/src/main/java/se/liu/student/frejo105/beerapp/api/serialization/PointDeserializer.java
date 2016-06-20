package se.liu.student.frejo105.beerapp.api.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import se.liu.student.frejo105.beerapp.api.model.Point;

/**
 * Created by vakz on 2016-06-19.
 */
public class PointDeserializer implements JsonDeserializer<Point> {
    @Override
    public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Point p = new Point();
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            p.longitude = jsonObject.get("lon").getAsDouble();
            p.latitude = jsonObject.get("lat").getAsDouble();
            return p;
        }
        catch(Exception _) {
            throw new JsonParseException("Unable to parse point");
        }
    }
}
