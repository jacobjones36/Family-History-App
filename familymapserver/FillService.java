package Service;

import DAO.*;
import DataBase.Database;
import Model.Person;
import Model.User;
import RequestResult.FillRequest;
import RequestResult.FillResult;

import java.io.FileNotFoundException;

/**
 * fills users family tree
 */
public class FillService {
    String userName;

    /**
     * fills the database with the given number of generations(default 4 generations)
     * @param fillRequest
     * @return fillResult
     */
    public FillResult fill(FillRequest fillRequest) throws FileNotFoundException {
        userName = fillRequest.getUsername();
        Database db = new Database();
        try{
            db.openConnection();
            //delete users connected events and persons
            new EventDao(db.getConnection()).deleteFromUser(fillRequest.getUsername());
            new PersonDao(db.getConnection()).deleteFromUser(fillRequest.getUsername());
            User user = new UserDao(db.getConnection()).find(userName);
            int numGens = getNumGenerations(fillRequest);
            //create family tree and update users person then return result
            GenerateTrees generateTrees = new GenerateTrees(userName, numGens);
            Person person = generateTrees.generatePerson(user.getGender(), numGens, db.getConnection());
            new UserDao(db.getConnection()).updatePersonId(person.getPersonID(), userName);
            new PersonDao(db.getConnection()).updateUserPersonName(person.getFirstName(), person.getLastName(), person.getPersonID());
            generateTrees.generateEvent("Birth", person.getPersonID(), db.getConnection(), numGens);
            FillResult fillResult = new FillResult("Successfully added " + generateTrees.getPersonCounter() +
                    " persons and " + generateTrees.getEventCounter() + " events to the database.", true);
            db.closeConnection(true);
            return fillResult;
        }
        catch (DataAccessException e){
            e.printStackTrace();
            db.closeConnection(false);
            throw new FileNotFoundException("Error: Data Access Exception");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new FileNotFoundException(e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new FileNotFoundException("Error: Invalid User");
        }
    }

    //if request specifies number of generations returns that number else returns the assumed number of 4
    private int getNumGenerations(FillRequest fillRequest){
        int numGenerations;
        if(fillRequest.getGenerations() == 0){
            numGenerations = 4;
        }
        else {
            numGenerations = fillRequest.getGenerations();
        }
        return numGenerations;
    }
}
