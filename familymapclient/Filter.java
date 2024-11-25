package com.jacob.family.map.project.anotherone.Helpers;


import com.jacob.family.map.project.anotherone.DataCache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Model.Event;

//sets the filter event map in the data cache everytime the settings are adjusted
public class Filter {

    public boolean filterEvents() {
        HashMap<String, LinkedList<Event>> filteredEvents = new HashMap<>(DataCache.getPersonEvents());
        if(!Setting.isFatherSide()) {
            for(String personId : DataCache.getPaternal()) {
                filteredEvents.remove(personId);
            }
        }
        if(!Setting.isMothersSide()) {
            for(String personId : DataCache.getMaternal()) {
                filteredEvents.remove(personId);
            }
        }
        if(!Setting.isMale()) {
            for(Map.Entry<String, LinkedList<Event>> personId : DataCache.getPersonEvents().entrySet()) {
                String gender = DataCache.getPersonById(personId.getKey()).getGender();
                if(gender.equals("m")) {
                    filteredEvents.remove(personId.getKey());
                }
            }
        }
        if(!Setting.isFemale()) {
            for(Map.Entry<String, LinkedList<Event>> personId : DataCache.getPersonEvents().entrySet()) {
                if(DataCache.getPersonById(personId.getKey()).getGender().equals("f")) {
                    filteredEvents.remove(personId.getKey());
                }
            }
        }
        DataCache.setFilteredEvents(filteredEvents);
        return true;
    }
}
