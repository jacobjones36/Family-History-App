package DeserializeJSON;

import java.util.Vector;

/**
 * stores the Deserialized JSON files
 */
public class Storage {
    private static Vector<String> latitudes;
    private static Vector<String> longitudes;
    private static Vector<String> city;
    private static Vector<String> country;
    private static String[] fNames;
    private static String[] mNames;
    private static String[] sNames;

    public void StoringLocations(Vector<String> latitudes, Vector<String> longitudes, Vector<String> city,
                                 Vector<String> country) {
        this.latitudes = latitudes;
        this.longitudes = longitudes;
        this.city = city;
        this.country =country;
    }
    public void mNamesStore(String[] mNames){
        this.mNames = mNames;
    }
    public void fNamesStore(String[] fNames){
        this.fNames = fNames;
    }
    public void sNamesStore(String[] sNames){
        this.sNames = sNames;
    }

    public static String getLatitudes(int index) {
        return latitudes.elementAt(index);
    }

    public static void setLatitudes(Vector<String> latitudes) {
        Storage.latitudes = latitudes;
    }

    public static String getLongitudes(int i) {
        return longitudes.elementAt(i);
    }

    public static void setLongitudes(Vector<String> longitudes) {
        Storage.longitudes = longitudes;
    }

    public static String getCity(int i) {
        return city.elementAt(i);
    }

    public static void setCity(Vector<String> city) {
        Storage.city = city;
    }

    public static String getCountry(int i) {
        return country.elementAt(i);
    }

    public static void setCountry(Vector<String> country) {
        Storage.country = country;
    }

    public static String[] getfNames() {
        return fNames;
    }

    public static void setfNames(String[] fNames) {
        Storage.fNames = fNames;
    }

    public static String[] getmNames() {
        return mNames;
    }

    public static void setmNames(String[] mNames) {
        Storage.mNames = mNames;
    }

    public static String[] getsNames() {
        return sNames;
    }

    public static void setsNames(String[] sNames) {
        Storage.sNames = sNames;
    }
    public int getSize(){
        return longitudes.size();
    }
}
