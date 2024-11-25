package RequestResult;

import Model.Person;

import java.util.Arrays;
import java.util.Objects;

/**
 * returns all the people connected to user
 */

public class PersonAllResult {

    Person[] data;

    boolean success;

    public PersonAllResult(Person[] data, boolean success) {
        this.data = data;
        this.success = success;
    }


    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonAllResult that = (PersonAllResult) o;
        return success == that.success && Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(success);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "PersonAllResult{" +
                "data=" + Arrays.toString(data) +
                ", success=" + success +
                '}';
    }
}
