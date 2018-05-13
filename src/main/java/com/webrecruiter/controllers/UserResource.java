/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.controllers;

import com.webrecruiter.jwtsecurity.JwtGenerator;
import com.webrecruiter.model.mysql.User;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.webrecruiter.repository.mysql.UserRepository;
import com.webrecruiter.utils.UserRole;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author irina
 */
@RestController
@Configuration
@PropertySource("classpath:http_messages.properties")
@RequestMapping("/loginModule")
public class UserResource {

    @Autowired
    UserRepository usersRepository;

    @Value("${error.userExistsInDb}")
    private String userExistsInDb;

    @Value("${success.userCreated}")
    private String userCreated;

    @Value("${success.passwordUpdated}")
    private String passwordUpdated;

    @Value("${error.userNotInDb}")
    private String userNotInDb;
    
    @Value("${error.invalidPassword}")
    private String invalidPassword;

    @Autowired
    private JwtGenerator jwtGenerator;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> login(@RequestBody final Map<String, String> loginCredentials) throws ServletException {
        String token = null;
        String userRole = null;
        Map<String, String> responseBody = new HashMap<>();
        User userToLogin = new User(loginCredentials.get("userName"));
        userToLogin.setId(loginCredentials.get("userName"));
        userToLogin.setUserPassword(loginCredentials.get("userPassword"));
        if (usersRepository.existsById(userToLogin.getId())) {
            User userInDb = usersRepository.getOne(userToLogin.getId());
            if (userInDb.getUserPassword().equals(userToLogin.getUserPassword())) {
                token = jwtGenerator.generate(userToLogin);
                userRole = userInDb.getRole();
                responseBody.put("token", token);
                responseBody.put("userRole", userRole);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody.put("message", invalidPassword);
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseBody.put("message", userNotInDb);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody final Map<String, String> changedCredentials) throws ServletException {
        Map<String, String> responseBody = new HashMap<>();
        User userToUpdate = new User(changedCredentials.get("userName"));
        userToUpdate.setId(changedCredentials.get("userName"));
        userToUpdate.setUserPassword(changedCredentials.get("newUserPassword"));
        if (usersRepository.existsById(userToUpdate.getId())) {
            usersRepository.changePassword(userToUpdate.getUserPassword(), userToUpdate.getId());
            responseBody.put("message", passwordUpdated);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("message", userNotInDb);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> register(@RequestBody final Map<String, String> userCredentials) throws ServletException {
        Map<String, String> responseBody = new HashMap<>();
        User newUser = new User(userCredentials.get("userName"), UserRole.CANDIDATE.toString());
        newUser.setId(userCredentials.get("userName"));
        newUser.setFirstName(userCredentials.get("firstName"));
        newUser.setLastName(userCredentials.get("lastName"));
        newUser.setEmail(userCredentials.get("email"));
        newUser.setUserPassword(userCredentials.get("userPassword"));
        if (usersRepository.existsById(newUser.getId())) {
            responseBody.put("message", userExistsInDb);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            usersRepository.save(newUser);
            responseBody.put("message", userCreated);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
    }

}
