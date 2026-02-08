package homeless.monkey.com.bankcards.service;

import homeless.monkey.com.bankcards.dto.user.UserCreationRequestDto;
import homeless.monkey.com.bankcards.dto.user.UserCreationResponseDto;
import homeless.monkey.com.bankcards.entity.UserEntity;
import homeless.monkey.com.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserCreationResponseDto createUser(UserCreationRequestDto dto){

        if(userRepository.existsByEmail(dto.email())){
            throw new IllegalStateException("Пользователь с email " + dto.email() + " существует!");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());
        userRepository.save(user);

        return new UserCreationResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole());
    }

    @Transactional
    public void deleteUser(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Пользователь с id:" + id + " существует!"));

        var currentUser = getCurrentUser();
        if(user == currentUser)
            throw new IllegalArgumentException("Нельзя удалить самого себя");

        userRepository.delete(user);
    }

    public UserEntity getCurrentUser(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated())
            throw new AccessDeniedException("Пользователь не аутентифицирован");

        String email = (String) auth.getPrincipal();
        if(email != null && !email.isEmpty()){
            return userRepository.findByEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("Пользователь с email:" + email + " не найден"));
        }

        throw new AccessDeniedException("Пустой principal");
    }
}
