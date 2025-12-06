package ru.ifmo.is.mfl.movies.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieUpdateDto {
  @NotNull
  @NotBlank
  private JsonNullable<String> title;

  private JsonNullable<String> description;

  private JsonNullable<LocalDate> releaseDate;

  private JsonNullable<Integer> duration;

  private JsonNullable<List<String>> categories;

  private JsonNullable<List<String>> tags;

  private JsonNullable<List<String>> productionCountries;

  private JsonNullable<List<String>> genres;

  private JsonNullable<List<String>> actors;

  private JsonNullable<List<String>> directors;

  private JsonNullable<Integer> seasons;

  private JsonNullable<Integer> series;
}
