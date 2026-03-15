package com.example.userservice;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.exception.DuplicateResourceException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    private CreateUserRequest validRequest;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        validRequest = CreateUserRequest.builder()
                .username("john.doe")
                .email("john@example.com")
                .password("Password@123")
                .firstName("John")
                .lastName("Doe")
                .role(User.Role.ROLE_USER)
                .build();

        sampleUser = User.builder()
                .id(1L)
                .username("john.doe")
                .email("john@example.com")
                .password("encoded-password")
                .firstName("John")
                .lastName("Doe")
                .role(User.Role.ROLE_USER)
                .enabled(true)
                .build();
    }

    @Test
    @DisplayName("Should create user successfully")
    void createUser_Success() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponse response = userService.createUser(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("john.doe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when username exists")
    void createUser_DuplicateUsername() {
        when(userRepository.existsByUsername("john.doe")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(validRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Username already exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email exists")
    void createUser_DuplicateEmail() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(validRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("john.doe");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found")
    void getUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: 99");
    }

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should toggle user enabled status")
    void toggleUserStatus_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserResponse response = userService.toggleUserStatus(1L);

        assertThat(response).isNotNull();
        verify(userRepository).save(any(User.class));
    }
}
