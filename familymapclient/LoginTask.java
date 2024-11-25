package com.jacob.family.map.project.anotherone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import RequestResult.LoginRequest;
import RequestResult.LoginResult;

public class LoginTask implements Runnable {
    private final String TAG = "LoginTask";
    private static final String USERNAME_KEY = "username";
    private static final String AUTH_TOKEN_KEY = "authToken";
    private static final String SUCCESS_BOOL_KEY = "success";
    private static final String PASSWORD_KEY = "password";
    private static final String HOST_NAME_KEY = "host name";
    private static final String PORT_NUMBER_KEY = "port number";
    private static final String PERSON_ID_KEY = "personID";


    private final Handler messageHandler;
    private final String[] data;

    public LoginTask(Handler messageHandler, String[] data) {
        this.messageHandler = messageHandler;
        this.data = data;
    }

    @Override
    public void run() {
        Log.d(TAG, "LoginTask RUN DANANNANANA");
        ServerFacade serverFacade = new ServerFacade();
        LoginRequest loginRequest = new LoginRequest(data[2], data[3]);
        try {
            LoginResult loginResult = serverFacade.login(data[0], data[1], loginRequest);
            Log.d(TAG, loginResult.toString());
            if (loginResult.isSuccess()) {
                Log.d(TAG, loginResult.toString());
                DataCache.setUserId(loginResult.getPersonID());
                Log.d(TAG, "SUCCESS");
                String[] GetTaskData = {data[0], data[1], loginResult.getAuthtoken(), loginResult.getPersonID()};
                GetTask getTask = new GetTask(messageHandler, GetTaskData);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(getTask);
            }
            else{
                Log.d(TAG, "ELSE FAIL");
                sendMessage(false);
            }
        } catch (IOException e) {
            Log.d(TAG, "IOException was caught");
            e.printStackTrace();
        }
    }

    private void sendMessage(Boolean success) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean(SUCCESS_BOOL_KEY, success);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }

}
