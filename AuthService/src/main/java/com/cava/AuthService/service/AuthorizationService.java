package com.cava.AuthService.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.cava.AuthService.model.RegisterDTO;
import com.cava.AuthService.model.TokenPayload;
import com.cava.AuthService.model.UserResponseDTO;
import com.cava.AuthService.model.Users;
import com.cava.AuthService.repository.UserRepository;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.NameBasedGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final UUID NAMESPACE = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8"); // namespace DNS padrão

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

    @Transactional
    public void registerUser(RegisterDTO data){
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UUID userId = generateUuidFromPhone(data.phoneNumber());

        if (userRepository.existsById(userId)) {
            throw new IllegalStateException("Usuário já existe com esse número.");
        }

        Users newUser = new Users(userId ,data.phoneNumber(),data.login(), encryptedPassword);

        this.userRepository.save(newUser);

    }

    private UUID generateUuidFromPhone(String phoneNumber) {
        NameBasedGenerator generator = Generators.nameBasedGenerator(NAMESPACE);
        return generator.generate(phoneNumber);
    }

    public UserResponseDTO getUserById(String id){
        Optional<Users> user = userRepository.findById(UUID.fromString(id));
        if(user.isPresent()){
            return new UserResponseDTO(user.get().getId().toString(),user.get().getPhoneNumber(), user.get().getName());
        }
        return null;
    }

}
