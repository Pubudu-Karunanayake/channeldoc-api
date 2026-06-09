package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.MasterScheduleRequestDto;
import com.medisync.channeldoc_api.dto.response.MasterScheduleResponseDto;
import com.medisync.channeldoc_api.model.User;

public interface MasterScheduleService {
    MasterScheduleResponseDto createMasterSchedule(MasterScheduleRequestDto request, User user);
}
