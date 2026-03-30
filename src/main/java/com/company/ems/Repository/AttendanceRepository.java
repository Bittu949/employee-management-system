package com.company.ems.Repository;

import com.company.ems.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    public long countByDateAndStatus(LocalDate date, String status);
    public long countByUserIdAndStatus(Long userId, String status);
    public long countByUserId(Long userId);
    public List<Attendance> findAllByUser_Id(long userId);
    @Query("SELECT DISTINCT YEAR(a.date) FROM Attendance a ORDER BY YEAR(a.date)")
    List<Integer> findDistinctYears();
    List<Attendance> findAllByUser_IdAndDateBetween(
            long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
