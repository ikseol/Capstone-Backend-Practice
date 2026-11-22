package com.everrent.domain.item.service;

import com.everrent.domain.category.entity.Category;
import com.everrent.domain.category.repository.CategoryRepository;
import com.everrent.domain.item.entity.Item;
import com.everrent.domain.item.repository.ItemRepository;
import com.everrent.domain.user.entity.User;
import com.everrent.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("더티 체크 테스트")
    @Transactional
    void dirtyCheckingTest() {
        // 1. Given : 데이터 준비
        // 테스트용 유저의 정보를 생성
        User owner = User.builder()
                .username("testUser")
                .password("password123")
                .nickname("테스트닉네임")
                .phone("010-1234-5678")
                .build();
        // 테스트용 유저의 정보를 저장
        User savedUser = userRepository.save(owner);

        // 테스트용 카테고리 정보를 생성, 저장
        Category category = Category.builder()
                .name("캠핑장비카테고리")
                .build();
        Category savedCategory = categoryRepository.save(category);

        // 위에서 생성한 테스트용 유저 + 카테고리와 함께 테스트용 물품 정보를 생성 + 저장
        Item item = Item.builder()
                .title("캠핑 텐트 대여")
                .description("상태 좋은 감성 텐트입니다.")
                .pricePerDay(25000)
                .isAvailable(true)
                .owner(savedUser)
                .category(savedCategory)
                .build();
        Item savedItem = itemRepository.save(item);


        // 2. When: 조회 후, Item의 update 비즈니스 메서드 호출 (itemRepository.save() 호출 안 함!)
        Item findItem = itemRepository.findById(savedItem.getId()).orElseThrow();

        // update 메서드 활용해 값 변경, 제목을 수정된 텐트 제목으로 변경, 일일대여금액을 30000으로 변경
        findItem.update("수정된 텐트 제목", "설명도 수정 가능", 30000, savedCategory);

        // 트랜잭션 커밋 시점에 쿼리가 나가도록 강제로 flush 호출 (콘솔 로그 확인용) itemRepository.save() 미실행.
        itemRepository.flush();


    }
}