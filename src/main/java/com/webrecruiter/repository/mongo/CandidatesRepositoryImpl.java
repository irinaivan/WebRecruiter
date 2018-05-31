/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.repository.mongo;

import com.webrecruiter.model.mongo.Candidate;
import com.webrecruiter.utils.CandidatesRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
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
    public boolean updateCandidateTestPoints(Candidate candidateToUpdate, int testPoints) {
        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("userName").is(candidateToUpdate.getUserName()),
                        Criteria.where("jobName").is(candidateToUpdate.getJobName()),
                        Criteria.where("jobProject").is(candidateToUpdate.getJobProject())
                )
        );
        Candidate existingCandidate = mongoTemplate.findOne(query, Candidate.class);
        if(existingCandidate != null) {
            existingCandidate.setTestPoints(testPoints);
            mongoTemplate.save(existingCandidate);
            return true;
        }else {
            return false;
        }
    }

}
