/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

import com.webrecruiter.model.mongo.Candidate;

/**
 *
 * @author irina
 */
public interface CandidatesRepositoryCustom {
    public boolean updateCandidateTestPoints(Candidate candidateToUpdate, int testPoints);
}
