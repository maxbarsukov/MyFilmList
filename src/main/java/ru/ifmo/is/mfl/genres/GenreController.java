package ru.ifmo.is.mfl.genres;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.is.mfl.genres.dto.GenreDto;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@Tag(name = "Genres")
public class GenreController {

  private final GenreService service;

  @GetMapping
  @Operation(summary = "Поиск жанров")
  public List<GenreDto> search(@RequestParam(defaultValue = "") String query) {
    return service.search(query);
  }
}
