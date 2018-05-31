/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.exceptions;

/**
 *
 * @author irina
 */
public class BoyerMooreException extends RuntimeException {
    public BoyerMooreException(String message) {
        super(message);
    }

    public BoyerMooreException(String message, Throwable cause) {
        super(message, cause);
    }
}
