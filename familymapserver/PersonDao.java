package DAO;
import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * interacts with the database for the persons object
 */
public class PersonDao {
    Connection conn;
    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO person (personID, associatedUsername, firstName, lastName," +
                "gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
    }

    /**
     * finds a person in the database with the given personID
     *
     * @param personID
     * @return the person object from database if exists
     */
    public Person find(String personID) throws DataAccessException {
        String sql = "SELECT * FROM person WHERE personID = ?";
        Person person;
        ResultSet rs;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    /**
     * gets all the people from the database connected to the user
     *
     * @param username
     * @return list of people in the users database
     */
    public HashSet<Person> findPersonsForUser(String username) throws DataAccessException {
        String sql = "SELECT * FROM person WHERE associatedUsername = ?";
        ResultSet rs;
        HashSet<Person> data = new HashSet<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            int i = 0;
            while (rs.next()) {
                data.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID")));
                ++i;
            }
            if (data.isEmpty()) {
                throw new Exception();
            }
            return data;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while returning list of people");
        } catch (Exception e) {
            throw new DataAccessException("Error: No Persons found");
        }
    }

    /**
     * deletes all the people connected to the user
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM person";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }

    /**
     * deletes all the persons associated with the given user
     * @param associatedUsername
     * @throws DataAccessException
     */
    public void deleteFromUser(String associatedUsername) throws DataAccessException {
        findUsingAssociatedUsername(associatedUsername);
        String sql = "DELETE FROM person WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            throw new DataAccessException("Error when deleting from user");
        }
    }

    /**
     * verifies that there are users to delete from the given username
     * @param associatedUsername
     * @throws DataAccessException
     */
    public void findUsingAssociatedUsername(String associatedUsername)throws DataAccessException{
        String sql = "SELECT * FROM person WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, associatedUsername);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()){
                return;
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Invalid User");
        }
    }

    /**
     * updates the spouse id of the given person id
     * @param personId
     * @param spouseId
     */
    public void updateSpouseId(String personId, String spouseId) {
        String sql = "UPDATE person SET spouseID = ? WHERE personID = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, spouseId);
            stmt.setString(2, personId);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * updates the giver person id with new first and last name
     * @param personId
     * @param firstName
     * @param lastName
     * @throws Exception
     */
    public void updateUserPersonName(String personId, String firstName, String lastName) throws Exception {
        String sql = "UPDATE person SET firstName = ?, lastName = ? WHERE personId = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, personId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Error thrown when updating Users persons name");
        }
    }
}