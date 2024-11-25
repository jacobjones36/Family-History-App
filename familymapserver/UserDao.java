package DAO;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

/**
 * interacts with the database for users
 */
public class UserDao {
    private final Connection conn;
    public UserDao(Connection conn){
        this.conn = conn;
    }

    /**
     * create user contains all the code (SQL) to insert a new row into the user table
     * @param user
     */
    public void insert(User user) throws DataAccessException{
        String sql = "INSERT INTO user (username, password, email, firstName, lastName, gender, personID)" +
        " VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
    }

    /** query the database and find the user and read the data
     *
     * @param
     * @return
     */
    public User find(String username)throws  DataAccessException{
        User user;
        ResultSet rs;
        String sql = "SELECT * From user WHERE username = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next()){
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("personID"));
                return user;
            }
            else{return null;}
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an user in the database");
        }
        catch (NullPointerException e){
            throw new DataAccessException("Error: User doesn't exist");
        }

    }

    /**
     * deletes all user
     */
    public void clear()throws DataAccessException{
        String sql = "DELETE FROM user";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the user table");
        }
    }

    /**
     * updates the users personID
     * @param personId
     * @param username
     * @throws Exception
     */
    public void updatePersonId(String personId, String username) throws Exception {
        String sql = "UPDATE user SET personId = ? WHERE username = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, personId);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
        catch (SQLException | NullPointerException e){
            throw new Exception("Error: Updating personID");
        }
    }
}
