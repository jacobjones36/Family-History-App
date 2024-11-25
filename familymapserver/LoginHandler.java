package handler;

import RequestResult.FailedResult;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;
import Service.LoginService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler implements HttpHandler {
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                StringBuilder sb = new StringBuilder();
                InputStream ios = exchange.getRequestBody();
                int i;
                while ((i = ios.read()) != -1) {
                    sb.append((char) i);
                }
                LoginRequest loginRequest = gson.fromJson(sb.toString(), LoginRequest.class);
                LoginResult loginResult = new LoginService().login(loginRequest);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(loginResult, resBody);
                resBody.close();
                success = true;
                CurrentAuthtoken authtoken = new CurrentAuthtoken();
                authtoken.setAuthtoken(loginResult.getAuthtoken());
            }
            if (!success) {
                throw new Exception("Error: Invalid Request");
            }
        }
        catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            FailedResult failedResult = new FailedResult("Error thrown when logging in", false);
            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(failedResult, resBody);
            resBody.close();
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
        catch (Exception e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            FailedResult failedResult = new FailedResult(e.toString(), false);
            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(failedResult, resBody);
            resBody.close();
            e.printStackTrace();
        }
    }
}
