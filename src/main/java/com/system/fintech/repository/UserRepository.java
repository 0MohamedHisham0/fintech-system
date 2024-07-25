package com.system.fintech.repository;

import com.system.fintech.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneById(Long id);

    User findOneByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE "user"
            SET name = :name,
                username = :username,
                phone_number = :phoneNumber,
                email = :email,
                password = :password,
                is_active = :isActive
            WHERE id = :id
            """
            , nativeQuery = true)
    int update(@Param("id") Long id,
               @Param("name") String name,
               @Param("username") String username,
               @Param("phoneNumber") String phoneNumber,
               @Param("email") String email,
               @Param("password") String password,
               @Param("isActive") Boolean isActive
    );

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE "user"
            SET balance = :newBalance
            WHERE id = :id
            AND balance = :oldBalance
            """
            , nativeQuery = true)
    int update(
            @Param("id") Long userId,
            @Param("newBalance") BigDecimal newBalance,
            @Param("oldBalance") BigDecimal oldBalance
    );


}
