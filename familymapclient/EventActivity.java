package com.jacob.family.map.project.anotherone;

import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import Model.Event;

public class EventActivity extends AppCompatActivity {
    private static String TAG = "EventActivity";
    private MapFragment fragment;
    private final String EVENT_KEY = "ClickedEvent";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragment = new MapFragment();
        String eventId = intent.getStringExtra(EVENT_KEY);
        Bundle args = new Bundle();
        args.putString(EVENT_KEY, eventId);
        fragment.setArguments(args);


        fragmentManager.beginTransaction().replace(R.id.eventActivityLayout, fragment).commit();
    }


}
