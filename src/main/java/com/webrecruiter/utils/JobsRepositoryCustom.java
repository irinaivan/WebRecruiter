/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

import com.webrecruiter.model.mongo.Job;
import java.util.List;

/**
 *
 * @author irina
 */
public interface JobsRepositoryCustom {
    public List<Job> getListOfJobsPartialData();
}
