/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

/**
 *
 * @author irina
 */
public enum UserRole {
    CANDIDATE("candidate"), ADMIN("admin");
    private final String description;
    
    public String getDescription() {
        return description;
    }
    
    UserRole(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
    
    
}
