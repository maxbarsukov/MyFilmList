package ru.ifmo.is.mfl.genres;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.is.mfl.genres.dto.GenreDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {
  GenreDto map(Genre genre);

  List<GenreDto> map(List<Genre> genres);
}
