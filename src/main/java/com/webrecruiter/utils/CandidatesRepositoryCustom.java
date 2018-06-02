/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

import com.webrecruiter.model.mongo.Candidate;
import java.util.List;

/**
 *
 * @author irina
 */
public interface CandidatesRepositoryCustom {

    public void updateCandidateTestPoints(Candidate candidateToUpdate, int testPoints);

    public void updateCandidateCVPathAndPoints(Candidate candidateToUpdate, double cvPoints, String cvPath);

    public List<Candidate> getAllCandidatesPerJob(String jobName, String jobProject, int testMinPoints);

    public List<Candidate> getAllCandidatesToDelete(String jobName, String jobProject);
}
