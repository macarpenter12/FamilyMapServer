package server;

/**
 * If a user is able to authenticate themselves with a correct username and password combination,
 * they will be given an Authentication Token, which will allow them to continue database operations
 * after authentication only once.
 */
public class AuthToken {
    private String token;
    private String username;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
