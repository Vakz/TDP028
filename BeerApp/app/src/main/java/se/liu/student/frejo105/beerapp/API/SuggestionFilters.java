package se.liu.student.frejo105.beerapp.API;

import java.util.ArrayList;

import se.liu.student.frejo105.beerapp.Model.Beer;

/**
 * Created by vakz on 2015-12-29.
 */

/**
 * Lists to use for filter for Suggestions
 * IDs in the excludeBeers list will be not be used to suggestions.
 * IDs in the excludeTypes list will not be used to suggestions.
 * IDs in the onlyIncludeTypes will be used exclusively for suggestions.
 * If any IDs are present in both type lists, exclusion will take precedent.
 */
public class SuggestionFilters {
    public ArrayList<String> excludeBeers = new ArrayList<>();
    public ArrayList<String> excludeTypes = new ArrayList<>();
    public ArrayList<String> onlyIncludeTypes = new ArrayList<>();
}
