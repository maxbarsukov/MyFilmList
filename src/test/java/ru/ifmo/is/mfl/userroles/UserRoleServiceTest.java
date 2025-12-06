package ru.ifmo.is.mfl.userroles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.ifmo.is.mfl.common.errors.ResourceAlreadyExists;
import ru.ifmo.is.mfl.userroles.dto.UserRoleChangeDto;
import ru.ifmo.is.mfl.users.User;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

  @Mock
  private UserRoleRepository repository;
  @Mock
  private UserRolePolicy policy;

  @InjectMocks
  private UserRoleService service;

  @Test
  void add_ShouldAddRole_WhenAllowed() {
    User user = new User();
    user.setRoles(new HashSet<>());
    User currentUser = new User();
    currentUser.setRoles(new HashSet<>());
    UserRole adminRole = new UserRole();
    adminRole.setRole(Role.ROLE_ADMIN);
    currentUser.getRoles().add(adminRole);

    UserRoleChangeDto dto = new UserRoleChangeDto();
    dto.setRole(Role.ROLE_MODERATOR);

    User result = service.add(user, currentUser, dto);

    assertThat(result.getRoles()).hasSize(1);
    assertThat(result.getRoles().iterator().next().getRole()).isEqualTo(Role.ROLE_MODERATOR);
    verify(policy).update(currentUser, user);
    verify(repository).save(any(UserRole.class));
  }

  @Test
  void add_ShouldThrowException_WhenRoleExists() {
    User user = new User();
    user.setRoles(new HashSet<>());
    UserRole existingRole = new UserRole();
    existingRole.setRole(Role.ROLE_USER);
    user.getRoles().add(existingRole);

    User currentUser = new User();
    UserRoleChangeDto dto = new UserRoleChangeDto();
    dto.setRole(Role.ROLE_USER);

    assertThatThrownBy(() -> service.add(user, currentUser, dto))
        .isInstanceOf(ResourceAlreadyExists.class)
        .hasMessage("User already has role ROLE_USER");
  }
}
