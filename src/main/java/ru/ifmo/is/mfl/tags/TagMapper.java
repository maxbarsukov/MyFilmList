package ru.ifmo.is.mfl.tags;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.ifmo.is.mfl.tags.dto.TagDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {
  TagDto map(Tag tag);

  List<TagDto> map(List<Tag> tags);
}
