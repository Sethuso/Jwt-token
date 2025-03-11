package com.example.jwt_token.controller;

import com.example.jwt_token.exception.CustomException;
import com.example.jwt_token.model.LoginRequest;
import com.example.jwt_token.model.LoginResponse;
import com.example.jwt_token.model.User;
import com.example.jwt_token.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/com/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) throws CustomException {
        System.out.println("user : "+user);
        User savedUser = userService.saveUser(user);
        System.out.println("user : "+user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/home")
    public String hello(){
        return "hello i am here";
    }

    @PutMapping("/assignRole")
    public ResponseEntity<User> assigningRole(@RequestParam(name = "email") String email,@RequestParam(name = "roleName") String roleName){
        return ResponseEntity.ok(userService.assigningRole(email,roleName));
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getAllUser() throws CustomException {
        return ResponseEntity.ok(userService.getAllUsers());

    }

    @PostMapping("/get-refresh-token")
    public ResponseEntity<String> getRefreshToken(@RequestParam(name = "token") String token){
        return ResponseEntity.ok(userService.getRefreshToken(token));
    }

    @GetMapping("/getById")
    public ResponseEntity<Optional<User>> getById(@RequestParam String id){
        return ResponseEntity.ok(userService.getById(id));
    }



    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<User>> getAllEmployees(){
        return ResponseEntity.ok(userService.getAllEmployees());
    }

    @GetMapping("/getAllAdmins")
    public ResponseEntity<List<User>> getAllAdmins(){

        return ResponseEntity.ok(userService.getAllAdmins());
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestParam String to ,@RequestParam String subject,@RequestParam String text){
        return ResponseEntity.ok(userService.sendEmail(to,subject,text));
    }


    @PostMapping("/send-email-files")
    public ResponseEntity<?> sendEmailWithFiles(@RequestParam String to , @RequestParam String subject, @RequestParam String text, @RequestParam List<MultipartFile> file){

        return ResponseEntity.ok(userService.sendEmailWithFiles(to,subject,text,file));
    }


    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam  String otp, @RequestParam String email){
        boolean isVerified = userService.verifyEmail(otp,email);
        if (isVerified) {
            return ResponseEntity.ok("Verification successfully completed.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification failed.");
        }
    }


    @PutMapping("/deleteByEmail")
    public ResponseEntity<Optional<User>> deleteByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.deleteUser(email));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession httpSession){
        return ResponseEntity.ok(userService.verify(loginRequest.getEmail(),loginRequest.getPassword(),httpSession));

    }

    @GetMapping("/oauth-success")
    public ResponseEntity<?> authSuccess(OAuth2AuthenticationToken authenticationToken,HttpSession httpSession){
       LoginResponse  response =  userService.authVerify(authenticationToken,httpSession);
       return ResponseEntity.ok(response);

    }

  

}
