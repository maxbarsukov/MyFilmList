package ru.ifmo.is.mfl.movies;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.is.mfl.categories.Category;
import ru.ifmo.is.mfl.categories.CategoryMapper;
import ru.ifmo.is.mfl.categories.CategoryRepository;
import ru.ifmo.is.mfl.common.framework.CrudMapper;
import ru.ifmo.is.mfl.common.mapper.JsonNullableMapper;
import ru.ifmo.is.mfl.common.mapper.ReferenceMapper;
import ru.ifmo.is.mfl.countries.Country;
import ru.ifmo.is.mfl.countries.CountryMapper;
import ru.ifmo.is.mfl.countries.CountryRepository;
import ru.ifmo.is.mfl.genres.Genre;
import ru.ifmo.is.mfl.genres.GenreMapper;
import ru.ifmo.is.mfl.genres.GenreRepository;
import ru.ifmo.is.mfl.movies.dto.MovieCreateDto;
import ru.ifmo.is.mfl.movies.dto.MovieDto;
import ru.ifmo.is.mfl.movies.dto.MovieUpdateDto;
import ru.ifmo.is.mfl.movies.dto.MovieWithAdditionalInfoDto;
import ru.ifmo.is.mfl.movies.query.MovieWithAdditionalInfo;
import ru.ifmo.is.mfl.people.*;
import ru.ifmo.is.mfl.people.dto.PersonDto;
import ru.ifmo.is.mfl.storage.StorageService;
import ru.ifmo.is.mfl.tags.Tag;
import ru.ifmo.is.mfl.tags.TagMapper;
import ru.ifmo.is.mfl.tags.TagRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
  uses = {
    JsonNullableMapper.class,
    ReferenceMapper.class,
    TagMapper.class,
    GenreMapper.class,
    CategoryMapper.class,
    CountryMapper.class,
    PersonMapper.class
  },
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class MovieMapper implements CrudMapper<Movie, MovieDto, MovieCreateDto, MovieUpdateDto> {

  @Autowired
  public StorageService storageService;
  @Autowired
  protected TagRepository tagRepository;
  @Autowired
  protected GenreRepository genreRepository;
  @Autowired
  protected CategoryRepository categoryRepository;
  @Autowired
  protected CountryRepository countryRepository;
  @Autowired
  protected PersonRepository personRepository;
  @Autowired
  protected PersonMapper personMapper;

  @Mapping(target = "viewedCounter", constant = "0")
  @Mapping(target = "ratedCounter", constant = "0")
  @Mapping(target = "reviewedCounter", constant = "0")
  @Mapping(target = "commentsCounter", constant = "0")
  @Mapping(target = "tags", expression = "java(mapTags(dto.getTags()))")
  @Mapping(target = "genres", expression = "java(mapGenres(dto.getGenres()))")
  @Mapping(target = "categories", expression = "java(mapCategories(dto.getCategories()))")
  @Mapping(target = "productionCountries", expression = "java(mapCountries(dto.getProductionCountries()))")
  @Mapping(target = "people", ignore = true)
  public abstract Movie map(MovieCreateDto dto);

  @AfterMapping
  protected void mapPeople(MovieCreateDto dto, @MappingTarget Movie movie) {
    Set<MoviePerson> people = new HashSet<>();
    if (dto.getActors() != null) {
      for (String actorName : dto.getActors()) {
        for (String name : actorName.split(",")) {
          String finalName = name.trim();
          if (finalName.isEmpty()) continue;
          Person actor = personRepository.findByName(finalName)
            .orElseGet(() -> personRepository.save(Person.builder().name(finalName).build()));
          people.add(MoviePerson.builder().movie(movie).person(actor).role(PersonRole.ACTOR).build());
        }
      }
    }
    if (dto.getDirectors() != null) {
      for (String directorName : dto.getDirectors()) {
        for (String name : directorName.split(",")) {
          String finalName = name.trim();
          if (finalName.isEmpty()) continue;
          Person director = personRepository.findByName(finalName)
            .orElseGet(() -> personRepository.save(Person.builder().name(finalName).build()));
          people.add(MoviePerson.builder().movie(movie).person(director).role(PersonRole.DIRECTOR).build());
        }
      }
    }
    movie.setPeople(people);
  }

  @Mapping(target = "poster", expression = "java(storageService.getFileUrl(model.getPoster()))")
  @Mapping(target = "actors", source = "people", qualifiedByName = "mapActors")
  @Mapping(target = "directors", source = "people", qualifiedByName = "mapDirectors")
  public abstract MovieDto map(Movie model);

  @Mapping(target = "poster", expression = "java(storageService.getFileUrl(model.getPoster()))")
  @Mapping(target = "currentUserViewed", source = "currentUserViewed", defaultValue = "false")
  @Mapping(target = "actors", source = "people", qualifiedByName = "mapActors")
  @Mapping(target = "directors", source = "people", qualifiedByName = "mapDirectors")
  public abstract MovieWithAdditionalInfoDto map(MovieWithAdditionalInfo model);

  @Mapping(target = "poster", expression = "java(storageService.getFileUrl(model.getPoster()))")
  @Mapping(target = "actors", source = "people", qualifiedByName = "mapActors")
  @Mapping(target = "directors", source = "people", qualifiedByName = "mapDirectors")
  public abstract MovieWithAdditionalInfoDto mapAdditionalInfo(Movie model);

  public abstract Movie map(MovieDto model);

  @Mapping(target = "tags", expression = "java(dto.getTags() != null && dto.getTags().isPresent() ? mapTags(dto.getTags().get()) : model.getTags())")
  @Mapping(target = "genres", expression = "java(dto.getGenres() != null && dto.getGenres().isPresent() ? mapGenres(dto.getGenres().get()) : model.getGenres())")
  @Mapping(target = "categories", expression = "java(dto.getCategories() != null && dto.getCategories().isPresent() ? mapCategories(dto.getCategories().get()) : model.getCategories())")
  @Mapping(target = "productionCountries", expression = "java(dto.getProductionCountries() != null && dto.getProductionCountries().isPresent() ? mapCountries(dto.getProductionCountries().get()) : model.getProductionCountries())")
  @Mapping(target = "people", ignore = true)
  public abstract void update(MovieUpdateDto dto, @MappingTarget Movie model);

  @AfterMapping
  protected void updatePeople(MovieUpdateDto dto, @MappingTarget Movie movie) {
    if (dto.getActors() != null && dto.getActors().isPresent()) {
      movie.getPeople().removeIf(p -> p.getRole() == PersonRole.ACTOR);
      for (String actorName : dto.getActors().get()) {
        for (String name : actorName.split(",")) {
          String finalName = name.trim();
          if (finalName.isEmpty()) continue;
          Person actor = personRepository.findByName(finalName)
            .orElseGet(() -> personRepository.save(Person.builder().name(finalName).build()));
          movie.getPeople().add(MoviePerson.builder().movie(movie).person(actor).role(PersonRole.ACTOR).build());
        }
      }
    }
    if (dto.getDirectors() != null && dto.getDirectors().isPresent()) {
      movie.getPeople().removeIf(p -> p.getRole() == PersonRole.DIRECTOR);
      for (String directorName : dto.getDirectors().get()) {
        for (String name : directorName.split(",")) {
          String finalName = name.trim();
          if (finalName.isEmpty()) continue;
          Person director = personRepository.findByName(finalName)
            .orElseGet(() -> personRepository.save(Person.builder().name(finalName).build()));
          movie.getPeople().add(MoviePerson.builder().movie(movie).person(director).role(PersonRole.DIRECTOR).build());
        }
      }
    }
  }

  protected Set<Tag> mapTags(List<String> names) {
    if (names == null) return new HashSet<>();
    Set<Tag> tags = new HashSet<>();
    for (String nameList : names) {
      for (String name : nameList.split(",")) {
        name = name.trim();
        if (name.isEmpty()) continue;
        String finalName = name;
        tags.add(tagRepository.findByName(name)
          .orElseGet(() -> tagRepository.save(Tag.builder().name(finalName).build())));
      }
    }
    return tags;
  }

  protected Set<Genre> mapGenres(List<String> names) {
    if (names == null) return new HashSet<>();
    Set<Genre> genres = new HashSet<>();
    for (String nameList : names) {
      for (String name : nameList.split(",")) {
        name = name.trim();
        if (name.isEmpty()) continue;
        String finalName = name;
        genres.add(genreRepository.findByName(name)
          .orElseGet(() -> genreRepository.save(Genre.builder().name(finalName).build())));
      }
    }
    return genres;
  }

  protected Set<Category> mapCategories(List<String> names) {
    if (names == null) return new HashSet<>();
    Set<Category> categories = new HashSet<>();
    for (String nameList : names) {
      for (String name : nameList.split(",")) {
        name = name.trim();
        if (name.isEmpty()) continue;
        String finalName = name;
        categories.add(categoryRepository.findByName(name)
          .orElseGet(() -> categoryRepository.save(Category.builder().name(finalName).build())));
      }
    }
    return categories;
  }

  protected Set<Country> mapCountries(List<String> names) {
    if (names == null) return new HashSet<>();
    Set<Country> countries = new HashSet<>();
    for (String nameList : names) {
      for (String name : nameList.split(",")) {
        name = name.trim();
        if (name.isEmpty()) continue;
        String finalName = name;
        countries.add(countryRepository.findByName(name)
          .orElseGet(() -> countryRepository.save(Country.builder().name(finalName).build())));
      }
    }
    return countries;
  }

  @Named("mapActors")
  protected List<PersonDto> mapActors(Set<MoviePerson> people) {
    if (people == null) return new ArrayList<>();
    return people.stream()
      .filter(p -> p.getRole() == PersonRole.ACTOR)
      .map(mp -> personMapper.map(mp.getPerson()))
      .collect(Collectors.toList());
  }

  @Named("mapDirectors")
  protected List<PersonDto> mapDirectors(Set<MoviePerson> people) {
    if (people == null) return new ArrayList<>();
    return people.stream()
      .filter(p -> p.getRole() == PersonRole.DIRECTOR)
      .map(mp -> personMapper.map(mp.getPerson()))
      .collect(Collectors.toList());
  }
}
