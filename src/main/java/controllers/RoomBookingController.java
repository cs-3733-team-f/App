package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import database.BookLocationTable;
import database.Database;
import database.LocationTable;
import database.RoomTable;
import helpers.Constants;
import helpers.DatabaseHelpers;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import models.room.Book;
import models.room.Room;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoomBookingController {

    /**
     * FXML
     */
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;

    // Buttons for booking and cancelling bookings
    public JFXButton btnBookSelected;
    public JFXButton btnCancel;

    // Table that displays available rooms
    public TableView<Room> tblRooms;
    public TableView<Room> tblRoomsBooked;

    // Columns
    public TableColumn<Room, String> tblRoomID;
    public TableColumn<Room, String> tblRoomCapacity;

    public TableColumn<Room, String> tblRoomIDBooked;
    public TableColumn<Room, String> tblRoomCapacityBooked;
    public JFXButton btnReturn1;

    ObservableList<Room> rooms = FXCollections.observableArrayList();
    ObservableList<Room> roomsBooked = FXCollections.observableArrayList();

    LocalDate startDate;
    LocalDate endDate;

    LocalTime startTime;
    LocalTime endTime;

    boolean timePeriodSet = false;

    private int totalBookings = 0;

    public void initialize() {

        btnBookSelected.setVisible(false);

        // Add listeners for date and time pickers
        datStartDay.valueProperty().addListener((observable, oldValue, newValue) -> {
            startDate = newValue;
            checkDateAndTime();
        });

        datEndDay.valueProperty().addListener((observable, oldValue, newValue) -> {
            endDate = newValue;
            checkDateAndTime();
        });

        datStartTime.valueProperty().addListener((observable, oldValue, newValue) -> {
           startTime = newValue;
            checkDateAndTime();
        });

        datEndTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            endTime = newValue;
            checkDateAndTime();
        });


        initBooking();
        initBooked();

    }

    private void initBooking() {

        // Initialize table
        // Column names SHOULD be EXACTLY EQUAL to the ObservableList attributes
        tblRoomID.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
        tblRoomCapacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        tblRooms.setItems(rooms);

        tblRoomIDBooked.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
        tblRoomCapacityBooked.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        tblRoomsBooked.setItems(roomsBooked);

    }

    List<Room> roomDetails = new ArrayList<>();
    private void initBooked() {
        User currentUser = UserHelpers.getCurrentUser();

        List<Book> roomsBooked = BookLocationTable.getBookingsForUser(currentUser);

        if(roomsBooked != null) {

            System.out.println(roomsBooked.toString());

            for (Book booking : roomsBooked) {
                Room currRoom = RoomTable.getRoomByID(booking.getRoomID());

                roomDetails.add(currRoom);
            }

            populateRoomBookedTable(roomDetails);
        }

    }

    public void logOut(MouseEvent event) {
        event.consume();
        ScreenController.logOut(btnReturn1);
        try {
            ScreenController.activate(Constants.Routes.LOGIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if start date/time and end date/time are set
     */
    List<Room> roomsAvailable;
    public void checkDateAndTime() {
        if (startDate != null
                && endDate != null
                && startTime != null
                && endTime != null) {

            timePeriodSet = true;

            roomsAvailable = LocationTable.checkAvailabilityByTime(
                    DatabaseHelpers.getDateTime(startDate, startTime),
                    DatabaseHelpers.getDateTime(endDate, endTime)
            );

            populateRoomBookingTable(roomsAvailable);

        }
    }

    /**
     * Populates the rooms available table
     * @param roomsA
     */
    private void populateRoomBookingTable(List<Room> roomsA) {

        rooms.clear();
        rooms.addAll(roomsA);

        tblRooms.refresh();

    }

    /**
     * Populates the booked rooms table
     * @param roomsB
     */
    private void populateRoomBookedTable(List<Room> roomsB) {

        roomsBooked.clear();
        roomsBooked.addAll(roomsB);

        tblRoomsBooked.refresh();

    }

    /**
     * Called on when user clicks on the book button
     */
    private void reserveRoom() {

        if (startDate == null ||
            startTime == null ||
            endDate == null ||
            endTime == null) {
                return;
        }

        // Get selected room
        Room selected = tblRooms.getSelectionModel().getSelectedItem();

        // Get currently authenticated user
        User currentUser = UserHelpers.getCurrentUser();

        // Add booking to the database
        Book book = new Book(
                totalBookings,
                selected.getRoomID(),
                currentUser,
                DatabaseHelpers.getDateTime(startDate, startTime),
                DatabaseHelpers.getDateTime(endDate, endTime)
        );

        boolean booked = BookLocationTable.createBooking(book);
            // Add to rooms booked table
            Room roomD = RoomTable.getRoomByID(book.getRoomID());
//            roomsAvailable.remove(roomD);

            for(int i=0; i<roomsAvailable.size(); i++) {
                if(roomsAvailable.get(i).getRoomID().equals(roomD.getRoomID())) {
                    roomsAvailable.remove(i);
                }
            }

            roomDetails.add(roomD);

            populateRoomBookedTable(roomDetails);
            populateRoomBookingTable(roomsAvailable);

//        } else {
//
//            // Show error
//            System.out.println("failed!!");
//
//        }

    }

    /**
     * Method to handle selected room
     * @param event
     */
    public void handleBookingSelection(MouseEvent event) {

        if(event.getButton().equals(MouseButton.PRIMARY)) {

            // Check if we have available rooms
            if(roomsAvailable != null) {
                if(roomsAvailable.size() > 0 && tblRooms.getSelectionModel().getSelectedItem() != null) {
                    btnBookSelected.setVisible(true);
                } else {
                    btnBookSelected.setVisible(false);
                }
            }

        }

    }

    /**
     * Method to handle the book room button on click event
     * @param event
     */
    public void handleBooking(MouseEvent event) {

        event.consume();
        reserveRoom();

    }

    /**
     * Called when user clicks on the cancel booking button
     */
    public void cancelReservation() {

        // Get selected room
        Room selected = tblRoomsBooked.getSelectionModel().getSelectedItem();

        // Get currently authenticated user
        User currentUser = UserHelpers.getCurrentUser();

    }

    /**
     * Method to handle selected booking
     * @param event
     */
    public void handleBookingCancellationSelection(MouseEvent event) {

        if(event.getButton().equals(MouseButton.PRIMARY)) {

            // Check if we have bookings
            if(roomDetails != null) {
                if(roomDetails.size() > 0 && tblRoomsBooked.getSelectionModel().getSelectedItem() != null) {
                    btnCancel.setVisible(true);
                } else {
                    btnCancel.setVisible(false);
                }
            }

        }

    }

    /**
     * Method to handle the cancel booking button on click event
     * @param event
     */
    public void handleBookingCancellation(MouseEvent event) {

        event.consume();
        cancelReservation();

    }

}
