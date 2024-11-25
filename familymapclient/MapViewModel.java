package com.jacob.family.map.project.anotherone.Helpers;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.*;

import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jacob.family.map.project.anotherone.DataCache;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;


import Model.Event;
import Model.Person;

public class MapViewModel extends ViewModel {
    private final String TAG = "MapViewModel";
    private GoogleMap googleMap;
    private static HashSet<Polyline> lines = new HashSet<>();
    private static String eventId;
    private final static HashMap<String, String> randomEventTypes = new HashMap<>();

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void createMarkers() {
        //clear map of any previous markers
        googleMap.clear();
        //check if settings have changed and if so filter out
        Filter filter = new Filter();
        if(filter.filterEvents()) {
            Log.d(TAG, String.valueOf(DataCache.getFilteredEvents().size()));
            Marker marker = null;
            // for each event get the event type and attach associated color
            for (Map.Entry<String, LinkedList<Event>> entry : DataCache.getFilteredEvents().entrySet()) {
                for (Event event : entry.getValue()) {
                    float googleColor = BitmapDescriptorFactory.HUE_GREEN;
                    if (event.getEventType().toLowerCase().equals("birth")) {
                        googleColor = HUE_BLUE;
                    } else if (event.getEventType().toLowerCase().equals("marriage")) {
                        googleColor = HUE_RED;
                    } else if (event.getEventType().toLowerCase().equals("death")) {
                        googleColor = HUE_GREEN;
                    } else {
                        googleColor = assignOtherEventColor(event.getEventType());
                    }
                    LatLng eventToAdd = new LatLng(event.getLatitude(), event.getLongitude());
                    marker = googleMap.addMarker(new MarkerOptions().position(eventToAdd)
                            .icon(BitmapDescriptorFactory.defaultMarker(googleColor)));
                    marker.setTag(event);
                }
            }
            setMap(googleMap);
        }

    }

    //tracks which other events have been already added and which color they are associated with
    //if the color hasn't been used already it assigns it a random new one
    private float assignOtherEventColor(String eventType) {
        float googleColor;
        if (randomEventTypes.size() == 0) {
            randomEventTypes.put(eventType, "yellow");
            googleColor = HUE_YELLOW;
        } else {
            googleColor = 0;
            for (Map.Entry<String, String> eventTypes : randomEventTypes.entrySet()) {
                if (eventType.toLowerCase().equals(eventTypes.getKey().toLowerCase())) {
                    googleColor = getRandomColor(eventTypes.getValue());
                }
            }
            if(googleColor == 0) {
                Random rand = new Random();
                String[] colorArray = {"azure", "cyan", "magenta", "orange", "rose",
                        "violet"};
                int randomInt = rand.nextInt(colorArray.length);
                String color = colorArray[randomInt];
                randomEventTypes.put(eventType, color);
                googleColor = getRandomColor(color);
            }
        }
        return googleColor;
    }

    private float getRandomColor(String color) {
        float googleColor;
        switch (color) {
            case ("yellow"):
                googleColor = HUE_YELLOW;
                break;
            case ("azure"):
                googleColor = HUE_AZURE;
                break;
            case ("cyan"):
                googleColor = HUE_CYAN;
                break;
            case ("magenta"):
                googleColor = HUE_MAGENTA;
                break;
            case ("orange"):
                googleColor = HUE_ORANGE;
                break;
            case ("rose"):
                googleColor = HUE_ROSE;
                break;
            case ("violet"):
                googleColor = HUE_VIOLET;
                break;
            default:
                googleColor = HUE_GREEN;
        }
        return googleColor;
    }

    private void drawLine(Event startEvent, Event endEvent, int color, float width) {

        //create start and end points
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        //add line to map specifying endpoints, color and width
        PolylineOptions options = new PolylineOptions()
                .add(startPoint)
                .add(endPoint)
                .color(color)
                .width(width);
        Polyline line = googleMap.addPolyline(options);
        lines.add(line);
    }

    public void addLines(Event clickedEvent) {
        for(Polyline line : lines) {
            line.remove();
        }
        Log.d(TAG, "addLines");
        HashMap<String, LinkedList<Event>> filteredEvents = DataCache.getFilteredEvents();
        Person person = DataCache.getPersonById(clickedEvent.getPersonID());
        int googleColor;

        //spouse line
        if (Setting.isSpouseLines() && filteredEvents.get(person.getSpouseID()) != null) {
            Event spouseEvent = getPersonEvent(person.getSpouseID());
            googleColor = Color.RED;
            drawLine(clickedEvent, spouseEvent, googleColor, 6);
        }

        //family tree lines
        if (Setting.isFamilyTreeLines()) {
            getParentsLine(person, clickedEvent, 25);
        }

        //life story lines
        if (Setting.isLifeStoryLines()) {
            LinkedList<Event> clickedPersonsEvents = filteredEvents.get(person.getPersonID());
            Collections.sort(clickedPersonsEvents, new EventComparator());
            googleColor = Color.BLUE;
            for(int i = 0; i < clickedPersonsEvents.size() - 1; ++i) {
                drawLine(clickedPersonsEvents.get(i), clickedPersonsEvents.get(i+1), googleColor, 6);
            }
        }
    }

    //recursive through the family to get each line
    private void getParentsLine(Person person, Event currEvent, int width) {
        HashMap<String, LinkedList<Event>> filteredEvents = DataCache.getFilteredEvents();
        if(person.getFatherID() != null && filteredEvents.containsKey(person.getFatherID())) {
            Person father = DataCache.getPersonById(person.getFatherID());
            Event fatherEvent = getPersonEvent(person.getFatherID());
            int googleColor =Color.GREEN;
            drawLine(currEvent, fatherEvent, googleColor, width);
            getParentsLine(father, fatherEvent, width-5);
        }

        if(person.getMotherID() != null && filteredEvents.containsKey(person.getMotherID())) {
            Person mother = DataCache.getPersonById(person.getMotherID());
            Event motherEvent = getPersonEvent(person.getMotherID());
            int googleColor = Color.GREEN;
            drawLine(currEvent, motherEvent, googleColor, width);
            getParentsLine(mother, motherEvent, width-5);
        }
    }

    //gets the event associated with the given id
    private Event getPersonEvent(String personID) {
        PriorityQueue<Event> spouseEvents = DataCache.getEventsForPerson(personID);
        Log.d(TAG, spouseEvents.peek().toString());
        return spouseEvents.peek();
    }
}
