package com.company.ems.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId","date"}))
@Getter
@Setter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String workingHours;
    private String status;

    public Attendance(User user,
                      LocalDate date,
                      LocalTime checkInTime,
                      LocalTime checkOutTime,
                      String status){
        this.user = user;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }
}
