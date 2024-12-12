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

        // ERROR
        Token token = tokenRepository.findAllValidTokenByUserId(user.getId()).getFirst();
        if (token == null) {
            log.error("Token has been expired");
            throw new BadCredentialsException("Your access token might be expired!\n You should refresh token!!!");
        }

        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

        return new AuthenticationResponse(user.getUsername(), token.getName(), refreshToken);

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

        String jwtToken = jwtUtils.generateAccessTokenFromUserName(user.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());
        saveUserToken(user, jwtToken);
        return new AuthenticationResponse(user.getUsername(), jwtToken, refreshToken);
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
        if (authorizationHeader == null) {
            return;
        }
        refreshToken = authorizationHeader.substring(7);
        userEmail = jwtUtils.getSubjectFromToken(refreshToken);

        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtUtils.validateToken(refreshToken)) {
            String token = jwtUtils.generateAccessTokenFromUserName(user.getUsername());
            revokeAllUserTokens(user);
            saveUserToken(user, token);
            var authenticationResponse = new AuthenticationResponse(
                    userEmail, token, refreshToken);
            new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
        }


    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
                    t.setExpired(true);
                    t.setRevoked(true);
                }
        );
        tokenRepository.saveAll(validUserTokens);
    }
}
