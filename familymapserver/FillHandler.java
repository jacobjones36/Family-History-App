package handler;

import RequestResult.FailedResult;
import RequestResult.FillRequest;
import RequestResult.FillResult;
import Service.FillService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                String urlPath = exchange.getRequestURI().toString();
                urlPath = urlPath.substring(6);
                int numGenerations = 0;
                String username = urlPath;
                if (urlPath.contains("/")) {
                    int i = urlPath.indexOf("/");
                    String generations = urlPath.substring(i + 1);
                    numGenerations = Integer.parseInt(generations);
                    username = urlPath.substring(0, i);
                }
                FillRequest fillRequest = new FillRequest(username, numGenerations);
                FillResult fillResult = new FillService().fill(fillRequest);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(fillResult, resBody);
                resBody.close();
                success = true;
            }
            if (success != true) {
                throw new FileNotFoundException("Error: Invalid Request");
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            FailedResult failedResult = new FailedResult(e.toString(), false);
            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(failedResult, resBody);
            resBody.close();
        }
    }
}
