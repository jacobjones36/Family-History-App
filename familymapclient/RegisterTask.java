package com.jacob.family.map.project.anotherone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;

public class RegisterTask implements Runnable{
    private final String TAG = "RegisterTask";
    private static final String SUCCESS_BOOL_KEY = "success";
    private static final String PERSON_ID_KEY = "personID";
    private final Handler messageHandler;
    private final String[] data;

    public RegisterTask(Handler messageHandler, String[] data) {
        this.messageHandler = messageHandler;
        this.data = data;
    }

    @Override
    public void run() {
        Log.d(TAG, "RUNNING");
        ServerFacade serverFacade = new ServerFacade();
        RegisterRequest registerRequest = new RegisterRequest(data[2], data[3], data[4], data [5],
                data[6], data[7]);
        try {
            RegisterResult registerResult = serverFacade.register(data[0], data[1], registerRequest);
            if(registerResult.isSuccess()) {
                DataCache.setUserId(registerResult.getPersonID());
                String[] GetTaskData = {data[0], data[1], registerResult.getAuthtoken(), registerResult.getPersonID()};
                GetTask getTask = new GetTask(messageHandler, GetTaskData);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(getTask);
            }
            else {
                Log.d(TAG, "FAIl BABY");
                sendMessage(registerResult.isSuccess());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
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
