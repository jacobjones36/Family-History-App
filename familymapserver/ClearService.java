package Service;

import DAO.*;
import DataBase.Database;
import RequestResult.ClearResult;

/**
 * clears database
 */
public class ClearService {

    /**
     * deletes all the data from the database
     * @return whether it was cleared successfully or not
     */
    public ClearResult clear() throws Exception {
        Database db = new Database();
        try{
            db.openConnection();
            new UserDao(db.getConnection()).clear();
            new PersonDao(db.getConnection()).clear();
            new EventDao(db.getConnection()).clear();
            new AuthDao(db.getConnection()).clear();
            db.closeConnection(true);

            ClearResult clearResult = new ClearResult("Clear succeeded.", true);
            return clearResult;
        }
        catch (DataAccessException e){
            db.closeConnection(false);
            throw new Exception();
        }
        catch (Exception e) {
            db.closeConnection(false);
            throw new Exception("Error thrown when Clearing");
        }

    }
}
