package com.aizs.service.impl;

import com.aizs.entity.Feedback;
import com.aizs.mapper.FeedbackMapper;
import com.aizs.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public List<Feedback> findByUserid(Long userid) {
        return feedbackMapper.findByUserid(userid);
    }

    @Override
    public List<Feedback> findAll() {
        return feedbackMapper.findAll();
    }

    @Override
    public void save(Feedback feedback) {
        feedbackMapper.insert(feedback);
    }

    @Override
    public void deleteById(Long feedbackid) {
        feedbackMapper.deleteById(feedbackid);
    }

    @Override
    public void deleteByIds(List<Long> feedbackids) {
        feedbackMapper.deleteByIds(feedbackids);
    }
}