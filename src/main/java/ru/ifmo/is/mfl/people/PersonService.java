package ru.ifmo.is.mfl.people;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.is.mfl.people.dto.PersonDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

  private final PersonRepository repository;
  private final PersonMapper mapper;

  public List<PersonDto> search(String query) {
    return mapper.map(repository.findByNameContainingIgnoreCase(query));
  }
}
