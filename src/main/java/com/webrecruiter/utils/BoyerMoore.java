/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author irina
 */
public class BoyerMoore {
    private Map<Character, Integer> badCharSkip;
    private String pattern;
    private int defaultSkip = -1;

    public BoyerMoore(String pattern) {
        this.pattern = pattern;
        badCharSkip = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            badCharSkip.put(pattern.charAt(i), i);
        }
    }

    public int search(String text) {
        int m = pattern.length();
        int n = text.length();
        int nrOfFinds = 0;
        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; j--) {
                if (pattern.charAt(j) != text.charAt(i + j)) {
                    if (badCharSkip.containsKey(text.charAt(i + j))) {
                        defaultSkip = badCharSkip.get(text.charAt(i + j));
                    }
                    skip = Math.max(1, j - defaultSkip);
                    break;
                }
            }
            if (skip == 0) {
                nrOfFinds++;
                skip = pattern.length();
            }
        }
        return nrOfFinds;
    }
}
