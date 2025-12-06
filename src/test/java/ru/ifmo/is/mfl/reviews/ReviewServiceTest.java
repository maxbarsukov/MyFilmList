package ru.ifmo.is.mfl.reviews;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import ru.ifmo.is.mfl.common.errors.ResourceNotFoundException;
import ru.ifmo.is.mfl.reviews.dto.ReviewDto;
import ru.ifmo.is.mfl.users.UserService;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  private ReviewRepository repository;
  @Mock
  private ReviewPolicy policy;
  @Mock
  private ReviewMapper mapper;
  @Mock
  private UserService userService;

  @InjectMocks
  private ReviewService service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "userService", userService);
  }

  @Test
  void getById_ShouldReturnReview_WhenVisible() {
    int id = 1;
    Review review = new Review();
    review.setId(id);
    review.setVisible(true);
    ReviewDto reviewDto = new ReviewDto();

    when(repository.findById(id)).thenReturn(Optional.of(review));
    when(repository.save(review)).thenReturn(review);
    when(mapper.map(review)).thenReturn(reviewDto);

    ReviewDto result = service.getById(id);

    assertThat(result).isEqualTo(reviewDto);
    verify(policy).show(any(), eq(review));
  }

  @Test
  void getById_ShouldThrowException_WhenNotFound() {
    int id = 1;
    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getById(id))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("Not Found: " + id);
  }
}
