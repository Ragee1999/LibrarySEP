package swe2024.librarysep.Model;

public class BookStateFactory {

    // Converts a string representation of a state into the corresponding BookStates object
    public static BookStates getStateFromString(String stateString) {
        switch (stateString) {
            case "Available":
                return new AvailableState();
            case "Borrowed":
                return new BorrowedState();
            case "Reserved":
                return new ReservedState();
            default:
                throw new IllegalArgumentException("Unknown state: " + stateString);
        }
    }
}
