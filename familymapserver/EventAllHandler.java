package handler;

import RequestResult.EventAllResult;
import RequestResult.FailedResult;
import Service.EventService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class EventAllHandler implements HttpHandler {
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                VerifyAuth verifyAuth = new VerifyAuth();
                if (verifyAuth.verify(exchange)) {
                    CurrentAuthtoken authtoken = new CurrentAuthtoken();
                    EventAllResult eventAllResult = new EventService().eventAll(authtoken.getAuthtoken());
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(eventAllResult, resBody);
                    resBody.close();
                    success = true;
                }
                else {
                    throw new Exception("Error: Invalid AuthToken");
                }
            }
            if (!success) {
                throw new Exception("Error: Invalid Request");
            }
        }catch (IOException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            FailedResult failedResult = new FailedResult("Server throws IO Exception", false);
            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(failedResult, resBody);
            resBody.close();
        }catch (Exception e){
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            FailedResult failedResult = new FailedResult(e.toString(), false);
            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(failedResult, resBody);
            resBody.close();
        }

    }
}
