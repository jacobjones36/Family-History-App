package Service;

import DAO.*;
import DataBase.Database;
import Model.Person;
import Model.User;
import RequestResult.RegisterRequest;
import RequestResult.RegisterResult;

import java.util.UUID;

/**
 * this does the actual registering process
 */
public class RegisterService {
    /**
     * creates new user in database, generates a family tree for the user,
     * and creates its authtoken
     * @return the register result object
     */
    public RegisterResult register(RegisterRequest r) throws Exception {
        RegisterResult registerResult;
        Database db = new Database();
        try{
            String personID = UUID.randomUUID().toString();
            db.openConnection();
            //creates user for given request then validates its real
            User user = new User(r.getUsername(), r.getPassword(), r.getEmail(),
                    r.getFirstName(), r.getLastName(), r.getGender(), personID);
            if(user.getUsername() != null) {
                //inserts user to db, generates users tree, updates person the user is connected with
                new UserDao(db.getConnection()).insert(user);
                GenerateTrees generateTrees = new GenerateTrees(user.getUsername(), 4);
                Person person = generateTrees.generatePerson(user.getGender(), 4, db.getConnection());
                new UserDao(db.getConnection()).updatePersonId(person.getPersonID(), r.getUsername());
                new PersonDao(db.getConnection()).updateUserPersonName(person.getPersonID(), r.getFirstName(), r.getLastName());
                generateTrees.generateEvent("Birth", person.getPersonID(), db.getConnection(), 4);
                String authtoken = new AuthDao(db.getConnection()).createAuthToken(r.getUsername());
                registerResult = new RegisterResult(authtoken, r.getUsername(), person.getPersonID(), true);
                db.closeConnection(true);
                return registerResult;
            }
            else{
                db.closeConnection(false);
                throw new Exception("Error: Username already taken.");
            }
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new Exception("Error: Username already taken");
        }
        catch (Exception ex){
            ex.printStackTrace();
            db.closeConnection(false);
            throw new Exception(ex.toString());
        }
    }
}
