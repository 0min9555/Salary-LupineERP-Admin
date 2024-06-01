package com.yangjae.lupine.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum UserError {
    BAD_REQUEST("001", "요청 값 오류", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED("002", "인증되지 않음", UNAUTHORIZED),
    HAVE_NO_DATA("003", "데이터 없음", NOT_FOUND),
    FORBIDDEN("004", "권한 없음", HttpStatus.FORBIDDEN),
    NOT_FOUND_USER("005", "계정 없음", UNAUTHORIZED),
    MISMATCH_PASSWORD("006", "비밀번호 불일치", UNAUTHORIZED),
    WITHDRAWAL_USER("008", "탈퇴한 계정", UNAUTHORIZED),
    NOT_ALLOWED_IP("009", "허용되지 않은 IP", UNAUTHORIZED),
    LOCKED_ACCOUNT("010", "잠김 계정", UNAUTHORIZED),
    FILE_UPLOAD_ERROR("011", "파일 업로드 실패", INTERNAL_SERVER_ERROR),
    FILE_DOWNLOAD_ERROR("012", "파일 다운로드 실패", INTERNAL_SERVER_ERROR),
    FILE_DELETE_ERROR("013", "파일 삭제 실패", INTERNAL_SERVER_ERROR),
    SELECT_ERROR("014", "조회 실패", INTERNAL_SERVER_ERROR),
    INSERT_ERROR("015", "등록 실패", INTERNAL_SERVER_ERROR),
    UPDATE_ERROR("016", "수정 실패", INTERNAL_SERVER_ERROR),
    DELETE_ERROR("017", "삭제 실패", INTERNAL_SERVER_ERROR),
    NOT_DEFINED("999", "서버 에러", INTERNAL_SERVER_ERROR)
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    UserError(String errorCode, String message, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public static UserError getErrorByAuthenticationEx(AuthenticationException exception) {
        UserError userError;

        if (exception instanceof BadCredentialsException && "password".equals(exception.getMessage())) {
            userError = MISMATCH_PASSWORD;
        } else if (exception instanceof BadCredentialsException && "ip".equals(exception.getMessage())) {
            userError = NOT_ALLOWED_IP;
        } else if (exception instanceof UsernameNotFoundException) {
            userError = NOT_FOUND_USER;
        } else if (exception instanceof DisabledException) {
            userError = WITHDRAWAL_USER;
        } else if (exception instanceof LockedException) {
            userError = LOCKED_ACCOUNT;
        } else {
            userError = NOT_DEFINED;
        }

        return userError;
    }
}
