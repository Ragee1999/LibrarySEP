package swe2024.librarysep.Server;

import swe2024.librarysep.Model.BookService;


/**
 * This class creates an instance of BookService that uses RMI for communication.
 * It provides a static method to get an instance of RMIBookService, which forwards
 * method calls to the RMIClient, enabling remote interactions with the server.
 */

public class RMIBookServiceFactory {
    private static final RMIClient rmiClient = new RMIClient();

    public static BookService getBookService() {
        return new RMIBookService(rmiClient);
    }
}
