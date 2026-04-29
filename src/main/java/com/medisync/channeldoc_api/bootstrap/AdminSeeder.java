package com.medisync.channeldoc_api.bootstrap;

import com.medisync.channeldoc_api.model.User;
import com.medisync.channeldoc_api.model.enums.AuthProvider;
import com.medisync.channeldoc_api.model.enums.UserRole;
import com.medisync.channeldoc_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("pubudu.karunanayake01@gmail.com")) {
            User superAdmin = User.builder()
                    .fullName("Super Admin")
                    .email("pubudu.karunanayake01@gmail.com")
                    .authProvider(AuthProvider.LOCAL)
                    .roles(Set.of(UserRole.ROLE_SUPERADMIN))
                    .build();
            userRepository.save(superAdmin);
        }
    }
}
