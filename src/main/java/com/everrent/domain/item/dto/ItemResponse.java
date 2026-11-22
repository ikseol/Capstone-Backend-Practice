package com.everrent.domain.item.dto;

import com.everrent.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ItemResponse {

    private Long id;
    private String title;
    private String description;
    private Integer pricePerDay;
    private Boolean isAvailable;
    private Long ownerId; // Backend Lead와 협의 필요
    private String ownerNickname; // Backend Lead와 협의 필요
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .pricePerDay(item.getPricePerDay())
                .isAvailable(item.getIsAvailable())
                .ownerId(item.getOwner().getId())
                .ownerNickname(item.getOwner().getNickname())
                .categoryId(item.getCategory().getId())
                .categoryName(item.getCategory().getName())
                .createdAt(item.getCreatedAt())
                .build();
    }
}