package ru.ifmo.is.mfl.genres.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.ifmo.is.mfl.common.framework.dto.CrudDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class GenreDto extends CrudDto {
  private int id;
  private String name;
}
