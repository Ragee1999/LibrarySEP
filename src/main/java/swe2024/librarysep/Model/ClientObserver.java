package swe2024.librarysep.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents a client observer in an RMI (Remote Method Invocation) system.
 * This interface should be implemented by any class that wants to be notified of updates.
 */
public interface ClientObserver extends Remote {
    /**
     * Called to notify the observer of an update.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    void update() throws RemoteException;
}
