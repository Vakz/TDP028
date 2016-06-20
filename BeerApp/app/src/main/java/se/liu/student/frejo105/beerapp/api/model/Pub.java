package se.liu.student.frejo105.beerapp.api.model;

import java.util.ArrayList;

/**
 * Created by vakz on 2016-06-19.
 */
public class Pub {
    public int id;
    public String name;
    public String description;
    public double distance;
    public ArrayList<Beer> serves;
    public Point location;
}
