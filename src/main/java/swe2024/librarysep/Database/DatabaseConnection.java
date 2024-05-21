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

    private static final Queue<Connection> pool = new LinkedList<>(); // Queue to hold database connections
    private static final int MAX_POOL_SIZE = 10;
    private static final Object lock = new Object(); // Object for synchronization

    private static final String url = "jdbc:postgresql://database2024sep.postgres.database.azure.com:5432/postgres";
    private static final String user = "via";
    private static final String password = "group6!%";


    // Change the details accordingly if you wish to use a localhost.
    /*
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "1234";
    */

    // Static initializer to initialize the connection pool
    static {
        initializeConnectionPool();
    }

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }

    /**
     * Initializes the connection pool with the maximum number of connections.
     */
    private static void initializeConnectionPool() {
        try {
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                pool.add(createNewConnection());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing the connection pool", e);
        }
    }

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
     * @throws SQLException if all connections are in use
     */
    public static Connection connect() throws SQLException {
        synchronized (lock) {
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
                        // System.out.println("Connection closed");
                    }
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}

