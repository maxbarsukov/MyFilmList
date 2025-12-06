package ru.ifmo.is.mfl.countries;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.is.mfl.countries.dto.CountryDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {

  private final CountryRepository repository;
  private final CountryMapper mapper;

  public List<CountryDto> search(String query) {
    return mapper.map(repository.findByNameContainingIgnoreCase(query));
  }
}
