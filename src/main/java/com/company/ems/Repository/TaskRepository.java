package com.company.ems.Repository;

import com.company.ems.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByStatus(String status);
    long countByUserId(Long userId);
    List<Task> findAllByUser_Id(long userId);
    long countByUserIdAndStatus(Long userId, String status);
    List<Task> findAllByStatus(String status);
    List<Task> findByUserIsNull();
}
