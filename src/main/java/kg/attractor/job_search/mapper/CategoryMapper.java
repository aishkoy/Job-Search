package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentId", source = "parentCategory.id")
    CategoryDto toDto(Category category);

    @Mapping(target = "parentCategory.id", source = "parentId")
    Category toEntity(CategoryDto categoryDto);
}
