import DeserializeJSON.DeserializeLocations;
import DeserializeJSON.DeserializeNames;
import com.sun.net.httpserver.HttpServer;
import handler.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;
    private static Logger logger = Logger.getLogger("Server");

    private void run(String portNumber) {
        logger.info("Initializing Server");
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);
        logger.info("Creating Context");
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/person/", new PersonSingleHandler());
        server.createContext("/person", new PersonAllHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/event/", new EventSingleHandler());
        server.createContext("/event", new EventAllHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/", new FileHandler());
        logger.info("Starting Server");

        server.start();

        //loads data from json files and stores it in storage class
        DeserializeLocations deserialize = new DeserializeLocations();
        DeserializeNames deserializeNames = new DeserializeNames();
        try {
            deserialize.deserializeLocation();
            deserializeNames.deserializeNames();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        logger.info("Server Started");
    }

    public static void main(String[] args)  {
        String portNumber = args[0];
        new Server().run(portNumber);
    }

}
