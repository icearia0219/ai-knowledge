package com.aizs.service;


import com.aizs.entity.ExamRecord;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import java.util.List;

// 服务层接口
public interface ExamRecordService {

    // 删除单条考核记录
    void deleteExamRecordById(Long examrecordid);

    // 批量删除考核记录
    void deleteExamRecordsByIds(List<Long> examrecordids);

    // 获取所有考核记录
    List<ExamRecord> getAllExamRecords();

    // 根据工人ID获取考核记录
    List<ExamRecord> getExamRecordByUserId(Long userid);

    // 导出所有考核记录到CSV
    ResponseEntity<InputStreamResource> exportAllRecordsToCSV();

    // 导出指定工人的考核记录到CSV
    ResponseEntity<InputStreamResource> exportWorkerRecordToCSV(Long userid);

}