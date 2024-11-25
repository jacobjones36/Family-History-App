package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * creates the test server website
 */
public class FileHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath.isEmpty() || urlPath.equals("/")) {
                    urlPath = "index.html";
                }
                urlPath = "web/" + urlPath;
                File file = new File(urlPath);
                if (file.exists() ) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream resBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), resBody);
                    resBody.close();
                }
                else {
                    //send 404
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream resBody = exchange.getResponseBody();
                    file = new File("web/HTML/404.html");
                    Files.copy(file.toPath(), resBody);
                    resBody.close();
                }
            }
            else {
                //send a 405
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e){
            System.out.print("IOException");
            e.printStackTrace();
        }
    }
}
