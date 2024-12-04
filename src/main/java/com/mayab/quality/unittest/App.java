package com.mayab.quality.unittest;

import com.mayab.quality.unittest.config.MySQLConnection;

//import com.mayab.quality.unittest.config.OracleConnection;
import com.mayab.quality.unittest.dao.DAOMySQLUser;
//import com.mayab.quality.unittest.dao.DAOOracleUser;
import com.mayab.quality.unittest.dao.IDAOUser;

class App {
    public static void main(String[] args) {
        MySQLConnection mySQLConnection = new MySQLConnection();
        IDAOUser daoUser = new DAOMySQLUser(mySQLConnection);
        // User userPrueba = new User("username", "password", "email@email.com");
        // daoUser.save(userPrueba);
        System.out.println(daoUser.findAll());

    }
}
