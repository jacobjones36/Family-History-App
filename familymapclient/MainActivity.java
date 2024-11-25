package com.jacob.family.map.project.anotherone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {
    private ViewModel viewModel;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate Called");

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);
        if (currentFragment == null) {
            currentFragment = createLoginFragment();
            fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, currentFragment).commit();
        }
        else {
            if(currentFragment instanceof LoginFragment) {
                ((LoginFragment) currentFragment).registerListener(this);
            }
        }

    }

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener(this);
        return fragment;
    }

    @Override
    public void notifyDone() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, fragment).commit();
        //viewModel = new ViewModelProvider(this).get(MapFragment.class);
    }
}
