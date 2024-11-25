package com.jacob.family.map.project.anotherone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;

public class LoginFragment extends Fragment {


    private final String TAG = "LoginFragment";
    private static final String SUCCESS_BOOL_KEY = "success";
    private final String FIRST_NAME_KEY = "firstName";
    private final String LAST_NAME_KEY = "lastName";

    private EditText usernameET;
    private EditText passwordET;
    private EditText hostET;
    private EditText portET;
    private EditText firstNameET;
    private EditText lastNameET;
    private EditText emailET;
    private Button registerButton;
    private Button signInButton;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private String username = null;
    private String password = null;
    private String hostName = "10.0.2.2";
    private String portNum = "8080";
    private String firstName = null;
    private String lastName = null;
    private String gender = null;
    private String email = null;


    private Listener listener;

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate Called");
    }

    @Override
    public View onCreateView(LayoutInflater inflation, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView Called");
        View view = inflation.inflate(R.layout.fragment_login, container, false);

        hostET = view.findViewById(R.id.serverHostField);
        portET = view.findViewById(R.id.serverPort);
        usernameET = view.findViewById(R.id.username);
        passwordET = view.findViewById(R.id.password);
        firstNameET = view.findViewById(R.id.firstName);
        lastNameET = view.findViewById(R.id.lastName);
        emailET = view.findViewById(R.id.emailAccount);
        maleRadioButton = view.findViewById(R.id.maleCheckBox);
        femaleRadioButton = view.findViewById(R.id.femaleCheckBox);
        signInButton = view.findViewById(R.id.signInButton);
        registerButton = view.findViewById(R.id.registerButton);
        signInButton.setEnabled(false);
        registerButton.setEnabled(false);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickSignInButton");

                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        Boolean success = bundle.getBoolean(SUCCESS_BOOL_KEY, true);
                        if(!success) {
                            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                        String firstName = bundle.getString(FIRST_NAME_KEY, null);
                        String lastName = bundle.getString(LAST_NAME_KEY, null);
                        if (firstName != null && lastName != null) {
                            Toast.makeText(getContext(), firstName + " " + lastName, Toast.LENGTH_SHORT).show();
                            if(listener != null) {
                                Log.d(TAG, "Putting up Map from login");
                                listener.notifyDone();
                            }
                        }



                    }
                };
                String[] data = {hostName, portNum, username, password};
                LoginTask task = new LoginTask(uiThreadMessageHandler, data);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClickRegisterButton");
                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        Boolean success = bundle.getBoolean(SUCCESS_BOOL_KEY, true);
                        if(!success) {
                            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                        String firstName = bundle.getString(FIRST_NAME_KEY, null);
                        String lastName = bundle.getString(LAST_NAME_KEY, null);
                        if (firstName != null && lastName != null) {
                            Toast.makeText(getContext(), firstName + " " + lastName, Toast.LENGTH_SHORT).show();
                            if(listener != null) {
                                Log.d(TAG, "Putting up Map from login");
                                listener.notifyDone();
                            }
                        }


                    }
                };
                String[] data = {hostName, portNum, username, password, email, firstName, lastName, gender};
                RegisterTask task = new RegisterTask(uiThreadMessageHandler, data);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(task);

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume Called");

        hostET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                hostName = charSequence.toString();
                if(username != null && portNum != null && password != null) {
                    signInButton.setEnabled(true);
                }
                if(username != null && password != null && hostName != null && portNum != null &&
                email != null && firstName != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        portET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                portNum = charSequence.toString();
                if(username != null && hostName != null && password != null) {
                    signInButton.setEnabled(true);
                }
                if(username != null && password != null && hostName != null && portNum != null &&
                        email != null && firstName != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        usernameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                username = charSequence.toString();
                if(portNum != null && hostName != null && password != null) {
                    signInButton.setEnabled(true);
                }
                if(username != null && password != null && hostName != null && portNum != null &&
                        email != null && firstName != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                password = charSequence.toString();
                if(username != null && portNum != null && hostName != null) {
                    signInButton.setEnabled(true);
                }
                if(username != null && password != null && hostName != null && portNum != null &&
                        email != null && firstName != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        firstNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                firstName = charSequence.toString();
                if(username != null && password != null && hostName != null && portNum != null &&
                        email != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        lastNameET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                lastName = charSequence.toString();
                if(username != null && password != null && hostName != null && portNum != null &&
                        email != null && firstName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                email = charSequence.toString();
                if(username != null && password != null && hostName != null && portNum != null &&
                        firstName != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        maleRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    gender = "m";
                }
                if(username != null && password != null && hostName != null && portNum != null &&
                        email != null && firstName != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }
        });

        femaleRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    gender = "f";
                }
                if(username != null && password != null && hostName != null && portNum != null &&
                        email != null && firstName != null && lastName != null && gender != null) {
                    registerButton.setEnabled(true);
                }
            }
        });
    }


    /*@Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSavedInstanceState Called");
        outState.putString(USERNAME_KEY, username);
        outState.putString(PASSWORD_KEY, password);
    }*/
}



