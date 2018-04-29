/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.repository.mongo;

import com.webrecruiter.model.mongo.Job;
import java.util.ArrayList;
import java.util.List;
import com.webrecruiter.utils.JobsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 *
 * @author irina
 */
public class JobsRepositoryImpl implements JobsRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Job> getListOfJobsPartialData() {
        List<Job> jobs = new ArrayList<Job>();
        Query query = new Query();
        query.fields().include("jobName");
        query.fields().include("jobProject");
        query.fields().include("jobDescription");
        query.fields().include("jobRequirements");
        query.fields().exclude("id");
        jobs = mongoTemplate.find(query, Job.class);
        return jobs;
    }
}
