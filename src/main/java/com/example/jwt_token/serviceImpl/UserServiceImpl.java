package com.example.jwt_token.serviceImpl;

import com.example.jwt_token.exception.CustomException;
import com.example.jwt_token.exception.ErrorCode;
import com.example.jwt_token.exception.ResourceNotFoundException;
import com.example.jwt_token.exception.ValidationException;
import com.example.jwt_token.model.LoginResponse;
import com.example.jwt_token.model.Roles;
import com.example.jwt_token.model.User;
import com.example.jwt_token.model.UserPrinciple;
import com.example.jwt_token.repository.RolesRepository;
import com.example.jwt_token.repository.UserRepository;
import com.example.jwt_token.service.UserService;
import com.example.jwt_token.util.JwtUtil;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private  JavaMailSender javaMailSender;

    private final SecureRandom random = new SecureRandom();

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Value("${spring.mail.username}")
    private String userid;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public User assigningRole(String email, String roleName) {
       try {
           Optional<Roles> role = rolesRepository.findByRoleName(roleName);
           Optional<User> user = userRepository.findByEmail(email);
           if (user.isPresent()){
               User  updateUser = user.get();
               updateUser.setRoles(role.get());

                return  userRepository.save(updateUser);
           }
       }catch (Exception e){
                e.printStackTrace();
       }

        return null;
    }

    @Override
    public User saveUser(User user) throws CustomException {
      try {
          Optional<User>  existingUser = userRepository.findByEmail(user.getEmail());
          if(existingUser.isPresent()){
              throw new RuntimeException("email is already exists....");
          }
          Optional<Roles> defaultRole = rolesRepository.findByRoleName("Employee");
          user.setRoles(defaultRole.get());
          user.setCreatedBy(user.getUserName());
          user.setPassword(encoder.encode(user.getPassword()));

           userRepository.save(user);

          sendVerificationEmail(user);

          return user;

      }catch (Exception e){
          throw new CustomException(ErrorCode.EXP_1001);
      }
    }

    public void sendVerificationEmail(User user){
//        String subject = "Verify your email address";
        String otp = String.format("%06d",random.nextInt(1000000));
        user.setVerificationToken(otp);
        userRepository.save(user);
//        String verificationLink = "http://localhost:8080/com/users/verify-email?token="+user.getEmail();
//        String content ="<p>Hello,</p>"
//                + "<p>Please verify your email address by clicking the link below:</p>"
//                + "<p><a href=\"" + verificationLink + "\">Verify</a></p>";
////
        String subject = "Your OTP for Email Verification";
        String content = "<p>Hello,</p>"
                + "<p>Your One-Time Password (OTP) for email verification is:</p>"
                + "<h2>" + otp + "</h2>"
                + "<p>This OTP is valid for 10 minutes.</p>";
//        """

//                <p> hello,</p>
//                <br>
//                <p>Please verify your email address by clicking this link below:</p>
//                <p>
//                <a href=""+verificationLink+"">Verify</a>
//                </p>
//        """;
        sendEmail(user.getEmail(),subject,content);
    }

