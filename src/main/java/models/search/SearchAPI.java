package models.search;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXTextField;
import controllers.search.SearchEngineController;
import database.LocationTable;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import models.map.Location;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class SearchAPI {

    private JFXTextField searchTextField;
    private boolean isMapController;
    private JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();

    public SearchAPI(JFXTextField searchTextField) {
        this.searchTextField = searchTextField;

        autoCompletePopup.setWidth(searchTextField.getWidth());
        autoCompletePopup.setHeight(300);
    }

    public SearchAPI(JFXTextField searchTextField, boolean isMapController) {
        this.searchTextField = searchTextField;
        this.isMapController = isMapController;

        autoCompletePopup.setWidth(searchTextField.getWidth());
        autoCompletePopup.setHeight(300);
    }

    public void searchable() {

        // Sets field text to selected suggestion
        autoCompletePopup.setSelectionHandler(event -> {
            searchTextField.setText(event.getObject());

            if(isMapController) {

                // Focus on node
                Set<Location> locations = LocationTable.getLocationByLongName(event.getObject());

                Iterator iterator = locations.iterator();

                try {
                    Location location = (Location) iterator.next();

                    SearchEngineController.focusOnNode(location);
                } catch (NoSuchElementException e) {
                    System.out.println("Name not mapped to location");
                }

            }
        });

        // Add event listener for search box
        searchTextField.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if (searchTextField.getText().length() > 3) {

                    // Get results
                    SearchEngine searchEngine = new SearchEngine(searchTextField.getText());

                    Set<String> results = searchEngine.getResults();

                    if (results.size() > 0) {

                        autoCompletePopup.getSuggestions().clear();

                        int resultsCount = 0;
                        for (String word : results) {
                            autoCompletePopup.getSuggestions().add(word);

                            resultsCount++;

                            if(resultsCount == 10)
                                break;
                        }

                        autoCompletePopup.show(searchTextField);

                    } else {

                        autoCompletePopup.getSuggestions().clear();
                        autoCompletePopup.hide();

                    }

                }

            }

        });

    }

}
