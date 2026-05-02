package com.cleartrack.ClearTrack.controller;


import com.cleartrack.ClearTrack.entity.Report;
import com.cleartrack.ClearTrack.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

      @GetMapping("/pending/reports")
    public List<Report> getAllPendingReports() {
        return reportService.getAllPendingReports();
    }

      @GetMapping("/approve/reports")
    public List<Report> getAllApprovedReports() {
        return reportService.getAllAprroveReports();
    }
    @GetMapping("/reject/reports")
    public List<Report> getAllRejectedReports() {
        return reportService.getAllRejectedReports();
    }

    @PutMapping("/approve/{id}")
    public Report approve(@PathVariable Long id) {
        return reportService.approveReport(id);
    }

    @PutMapping("/reject/{id}")
    public Report reject(@PathVariable Long id) {
        return reportService.rejectReport(id);
    }
}