package com.system.fintech.repository;

import com.system.fintech.model.Admin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findOneById(long id);

    Admin findOneByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE admin
            SET name = :name,
                username = :username,
                phone_number = :phoneNumber,
                email = :email,
                password = :password,
                adminRoleId = :adminRoleId,
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
               @Param("adminRoleId") Long adminRoleId,
               @Param("isActive") Boolean isActive
    );
}
