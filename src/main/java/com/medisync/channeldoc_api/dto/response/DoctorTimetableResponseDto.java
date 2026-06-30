package com.medisync.channeldoc_api.dto.response;

import com.medisync.channeldoc_api.model.enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Response DTO representing a single timetable entry for a doctor.
 *
 * <p>Each entry corresponds to one {@code MasterSchedule} row and shows
 * the day, time window, hospital, and consultation fee. For entries matching
 * today's day of the week, {@code isToday} is {@code true} and
 * {@code todayAppointmentCount} is populated with the live count of
 * active appointments at that hospital.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorTimetableResponseDto {
    private Day day;
    private String hospitalName;
    private Long hospitalId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double consultationFee;
    private boolean isToday;
    private Integer todayAppointmentCount;
}
