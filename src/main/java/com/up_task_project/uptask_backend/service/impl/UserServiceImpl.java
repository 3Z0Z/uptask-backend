package com.up_task_project.uptask_backend.service.impl;

import com.up_task_project.uptask_backend.config.JwtService;
import com.up_task_project.uptask_backend.config.MailManager;
import com.up_task_project.uptask_backend.dto.request.user.LoginUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RegisterUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RequestCodeDTO;
import com.up_task_project.uptask_backend.dto.request.user.ValidateCodeDTO;
import com.up_task_project.uptask_backend.dto.request.user.ChangePasswordDTO;
import com.up_task_project.uptask_backend.dto.response.TokenJwtDTO;
import com.up_task_project.uptask_backend.dto.response.UserDTO;
import com.up_task_project.uptask_backend.exception.exceptions.NoCookiesFoundException;
import com.up_task_project.uptask_backend.exception.exceptions.TokenNotFoundOrExpiredException;
import com.up_task_project.uptask_backend.exception.exceptions.UserAlreadyRegisterException;
import com.up_task_project.uptask_backend.exception.exceptions.UserNotFoundException;
import com.up_task_project.uptask_backend.model.Session;
import com.up_task_project.uptask_backend.model.Token;
import com.up_task_project.uptask_backend.model.User;
import com.up_task_project.uptask_backend.repository.SessionRepository;
import com.up_task_project.uptask_backend.repository.TokenRepository;
import com.up_task_project.uptask_backend.repository.UserRepository;
import com.up_task_project.uptask_backend.service.UserService;
import com.up_task_project.uptask_backend.service.responses.JwtTokens;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final SessionRepository sessionRepository;

    private final PasswordEncoder passwordEncoder;
    private final MailManager mailManager;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDTO registerUser(RegisterUserDTO request) {
        boolean userExist = this.userRepository.existsByEmailOrUsername(request.email(), request.username());
        if (userExist) throw new UserAlreadyRegisterException("User already exist");
        User newUser = User.builder()
            .username(request.username().trim())
            .email(request.email().trim().toLowerCase())
            .password(this.passwordEncoder.encode(request.password()))
            .isEmailConfirmed(false)
            .isAbleChangePassword(false)
            .build();
        newUser = this.userRepository.save(newUser);
        String tokenCode = this.generateCode();
        Token newToken = Token.builder().userId(newUser.get_id()).token(tokenCode).build();
        this.tokenRepository.save(newToken);
        this.sendEmailForConfirmation(
            newUser.getEmail(),
            newUser.getUsername(),
            newUser.get_id(),
            tokenCode,
            "Confirm Email :: UpTask",
            "confirmEmail"
        );
        log.info("User {} created", newUser.getUsername());
        return UserDTO.builder()
            .id(newUser.get_id())
            .username(newUser.getUsername())
            .email(newUser.getEmail())
            .createdAt(newUser.getCreateAt())
            .build();
    }

    @Override
    public void validateEmail(ValidateCodeDTO request) {
        User user = this.findUserById(request.userId());
        Token token = this.tokenRepository.findByUserId(request.userId())
            .orElseThrow(() -> new TokenNotFoundOrExpiredException("Incorrect token or expired"));
        if (!token.getToken().equals(request.token())) throw new TokenNotFoundOrExpiredException("Incorrect token or expired");
        user.setEmailConfirmed(true);
        this.userRepository.save(user);
        this.tokenRepository.delete(token);
    }

    @Override
    public JwtTokens login(LoginUserDTO request) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = this.findUserByUsername(request.username());
        if (!user.isEmailConfirmed()) {
            boolean existToken = this.tokenRepository.existsByUserId(user.get_id());
            if (existToken) throw new TokenNotFoundOrExpiredException("Please use the code sended or wait until it expires to get a new one");
            Token newToken = Token.builder().userId(user.get_id()).token(this.generateCode()).build();
            this.tokenRepository.save(newToken);
            this.sendEmailForConfirmation(
                user.getEmail(),
                user.getUsername(),
                user.get_id(),
                newToken.getToken(),
                "Confirm Email :: UpTask",
                "confirmEmail"
            );
            throw new TokenNotFoundOrExpiredException("Account not confirmed, we have send you a new code to your email");
        }
        String refreshToken = this.jwtService.generateToken(user, true);
        String tokenJwt = this.jwtService.generateToken(user, false);
        Session newSession = Session.builder()
            .userId(user.get_id())
            .refreshToken(refreshToken)
            .refreshTokenRevoked(false)
            .build();
        this.sessionRepository.save(newSession);
        return JwtTokens.builder().refreshToken(refreshToken).token(tokenJwt).build();
    }

    @Override
    public TokenJwtDTO refreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new NoCookiesFoundException("No cookies found in the request");
        Cookie cookieRefreshToken = Arrays.stream(cookies).filter(cookie -> "refreshTokenUpTask".equals(cookie.getName())).findFirst()
            .orElseThrow(() -> new NoCookiesFoundException("No refresh cookie found on request"));
        String refreshToken = cookieRefreshToken.getValue();
        if (!this.jwtService.isTokenNonExpired(refreshToken)) throw new TokenNotFoundOrExpiredException("Refresh token is expired");
        Session session = this.sessionRepository.findByRefreshToken(refreshToken).filter(t -> !t.isRefreshTokenRevoked())
            .orElseThrow(() -> new TokenNotFoundOrExpiredException("Token not found"));
        User user = this.findUserById(session.getUserId());
        return new TokenJwtDTO(this.jwtService.generateToken(user, false));
    }

    @Override
    public void requestCode(RequestCodeDTO request, boolean isForgotPassword) {
        Optional<User> user = this.userRepository.findByEmail(request.email());
        if (user.isEmpty() || !user.get().isEmailConfirmed()) return;
        this.tokenRepository.findByUserId(user.get().get_id()).ifPresent(this.tokenRepository::delete);
        Token newToken = Token.builder()
            .userId(user.get().get_id())
            .token(this.generateCode())
            .build();
        this.tokenRepository.save(newToken);
        this.sendEmailForConfirmation(
            user.get().getEmail(),
            user.get().getUsername(),
            user.get().get_id(),
            newToken.getToken(),
            "Reset Password :: UpTask",
            isForgotPassword ? "resetPassword" : "confirmEmail"
        );
    }

    @Override
    public void validateCodeForChangePassword(ValidateCodeDTO request) {
        User user = this.findUserById(request.userId());
        Token token = this.tokenRepository.findByUserId(request.userId())
            .orElseThrow(() -> new TokenNotFoundOrExpiredException("Incorrect token or expired"));
        if (!token.getToken().equals(request.token())) throw new TokenNotFoundOrExpiredException("Incorrect token or expired");
        if (user.isAbleChangePassword()) return;
        user.setAbleChangePassword(true);
        this.userRepository.save(user);
        this.tokenRepository.delete(token);
    }

    @Override
    public void changePassword(ChangePasswordDTO request) {
        User user = this.findUserById(request.userId());
        if (!user.isAbleChangePassword()) return;
        user.setPassword(this.passwordEncoder.encode(request.password()));
        user.setAbleChangePassword(false);
        this.userRepository.save(user);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int token = random.nextInt(900000) + 100000;
        return String.valueOf(token);
    }

    private User findUserById(String userId) {
        return this.userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private User findUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private void sendEmailForConfirmation(String email, String username, String userId, String code, String subject, String template) {
        Map<String, Object> emailValues = new HashMap<>();
        emailValues.put("username", username);
        emailValues.put("code", code);
        emailValues.put("userId", userId);
        this.mailManager.sendMessage(subject, email, emailValues, template);
    }

}
