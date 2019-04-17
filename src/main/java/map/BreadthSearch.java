package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.*;

public class BreadthSearch extends SearchAlgorithm {

    @Override
    public Stack<Location> findPath(Location start, Location end) {
        return super.findPath(start, end);
    }

    @Override
    public void searchAlg(SubPath sNext, Location lNext, Location start, Location end) {
        used.put(lNext.getNodeID(), sNext);
            List<SubPath> lstNeighbors = lNext.getSubPaths();
            for (SubPath nCurr : lstNeighbors) {
                Location lCurr = nCurr.getLocation();
                if (!used.containsKey(lCurr.getNodeID())) {
                    nCurr.setParent(sNext);
                    queue.add(nCurr);
                }
            }
        }

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.BFS;
    }
}
