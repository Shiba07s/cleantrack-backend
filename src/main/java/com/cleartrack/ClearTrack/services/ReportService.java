package com.cleartrack.ClearTrack.services;


import com.cleartrack.ClearTrack.entity.Report;
import com.cleartrack.ClearTrack.entity.User;
import com.cleartrack.ClearTrack.repositories.ReportRepository;
import com.cleartrack.ClearTrack.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/images/";

    public Report createReport(Long userId, MultipartFile image, String description, String location) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Upload to S3
        String imageUrl = s3Service.uploadFile(image);

        Report report = new Report();
        report.setUser(user);
        report.setDescription(description);
        report.setLocation(location);
        report.setImageUrl(imageUrl);
        report.setStatus("PENDING");
        report.setCreatedAt(LocalDateTime.now());

        return reportRepository.save(report);
    }
    public List<Report> getAllPendingReports() {
        return reportRepository.findAll().stream().filter(a->a.getStatus().equals("PENDING")).toList();
    }
    public List<Report> getAllAprroveReports() {
        return reportRepository.findAll().stream().filter(a->a.getStatus().equals("APPROVED")).toList();
    }
    public List<Report> getAllRejectedReports() {
        return reportRepository.findAll().stream().filter(a->a.getStatus().equals("REJECTED")).toList();
    }
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getUserReports(Long userId) {
        return reportRepository.findByUserId(userId);
    }

    public Report getReportsById(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(()->new RuntimeException("report not found : "+reportId));
    }

    public Report  approveReport(Long id) {

        Report report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("APPROVED");

        User user = report.getUser();
        user.setRewardPoints(user.getRewardPoints() + 10);

        userRepository.save(user);

        return reportRepository.save(report);
    }

    public Report rejectReport(Long id) {

        Report report = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("REJECTED");

        return reportRepository.save(report);
    }

    public List<Report> getAllUserReports() {
        return reportRepository.findAll();
    }

    // Upload After Cleaning Image + Update Status
    public Report uploadAfterImage(Long reportId, MultipartFile image) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // Upload image to S3
        String imageUrl = s3Service.uploadFile(image);

        // Update fields
        report.setAfterImageUrl(imageUrl);
        report.setStatus("CLEANED");

        return reportRepository.save(report);
    }

}