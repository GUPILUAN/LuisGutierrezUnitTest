package com.mayab.quality.unittest.dao;

import java.util.List;

import com.mayab.quality.unittest.model.User;

public interface IDAOUser {
    User findByUserName(String name);

    int save(User user);

    User findUserByEmail(String email);

    List<User> findAll();

    User findById(int id);

    boolean deleteById(int id);

    User updateUser(User userOld);

}