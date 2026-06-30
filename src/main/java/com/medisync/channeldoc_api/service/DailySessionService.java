package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.response.DailySessionResponseDto;
import com.medisync.channeldoc_api.model.User;

import java.time.LocalDate;
import java.util.List;

public interface DailySessionService {
    List<DailySessionResponseDto> getDailySessions(LocalDate date, Long masterScheduleId, User user);

    DailySessionResponseDto cancelDailySession(Long sessionId, User user);
}
