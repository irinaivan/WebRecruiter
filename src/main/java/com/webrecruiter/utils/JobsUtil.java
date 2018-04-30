/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

import com.webrecruiter.model.mongo.Job;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author irina
 */
public class JobsUtil {
    public static List<JobForCombo> prepareJobsInfoForCombo(List<Job> jobsForCombo) {
        List<JobForCombo> jobsInfoForCombo = new ArrayList<JobForCombo>();
        for (Job job : jobsForCombo) {
            jobsInfoForCombo.add(new JobForCombo(job.getJobName(), job.getJobProject()));
        }
        return jobsInfoForCombo;
    }
    
    public static boolean jobNameAndProjectValidation(String jobNameOrProject){
        return jobNameOrProject.contains("-");
    }
}
