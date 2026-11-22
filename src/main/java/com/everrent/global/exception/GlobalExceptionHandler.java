package com.everrent.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException ex) {
        // MethodArgumentNotValidException 예외 객체에서 예외 메시지만 추출하기 위해 작성한 코드
        // ex.getMessage()는 필요한 메시지 외에 모든 것을 가져옴
        String errorMessage = ex.getBindingResult().getFieldErrors()  // 원본 리스트, 예외 메세지들이 있는 경로
                                .stream()                             // 리스트의 데이터를 가공하기 위해 스트림 파이프라인으로 변환
                                .map(FieldError::getDefaultMessage)   // 각 FieldError 객체에서 에러 문구만 추출
                                .collect(Collectors.joining(", "));  // 꺼낸 모든 원소를 하나의 문자열로 합침 + 쉼표로 구분


        // 발생한 에러 정보를 전달하기 위한 DTO를 작성하고
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),  // HTTP 상태코드는 400
                "Bad Request",                   // 전달될 메시지는 "Bad Request"
                errorMessage                   // 추가로 DTO에서 지정한 에러 메시지도 같이 전달
        );

        // 상태 코드는 400으로 주고 body에 DTO를 담아 리턴
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
