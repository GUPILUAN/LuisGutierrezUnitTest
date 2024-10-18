package com.mayab.quality.loginunittest.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean isLoggedIn;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public void setLoggedIn(boolean logged) {
        isLoggedIn = logged;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
