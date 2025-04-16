package kg.attractor.job_search.service;

import kg.attractor.job_search.dto.CategoryDto;

public interface CategoryService {
    CategoryDto getCategoryIfPresent(Long id);
}
