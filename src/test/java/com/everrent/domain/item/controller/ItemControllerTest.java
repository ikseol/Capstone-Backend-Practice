package com.everrent.domain.item.controller;

import com.everrent.domain.category.entity.Category;
import com.everrent.domain.category.repository.CategoryRepository;
import com.everrent.domain.item.entity.Item;
import com.everrent.domain.item.repository.ItemRepository;
import com.everrent.domain.user.entity.User;
import com.everrent.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;


    Category category;
    User owner;
    @BeforeEach
    void setUp() {
        category = categoryRepository.save(Category.builder()
                .name("장비")
                .build());

        owner = userRepository.save(User.builder()
                .username("1")
                .password("1234")
                .nickname("닉네임1")
                .phone("010-0000-0000")
                .build());
    }

    @Test
    @DisplayName("CRUD 테스트 - 전체 물품 조회")
    @Transactional
    void findAllItems() throws Exception {
        // 1. Given 준비
        Item item = Item.builder()
                .title("캠핑 텐트")
                .pricePerDay(50000)
                .description("상태 좋은 텐트 대여합니다.")
                .owner(owner)
                .category(category)
                .build();

        Item item2 = Item.builder()
                .title("가스 버너")
                .pricePerDay(20000)
                .description("상태 좋은 가스 버너 대여합니다.")
                .owner(owner)
                .category(category)
                .build();

        Item savedItem = itemRepository.save(item);
        Item savedItem2 = itemRepository.save(item2);

        // 2. When 실행
        ResultActions result = mockMvc.perform(get("/api/items"));

        // 3. then 검증
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].title").value(
                        org.hamcrest.Matchers.containsInAnyOrder("캠핑 텐트", "가스 버너")
                ));
    }


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