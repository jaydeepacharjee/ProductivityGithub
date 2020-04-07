package com.aroha.pet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.aroha.pet.service.QueryInfoService;

/**
 *
 * @author Sony George | Date : 5 Mar, 2019 5:39:05 PM
 */
@RestController
@RequestMapping("/api/mentor")
public class MentorController {

    @Autowired
    QueryInfoService queryInfoService;

    @GetMapping("/queries")
    public ResponseEntity<?> getQueryOf(
            @Valid @RequestParam(value = "userId", required = true) Long userId,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) throws Exception {
// date should be in "dd-MM-yyyy" format
        if (fromDate != null && fromDate.isEmpty()) {
            fromDate = null;
        }
        if (toDate != null && toDate.isEmpty()) {
            toDate = null;
        }
        return ResponseEntity.ok(queryInfoService.getAllQueryInfoByParams(userId, fromDate, toDate));
    }

}
