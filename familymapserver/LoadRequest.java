package RequestResult;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * loads information in body below to database
 */

public class LoadRequest {
    /**
     * array of users
     */
    User[] users;
    /**
     * array of persons
     */
    Person[] persons;
    /**
     * array of events
     */
    Event[] events;

    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
