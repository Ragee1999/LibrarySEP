package swe2024.librarysep.Utility;

import swe2024.librarysep.Model.ClientObserver;
import swe2024.librarysep.ViewModel.AdminDashboardViewModel;
import swe2024.librarysep.ViewModel.UserDashboardViewModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ObserverManager acts as a remote observer that gets notified by the server when
 * there are updates that need to be reflected in the client-side view models.
 */
public class ObserverManager extends UnicastRemoteObject implements ClientObserver {

    private AdminDashboardViewModel adminViewModel;
    private UserDashboardViewModel userViewModel;

    /**
     * Constructs an ObserverManager for the admin dashboard.
     *
     * @param adminViewModel the view model for the admin dashboard
     * @throws RemoteException if a remote communication error occurs
     */
    public ObserverManager(AdminDashboardViewModel adminViewModel) throws RemoteException {
        this.adminViewModel = adminViewModel;
    }

    /**
     * Constructs an ObserverManager for the user dashboard.
     *
     * @param userViewModel the view model for the user dashboard
     * @throws RemoteException if a remote communication error occurs
     */
    public ObserverManager(UserDashboardViewModel userViewModel) throws RemoteException {
        this.userViewModel = userViewModel;
    }

    /**
     * Notifies the view models to refresh their data when the server signals an update.
     *
     * @throws RemoteException if a remote communication error occurs
     */
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
