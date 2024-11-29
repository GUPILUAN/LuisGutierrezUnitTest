package com.mayab.quality.integration;

//import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
//import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
//import org.dbunit.database.QueryDataSet;
//import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
//import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.unittest.dao.DAOMySQLUser;
//import com.mayab.quality.unittest.dao.DAOOracleUser;
import com.mayab.quality.unittest.model.User;
import com.mayab.quality.unittest.service.UserService;

import io.github.cdimascio.dotenv.Dotenv;

public class UserServiceDbTest extends DBTestCase {
    Dotenv dotenv = Dotenv.load();

    String databaseUrl = dotenv.get("DATABASE_MYSQL_URL");
    String username = dotenv.get("DATABASE_MYSQL_USER");
    String password = dotenv.get("DATABASE_MYSQL_PASSWORD");
    String driver = dotenv.get("DATABASE_MYSQL_DRIVER");
    private UserService userService;
    private DAOMySQLUser daoUser;
    // private DAOOracleUser daoUser;
    // private static final String DB_URL =
    // "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=FREEPDB1)))";
    // private static final String USERNAME = "SYSTEM";
    // private static final String PASSWORD = "1234567890";

    public UserServiceDbTest() {
        // System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
        // "oracle.jdbc.driver.OracleDriver");
        // System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
        // DB_URL);
        // System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
        // USERNAME);
        // System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
        // PASSWORD);
        // System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA,
        // USERNAME);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, driver);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, databaseUrl);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, username);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, password);

    }

    @BeforeEach
    protected void setUp() throws Exception {
        daoUser = new DAOMySQLUser();
        userService = new UserService(daoUser);
        IDatabaseConnection connection = null;
        try {
            connection = getConnection();
            // connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
            // new OracleDataTypeFactory());
            // System.out.println("Connection established.");

            IDataSet dataSet = getDataSet();
            System.out.println("DataSet loaded: " + dataSet);
            // System.out.println(connection);

            // Truncar y limpiar la tabla
            // DatabaseOperation.TRUNCATE_TABLE.execute(connection, dataSet);
            // Iniciar la base de datos

            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

            // Ejecutar el bloque PL/SQL para manejar la secuencia en Oracle
            // executeSequenceManagement(connection);
            restartSequenceManagement(connection);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error in setup: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @SuppressWarnings("unused")
    private void executeSequenceManagement(IDatabaseConnection connection) throws Exception {
        String plsqlBlock = "DECLARE " +
                "   max_id NUMBER; " +
                "BEGIN " +
                "   SELECT NVL(MAX(id), 0) " +
                "     INTO max_id " +
                "     FROM usuarios; " +
                "   EXECUTE IMMEDIATE 'DROP SEQUENCE usuarios_seq'; " +
                "   EXECUTE IMMEDIATE 'CREATE SEQUENCE usuarios_seq START WITH ' || (max_id + 1) || ' INCREMENT BY 1'; "
                +
                "END;";

        try {
            java.sql.Connection jdbcConnection = connection.getConnection();
            jdbcConnection.createStatement().execute(plsqlBlock);
            System.out.println("Sequence management executed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error executing PL/SQL block: " + e.getMessage());
        }
    }

    private void restartSequenceManagement(IDatabaseConnection connection) throws Exception {
        String sqlQuery = "ALTER TABLE USUARIOS AUTO_INCREMENT = 1;";

        try {
            java.sql.Connection jdbcConnection = connection.getConnection();
            jdbcConnection.createStatement().execute(sqlQuery);
            System.out.println("Sequence management executed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error executing SQL query: " + e.getMessage());
        }
    }

    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

    // TESTS

    // Happy path
    @Test
    public void testCreate() {

        userService.createUser("correo6@correo.com", "username6", "12345678");
        try {
            IDatabaseConnection conn = getConnection();
            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("USUARIOS");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/createUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("USUARIOS");

            Assertion.assertEquals(actualTable, expectedTable);
        } catch (Exception e) {
            fail("Error en la verificación de la inserción: " + e.getMessage());
        }

    }

    // User with that email already exists
    @Test
    public void testCreateWhenAlreadyExists() {
        User userCreated = userService.createUser("correo1@correo.com", "newUser", "12345678");
        if (userCreated == null) {
            System.out.println("Ya existe el usuario con ese email");

        }
        assertNull(userCreated);
    }

    // Wrong password length
    @Test
    public void testCreateWhenPasswordLongOrShort() {

        User userCreated = userService.createUser("newuser@email.com", "newUser",
                "12345678901234567890");
        if (userCreated == null) {
            System.out.println("Se excedió de longitud de password");

        }
        assertNull(userCreated);

    }

    // Find user by email
    @Test
    public void testFindUserByEmail() {
        User userFound = userService.findUserByEmail("correo4@correo.com");

        try {

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/findOneEmail.xml"));
            String expectedIdString = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "id");
            int expectedId = Integer.parseInt(expectedIdString); // Convertir el valor de String a int

            String expectedUsername = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "username");
            String expectedEmail = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "email");
            String expectedPassword = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "password");

