package com.everrent.domain.item.controller;

import com.everrent.domain.category.entity.Category;
import com.everrent.domain.category.repository.CategoryRepository;
import com.everrent.domain.item.entity.Item;
import com.everrent.domain.item.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("더티 체킹(변경 감지) 테스트")
    @Transactional
        // 테스트 메서드 내에서 트랜잭션을 유지해 줌
    void dirtyCheckingTest() {
        // 1. Given: 테스트용 물품을 DB에 저장
        Item item = Item.builder()
                .title("캠핑 텐트")
                .pricePerDay(50000)
                .description("상태 좋은 텐트 대여합니다.")
                .build();
        Item savedItem = itemRepository.save(item);

        // 테스트용 카테고리 정보를 생성, 저장
        Category category = Category.builder()
                .name("캠핑장비카테고리")
                .build();
        Category savedCategory = categoryRepository.save(category);

        // 2. When: ID로 엔티티를 조회한 후, setter나 변경 메서드로 값만 수정
        // (주의: itemRepository.save()를 호출하지 않습니다!)
        Item findItem = itemRepository.findById(savedItem.getId()).orElseThrow();

        // 엔티티 내부의 값 변경 (예시 메서드 또는 setter)
        findItem.update("감성 캠핑 텐트(수정)", "셜명 수정", 3000, savedCategory);

        // 트랜잭션 커밋 시점에 쿼리가 나가도록 강제로 flush 호출 (콘솔 로그 확인용)
        itemRepository.flush();

        // 3. Then: 값이 정상적으로 변경되었는지 단언(Assertion)
        Item updatedItem = itemRepository.findById(savedItem.getId()).orElseThrow();
        assertThat(updatedItem.getTitle()).isEqualTo("감성 캠핑 텐트(수정)");
        assertThat(updatedItem.getPricePerDay()).isEqualTo(60000);
    }
}