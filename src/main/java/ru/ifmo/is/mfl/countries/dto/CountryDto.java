package ru.ifmo.is.mfl.countries.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.ifmo.is.mfl.common.framework.dto.CrudDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class CountryDto extends CrudDto {
  private int id;
  private String name;
}
