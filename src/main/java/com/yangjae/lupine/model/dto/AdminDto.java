package com.yangjae.lupine.model.dto;

import com.yangjae.lupine.model.entity.Admin;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminDto {

    private Integer idx;

    private String id;

    @Setter
    private String password;

    private String name;

    private String allowedIp;

    @Setter
    private Integer loginFailureCnt;

    private LocalDateTime lastLoginDate;

    private String createdBy;

    private String updatedBy;

    @Builder
    public AdminDto(Integer idx, String id, String password, String name, String allowedIp,
                    Integer loginFailureCnt, LocalDateTime lastLoginDate, String createdBy, String updatedBy) {
        this.idx = idx;
        this.id = id;
        this.password = password;
        this.name = name;
        this.allowedIp = allowedIp;
        this.loginFailureCnt = loginFailureCnt;
        this.lastLoginDate = lastLoginDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        this.updatedBy = createdBy;
    }

    public static AdminDto toDto(Admin admin) {
        return AdminDto.builder()
                .idx(admin.getIdx())
                .id(admin.getId())
                .password(admin.getPassword())
                .name(admin.getName())
                .allowedIp(admin.getAllowedIp())
                .loginFailureCnt(admin.getLoginFailureCnt())
                .lastLoginDate(admin.getLastLoginDate())
                .createdBy(admin.getCreatedBy())
                .updatedBy(admin.getUpdatedBy())
                .build();
    }
}
