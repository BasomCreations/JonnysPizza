package com.example.jonnyspizza;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserAccount implements Serializable {

    @JsonProperty(DB_Util.USER_ACCOUNT_PK)
    private int id;

    @JsonProperty(DB_Util.USER_ACCOUNT_USERNAME)
    private String userName;

    @JsonProperty(DB_Util.USER_ACCOUNT_PASSWORD)
    private String password;

    /**
     * Constructor
     * @param userName
     * @param password
     */
    public UserAccount(String userName, String password) {
        this.id = -1;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Constructor
     * @param id
     * @param userName
     * @param password
     */
    public UserAccount(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Constructor
     */
    public UserAccount() {
        this.id = -1;
    }

    /**
     * Determines if the user is currently signed into an account
     * @return boolean - true if the user is signed in, false if not
     */
    public boolean isSignedIn(){
        return (this.userName != null && this.password != null);
    }

    /**
     * Signs the user into the account with the corresponding credentials
     * @param id int
     * @param userName String
     * @param password String
     */
    public void signIn(int id, String userName, String password){
        this.setId(id);
        this.setUserName(userName);
        this.setPassword(password);
    }

    /**
     * Signs the user out of the account by clearing the user credentials
     */
    public void signOut(){
        this.id = -1;
        this.userName = null;
        this.password = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
