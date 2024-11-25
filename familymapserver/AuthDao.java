package DAO;
import Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * interacts with the authtoken table in the database
 */
public class AuthDao {
    private final Connection conn;
    public AuthDao(Connection conn){
        this.conn = conn;
    }

    /**
     * creates a row in the authtoken table in the database
     *
     * @param username
     */
    public String createAuthToken(String username) throws DataAccessException {
        String token =  UUID.randomUUID().toString();
        String sql = "INSERT INTO authtoken (authtoken, username) VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, token);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error: sql exception with create auth");
        }
        return token;
    }

    /**
     * finds the username connected with the given authtoken
     * @param authToken
     * @return Authtoken object if found in database
     */
    public AuthToken find(String authToken)throws DataAccessException{
        String sql = "SELECT * FROM authtoken WHERE authtoken = ?";
        ResultSet rs;
        AuthToken authtoken;
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if(rs.next()){
                authtoken = new AuthToken(rs.getString("authtoken"), rs.getString("username"));
                return authtoken;
            }
            else{throw new SQLException();}
        }catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authtoken");
        }
    }

    /**
     * clears the authtoken table
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException{
        String sql = "DELETE FROM authtoken";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the authtoken table");
        }
    }
}
