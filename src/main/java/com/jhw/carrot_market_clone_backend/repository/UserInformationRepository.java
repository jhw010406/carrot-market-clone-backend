package com.jhw.carrot_market_clone_backend.repository;

import com.jhw.carrot_market_clone_backend.model.user.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<T, ID> : 영속성 컨텍스트에 등록될 엔티티의 자료형(T)과 해당 엔티티의 기본 키 자료형(ID)
public interface UserInformationRepository extends JpaRepository<UserInformation, String> {
}
