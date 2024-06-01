package com.yangjae.lupine.config.security;

import com.yangjae.lupine.model.dto.ErrorResponse;
import com.yangjae.lupine.model.enums.UserError;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Server 에 요청 시 권한이 없는 경우 핸들링 하여 응답 포맷 지정
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(UserError.FORBIDDEN);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        if (jsonConverter.canWrite(errorResponse.getClass(), jsonMimeType)) {
            jsonConverter.write(errorResponse, jsonMimeType, new ServletServerHttpResponse(response));
        }
    }

}
