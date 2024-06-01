package com.yangjae.lupine.config.security.admin;

import com.yangjae.lupine.model.dto.CustomUserDetails;
import com.yangjae.lupine.model.entity.Admin;
import com.yangjae.lupine.admin.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public CustomAdminUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // AuthenticationProvider 에서 받은 파라미터 (username) 를 통하여 DB 조회
        Admin admin = adminRepository.findById(username).orElseThrow(()
                -> new UsernameNotFoundException("USER NOT FOUND"));

        // UserDetails 를 상속하여 만든 CustomUserDetails 를 user 객체의 정보를 추가하여 생성 후 리턴
        return CustomUserDetails.builder()
                .type("ADMIN")
                .idx(admin.getIdx())
                .id(admin.getId())
                .password(admin.getPassword())
                .name(admin.getName())
                .allowedIp(admin.getAllowedIp())
                .loginFailureCnt(admin.getLoginFailureCnt())
                .build();
    }

    public boolean hasType(Authentication authentication, String type) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return type.equals(userDetails.getType());
        }

        return false;
    }
}