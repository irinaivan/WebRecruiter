/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.repository.mongo;

import com.webrecruiter.model.mongo.Candidate;
import com.webrecruiter.utils.CandidatesRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author irina
 */
public interface CandidatesRepository extends MongoRepository<Candidate, ObjectId>, CandidatesRepositoryCustom {
    Candidate findOneByUserNameAndJobNameAndJobProject(String userName, String jobName, String jobProject);
}
