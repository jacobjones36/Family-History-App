package RequestResult;

import java.util.Objects;

/**
 * filling users family tree
 */
public class FillRequest {

    String username;

    int generations;

    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillRequest that = (FillRequest) o;
        return generations == that.generations && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, generations);
    }
}
