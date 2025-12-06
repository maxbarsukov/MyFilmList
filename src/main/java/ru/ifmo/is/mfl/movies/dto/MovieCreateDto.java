package ru.ifmo.is.mfl.movies.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieCreateDto {
  @NotNull
  @NotBlank
  private String title;

  private String description;

  private LocalDate releaseDate;

  private Integer duration;

  private List<String> categories;

  private List<String> tags;

  private List<String> productionCountries;

  private List<String> genres;

  private List<String> actors;

  private List<String> directors;

  private Integer seasons;

  private Integer series;
}
