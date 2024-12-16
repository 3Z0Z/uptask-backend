package com.up_task_project.uptask_backend.service.impl;

import com.up_task_project.uptask_backend.config.JwtService;
import com.up_task_project.uptask_backend.model.Session;
import com.up_task_project.uptask_backend.model.User;
import com.up_task_project.uptask_backend.repository.SessionRepository;
import com.up_task_project.uptask_backend.repository.UserRepository;
import com.up_task_project.uptask_backend.service.LogoutUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogoutUserServiceImpl implements LogoutUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username = this.jwtService.extractUsernameFromRequest(request);
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) return;
        Session storedSession = this.sessionRepository.findByUserId(user.get().get_id()).orElse(null);
        if (storedSession != null) {
            storedSession.setRefreshTokenRevoked(true);
            this.sessionRepository.save(storedSession);
            SecurityContextHolder.clearContext();
        }
    }

}
