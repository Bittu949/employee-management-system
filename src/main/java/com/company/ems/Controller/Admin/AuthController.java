//package com.company.ems.Controller;
//
//import com.company.ems.Dto.LoginRequest;
//import com.company.ems.Security.JwtUtil;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtUtil jwtUtil;
//    @PostMapping("/login")
//    public void login(@RequestBody LoginRequest request,
//                        HttpServletResponse response) {
//        String email = request.getEmail().trim().toLowerCase();
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        email,
//                        request.getPassword()
//                )
//        );
//        if (authentication.isAuthenticated()) {
//            String token = jwtUtil.generateToken(email);
//            Cookie cookie = new Cookie("jwt", token);
//            cookie.setHttpOnly(true);     // JS cannot access (secure)
//            cookie.setSecure(false);      // true in production (HTTPS)
//            cookie.setPath("/");          // available for all endpoints
//            cookie.setMaxAge(24 * 60 * 60); // 1 day
//            response.addCookie(cookie);
//        } else {
//            throw new RuntimeException("Invalid credentials");
//        }
//    }
//}


package com.company.ems.Controller.Admin;

import com.company.ems.Dto.LoginRequest;
import com.company.ems.Entity.User;
import com.company.ems.Exception.InvalidCredentialsException;
import com.company.ems.Repository.UserRepository;
import com.company.ems.Security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request,
                      HttpServletResponse response) {

        String email = request.getEmail().trim().toLowerCase();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(email);

                Cookie cookie = new Cookie("jwt", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(24 * 60 * 60);

                response.addCookie(cookie);
            } else {
                throw new InvalidCredentialsException("Invalid email or password");
            }

        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @GetMapping("/me")
    @ResponseBody
    public Map<String, String> getCurrentUser(Principal principal) {

        if (principal == null) {
            throw new RuntimeException("User not authenticated");
        }
        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Map.of("role", user.getRole());
    }
}