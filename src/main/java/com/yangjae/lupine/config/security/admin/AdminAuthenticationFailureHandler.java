package com.yangjae.lupine.config.security.admin;

import com.yangjae.lupine.model.entity.Admin;
import com.yangjae.lupine.model.enums.UserError;
import com.yangjae.lupine.admin.service.AdminService;
import com.yangjae.lupine.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AdminAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final AdminService adminService;

    public AdminAuthenticationFailureHandler(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String id = request.getParameter("username");

        // 비밀번호 오입력 시 로그인 실패횟수 카운트 업데이트
        if (exception instanceof BadCredentialsException) {
            Admin admin = adminService.findByAdminId(id).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

            admin.setLoginFailureCnt(admin.getLoginFailureCnt() + 1);
            log.debug("Admin {} Login Failure Count :: {}", admin.getName(), admin.getLoginFailureCnt());

            adminService.saveAdmin(admin);
        } else if (exception instanceof LockedException) {
            // 비밀번호 초기화 후 이메일 발송
            Admin admin = adminService.findByAdminId(id).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

            String newPassword = CommonUtil.generatePassword();
            log.debug("newPassword : {}", newPassword);
            // TODO: 문구 전달 받아서 넣어야함
            String subject = "New Password";
            String text = "New Password : " + newPassword + "\n변경된 비밀번호로 로그인 해주세요";


            String encodedPassword = adminService.encodePassword(newPassword);
            log.debug("encodedPassword : {}", encodedPassword);

            admin.setPassword(encodedPassword);

            adminService.saveAdmin(admin);
        }

        /*
        스프링 시큐리티 AuthenticationException 종류
        - UsernameNotFoundException : 계정 없음
        - BadCredentialsException : 비밀번호 미일치
        - AccountStatusException
            - AccountExpiredException : 계정만료
            - CredentialsExpiredException : 비밀번호 만료
            - DisabledException : 계정 비활성화
            - LockedException : 계정 잠김
        */
        UserError userError = UserError.getErrorByAuthenticationEx(exception);

        CommonUtil.printErrorMessage(response, userError);
    }
}
