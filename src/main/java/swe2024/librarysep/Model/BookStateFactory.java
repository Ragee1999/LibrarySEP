package swe2024.librarysep.Model;

/**
 * Factory class for converting string representations of book states into corresponding {@link BookStates} objects.
 */
public class BookStateFactory {

    /**
     * Converts a string representation of a state into the corresponding {@link BookStates} object.
     *
     * @param stateString the string representation of the state
     * @return the corresponding {@link BookStates} object
     * @throws IllegalArgumentException if the state string is unknown
     */
    public static BookStates getStateFromString(String stateString) {
        return switch (stateString) {
            case "Available" -> new AvailableState();
            case "Borrowed" -> new BorrowedState();
            case "Reserved" -> new ReservedState();
            default -> throw new IllegalArgumentException("Unknown state: " + stateString);
        };
    }
}
