package ru.ifmo.is.mfl.tags;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.is.mfl.tags.dto.TagDto;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tags")
public class TagController {

  private final TagService service;

  @GetMapping
  @Operation(summary = "Поиск тегов")
  public List<TagDto> search(@RequestParam(defaultValue = "") String query) {
    return service.search(query);
  }
}
