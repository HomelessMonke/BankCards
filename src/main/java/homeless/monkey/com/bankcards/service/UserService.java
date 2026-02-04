package homeless.monkey.com.bankcards.service;

import homeless.monkey.com.bankcards.dto.UserCreationRequestDTO;
import homeless.monkey.com.bankcards.dto.UserCreationResponseDTO;
import homeless.monkey.com.bankcards.entity.User;
import homeless.monkey.com.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserCreationResponseDTO createUser(UserCreationRequestDTO dto){

        if(userRepository.existsByEmail(dto.email())){
            throw new IllegalStateException("Пользователь с email " + dto.email() + " существует!");
        }

        User user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());
        userRepository.save(user);

        return new UserCreationResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole());
    }
}
