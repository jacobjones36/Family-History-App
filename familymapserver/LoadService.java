package Service;

import DAO.*;
import DataBase.Database;
import Model.Event;
import Model.Person;
import Model.User;
import RequestResult.LoadRequest;
import RequestResult.LoadResult;

/**
 * loads information into database
 */
public class LoadService {
    /**
     * clears the data from the database and loads the data from the request into database
     * @return load result object
     */
    public LoadResult load(LoadRequest loadRequest){
        Database db = null;
        try{
            new ClearService().clear();
            db = new Database();
            db.openConnection();
            int userCounter = 0;
            int personCounter = 0;
            int eventCounter = 0;
            for(User user : loadRequest.getUsers()){
                new UserDao(db.getConnection()).insert(user);
                ++userCounter;
            }
            for(Person person : loadRequest.getPersons()){
                new PersonDao(db.getConnection()).insert(person);
                ++personCounter;
            }
            for (Event event : loadRequest.getEvents()){
                new EventDao(db.getConnection()).insert(event);
                ++eventCounter;
            }
            LoadResult loadResult = new LoadResult("Successfully added " + userCounter +
                    " users, " + personCounter + " persons, and " + eventCounter + " events to the database.",
                    true);
            db.closeConnection(true);
            return loadResult;
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw new RuntimeException("Error: loading data into database");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error: Invalid Request. Missing Data");
        }
    }
}
