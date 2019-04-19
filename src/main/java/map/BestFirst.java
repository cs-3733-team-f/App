package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.PriorityQueue;

public class BestFirst extends PathFinder {
    private PriorityQueue<SubPath> queue;

    @Override
    protected void setUp(Location start) {
        queue = new PriorityQueue<>();
        SubPath sStart = new SubPath("", start, 0.0);
        queue.add(sStart);
    }

    @Override
    protected boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    protected SubPath getNext() {
        return queue.poll();
    }

    @Override
    protected void addNext(SubPath next) {
        queue.add(next);
    }

    @Override
    protected double getDist(SubPath loc1, SubPath loc2) {
        return 0;
    }

    @Override
    protected double getHeuristic(Location loc1, Location loc2) {
        double heuristic = calcDist(loc1.getxCord(), loc1.getyCord(), loc2.getxCord(), loc2.getyCord());
        heuristic += FLOOR_HEURISTIC * Math.abs(floorToInt(loc1.getFloor()) - floorToInt(loc2.getFloor()));
        return heuristic;
    }

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.BEST;
    }
}
