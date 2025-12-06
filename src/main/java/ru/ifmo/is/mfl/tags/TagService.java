package ru.ifmo.is.mfl.tags;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.is.mfl.tags.dto.TagDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository repository;
  private final TagMapper mapper;

  public List<TagDto> search(String query) {
    return mapper.map(repository.findByNameContainingIgnoreCase(query));
  }
}
