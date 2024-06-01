package com.yangjae.lupine.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@EqualsAndHashCode(of = {"idx"}) // 로그인 최대 허용치 설정을 위해 lombok 을 통해 idx 컬럼으로 equals 와 hashcode 오버라이딩
public class CustomUserDetails implements UserDetails, OAuth2User {
    // common
    private String type;

    private Integer idx;

    private String id;

    private String password;

    private String name;

    private LocalDate withdrawalDate;

    // user
    private Map<String, Object> attributes;

    private String oauthType;

    private String oauthId;

    // admin
    private String role;

    private String allowedIp;

    private Integer loginFailureCnt;


    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.withdrawalDate == null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Builder
    public CustomUserDetails(String type, Integer idx, String id, String password, String name, LocalDate withdrawalDate,
                             Map<String, Object> attributes, String role, String allowedIp, String oauthType,
                             String oauthId, Integer loginFailureCnt) {
        this.type = type;
        this.idx = idx;
        this.id = id;
        this.password = password;
        this.name = name;
        this.withdrawalDate = withdrawalDate;
        this.attributes = attributes;
        this.role = role;
        this.allowedIp = allowedIp;
        this.oauthType = oauthType;
        this.oauthId = oauthId;
        this.loginFailureCnt = loginFailureCnt;
    }
}
