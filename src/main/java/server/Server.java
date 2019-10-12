package server;

import familymap.User;

/**
 * Main class of the program. Handles login operations, calls to DAO classes, and user interface.
 */
public class Server {
    /**
     * Main function
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

    }

    /**
     * Will call UserDAO to handle insertion of a new User into the database.
     * @param user The User object to be added to the database.
     * @return If the operation was successful.
     */
    public boolean registerUser(User user) { return false; }

    /**
     * Will call UserDAO to authenticate the user and give them an Authentication Token, which will
     * grant them access to further database operations without needing to login again.
     * @param username The username associated with an existing User in the database.
     * @param password The password for the User object associated with the given username in the DB.
     * @return An Authentication Token granting access to the account, or null if incorrect credentials/error
     */
    public AuthToken login(String username, String password) { return null; }

    /**
     * Will randomly generate Person and Event objects and insert them into the database.
     * @param user The User to generate family history information for
     * @param generations The number of generations to generate besides the user (i.e., parents = 1,
     *                    grandparents = 2, etc...)
     */
    public void generateFamilyHistory(User user, int generations) {}
}