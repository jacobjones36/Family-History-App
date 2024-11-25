package com.jacob.family.map.project.anotherone;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

import Model.Event;
import Model.Person;

public final class DataCache {
    private static DataCache instance = new DataCache();
    private static DataCache getInstance() {
        return instance;
    }

    private DataCache() {}

    //map with personId as key and the person object as the value
    private static Map<String, Person> people;
    //map with eventId and events
    private static Map<String, Event> events;
    //map with personId and event
    private static Map<String, LinkedList<Event>> personEvents;
    //set of the maternal persons ids
    private static HashSet<String> maternal;
    //set of the paternal persons ids
    private static HashSet<String> paternal;
    //vector to return the children. not necessary for this assignment but if more children were
    //added it would return each child
    private static Vector<Person> personsRelations;

    private static String userId;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        DataCache.userId = userId;
    }

    public static Vector<Person> getChildren(Person person) {
        personsRelations = new Vector<>();
        if(person.getGender().equals("m")) {
            for (Map.Entry<String, Person> entry: getPeople().entrySet()) {
                if (entry.getValue().getFatherID() != null && entry.getValue().getFatherID().equals(person.getPersonID())) {
                    personsRelations.addElement(entry.getValue());
                }
            }
        }
        else if(person.getGender().equals("f")) {
            for (Map.Entry<String, Person> entry : getPeople().entrySet()) {
                if (entry.getValue().getMotherID() != null && entry.getValue().getMotherID().equals(person.getPersonID())) {
                    personsRelations.addElement(entry.getValue());
                }
            }
        }
        return personsRelations;
    }



    public static HashSet<String> getPaternal(){
        return paternal;
    }

    public static HashSet<String> getMaternal() {
        return maternal;
    }
    public static HashMap<String, LinkedList<Event>> filteredEvents;

    public static HashMap<String, LinkedList<Event>> getFilteredEvents() {
        return filteredEvents;
    }



    public static void setFilteredEvents(HashMap<String, LinkedList<Event>> filteredEvents) {
        DataCache.filteredEvents = filteredEvents;
    }

    public static void setPaternal(String personId) {
        HashSet<String> toSetPaternal = new HashSet<>();
        Person person = getPersonById(personId);
        if(person.getFatherID() != null) {
            toSetPaternal.add(person.getFatherID());
            toSetPaternal = setPaternalRecursive(person.getFatherID(), toSetPaternal);
        }
        paternal = toSetPaternal;
    }
    private static HashSet<String> setPaternalRecursive(String personId, HashSet<String> toAddPaternal) {
        Person person = getPersonById(personId);
        if(person.getMotherID() != null) {
            toAddPaternal.add(person.getMotherID());
            setPaternalRecursive(person.getMotherID(), toAddPaternal);
        }
        if(person.getFatherID() != null) {
            toAddPaternal.add(person.getFatherID());
            setPaternalRecursive(person.getFatherID(), toAddPaternal);
        }
        return toAddPaternal;
    }
    public static void setMaternal(String personId) {
        HashSet<String> toSetMaternal = new HashSet<>();
        Person person = getPersonById(personId);
        if(person.getMotherID() != null) {
            toSetMaternal.add(person.getMotherID());
            toSetMaternal = setMaternalRecursive(person.getMotherID(), toSetMaternal);
        }
        maternal = toSetMaternal;
    }

    private static HashSet<String> setMaternalRecursive(String personId, HashSet<String> toSetMaternal) {
        Person person = getPersonById(personId);
        if(person.getMotherID() != null) {
            toSetMaternal.add(person.getMotherID());
            setMaternalRecursive(person.getMotherID(), toSetMaternal);
        }
        if(person.getFatherID() != null) {
            toSetMaternal.add(person.getFatherID());
            setMaternalRecursive(person.getFatherID(), toSetMaternal);
        }
        return toSetMaternal;
    }

    public static Person getPersonById(String personId) {
        Person requestedPerson = null;
        for (Map.Entry<String, Person> myPeople : getPeople().entrySet()) {
            if (myPeople.getValue().getPersonID().equals(personId)) {
                requestedPerson = myPeople.getValue();
            }
        }
        return requestedPerson;
    }
    public static Event getEventById(String eventId) {
        Event requestedEvent = null;
        for (Map.Entry<String, Event> myEvents : getEvents().entrySet()) {
            if (myEvents.getValue().getEventID().equals(eventId)) {
                requestedEvent = myEvents.getValue();
            }
        }
        return requestedEvent;
    }
    public static PriorityQueue<Event> getEventsForPerson(String personId){
        PriorityQueue<Event> eventsForPerson = new PriorityQueue<>(3, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                if (event.getYear() < t1.getYear()) {
                    return -1;
                }
                else {return 1;}
            }
        });

        for (Map.Entry<String, Event> myEvents : getEvents().entrySet()) {
            if (myEvents.getValue().getPersonID().equals(personId)) {
                eventsForPerson.add(myEvents.getValue());
            }
        }
        return eventsForPerson;
    }


    public static Map<String, Person> getPeople() {
        return people;
    }

    public static void setPeople(Map<String, Person> people) {
        DataCache.people = people;
    }

    public static Map<String, Event> getEvents() {
        return events;
    }

    public static void setEvents(Map<String, Event> events) {
        DataCache.events = events;
    }

    public static Map<String, LinkedList<Event>> getPersonEvents() {
        return personEvents;
    }

    public static void setPersonEvents(Map<String, LinkedList<Event>> personEvents) {
        DataCache.personEvents = personEvents;
    }



}

