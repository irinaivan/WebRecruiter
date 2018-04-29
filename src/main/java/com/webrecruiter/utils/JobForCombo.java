/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

/**
 *
 * @author irina
 */
public class JobForCombo {
    private String jobInfo;
    
    public JobForCombo(String jobName, String jobProject) {
        this.jobInfo = String.format("%s-%s", jobName, jobProject);
    }

    public String getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo;
    }
}
