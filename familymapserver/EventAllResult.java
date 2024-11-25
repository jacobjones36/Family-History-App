package RequestResult;

import Model.Event;

/**
 * returning all events
 */

public class EventAllResult {
    /**
     * all the event objects
     */
    Event[] data;

    /**
     * succeeded or not
     */
    boolean success;
    public EventAllResult(Event[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
