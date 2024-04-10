package com.notes.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notes.authentication.JwtTokenProvider;
import com.notes.models.LoginDto;
import com.notes.models.User;
import com.notes.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor

public class AuthServiceImpl implements AuthService {
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;
	
	  private final ModelMapper modelMapper;

	    public AuthServiceImpl(ModelMapper modelMapper) {
	        this.modelMapper = modelMapper;
	    }

    
    @Override
    public String login(LoginDto loginDto) {

	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
	                loginDto.getUsernameOrEmail(),
	                loginDto.getPassword()
	        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }
    
    @Transactional
    @Override
    public User saveUser(User userRequest) {
        if(userRequest.getUsername() == null){
            throw new RuntimeException("Parameter username is not found in request..!!");
        } else if(userRequest.getPassword() == null){
            throw new RuntimeException("Parameter password is not found in request..!!");
        }


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
//        String usernameFromAccessToken = userDetail.getUsername();
//
//        UserInfo currentUser = userRepository.findByUsername(usernameFromAccessToken);

        User savedUser = null;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = userRequest.getPassword();
        String encodedPassword = encoder.encode(rawPassword);

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(encodedPassword);
        if(userRequest.getId() != null){
            User oldUser = userRepository.findById(userRequest.getId()).get();
            if(oldUser != null){
                oldUser.setId(user.getId());
                oldUser.setPassword(user.getPassword());
                oldUser.setUsername(user.getUsername());
              //  oldUser.setRoles(user.getRoles());

                savedUser = userRepository.save(oldUser);
              //  userRepository.refresh(savedUser);
            } else {
                throw new RuntimeException("Can't find record with identifier: " + userRequest.getId());
            }
        } else {
//            user.setCreatedBy(currentUser);
        	 UUID uuid = UUID.randomUUID();
//        	user.setId(uuid.toString());
            savedUser = userRepository.save(user);
        }
       // userRepository.refresh(savedUser);
        User userResponse = modelMapper.map(savedUser, User.class);
        return userResponse;
    }
}
