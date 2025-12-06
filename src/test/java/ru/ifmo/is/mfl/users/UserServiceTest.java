package ru.ifmo.is.mfl.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import jakarta.servlet.http.HttpServletRequest;
import ru.ifmo.is.mfl.common.config.PasswordEncoderProvider;
import ru.ifmo.is.mfl.common.errors.UserWithThisUsernameAlreadyExists;
import ru.ifmo.is.mfl.common.search.SearchMapper;
import ru.ifmo.is.mfl.common.utils.images.ImageProcessor;
import ru.ifmo.is.mfl.storage.StorageService;
import ru.ifmo.is.mfl.userroles.UserRoleService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserMapper mapper;
  @Mock
  private UserPolicy policy;
  @Mock
  private SearchMapper<User> searchMapper;
  @Mock
  private UserRepository repository;
  @Mock
  private PasswordEncoderProvider passwordEncoderProvider;
  @Mock
  private UserRoleService userRoleService;
  @Mock
  private StorageService storageService;
  @Mock
  private ImageProcessor imageProcessor;
  @Mock
  private HttpServletRequest httpRequest;
  @Mock
  private ApplicationEventPublisher eventPublisher;

  @InjectMocks
  private UserService service;

  @Test
  void create_ShouldSaveUser_WhenUserIsValid() {
    User user = User.builder()
        .username("testuser")
        .email("test@example.com")
        .build();

    when(repository.existsByUsername(user.getUsername())).thenReturn(false);
    when(repository.existsByEmail(user.getEmail())).thenReturn(false);
    when(repository.save(user)).thenReturn(user);
    when(repository.count()).thenReturn(2L);

    User result = service.create(user);

    assertThat(result).isEqualTo(user);
    verify(repository).save(user);
    verify(eventPublisher).publishEvent(any());
  }

  @Test
  void create_ShouldThrowException_WhenUsernameExists() {
    User user = User.builder()
        .username("existinguser")
        .email("test@example.com")
        .build();

    when(repository.existsByUsername(user.getUsername())).thenReturn(true);

    assertThatThrownBy(() -> service.create(user))
        .isInstanceOf(UserWithThisUsernameAlreadyExists.class)
        .hasMessage("Пользователь с таким именем уже существует");
    verify(repository, never()).save(any());
  }

  @Test
  void create_ShouldThrowException_WhenEmailExists() {
    User user = User.builder()
        .username("newuser")
        .email("existing@example.com")
        .build();

    when(repository.existsByUsername(user.getUsername())).thenReturn(false);
    when(repository.existsByEmail(user.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> service.create(user))
        .isInstanceOf(UserWithThisUsernameAlreadyExists.class)
        .hasMessage("Этот email уже зарегистрирован");
    verify(repository, never()).save(any());
  }
}
