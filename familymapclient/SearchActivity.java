package com.jacob.family.map.project.anotherone;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import Model.Event;
import Model.Person;


public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_VIEW_TYPE = 0;
    private static final int EVENT_VIEW_TYPE = 1;
    private final String TAG = "SearchActivity";
    private final String PERSON_KEY = "ClickedPerson";
    private EditText searchBar;
    private RecyclerView recyclerView;
    private final String EVENT_KEY = "ClickedEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate Called");
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.searchTextField);
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged");
                List<Event> eventList = new LinkedList<>();
                for (LinkedList<Event> hashEvent : DataCache.getFilteredEvents().values()) {
                    for(Event event : hashEvent) {
                        String eventString = event.getEventType().toUpperCase() + event.getCity() +
                                event.getCountry() + event.getYear();
                        if (eventString.contains(charSequence) || eventString.toLowerCase().contains(charSequence)) {
                            eventList.add(event);
                        }
                    }
                }
                List<Person> personList = new LinkedList<>();
                for (Person person : DataCache.getPeople().values()) {
                    String name = person.getFirstName() + " " + person.getLastName();
                    if(name.contains(charSequence) || name.toLowerCase().contains(charSequence)) {
                        personList.add(person);
                        for(String personId : DataCache.getFilteredEvents().keySet()) {
                            if(person.getPersonID().equals(personId)) {
                                for(Event event : DataCache.getEventsForPerson(personId)) {
                                    eventList.add(event);
                                }
                            }
                        }
                    }
                }

                SearchAdapter searchAdapter = new SearchAdapter(eventList, personList);
                recyclerView.setAdapter(searchAdapter);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                onTextChanged(editable, 1,2,  3);
            }
        });
    }
    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

        private final List<Event> eventList;
        private final List<Person> personList;

        public SearchAdapter(List<Event> eventList, List<Person> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }

        @Override
        public int getItemViewType(int position) {
            return  position < personList.size() ? PERSON_VIEW_TYPE : EVENT_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_list, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.event_list, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < personList.size()) {
                holder.bind(personList.get(position));
            }
            else {
                holder.bind(eventList.get(position - personList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return personList.size() + eventList.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView firstRow;
        private final TextView secondRow;
        private final ImageView icon;
        private final int viewType;
        private Person person;
        private Event event;
        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_VIEW_TYPE) {
                icon = itemView.findViewById(R.id.gender_icon);
                firstRow = itemView.findViewById(R.id.personName);
                secondRow = null;
            }
            else {
                icon = itemView.findViewById(R.id.location_icon);
                firstRow = itemView.findViewById(R.id.eventInfo);
                secondRow = itemView.findViewById(R.id.eventPersonName);
            }
        }

        private void bind(Person person) {
            Log.d(TAG, "Bind Person");
            this.person = person;
            if(person.getGender().equals("m")) {
                icon.setImageResource(R.drawable.man_icon);
            }
            else {
                icon.setImageResource(R.drawable.woman_icon);
            }
            firstRow.setText(person.getFirstName() + " " + person.getLastName());
        }
        private void bind(Event event) {
            Log.d(TAG, "Bind event");
            this.event = event;
            icon.setImageResource(R.drawable.location_icon);
            firstRow.setText(event.getEventType().toUpperCase() + ": " + event.getCity() +
                    ", " + event.getCountry() + "(" + event.getYear() + ")");
            Person person = DataCache.getPersonById(event.getPersonID());
            Log.d(TAG, person.toString());
            secondRow.setText(person.getFirstName() + " " + person.getLastName());
        }
        @Override
        public void onClick(View view) {
            if(viewType == PERSON_VIEW_TYPE) {
                Log.d(TAG, "viewTypeIsPerson");
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PERSON_KEY, person.getPersonID());
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EVENT_KEY, event.getEventID());
                startActivity(intent);
            }

        }
    }
}
