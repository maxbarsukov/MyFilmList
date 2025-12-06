package ru.ifmo.is.mfl.watchlists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
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
import ru.ifmo.is.mfl.users.UserService;
import ru.ifmo.is.mfl.watchlists.dto.WatchListDto;

@ExtendWith(MockitoExtension.class)
class WatchListServiceTest {

  @Mock
  private WatchListRepository repository;
  @Mock
  private WatchListPolicy policy;
  @Mock
  private WatchListMapper mapper;
  @Mock
  private UserService userService;

  @InjectMocks
  private WatchListService service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "userService", userService);
  }

  @Test
  void addMovie_ShouldAddMovie_WhenNotExists() throws Exception {
    int id = 1;
    Movie movie = new Movie();
    movie.setId(100);
    WatchList watchList = new WatchList();
    watchList.setId(id);
    watchList.setMovies(new HashSet<>());
    WatchListDto watchListDto = new WatchListDto();

    when(repository.findById(id)).thenReturn(Optional.of(watchList));
    when(repository.save(watchList)).thenReturn(watchList);
    when(mapper.map(watchList)).thenReturn(watchListDto);

    WatchListDto result = service.addMovie(id, movie);

    assertThat(result).isEqualTo(watchListDto);
    assertThat(watchList.getMovies()).contains(movie);
    verify(policy).update(any(), eq(watchList));
    verify(repository).save(watchList);
  }

  @Test
  void addMovie_ShouldThrowException_WhenMovieAlreadyExists() {
    int id = 1;
    Movie movie = new Movie();
    movie.setId(100);
    WatchList watchList = new WatchList();
    watchList.setId(id);
    watchList.setMovies(new HashSet<>());
    watchList.getMovies().add(movie);

    when(repository.findById(id)).thenReturn(Optional.of(watchList));

    assertThatThrownBy(() -> service.addMovie(id, movie))
        .isInstanceOf(ResourceAlreadyExists.class)
        .hasMessage("You already added this movie to the watch list");
  }

  @Test
  void findById_ShouldReturnWatchList_WhenVisible() {
    int id = 1;
    WatchList watchList = new WatchList();
    watchList.setId(id);
    watchList.setVisibility(true);

    when(repository.findById(id)).thenReturn(Optional.of(watchList));

    Optional<WatchList> result = service.findById(id);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(watchList);
  }
}
