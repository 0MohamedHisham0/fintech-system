package com.system.fintech.controller;


import com.system.fintech.dto.AdminDto;
import com.system.fintech.dto.request.CreateAdminRoleReq;
import com.system.fintech.dto.request.LoginRequest;
import com.system.fintech.dto.request.UpdateAdminRoleReq;
import com.system.fintech.dto.response.TokenResponse;
import com.system.fintech.model.AdminRole;
import com.system.fintech.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = adminService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TokenResponse> logout() {
        adminService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> create(
            @RequestBody AdminDto admin
    ) {
        adminService.create(admin);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/delete/{adminId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long adminId) {
        adminService.delete(adminId);
        return ResponseEntity.ok().body("Deleted Successfully");
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> update(@RequestBody AdminDto adminDto) {
        boolean updated = adminService.update(adminDto);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/createRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createRole(@RequestBody CreateAdminRoleReq createAdminRoleReq) {
        adminService.createRole(createAdminRoleReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/updateRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateRole(@RequestBody UpdateAdminRoleReq updateAdminRoleReq) {
        adminService.updateRole(updateAdminRoleReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getPermissionList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<String>> getPermissionList() {
        return ResponseEntity.ok(adminService.getPermissionList());
    }

    @GetMapping("/getAllRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AdminRole>> getAllRole() {
        return ResponseEntity.ok(adminService.getAllRole());
    }

}
