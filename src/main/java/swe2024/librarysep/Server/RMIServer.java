package swe2024.librarysep.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            LibraryManagerImpl obj = new LibraryManagerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("LibraryManager", obj);
            System.out.println("RMI Server is ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}