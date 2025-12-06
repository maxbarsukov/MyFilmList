package ru.ifmo.is.mfl.movies;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.ifmo.is.mfl.categories.Category;
import ru.ifmo.is.mfl.common.framework.CrudEntity;
import ru.ifmo.is.mfl.countries.Country;
import ru.ifmo.is.mfl.genres.Genre;
import ru.ifmo.is.mfl.people.MoviePerson;
import ru.ifmo.is.mfl.tags.Tag;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "movies")
public class Movie extends CrudEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movies_id_seq")
  @SequenceGenerator(name = "movies_id_seq", sequenceName = "movies_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private int id;

  @NotNull
  @NotBlank
  @Size(max = 127)
  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @Size(max = 255)
  @Column(name = "poster")
  private String poster;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Column(name = "duration")
  private Integer duration;

  @Column(name = "rating")
  private Float rating;

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "movie_categories",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  @ToString.Exclude
  private Set<Category> categories = new HashSet<>();

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "movie_tags",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  @ToString.Exclude
  private Set<Tag> tags = new HashSet<>();

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "movie_countries",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "country_id")
  )
  @ToString.Exclude
  private Set<Country> productionCountries = new HashSet<>();

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "movie_genres",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  @ToString.Exclude
  private Set<Genre> genres = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private Set<MoviePerson> people = new HashSet<>();

  @Column(name = "seasons")
  private Integer seasons;

  @Column(name = "series")
  private Integer series;

  @NotNull
  @Column(name = "viewed_counter", nullable = false)
  private int viewedCounter;
  @NotNull
  @Column(name = "rated_counter", nullable = false)
  private int ratedCounter;
  @NotNull
  @Column(name = "reviewed_counter", nullable = false)
  private int reviewedCounter;
  @NotNull
  @Column(name = "comments_counter", nullable = false)
  private int commentsCounter;

  public void incrementViewedCounter() {
    viewedCounter++;
  }

  public void decrementViewedCounter() {
    viewedCounter--;
  }

  public void incrementRatedCounter() {
    ratedCounter++;
  }

  public void decrementRatedCounter() {
    ratedCounter--;
  }

  public void incrementReviewedCounter() {
    reviewedCounter++;
  }

  public void decrementReviewedCounter() {
    reviewedCounter--;
  }

  public void incrementCommentsCounter() {
    commentsCounter++;
  }

  public void decrementCommentsCounter() {
    commentsCounter--;
  }
}
