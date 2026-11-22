package com.everrent.domain.user.repository;

import com.everrent.domain.user.entity.User; // [Backend Lead와 협의 필요] 임시 생성입니다.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}