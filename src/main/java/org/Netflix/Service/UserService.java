package org.Netflix.Service;


import org.Netflix.Exeptions.InvalidDataException;
import org.Netflix.accessor.UserAccessor;
import org.Netflix.accessor.UserDTO;
import org.Netflix.accessor.UserRole;
import org.Netflix.accessor.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserService {

    @Autowired
    private UserAccessor userAccessor;

    public void addNewUser(final String email,final String name,final String password, final String phoneNo){

        if(phoneNo.length()!= 10){
            throw new InvalidDataException("Phone No "+ phoneNo +" is invalid.");
        }
        if(password.length() < 6){
            throw new InvalidDataException("Password is too easy!");
        }
        if (name.length()< 5){
            throw new InvalidDataException("Name is not correct");
        }
        String emailRegex = "^[a-zA-Z0-9 +&*-]+(?:\\."+
                "[a-zA-Z0-9 +&*-]+)*@" +
                "(?:[a-zA-z0-9-]+\\.)+[a-z" +
                "A-z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if(!pat.matcher(email).matches()){
            throw new InvalidDataException("email is not correct!");
        }
        UserDTO userDTO = userAccessor.getUserByEmail(email);
        if(userDTO != null){
            throw new InvalidDataException("User with given email already exists!");
        }
         userDTO = userAccessor.getUserByphoneNo(phoneNo);
        if(userDTO !=null){
            throw new InvalidDataException("User with given phoneNO already exists!");
        }
        userAccessor.addNewUser(email,name,password,phoneNo, UserState.ACTIVE, UserRole.ROLE_USER);
    }
}
