package DeserializeJSON;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Vector;

/**
 * Deserializes the location JSON file and sends it to the storage class
 */
public class DeserializeLocations {
    class LocationData {
        public Location[] data;
    }
    class Location {
        public String latitude;
        public String longitude;
        public String city;
        public String country;
    }
    public void deserializeLocation() throws FileNotFoundException {
        Gson gson = new Gson();
        Reader reader = new FileReader("json/locations.json");
        LocationData locData = gson.fromJson(reader, LocationData.class);
        Vector<String> latitudes = new Vector<>();
        Vector<String> longitudes = new Vector<>();
        Vector<String> city = new Vector<>();
        Vector<String> country = new Vector<>();
        for(Location location : locData.data){
            latitudes.add(location.latitude);
            longitudes.add(location.longitude);
            city.add(location.city);
            country.add(location.country);
        }
        Storage storage = new Storage();
        storage.StoringLocations(latitudes, longitudes, city, country);
    }
}
