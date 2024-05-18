package swe2024.librarysep.Model;

public class BookStateFactory {

    // This Book State Factory is not creating the states but instead,
    // adding the feature of changing the state into strings to be displayed in the UI
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
