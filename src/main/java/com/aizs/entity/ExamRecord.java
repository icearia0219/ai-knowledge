package com.aizs.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Getter
@Setter
@Table(name = "`exam_records`")
public class ExamRecord {
    @Id
    private Long examrecordid;  // examrecordid 小写
    private Long userid;        // userid 小写
    private Date examDate;
    private Integer score;
    private Integer correctCount;
    private Integer totalQuestions;
    private String grade;
}
