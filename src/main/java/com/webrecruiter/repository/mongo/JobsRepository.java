/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.repository.mongo;

import com.webrecruiter.model.mongo.Job;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.webrecruiter.utils.JobsRepositoryCustom;

/**
 *
 * @author irina
 */
public interface JobsRepository extends MongoRepository<Job, ObjectId>, JobsRepositoryCustom {
    
    Job findOneByJobNameAndJobProject(String jobName, String jobProject);
    
}
