package com.example.jwt_token.service;

import com.example.jwt_token.exception.CustomException;
import com.example.jwt_token.model.LoginResponse;
import com.example.jwt_token.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User saveUser(User user) throws CustomException;
    public List<User> getAllUsers() throws CustomException;
    public Optional<User> getById(String userId);
    public Optional<User> deleteUser(String userId);
    public User updateUser(User user);

    User assigningRole(String email, String roleName);

    String verify(String email, String password, HttpSession httpSession);

    List<User> getAllEmployees();

    List<User> getAllAdmins();

    String sendEmail(String to, String subject, String text);

    String sendEmailWithFiles(String to, String subject, String text, List<MultipartFile> files);

    boolean verifyEmail(String otp, String email);

    LoginResponse authVerify(OAuth2AuthenticationToken authenticationToken,HttpSession httpSession);

    String getRefreshToken(String token);
}
