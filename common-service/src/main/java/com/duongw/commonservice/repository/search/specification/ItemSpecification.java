package com.duongw.commonservice.repository.search.specification;

import com.duongw.common.model.entity.BaseSpecification;
import com.duongw.common.model.entity.SearchCriteria;
import com.duongw.commonservice.model.entity.Category;
import com.duongw.commonservice.model.entity.Item;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification extends BaseSpecification<Item> {
    public ItemSpecification(SearchCriteria searchCriteria) {
        super(searchCriteria);
    }

    public static Specification<Item> searchByMultipleFields(String itemName, String itemCode, String categoryCode) {
            return Specification.where(searchByItemFields(itemName, itemCode)).and(searchByCategoryCode(categoryCode));
    }


    public static Specification<Item> searchByItemFields(String itemName, String itemCode) {
        Specification<Item> spec = Specification.where(null);

        if (itemName != null && !itemName.isEmpty()) {
            spec = spec.and(new ItemSpecification(
                    new SearchCriteria("itemName", ":", itemName)));
            spec = spec.and(new ItemSpecification(
                    new SearchCriteria("itemCode", ":", itemCode)));
        }
        return spec;
    }

    public static Specification<Item> searchByCategoryCode(String categoryCode) {
        return (root, query, cb) -> {
            if (categoryCode == null) {
                return null;
            }

            // Tạo subquery
            Subquery<Long> subqueryCategory = query.subquery(Long.class);
            Root<Category> categoryRoot = subqueryCategory.from(Category.class);

            // Select categoryId từ Category where categoryCode like ...
            subqueryCategory.select(categoryRoot.get("categoryId"))
                    .where(
                            cb.like(
                                    cb.lower(categoryRoot.get("categoryCode")),
                                    "%" + categoryCode.toLowerCase() + "%"
                            )
                    );

            // So sánh categoryId của Item với kết quả của subquery
            return cb.in(root.get("category").get("categoryId")).value(subqueryCategory);
        };
    }


}
