package Service;

import DAO.DataAccessException;
import DAO.EventDao;
import DAO.PersonDao;
import DeserializeJSON.Storage;
import Model.Event;
import Model.Person;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class GenerateTrees {
    private String userName;
    private int totalGens;
    private int eventCounter = 0;
    private int personCounter = 0;

    /**
     * constructor used to specify username and the total generations being processed is
     * @param userName
     * @param totalGens
     */
    public GenerateTrees(String userName, int totalGens) {
        this.userName = userName;
        this.totalGens = totalGens;
    }

    /**
     * recursively calls generate person to create family tree
     * @param gender
     * @param generations
     * @param conn
     * @return person
     * @throws FileNotFoundException
     * @throws DataAccessException
     */
    public Person generatePerson(String gender, int generations, Connection conn) throws FileNotFoundException, DataAccessException {
        Person mother = null;
        Person father = null;
        // if they aren't the last generation
        if(generations >= 1) {
            //creates mother and father, updates spouseId's, and generates marriage event
            mother = generatePerson("f", (generations - 1), conn);
            father = generatePerson("m", (generations - 1), conn);
            new PersonDao(conn).updateSpouseId(mother.getPersonID(), father.getPersonID());
            new PersonDao(conn).updateSpouseId(father.getPersonID(), mother.getPersonID());
            generateMarriage(mother.getPersonID(), father.getPersonID(), conn, generations);
        }
        //creates the person info and adds to database
        String fatherID = null;
        if(father != null){
            fatherID = father.getPersonID();
        }
        String motherID = null;
        if(mother != null){
            motherID = mother.getPersonID();
        }
        String personID = UUID.randomUUID().toString();
        Random rand = new Random();
        int nameIndex = rand.nextInt(144);
        Person person = new Person(personID, userName, getName(gender, nameIndex), getName("s", nameIndex),
                gender, fatherID, motherID, null);
        new PersonDao(conn).insert(person);
        ++personCounter;
        if((totalGens - generations) != 0) {
            generateEvent("Birth", personID, conn, generations);
            generateEvent("Death", personID, conn, generations);
        }
        return person;
    }

    /**
     * creates event and adds to database
     * @param eventType
     * @param personID
     * @param conn
     * @param generation
     * @throws FileNotFoundException
     * @throws DataAccessException
     */
    public void generateEvent(String eventType, String personID, Connection conn, int generation) throws FileNotFoundException, DataAccessException {
        Random rand = new Random();
        int index = rand.nextInt(977);
        String [] location = getLocation(index);
        float latitude = Float.parseFloat(location[0]);
        float longitude = Float.parseFloat(location[1]);
        String eventID = UUID.randomUUID().toString();
        int year = getYear(totalGens - generation, eventType);
        if(year > 2023){
            year = 2023;
        }
        if(year > 0) {
            Event event = new Event(eventID, userName, personID, latitude, longitude, location[3], location[2],
                    eventType, year);
            new EventDao(conn).insert(event);
            ++eventCounter;
        }

    }

    /**
     * generates marriage for couples to have same information but different ids
     * @param motherID
     * @param fatherID
     * @param conn
     * @param generations
     * @throws FileNotFoundException
     * @throws DataAccessException
     */
    private void generateMarriage(String motherID, String fatherID, Connection conn, int generations) throws DataAccessException {
        String eventType = "Marriage";
        Random rand = new Random();
        int index = rand.nextInt(978);
        String [] location = getLocation(index);
        float latitude = Float.parseFloat(location[0]);
        float longitude = Float.parseFloat(location[1]);
        String eventID = UUID.randomUUID().toString();
        int year = getYear(totalGens - generations + 1, eventType);
        Event event = new Event(eventID, userName, motherID, latitude, longitude, location[3], location[2],
                eventType , year);
        new EventDao(conn).insert(event);
        ++eventCounter;
        eventID = UUID.randomUUID().toString();
        event = new Event(eventID, userName, fatherID, latitude, longitude, location[3], location[2],
                eventType , year);
        new EventDao(conn).insert(event);
        ++eventCounter;
    }

    /**
     * gets location based on random given index
     * @param index
     * @return
     * @throws FileNotFoundException
     */
    public String[] getLocation(int index) {
        Storage storage = new Storage();
        String[] location = new String[4];
        location[0] = storage.getLatitudes(index);
        location[1] = storage.getLongitudes(index);
        location[2] = storage.getCity(index);
        location[3] = storage.getCountry(index);
        return location;
    }

    /**
     * gets a name based on a random given index
     * @param gender
     * @param index
     * @return
     * @throws FileNotFoundException
     */
    public String getName(String gender, int index) {
        String name;
        Storage storage = new Storage();
        switch (gender){
            case "f": name = storage.getfNames()[index];
                break;
            case "m": name = storage.getmNames()[index];
                break;
            case "s": name = storage.getsNames()[index];
                break;
            default:  name = "UrMom";
                break;
        }
        return name;

    }

    /**
     * generates a year randomly within the realistic given parameters
     * @param generation
     * @param eventType
     * @return
     * @throws DataAccessException
     */
    private int getYear(int generation, String eventType) throws DataAccessException {
        int year;
        switch (generation){
            case 0: year = 0;
            break;
            case 1: year = 1;
                break;
            case 2: year = 2;
                break;
            case 3: year = 3;
                break;
            case 4: year = 4;
                break;
            case 5: year = 5;
                break;
            default: year = 1000;
            break;
        }
        return randomizeYear(year, eventType);
    }
    private int randomizeYear(int year, String eventType){
        Random rand = new Random();
        year = 2000 - (year * 25);
        int mixer = rand.nextInt(12);
        switch (eventType){
            case "Birth": year = year + mixer;
                break;
            case "Marriage": year = year + 25 + mixer;
                break;
            case "Death": year = year + 80  + mixer;
                break;
            default: year = year + ((mixer + 10) * 2);
        }
        return year;
    }

    /**
     * returns number of events added
     * @return
     */

    public int getEventCounter() {
        return eventCounter;
    }

    /**
     * returns number of persons added
     * @return
     */
    public int getPersonCounter() {
        return personCounter;
    }
}
