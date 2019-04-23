/**
 * Helper for interfacing with APIs.
 */

package helpers.api;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class APIHelper {

    private static String startLocID;
    private static String endLocID;

    public static String getStartLocID() {
        return startLocID;
    }

    public static String getEndLocID() {
        return endLocID;
    }

    public static void setStartLocID(String newID) {
        startLocID = newID;
    }

    public static void setEndLocID(String newID) {
        endLocID = newID;
    }
}