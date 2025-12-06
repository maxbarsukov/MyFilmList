package ru.ifmo.is.mfl.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.is.mfl.categories.dto.CategoryDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository repository;
  private final CategoryMapper mapper;

  public List<CategoryDto> search(String query) {
    return mapper.map(repository.findByNameContainingIgnoreCase(query));
  }
}
