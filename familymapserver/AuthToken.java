package Model;

import java.util.Objects;

/**
 * stores the information for an authtoken object
 */
public class AuthToken {
    /**
     * the authtoken
     */
    String authtoken;
    /**
     * the username
     */
    String username;

    /**
     * initializes authtoken
     * @param authtoken
     * @param username
     */
    public AuthToken (String authtoken, String username){
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authtoken = (AuthToken) o;
        return Objects.equals(authtoken, authtoken.authtoken) && Objects.equals(username, authtoken.username);
    }
}
