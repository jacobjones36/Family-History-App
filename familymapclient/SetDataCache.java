package com.jacob.family.map.project.anotherone.Helpers;

import android.util.Log;

import com.jacob.family.map.project.anotherone.DataCache;
import com.jacob.family.map.project.anotherone.ServerFacade;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import Model.Event;
import Model.Person;
import RequestResult.EventAllResult;
import RequestResult.PersonAllResult;

//sets the data cache up to provide the rest of the program with the organized needed info
public class SetDataCache {

    public String[] setDataCache(String[] data) throws IOException {
        String firstName = null;
        String lastName = null;
        ServerFacade serverFacade = new ServerFacade();
        PersonAllResult personAllResult = serverFacade.getPersons(data[0], data[1], data[2]);
        EventAllResult eventAllResult = serverFacade.getEvents(data[0], data[1], data[2]);
        HashMap<String, Person> people = new HashMap<>();
        for (Person person : personAllResult.getData()) {
            people.put(person.getPersonID(), person);
            if (person.getPersonID().equals(data[3])) {
                firstName = person.getFirstName();
                lastName = person.getLastName();
            }
        }
        DataCache.setPeople(people);
        HashMap<String, Event> events = new HashMap<>();
        HashMap<String, LinkedList<Event>> personEvents = new HashMap<>();
        for (Event event : eventAllResult.getData()) {
            events.put(event.getEventID(), event);
            LinkedList<Event> idsEvents;
            if(personEvents.containsKey(event.getPersonID())) {
                idsEvents = personEvents.get(event.getPersonID());
                idsEvents.add(event);
            }
            else {
                idsEvents = new LinkedList<>();
                idsEvents.add(event);
            }
            personEvents.put(event.getPersonID(), idsEvents);

        }
        DataCache.setEvents(events);
        DataCache.setPersonEvents(personEvents);
        DataCache.setMaternal(data[3]);
        DataCache.setPaternal(data[3]);
        String[] name = {firstName, lastName};
        return name;
    }
}
