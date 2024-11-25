package RequestResult;

import java.util.Objects;

/**
 * users authtoken and whether it successfully logged them in
 */
public class LoginResult {
    String authtoken;

    String username;

    String personID;

    boolean success;

    public LoginResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    @Override
    public String toString() {
        return "LoginResult{" +
                "authtoken='" + authtoken + '\'' +
                ", username='" + username + '\'' +
                ", personID='" + personID + '\'' +
                ", success=" + success +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResult that = (LoginResult) o;
        return success == that.success && Objects.equals(authtoken, that.authtoken) && Objects.equals(username, that.username) && Objects.equals(personID, that.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authtoken, username, personID, success);
    }
}
