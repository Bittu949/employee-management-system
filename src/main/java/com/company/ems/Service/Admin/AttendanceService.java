//package com.company.ems.Service;
//
//import com.company.ems.Dto.Performance;
//import com.company.ems.Entity.Attendance;
//import com.company.ems.Repository.AttendanceRepository;
//import com.company.ems.Repository.UserRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//public class AttendanceService {
//    private final AttendanceRepository attendanceRepository;
//    private final UserRepository userRepository;
//
//    public Page<Attendance> getPaginatedAttendance(int page, int size){
//
//        if(page < 0) page = 0;
//
//        List<Attendance> attendances = showAttendanceRecords();
//
//        int start = page * size;
//        int end = Math.min(start + size, attendances.size());
//
//        List<Attendance> paginatedList;
//
//        if(start < attendances.size()){
//            paginatedList = attendances.subList(start, end);
//        } else {
//            paginatedList = new ArrayList<>();
//        }
//
//        return new PageImpl<>(paginatedList, PageRequest.of(page, size), attendances.size());
//    }
//    public List<Attendance> showAttendanceRecords(){
//        return attendanceRepository.findAll();
//    }
//    public long totalEmployeesCount(){
//        return userRepository.count();
//    }
//    public long presentCount(){
//        return attendanceRepository.countByDateAndStatus(LocalDate.now(), "PRESENT");
//    }
//    public long absentCount(){
//        return attendanceRepository.countByDateAndStatus(LocalDate.now(), "ABSENT");
//    }
//    public double showAttendancePercentage(){
//        long present = attendanceRepository
//                .countByDateAndStatus(LocalDate.now(), "PRESENT");
//        long total = userRepository.count();
//        if(total == 0) return 0;
//        return (present * 100.0) / total;
//    }
//    public Page<Attendance> filterAttendanceWithPagination(String name,
//                                                           Integer month,
//                                                           Integer year,
//                                                           String status,
//                                                           int page,
//                                                           int size){
//        if(page < 0) page = 0;
//
//        List<Attendance> attendances = filterAttendance(name, month, year, status);
//
//        int start = page * size;
//        int end = Math.min(start + size, attendances.size());
//
//        List<Attendance> paginatedList;
//
//        if(start < attendances.size()){
//            paginatedList = attendances.subList(start, end);
//        } else {
//            paginatedList = new ArrayList<>();
//        }
//
//        return new PageImpl<>(paginatedList, PageRequest.of(page, size), attendances.size());
//    }
//    public List<Attendance> filterAttendance(String name, Integer month, Integer year, String status){
//        List<Attendance> attendance = attendanceRepository.findAll();
//        if(name!=null && !name.trim().isEmpty())
//            attendance = attendance.stream().filter(a -> a.getUser() != null &&
//                                                         a.getUser().getFullName() != null &&
//                                                         a.getUser().getFullName().toLowerCase().contains(name.trim().toLowerCase()))
//                                                         .toList();
//        if(month!=null){
//            attendance = attendance.stream().filter(a -> a.getDate().getMonthValue()==month).toList();
//        }
//        if(year!=null){
//            attendance = attendance.stream().filter(a -> a.getDate().getYear()==year).toList();
//        }
//        if(status!=null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")){
//            attendance = attendance.stream().filter(a -> a.getStatus()!=null && a.getStatus().equalsIgnoreCase(status)).toList();
//        }
//        return attendance;
//    }
//}


package com.company.ems.Service.Admin;

