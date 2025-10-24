package org.spring.mapper;


import org.spring.dto.UserDto;
import org.spring.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Convertir User → UserDTO
    public UserDto toDto(User user) {
        if(user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
    // Convertir UserDTO → User
    public User toEntity(UserDto dto) {
        if(dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        return user;
    }
}
