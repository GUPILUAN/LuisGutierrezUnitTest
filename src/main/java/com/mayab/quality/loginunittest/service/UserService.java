package com.mayab.quality.loginunittest.service;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

public class UserService {
    private IDAOUser daoUser;

    public UserService(IDAOUser daoUser) {
        this.daoUser = daoUser;
    }

    public User createUser(String email, String username, String password) {
        User userFound = this.daoUser.findUserByEmail(email);
        return userFound == null && password.length() >= 8 && password.length() <= 16
                ? this.daoUser.registerUser(new User(email, username, password))
                : null;
    }

    public boolean logIn(String username, String password) {
        User userFound = daoUser.findUserByUsername(username);
        if (userFound != null && userFound.getPassword().equals(password)) {
            userFound.setLoggedIn(true);
            return true;
        }
        return false;
    }
}
