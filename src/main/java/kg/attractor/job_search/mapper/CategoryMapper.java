package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);
    Category toCategory(CategoryDto categoryDto);
}
