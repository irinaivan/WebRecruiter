/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.model.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author irina
 */
@Document(collection="candidates")
public class Candidate {
    @Id
    @JsonIgnore
    private ObjectId id;
    
    private String userName;
    
    private String firstName;
    
    private String lastName;
    
    private String jobName;
    
    private String jobProject;
    
    private int testPoints;
    
    private int cvPoints;
    
    private String cvPath;
    
    public Candidate() {
    }
    
    public Candidate(String userName, String firstName, String lastName, String jobName, String jobProject){
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobName = jobName;
        this.jobProject = jobProject;
    }

    @JsonIgnore
    public ObjectId getId() {
        return id;
    }

    @JsonIgnore
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public int getTestPoints() {
        return testPoints;
    }

    public void setTestPoints(int testPoints) {
        this.testPoints = testPoints;
    }

    public int getCvPoints() {
        return cvPoints;
    }

    public void setCvPoints(int cvPoints) {
        this.cvPoints = cvPoints;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }
}
