package org.Netflix.Exeptions.controller;

import org.Netflix.Exeptions.InvalidDataException;
import org.Netflix.Service.UserService;
import org.Netflix.Exeptions.controller.model.CreateUserInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public class UserController {

    @Autowired
    UserService userService;

    public ResponseEntity<String>addNewUser(@RequestBody CreateUserInput createUserInput){
         String name = createUserInput.getName();
         String email = createUserInput.getEmail();
         String phoneNo = createUserInput.getPhoneNo();
         String password = createUserInput.getPassword();

         try{
             userService.addNewUser(email, name, password, phoneNo);
             return ResponseEntity.status(HttpStatus.OK).body("User created successfully!");
         }
         catch (InvalidDataException ex){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
         }
         catch(Exception ex){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
         }
    }
}
