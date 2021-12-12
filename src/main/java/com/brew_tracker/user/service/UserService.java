package com.brew_tracker.user.service;

import com.brew_tracker.brew.model.BrewLogDto;
import com.brew_tracker.brew.service.BrewLogService;
import com.brew_tracker.user.model.UserDto;
import com.brew_tracker.user.persistence.entity.User;
import com.brew_tracker.user.persistence.repository.UserRepository;
import com.brew_tracker.user.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final BrewLogService brewLogService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, BrewLogService brewLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.brewLogService = brewLogService;
    }

    public String login(String username, String password) throws AuthenticationException {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }

    public String register(UserDto user) throws IllegalStateException {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new IllegalStateException("Username is already in use!");
        }
        if(userRepository.existsByEmail(user.getEmail()))
            throw new IllegalStateException("Email is already in use!");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(convertDtoToEntity(user));
        jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        return "Success";
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
        for(BrewLogDto element : brewLogService.getAllBrewLogsForUser(username)){
            brewLogService.deleteBrewLog(element.getId());
        }
    }

    public void logout(){
        SecurityContextHolder.clearContext();
    }

    public UserDto search(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalStateException("The user doesn't exist");
        }
        return convertEntityToDto(user);
    }


    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password(user.getPassword())//
                .authorities(user.getRoles())//
                .accountExpired(false)//
                .accountLocked(false)//
                .credentialsExpired(false)//
                .disabled(false)//
                .build();
    }

    private User convertDtoToEntity(UserDto userDto){
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .build();

    }

    private UserDto convertEntityToDto(User user){
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();

    }
}

