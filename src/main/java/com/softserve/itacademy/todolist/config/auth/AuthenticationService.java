package com.softserve.itacademy.todolist.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.todolist.config.authDto.AuthenticationResponse;
import com.softserve.itacademy.todolist.dto.user.UserRequestDto;
import com.softserve.itacademy.todolist.model.Role;
import com.softserve.itacademy.todolist.model.Token;
import com.softserve.itacademy.todolist.model.TokenType;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.repository.RoleRepository;
import com.softserve.itacademy.todolist.repository.TokenRepository;
import com.softserve.itacademy.todolist.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    public AuthenticationResponse authenticate(UserRequestDto userRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.getEmail(), userRequestDto.getPassword()));

        if (! authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessTokenFromUserName(user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());
        Optional<Token> validTokenByUserId = tokenRepository.findValidTokenByUserId(user.getId());
        if (validTokenByUserId.isEmpty()) {
            saveUserToken(user, accessToken);

            return AuthenticationResponse.builder()
                    .username(user.getUsername())
                    .message("Your access token was returned")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .message("Your token has expired! You should refresh your token to get access to resources ")
                .refreshToken(refreshToken)
                .build();

    }

    public AuthenticationResponse register(UserRequestDto userRequestDto) {
        Role role = roleRepository.findByName("USER");
        User user = User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .message("New user has been registered. You have to log in to get access to resources!")
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .name(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authorizationHeader == null || ! authorizationHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authorizationHeader.substring(7);
        userEmail = jwtUtils.getSubjectFromToken(refreshToken);

        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtUtils.validateToken(refreshToken)) {
            revokeUserToken(user);
            var authenticationResponse = AuthenticationResponse.builder()
                    .username(user.getUsername())
                    .message("Your token was refreshed. You have to log in to get new access token")
                    .build();

            new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
        }

    }

    private void revokeUserToken(User user) {
        var validUserToken = tokenRepository.findValidTokenByUserId(user.getId());
        if (validUserToken.isEmpty())
            return;
        validUserToken.ifPresent(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.save(validUserToken.get());
    }

}
