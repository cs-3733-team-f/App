package helpers;

public class Constants {

    /**
     * DATABASE
     */
    public static String DB_NAME = "teamF";

    public static String USERS_TABLE = "USERS";
    public static String EMPLOYEE_TABLE = "EMPLOYEE";
    public static String CUSTODIAN_TABLE = "CUSTODIAN";
    public static String ADMIN_TABLE = "ADMIN";
    public static String NODES_TABLE = "NODES";
    public static String EDGES_TABLE = "EDGES";

    public static String DB_PROJECTION = "projection";
    public static String DB_RELATION = "relation";
    public static String DB_CONDITION = "condition";

    /**
     * FILES
     */
    public static String CSV_NODES = "/data/nodes.csv";
    public static String CSV_EDGES = "/data/edges.csv";

    public static String INTRO_VIDEO = "/images/movie.mp4";


    /**
     * UI
     */
    public static String SETTINGS_BUTTON_TOOLTIP = "Access user settings";
    public static String LOGOUT_BUTTON_TOOLTIP = "Logout";

    public static String LABEL_NODE_ERROR = "You should specify a node type using the dropdown menu";
    public static String BUTTON_UPDATE_NODE_ERROR = "OK";


    public static enum NodeType {
        BATH, CONF, DEPT, ELEV, EXIT, HALL, INFO, LABS, REST, RETL, SERV, STAI
    }


}
