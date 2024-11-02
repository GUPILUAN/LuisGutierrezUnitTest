package com.mayab.quality.unittest.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean isLoggedIn;

    public User(String username, String password, String email) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

    public User() {
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

    public int isLoggedIn() {
        return this.isLoggedIn ? 1 : 0;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
