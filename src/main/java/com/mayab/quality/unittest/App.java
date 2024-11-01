package com.mayab.quality.unittest;

/**
 * Hello world!
 */
import com.mayab.quality.loginunittest.config.OracleConnection;
import com.mayab.quality.loginunittest.dao.DAOUser;
import com.mayab.quality.loginunittest.dao.IDAOUser;
//import com.mayab.quality.loginunittest.model.User;

class App {
    public static void main(String[] args) {
        OracleConnection oracleConnection = new OracleConnection(args);
        IDAOUser daoUser = new DAOUser(oracleConnection);
        // User userPrueba = new User("username", "password", "email@email.com");
        // daoUser.save(userPrueba);
        System.out.println(daoUser.findAll());

    }
}
