package com.everrent.domain.item.repository;

import com.everrent.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 전체 물품 목록 조회 (ID 기준 내림차순 / 최신순)
    List<Item> findAllByOrderByIdDesc();

    // 내가 등록한 물품 조회 (ID 기준 내림차순 / 최신순)
    List<Item> findByOwnerIdOrderByIdDesc(Long ownerId);
}