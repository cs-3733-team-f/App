package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.*;

public abstract class SearchAlgorithm extends PathFinder {
    protected Stack<Location> path = new Stack<>();
    protected Queue<SubPath> queue = new LinkedList<>();
    protected HashMap<String, SubPath> used = new HashMap<>();

    public Stack<Location> findPath(Location start, Location end) {

        SubPath sNeigh = new SubPath("", start, 0.0);
        queue.add(sNeigh);

        while (!queue.isEmpty()) {
            // Poll next neighbor off the queue and get its map
            SubPath sNext = queue.poll();
            Location lNext = sNext.getLocation();
            if (used.containsKey(lNext.getNodeID())) {
                continue;
            }

            // Check to see if map is our end map
            if (lNext.getNodeID().equals(end.getNodeID())) {
                // Generate path from parent map and end node
                path = genPath(sNext);
                break;
            }
            searchAlg(sNext, lNext, start, end);
        }
        return path;
    }

    public abstract void searchAlg(SubPath sn, Location ln, Location start, Location end);
}