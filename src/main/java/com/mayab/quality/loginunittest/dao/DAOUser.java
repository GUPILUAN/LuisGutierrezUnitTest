package com.mayab.quality.loginunittest.dao;

import java.sql.*;
import java.util.*;

import com.mayab.quality.loginunittest.config.OracleConnection;
import com.mayab.quality.loginunittest.model.User;

public class DAOUser implements IDAOUser {
    private OracleConnection oracleConnection;

    public DAOUser(OracleConnection oc) {
        this.oracleConnection = oc;
    };

    public DAOUser() {
        this.oracleConnection = new OracleConnection(new String[] {});
    }

    @Override
    public User findByUserName(String name) {
        User user = new User();
        Connection con = oracleConnection.connect();
        if (con == null) {
            System.out.println("Error connecting to database");
            return null;
        }
        try {

            PreparedStatement ps = con.prepareStatement("SELECT * FROM USUARIOS WHERE username=?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setLoggedIn(rs.getInt(5) == 1);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return user;
    }

    @Override
    public int save(User user) {
        int status = 0;
        Connection con = oracleConnection.connect();
        if (con == null) {
            System.out.println("Error connecting to database");
            return status;
        }
        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO USUARIOS(id, username, password, email, isloggedin) VALUES (USUARIOS_SEQ.nextval, ?, ?, ?, ?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.isLoggedIn());

            status = ps.executeUpdate();

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return status;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = new User();
        Connection con = oracleConnection.connect();
        if (con == null) {
            System.out.println("Error connecting to database");
            return null;
        }

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USUARIOS WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setLoggedIn(rs.getInt(5) == 1);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<User>();
        Connection con = oracleConnection.connect();
        if (con == null) {
            System.out.println("Error connecting to database");
            return list;
        }
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM USUARIOS");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setLoggedIn(rs.getInt(5) == 1);
                list.add(user);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public User findById(int id) {

        User user = new User();
        Connection con = oracleConnection.connect();
        if (con == null) {
            System.out.println("Error connecting to database");
            return null;
        }

        try {

            PreparedStatement ps = con.prepareStatement("SELECT * FROM USUARIOS WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setLoggedIn(rs.getInt(5) == 1);
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return user;
    }

    @Override
    public boolean deleteById(int id) {
        int status = 0;
        Connection con = oracleConnection.connect();
        if (con == null) {
            System.out.println("Error connecting to database");
            return false;
        }
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM USUARIOS WHERE id=?");
            ps.setInt(1, id);
            status = ps.executeUpdate();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status != 0;
    }

    @Override
    public User updateUser(User userOld) {

        int status = 0;
        Connection con = oracleConnection.connect();
        if (con == null) {
            System.out.println("Error connecting to database");
            return null;
        }
        try {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE USUARIOS set username=?,password=?,email=?,isloggedin=? WHERE id=?");
            ps.setString(1, userOld.getUsername());
            ps.setString(2, userOld.getPassword());
            ps.setString(3, userOld.getEmail());
            ps.setInt(4, userOld.isLoggedIn());
            ps.setInt(5, userOld.getId());

            status = ps.executeUpdate();

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return status != 0 ? userOld : null;
    }

}
