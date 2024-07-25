package com.system.fintech.repository;

import com.system.fintech.model.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, Long> {
    AdminRole findOneById(long id);

    List<AdminRole> findAll();

    @Transactional
    @Modifying
    @Query(value = " UPDATE admin_role SET permission = :permission where id = :roleId ; ", nativeQuery = true)
    int updateRolePermissions(@Param("roleId") Long roleId, @Param("permission") String permission);

}
