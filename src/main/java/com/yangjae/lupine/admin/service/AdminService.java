package com.yangjae.lupine.admin.service;

import com.yangjae.lupine.admin.mapper.AdminMapper;
import com.yangjae.lupine.admin.repository.*;
import com.yangjae.lupine.config.exception.UserException;
import com.yangjae.lupine.model.dto.AdminDto;
import com.yangjae.lupine.model.entity.*;
import com.yangjae.lupine.model.enums.UserError;
import com.yangjae.lupine.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AdminService {
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;


    public AdminService(PasswordEncoder passwordEncoder, AdminRepository adminRepository,
                        AdminMapper adminMapper) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    public Optional<Admin> findByAdminId(String id) {
        return adminRepository.findById(id);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public Map<String, Object> getAdminByIdx(int adminIdx) {
        Map<String, Object> result = adminMapper.selectAdminByIdx(adminIdx);

        return result;
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

}
