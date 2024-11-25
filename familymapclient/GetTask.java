package com.jacob.family.map.project.anotherone;

import android.util.Log;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import Model.Event;
import Model.Person;
import RequestResult.EventAllResult;
import RequestResult.PersonAllResult;

public class GetTask implements Runnable {
    private final String TAG = "GetTask";
    private final String FIRST_NAME_KEY = "firstName";
    private final String LAST_NAME_KEY = "lastName";

    private final Handler messageHandler;
    private final String[] data;

    public GetTask(Handler messageHandler, String[] data) {
        this.messageHandler = messageHandler;
        this.data = data;
    }

    @Override
    public void run() {
        Log.d(TAG, "GetTask Running");
        ServerFacade serverFacade = new ServerFacade();
        String firstName = null;
        String lastName = null;
        try {
            PersonAllResult personAllResult = serverFacade.getPersons(data[0], data[1], data[2]);
            EventAllResult eventAllResult = serverFacade.getEvents(data[0], data[1], data[2]);
            HashMap<String, Person> people = new HashMap<>();
            for (Person person : personAllResult.getData()) {
                people.put(person.getPersonID(), person);
                if (person.getPersonID().equals(data[3])) {
                    firstName = person.getFirstName();
                    lastName = person.getLastName();
                    sendMessage(firstName, lastName);
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
            Log.d(TAG, DataCache.getUserId() + "Users personId");
            DataCache.setMaternal(DataCache.getUserId());
            DataCache.setPaternal(DataCache.getUserId());
        } catch (IOException e) {
            Log.d(TAG, "IOException was caught");
            e.printStackTrace();
        }

    }

    private void sendMessage(String firstName, String lastName) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString(FIRST_NAME_KEY, firstName);
        messageBundle.putString(LAST_NAME_KEY, lastName);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
