package com.system.fintech.service;

import com.system.fintech.auth.AuthenticationManger;
import com.system.fintech.auth.JwtTokenManager;
import com.system.fintech.dto.TokenUser;
import com.system.fintech.dto.UserDto;
import com.system.fintech.dto.request.LoginRequest;
import com.system.fintech.dto.response.TokenResponse;
import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.exception.CustomException;
import com.system.fintech.model.Token;
import com.system.fintech.model.User;
import com.system.fintech.repository.UserRepository;
import com.system.fintech.utils.Constants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.system.fintech.utils.Constants.InternalErrorCodes.INVALID_USERNAME_OR_PASSWORD;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtTokenManager jwtTokenManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManger authenticationManger;

    public User getUser(Long userId) {
        return userRepository.findOneById(userId);
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();

        User user = userRepository.findOneByEmail(email);
        if (user == null) throw new CustomException(INVALID_USERNAME_OR_PASSWORD);

        boolean isPasswordValid = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!isPasswordValid) throw new CustomException(INVALID_USERNAME_OR_PASSWORD);

        TokenUser userToken = TokenUser
                .builder()
                .userId(user.getId())
                .userType(UserTypeEnum.USER)
                .build();

        String jwtToken = jwtTokenManager.generateToken(userToken);
        Token token = Token.builder()
                .token(jwtToken)
                .userType(UserTypeEnum.USER)
                .userId(user.getId())
                .isActive(true)
                .build();
        tokenService.createToken(token);

        return new TokenResponse(jwtToken);
    }

    @Transactional
    public TokenResponse signUp(UserDto userDto) {
        String encryptedPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

        User user = User
                .builder()
                .name(userDto.getName())
                .password(encryptedPassword)
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .phoneNumber(userDto.getPhoneNumber())
                .isActive(true)
                .build();

        userRepository.save(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();

        return login(loginRequest);
    }

    public void create(UserDto userDto) {
        String encryptedPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

        User user = User
                .builder()
                .name(userDto.getName())
                .password(encryptedPassword)
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .phoneNumber(userDto.getPhoneNumber())
                .isActive(true)
                .build();
        userRepository.save(user);
    }

    public void logout() {
        String userToken = authenticationManger.getAuthenticatedToken();
        tokenService.inValidToken(userToken);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
        tokenService.inValidToken(userId);
    }

    public boolean update(UserDto userDto) {
        String encryptedPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

        Long id = userDto.getId();
        String name = userDto.getName();
        String username = userDto.getUsername();
        String phoneNumber = userDto.getPhoneNumber();
        String email = userDto.getEmail();
        Boolean isActive = true;

        int affectedRows = userRepository.update(id, name, username, phoneNumber, email, encryptedPassword, isActive);
        if (affectedRows != 1) {
            throw new CustomException("Couldn't Update User", Constants.InternalErrorCodes.INVALID_DATA);
        }
        return true;
    }
    public BigDecimal checkBalance() {
        TokenUser user = authenticationManger.getAuthenticatedUserData();

        Optional<User> userOptional = userRepository.findById(user.getUserId());
        return userOptional.map(User::getBalance).orElse(BigDecimal.ZERO);
    }
}
