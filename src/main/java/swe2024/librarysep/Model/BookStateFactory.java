package swe2024.librarysep.Model;

public class BookStateFactory {

    // Converts a string representation of a state into the corresponding BookStates object
    public static BookStates getStateFromString(String stateString) {
        return switch (stateString) {
            case "Available" -> new AvailableState();
            case "Borrowed" -> new BorrowedState();
            case "Reserved" -> new ReservedState();
            default -> throw new IllegalArgumentException("Unknown state: " + stateString);
        };
    }
}
