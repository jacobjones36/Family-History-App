package com.jacob.family.map.project.anotherone.Helpers;

import com.jacob.family.map.project.anotherone.DataCache;

import java.util.LinkedList;

import Model.Event;
import Model.Person;

public class SearchResults {

    public LinkedList<Person> getPersonResults(CharSequence typedLetters) {
        LinkedList<Person> result = new LinkedList<>();
        if(typedLetters.length() == 0) {
            return result;
        }
        for (Person person : DataCache.getPeople().values()) {
            String name = person.getFirstName() + " " + person.getLastName();
            if (name.contains(typedLetters) || name.toLowerCase().contains(typedLetters)) {
                result.add(person);
            }
        }
        return result;
    }
    public LinkedList<Event> getEventResults(CharSequence typedLetters) {
        LinkedList<Event> result = new LinkedList<>();
        if(typedLetters.length() == 0) {
            return result;
        }
        for (LinkedList<Event> hashEvent : DataCache.getFilteredEvents().values()) {
            for(Event event : hashEvent) {
                String eventString = event.getEventType().toUpperCase() + event.getCity() +
                        event.getCountry() + event.getYear();
                if (eventString.contains(typedLetters) || eventString.toLowerCase().contains(typedLetters)) {
                    result.add(event);
                }
            }
        }
        return result;
    }
}
