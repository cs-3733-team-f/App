package models.map;

import models.map.Location;

import java.util.HashMap;

public class Map {
    private HashMap<String, Location> lstLocations;

    public Map(HashMap<String, Location> lstLocations) {
        this.lstLocations = lstLocations;
    }

    public Location getLocation(String nodeID) {
        return lstLocations.get(nodeID);
    }

    public HashMap<String, Location> getAllLocations() {
        return lstLocations;
    }
}