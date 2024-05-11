package swe2024.librarysep.Server;

import swe2024.librarysep.Model.BookService;


// This class creates an instance of book service that use rmi for communication
public class RMIBookServiceFactory {
    private static final RMIClient rmiClient = new RMIClient();

    public static BookService getBookService() {
        return new RMIBookService(rmiClient);
    }
}
