package com.yangjae.lupine.config.security.admin;

import com.yangjae.lupine.model.dto.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private final CustomAdminUserDetailsService customAdminUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    public AdminAuthenticationProvider(CustomAdminUserDetailsService customAdminUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customAdminUserDetailsService = customAdminUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        CustomUserDetails customUserDetails = (CustomUserDetails) customAdminUserDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, customUserDetails.getPassword())) {
            throw new BadCredentialsException("password");
        }

        if (!customUserDetails.isEnabled()) {
            throw new DisabledException("탈퇴한 회원 입니다.");
        }

        Integer loginFailureCnt = customUserDetails.getLoginFailureCnt();
        if (loginFailureCnt > 5) {
            throw new LockedException("잠김 계정");
        }

        String allowedIp = customUserDetails.getAllowedIp();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String accessIp = request.getRemoteAddr();

        if (allowedIp != null) {
            List<String> allowedIpList = Arrays.asList(allowedIp.split(","));
            boolean anyMatch = allowedIpList.stream().anyMatch(v -> v.equals(accessIp));

            if (anyMatch) throw new BadCredentialsException("ip");
        }

        return new UsernamePasswordAuthenticationToken(customUserDetails, password, customUserDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AnonymousAuthenticationToken.class.isAssignableFrom(authentication)
                || UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
                || OAuth2AuthenticationToken.class.isAssignableFrom(authentication)
                || RememberMeAuthenticationToken.class.isAssignableFrom(authentication)
                ;
    }
}
