package swe2024.librarysep.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;


 // Manages a connection pool for database access.

public class DatabaseConnection {

    private static final Queue<Connection> pool = new LinkedList<>();  // Queue to hold database connections
    private static final int MAX_POOL_SIZE = 10;
    private static final Object lock = new Object();  // Object for synchronization

  // For the local host
  /*
  private static final String url = "jdbc:postgresql://localhost:5432/postgres";
  private static final String user = "postgres";
  private static final String password = "1234";
  */

    private static final String url = "jdbc:postgresql://database2024sep.postgres.database.azure.com:5432/postgres";
    private static final String user = "via";
    private static final String password = "group6!%";

    static {
        initializeConnectionPool();
    }

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }

    // Initialize the connection pool with the maximum number of connections
    private static void initializeConnectionPool() {
        try {
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                pool.add(createNewConnection());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing the connection pool", e);
        }
    }

    // Create a new database connection
    private static Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Retrieve a connection from the pool
    public static Connection connect() throws SQLException {
        synchronized (lock) {
            if (pool.isEmpty()) {
                // in case all pools are unavailable we throw and exception
                throw new SQLException("All connections are in use.");
            }
            Connection connection = pool.poll();
            System.out.println("Connection retrieved from pool");
            return connection;
        }
    }

    // Return a connection to the pool
    public static void returnConnection(Connection connection) {
        synchronized (lock) {
            if (connection != null) {
                pool.add(connection);
                System.out.println("Connection returned to pool");
            }
        }
    }

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

