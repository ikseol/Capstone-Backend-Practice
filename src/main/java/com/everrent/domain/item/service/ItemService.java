package com.everrent.domain.item.service;

import com.everrent.domain.category.entity.Category;
import com.everrent.domain.category.repository.CategoryRepository;
import com.everrent.domain.item.dto.ItemCreateRequest;
import com.everrent.domain.item.dto.ItemResponse;
import com.everrent.domain.item.dto.ItemUpdateRequest;
import com.everrent.domain.item.entity.Item;
import com.everrent.domain.item.repository.ItemRepository;
import com.everrent.domain.user.entity.User;
import com.everrent.domain.user.repository.UserRepository; // [Backend Lead와 협의 필요] 김민석 담당 영역
import com.everrent.global.exception.OwnerCustomException;
import com.everrent.global.exception.temp.TempCustomException;
import com.everrent.global.exception.temp.TempErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository; // [Backend Lead와 협의 필요]

    /**
     * 1. 물품 등록
     */
    @Transactional
    public ItemResponse createItem(ItemCreateRequest request, Long userId) {
        User owner = userRepository.findById(userId).get(); // find는 옵셔널 객체로 받기떄문에 get()사용, 데이터는 무조건 있다고 가정
//                .orElseThrow(() -> new OwnerCustomException(TempErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new TempCustomException(TempErrorCode.CATEGORY_NOT_FOUND));

        Item item = Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .pricePerDay(request.getPricePerDay())
                .isAvailable(true)
                .owner(owner)
                .category(category)
                .build();

        Item savedItem = itemRepository.save(item);
        return ItemResponse.from(savedItem);
    }

    /**
     * 2. 전체 물품 목록 조회
     */
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAllByOrderByIdDesc().stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 3. 물품 상세 조회
     */
    public ItemResponse getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new TempCustomException(TempErrorCode.ITEM_NOT_FOUND));
        return ItemResponse.from(item);
    }

    /**
     * 4. 내가 등록한 물품 조회
     */
    public List<ItemResponse> getMyItems(Long userId) {
        return itemRepository.findByOwnerIdOrderByIdDesc(userId).stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 5. 물품 수정
     */
    @Transactional
    public ItemResponse updateItem(Long itemId, ItemUpdateRequest request, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new TempCustomException(TempErrorCode.ITEM_NOT_FOUND));

        // 소유자 검증 (자신의 물품만 수정 가능)
        if (!item.getOwner().getId().equals(userId)) {
            // 기존 코드 : 소유자가 다르면 런타임에러 발생시켜서 종료시킴
            // throw new RuntimeException("소유자가 다릅니다");
            throw new OwnerCustomException("현재 사용자와 소유자가 다릅니다.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new TempCustomException(TempErrorCode.CATEGORY_NOT_FOUND));

        item.update(request.getTitle(), request.getDescription(), request.getPricePerDay(), category);
        return ItemResponse.from(item);
    }

    /**
     * 6. 물품 삭제
     */
    @Transactional
    public void deleteItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new TempCustomException(TempErrorCode.ITEM_NOT_FOUND));

        // 소유자 검증 (자신의 물품만 삭제 가능)[cite: 1]
        if (!item.getOwner().getId().equals(userId)) {
//            throw new OwnerCustomException(TempErrorCode.UNAUTHORIZED_ACTION);
            throw new RuntimeException("소유가자 다릅니다"); // 소유자가 다르면 런타임에러 발생시켜서 종료시킴
        }

        // [Backend Lead와 협의 필요] 대여 중인 물품은 삭제할 수 없는 검증 로직 추가 예정 (Rental 연동)[cite: 1]
        // if (rentalRepository.existsByItemAndStatus(item, RentalStatus.RENTED)) {
        //     throw new OwnerCustomException(TempErrorCode.ITEM_IS_RENTED);
        // }

        itemRepository.delete(item);
    }
}