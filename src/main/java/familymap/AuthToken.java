package familymap;

import java.util.UUID;

/**
 * If a user is able to authenticate themselves with a correct username and password combination,
 * they will be given an Authentication Token, which will allow them to continue database operations
 * after authentication only once.
 */
public class AuthToken {
    private String authToken;
    private String userName;

    public AuthToken(String username) {
        this.userName = username;
        this.authToken = UUID.randomUUID().toString();
    }

    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.userName = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return this.authToken.equals(authToken.authToken) &&
                userName.equals(authToken.userName);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }
}
