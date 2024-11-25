package handler;

import RequestResult.FailedResult;
import RequestResult.LoadRequest;
import RequestResult.LoadResult;
import Service.LoadService;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            if(exchange.getRequestMethod().toLowerCase().equals("post")){
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                LoadRequest loadRequest = gson.fromJson(reqData, LoadRequest.class);
                LoadResult loadResult = new LoadService().load(loadRequest);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(loadResult, resBody);
                resBody.close();
            }
        }
        catch (JsonSyntaxException e) {
            throw new RuntimeException("Error: Json Syntax Error");
        }
        catch (IOException e) {
            throw new RuntimeException("Error: IO Exception");
        }
        catch (JsonIOException e) {
            throw new RuntimeException("Error: JSON Io Exception");
        }
        catch (RuntimeException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            FailedResult failedResult = new FailedResult(e.toString(), false);
            Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
            gson.toJson(failedResult, resBody);
            resBody.close();
            e.printStackTrace();
        }

    }
    private String readString(InputStream is)throws IOException{
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while((len = sr.read(buf)) > 0){
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
