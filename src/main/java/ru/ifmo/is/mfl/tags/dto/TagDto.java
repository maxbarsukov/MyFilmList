package ru.ifmo.is.mfl.tags.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.ifmo.is.mfl.common.framework.dto.CrudDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagDto extends CrudDto {
  private int id;
  private String name;
}
