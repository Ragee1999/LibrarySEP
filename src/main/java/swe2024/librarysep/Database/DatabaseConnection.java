package swe2024.librarysep.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages a connection pool for database access.
 * This class is responsible for creating, managing, and providing database connections from a pool.
 */
public class DatabaseConnection {

    private static final Queue<Connection> pool = new LinkedList<>();
    private static final int MAX_POOL_SIZE = 15;
    private static int activeConnections = 0; // Track active connections
    private static boolean poolClosed = false; // Track if the pool is closed
    private static final Object lock = new Object();

    private static final String url = "jdbc:postgresql://database2024sep.postgres.database.azure.com:5432/postgres";
    private static final String user = "via";
    private static final String password = "group6!%";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    /**
     * Creates a new database connection.
     *
     * @return a new {@link Connection} object
     * @throws SQLException if a database access error occurs
     */
    private static Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Retrieves a connection from the pool.
     *
     * @return a {@link Connection} object from the pool
     * @throws SQLException if all connections are in use or the pool is closed
     */
    public static Connection connect() throws SQLException {
        synchronized (lock) {
            if (poolClosed) {
                throw new SQLException("Connection pool is closed.");
            }
            if (pool.isEmpty() && activeConnections < MAX_POOL_SIZE) {
                pool.add(createNewConnection());
                activeConnections++;
            }
            if (pool.isEmpty()) {
                throw new SQLException("All connections are in use.");
            }
            Connection connection = pool.poll();
            System.out.println("Connection retrieved from pool");
            return connection;
        }
    }

    /**
     * Returns a connection to the pool.
     *
     * @param connection the {@link Connection} object to be returned to the pool
     */
    public static void returnConnection(Connection connection) {
        synchronized (lock) {
            if (connection != null) {
                pool.add(connection);
                System.out.println("Connection returned to pool");
            }
        }
    }

    /**
     * Closes all connections in the pool.
     */
    public static void closeAllConnections() {
        synchronized (lock) {
            while (!pool.isEmpty()) {
                try {
                    Connection connection = pool.poll();
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                        activeConnections--;
                    }
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
            poolClosed = true;
        }
    }
}


