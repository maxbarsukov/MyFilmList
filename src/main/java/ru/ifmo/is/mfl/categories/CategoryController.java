package ru.ifmo.is.mfl.categories;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.is.mfl.categories.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories")
public class CategoryController {

  private final CategoryService service;

  @GetMapping
  @Operation(summary = "Поиск категорий")
  public List<CategoryDto> search(@RequestParam(defaultValue = "") String query) {
    return service.search(query);
  }
}
