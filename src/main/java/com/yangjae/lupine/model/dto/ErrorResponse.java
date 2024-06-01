package com.yangjae.lupine.model.dto;

import com.yangjae.lupine.config.exception.UserException;
import com.yangjae.lupine.model.enums.UserError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String errorCode;
    private String message;
    private String detail;

    @Builder
    public ErrorResponse(String errorCode, String message, String detail) {
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
    }

    public static ErrorResponse of(UserError userError) {
        return ErrorResponse.builder()
                .errorCode(userError.getErrorCode())
                .message(userError.getMessage())
                .build();
    }

    public static ErrorResponse of(UserError userError, UserException e) {
        return ErrorResponse.builder()
                .errorCode(userError.getErrorCode())
                .message(userError.getMessage())
                .detail(e.getDetail())
                .build();
    }
}
