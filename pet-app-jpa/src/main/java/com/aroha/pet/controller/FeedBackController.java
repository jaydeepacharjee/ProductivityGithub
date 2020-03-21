package com.aroha.pet.controller;

import com.aroha.pet.payload.MentorFeedback;
import com.aroha.pet.security.CurrentUser;
import com.aroha.pet.security.UserPrincipal;
import com.aroha.pet.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
public class FeedBackController {

    @Autowired
    private FeedBackService feedService;

    @RequestMapping("/status")
    public ResponseEntity<?> getfeedback() {
        if (feedService.getData().isEmpty()) {
            return ResponseEntity.ok("No Data");
        }
        return ResponseEntity.ok(feedService.getData());
    }

    @PostMapping("/analysis")
    public ResponseEntity<?> showStudentAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId) {
        if (feedService.showAnalysis(created_by, createdAt, domainId) == null) {
            return ResponseEntity.ok("No data");
        }
        //        return ResponseEntity.ok(feedService.showAnalysis(created_by, createdAt,questionId));
        return new ResponseEntity<>((feedService.showAnalysis(created_by, createdAt, domainId)), HttpStatus.OK);
    }

    @PostMapping("/getDomainAnalysis")
    public ResponseEntity<?> getDomainAnalysis(@RequestParam String createdAt, @RequestParam long created_by) {
        return ResponseEntity.ok(feedService.getDomainResponse(created_by, createdAt));
    }

    @PostMapping("/getFunctionAnalysis")
    public ResponseEntity<?> getFunctionAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId) {
        return ResponseEntity.ok(feedService.getFunctionResponse(created_by, createdAt, domainId));
    }

    @PostMapping("/getScenarioAnalysis")
    public ResponseEntity<?> getScenarioAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId, @RequestParam int functionId) {
        return ResponseEntity.ok(feedService.getScenarioResponse(created_by, createdAt, domainId, functionId));
    }

    @PostMapping("/getQuestionAnalysis")
    public ResponseEntity<?> getQuestionAnalysis(@RequestParam String createdAt, @RequestParam long created_by, @RequestParam int domainId, @RequestParam int functionId,
            @RequestParam int scenarioId) {
        return ResponseEntity.ok(feedService.getQuestionResponse(created_by, createdAt, domainId, functionId, scenarioId));
    }

    @RequestMapping(value = "/exceptionAnalysis", method = RequestMethod.POST)
    public ResponseEntity<?> checkException(@RequestParam String createdAt, @RequestParam long created_by) {
        if (feedService.checkException(created_by, createdAt).isEmpty()) {
            return ResponseEntity.ok("No Data");
        }

        return ResponseEntity.ok(feedService.checkException(created_by, createdAt));
    }

    //    Capture Mentor Feedback
    @PostMapping("/saveMentorFeedback")
    public ResponseEntity<?> saveMentorFeedback(@RequestBody MentorFeedback mentorFeedback, @CurrentUser UserPrincipal user) {
        if (user.isLearnerRole()) {
            return ResponseEntity.ok("Not elligible");
        }
        return ResponseEntity.ok(feedService.saveFeedback(mentorFeedback, user));
    }

    // Show Mentor FeedBack
    @GetMapping("/showMentorFeedback")
    public ResponseEntity<?> showMentorFeedback(@CurrentUser UserPrincipal user) {
        if (feedService.showFeedback(user).isEmpty()) {
            return ResponseEntity.ok("No Feedback given by mentor");
        }
        return ResponseEntity.ok(feedService.showFeedback(user));
    }

    //Clear notification
    @GetMapping("/clearNotification")
    public ResponseEntity<?> clearNotification(@CurrentUser UserPrincipal user) {
        feedService.clearNotification(user);
        return ResponseEntity.ok("cleared notification");
    }

}
