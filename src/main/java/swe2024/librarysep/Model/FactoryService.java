package swe2024.librarysep.Model;

import swe2024.librarysep.Server.RMIBookService;
import swe2024.librarysep.Server.RMIClient;

public class FactoryService {
    private static final RMIClient rmiClient = new RMIClient();

    public static BookService getBookService() {
        return new RMIBookService(rmiClient);
    }
}
