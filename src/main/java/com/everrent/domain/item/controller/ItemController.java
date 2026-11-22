package com.everrent.domain.item.controller;

import com.everrent.domain.item.dto.ItemCreateRequest;
import com.everrent.domain.item.dto.ItemResponse;
import com.everrent.domain.item.dto.ItemUpdateRequest;
import com.everrent.domain.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * 1. 물품 등록 API
     * POST /api/items
     */
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @Valid @RequestBody ItemCreateRequest request,
            @RequestHeader("X-User-Id") Long userId // [Backend Lead와 협의 필요] 인증 토큰으로 대체 예정
    ) {
        ItemResponse response = itemService.createItem(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 2. 전체 물품 목록 조회 API
     * GET /api/items
     */
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> response = itemService.getAllItems();
        return ResponseEntity.ok(response);
    }

    /**
     * 3. 물품 상세 조회 API
     * GET /api/items/{itemId}
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId) {
        ItemResponse response = itemService.getItemById(itemId);
        return ResponseEntity.ok(response);
    }

    /**
     * 4. 내가 등록한 물품 조회 API
     * GET /api/items/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<ItemResponse>> getMyItems(
            @RequestHeader("X-User-Id") Long userId // [Backend Lead와 협의 필요] 인증 토큰으로 대체 예정
    ) {
        List<ItemResponse> response = itemService.getMyItems(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 5. 물품 수정 API
     * PUT /api/items/{itemId}
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemUpdateRequest request,
            @RequestHeader("X-User-Id") Long userId // [Backend Lead와 협의 필요] 인증 토큰으로 대체 예정
    ) {
        ItemResponse response = itemService.updateItem(itemId, request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 6. 물품 삭제 API
     * DELETE /api/items/{itemId}
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long itemId,
            @RequestHeader("X-User-Id") Long userId // [Backend Lead와 협의 필요] 인증 토큰으로 대체 예정
    ) {
        itemService.deleteItem(itemId, userId);
        return ResponseEntity.noContent().build();
    }
}