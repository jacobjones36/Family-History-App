package Service;

import DAO.AuthDao;
import DAO.DataAccessException;
import DataBase.Database;
import DAO.UserDao;
import Model.User;
import RequestResult.LoginRequest;
import RequestResult.LoginResult;


/**
 * performs the login function of the server
 */
public class LoginService {

    /**
     * takes in the username and password and returns an authtoken if true
     * @param loginRequest
     * @return the login result object
     */
    public LoginResult login(LoginRequest loginRequest) throws Exception {
        Database db = new Database();
        try {
            db.openConnection();
            //validate the user is who they say they are
            User user = new UserDao(db.getConnection()).find(loginRequest.getUsername());
            if(user.getUsername() == null){
                throw new DataAccessException("Error: No user found");
            }
            if (user.getPassword() != null && user.getPassword().equals(loginRequest.getPassword())) {
                //create an authtoken to return to the client
                new AuthDao(db.getConnection()).clear();
                String authtoken = new AuthDao(db.getConnection()).createAuthToken(user.getUsername());
                LoginResult loginResult = new LoginResult(authtoken, user.getUsername(), user.getPersonID(), true);
                db.closeConnection(true);
                return loginResult;
            }
            else {
                db.closeConnection(false);
                throw new Exception("Error: Incorrect Password");
            }
        }
        catch (NullPointerException n){
            n.printStackTrace();
            db.closeConnection(false);
            throw new NullPointerException("Error: No user found");
        }
        catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            throw new Exception("Error: logging in");
        }
    }
}
