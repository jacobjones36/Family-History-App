package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * verifies the given auth to make sure it is still valid
 */
public class VerifyAuth {
    public boolean verify(HttpExchange exchange) throws Exception {
        boolean success = false;
        Headers reqHeader = exchange.getRequestHeaders();
        if(reqHeader.containsKey("Authorization")){
            String authtoken = reqHeader.getFirst("Authorization");
            CurrentAuthtoken currAuth = new CurrentAuthtoken();
            if(currAuth.getAuthtoken().equals(authtoken)){
                success = true;
            }
        }
        return success;
    }
}
