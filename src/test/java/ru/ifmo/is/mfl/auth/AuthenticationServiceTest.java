package ru.ifmo.is.mfl.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.ifmo.is.mfl.auth.dto.AuthenticationDto;
import ru.ifmo.is.mfl.auth.dto.SignUpDto;
import ru.ifmo.is.mfl.refreshtokens.RefreshToken;
import ru.ifmo.is.mfl.refreshtokens.RefreshTokenService;
import ru.ifmo.is.mfl.users.User;
import ru.ifmo.is.mfl.users.UserMapper;
import ru.ifmo.is.mfl.users.UserService;
import ru.ifmo.is.mfl.users.dto.UserDto;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private UserService userService;
  @Mock
  private UserMapper mapper;
  @Mock
  private JwtService jwtService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private RefreshTokenService refreshService;

  @InjectMocks
  private AuthenticationService service;

  @Test
  void signUp_ShouldCreateUserAndReturnToken() {
    SignUpDto signUpDto = new SignUpDto();
    signUpDto.setUsername("testuser");
    signUpDto.setEmail("test@example.com");
    signUpDto.setPassword("password");

    User user = new User();
    user.setId(1);
    user.setUsername("testuser");

    UserDto userDto = new UserDto();
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken("refresh-token");

    when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encoded-password");
    when(userService.create(any(User.class))).thenReturn(user);
    when(jwtService.generateToken(user)).thenReturn("jwt-token");
    when(refreshService.createRefreshToken(user.getId())).thenReturn(refreshToken);
    when(mapper.map(user)).thenReturn(userDto);

    AuthenticationDto result = service.signUp(signUpDto);

    assertThat(result.getAccessToken()).isEqualTo("jwt-token");
    assertThat(result.getRefreshToken()).isEqualTo("refresh-token");
    assertThat(result.getUser()).isEqualTo(userDto);

    verify(userService).create(any(User.class));
    verify(jwtService).generateToken(user);
    verify(refreshService).createRefreshToken(user.getId());
  }
}
