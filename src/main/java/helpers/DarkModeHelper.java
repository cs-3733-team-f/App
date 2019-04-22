package helpers;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ArrayList;

public class DarkModeHelper {
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendants(root, nodes);
        return nodes;
    }

    public static void addAllDescendants(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            node.getStyleClass().remove("css/colorScheme.css");
            node.getStyleClass().add("css/dark-theme.css");
            if (node instanceof Parent)
                addAllDescendants((Parent)node, nodes);
        }
    }
}