//    @Override
//    public boolean verifyEmail(String token) {
//        Optional<User> userOptional= userRepository.findByVerificationToken(token);
//        if(userOptional.isPresent()){
//            User user = userOptional.get();
//            user.setIsVerified(true);
//            user.setVerificationToken(null);
//            userRepository.save(user);
//        return true;
//        }else {
//            return false;
//        }
//    }

    public boolean verifyEmail(String otp, String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getVerificationToken() != null && user.getVerificationToken().equals(otp)) {
                user.setIsVerified(true);
                user.setVerificationToken(null); // Clear OTP after verification
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
        @Override
        public List<User> getAllUsers() throws CustomException {
          try {
              return userRepository.findAllUsers();
          }catch (Exception e){
            throw new CustomException(ErrorCode.EXP_1003);
          }
        }
    @Override
    public Optional<User> getById(String userId) {

        if(userId.trim().isEmpty()){
            throw new ValidationException("please give me the userId....");
        }
       try {
           Optional<User> u = userRepository.findById(userId);
           if (u.isEmpty()){
               throw new ResourceNotFoundException("user with id :  "+userId+"  is not found...");
           }
           return u;
       }catch (ResourceNotFoundException e) {
           throw new ResourceNotFoundException("user with id :" + userId + "is not found...");
       }
    }
//    @Override
//    public String verify(String email, String password, HttpSession httpSession) {
//        log.warn("attempting in the user: {}",email);
//       try {
//           Authentication authentication= authenticationManager.authenticate(
//                   new UsernamePasswordAuthenticationToken(email,password));
//           if (authentication.isAuthenticated()){
//               UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//               User user = userPrinciple.getUser();
//               return jwtUtil.generateToken(user,httpSession);
//           }else {
//               log.warn("Authentication failed for user:{}",email);
//               throw new ResourceNotFoundException("the user called "+email+"or"+password+"is invalid.....");
//           }
//       }catch (AuthenticationException e){
//           log.error("Authentication exception for user:{}",e.getMessage());
//            throw new ResourceNotFoundException("Invalid username or password for user: "+email);
//       }
//    }

    @Override
    public String verify(String email, String password, HttpSession httpSession) {
        log.warn("Attempting login for user: {}", email);

        try {
            // Check if user exists
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email"));

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            if (authentication.isAuthenticated()) {
                UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
                String token = jwtUtil.generateToken(userPrinciple.getUser(), httpSession);

                // Return success response
                return token;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        } catch (BadCredentialsException e) {
            log.error("Invalid password for user: {}", email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        } catch (ResponseStatusException e) {
            throw e; // Forward specific error messages
        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again.");
        }
    }


    @Override
    public LoginResponse authVerify(OAuth2AuthenticationToken authenticationToken,HttpSession httpSession) {
        Map<String,Object> values = authenticationToken.getPrincipal().getAttributes();
        String email = (String) values.get("email");
        String name = (String) values.get("name");
        Optional<User> existingUser =  userRepository.findByEmail(email);
        if (existingUser.isPresent()){
            User user = existingUser.get();

            String token = jwtUtil.generateToken(user,httpSession);



            return new LoginResponse("login success from existing user",token);

        }else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(name);
            newUser.setPassword(encoder.encode(UUID.randomUUID().toString()));
            newUser.setIsVerified(true);
            Optional<Roles> defaultRole = rolesRepository.findByRoleName("Employee");
            newUser.setRoles(defaultRole.orElseThrow(()-> new ResourceNotFoundException("role is not found...")));
            userRepository.save(newUser);
            String token = jwtUtil.generateToken(newUser,httpSession);

            return new LoginResponse("login success from new user",token);

        }
    }
    @Override
    public List<User> getAllAdmins() {
       try {
           return userRepository.getAllAdmins();
       }catch (Exception e){
           throw new RuntimeException(e.getMessage());
       }
    }
    @Override
    public List<User> getAllEmployees() {
        try {
            return userRepository.getAllEmployees();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String sendEmail(String to, String subject, String text) {
       try {
         if (!to.trim().isEmpty()){
             MimeMessage message = javaMailSender.createMimeMessage();
             MimeMessageHelper helper = new MimeMessageHelper(message,true);
             helper.setFrom(userid);
             helper.setTo(to);
             helper.setSubject(subject);
             helper.setText(text,true);
             javaMailSender.send(message);
            return "sent successfully";
         }else {
             return "something went wrong";
         }
       }catch (Exception e){
          throw new ValidationException("give me the correct mail address");
       }
    }


        @Override
        public String sendEmailWithFiles(String to, String subject, String text, List<MultipartFile> files) {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message,true);
                helper.setFrom(userid);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text);

                if (!files.isEmpty()){
                    for(MultipartFile fs : files){
                        helper.addAttachment(fs.getOriginalFilename(),fs);
                    }
                }

                javaMailSender.send(message);

                return "sent successfully";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public User updateUser(User user) {
        return null;
    }



    @Override
    public Optional<User> deleteUser(String email) {
      try {
          Optional<User> user=  userRepository.deleteByEmailId(email);
          if(user.isPresent()){
              User u = user.get();
              u.setIsActive(false);
              userRepository.save(u);
          }
          return user;
      }catch (Exception e){
       throw  new RuntimeException("user is not found");
      }

    }


    @Override
    public String getRefreshToken(String token) {
        try {
           return jwtUtil.refreshToken(token);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
