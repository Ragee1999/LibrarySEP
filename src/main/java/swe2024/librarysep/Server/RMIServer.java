package swe2024.librarysep.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Sets up the RMI registry.
 * Binds the {@link LibraryManager} instance to the registry with a specific name (e.g., "BookService").
 * Makes the {@link LibraryManager} available for remote clients to look up and use.
 */
public class RMIServer {
    public static void main(String[] args) {
        try {
            LibraryManager obj = new LibraryManager();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("BookService", obj);
            System.out.println("RMI Server is ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
