package ru.ifmo.is.mfl.people;

import jakarta.persistence.*;
import lombok.*;
import ru.ifmo.is.mfl.movies.Movie;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "movie_people")
public class MoviePerson {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_people_id_seq")
  @SequenceGenerator(name = "movie_people_id_seq", sequenceName = "movie_people_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id", nullable = false)
  @ToString.Exclude
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id", nullable = false)
  @ToString.Exclude
  private Person person;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private PersonRole role;
}
