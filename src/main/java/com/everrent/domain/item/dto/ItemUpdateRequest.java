package com.everrent.domain.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemUpdateRequest {

    @NotBlank(message = "물품명은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "물품 설명은 필수 입력 항목입니다.")
    private String description;

    @NotNull(message = "일일 대여료는 필수 입력 항목입니다.")
    @Min(value = 0, message = "대여료는 0원 이상이어야 합니다.")
    private Integer pricePerDay;

    @NotNull(message = "카테고리 ID는 필수 입력 항목입니다.")
    private Long categoryId;
}