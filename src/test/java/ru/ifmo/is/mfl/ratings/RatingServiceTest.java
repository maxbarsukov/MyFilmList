package ru.ifmo.is.mfl.ratings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ru.ifmo.is.mfl.common.errors.ResourceAlreadyExists;
import ru.ifmo.is.mfl.movies.Movie;
import ru.ifmo.is.mfl.movieviews.MovieViewService;
import ru.ifmo.is.mfl.ratings.dto.RatingCreateDto;
import ru.ifmo.is.mfl.ratings.dto.RatingDto;
import ru.ifmo.is.mfl.users.User;
import ru.ifmo.is.mfl.users.UserService;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

  @Mock
  private RatingRepository repository;
  @Mock
  private RatingPolicy policy;
  @Mock
  private RatingMapper mapper;
  @Mock
  private MovieViewService movieViewService;
  @Mock
  private UserService userService;

  @InjectMocks
  private RatingService service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "userService", userService);
  }

  @Test
  void create_ShouldCreateRating_WhenNotExists() {
    Movie movie = new Movie();
    movie.setId(1);
    RatingCreateDto dto = new RatingCreateDto();
    dto.setValue(5);
    User user = new User();
    user.setId(1);

    Rating rating = new Rating();
    RatingDto ratingDto = new RatingDto();

    when(userService.getCurrentUser()).thenReturn(user);
    when(repository.findByMovieAndUser(movie, user)).thenReturn(Optional.empty());
    when(repository.save(any(Rating.class))).thenReturn(rating);
    when(mapper.map(rating)).thenReturn(ratingDto);

    RatingDto result = service.create(movie, dto);

    assertThat(result).isEqualTo(ratingDto);
    verify(policy).create(user);
    verify(movieViewService).watchMovie(movie);
    verify(repository).save(any(Rating.class));
  }

  @Test
  void create_ShouldThrowException_WhenRatingExists() {
    Movie movie = new Movie();
    RatingCreateDto dto = new RatingCreateDto();
    User user = new User();

    when(userService.getCurrentUser()).thenReturn(user);
    when(repository.findByMovieAndUser(movie, user)).thenReturn(Optional.of(new Rating()));

    assertThatThrownBy(() -> service.create(movie, dto))
        .isInstanceOf(ResourceAlreadyExists.class)
        .hasMessage("This movie is already rated");
  }
}
