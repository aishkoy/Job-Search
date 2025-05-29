package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<Long> findCategoriesById(Long categoryId);

    boolean areCategoriesStrictlyCompatible(Long categoryId1, Long categoryId2);

    CategoryDto getCategoryIfPresent(Long id);

    List<CategoryDto> getAllCategories();
}
