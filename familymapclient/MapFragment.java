package com.jacob.family.map.project.anotherone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final String TAG = "MapFragment";
    private static String eventId = null;
    private final String EVENT_KEY = "ClickedEvent";
    private final String PERSON_KEY = "ClickedPerson";

    private MapViewModel getViewModel() {
        return new ViewModelProvider(this).get(MapViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreate Called");
        Bundle args = getArguments();
        if(args != null) {
            Log.d(TAG, args.getString(EVENT_KEY) + "event Id kry");
            getViewModel().setEventId(args.getString(EVENT_KEY));
        }
        else {
            getViewModel().setEventId(null);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        if(getViewModel().getEventId() != null) {
            createEventInfo(eventId, view);
        }
        SupportMapFragment mapFragment =
                (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, menuInflater);
        if(getActivity().getClass() == MainActivity.class) {
            menuInflater.inflate(R.menu.options_menu, menu);
        }
        else {
            menuInflater.inflate(R.menu.back_button_menu, menu);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if(item.getItemId() == R.id.searchMenuItem) {
            Log.d(TAG, "item search");
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.settingMenuItem){
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        getViewModel().setMap(googleMap);
        eventId = getViewModel().getEventId();
        if(eventId == null) {
            //fix camera in dallas
            getViewModel().createMarkers();
            getViewModel().getGoogleMap().animateCamera(CameraUpdateFactory.newLatLng(new LatLng(32.7767, -96.7970)));
        }
        else {
            Log.d(TAG, "createMarkers onMapReady");
            getViewModel().createMarkers();
            Event event = DataCache.getEventById(eventId);
            getViewModel().getGoogleMap().animateCamera(CameraUpdateFactory.newLatLng(new LatLng(event.getLatitude(), event.getLongitude())));
            getViewModel().addLines(event);
        }
        getViewModel().getGoogleMap().setOnMarkerClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if(Setting.isChange()) {
            Setting.setChange(false);
            Log.d(TAG, "creatingMakers");
            getViewModel().createMarkers();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Event clickedEvent = (Event) marker.getTag();
        Log.d(TAG, getActivity().toString());
        Intent intent = new Intent(getActivity(), EventActivity.class);
        intent.putExtra(EVENT_KEY, clickedEvent.getEventID());
        startActivity(intent);

        return true;
    }

    private void createEventInfo(String eventId, View view) {
        Event event = DataCache.getEventById(getViewModel().getEventId());
        Person person = DataCache.getPersonById(event.getPersonID());
        ImageView genderIcon = (ImageView) view.findViewById(R.id.android_guy);
        if(person.getGender().equals("m")) {
            genderIcon.setImageResource(R.drawable.man_icon);
        }
        else {
            genderIcon.setImageResource(R.drawable.woman_icon);
        }
        TextView nameText = (TextView) view.findViewById(R.id.nameRowText);
        nameText.setText(person.getFirstName() + " " + person.getLastName());
        TextView eventText = (TextView) view.findViewById(R.id.eventRowText);
        eventText.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " +
                event.getCountry() + " (" + event.getYear() + ")");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra(PERSON_KEY, person.getPersonID());
                startActivity(intent);
            }
        });
    }
}


