package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto getCategoryIfPresent(Long id);

    List<CategoryDto> getAllCategories();
}
