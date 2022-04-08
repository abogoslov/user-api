package ru.bogoslov.userapi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bogoslov.userapi.config.JwtConfig;
import ru.bogoslov.userapi.controller.request.UserAuthRequest;
import ru.bogoslov.userapi.controller.request.UserRegisterRequest;
import ru.bogoslov.userapi.controller.response.TokenPairResponse;
import ru.bogoslov.userapi.entity.Role;
import ru.bogoslov.userapi.entity.User;
import ru.bogoslov.userapi.exception.BusinessException;
import ru.bogoslov.userapi.mapper.UserMapper;
import ru.bogoslov.userapi.model.RequestContext;
import ru.bogoslov.userapi.model.TokenScope;
import ru.bogoslov.userapi.repository.UserRepository;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final RequestContext requestContext;

    public UserController(JwtConfig jwtConfig, UserRepository userRepository, RequestContext requestContext) {
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
        this.requestContext = requestContext;
    }

    @PostMapping
    @ApiOperation(value = "User registration")
    public ResponseEntity<TokenPairResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new BusinessException("Password confirmation failed");
        }

        userRepository.saveAndFlush(UserMapper.mapper.userRegisterRequestToUser(request));

        return ResponseEntity.ok(
                new TokenPairResponse(
                        generateJwt(jwtConfig.getAccessTokenExpirationTime(), TokenScope.REFRESH, request.getRole()),
                        generateJwt(jwtConfig.getRefreshTokenExpirationTime(), TokenScope.REFRESH, request.getRole())
                )
        );
    }

    @PostMapping("/authenticate")
    @ApiOperation(value = "User authentication")
    public ResponseEntity<TokenPairResponse> authenticate(@Valid @RequestBody UserAuthRequest request) {
        final User user = userRepository.findDistinctByLoginAndPassword(request.getLogin(), request.getPassword());

        if (user == null) {
            throw new BusinessException("Incorrect login or password");
        }

        final Role userRole = user.getUserRole();
        final String roleName = userRole.getName();

        return ResponseEntity.ok(
                new TokenPairResponse(
                        generateJwt(jwtConfig.getAccessTokenExpirationTime(), TokenScope.REFRESH, roleName),
                        generateJwt(jwtConfig.getRefreshTokenExpirationTime(), TokenScope.REFRESH, roleName)
                )
        );
    }

    @PostMapping("/refreshToken")
    @ApiOperation(value = "New JWT-pair generation")
    @ApiImplicitParam(
            name = "x-api-role", allowableValues = "user, admin, courier", paramType = "header", required = true
    )
    public ResponseEntity<TokenPairResponse> refreshToken() {
        return ResponseEntity.ok(
                new TokenPairResponse(
                        generateJwt(
                                jwtConfig.getAccessTokenExpirationTime(), TokenScope.ACCESS, requestContext.getRole()
                        ),
                        generateJwt(
                                jwtConfig.getRefreshTokenExpirationTime(), TokenScope.REFRESH, requestContext.getRole()
                        )
                )
        );
    }

    private String generateJwt(Integer exp, TokenScope tokenScope, String role) {
        final Instant expInstant = LocalDateTime
                .now()
                .plusMinutes(exp)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return JWT
                .create()
                .withClaim("role", role)
                .withClaim("scope", tokenScope.toString())
                .withExpiresAt(Date.from(expInstant))
                .withIssuer(jwtConfig.getTokenIssuer())
                .sign(Algorithm.HMAC256(jwtConfig.getTokenSigningKey()));
    }
}
