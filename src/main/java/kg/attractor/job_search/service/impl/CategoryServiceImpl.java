package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.entity.Category;
import kg.attractor.job_search.exception.CategoryNotFoundException;
import kg.attractor.job_search.mapper.CategoryMapper;
import kg.attractor.job_search.repository.CategoryRepository;
import kg.attractor.job_search.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto getCategoryIfPresent(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Не существует такой категории!"));
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories(){
        List<CategoryDto> categories = categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        if(categories.isEmpty()){
            throw new CategoryNotFoundException("Не найдено никаких категорий!");
        }
        return categories;
    }
}
