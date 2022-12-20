package demos.springdata.restdemo.web;

import demos.springdata.restdemo.model.Credentials;
import demos.springdata.restdemo.model.JwtResponse;
import demos.springdata.restdemo.model.User;
import demos.springdata.restdemo.service.UserService;
import demos.springdata.restdemo.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class LoginController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public LoginController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<JwtResponse> login(@RequestBody Credentials credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

        final User user = userService
                .getUserByUsername(credentials.getUsername());

        final String token = jwtUtils.generateToken(user);

        log.info("Login successful for {}: {}", user.getUsername(), token);
        return ResponseEntity.ok(new JwtResponse(token, user));
    }

}
