package swe2024.librarysep.Utility;

import swe2024.librarysep.Model.ClientObserver;
import swe2024.librarysep.ViewModel.AdminDashboardViewModel;
import swe2024.librarysep.ViewModel.userDashboardViewModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverManager extends UnicastRemoteObject implements ClientObserver {

    private AdminDashboardViewModel adminViewModel;
    private userDashboardViewModel userViewModel;

    public ObserverManager(AdminDashboardViewModel adminViewModel) throws RemoteException {
        this.adminViewModel = adminViewModel;
    }

    public ObserverManager(userDashboardViewModel userViewModel) throws RemoteException {
        this.userViewModel = userViewModel;
    }

    @Override
    public void update() throws RemoteException {
        if (adminViewModel != null) {
            adminViewModel.refreshBooks();
        }
        if (userViewModel != null) {
            userViewModel.refreshBooks();
        }
    }
}