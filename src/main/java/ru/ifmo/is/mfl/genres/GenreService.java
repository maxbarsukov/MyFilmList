package ru.ifmo.is.mfl.genres;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.is.mfl.genres.dto.GenreDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

  private final GenreRepository repository;
  private final GenreMapper mapper;

  public List<GenreDto> search(String query) {
    return mapper.map(repository.findByNameContainingIgnoreCase(query));
  }
}
