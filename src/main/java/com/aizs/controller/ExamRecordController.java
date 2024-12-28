package com.aizs.controller;

import com.aizs.entity.ExamRecord;
import com.aizs.service.ExamRecordService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
public class ExamRecordController {

    private final ExamRecordService examRecordService;

    public ExamRecordController(ExamRecordService examRecordService) {
        this.examRecordService = examRecordService;
    }

    // 工人只能查看自己的考核记录
    @PreAuthorize("hasRole('worker') and #userid == principal.id")
    @GetMapping("/records/{userid}")
    public List<ExamRecord> getExamRecordByUserId(@PathVariable Long userid) {
        return examRecordService.getExamRecordByUserId(userid);
    }

    // 管理员导出所有考核记录
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/records/export")
    public ResponseEntity<InputStreamResource> exportAllRecords() {
        return examRecordService.exportAllRecordsToCSV();
    }

    // 工人导出自己的考核记录
    @PreAuthorize("hasRole('worker') and #userid == principal.id")
    @GetMapping("/records/{userid}/export")
    public ResponseEntity<InputStreamResource> exportWorkerRecord(@PathVariable Long userid) {
        return examRecordService.exportWorkerRecordToCSV(userid);
    }

    // 管理员获取所有考核记录
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/records")
    public List<ExamRecord> getAllExamRecords() {
        List<ExamRecord> records = examRecordService.getAllExamRecords();
        System.out.println("Fetched Exam Records: " + records);
        return records;
    }
}
