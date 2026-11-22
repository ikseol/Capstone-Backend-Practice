package com.everrent.global.exception;

public class OwnerCustomException extends RuntimeException {
    public OwnerCustomException(String message) {
        super(message); // 비즈니스 코드에서 전달된 에러 메시지를 저장하기 위한 부모 생성자 호출
    }
}
