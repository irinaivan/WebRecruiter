/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webrecruiter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author irina
 */
@Service
public class CvFileUtilsService {

    private final Path fileLocation;
    private final int MAXIMUM_POINTS_PER_PATTERN = 3;

    @Autowired
    public CvFileUtilsService(FileStorageProperties fileStorageProperties) {
        fileLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    public String getCVText(String fileName) {
        FileInputStream fileInpuStream = null;
        String documentText = "";
        try {
            File cvFile = new File(fileName);
            fileInpuStream = new FileInputStream(getCVLocation(fileName));
            XWPFDocument document = new XWPFDocument(fileInpuStream);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                String paragraphText = paragraph.getText();
                if (paragraphText != null) {
                    documentText = documentText.concat(paragraphText.toLowerCase());
                }
            }
            fileInpuStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CvFileUtilsService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CvFileUtilsService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInpuStream.close();
            } catch (IOException ex) {
                Logger.getLogger(CvFileUtilsService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return documentText;
    }

    public Map<String, Integer> getNumberOfFindsForPatterns(Set<String> patterns, String documentText) {
        Map<String, Integer> numberOfFindsPerPattern = new HashMap<>();
        for (String pattern : patterns) {
            BoyerMoore boyerMoore = new BoyerMoore(pattern.toLowerCase());
            numberOfFindsPerPattern.put(pattern.toLowerCase(), boyerMoore.search(documentText));
        }
        return numberOfFindsPerPattern;
    }

    public double calculateCVPoints(Map<String, Integer> numberOfFindsPerPattern) {
        double totalCVPoints = 0;
        double totalPoints = 0;
        for (Map.Entry<String, Integer> entry : numberOfFindsPerPattern.entrySet()) {
            if (entry.getValue() > MAXIMUM_POINTS_PER_PATTERN) {
                totalPoints += MAXIMUM_POINTS_PER_PATTERN;
            } else {
                totalPoints += entry.getValue();
            }
        }
        totalCVPoints = totalPoints / numberOfFindsPerPattern.size();
        return totalCVPoints;
    }
    
    public String getCVLocation (String fileName) {
        return fileLocation.toString() + "/" + fileName;
    }
}
