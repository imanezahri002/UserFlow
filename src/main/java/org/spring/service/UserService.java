package org.spring.service;


import org.spring.dto.UserDto;
import org.spring.entities.User;
import org.spring.mapper.UserMapper;
import org.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserDto createUser(UserDto dto) {
        // Vérifier si l'email existe déjà
        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("Email déjà utilisé");
        }
        // Mapper DTO → Entity
        User user = userMapper.toEntity(dto);
        // Hasher le mot de passe
        user.setPassword(encoder.encode(dto.getPassword()));
        // Sauvegarder en DB
        User savedUser = userRepository.save(user);

        // Mapper Entity → DTO pour la réponse
        return userMapper.toDto(savedUser);
    }




}
