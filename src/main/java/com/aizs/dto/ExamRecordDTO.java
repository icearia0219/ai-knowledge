package com.aizs.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class ExamRecordDTO {
    private Long examrecordid;    // examrecordid 小写
    private Long userid;          // userid 小写
    private Date examDate;
    private Integer score;
    private Integer correctCount;
    private Integer totalQuestions;
    private String grade;

}
