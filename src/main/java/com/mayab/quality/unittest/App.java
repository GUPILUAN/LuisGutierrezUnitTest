package com.mayab.quality.unittest;

//import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.unittest.config.OracleConnection;
import com.mayab.quality.unittest.dao.DAOUser;
import com.mayab.quality.unittest.dao.IDAOUser;

class App {
    public static void main(String[] args) {
        OracleConnection oracleConnection = new OracleConnection(args);
        IDAOUser daoUser = new DAOUser(oracleConnection);
        // User userPrueba = new User("username", "password", "email@email.com");
        // daoUser.save(userPrueba);
        System.out.println(daoUser.findAll());

    }
}
