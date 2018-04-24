/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author irina
 */
@Document(collection="jobs")
@CompoundIndex(def = "{'jobName':1, 'jobProject':1}", name = "index")
public class Job {
    @Id
    private ObjectId id;
    
    private String jobName;
    
    private String jobProject;
    
    private String jobDescription;
    
    private String jobRequirements;
    
    private List<Question> questions;
    
    public Job() {}
    
    public Job(String jobName, String jobProject, String jobDescription, String jobRequirements) {
        this.jobName = jobName;
        this.jobProject = jobProject;
        this.jobDescription = jobDescription;
        this.jobRequirements = jobRequirements;
    }
    
    
    public String getId() {
        return id.toString();
    }
    public void setId(String id) {
        this.id = new ObjectId(id);
    }
    
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobProject() {
        return jobProject;
    }

    public void setJobProject(String jobProject) {
        this.jobProject = jobProject;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }

    public void setJobRequirements(String jobRequirements) {
        this.jobRequirements = jobRequirements;
    }
    
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}