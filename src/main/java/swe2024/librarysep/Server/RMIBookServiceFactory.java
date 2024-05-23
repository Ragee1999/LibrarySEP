package swe2024.librarysep.Server;

import swe2024.librarysep.Model.BookService;

/**
 * Factory class for creating instances of {@link BookService} that use RMI for communication.
 * This class provides a static method to get an instance of {@link RMIBookService}, which forwards
 * method calls to the {@link RMIClient}, enabling remote interactions with the server.
 */
public class RMIBookServiceFactory {
    private static final RMIClient rmiClient = new RMIClient();

    /**
     * Gets an instance of {@link BookService} that uses RMI for communication.
     *
     * @return an instance of {@link RMIBookService}
     */
    public static BookService getBookService() {
        return new RMIBookService(rmiClient);
    }
}
