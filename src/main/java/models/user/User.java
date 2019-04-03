package models.user;

import database.Database;
import helpers.Constants;

public class User {

    int userID;
    String username;
    String password;
    Constants.Auth userType;

    public User(int userID, String username, String password, Constants.Auth userType) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.userType = userType;
        create();
    }
    private boolean create(){
        return Database.createUser(this);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Constants.Auth getUserType() {
        return userType;
    }

    public void setUserType(Constants.Auth userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            if(((User) obj).getUserID()==(this.getUserID())){
                return true;
            }else {
                return false;
            }
        } else {
            return false;
        }
    }
}
