package ru.ifmo.is.mfl.reports;

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

import ru.ifmo.is.mfl.comments.Comment;
import ru.ifmo.is.mfl.common.errors.ResourceAlreadyExists;
import ru.ifmo.is.mfl.reports.dto.ReportCreateDto;
import ru.ifmo.is.mfl.reports.dto.ReportDto;
import ru.ifmo.is.mfl.reviews.Review;
import ru.ifmo.is.mfl.users.User;
import ru.ifmo.is.mfl.users.UserService;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @Mock
  private ReportRepository repository;
  @Mock
  private ReportPolicy policy;
  @Mock
  private ReportMapper mapper;
  @Mock
  private UserService userService;

  @InjectMocks
  private ReportService service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "userService", userService);
  }

  @Test
  void create_ShouldCreateReportForComment_WhenNotExists() {
    Comment comment = new Comment();
    ReportCreateDto dto = new ReportCreateDto();
    dto.setIssue("Spam");
    User user = new User();

    ReportDto reportDto = new ReportDto();

    when(userService.getCurrentUser()).thenReturn(user);
    when(repository.findByUserAndComment(user, comment)).thenReturn(Optional.empty());
    when(repository.save(any(Report.class))).thenAnswer(i -> i.getArguments()[0]);
    when(mapper.map(any(Report.class))).thenReturn(reportDto);

    ReportDto result = service.create(dto, comment);

    assertThat(result).isEqualTo(reportDto);
    verify(policy).create(user);
    verify(repository).save(any(Report.class));
  }

  @Test
  void create_ShouldThrowException_WhenReportForCommentExists() {
    Comment comment = new Comment();
    ReportCreateDto dto = new ReportCreateDto();
    User user = new User();

    when(userService.getCurrentUser()).thenReturn(user);
    when(repository.findByUserAndComment(user, comment)).thenReturn(Optional.of(new Report()));

    assertThatThrownBy(() -> service.create(dto, comment))
        .isInstanceOf(ResourceAlreadyExists.class)
        .hasMessage("You already reported this comment.");
  }

  @Test
  void create_ShouldCreateReportForReview_WhenNotExists() {
    Review review = new Review();
    ReportCreateDto dto = new ReportCreateDto();
    dto.setIssue("Spam");
    User user = new User();

    ReportDto reportDto = new ReportDto();

    when(userService.getCurrentUser()).thenReturn(user);
    when(repository.findByUserAndReview(user, review)).thenReturn(Optional.empty());
    when(repository.save(any(Report.class))).thenAnswer(i -> i.getArguments()[0]);
    when(mapper.map(any(Report.class))).thenReturn(reportDto);

    ReportDto result = service.create(dto, review);

    assertThat(result).isEqualTo(reportDto);
    verify(policy).create(user);
    verify(repository).save(any(Report.class));
  }
}
