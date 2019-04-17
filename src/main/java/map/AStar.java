package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class AStar extends SearchAlgorithm {


    @Override
    public Stack<Location> findPath(Location start, Location end) {
        return super.findPath(start, end);
    }

    @Override
    public void searchAlg(SubPath sNext, Location lNext, Location start, Location end) {
        // Get the node's value and add it to the used map
        double currDist = sNext.getDist();
        used.put(lNext.getNodeID(), sNext);

        // Gets the node's neighbors and loop thru them all
        List<SubPath> lstSubPaths = lNext.getSubPaths();
        for (SubPath nCurr : lstSubPaths) {
            // Get the real map from the neighbor
            Location lCurr = nCurr.getLocation();
            // Check duplicate
            if (!used.containsKey(lCurr.getNodeID())) {
                // Add the node's value to the current value
                double newDist = currDist + nCurr.getDist();
                // Calculate the heuristic based on distance to end map
                //TODO: Incentive to not get off the elevator
                double heuristic = calcDist(lCurr.getxCord(), lCurr.getyCord(), end.getxCord(), end.getyCord());
                heuristic += FLOOR_HEURISTIC * Math.abs(floorToInt(lCurr.getFloor()) - floorToInt(end.getFloor()));
                // Create a new neighbor with updated distance value
                SubPath newNeigh = new SubPath(nCurr.getEdgeID(), nCurr.getLocation(), newDist, heuristic);
                // Add the new neighbor into the queue and add its parent into the parent map
                queue.add(newNeigh);
                newNeigh.setParent(sNext);
            }
        }
    }

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.ASTAR;
    }
}