            if (userFound != null) {
                assertEquals(userFound.getId(), expectedId);
                assertEquals(userFound.getUsername(), expectedUsername);
                assertEquals(userFound.getEmail(), expectedEmail);
                assertEquals(userFound.getPassword(), expectedPassword);
            }

        } catch (Exception e) {
            fail("Error en la verificación de la busqueda: " + e.getMessage());
        }

    }

    /*
     * @Test
     * public void testFindUserByEmailFail() {
     * User userFound = userService.findUserByEmail("correoInexistete@correo.com");
     * assertNull(userFound);
     * }
     */

    // Find user by id
    @Test
    public void testFindUserById() {
        User userFound = userService.findUserById(5);

        try {

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/findOneId.xml"));
            String expectedIdString = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "id");
            int expectedId = Integer.parseInt(expectedIdString); // Convertir el valor de String a int

            String expectedUsername = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "username");
            String expectedEmail = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "email");
            String expectedPassword = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "password");

            if (userFound != null) {
                assertEquals(userFound.getId(), expectedId);
                assertEquals(userFound.getUsername(), expectedUsername);
                assertEquals(userFound.getEmail(), expectedEmail);
                assertEquals(userFound.getPassword(), expectedPassword);
            }

        } catch (Exception e) {
            fail("Error en la verificación de la busqueda: " + e.getMessage());
        }

    }

    // Find all users
    @Test
    public void testFindAll() {
        List<User> table = userService.findAllUsers();

        try {
            IDatabaseConnection conn = getConnection();
            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("USUARIOS");
            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/findAll.xml"));
            ITable expectedTable = expectedDataSet.getTable("USUARIOS");

            Assertion.assertEquals(actualTable, expectedTable);

            System.out.println("TAMAÑO DE TABLA: " + table.size() + "Tamaño de tabla: " +
                    actualTable.getRowCount());
            assertEquals(table.size(), actualTable.getRowCount());

            for (int i = 0; i < table.size(); i++) {
                User user = table.get(i);

                String expectedIdString = (String) expectedDataSet.getTable("USUARIOS").getValue(i, "id");
                int expectedId = Integer.parseInt(expectedIdString); // Convertir el valor de String a int

                String actualUsername = (String) actualTable.getValue(i, "username");
                String actualEmail = (String) actualTable.getValue(i,
                        "email");
                String actualPassword = (String) actualTable.getValue(i, "password");

                assertEquals(expectedId, user.getId());
                assertEquals(actualUsername, user.getUsername());
                assertEquals(actualEmail, user.getEmail());
                assertEquals(actualPassword, user.getPassword());
            }

        } catch (Exception e) {
            fail("Error en la verificación de la inserción: " + e.getMessage());
        }
    }

    // Update user
    @Test
    public void testUpdate() {
        String newUsername = "usernameUpdated";
        User userFound = userService.findUserByEmail("correo1@correo.com");
        userFound.setUsername(newUsername);
        User userUpdated = userService.updateUser(userFound);

        assertNotNull(userUpdated);
        try {

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/updateUser.xml"));
            String expectedIdString = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "id");
            int expectedId = Integer.parseInt(expectedIdString); // Convertir el valor de String a int
            String expectedUsername = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "username");
            String expectedEmail = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "email");
            String expectedPassword = (String) expectedDataSet.getTable("USUARIOS").getValue(0, "password");

            if (userUpdated != null) {
                assertEquals(userUpdated.getId(), expectedId);
                assertEquals(userUpdated.getUsername(), expectedUsername);
                assertEquals(userUpdated.getEmail(), expectedEmail);
                assertEquals(userUpdated.getPassword(), expectedPassword);
            }

        } catch (Exception e) {
            fail("Error en la verificación de la busqueda: " + e.getMessage());
        }

    }

    // Delete user
    @Test
    public void testDelete() {
        boolean wasErased = userService.deleteUser(3);
        assertTrue(wasErased);

        try {
            IDatabaseConnection conn = getConnection();
            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("USUARIOS");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/deleteUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("USUARIOS");

            Assertion.assertEquals(actualTable, expectedTable);
        } catch (Exception e) {
            fail("Error en la verificación de la inserción: " + e.getMessage());
        }

    }

    /*
     * private void verifyUserInsertedByQuery(String email, String username, String
     * password) {
     * User newUser = userService.createUser(email, username, password);
     * try {
     * IDatabaseConnection conn = getConnection();
     * 
     * // IDataSet databaseDataSet = conn.createDataSet();
     * QueryDataSet actualTable = new QueryDataSet(conn);
     * actualTable.addTable("insertTMP", "SELECT * FROM USUARIOS WHERE ID = " +
     * newUser.getId());
     * 
     * String actualUsername = (String)
     * actualTable.getTable("insertTMP").getValue(0, "username");
     * String actualEmail = (String) actualTable.getTable("insertTMP").getValue(0,
     * "email");
     * String actualPassword = (String)
     * actualTable.getTable("insertTMP").getValue(0, "password");
     * 
     * assertEquals(actualUsername, newUser.getUsername());
     * assertEquals(actualEmail, newUser.getEmail());
     * assertEquals(actualPassword, newUser.getPassword());
     * } catch (Exception e) {
     * fail("Error en la verificación de la inserción: " + e.getMessage());
     * }
     * }
     */
}
