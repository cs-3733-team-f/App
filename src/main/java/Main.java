import controllers.ScreenController;
import database.Database;
import helpers.Constants;
import helpers.FileHelpers;
import javafx.application.Application;
import javafx.stage.Stage;
import database.CSVParser;
import map.PathFinder;

import java.io.File;

public class Main extends Application {
    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        if(!Database.databaseExists()) {
            CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV(), FileHelpers.getWorkspacesCSV());
        }

        screenController = new ScreenController(primaryStage);

        PathFinder.setDefLocation("HLABS00103");

    }

}
