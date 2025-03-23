package kg.attractor.job_search.service;

import java.util.Optional;

public interface CategoryService {
    Optional<Long> getCategoryIdIfPresent(Long id);
}
