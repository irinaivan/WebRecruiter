/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.repository.mongo;

import com.webrecruiter.model.mongo.Candidate;
import com.webrecruiter.utils.CandidatesRepositoryCustom;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 *
 * @author irina
 */
public class CandidatesRepositoryImpl implements CandidatesRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void updateCandidateTestPoints(Candidate candidateToUpdate, int testPoints) {
        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("userName").is(candidateToUpdate.getUserName()),
                        Criteria.where("jobName").is(candidateToUpdate.getJobName()),
                        Criteria.where("jobProject").is(candidateToUpdate.getJobProject())
                )
        );
        Candidate existingCandidate = mongoTemplate.findOne(query, Candidate.class);
        if (existingCandidate != null) {
            existingCandidate.setTestPoints(testPoints);
            mongoTemplate.save(existingCandidate);
        }
    }

    @Override
    public void updateCandidateCVPathAndPoints(Candidate candidateToUpdate, double cvPoints, String cvPath) {
        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("userName").is(candidateToUpdate.getUserName()),
                        Criteria.where("jobName").is(candidateToUpdate.getJobName()),
                        Criteria.where("jobProject").is(candidateToUpdate.getJobProject())
                )
        );
        Candidate existingCandidate = mongoTemplate.findOne(query, Candidate.class);
        if (existingCandidate != null) {
            existingCandidate.setCvPoints(cvPoints);
            existingCandidate.setCvPath(cvPath);
            mongoTemplate.save(existingCandidate);
        }
    }

    @Override
    public List<Candidate> getAllCandidatesPerJob(String jobName, String jobProject, int testMinPoints) {
        List<Candidate> candidatesPerJob = new ArrayList();
        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("jobName").is(jobName),
                        Criteria.where("jobProject").is(jobProject),
                        Criteria.where("testPoints").gt(testMinPoints)
                )
        );
        query.with(new Sort(Sort.Direction.DESC, "testPoints"));
        query.with(new Sort(Sort.Direction.DESC, "cvPoints"));
        candidatesPerJob = mongoTemplate.find(query, Candidate.class);
        return candidatesPerJob;
    }

}
