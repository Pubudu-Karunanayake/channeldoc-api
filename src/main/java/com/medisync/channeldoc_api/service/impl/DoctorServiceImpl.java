package com.medisync.channeldoc_api.service.impl;

import com.medisync.channeldoc_api.dto.request.DoctorRequestDto;
import com.medisync.channeldoc_api.dto.response.DoctorResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorSearchResponseDto;
import com.medisync.channeldoc_api.dto.response.DoctorTimetableResponseDto;
import com.medisync.channeldoc_api.dto.response.RestPage;
import com.medisync.channeldoc_api.exception.ResourceNotFoundException;
import com.medisync.channeldoc_api.model.DoctorProfile;
import com.medisync.channeldoc_api.model.Hospital;
import com.medisync.channeldoc_api.model.MasterSchedule;
import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AppointmentStatus;
import com.medisync.channeldoc_api.model.enums.AuthProvider;
import com.medisync.channeldoc_api.model.enums.Day;
import com.medisync.channeldoc_api.model.enums.Specialization;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.AppointmentRepository;
import com.medisync.channeldoc_api.repository.DoctorProfileRepository;
import com.medisync.channeldoc_api.repository.HospitalRepository;
import com.medisync.channeldoc_api.repository.MasterScheduleRepository;
import com.medisync.channeldoc_api.repository.UserRepository;
import com.medisync.channeldoc_api.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final MasterScheduleRepository masterScheduleRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "doctors_by_spec", allEntries = true),
            @CacheEvict(value = "doctors_by_name", allEntries = true)
    })
    public DoctorResponseDto createDoctor(DoctorRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User already exists with this email");
        }

        if (doctorProfileRepository.existsBySlmcNumber(request.getSlmcNumber())) {
            throw new IllegalArgumentException("Doctor already exists with this SLMC number");
        }

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + request.getHospitalId()));

        User newUser = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .roles(Set.of(UserRole.ROLE_DOCTOR))
                .hospital(hospital)
                .authProvider(AuthProvider.LOCAL)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(newUser);

        DoctorProfile doctorProfile = DoctorProfile.builder()
                .user(savedUser)
                .slmcNumber(request.getSlmcNumber())
                .specialization(request.getSpecialization())
                .build();

        DoctorProfile savedProfile = doctorProfileRepository.save(doctorProfile);

        log.info("Created doctor [{}] with specialization [{}] — Redis cache evicted",
                savedUser.getFullName(), savedProfile.getSpecialization());

        return DoctorResponseDto.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .roles(savedUser.getRoles())
                .hospitalId(savedUser.getHospital() != null ? savedUser.getHospital().getId() : null)
                .isActive(savedUser.getIsActive())
                .slmcNumber(savedProfile.getSlmcNumber())
                .specialization(savedProfile.getSpecialization())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "doctors_by_spec",
            key = "#specialization.name() + '::' + #pageable.pageNumber + '::' + #pageable.pageSize"
    )
    public RestPage<DoctorSearchResponseDto> searchBySpecialization(Specialization specialization, Pageable pageable) {
        log.info("Cache MISS — querying database for specialization [{}], page [{}], size [{}]",
                specialization, pageable.getPageNumber(), pageable.getPageSize());

        Page<DoctorSearchResponseDto> mappedPage = doctorProfileRepository
                .findBySpecialization(specialization, pageable)
                .map(this::mapToSearchDto);

        return new RestPage<>(mappedPage);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "doctors_by_name", key = "#name")
    public List<DoctorSearchResponseDto> searchByName(String name) {
        log.info("Cache MISS — querying database for doctor name containing [{}]", name);

        return doctorProfileRepository.searchByUserFullName(name)
                .stream()
                .map(this::mapToSearchDto)
                .toList();
    }

    /**
     * Retrieves the logged-in doctor's full weekly timetable from MasterSchedule.
     *
     * <p>For entries matching today's day of the week, the response includes
     * the live count of active (non-cancelled) appointments at each hospital.</p>
     *
     * <p>Results are sorted: today's entries first, then by day-of-week ordinal,
     * then by start time within each day.</p>
     */
    @Override
    @Transactional(readOnly = true)
    public List<DoctorTimetableResponseDto> getMyTimetable(User user) {
        // 1. Resolve the DoctorProfile from the authenticated User
        DoctorProfile doctorProfile = doctorProfileRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor profile not found for user: " + user.getId()));

        // 2. Determine today's Day enum
        LocalDate today = LocalDate.now();
        Day todayDay = Day.valueOf(today.getDayOfWeek().name());

        // 3. Fetch all active schedules for this doctor
        List<MasterSchedule> schedules = masterScheduleRepository
                .findByDoctorIdAndIsActiveTrue(doctorProfile.getId());

        log.info("Fetched {} active master schedules for doctor [{}]",
                schedules.size(), user.getFullName());

        // 4. Map each schedule to a DTO, enriching today's entries with appointment count
        return schedules.stream()
                .map(schedule -> mapToTimetableDto(schedule, todayDay, today, doctorProfile.getId()))
                .sorted(timetableSorter(todayDay))
                .toList();
    }

    private DoctorTimetableResponseDto mapToTimetableDto(
            MasterSchedule schedule, Day todayDay, LocalDate today, Long doctorId) {

        boolean isToday = schedule.getDay() == todayDay;
        Integer appointmentCount = null;

        if (isToday) {
            long count = appointmentRepository.countByDoctorIdAndHospitalIdAndAppointmentDateAndStatusNot(
                    doctorId,
                    schedule.getHospital().getId(),
                    today,
                    AppointmentStatus.CANCELLED
            );
            appointmentCount = (int) count;
        }

        return DoctorTimetableResponseDto.builder()
                .day(schedule.getDay())
                .hospitalName(schedule.getHospital().getName())
                .hospitalId(schedule.getHospital().getId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .consultationFee(schedule.getConsultationFee())
                .isToday(isToday)
                .todayAppointmentCount(appointmentCount)
                .build();
    }

    /**
     * Comparator that sorts timetable entries:
     * 1. Today's entries first
     * 2. Then by day-of-week ordinal (Monday=0 .. Sunday=6)
     * 3. Then by start time within each day
     */
    private Comparator<DoctorTimetableResponseDto> timetableSorter(Day todayDay) {
        return Comparator
                .comparing((DoctorTimetableResponseDto dto) -> dto.getDay() != todayDay)
                .thenComparing(dto -> dto.getDay().ordinal())
                .thenComparing(DoctorTimetableResponseDto::getStartTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto> getMonthlyIncomeAnalysis(User user, int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }

        // 1. Resolve the DoctorProfile from the authenticated User
        DoctorProfile doctorProfile = doctorProfileRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor profile not found for user: " + user.getId()));

        // 2. Query the repository for the monthly income analysis
        List<com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto> analysis = appointmentRepository.findMonthlyIncomeByDoctor(
                doctorProfile.getId(), year, month);

        // 3. Sort results by doctorNetIncome descending
        return analysis.stream()
                .sorted(Comparator.comparing(
                        com.medisync.channeldoc_api.dto.response.DoctorMonthlyIncomeResponseDto::getDoctorNetIncome).reversed())
                .toList();
    }

    private DoctorSearchResponseDto mapToSearchDto(DoctorProfile profile) {
        User user = profile.getUser();
        Hospital hospital = user.getHospital();

        return DoctorSearchResponseDto.builder()
                .id(profile.getId())
                .doctorName(user.getFullName())
                .specialization(profile.getSpecialization())
                .hospitalName(hospital != null ? hospital.getName() : null)
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
