package ru.ifmo.is.mfl.movies.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.ifmo.is.mfl.categories.Category;
import ru.ifmo.is.mfl.countries.Country;
import ru.ifmo.is.mfl.genres.Genre;
import ru.ifmo.is.mfl.movies.Movie;
import ru.ifmo.is.mfl.people.MoviePerson;
import ru.ifmo.is.mfl.tags.Tag;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MovieWithAdditionalInfo extends Movie {

  private Integer currentUserRating;
  private Boolean currentUserViewed;

  public MovieWithAdditionalInfo(Movie movie, Integer currentUserRating, Boolean currentUserViewed) {
    this(
      movie.getId(),
      movie.getTitle(),
      movie.getDescription(),
      movie.getPoster(),
      movie.getReleaseDate(),
      movie.getDuration(),
      movie.getRating(),
      movie.getCategories(),
      movie.getTags(),
      movie.getProductionCountries(),
      movie.getGenres(),
      movie.getPeople(),
      movie.getSeasons(),
      movie.getSeries(),
      movie.getViewedCounter(),
      movie.getRatedCounter(),
      movie.getReviewedCounter(),
      movie.getCommentsCounter(),
      currentUserRating,
      currentUserViewed
    );
  }

  public MovieWithAdditionalInfo(
    int id,
    @NotNull @NotBlank @Size(max = 127) String title,
    String description,
    @Size(max = 255) String poster,
    LocalDate releaseDate,
    Integer duration,
    Float rating,
    Set<Category> categories,
    Set<Tag> tags,
    Set<Country> productionCountries,
    Set<Genre> genres,
    Set<MoviePerson> people,
    Integer seasons,
    Integer series,
    int viewedCounter,
    int ratedCounter,
    int reviewedCounter,
    int commentsCounter,
    Integer currentUserRating,
    Boolean currentUserViewed
  ) {
    super(
      id,
      title,
      description,
      poster,
      releaseDate,
      duration,
      rating,
      categories,
      tags,
      productionCountries,
      genres,
      people,
      seasons,
      series,
      viewedCounter,
      ratedCounter,
      reviewedCounter,
      commentsCounter
    );
    this.currentUserRating = currentUserRating;
    this.currentUserViewed = currentUserViewed;
  }
}
