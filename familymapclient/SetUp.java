package com.jacob.family.map.project.anotherone.Helpers;

import com.jacob.family.map.project.anotherone.Helpers.SetDataCache;
import com.jacob.family.map.project.anotherone.ServerFacade;

import java.io.IOException;

import RequestResult.LoginRequest;
import RequestResult.LoginResult;


//sets up server facade, logs in user, sets up data cache and returns persons Id
public class SetUp {
    private LoginResult loginResult;
    private String personId;


    public String setUpTests() throws IOException {
        String serverHost = "localhost";
        String serverPort = "8080";
        ServerFacade serverFacade = new ServerFacade();
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        loginResult = serverFacade.login(serverHost, serverPort, loginRequest);
        SetDataCache setDataCache = new SetDataCache();
        String[] data = {serverHost, serverPort, loginResult.getAuthtoken(), loginResult.getPersonID()};
        setDataCache.setDataCache(data);
        personId = loginResult.getPersonID();
        return personId;
    }
}
