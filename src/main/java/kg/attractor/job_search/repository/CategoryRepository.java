package kg.attractor.job_search.repository;

import kg.attractor.job_search.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c.id FROM Category c WHERE c.id = :categoryId OR c.parentCategory.id = :categoryId")
    List<Long> findAllCategoryIds(@Param("categoryId") Long categoryId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Category c WHERE c.id = :childId AND c.parentCategory.id = :parentId")
    boolean isParentCategory(@Param("parentId") Long parentId, @Param("childId") Long childId);
}
