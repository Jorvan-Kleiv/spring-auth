package Immersive.kleiv.Services;

import Immersive.kleiv.Entity.Role;
import Immersive.kleiv.Entity.RoleType;
import Immersive.kleiv.Entity.User;
import Immersive.kleiv.Entity.Validation;
import Immersive.kleiv.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User registerUser(User user) {
        if(!this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            if (user.getEmail().contains("@") && user.getEmail().contains(".")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                Role role = new Role();
                role.setLabel(RoleType.USER);
                user.setRole(role);
                User savedUser = userRepository.save(user);
                validationService.generateCode(savedUser);
                return savedUser;
            }

        }
        return null;
    }
   public void activateUser(Map<String , String> activationCodes) {
       Validation validation = validationService.getValidationByCode(activationCodes.get("code"));
       if(Instant.now().isAfter(validation.getXpiredAt()))
       {
           throw new RuntimeException("Code expired");
       }
       userRepository.findById(validation.getUser().getId()).ifPresent(user -> {
           user.setActif(true);
           userRepository.save(user);
       });
   }
}
