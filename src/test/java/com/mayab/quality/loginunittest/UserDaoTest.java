package com.mayab.quality.loginunittest;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.DAOUser;
import com.mayab.quality.loginunittest.model.User;

public class UserDaoTest extends DBTestCase {

    private DAOUser daoUserOracle;
    private static final String DB_URL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=FREEPDB1)))";
    private static final String USERNAME = "SYSTEM";
    private static final String PASSWORD = "1234567890";

    public UserDaoTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "oracle.jdbc.driver.OracleDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, DB_URL);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, USERNAME);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, PASSWORD);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, USERNAME);

    }

    @BeforeEach
    protected void setUp() throws Exception {
        daoUserOracle = new DAOUser();
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

    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

    @Test
    public void testAgregarUsuario() {
        User usuario = new User("username2", "123456", "correo2@correo.com");
        // Verificar que el usuario fue insertado correctamente.
        verifyUserInserted(usuario);
    }

    private void verifyUserInserted(User usuario) {
        daoUserOracle.save(usuario);
        try {
            IDatabaseConnection conn = getConnection();

            IDataSet databaseDataSet = conn.createDataSet();
            ITable actualTable = databaseDataSet.getTable("USUARIOS");

            // Read XML with the expected result
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/create.xml"));
            ITable expectedTable = expectedDataSet.getTable("USUARIOS");

            Assertion.assertEquals(expectedTable, actualTable);
        } catch (Exception e) {
            fail("Error en la verificación de la inserción: " + e.getMessage());
        }
    }

}
