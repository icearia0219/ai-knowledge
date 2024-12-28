package com.aizs.Repository;

import com.aizs.entity.ExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRecordRepository extends JpaRepository<ExamRecord, Long> {

    // 根据工人ID查找考核记录
    List<ExamRecord> findByUserid(Long userid);

    // 查找所有考核记录
    List<ExamRecord> findAll();
}
