package com.jacob.family.map.project.anotherone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Set;

public class SettingActivity extends AppCompatActivity {
    private static String TAG = "SettingActivity";
    private static Switch lifeStorySwitch;
    private Switch familyTreeSwitch;
    private Switch spouseSwitch;
    private Switch fatherSwitch;
    private Switch motherSwitch;
    private Switch maleSwitch;
    private Switch femaleSwitch;
    private TextView logout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateCalled");
        setContentView(R.layout.settings_layout);

        Setting.setChange(false);
        lifeStorySwitch = findViewById(R.id.lifeStoryLinesToggle);
        familyTreeSwitch = findViewById(R.id.familyTreeLinesToggle);
        spouseSwitch = findViewById(R.id.SpouseLinesToggle);
        fatherSwitch = findViewById(R.id.FatherSideToggle);
        motherSwitch = findViewById(R.id.motherSideToggle);
        maleSwitch = findViewById(R.id.maleEventsToggle);
        femaleSwitch = findViewById(R.id.femaleEventsToggle);
        logout = findViewById(R.id.logoutView);
        if(Setting.isLifeStoryLines()) {
            lifeStorySwitch.setChecked(true);
        }
        if(Setting.isFamilyTreeLines()) {
            familyTreeSwitch.setChecked(true);
        }
        if(Setting.isSpouseLines()) {
            spouseSwitch.setChecked(true);
        }
        if(Setting.isFatherSide()) {
            fatherSwitch.setChecked(true);
        }
        if(Setting.isMothersSide()) {
            motherSwitch.setChecked(true);
        }
        if(Setting.isMale()) {
            maleSwitch.setChecked(true);
        }
        if(Setting.isFemale()) {
            femaleSwitch.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_button_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResumeCalled");

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, String.valueOf(b));
                Setting.setLifeStoryLines(b);
                Setting.setChange(true);
            }
        });
        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Setting.setFamilyTreeLines(b);
                Setting.setChange(true);
            }
        });
        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Setting.setSpouseLines(b);
                Setting.setChange(true);
            }
        });
        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Setting.setFatherSide(b);
                Setting.setChange(true);
            }
        });
        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Setting.setMothersSide(b);
                Setting.setChange(true);
            }
        });
        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Setting.setMale(b);
                Setting.setChange(true);
            }
        });
        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Setting.setFemale(b);
                Setting.setChange(true);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
}
