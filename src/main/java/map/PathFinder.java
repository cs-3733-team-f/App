package map;

import database.CSVParser;
import database.Database;
import helpers.FileHelpers;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class PathFinder {

    public static void main(String[] args) {
        Database db = new Database();
        CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV());

        // Create map
        Map map = MapParser.parse();
        // Start and end locations
        Location start = map.getLocation("ADEPT00301");
        Location end = map.getLocation("DDEPT00402");

        // Timer for findPath
        long startTime = System.currentTimeMillis();
        // Finds the path from start to end
        Stack<SubPath> path = findPath(map, start, end);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        // Prints out path and time
        printPath(path);
        System.out.println("Time taken: " + totalTime);
    }

    /**
     * Finds a path from the start map to the end map using a*
     * @param map The map of the hospital
     * @param start The start map
     * @param end The end map
     * @return A stack of locations that contains the path
     */
    public static Stack<SubPath> findPath(Map map, Location start, Location end) {
        // Create a new stack to hold the path from start to end
        Stack<SubPath> path = new Stack<>();

        // Create priority queue and hashmaps
        PriorityQueue<SubPath> inQueue = new PriorityQueue<>();
        HashMap<String, SubPath> parent = new HashMap<>();
        HashMap<String, SubPath> used = new HashMap<>();

        // Initialize values
        SubPath sNeigh = new SubPath("", start, 0.0);
        inQueue.add(sNeigh);
        parent.put(start.getNodeID(), null);

        // Loop while queue isn't empty or end map is found
        while (!inQueue.isEmpty()) {
            // Poll next neighbor off the queue and get its map
            SubPath nNext = inQueue.poll();
            Location lNext = nNext.getLocation();

            // Check to see if map is our end map
            if (lNext.getNodeID().equals(end.getNodeID())) {
                // Generate path from parent map and end node
                path = genPath(parent, nNext);
                break;
            }

            // Get the node's value and add it to the used map
            double currDist = nNext.getDist();
            used.put(lNext.getNodeID(), nNext);

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
                    //TODO: Create a more accurate heuristic for nodes on different floors
                    double heuristic = calcDist(lCurr.getxCord(), lCurr.getyCord(), end.getxCord(), end.getyCord());
                    if (!lCurr.getFloor().equals(end.getFloor())) {
                        heuristic += 999999999;
                    }
                    // Create a new neighbor with updated distance value
                    SubPath newNeigh = new SubPath(nCurr.getEdgeID(), nCurr.getLocation(), newDist + heuristic);
                    // Add the new neighbor into the queue and add its parent into the parent map
                    inQueue.add(newNeigh);
                    parent.put(lCurr.getNodeID(), nNext);
                }
            }
        }
        return path;
    }

    /**
     * Generates a path from the given parent map and end map
     * @param parent Hashmap of each node's parent
     * @param end The end map
     * @return A stack of locations containing the path
     */
    private static Stack<SubPath> genPath(HashMap<String, SubPath> parent, SubPath end) {
        // Create an empty stack of neighbors
        Stack<SubPath> path = new Stack<>();
        // Start at the last node (end)
        SubPath prev = end;
        // Loop thru until the start is reached (parent == null)
        while (prev != null) {
            path.push(prev);
            // prev := prev -> parent
            prev = parent.get(prev.getLocation().getNodeID());
        }
        return path;
    }

    /**
     * Prints out the path (represented in a stack)
     * @param path A stack of locations containing the path
     */
    private static void printPath(Stack<SubPath> path) {
        // Pop thru the stack and print out each element
        while (!path.isEmpty()) {
            SubPath curr = path.pop();
            System.out.println("Location: " + curr.getLocation().getNodeID());
            System.out.println("Distance: " + curr.getDist());
        }
    }

    /**
     * Equation to calculate the distance between two points
     * @param x1 X-Location of point 1
     * @param y1 Y-Location of point 1
     * @param x2 X-Location of point 2
     * @param y2 Y-Location of point 2
     * @return The distance as a double
     */
    public static double calcDist(int x1, int y1, int x2, int y2) {
        return Math.pow((Math.pow(x1 - x2, 2.0) + (Math.pow(y1 - y2, 2.0))), 0.5);
    }
}
