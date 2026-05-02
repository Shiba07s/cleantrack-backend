package com.cleartrack.ClearTrack.controller;

import com.cleartrack.ClearTrack.entity.Report;
import com.cleartrack.ClearTrack.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReportController {

    private final ReportService reportService;

    @PostMapping(value = "/create/{userId}", consumes = "multipart/form-data")
    public Report createReport(
            @PathVariable Long userId,
            @RequestParam("image") MultipartFile image,
            @RequestParam("description") String description,
            @RequestParam("location") String location) {

        return reportService.createReport(userId, image, description, location);
    }
    @GetMapping("/user/{userId}")
    public List<Report> getUserReports(@PathVariable Long userId) {
        return reportService.getUserReports(userId);
    }
    @GetMapping("/{reportId}")
    public Report getReportsById(@PathVariable Long reportId) {
        return reportService.getReportsById(reportId);
    }
    @GetMapping("/user/get-all")
    public List<Report> getAllUserReports() {
        return reportService.getAllUserReports();
    }

    @PostMapping("/upload-after-photo/{reportId}")
    public ResponseEntity<Report> uploadAfterPhoto(
            @PathVariable Long reportId,
            @RequestParam("image") MultipartFile image) {

        Report updatedReport =
                reportService.uploadAfterImage(reportId, image);

        return ResponseEntity.ok(updatedReport);
    }
}