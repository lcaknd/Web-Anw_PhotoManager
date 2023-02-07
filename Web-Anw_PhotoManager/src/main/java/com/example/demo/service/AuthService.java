package com.example.demo.service;

import com.example.demo.config.jwt.JwtUtils;
import com.example.demo.config.service.impl.UserDetailsImpl;
import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public JwtResponse authenticateUser(LoginRequest request) {
        Authentication authentication =
              authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles =
              userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
    }

    public ResponseEntity<?> registerUser(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                  .body(new MessageResponse("ERROR: Username is already exist!"));
        }

        User user = new User(request.getUsername(), encoder.encode(request.getPassword()));
        Set<Role> roles = new HashSet<>();

        if (request.getRole() == null) {
            Role userRole =
                  roleRepository
                        .findByName(ERole.USER)
                        .orElseThrow(() -> new RuntimeException("ERROR: Role is not found"));
            roles.add(userRole);
        } else {
            request
                  .getRole()
                  .forEach(
                        role -> {
                            if ("admin".equals(role)) {
                                Role adminRole =
                                      roleRepository
                                            .findByName(ERole.ADMIN)
                                            .orElseThrow(() -> new RuntimeException("ERROR: Role is not found"));
                                roles.add(adminRole);
                            } else {
                                Role userRole =
                                      roleRepository
                                            .findByName(ERole.USER)
                                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                                roles.add(userRole);
                            }
                        });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
