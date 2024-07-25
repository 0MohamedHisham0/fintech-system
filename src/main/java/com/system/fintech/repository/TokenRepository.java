package com.system.fintech.repository;


import com.system.fintech.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findById(long id);

    @Query(value = " SELECT * FROM token WHERE token = :tokenValue AND is_active = TRUE AND user_type = :userType ", nativeQuery = true)
    Token findValidToken(@Param("tokenValue") String token, @Param("userType") String userType);

    @Transactional
    @Modifying
    @Query(value = " UPDATE token SET is_active = FALSE where token = :tokenValue ; ", nativeQuery = true)
    void inValidToken(@Param("tokenValue") String token);

    @Transactional
    @Modifying
    @Query(value = " UPDATE token SET is_active = FALSE where user_id = :userId ; ", nativeQuery = true)
    void inValidToken(@Param("userId") long userId);

}
