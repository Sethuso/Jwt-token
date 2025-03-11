package com.example.jwt_token.serviceImpl;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Load the user using the default service
        OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);

        // Retrieve the user attributes
        Map<String, Object> attributes = new HashMap<>(user.getAttributes()); // Copy to a mutable map

        // Get the client name (e.g., "GitHub")
        String clientName = userRequest.getClientRegistration().getClientName();

        // Modify attributes based on the client
        if ("GitHub".equalsIgnoreCase(clientName)) {
            attributes.put("email", attributes.get("login")); // Map "login" to "email" for GitHub
        }

        // Return a new custom OAuth2User
        return new DefaultOAuth2User(
                user.getAuthorities(),  // Use the same authorities
                attributes,             // Modified attributes
                "email"                 // Specify the key for the principal name
        );
    }
}





//package com.example.jwt_token.serviceImpl;
//
//
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
//        OAuth2User user = super.loadUser(userRequest);
//
//        // Custom processing, e.g., checking if the "name" attribute exists
//        if (user.getAttribute("name") == null) {
//            String login = user.getAttribute("login");
//            user.getAttributes().put("name", login);  // Set fallback to "login"
//        }
//
//        return user;
//    }
//}