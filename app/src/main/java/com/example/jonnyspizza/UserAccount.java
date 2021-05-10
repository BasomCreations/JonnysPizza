package com.example.jonnyspizza;

import java.io.Serializable;

public class UserAccount implements Serializable {

    private String userName;
    private String password;

    /**
     * Constructor
     * @param userName
     * @param password
     */
    public UserAccount(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Constructor
     */
    public UserAccount() {
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
     * @param userName String
     * @param password String
     */
    public void signIn(String userName, String password){
        this.setUserName(userName);
        this.setPassword(password);
    }

    /**
     * Signs the user out of the account by clearing the user credentials
     */
    public void signOut(){
        this.userName = null;
        this.password = null;
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
