package com.yangjae.lupine.config.security;

import com.yangjae.lupine.model.dto.ErrorResponse;
import com.yangjae.lupine.model.enums.UserError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Server 에 요청 시 미인증 상태인 경우 핸들링 하여 응답 포맷 지정
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(UserError.PERMISSION_DENIED);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect("/admin/login");

        if (jsonConverter.canWrite(errorResponse.getClass(), jsonMimeType)) {
            jsonConverter.write(errorResponse, jsonMimeType, new ServletServerHttpResponse(response));
        }

    }
}