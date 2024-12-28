package com.aizs.controller;

import com.aizs.dto.FeedbackDTO;
import com.aizs.entity.Feedback;
import com.aizs.service.FeedbackService;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        Feedback feedback = new Feedback();
        feedback.setUserid(userId);
        feedback.setContent(feedbackDTO.getContent());
        feedbackService.save(feedback);
        return ResponseEntity.ok("Feedback submitted successfully");
    }

    @GetMapping("/user")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }
        List<Feedback> feedbacks = feedbackService.findByUserid(userId);
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream().map(f -> {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setFeedbackid(f.getFeedbackid());
            dto.setUserid(f.getUserid());
            dto.setContent(f.getContent());
            dto.setCreated_at(f.getCreated_at());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(feedbackDTOs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedbacks() {
        // 从会话中获取角色
        String role = (String) StpUtil.getSession().get("role");
        System.out.println("Role from session: " + role);

        if (!"admin".equals(role)) {
            System.out.println("User does not have admin role, returning 403 Forbidden");
            return ResponseEntity.status(403).body(null);
        }

        List<Feedback> feedbacks = feedbackService.findAll();
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream().map(f -> {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setFeedbackid(f.getFeedbackid());
            dto.setUserid(f.getUserid());
            dto.setContent(f.getContent());
            dto.setCreated_at(f.getCreated_at());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(feedbackDTOs);
    }

    @DeleteMapping("/delete/{feedbackid}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long feedbackid) {
        Long userId = StpUtil.getLoginIdAsLong();
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }
        feedbackService.deleteById(feedbackid);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFeedbacks(@RequestParam List<Long> feedbackids) {
        Long userId = StpUtil.getLoginIdAsLong();
        if (userId == null) {
            return ResponseEntity.status(401).body(null);
        }
        feedbackService.deleteByIds(feedbackids);
        return ResponseEntity.ok("Feedbacks deleted successfully");
    }

    @GetMapping("/export")
    public void exportFeedbacks(HttpServletResponse response) throws IOException {
        String role = (String) StpUtil.getSession().get("role");
        if (!"admin".equals(role)) {
            response.sendError(403, "Forbidden");
            return;
        }
        List<Feedback> feedbacks = feedbackService.findAll();

        // 设置响应类型及字符集
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=feedbacks.csv");

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8), true);
        writer.println("Feedback ID,User ID,Content,Created At");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Feedback feedback : feedbacks) {
            System.out.println("Feedback ID: " + feedback.getFeedbackid() +
                    ", Created At: " + feedback.getCreated_at()); // Debug log

            writer.println(feedback.getFeedbackid() + "," +
                    feedback.getUserid() + "," +
                    feedback.getContent() + "," +
                    (feedback.getCreated_at() != null ? dateFormat.format(feedback.getCreated_at()) : ""));
        }
        writer.flush();
    }


    @GetMapping("/user/{userid}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByUserId(@PathVariable Long userid) {
        // 从会话中获取角色
        String role = (String) StpUtil.getSession().get("role");
        if (!"admin".equals(role)) {
            return ResponseEntity.status(403).body(null);
        }
        List<Feedback> feedbacks = feedbackService.findByUserid(userid);
        List<FeedbackDTO> feedbackDTOs = feedbacks.stream().map(f -> {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setFeedbackid(f.getFeedbackid());
            dto.setUserid(f.getUserid());
            dto.setContent(f.getContent());
            dto.setCreated_at(f.getCreated_at());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(feedbackDTOs);
    }
}