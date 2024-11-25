package com.jacob.family.map.project.anotherone;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import RequestResult.EventAllResult;
import RequestResult.FailedResult;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import RequestResult.PersonAllResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;


public class ServerFacade {
    Gson gson = new Gson();
    private final String TAG = "ServerFacade";


    public LoginResult login(String serverHost, String serverPort, LoginRequest loginRequest) throws IOException {

        try {
            LoginResult loginResult = null;
            Log.d(TAG, "ENTERING SERVER FACADE");
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            String reqData =
                    "{" +
                            "\"username\": \"" + loginRequest.getUsername() + "\"," +
                            "\"password\": \"" + loginRequest.getPassword() + "\"" +
                            "}";
            Log.d(TAG, reqData);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            Log.d(TAG, reqBody.toString());
            reqBody.close();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = this.readString(resBody);
                loginResult = this.gson.fromJson(resData, LoginResult.class);
                Log.d(TAG, "SUCCESS BABY");
            }
            else {
                Log.d(TAG, "ERROR" + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String respData = readString(resBody);
                FailedResult failedResult = this.gson.fromJson(respData, FailedResult.class);
                loginResult = new LoginResult(false, failedResult.getMessage());
                Log.d(TAG, failedResult.getMessage());
            }
            Log.d(TAG, loginResult.toString());
            return loginResult;
        }
        catch (IOException e){
            e.printStackTrace();
            throw new IOException("Error: Thrown In ServerFacade When Logging in");
        }
    }

    public RegisterResult register(String serverHost, String serverPort, RegisterRequest registerRequest) throws IOException {
        try {
            RegisterResult registerResult;
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            String reqData =
                    "{" +
                            "\"username\": \"" + registerRequest.getUsername() + "\", " +
                            "\"password\": \"" + registerRequest.getPassword() + "\", " +
                            "\"email\": \"" + registerRequest.getEmail() + "\", " +
                            "\"firstName\": \"" + registerRequest.getFirstName() + "\", " +
                            "\"lastName\": \"" + registerRequest.getLastName() + "\", " +
                            "\"gender\": \"" + registerRequest.getGender() + "\"" +
                            "}";
            Log.d(TAG, reqData.toString());
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                registerResult = this.gson.fromJson(respData, RegisterResult.class);
                Log.d(TAG, respData);

            }
            else {
               Log.d(TAG, "ERROR: " + http.getResponseMessage());
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                registerResult = this.gson.fromJson(respData, RegisterResult.class);
                Log.d(TAG, respData);
            }
            return registerResult;
        }
        catch (IOException e) {
            throw new IOException("Error: ServerFacade when registering");
        }
    }
    public PersonAllResult getPersons(String serverHost, String serverPort, String authToken) throws IOException {
        try {
            PersonAllResult personAllResult = null;
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                personAllResult = this.gson.fromJson(respData, PersonAllResult.class);
            }
            else {
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                personAllResult = this.gson.fromJson(resData, PersonAllResult.class);
            }
            return personAllResult;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IOException("In Server Facade Getting MY People");
        }
    }

    public EventAllResult getEvents(String serverHost, String serverPort, String authToken) throws IOException {
        try {
            EventAllResult eventAllResult = null;
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String resData = readString(respBody);
                eventAllResult = this.gson.fromJson(resData, EventAllResult.class);
            }
            else {
                InputStream resBody = http.getErrorStream();
                String respData = readString(resBody);
                eventAllResult = this.gson.fromJson(respData, EventAllResult.class);
            }
            return eventAllResult;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Serer Facade when Getting the events");
        }
    }


    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}


