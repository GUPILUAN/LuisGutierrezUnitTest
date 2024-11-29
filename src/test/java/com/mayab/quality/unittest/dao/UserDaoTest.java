package com.mayab.quality.unittest.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.unittest.model.User;

import io.github.cdimascio.dotenv.Dotenv;

public class UserDaoTest extends DBTestCase {

    private DAOMySQLUser daoMySQLUser;
    Dotenv dotenv = Dotenv.load();
    String databaseUrl = dotenv.get("DATABASE_MYSQL_URL");
    String username = dotenv.get("DATABASE_MYSQL_USER");
    String password = dotenv.get("DATABASE_MYSQL_PASSWORD");
    String driver = dotenv.get("DATABASE_MYSQL_DRIVER");

    public UserDaoTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "oracle.jdbc.driver.OracleDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, databaseUrl);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, username);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, password);

    }

    @BeforeEach
    protected void setUp() throws Exception {
        daoMySQLUser = new DAOMySQLUser();
        IDatabaseConnection connection = null;
        try {
            connection = getConnection();
            connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
            System.out.println("Connection established.");

            IDataSet dataSet = getDataSet();
            System.out.println("DataSet loaded: " + dataSet);
            System.out.println(connection);

            // Truncar y limpiar la tabla
            DatabaseOperation.TRUNCATE_TABLE.execute(connection, dataSet);
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

            // Ejecutar el bloque PL/SQL para manejar la secuencia
            executeSequenceManagement(connection);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Error in setup: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.close(); // Cierra la conexión solo al final
            }
        }
    }

    private void executeSequenceManagement(IDatabaseConnection connection) throws Exception {
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

    @Test
    public void testAgregarUsuario() {

        User usuario = new User("username6", "12345678", "correo6@correo.com");
        // Verificar que el usuario fue insertado correctamente.
        verifyUserInserted(usuario);
    }

    @Test
    public void testAddUserQuery() {
        User usuario = new User("username1", "12345678", "correo1@correo.com");
        // Verificar
        verifyUserInsertedByQuery(usuario);

    }

    private void verifyUserInserted(User usuario) {
        daoMySQLUser.save(usuario);
        try {
            IDatabaseConnection conn = getConnection();
            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("USUARIOS");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/createUser.xml"));
            ITable expectedTable = expectedDataSet.getTable("USUARIOS");

            Assertion.assertEquals(expectedTable, actualTable);
        } catch (Exception e) {
            fail("Error en la verificación de la inserción: " + e.getMessage());
        }
    }

    private void verifyUserInsertedByQuery(User usuario) {
        int newId = daoMySQLUser.save(usuario);
        try {
            IDatabaseConnection conn = getConnection();

            // IDataSet databaseDataSet = conn.createDataSet();
            QueryDataSet actualTable = new QueryDataSet(conn);
            actualTable.addTable("insertTMP", "SELECT * FROM USUARIOS WHERE ID = " + newId);

            String actualUsername = (String) actualTable.getTable("insertTMP").getValue(0, "username");
            String actualEmail = (String) actualTable.getTable("insertTMP").getValue(0, "email");
            String actualPassword = (String) actualTable.getTable("insertTMP").getValue(0, "password");

            assertEquals(actualUsername, usuario.getUsername());
            assertEquals(actualEmail, usuario.getEmail());
            assertEquals(actualPassword, usuario.getPassword());
        } catch (Exception e) {
            fail("Error en la verificación de la inserción: " + e.getMessage());
        }
    }
}
