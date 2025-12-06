package ru.ifmo.is.mfl.movies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ru.ifmo.is.mfl.movies.dto.MovieCreateDto;
import ru.ifmo.is.mfl.movies.dto.MovieDto;
import ru.ifmo.is.mfl.users.UserService;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock
  private MovieRepository repository;

  @Mock
  private MovieMapper mapper;

  @Mock
  private MoviePolicy policy;

  @Mock
  private UserService userService;

  @InjectMocks
  private MovieService service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "userService", userService);
  }

  @Test
  void create_ShouldSaveAndReturnMovie() {
    MovieCreateDto createDto = new MovieCreateDto();
    Movie movie = new Movie();
    MovieDto movieDto = new MovieDto();

    when(mapper.map(createDto)).thenReturn(movie);
    when(repository.save(movie)).thenReturn(movie);
    when(mapper.map(movie)).thenReturn(movieDto);

    MovieDto result = service.create(createDto);

    assertThat(result).isEqualTo(movieDto);
    verify(policy).create(any());
    verify(repository).save(movie);
  }
}
