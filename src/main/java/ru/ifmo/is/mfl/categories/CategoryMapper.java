package ru.ifmo.is.mfl.categories;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.is.mfl.categories.dto.CategoryDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
  CategoryDto map(Category category);

  List<CategoryDto> map(List<Category> categories);
}
