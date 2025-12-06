package ru.ifmo.is.mfl.comments;

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

import ru.ifmo.is.mfl.comments.dto.CommentCreateDto;
import ru.ifmo.is.mfl.comments.dto.CommentDto;
import ru.ifmo.is.mfl.movies.Movie;
import ru.ifmo.is.mfl.users.UserService;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @Mock
  private CommentRepository repository;
  @Mock
  private CommentPolicy policy;
  @Mock
  private CommentMapper mapper;
  @Mock
  private UserService userService;

  @InjectMocks
  private CommentService service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "userService", userService);
  }

  @Test
  void create_ShouldCreateCommentForMovie() {
    CommentCreateDto dto = new CommentCreateDto();
    dto.setText("Nice movie!");
    Movie movie = new Movie();
    movie.setId(1);

    CommentDto commentDto = new CommentDto();

    when(repository.save(any(Comment.class))).thenAnswer(i -> i.getArguments()[0]);
    when(mapper.map(any(Comment.class))).thenReturn(commentDto);

    CommentDto result = service.create(dto, movie);

    assertThat(result).isEqualTo(commentDto);
    verify(policy).create(any());
    verify(repository).save(any(Comment.class));
  }
}
