package org.Netflix.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.Netflix.Exeptions.InvalidCredentialsException;
import org.Netflix.Security.SecurityConstants;
import org.Netflix.accessor.UserAccessor;
import org.Netflix.accessor.AuthAccessor;
import org.Netflix.accessor.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthService {
    @Autowired
    private UserAccessor userAccessor;
    @Autowired
    private AuthAccessor authAccessor;

    public String login(final String email, final String password){
        UserDTO userDTO = userAccessor.getUserByEmail(email);

           if (userDTO != null && userDTO.getPassword().equals(password)) {
               String token = Jwts.builder()
                       .setSubject(email)
                       .setAudience(userDTO.getRole().name())
                       .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                       .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY)
                       .compact();
               authAccessor.storeToken(userDTO.getUserID(), token);
               return token;
           }
           throw new InvalidCredentialsException("Either the password or email is incorrect!!");

       


        }
    }

