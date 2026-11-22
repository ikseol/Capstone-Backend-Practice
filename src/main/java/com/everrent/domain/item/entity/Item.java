package com.everrent.domain.item.entity;

import com.everrent.domain.category.entity.Category;
import com.everrent.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer pricePerDay;

    @Column(nullable = false)
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Item(String title, String description, Integer pricePerDay, Boolean isAvailable, User owner, Category category) {
        this.title = title;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.isAvailable = (isAvailable == null) || isAvailable;
        this.owner = owner;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    // 물품 정보 수정 비즈니스 메서드 추가
    public void update(String title, String description, Integer pricePerDay, Category category) {
        this.title = title;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.category = category;
    }

    public void updateAvailability(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    }