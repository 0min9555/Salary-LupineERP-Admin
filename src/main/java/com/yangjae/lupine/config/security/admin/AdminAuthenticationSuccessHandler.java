package com.yangjae.lupine.config.security.admin;

import com.yangjae.lupine.model.dto.CustomUserDetails;
import com.yangjae.lupine.model.entity.Admin;
import com.yangjae.lupine.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class AdminAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AdminService adminService;

    public AdminAuthenticationSuccessHandler(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인된 유저객체 얻어오기
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String id = customUserDetails.getUsername();

        Admin admin = adminService.findByAdminId(id).orElseThrow(()
                -> new UsernameNotFoundException("USER NOT FOUND"));

        // 로그인에 성공 시 마지막 로그인 시간 갱신
        admin.updateLoginTime();
        // 로그인 실패 카운트 초기화
        admin.setLoginFailureCnt(0);

        adminService.saveAdmin(admin);

        Map<String, Object> adminMap = adminService.getAdminByIdx(admin.getIdx());
        log.debug("admin : {}", adminMap);

        HttpSession session = request.getSession();
        session.setAttribute("adminInfo", adminMap);

        // TODO: channelMenu 에서 어떤 기준으로 첫페이지를 정할지
        super.setDefaultTargetUrl("/admin/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
