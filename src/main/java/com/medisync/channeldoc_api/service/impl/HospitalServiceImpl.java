package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.HospitalRequestDto;
import com.medisync.channeldoc_api.dto.response.HospitalResponseDto;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.repository.HospitalRepository;
import com.medisync.channeldoc_api.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    @Override
    public HospitalResponseDto createHospital(HospitalRequestDto requestDto) {
        Hospital hospital = Hospital.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .contactNumber(requestDto.getContactNumber())
                .build();

        Hospital savedHospital = hospitalRepository.save(hospital);

        return HospitalResponseDto.builder()
                .id(savedHospital.getId())
                .name(savedHospital.getName())
                .address(savedHospital.getAddress())
                .contactNumber(savedHospital.getContactNumber())
                .build();
    }
}
