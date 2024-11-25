package DeserializeJSON;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 * Deserializes the JSON name files and sends them to the storage class
 */
public class DeserializeNames {
    class MNameData {
        String[] data;
    }
    class FNamesData {
        String[] data;
    }
    class SNamesData {
        String[] data;
    }
    public void deserializeNames()throws FileNotFoundException {
        Gson gson = new Gson();
        Storage storage = new Storage();
        Reader reader = new FileReader("json/mnames.json");
        MNameData mNameData = gson.fromJson(reader, MNameData.class);
        storage.mNamesStore(mNameData.data);
        reader = new FileReader("json/fnames.json");
        FNamesData fNamesData = gson.fromJson(reader, FNamesData.class);
        storage.fNamesStore(fNamesData.data);
        reader = new FileReader("json/snames.json");
        SNamesData sNamesData = gson.fromJson(reader, SNamesData.class);
        storage.sNamesStore(sNamesData.data);
    }
}
