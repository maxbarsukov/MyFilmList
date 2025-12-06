package ru.ifmo.is.mfl.movies.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.ifmo.is.mfl.common.framework.dto.CrudDto;
import ru.ifmo.is.mfl.tags.dto.TagDto;
import ru.ifmo.is.mfl.genres.dto.GenreDto;
import ru.ifmo.is.mfl.categories.dto.CategoryDto;
import ru.ifmo.is.mfl.countries.dto.CountryDto;
import ru.ifmo.is.mfl.people.dto.PersonDto;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovieDto extends CrudDto {
  private int id;
  private String title;
  private String description;
  private String poster;
  private LocalDate releaseDate;
  private Integer duration;
  private Float rating;
  private List<CategoryDto> categories;
  private List<TagDto> tags;
  private List<CountryDto> productionCountries;
  private List<GenreDto> genres;
  private List<PersonDto> actors;
  private List<PersonDto> directors;
  private Integer seasons;
  private Integer series;
  private int viewedCounter;
  private int ratedCounter;
  private int reviewedCounter;
  private int commentsCounter;
}
