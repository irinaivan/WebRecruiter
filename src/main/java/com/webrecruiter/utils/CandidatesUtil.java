/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

import com.webrecruiter.model.mongo.Question;
import java.util.List;
import java.util.Map;

/**
 *
 * @author irina
 */
public class CandidatesUtil {
    public static int calculateTestPoints(List<Question> jobQuestions, Map<String, String> testData) {
        int points = 0;
        int count = 1;
        for (Question q : jobQuestions) {
            String mapKey = String.format("%s%s%s", "question", count, "Answer");
            if (q.getCorrectAnswer().equals(testData.get(mapKey))) {
                points++;
            }
            count++;
        }
        return points;
    }   
}
