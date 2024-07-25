package com.system.fintech.configuration;

import com.system.fintech.auth.CustomJwtDecoder;
import com.system.fintech.enums.UserTypeEnum;
import com.system.fintech.model.Admin;
import com.system.fintech.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.system.fintech.utils.Constants;
import com.system.fintech.enums.PermissionEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final CustomJwtDecoder customJwtDecoder;
    @Autowired
    private AdminService adminService;
    private static final String[] WHITE_LIST_URL_PATTERNS = {
            "/*/signUp",
            "/*/login",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(WHITE_LIST_URL_PATTERNS)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .oauth2ResourceServer(auth2 -> auth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );
        return http.build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            UserTypeEnum userType = UserTypeEnum.valueOf(jwt.getClaim(Constants.JwtClaims.USER_TYPE).toString());
            Long userId = Long.parseLong(jwt.getClaim(Constants.JwtClaims.USER_ID).toString());

            List<String> permissionList = new ArrayList<>();
            if (userType == UserTypeEnum.ADMIN) {
                Admin admin = adminService.getUser(userId);
                permissionList = adminService.getPermissionList(admin.getAdminRoleId());
            }

            List<PermissionEnum> permissions = permissionList.stream().map(PermissionEnum::valueOf).toList();
            Collection<GrantedAuthority> authorities = permissions.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                    .collect(Collectors.toList());

            authorities.add(new SimpleGrantedAuthority("ROLE_" + userType.name()));
            return authorities;
        });

        return converter;
    }

}
