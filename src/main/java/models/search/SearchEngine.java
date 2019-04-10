package models.search;

import java.util.*;

public class SearchEngine {

    String term;
    Set<String> results = new HashSet<>();
    Stack<String> neighbors = new Stack<>();

    public SearchEngine(String term) {
        this.term = term;
        this.search();
    }

    private void search() {

        SearchKeywords searchKeywords = new SearchKeywords();
        Damerau damerau = new Damerau();

        Iterator it = searchKeywords.getKeys().entrySet().iterator();

        double minDistance = Double.MAX_VALUE;
        double minDistanceKeyword = Double.MAX_VALUE;

        String closestCategory = "";
        String closestKeyword = "";
        while (it.hasNext()) {

            // Get next item
            Map.Entry pair = (Map.Entry) it.next();

            // Get category
            String category = pair.getKey().toString();

            double currDist = damerau.distance(this.term, category);

            if(currDist < minDistance) {
                minDistance = currDist;
                closestCategory = category;
                System.out.println("currCategory: " + closestCategory);
            }


            List<String> keywords = (List<String>) pair.getValue();

            for(String k : keywords) {
                double currDistKeyword = damerau.distance(this.term, k);

                if(currDistKeyword < minDistanceKeyword) {
                    minDistanceKeyword = currDistKeyword;
                    closestKeyword = k;
                    System.out.println("currKeyword: " + closestKeyword);

                    neighbors.push(closestKeyword);
                }
            }

            // Remove item
            it.remove();

        }

        if(minDistance == Double.MAX_VALUE) {
            System.out.println("No match!");
        } else {
            System.out.println("Distance is: " + minDistance);
            System.out.println("Distance for keyword is: " + minDistanceKeyword);

            System.out.println("Search term: " + this.term);
            System.out.println("Closest category: " + closestCategory);
            System.out.println("Closest keyword: " + closestKeyword);

            results.add(closestCategory);
            results.add(closestKeyword);
        }

    }

    public Set<String> getResults() {

        while(!neighbors.isEmpty()) {
            results.add(neighbors.pop());
        }

        return results;
    }

    public static void main(String[] args)
    {

        SearchEngine searchEngine = new SearchEngine("hola");

    }

}
