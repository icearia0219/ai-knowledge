package com.aizs.service.impl;


import com.aizs.Repository.ExamRecordRepository;
import com.aizs.entity.ExamRecord;
import com.aizs.mapper.ExamRecordMapper;
import com.aizs.service.ExamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

// 服务层实现
@Service
public class ExamRecordServiceImpl implements ExamRecordService {

    @Autowired
    private ExamRecordRepository examRecordRepository;

    @Override
    public List<ExamRecord> getAllExamRecords() {
        return examRecordRepository.findAll();
    }

    @Override
    public List<ExamRecord> getExamRecordByUserId(Long userid) {
        return examRecordRepository.findByUserid(userid);
    }

    @Override
    public ResponseEntity<InputStreamResource> exportAllRecordsToCSV() {
        List<ExamRecord> records = examRecordRepository.findAll();
        return generateCSV(records);
    }

    @Override
    public ResponseEntity<InputStreamResource> exportWorkerRecordToCSV(Long userid) {
        List<ExamRecord> records = examRecordRepository.findByUserid(userid);
        return generateCSV(records);
    }

    // 生成CSV文件
    private ResponseEntity<InputStreamResource> generateCSV(List<ExamRecord> records) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Exam Record ID", "User ID", "Exam Date", "Score", "Correct Count", "Total Questions", "Grade"))) {

            for (ExamRecord record : records) {
                csvPrinter.printRecord(
                        record.getExamrecordid(),
                        record.getUserid(),
                        record.getExamDate(),
                        record.getScore(),
                        record.getCorrectCount(),
                        record.getTotalQuestions(),
                        record.getGrade()
                );
            }

            csvPrinter.flush();

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exam_records.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }
}
