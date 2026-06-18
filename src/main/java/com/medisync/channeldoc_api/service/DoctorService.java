package com.medisync.channeldoc_api.service;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorSearchResponseDto;
import com.medisync.channeldoc_api.dto.response.RestPage;
import com.medisync.channeldoc_api.model.enums.Specialization;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    DoctorResponseDto createDoctor(DoctorRequestDto request);

    RestPage<DoctorSearchResponseDto> searchBySpecialization(Specialization specialization, Pageable pageable);
}
