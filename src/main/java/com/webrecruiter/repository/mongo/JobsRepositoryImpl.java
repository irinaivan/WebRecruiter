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
import org.springframework.data.mongodb.core.query.Criteria;

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

    @Override
    public List<Job> getListOfJobsForCombo() {
        List<Job> jobs = new ArrayList<Job>();
        Query query = new Query();
        query.fields().include("jobName");
        query.fields().include("jobProject");
        query.fields().exclude("id");
        jobs = mongoTemplate.find(query, Job.class);
        return jobs;
    }

    @Override
    public Job getJobAllInfo(String jobName, String jobProject) {
        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("jobName").is(jobName),
                        Criteria.where("jobProject").is(jobProject)
                )
        );
        query.fields().exclude("id");
        Job job = mongoTemplate.findOne(query, Job.class);
        return job;
    }

    @Override
    public boolean updateJob(Job jobToUpdate, Job modifiedJob) {
        Query query = new Query(
                new Criteria().andOperator(
                        Criteria.where("jobName").is(jobToUpdate.getJobName()),
                        Criteria.where("jobProject").is(jobToUpdate.getJobProject())
                )
        );
        //search again in db because we need the id
        Job existingJob = mongoTemplate.findOne(query, Job.class);
        if (existingJob != null && modifiedJob != null) {
            existingJob.setJobName(modifiedJob.getJobName());
            existingJob.setJobProject(modifiedJob.getJobProject());
            existingJob.setJobDescription(modifiedJob.getJobDescription());
            existingJob.setJobRequirements(modifiedJob.getJobRequirements());
            existingJob.setQuestions(modifiedJob.getQuestions());
            mongoTemplate.save(existingJob);
            return true;
        } else {
            return false;
        }
    }
}
