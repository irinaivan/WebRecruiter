/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.controllers;

import com.webrecruiter.model.mongo.Candidate;
import com.webrecruiter.model.mongo.Job;
import com.webrecruiter.model.mongo.Question;
import com.webrecruiter.model.mysql.User;
import com.webrecruiter.repository.mongo.CandidatesRepository;
import com.webrecruiter.repository.mongo.JobsRepository;
import com.webrecruiter.repository.mysql.UserRepository;
import com.webrecruiter.utils.CandidatesUtil;
import com.webrecruiter.utils.CvFileUtilsService;
import com.webrecruiter.utils.FileStorageService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    UserRepository usersRepository;

    @Autowired
    CandidatesRepository candidatesRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    CvFileUtilsService cvFileService;

    @Value("${error.jobNotInDb}")
    private String jobNotInDb;

    @Value("${success.underMinPoints}")
    private String underMinPoints;

    @Value("${success.testPointsMessage}")
    private String testPointsMessage;

    @Value("${success.uploadCV}")
    private String uploadCV;

    @Value("${error.testAlreadyTaken}")
    private String testAlreadyTaken;

    @Value("${success.testCanBeTaken}")
    private String testCanBeTaken;

    @Value("${error.cvInappropiate")
    private String cvInappropiate;

    @Value("${succes.cvPointsMessage}")
    private String cvPointsMessage;

    @Value("${error.candidateInexistent}")
    private String candidateInexistent;

    @RequestMapping(value = "/checkTestAlreadyTaken", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> checkIfTestWasTakenByUser(@RequestBody final Map<String, String> candidateInfo) {
        Map<String, String> responseBody = new HashMap<>();
        String[] jobInfo = candidateInfo.get("candidateSelectedJob").split("-");
        Job existingJob = jobsRepository.findOneByJobNameAndJobProject(jobInfo[0], jobInfo[1]);
        if (existingJob == null) {
            responseBody.put("message", jobNotInDb);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            Candidate existingCandidate = candidatesRepository.findOneByUserNameAndJobNameAndJobProject(candidateInfo.get("userName"), jobInfo[0], jobInfo[1]);
            if (existingCandidate != null) {
                responseBody.put("message", testAlreadyTaken);
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                User user = new User(candidateInfo.get("userName"));
                user.setId(candidateInfo.get("userName"));
                User existingUser = usersRepository.findOneById(user.getId());
                Candidate newCandidate = new Candidate(existingUser.getUserName(), existingUser.getFirstName(), existingUser.getLastName(), existingJob.getJobName(), existingJob.getJobProject());
                candidatesRepository.insert(newCandidate);
                responseBody.put("message", testCanBeTaken);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            }
        }
    }

    @RequestMapping(value = "/candidateJobQuestions", method = RequestMethod.GET)
    @ResponseBody
    public List<Question> getJobQuestions(@RequestParam("candidateSelectedJob") String jobNameAndProject) throws ServletException {
        String[] jobInfo = jobNameAndProject.split("-");
        List<Question> jobQuestions = new ArrayList<>();
        Job existingJob = jobsRepository.findOneByJobNameAndJobProject(jobInfo[0], jobInfo[1]);
        if (existingJob != null) {
            jobQuestions = existingJob.getQuestions();
            for (Question q : jobQuestions) {
                q.setCorrectAnswer(null);
            }
        }
        return jobQuestions;
    }

    @RequestMapping(value = "/candidateAnswers", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> calculateCandidateTestGrade(@RequestBody final Map<String, String> candidateTestData) {
        Map<String, String> responseBody = new HashMap<>();
        String[] jobInfo = candidateTestData.get("candidateSelectedJob").split("-");
        Job existingJob = jobsRepository.findOneByJobNameAndJobProject(jobInfo[0], jobInfo[1]);
        if (existingJob != null) {
            List<Question> jobQuestions = existingJob.getQuestions();
            User user = new User(candidateTestData.get("candidate"));
            user.setId(candidateTestData.get("candidate"));
            User existingUser = usersRepository.findOneById(user.getId());
            Candidate existingCandidate = candidatesRepository.findOneByUserNameAndJobNameAndJobProject(existingUser.getUserName(), existingJob.getJobName(), existingJob.getJobProject());
            int testGrade = CandidatesUtil.calculateTestPoints(jobQuestions, candidateTestData);
            int minGrade = jobQuestions.size() / 2;
            if (testGrade > minGrade) {
                responseBody.put("message", String.format("%s: %s. %s", testPointsMessage, testGrade, uploadCV));
                responseBody.put("passed", "true");
            } else {
                responseBody.put("message", underMinPoints);
                responseBody.put("passed", "false");
            }
            candidatesRepository.updateCandidateTestPoints(existingCandidate, testGrade);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("message", jobNotInDb);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/uploadCV", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> uploadCVAndCalculatePoints(@RequestParam("cv") MultipartFile file, @RequestParam("userName") String userName, @RequestParam("candidateSelectedJob") String jobNameAndProject) {
        Map<String, String> responseBody = new HashMap<>();
        String fileName = fileStorageService.storeFile(file, userName, jobNameAndProject);
        String cvText = cvFileService.getCVText(fileName);
        if (cvText != "") {
            String[] jobInfo = jobNameAndProject.split("-");
            Job existingJob = jobsRepository.findOneByJobNameAndJobProject(jobInfo[0], jobInfo[1]);
            if (existingJob != null) {
                User user = new User(userName);
                user.setId(userName);
                User existingUser = usersRepository.findOneById(user.getId());
                Candidate existingCandidate = candidatesRepository.findOneByUserNameAndJobNameAndJobProject(existingUser.getUserName(), existingJob.getJobName(), existingJob.getJobProject());
                String jobRequirements = existingJob.getJobRequirements();
                String[] requirements = jobRequirements.split(",");
                for (int i = 0; i < requirements.length; i++) {
                    requirements[i] = requirements[i].trim();
                }
                SortedSet<String> patterns = new TreeSet<String>(Arrays.asList(requirements));
                Map<String, Integer> numberOfFindsPerPattern = cvFileService.getNumberOfFindsForPatterns(patterns, cvText);
                double cvPoints = cvFileService.calculateCVPoints(numberOfFindsPerPattern);
                cvPoints = Math.floor(cvPoints * 100) / 100;
                candidatesRepository.updateCandidateCVPathAndPoints(existingCandidate, cvPoints, cvFileService.getCVLocation(fileName));
                responseBody.put("message", String.format(cvPointsMessage, cvPoints));
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody.put("message", jobNotInDb);
                return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            responseBody.put("message", cvInappropiate);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
