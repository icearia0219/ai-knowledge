package com.aizs.service;

import com.aizs.entity.Feedback;
import java.util.List;

public interface FeedbackService {
    List<Feedback> findByUserid(Long userid);
    List<Feedback> findAll();
    void save(Feedback feedback);
    void deleteById(Long feedbackid);
    void deleteByIds(List<Long> feedbackids);
}