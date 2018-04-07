/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author irina
 */
@Entity
public class User {
    @Id
    @Column(name="id")
    private long id;
    @Column(name="user_name")
    private String userName;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String userPassword;
    @Column(name="role")
    private String role;
    
    public User() {
    }
    
    public User (String userName) {
        this.userName = userName;
    }
    
    public User (String userName, String role) {
        this.userName = userName;
        this.role = role;
    }

    public void setId(String userName) {
        this.id = getUserNameHash(userName);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getRole() {
        return role;
    }
    
    public long getUserNameHash(String userName) {
        int hash = 7;
        for (int i = 0; i < this.userName.length(); i++) {
            hash = hash*31 + this.userName.charAt(i);
        }
        return hash;
    }
        
}
