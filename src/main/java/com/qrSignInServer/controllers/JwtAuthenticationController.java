package com.qrSignInServer.controllers;

import com.qrSignInServer.config.security.JwtTokenUtil;
import com.qrSignInServer.dto.CreateUserRequest;
import com.qrSignInServer.dto.JwtRequest;
import com.qrSignInServer.dto.JwtResponse;
import com.qrSignInServer.models.User;
import com.qrSignInServer.services.JwtUserDetailsService;
import com.qrSignInServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping(value = "/jwt")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user_tenant/authenticate")
    public ResponseEntity<?> createAuthenticationTokenTenant(@RequestBody @Valid JwtRequest authRequest) throws Exception {
        authenticate(authRequest.getUsername(), authRequest.getPassword());
        final User user = userDetailsService.loadUserByUsernameAndTipo(authRequest.getUsername(), "tenant");
        final String token = jwtTokenUtil.generateToken(user);
        JwtResponse jwtResponse = new JwtResponse(token);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping(value = "/user_lessor/authenticate")
    public ResponseEntity<?> createAuthenticationTokenLessor(@RequestBody @Valid JwtRequest authRequest) throws Exception {
        authenticate(authRequest.getUsername(), authRequest.getPassword());
        final User user = userDetailsService.loadUserByUsernameAndTipo(authRequest.getUsername(), "lessor");
        final String token = jwtTokenUtil.generateToken(user);
        JwtResponse jwtResponse = new JwtResponse(token);
        return ResponseEntity.ok(jwtResponse);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    //lessor: es el propietario que ofrece - en nuestro, nuestros usuarios
    @PostMapping("/user/register")
    public Optional<User> userLessorRegister(@RequestBody @Valid CreateUserRequest request) throws ValidationException {
        return userService.create(request, "lessor");
    }

    //tenant: es el que alquila - en nuestro caso el que utiliza nuestros servicios.
    @PostMapping("/user_tenant/register")
    public Optional<User> userTenantRegister(@RequestBody @Valid CreateUserRequest request) throws ValidationException {
        return userService.create(request, "tenant");
    }

    @PostMapping(value = "/getReference")
    public String getReference(@RequestBody @Valid HashMap<String, String> data) throws ValidationException {
        String tokenQR = data.get("tokenReference");
        return jwtTokenUtil.getReferenceFromToken(tokenQR);
    }

}
