package com.everrent.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 에러 응답을 담아서 보내 줄 DTO
@Getter // Get 메소드 자동 생성
@AllArgsConstructor // 생성자를 자동으로 생성해주는 어노테이션
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
}
