package com.jacob.family.map.project.anotherone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {
    private final String TAG = "PersonActivity";
    private final String PERSON_KEY = "ClickedPerson";
    private final String EVENT_KEY = "ClickedEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String personId = intent.getStringExtra(PERSON_KEY);
        Person person = DataCache.getPersonById(personId);
        setContentView(R.layout.activity_person);
        TextView firstName = findViewById(R.id.firstNameText);
        firstName.setText(person.getFirstName());
        TextView lastName = findViewById(R.id.lastNameText);
        lastName.setText(person.getLastName());
        TextView gender = findViewById(R.id.genderText);
        if(person.getGender().equals("m")) {
            gender.setText("Male");
        }
        else {
            gender.setText("Female");
        }

        Log.d(TAG, personId.toString());
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        //creating event list
        LinkedList<Event> eventList = DataCache.getFilteredEvents().get(personId);
        if(eventList != null) {
            Collections.sort(eventList, new EventComparator());
        }

        //creating list of relatives
        LinkedList<String[]> personList = getImmediateFamily(person);

        expandableListView.setAdapter(new ExpandableListAdapter(eventList, personList));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return true;
    }

    private LinkedList<String[]> getImmediateFamily(Person person) {
        Log.d(TAG, "Entering getFamily");
        LinkedList<String[]> immediateFamily = new LinkedList<>();

        if(person.getFatherID() != null) {
            Person father = DataCache.getPersonById(person.getFatherID());
            String[] fatherInfo = {father.getPersonID(), father.getFirstName(), father.getLastName(), "Father", father.getGender()};
            immediateFamily.add( fatherInfo);
        }
        Log.d(TAG, person.toString());
        if(person.getMotherID() != null) {
            Person mother = DataCache.getPersonById(person.getMotherID());
            String[] motherInfo = {mother.getPersonID(), mother.getFirstName(), mother.getLastName(), "Mother", mother.getGender()};
            immediateFamily.add(motherInfo);
        }
        if(person.getSpouseID() != null) {
            Person spouse = DataCache.getPersonById(person.getSpouseID());
            String[] spouseInfo = {spouse.getPersonID(), spouse.getFirstName(), spouse.getLastName(), "Spouse", spouse.getGender()};
            immediateFamily.add( spouseInfo);
        }

        if(!DataCache.getChildren(person).isEmpty()) {
            Person child = DataCache.getChildren(person).elementAt(0);
            String[] childInfo = {child.getPersonID(), child.getFirstName(), child.getLastName(), "Child", child.getGender()};
            immediateFamily.add( childInfo);
        }
        return immediateFamily;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_POSITION = 0;
        private static final int PERSON_POSITION = 1;
        private final List<Event> eventList;
        private final List<String[]> personList;

        public ExpandableListAdapter(List<Event> eventList, List<String[]> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int position) {
            switch (position) {
                case EVENT_POSITION:
                    if(eventList == null) {
                        return 0;
                    }
                    else {
                        return eventList.size();
                    }
                case PERSON_POSITION:
                    Log.d(TAG, String.valueOf(personList.size()));
                    return personList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getGroup(int position) {
            switch (position) {
                case EVENT_POSITION:
                    return getString(R.string.events_expandable_list_title);
                case PERSON_POSITION:
                    return getString(R.string.person_expandable_list_title);
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return eventList.get(childPosition);
                case PERSON_POSITION:
                    return personList.get(childPosition);
                default: throw new IllegalArgumentException("Unrecognized group position");
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_POSITION:
                    titleView.setText(R.string.events_expandable_list_title);
                    break;
                case PERSON_POSITION:
                    titleView.setText(R.string.person_expandable_list_title);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView = null;


            switch (groupPosition) {
                case EVENT_POSITION:
                    if(!eventList.isEmpty()) {
                        itemView = getLayoutInflater().inflate(R.layout.event_list, parent, false);
                        initializeEventView(itemView, childPosition);
                    }
                    break;
                case PERSON_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_list, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position");
            }

            return itemView;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            TextView eventInfoView = eventItemView.findViewById(R.id.eventInfo);
            Event event = eventList.get(childPosition);
            Log.d(TAG, event.toString() + "Event ToString");
            eventInfoView.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getYear() + ")");

            TextView eventPersonNameView = eventItemView.findViewById(R.id.eventPersonName);
            Person person = DataCache.getPersonById(event.getPersonID());
            Log.d(TAG, person.toString() + "PersonToString");
            eventPersonNameView.setText(person.getFirstName() + " " + person.getLastName());
            ImageView locationIcon = eventItemView.findViewById(R.id.location_icon);
            locationIcon.setImageResource(R.drawable.location_icon);

            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(EVENT_KEY, event.getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializePersonView(View personItemView, int childPosition) {
            TextView personNameView = personItemView.findViewById(R.id.personName);
            Log.d(TAG, personList.get(childPosition) + "person first name");
            personNameView.setText(personList.get(childPosition)[1] + " " +
                    personList.get(childPosition)[2]);
            TextView personRelationView = personItemView.findViewById(R.id.personRelation);
            personRelationView.setText(personList.get(childPosition)[3]);
            ImageView genderIcon = personItemView.findViewById(R.id.gender_icon);
            if(personList.get(childPosition)[4].equals("m")) {
                genderIcon.setImageResource(R.drawable.man_icon);
            }
            else{
                genderIcon.setImageResource(R.drawable.woman_icon);
            }

            personItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PERSON_KEY, personList.get(childPosition)[0]);
                    startActivity(intent);

                }
            });
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
