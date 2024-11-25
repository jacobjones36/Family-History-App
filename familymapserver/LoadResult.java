package RequestResult;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * letting user know if it loaded it or not
 */
public class LoadResult {
    /**
     * message to user
     */
    String message;
    /**
     * succeeded or not
     */
    boolean success;

    public LoadResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
