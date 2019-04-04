package database;

import helpers.Constants;
import models.room.Room;

import java.sql.*;
import java.util.ArrayList;

public class RoomTable {
    private static Connection connection;

    private static void RoomTable(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String roomTable = "CREATE TABLE " + Constants.ROOM_TABLE +
                "(nodeID VARCHAR(100)," +
                "capacity INT," +
                "CONSTRAINT nodeIDRoom_fk FOREIGN KEY(nodeID) REFERENCES " + Constants.LOCATION_TABLE + "(nodeID))";
        try {
            statement.execute(roomTable);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static Room getRoomByID(String roomID) {
        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.ROOM_TABLE + " WHERE NODEID=?"
            );

            statement.setString(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Room room = new Room(
                        resultSet.getString("NODEID"),
                        resultSet.getInt("CAPACITY")
                );
                return room;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Cannot get room by ID!");
            e.printStackTrace();

            return null;
        }
    }

    public static boolean addRoom(Room room) {
        try {
            PreparedStatement statement;
            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.ROOM_TABLE + " (NODEID, CAPACITY) " +
                            "VALUES (?, ?)"
            );

            statement.setString(1, room.getRoomID());
            statement.setInt(2, room.getCapacity());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Room " + room.getRoomID() + " cannot be added!");
            e.printStackTrace();

            return false;
        }
    }

    public static boolean dropRoomTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.ROOM_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.ROOM_TABLE + " cannot be dropped");

            return false;
        }
    }

    public static ArrayList<Room> getRooms() {
        try {
            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.ROOM_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Room> returnList = new ArrayList<>();

            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getString("ROOMID"),
                        resultSet.getInt("CAPACITY")
                );
                returnList.add(room);
            }
            return returnList;
        } catch (SQLException e) {
            System.out.println("Failed to get users!");
            return null;
        }
    }


}
