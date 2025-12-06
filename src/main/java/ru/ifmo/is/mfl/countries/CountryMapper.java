package ru.ifmo.is.mfl.countries;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.is.mfl.countries.dto.CountryDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {
  CountryDto map(Country country);

  List<CountryDto> map(List<Country> countries);
}
