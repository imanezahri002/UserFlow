package org.spring.service;


import org.spring.dto.UserDto;
import org.spring.entities.User;
import org.spring.mapper.UserMapper;
import org.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        if(dto.getPassword() != null && !dto.getPassword().isEmpty()){
            user.setPassword(encoder.encode(dto.getPassword()));
        }

        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }




}
