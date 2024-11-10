package com.nyasha.drone.auth;

import com.nyasha.drone.email.EmailService;
import com.nyasha.drone.email.EmailTemplateName;
import com.nyasha.drone.role.Role;
import com.nyasha.drone.role.RoleRepository;
import com.nyasha.drone.user.Token;
import com.nyasha.drone.user.TokenRepository;
import com.nyasha.drone.user.User;
import com.nyasha.drone.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    public void register(RegistrationRequest request) throws MessagingException {
        var userRole =roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("User not found"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        // generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i =0; i<length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public void authenticate(AuthenticationRequest request) {
        // Perform authentication
        log.warn("This is the start of the login process in the service!!");
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       if(authentication != null){
           Object principal = authentication.getPrincipal();
           UserDetails userDetails = (UserDetails) principal;
            log.info("User logged in: Username: {}, Authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());
       }else{
           log.warn("No authenticated user found.");
       }


    }

    @Transactional
    public void activateAccount(TokenRequest request) throws MessagingException {
        String token = request.token();
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public void changePassword(@Valid AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException(("No user found wit the email " + request.getEmail())));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("current password is wrong");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(int id, @Valid UpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("user was not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEnabled(request.isEnabled());
        user.setAccountLocked(request.isAccountLocked());
        List<Integer> roleIds = request.getRoles();
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> fetchedRoles = roleRepository.findByIdIn(roleIds);
            user.setRoles(fetchedRoles);
        }

        userRepository.save(user);
    }

    public UpdateRequest getUser(int id, UpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("user was not found"));
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setId(user.getId());
        List<Integer> roleIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList());
        request.setRoles(roleIds);
        request.setDateOfBirth(user.getDateOfBirth());
        request.setEnabled(user.isEnabled());
        request.setAccountLocked(user.isAccountLocked());
        return request;
    }

    public User getUserDetails(int id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("user was not found"));
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public User getProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // The email is usually the username in Spring Security
        } else {
            email = principal.toString();
        }

        // Fetch the user from the database by email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

    }
}
