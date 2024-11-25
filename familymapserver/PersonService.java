package Service;

import DAO.*;
import DataBase.Database;
import Model.AuthToken;
import Model.Person;
import RequestResult.PersonAllResult;
import RequestResult.PersonSingleResult;

import java.util.HashSet;
import java.util.Iterator;

/**
 * obtains information about given person
 */
public class PersonService {
    /**
     * returns the information about the person requested
     * @param personID
     * @return persons single result
     */
    Database db =  new Database();
    public PersonSingleResult personSingle(String personID, String authToken) throws Exception {
        try{
            db.openConnection();
            AuthToken authToken1 = new AuthDao(db.getConnection()).find(authToken);
            Person person = new PersonDao(db.getConnection()).find(personID);
            if(person != null && person.getAssociatedUsername().equals(authToken1.getUsername())){
                PersonSingleResult personSingleResult = new PersonSingleResult(person.getAssociatedUsername(),
                        person.getPersonID(), person.getFirstName(), person.getLastName(), person.getGender(),
                        person.getFatherID(), person.getMotherID(), person.getSpouseID(), true);
                db.closeConnection(true);
                return personSingleResult;
            }
            else{
                db.closeConnection(false);
                throw new DataAccessException("Error: No Person was found");
            }
        }
        catch (DataAccessException ex){
            ex.printStackTrace();
            throw new Exception(ex.toString());
        }
        catch (NullPointerException e) {
            db.closeConnection(false);
            throw new Exception("Error: Null Pointer Exception");
        }
    }

    /**
     * validates the current authtoken and then returns all the persons connected with the user
     * @param authtoken
     * @return personAllResult
     * @throws Exception
     */

    public PersonAllResult personsAll(String authtoken) throws Exception {
        try{
            db.openConnection();
            AuthToken authToken = new AuthDao(db.getConnection()).find(authtoken);
            String associatedUsername = authToken.getUsername();
            HashSet<Person> hashData = new PersonDao(db.getConnection()).findPersonsForUser(associatedUsername);
            if(!hashData.isEmpty()) {
                Person[] data = new Person[hashData.size()];
                Iterator<Person> itr = hashData.iterator();
                int i = 0;
                while (itr.hasNext()){
                    data[i] = itr.next();
                    ++i;
                }
                PersonAllResult personAllResult = new PersonAllResult(data, true);
                db.closeConnection(true);
                return personAllResult;
            }
            else if (hashData.isEmpty()){
                db.closeConnection(false);
                throw new Exception("Error: User has no Persons in Database");
            }
            db.closeConnection(false);
            throw new Exception("Error:Who knows");
        }
        catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            throw new Exception("Error: " + ex.toString());
        }
        catch (NullPointerException e){
            db.closeConnection(false);
            throw new Exception("Error: Null Pointer Exception");
        }
        catch (Exception e) {
            db.closeConnection(false);
            throw new Exception(e.toString());
        }
    }
}
