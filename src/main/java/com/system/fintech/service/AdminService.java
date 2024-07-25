package com.system.fintech.service;

import com.system.fintech.auth.AuthenticationManger;
import com.system.fintech.auth.JwtTokenManager;
import com.system.fintech.dto.AdminDto;
import com.system.fintech.dto.TokenUser;
import com.system.fintech.dto.request.CreateAdminRoleReq;
import com.system.fintech.dto.request.LoginRequest;
import com.system.fintech.dto.request.UpdateAdminRoleReq;
import com.system.fintech.dto.response.TokenResponse;
import com.system.fintech.enums.PermissionEnum;
import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.exception.CustomException;
import com.system.fintech.model.Admin;
import com.system.fintech.model.AdminRole;
import com.system.fintech.model.Token;
import com.system.fintech.repository.AdminRepository;
import com.system.fintech.repository.AdminRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.system.fintech.utils.Constants.InternalErrorCodes.*;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtTokenManager jwtTokenManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManger authenticationManger;
    @Autowired
    private AdminRoleRepository adminRoleRepository;

    public Admin getUser(Long userId) {
        return adminRepository.findOneById(userId);
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();

        Admin admin = adminRepository.findOneByEmail(email);
        if (admin == null) throw new CustomException(INVALID_USERNAME_OR_PASSWORD);

        boolean isPasswordValid = bCryptPasswordEncoder.matches(password, admin.getPassword());
        if (!isPasswordValid) throw new CustomException(INVALID_USERNAME_OR_PASSWORD);

        TokenUser userToken = TokenUser
                .builder()
                .userId(admin.getId())
                .userType(UserTypeEnum.ADMIN)
                .build();

        String jwtToken = jwtTokenManager.generateToken(userToken);
        Token token = Token.builder()
                .token(jwtToken)
                .userType(UserTypeEnum.ADMIN)
                .userId(admin.getId())
                .isActive(true)
                .build();
        tokenService.createToken(token);

        return new TokenResponse(jwtToken);
    }

    @Transactional
    public void create(AdminDto adminDto) {
        String encryptedPassword = bCryptPasswordEncoder.encode(adminDto.getPassword());

        validateAdminCreation(adminDto);
        Admin admin = Admin
                .builder()
                .name(adminDto.getName())
                .password(encryptedPassword)
                .email(adminDto.getEmail())
                .username(adminDto.getUsername())
                .phoneNumber(adminDto.getPhoneNumber())
                .adminRoleId(adminDto.getAdminRoleId())
                .isActive(true)
                .build();

        adminRepository.save(admin);
    }

    private void validateAdminCreation(AdminDto adminDto) {
        boolean isAdminRoleIdExists = adminRoleRepository.existsById(adminDto.getAdminRoleId());
        if (!isAdminRoleIdExists) throw new CustomException("Invalid Admin Role Id", INVALID_DATA);
    }

    public void logout() {
        String userToken = authenticationManger.getAuthenticatedToken();
        tokenService.inValidToken(userToken);
    }

    @Transactional
    public void delete(Long userId) {
        adminRepository.deleteById(userId);
        tokenService.inValidToken(userId);
    }

    public boolean update(AdminDto admin) {
        Long id = admin.getId();
        Long adminRoleId = admin.getAdminRoleId();
        String name = admin.getName();
        String username = admin.getUsername();
        String phoneNumber = admin.getPhoneNumber();
        String email = admin.getEmail();
        String password = admin.getPassword();
        Boolean isActive = true;

        int affectedRows = adminRepository.update(id, name, username, phoneNumber, email, password, adminRoleId, isActive);
        if (affectedRows != 1) {
            throw new CustomException("Couldn't Update Admin", INVALID_DATA);
        }
        return true;
    }

    public List<String> getPermissionList(Long adminRoleId) {
        return adminRoleRepository.findOneById(adminRoleId).getPermissionList();
    }

    public void createRole(CreateAdminRoleReq createAdminRoleReq) {
        String roleName = createAdminRoleReq.getRoleName();
        List<String> permissions = createAdminRoleReq.getPermissions();

        validatePermissions(permissions);

        AdminRole adminRole = AdminRole.builder()
                .name(roleName)
                .permission(permissions.toString())
                .build();
        adminRoleRepository.save(adminRole);
    }

    public void updateRole(UpdateAdminRoleReq updateAdminRoleReq) {
        Long roleId = updateAdminRoleReq.getRoleId();
        List<String> permissions = updateAdminRoleReq.getPermissions();

        validatePermissions(permissions);

        int affectedRows = adminRoleRepository.updateRolePermissions(roleId, permissions.toString());
        if (affectedRows != 1) {
            throw new CustomException("Couldn't Update Role", INVALID_DATA);
        }
    }

    private void validatePermissions(List<String> permissions) {
        if(permissions.isEmpty()){
            throw new CustomException("Invalid Permission", INVALID_PERMISSION);
        }

        Set<String> validPermissions = new HashSet<>(getPermissionList());
        permissions.forEach(permission -> {
            if (!validPermissions.contains(permission)) {
                throw new CustomException("Invalid Permission", INVALID_PERMISSION);
            }
        });
    }

    public List<String> getPermissionList() {
        return Arrays.stream(PermissionEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public List<AdminRole> getAllRole() {
        return adminRoleRepository.findAll();
    }

}
