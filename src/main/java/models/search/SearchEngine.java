package models.search;

import java.util.*;

public class SearchEngine {

    private String term;
    private Set<String> results;
    private Stack<String> neighbors;
    private Set<String> uniqueNeighbors;

    private SearchKeywords searchKeywords;

    public SearchEngine() {
        searchKeywords = new SearchKeywords();
    }

    public void search(String newTerm) {
        // Clear old results
        results = new HashSet<>();
        neighbors = new Stack<>();
        uniqueNeighbors = new HashSet<>();

        term = newTerm;

        this.runSearch();
    }

    // TODO: Check if valid english word. If not, show results not found.
    // TODO: Link spanish words to categories
    private void runSearch() {

        if(searchKeywords.validEnglishWord(this.term)) {

            // Search for nodes
            search();

        } else {

            // Convert to valid word
            runDamerau();

        }

    }

    private void search() {

        System.out.println("Term: " + this.term);

        Map<String, List<String>> tmp = new HashMap<>(searchKeywords.getKeys());

        Iterator it = tmp.entrySet().iterator();

        while (it.hasNext()) {

            // Get next item
            Map.Entry pair = (Map.Entry) it.next();

            // Get category
            String category = pair.getKey().toString();

            if(this.term.equals(category)) { // Found category match!

                results.addAll((ArrayList<String>) pair.getValue());

                break;

            }

            List<String> keywords = (List<String>) pair.getValue();

            for(String k : keywords) {

                if(this.term.equals(k)) { // Found match!

                    results.addAll((ArrayList<String>) pair.getValue());

                    break;

                }

            }

            // Remove item
            it.remove();

        }

    }

    private void runDamerau() {

        Damerau damerau = new Damerau();

        Map<String, List<String>> tmp = new HashMap<>(searchKeywords.getKeys());

        Iterator<Map.Entry<String, List<String>>> it = tmp.entrySet().iterator();

        double minDistance = Double.MAX_VALUE;
        double minDistanceKeyword = Double.MAX_VALUE;

        String closestCategory = "";
        String closestKeyword = "";

        while (it.hasNext()) {

            // Get next item
            Map.Entry<String, List<String>> pair = it.next();

            // Get category
            String category = pair.getKey().toString();

            double currDist = damerau.distance(this.term, category);

            if(currDist < minDistance) {
                minDistance = currDist;
                closestCategory = category;
            }

            List<String> keywords = pair.getValue();

            for(String k : keywords) {
                double currDistKeyword = damerau.distance(this.term, k);

                if(currDistKeyword < minDistanceKeyword) {
                    minDistanceKeyword = currDistKeyword;
                    closestKeyword = k;

                    neighbors.push(closestKeyword);
                }
            }

            // Remove item
            it.remove();

        }

        if(minDistance == Double.MAX_VALUE) {
            System.out.println("No match!");
        } else {
            // TODO: Check string length and minimum distance
            System.out.println("Distance is: " + minDistance);
            System.out.println("Distance for keyword is: " + minDistanceKeyword);
//
//            System.out.println("Search term: " + this.term);
            System.out.println("Closest category: " + closestCategory);
            System.out.println("Closest keyword: " + closestKeyword);

//            results.add(closestCategory);
//            results.add(closestKeyword);
        }

    }

    public Set<String> getResults() {

        return results;
    }

    public static void main(String[] args)
    {

        SearchEngine searchEngine = new SearchEngine();
        searchEngine.search("doctor");

        searchEngine.search("food");

        searchEngine.search("drinks");

    }

}
