/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.controllers;

import com.webrecruiter.model.mongo.Job;
import com.webrecruiter.model.mongo.Question;
import com.webrecruiter.repository.mongo.JobsRepository;
import com.webrecruiter.utils.JobForCombo;
import com.webrecruiter.utils.JobsUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author irina
 */
@RestController
@Configuration
@PropertySource("classpath:http_messages.properties")
@RequestMapping("/adminModule")
public class AdminResource {

    @Autowired
    JobsRepository jobsRepository;

    @Value("${error.jobExistsInDb}")
    private String jobExistsInDb;

    @Value("${success.jobCreated}")
    private String jobCreated;

    @Value("${error.jobNotInDb}")
    private String jobNotInDb;

    @Value("${success.jobDeleted}")
    private String jobDeleted;

    @Value("${error.unableToUpdateJob}")
    private String unableToUpdateJob;

    @Value("${success.jobUpdated}")
    private String jobUpdated;

    @Value("${error.jobNameOrProjectIncorrect}")
    private String jobNameOrProjectIncorrect;

    private Job jobToUpdate = new Job();

    @RequestMapping(value = "/createJob", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> createJob(@RequestBody final Map<String, String> jobData) throws ServletException {
        Map<String, String> responseBody = new HashMap<>();
        if (JobsUtil.jobNameAndProjectValidation(jobData.get("jobName")) || JobsUtil.jobNameAndProjectValidation(jobData.get("jobProject"))) {
            responseBody.put("message", jobNameOrProjectIncorrect);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Job jobToCreate = new Job(jobData.get("jobName"), jobData.get("jobProject"), jobData.get("jobDescription"), jobData.get("jobRequirements"));
        Job sameJob = jobsRepository.findOneByJobNameAndJobProject(jobToCreate.getJobName(), jobToCreate.getJobProject());
        if (sameJob == null) {
            List<Question> jobQuestions = new ArrayList<Question>();
            jobQuestions.add(new Question(jobData.get("question1"), jobData.get("response1Q1"), jobData.get("response2Q1"), jobData.get("response3Q1"), jobData.get("correctResponseQ1")));
            jobQuestions.add(new Question(jobData.get("question2"), jobData.get("response1Q2"), jobData.get("response2Q2"), jobData.get("response3Q2"), jobData.get("correctResponseQ2")));
            jobQuestions.add(new Question(jobData.get("question3"), jobData.get("response1Q3"), jobData.get("response2Q3"), jobData.get("response3Q3"), jobData.get("correctResponseQ3")));
            jobQuestions.add(new Question(jobData.get("question4"), jobData.get("response1Q4"), jobData.get("response2Q4"), jobData.get("response3Q4"), jobData.get("correctResponseQ4")));
            jobQuestions.add(new Question(jobData.get("question5"), jobData.get("response1Q5"), jobData.get("response2Q5"), jobData.get("response3Q5"), jobData.get("correctResponseQ5")));
            jobToCreate.setQuestions(jobQuestions);
            jobsRepository.insert(jobToCreate);
            responseBody.put("message", jobCreated);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("message", jobExistsInDb);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/listOfJobs", method = RequestMethod.GET)
    @ResponseBody
    public List<Job> getListOfJobs() {
        List<Job> jobs = jobsRepository.getListOfJobsPartialData();
        return jobs;
    }

    @RequestMapping(value = "/jobsForCombo", method = RequestMethod.GET)
    @ResponseBody
    public List<JobForCombo> getJobsForCombo() {
        List<Job> jobs = jobsRepository.getListOfJobsForCombo();
        List<JobForCombo> jobsInfoForCombo = JobsUtil.prepareJobsInfoForCombo(jobs);
        return jobsInfoForCombo;
    }

    @RequestMapping(value = "/deleteJob", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> deleteJob(@RequestParam("jobInfo") String jobNameAndProject) {
        Map<String, String> responseBody = new HashMap<>();
        String[] jobInfo = jobNameAndProject.split("-");
        Job existingJob = jobsRepository.findOneByJobNameAndJobProject(jobInfo[0], jobInfo[1]);
        if (existingJob == null) {
            responseBody.put("message", jobNotInDb);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            jobsRepository.delete(existingJob);
            responseBody.put("message", jobDeleted);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/allJobInfo", method = RequestMethod.GET)
    @ResponseBody
    public Job getJobAllInfo(@RequestParam("jobInfo") String jobNameAndProject) {
        String[] jobInfo = jobNameAndProject.split("-");
        jobToUpdate = jobsRepository.getJobAllInfo(jobInfo[0], jobInfo[1]);
        return jobToUpdate;
    }

    @RequestMapping(value = "/updateJob", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateJob(@RequestBody final Map<String, String> jobData) {
        Map<String, String> responseBody = new HashMap<>();
        if (JobsUtil.jobNameAndProjectValidation(jobData.get("jobName")) || JobsUtil.jobNameAndProjectValidation(jobData.get("jobProject"))) {
            responseBody.put("message", jobNameOrProjectIncorrect);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Job modifiedJob = new Job(jobData.get("jobName"), jobData.get("jobProject"), jobData.get("jobDescription"), jobData.get("jobRequirements"));
        List<Question> modifiedJobQuestions = new ArrayList<Question>();
        modifiedJobQuestions.add(new Question(jobData.get("question1"), jobData.get("response1Q1"), jobData.get("response2Q1"), jobData.get("response3Q1"), jobData.get("correctResponseQ1")));
        modifiedJobQuestions.add(new Question(jobData.get("question2"), jobData.get("response1Q2"), jobData.get("response2Q2"), jobData.get("response3Q2"), jobData.get("correctResponseQ2")));
        modifiedJobQuestions.add(new Question(jobData.get("question3"), jobData.get("response1Q3"), jobData.get("response2Q3"), jobData.get("response3Q3"), jobData.get("correctResponseQ3")));
        modifiedJobQuestions.add(new Question(jobData.get("question4"), jobData.get("response1Q4"), jobData.get("response2Q4"), jobData.get("response3Q4"), jobData.get("correctResponseQ4")));
        modifiedJobQuestions.add(new Question(jobData.get("question5"), jobData.get("response1Q5"), jobData.get("response2Q5"), jobData.get("response3Q5"), jobData.get("correctResponseQ5")));
        modifiedJob.setQuestions(modifiedJobQuestions);
        boolean isJobUpdated = jobsRepository.updateJob(jobToUpdate, modifiedJob);
        if (isJobUpdated) {
            responseBody.put("message", jobUpdated);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("message", unableToUpdateJob);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
