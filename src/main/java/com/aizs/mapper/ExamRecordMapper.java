package com.aizs.mapper;

import com.aizs.entity.ExamRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamRecordMapper {

    // 插入考核记录
    @Insert("INSERT INTO exam_records (userid, exam_date, score, correct_count, total_questions, grade) " +
            "VALUES (#{userid}, NOW(), #{score}, #{correctCount}, #{totalQuestions}, #{grade})")
    void insertExamRecord(ExamRecord examRecord);

    // 根据工人ID查询工人考核记录
    @Select("SELECT * FROM exam_records WHERE userid = #{userid}")
    List<ExamRecord> findByUserId(Long userid);

    // 查询所有考核记录 (管理员)
    @Select("SELECT * FROM exam_records")
    List<ExamRecord> findAllExamRecords();

    // 根据ID删除考核记录
    @Delete("DELETE FROM exam_records WHERE examrecordid = #{examrecordid}")
    void deleteById(Long examrecordid);
    @Delete("<script>" +
            "DELETE FROM exam_records WHERE examrecordid IN " +
            "<foreach item='id' collection='list' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteByIds(@Param("list") List<Long> ids);

}
