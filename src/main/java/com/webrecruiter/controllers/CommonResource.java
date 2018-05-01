/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.controllers;

import com.webrecruiter.model.mongo.Job;
import com.webrecruiter.repository.mongo.JobsRepository;
import com.webrecruiter.utils.JobForCombo;
import com.webrecruiter.utils.JobsUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author irina
 */
@RestController
@Configuration
@PropertySource("classpath:http_messages.properties")
@RequestMapping("/commonModule")
public class CommonResource {

    @Autowired
    JobsRepository jobsRepository;

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
}

