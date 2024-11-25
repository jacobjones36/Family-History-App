package Service;

import DAO.AuthDao;
import DAO.DataAccessException;
import DataBase.Database;
import DAO.EventDao;
import Model.AuthToken;
import Model.Event;
import RequestResult.EventAllResult;
import RequestResult.EventSingleResult;

import java.util.HashSet;
import java.util.Iterator;

/**
 * returns one event
 */
public class EventService {
    Database db = new Database();

    /**
     * returns the details about the object connected with the given eventID
     * @param eventId
     * @param authToken
     * @return the event single result
     */
    public EventSingleResult eventSingle(String eventId, String authToken) throws Exception {
        boolean success = false;
        EventSingleResult eventSingleResult = null;
        try {
            db.openConnection();
            AuthToken authToken1 =  new AuthDao(db.getConnection()).find(authToken);
            Event event = new EventDao(db.getConnection()).find(eventId);
            if(event != null && event.getAssociatedUsername().equals(authToken1.getUsername())){
                success = true;
                eventSingleResult = new EventSingleResult(event.getAssociatedUsername(), eventId,
                        event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(),
                        event.getCity(), event.getEventType(), event.getYear(), true);
                db.closeConnection(true);
            }
            if(!success){
                throw new DataAccessException("Error: Invalid AuthToken");
            }
            return eventSingleResult;
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new Exception(e.toString());
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new Exception("Error: Null Pointer Exception");
        }
    }

    /**
     * takes in the current authtoken and returns all the events connected to the user
     * @param authtoken
     * @return eventAllResult
     * @throws Exception
     */
    public EventAllResult eventAll(String authtoken) throws Exception {
        try{
            db.openConnection();
            AuthToken authToken = new AuthDao(db.getConnection()).find(authtoken);
            String associatedUsername = authToken.getUsername();
            HashSet<Event> hashData = new EventDao(db.getConnection()).findEventsForUser(associatedUsername);
            if(hashData.size() != 0){
                Event[] data = new Event[hashData.size()];
                Iterator<Event> itr = hashData.iterator();
                int i = 0;
                while (itr.hasNext()){
                    data[i] = itr.next();;
                    ++i;
                }
                EventAllResult eventAllResult = new EventAllResult(data, true);
                db.closeConnection(true);
                return eventAllResult;
            }
            else{
                db.closeConnection(false);
                throw new Exception("No events associated with the User");
            }
        }
        catch (DataAccessException e){
            e.printStackTrace();
            db.closeConnection(false);
            throw new Exception("Error: " + e.toString());
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw new Exception("Error: Null Pointer Exception");
        }
    }
}
