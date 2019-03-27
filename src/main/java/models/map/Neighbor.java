package models.map;

public class Neighbor implements Comparable<Neighbor> {

    private String edgeID;
    private Location node;
    private double dist;

    public Neighbor(String edgeID, Location node, double dist) {
        this.edgeID = edgeID;
        this.node = node;
        this.dist = dist;
    }

    public String getEdgeID() {
        return edgeID;
    }

    public Location getLocation() {
        return node;
    }

    public double getDist() {
        return dist;
    }

    @Override
    public int compareTo(Neighbor n) {
        if (dist - n.getDist() > 0) { // Positive: this > n
            return 1;
        } else if (dist - n.getDist() == 0.0) { // Zero: this == n
            return 0;
        } else { // Negative: this < n
            return -1;
        }
    }
}