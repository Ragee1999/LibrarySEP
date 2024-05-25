package swe2024.librarysep.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swe2024.librarysep.Database.DatabaseConnection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @BeforeEach
    public void setUp() throws Exception {
        // Reinitialize the connection pool before each test
        resetConnectionPool();
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Access and invoke the private static method to close all connections
        Method closeAllConnectionsMethod = DatabaseConnection.class.getDeclaredMethod("closeAllConnections");
        closeAllConnectionsMethod.setAccessible(true);
        closeAllConnectionsMethod.invoke(null);
    }

    private void resetConnectionPool() throws Exception {
        // Reset the pool state by setting the poolClosed flag to false and clearing the pool
        Field poolClosedField = DatabaseConnection.class.getDeclaredField("poolClosed");
        poolClosedField.setAccessible(true);
        poolClosedField.setBoolean(null, false);

        Field poolField = DatabaseConnection.class.getDeclaredField("pool");
        poolField.setAccessible(true);
        ((Queue<Connection>) poolField.get(null)).clear();

        Field activeConnectionsField = DatabaseConnection.class.getDeclaredField("activeConnections");
        activeConnectionsField.setAccessible(true);
        activeConnectionsField.setInt(null, 0);
    }

    @Test
    public void testConnection() {
        try {
            long startTime = System.currentTimeMillis();

            // Attempt to get a connection from the pool
            Connection connection = DatabaseConnection.connect();
            long connectionTime = System.currentTimeMillis();
            System.out.println("Time to get connection: " + (connectionTime - startTime) + " ms");

            // Verify the connection is not null
            assertNotNull(connection, "Connection should not be null");

            // Verify the connection is valid
            long validationStartTime = System.currentTimeMillis();
            assertTrue(connection.isValid(2), "Connection should be valid");
            long validationTime = System.currentTimeMillis();
            System.out.println("Time to validate connection: " + (validationTime - validationStartTime) + " ms");

            // Return the connection to the pool
            DatabaseConnection.returnConnection(connection);
            long endTime = System.currentTimeMillis();
            System.out.println("Total time: " + (endTime - startTime) + " ms");
        } catch (SQLException e) {
            fail("Connection test failed: " + e.getMessage());
        }
    }

    @Test
    public void testConnectionPoolExhaustion() {
        try {
            // Exhaust the connection pool
            for (int i = 0; i < 15; i++) {
                Connection connection = DatabaseConnection.connect();
                assertNotNull(connection, "Connection should not be null");
            }

            // Attempt to get another connection which should fail
            assertThrows(SQLException.class, () -> {
                DatabaseConnection.connect();
            }, "Expected SQLException when all connections are in use");
        } catch (SQLException e) {
            fail("Connection pool exhaustion test failed: " + e.getMessage());
        }
    }

    @Test
    public void testReturnConnection() {
        try {
            // Get a connection from the pool
            Connection connection = DatabaseConnection.connect();

            // Return the connection to the pool
            DatabaseConnection.returnConnection(connection);

            // Get another connection to verify the connection was returned
            Connection newConnection = DatabaseConnection.connect();
            assertNotNull(newConnection, "Connection should not be null");

            // Return the connection to the pool
            DatabaseConnection.returnConnection(newConnection);
        } catch (SQLException e) {
            fail("Return connection test failed: " + e.getMessage());
        }
    }

    @Test
    public void testCloseAllConnections() {
        try {
            // Close all connections in the pool
            Method closeAllConnectionsMethod = DatabaseConnection.class.getDeclaredMethod("closeAllConnections");
            closeAllConnectionsMethod.setAccessible(true);
            closeAllConnectionsMethod.invoke(null);

            // Verify all connections are closed by trying to get a connection
            assertThrows(SQLException.class, () -> {
                DatabaseConnection.connect();
            }, "Expected SQLException when all connections are closed");
        } catch (Exception e) {
            fail("Close all connections test failed: " + e.getMessage());
        }
    }
}

