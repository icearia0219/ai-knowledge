package com.aizs.service.impl;

import com.aizs.mapper.ExamRecordMapper;  // 导入 ExamRecordMapper
import com.aizs.Repository.ExamRecordRepository;
import com.aizs.entity.ExamRecord;
import com.aizs.service.ExamRecordService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExamRecordServiceImpl implements ExamRecordService {

    @Autowired
    private ExamRecordRepository examRecordRepository;

    // 修改：注入 ExamRecordMapper
    @Autowired
    private ExamRecordMapper examRecordMapper;

    @Override
    public void deleteExamRecordById(Long examrecordid) {
        examRecordRepository.deleteById(examrecordid);
    }

    @Override
    public void deleteExamRecordsByIds(List<Long> examrecordids) {
        // 修改：通过实例调用 deleteByIds
        examRecordMapper.deleteByIds(examrecordids);
    }

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
        return generateExcel(records);
    }

    @Override
    public ResponseEntity<InputStreamResource> exportWorkerRecordToCSV(Long userid) {
        List<ExamRecord> records = examRecordRepository.findByUserid(userid);
        return generateExcel(records);
    }

    // 生成Excel文件
    private ResponseEntity<InputStreamResource> generateExcel(List<ExamRecord> records) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Exam Records");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Exam Record ID", "User ID", "Exam Date",
                    "Score", "Correct Count", "Total Questions", "Grade"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderCellStyle(workbook));
            }

            // 填充数据
            int rowIndex = 1;
            for (ExamRecord record : records) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(record.getExamrecordid());
                row.createCell(1).setCellValue(record.getUserid());
                row.createCell(2).setCellValue(record.getExamDate().toString());
                row.createCell(3).setCellValue(record.getScore());
                row.createCell(4).setCellValue(record.getCorrectCount());
                row.createCell(5).setCellValue(record.getTotalQuestions());
                row.createCell(6).setCellValue(record.getGrade());
            }

            workbook.write(baos);

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exam_records.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }

    // 获取表头样式
    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}
