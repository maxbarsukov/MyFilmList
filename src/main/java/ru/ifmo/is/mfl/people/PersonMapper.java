package ru.ifmo.is.mfl.people;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.is.mfl.people.dto.PersonDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {
  PersonDto map(Person person);

  List<PersonDto> map(List<Person> people);
}
