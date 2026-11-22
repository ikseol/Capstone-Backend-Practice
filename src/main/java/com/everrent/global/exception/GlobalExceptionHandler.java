package com.everrent.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 해당 프로젝트에서 발생하는 모든 에러를 여기에서 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 방금 작성한 OwnerCustomException.class 가 발생하면 아래의 메소드를 실행
    @ExceptionHandler(OwnerCustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(OwnerCustomException ex) {
        /// OwnerCustomException ex는 비즈니스 코드에서 전달된 에러 메시지를 꺼내오기 위해서 매개변수로 받음

        // ErrorResponse 객체에 http 상태코드, 에러종류, 에러메시지를 담아서 생성
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage()
        );

        // 리스폰스 엔티티 객체에 상태코드를 FORBIDDEN (403) 으로 주고 Body 내부에는 에러 메시지 등이 포함된 객체를 그대로 전달
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
