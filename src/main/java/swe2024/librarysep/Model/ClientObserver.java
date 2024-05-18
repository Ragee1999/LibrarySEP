package swe2024.librarysep.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientObserver extends Remote {
    void update() throws RemoteException;
}
