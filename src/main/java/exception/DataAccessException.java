package exception;

/**
 * Replaces the SQL Exception. Allows DAO classes the option of using JDBC or not.
 */
public class DataAccessException extends Exception {
    public DataAccessException(String s) {
        super(s);
    }
    public DataAccessException() { super(); }
}
