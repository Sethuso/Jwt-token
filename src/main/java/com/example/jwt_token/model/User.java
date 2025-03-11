package com.example.jwt_token.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.time.LocalDateTime;


@Data
//@Setter
//@Getter
@NoArgsConstructor
@Document(collection = "user-front-end")
public class User  {

    @Id
    @Indexed
    private String userId;
    private String userName;
    private String password;
    @Email(message = "email must be valid......")
    @NotNull(message = "email must be required........")
    private String email;
    private String age;
    @Indexed
    private Boolean isActive = true;
    @DocumentReference(lazy = false)
    private Roles roles;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String createdBy;
    private Boolean isVerified = false;
    private String verificationToken;


//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public @Email(message = "email must be valid......") @NotNull(message = "email must be required........") String getEmail() {
//        return email;
//    }
//
//    public void setEmail(@Email(message = "email must be valid......") @NotNull(message = "email must be required........") String email) {
//        this.email = email;
//    }
//
//    public String getAge() {
//        return age;
//    }
//
//    public void setAge(String age) {
//        this.age = age;
//    }
//
//    public Boolean getActive() {
//        return isActive;
//    }
//
//    public void setActive(Boolean active) {
//        isActive = active;
//    }
//
//    public Roles getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Roles roles) {
//        this.roles = roles;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public Boolean getVerified() {
//        return isVerified;
//    }
//
//    public void setVerified(Boolean verified) {
//        isVerified = verified;
//    }
//
//    public String getVerificationToken() {
//        return verificationToken;
//    }
//
//    public void setVerificationToken(String verificationToken) {
//        this.verificationToken = verificationToken;
//    }
}

