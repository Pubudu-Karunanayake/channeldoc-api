package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.response.DailySessionResponseDto;
import com.medisync.channeldoc_api.model.DailySession;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.repository.DailySessionRepository;
import com.medisync.channeldoc_api.service.DailySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailySessionServiceImpl implements DailySessionService {

    private final DailySessionRepository dailySessionRepository;

    @Override
    public List<DailySessionResponseDto> getDailySessions(LocalDate date, Long masterScheduleId, User user) {
        Hospital hospital = user.getHospital();
        if (hospital == null) {
            throw new IllegalArgumentException("User does not belong to any hospital");
        }

        if (masterScheduleId != null) {
            Optional<DailySession> session = dailySessionRepository.findByMasterScheduleIdAndSessionDateAndHospital(masterScheduleId, date, hospital);
            return session.map(s -> List.of(mapToDto(s))).orElse(List.of());
        }

        List<DailySession> sessions = dailySessionRepository.findByHospitalAndSessionDate(hospital, date);
        return sessions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private DailySessionResponseDto mapToDto(DailySession session) {
        return DailySessionResponseDto.builder()
                .id(session.getId())
                .hospitalId(session.getHospital() != null ? session.getHospital().getId() : null)
                .doctorId(session.getDoctor() != null ? session.getDoctor().getId() : null)
                .masterScheduleId(session.getMasterSchedule() != null ? session.getMasterSchedule().getId() : null)
                .sessionDate(session.getSessionDate())
                .status(session.getStatus())
                .build();
    }
}
