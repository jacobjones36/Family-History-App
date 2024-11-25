package com.jacob.family.map.project.anotherone.Helpers;

import java.util.Comparator;

import Model.Event;

//organizes the events in the linked list to be sorted by year
public class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event event, Event t1) {
        if(event.getYear() < t1.getYear()) {
            return -1;
        }
        else if(event.getYear() == t1.getYear()) {
            for(int i = 0; i < t1.getEventType().length(); ++i) {
                if(event.getEventType().toLowerCase().charAt(i) < t1.getEventType().toLowerCase().charAt(i)) {
                    return -1;
                }
                else if(event.getEventType().toLowerCase().charAt(i) > t1.getEventType().toLowerCase().charAt(i)) {
                    return 1;
                }
            }
            return 0;
        }
        else {
            return 1;
        }
    }
}
