package DAO;
import Model.Event;
import Model.Person;

import java.sql.Connection;

import java.util.HashSet;
import java.sql.*;

/**
 * interacts with the database for events
 */
public class EventDao {
    private final Connection conn;
    public EventDao (Connection conn){
        this.conn = conn;
    }

    /**
     * inserts a row into the event table in the database
     * @param event
     */

    public void insert(Event event) throws DataAccessException{
        String sql = "INSERT INTO event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * goes to the database to find the event with that eventID
     * @param eventID
     * @return Event associated with the eventID
     */

    public Event find(String eventID) throws DataAccessException{
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM event WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            } else {
                throw new SQLException();
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * deletes all the events connected to the user
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM event";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

    /**
     * deletes all events connected to the given user
     * @param username
     * @throws Exception
     */
    public void deleteFromUser(String username) throws Exception {
        findUsingAssociatedUsername(username);
        String sql = "DELETE FROM event WHERE associatedUsername = ?";
        HashSet<Person> data = new HashSet<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error when deleting from user");
        }

    }

    /**
     * verifies there are events connected to the username
     * @param associatedUsername
     * @return
     * @throws Exception
     */
    public Event findUsingAssociatedUsername(String associatedUsername) throws Exception {
        String sql = "SELECT * FROM event WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, associatedUsername);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()){
                return null;
            }
            else{
                Event event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new Exception("Error: Invalid User");
        }
    }

    /**
     * Grabs all event objects from database and return them as a list
     * @param username
     * @return HashSet<Event> listOfEvents
     */
    public HashSet<Event> findEventsForUser(String username) throws DataAccessException {
        String sql = "SELECT * FROM event WHERE associatedUsername = ?";
        ResultSet rs;
        HashSet<Event> data = new HashSet<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat(4),
                        rs.getFloat(5), rs.getString("country"),
                        rs.getString("city"), rs.getString("eventType"), rs.getInt(9)));
            }
            if (data.isEmpty()) {
                throw new Exception();
            }
            return data;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while returning list of events");
        } catch (Exception e) {
            throw new DataAccessException("No Events found");
        }
    }

}
