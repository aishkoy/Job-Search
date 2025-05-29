package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CategoryDto;
import kg.attractor.job_search.entity.Category;
import kg.attractor.job_search.exception.nsee.CategoryNotFoundException;
import kg.attractor.job_search.mapper.CategoryMapper;
import kg.attractor.job_search.repository.CategoryRepository;
import kg.attractor.job_search.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Long> findCategoriesById(Long categoryId){
        List<Long> ids =  categoryRepository.findAllCategoryIds(categoryId);
        if(ids.isEmpty()){
            throw new CategoryNotFoundException();
        }
        return ids;
    }

    @Override
    public boolean areCategoriesStrictlyCompatible(Long categoryId1, Long categoryId2) {
        if (categoryId1.equals(categoryId2)) {
            return true;
        }

        if (categoryRepository.isParentCategory(categoryId1, categoryId2)) {
            return true;
        }

        return categoryRepository.isParentCategory(categoryId2, categoryId1);
    }

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
