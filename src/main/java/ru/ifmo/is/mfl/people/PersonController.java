package ru.ifmo.is.mfl.people;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.is.mfl.people.dto.PersonDto;

import java.util.List;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
@Tag(name = "People")
public class PersonController {

  private final PersonService service;

  @GetMapping
  @Operation(summary = "Поиск людей")
  public List<PersonDto> search(@RequestParam(defaultValue = "") String query) {
    return service.search(query);
  }
}
