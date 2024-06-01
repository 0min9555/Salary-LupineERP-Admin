package com.yangjae.lupine.config.exception;

import com.yangjae.lupine.model.enums.UserError;
import lombok.Getter;

@Getter
public class UserException extends Exception {
    private final UserError userError;
    private final String detail;

    public UserException(UserError userError) {
        this.userError = userError;
        this.detail = null;
    }

    public UserException(UserError userError, String detail) {
        this.userError = userError;
        this.detail = detail;
    }
}
