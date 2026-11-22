package com.everrent.global.exception.temp;

import lombok.Getter;

@Getter
public class TempCustomException extends RuntimeException {

    private final TempErrorCode tempErrorCode;

    public TempCustomException(TempErrorCode tempErrorCode) {
        super(tempErrorCode.getMessage());
        this.tempErrorCode = tempErrorCode;
    }
}