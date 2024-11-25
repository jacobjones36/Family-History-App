package handler;

import RequestResult.FailedResult;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try{
            if(exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                RegisterRequest registerRequest = gson.fromJson(reqData, RegisterRequest.class);
                RegisterResult registerResult = new RegisterService().register(registerRequest);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(registerResult, resBody);
                resBody.close();
                success = true;
                CurrentAuthtoken authtoken = new CurrentAuthtoken();
                authtoken.setAuthtoken(registerResult.getAuthtoken());
            }
            if(!success){
                throw new Exception("Error: Invalid request");
            }
        }
        catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            FailedResult failedResult = new FailedResult("Failed to Register", false);
            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(failedResult, resBody);
            resBody.close();
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

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}
