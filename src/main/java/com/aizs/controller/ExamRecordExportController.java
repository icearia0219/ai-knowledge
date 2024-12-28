package com.aizs.controller;


import com.aizs.entity.ExamRecord;
import com.aizs.service.ExamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/exam-records")
public class ExamRecordExportController {

    @Autowired
    private ExamRecordService examRecordService;

    // 导出所有考核记录为 CSV
    @GetMapping("/export")
    public void exportExamRecords(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=exam_records.csv");

        List<ExamRecord> examRecords = examRecordService.getAllExamRecords();
        try (OutputStream out = response.getOutputStream()) {
            String header = "examrecordid,userid,exam_date,score,correct_count,total_questions,grade\n";
            out.write(header.getBytes());

            for (ExamRecord record : examRecords) {
                String line = String.format("%d,%d,%s,%d,%d,%d,%s\n",
                        record.getExamrecordid(), record.getUserid(), record.getExamDate(),
                        record.getScore(), record.getCorrectCount(), record.getTotalQuestions(),
                        record.getGrade());
                out.write(line.getBytes());
            }
        }
    }
}