import com.company.ems.Entity.Attendance;
import com.company.ems.Entity.User;
import com.company.ems.Exception.InvalidDataException;
import com.company.ems.Repository.AttendanceRepository;
import com.company.ems.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public Page<Attendance> getPaginatedAttendance(int page, int size, List<Attendance> attendanceList) {

        if (page < 0) page = 0;

        if (size <= 0) {
            throw new InvalidDataException("Page size must be greater than 0");
        }

        int start = page * size;
        int end = Math.min(start + size, attendanceList.size());

        List<Attendance> paginatedList;

        if (start < attendanceList.size()) {
            paginatedList = attendanceList.subList(start, end);
        } else {
            paginatedList = new ArrayList<>();
        }

        return new PageImpl<>(paginatedList, PageRequest.of(page, size), attendanceList.size());
    }

    public List<Attendance> showAttendanceRecords() {

        List<User> users = userRepository.findAllByRole("EMPLOYEE");
        List<Attendance> finalAttendanceList = new ArrayList<>();

        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDate today = LocalDate.now();

        for(User user : users) {

            if(user.getJoiningDate() == null){
                continue;
            }

            LocalDate startDate = user.getJoiningDate();
            LocalDate endDate = (user.getLeavingDate() != null) ? user.getLeavingDate().minusDays(1) : today;

            if (endDate.isBefore(startDate)) {
                continue;
            }

            // Step 1: Fetch attendance (1 DB call per user)
            List<Attendance> userAttendance =
                    attendanceRepository.findAllByUser_IdAndDateBetween(
                            user.getId(),
                            startDate,
                            endDate
                    );

            // Step 2: Convert to Map
            Map<LocalDate, Attendance> attendanceMap =
                    userAttendance.stream()
                            .collect(Collectors.toMap(
                                    Attendance::getDate,
                                    a -> a,
                                    (a1, a2) -> a1
                            ));

            // Step 3: Loop through dates
            for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){

                if(attendanceMap.containsKey(date)){
                    finalAttendanceList.add(attendanceMap.get(date));
                } else {
                    Attendance absent = new Attendance();
                    absent.setUser(user);
                    absent.setDate(date);
                    absent.setStatus("ABSENT");

                    finalAttendanceList.add(absent);
                }
            }
        }

        // Optional: sort final list
        finalAttendanceList.sort(
                Comparator.comparing(Attendance::getDate).reversed()
        );

        return finalAttendanceList;
    }

    public long totalEmployeesCount() {
        return userRepository.countByRoleAndStatus("EMPLOYEE", "ACTIVE");
    }

    public List<Integer> getYears(){
        List<Integer> years = attendanceRepository.findDistinctYears();
        if (years.isEmpty()) {
            years.add(LocalDate.now().getYear());
        }
        Collections.reverse(years);
        return years;
    }

    public long presentCount(List<Attendance> attendanceList) {
        long count = 0;
        LocalDate today = LocalDate.now();
        for(Attendance attendance : attendanceList){
            if(today.equals(attendance.getDate())){
                if("PRESENT".equalsIgnoreCase(attendance.getStatus())){
                    count++;
                }
            }
        }
        return count;
    }

    public long absentCount(List<Attendance> attendanceList) {
        long count = 0;
        LocalDate today = LocalDate.now();
        for(Attendance attendance : attendanceList){
            if(today.equals(attendance.getDate())){
                if("ABSENT".equalsIgnoreCase(attendance.getStatus())){
                    count++;
                }
            }
        }
        return count;
    }

    public double showAttendancePercentage(List<Attendance> attendanceList) {

        long present = presentCount(attendanceList);

        long total = userRepository.countByRoleAndStatus("EMPLOYEE", "ACTIVE");

        if (total == 0) return 0;

        return Math.round((present * 100.0) / total);
    }

    public Page<Attendance> filterAttendanceWithPagination(String name,
                                                           Integer month,
                                                           Integer year,
                                                           String status,
                                                           int page,
                                                           int size) {

        if (page < 0) page = 0;

        if (size <= 0) {
            throw new InvalidDataException("Page size must be greater than 0");
        }

        List<Attendance> attendances = filterAttendance(name, month, year, status);

        int start = page * size;
        int end = Math.min(start + size, attendances.size());

        List<Attendance> paginatedList;

        if (start < attendances.size()) {
            paginatedList = attendances.subList(start, end);
        } else {
            paginatedList = new ArrayList<>();
        }

        return new PageImpl<>(paginatedList, PageRequest.of(page, size), attendances.size());
    }

    public List<Attendance> filterAttendance(String name,
                                             Integer month,
                                             Integer year,
                                             String status) {

        List<Attendance> attendance = showAttendanceRecords();

        // Normalize search (BEST PRACTICE 🔥)
        if (name != null) {
            name = name.trim().toLowerCase();
        }

        if (name != null && !name.isEmpty()) {
            String finalName = name;
            attendance = attendance.stream()
                    .filter(a -> a.getUser() != null &&
                            a.getUser().getFullName() != null &&
                            a.getUser().getFullName().toLowerCase().contains(finalName))
                    .toList();
        }

        if (month != null) {

            if (month < 1 || month > 12) {
                throw new InvalidDataException("Invalid month value");
            }

            attendance = attendance.stream()
                    .filter(a -> a.getDate() != null &&
                            a.getDate().getMonthValue() == month)
                    .toList();
        }

        if (year != null) {
            attendance = attendance.stream()
                    .filter(a -> a.getDate() != null &&
                            a.getDate().getYear() == year)
                    .toList();
        }

        if (status != null && !status.trim().isEmpty()
                && !status.equalsIgnoreCase("ALL")) {

            attendance = attendance.stream()
                    .filter(a -> a.getStatus() != null &&
                            a.getStatus().equalsIgnoreCase(status))
                    .toList();
        }

        return attendance;
    }
}