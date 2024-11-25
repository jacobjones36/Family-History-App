package RequestResult;

/**
 * letting user know if it filled or not
 */
public class FillResult {
    /**
     * message to user
     */
    String message;
    /**
     * succeeded or not
     */
    boolean success;

    public FillResult(String message, boolean success) {
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
