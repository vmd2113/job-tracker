package com.duongw.commonservice.repository.search.specification;

import com.duongw.common.model.entity.BaseSpecification;
import com.duongw.common.model.entity.SearchCriteria;
import com.duongw.commonservice.model.entity.Category;
import org.springframework.data.jpa.domain.Specification;


public class CategorySpecification extends BaseSpecification<Category> {


    public CategorySpecification(SearchCriteria searchCriteria) {
        super(searchCriteria);
    }

    public static Specification<Category> searchByMultipleFields(String categoryName, String categoryCode) {
        Specification<Category> spec = Specification.where(null);

        if (categoryName != null && !categoryName.isEmpty()) {
            spec = spec.and(new CategorySpecification(
                    new SearchCriteria("categoryName", ":", categoryName)));
        }

        if (categoryCode != null && !categoryCode.isEmpty()) {
            spec = spec.and(new CategorySpecification(
                    new SearchCriteria("categoryCode", ":", categoryCode)));
        }

        return spec;
    }
}
