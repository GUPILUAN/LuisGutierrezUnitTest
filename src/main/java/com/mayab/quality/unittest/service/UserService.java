package com.mayab.quality.unittest.service;

import java.util.ArrayList;
import java.util.List;

import com.mayab.quality.unittest.dao.IDAOUser;
import com.mayab.quality.unittest.model.User;

public class UserService {

    private IDAOUser dao;

    public UserService(IDAOUser dao) {
        this.dao = dao;
    }

    public User createUser(String email, String username, String password) {

        if (password.length() >= 8 && password.length() <= 16) {
            User user = dao.findUserByEmail(email);

            if (user == null) {
                user = new User(username, password, email);
                int id = dao.save(user);
                user.setId(id);
                return user;
            }

        }
        return null;
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<User>();
        users = dao.findAll();

        return users;
    }

    public User findUserByEmail(String email) {

        return dao.findUserByEmail(email);
    }

    public User findUserById(int id) {

        return dao.findById(id);
    }

    public User updateUser(User user) {
        return dao.findById(user.getId()).getEmail().equals(user.getEmail()) ? dao.updateUser(user) : null;
    }

    public boolean deleteUser(int id) {
        return dao.deleteById(id);
    }

    public boolean logIn(String username, String password) {
        User user = dao.findByUserName(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;

    }
}
