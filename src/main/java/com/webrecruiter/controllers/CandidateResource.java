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
@RequestMapping("/candidateModule")
public class CandidateResource {

    @Autowired
    JobsRepository jobsRepository;

}