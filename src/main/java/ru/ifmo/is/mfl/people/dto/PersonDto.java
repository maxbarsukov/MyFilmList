package ru.ifmo.is.mfl.people.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.ifmo.is.mfl.common.framework.dto.CrudDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonDto extends CrudDto {
  private int id;
  private String name;
}
