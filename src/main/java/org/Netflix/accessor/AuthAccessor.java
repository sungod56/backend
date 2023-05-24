package org.Netflix.accessor;

import com.mysql.cj.Query;
import com.mysql.cj.xdevapi.PreparableStatement;
import org.Netflix.Exeptions.DependancyFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;



@Repository
public class AuthAccessor {
    @Autowired
    DataSource dataSource;

    public void storeToken(final String userID, final String token){
        String insertQuery = "INSERT INTO auth(authID, token, userID) values(?,?,?)";
        String uuid = UUID.randomUUID().toString();

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);
            pstmt.setString(1,uuid);
            pstmt.setString(2, token);
            pstmt.setString(3, userID);
            pstmt.executeUpdate();


        }
        catch(SQLException ex){
            ex.printStackTrace();
            throw new DependancyFailureException(ex);
        }
    }

    public AuthDTO getAuthByToken(final String token){
        String query ="SELECT authID, token, userID from auth where token = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,token);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                AuthDTO authDTO =AuthDTO.builder()
                        .authID(resultSet.getString(1))
                        .token(resultSet.getNString(2))
                        .UserID(resultSet.getString(3))
                        .build();
                return authDTO;
            }
            return null;

            }
        catch(SQLException ex){
            ex.printStackTrace();
            throw new DependancyFailureException(ex);
        }
    }


}
