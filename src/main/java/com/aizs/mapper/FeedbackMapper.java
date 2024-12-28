package com.aizs.mapper;

import com.aizs.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    List<Feedback> findByUserid(Long userid);
    List<Feedback> findAll();
    void insert(Feedback feedback);
    void deleteById(Long feedbackid);
    void deleteByIds(List<Long> feedbackids);
}
