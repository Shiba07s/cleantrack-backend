package com.cleartrack.ClearTrack.repositories;

 import com.cleartrack.ClearTrack.entity.Report;
 import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUserId(Long userId);

}