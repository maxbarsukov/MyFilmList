package ru.ifmo.is.mfl.countries;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.is.mfl.countries.dto.CountryDto;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
@Tag(name = "Countries")
public class CountryController {

  private final CountryService service;

  @GetMapping
  @Operation(summary = "Поиск стран")
  public List<CountryDto> search(@RequestParam(defaultValue = "") String query) {
    return service.search(query);
  }
}
