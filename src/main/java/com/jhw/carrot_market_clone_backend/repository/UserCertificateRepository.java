package com.jhw.carrot_market_clone_backend.repository;

import com.jhw.carrot_market_clone_backend.model.user.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCertificateRepository extends JpaRepository<UserCertificate, String> {

    @Query("select u from user_certificate u where u.id = :user_id")
    Optional<UserCertificate> findByUserId(@Param("user_id") String user_id);

    @Query("select u.id from user_certificate u where u.uid = :user_uid")
    Optional<String> findIdByUid(@Param("user_uid") int userUid);

    @Query("select u.uid from user_certificate u where u.id = :user_id")
    Integer findUidByUserId(@Param("user_id") String userId);
}
