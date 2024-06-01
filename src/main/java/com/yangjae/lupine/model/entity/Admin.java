package com.yangjae.lupine.model.entity;

import com.yangjae.lupine.model.dto.AdminDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public Admin(LocalDateTime createdAt, LocalDateTime updatedAt, Integer idx, String id, String password, String name,
                 String allowedIp, Integer loginFailureCnt, LocalDate withdrawalDate, LocalDateTime lastLoginDate,
                 String createdBy, String updatedBy) {
        super(createdAt, updatedAt);
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

    public void updateLoginTime() {
        this.lastLoginDate = LocalDateTime.now();
    }

}
