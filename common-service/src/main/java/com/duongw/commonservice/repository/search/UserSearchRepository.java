package com.duongw.commonservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.model.entity.AbstractEntityCriteriaQuery;
import com.duongw.commonservice.model.dto.request.user.SearchUserDTO;
import com.duongw.commonservice.model.entity.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Slf4j

public class UserSearchRepository extends AbstractEntityCriteriaQuery<Users> {

    private final EntityManager entityManager;
    public UserSearchRepository(EntityManager entityManager, EntityManager entityManager1) {
        super(entityManager);
        this.entityManager = entityManager1;
    }

    public PageResponse<?> searchUserByCriteria(
            int pageNo, int pageSize,
            String usernameSearch, String emailSearch, String phoneNumberSearch, String firstNameSearch,
            String sortBy, String sortDirection

    ) {

        log.info("USER_REPOSITORY  -> searchUserByCriteria");
        SearchUserDTO searchUserDTO = SearchUserDTO.builder()
                .username(usernameSearch)
                .email(emailSearch)
                .phoneNumber(phoneNumberSearch)
                .firstName(firstNameSearch)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        return findByCriteria(searchUserDTO, pageNo, pageSize);

    }

    private PageResponse<?> findByCriteria(SearchUserDTO searchDTO, int pageNo, int pageSize) {
        log.info("Search user request by criteria");
        // Create Query
        CriteriaQuery<Users> query = createQuery();
        Root<Users> root = query.from(Users.class);
        // Apply predicates
        List<Predicate> predicates = createPredicates(root, searchDTO);
        query.where(predicates.toArray(new Predicate[0]));
        // Apply sorting
        if (searchDTO.getSortBy() != null) {
            Order order = createOrder(root, searchDTO);
            query.orderBy(order);
        }
        // Create TypedQuery for pagination
        TypedQuery<Users> typedQuery = entityManager.createQuery(query);
        // Apply pagination
        if (pageNo != 0 && pageSize != 0) {
            typedQuery.setFirstResult(pageNo * pageSize);
            typedQuery.setMaxResults(pageSize);
        }
        // Execute query
        List<Users> users = typedQuery.getResultList();
        // Get total count
        Long totalCount = getTotalCount(searchDTO);
        return PageResponse.<Users>builder()
                .total(totalCount)
                .items(users)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .build();

    }

    private List<Predicate> createPredicates(Root<Users> root, SearchUserDTO searchDTO) {
        List<Predicate> predicates = new ArrayList<>();
        log.info("USER_REPOSITORY  -> createPredicates");

        addPredicateIfNotNull(predicates, root, "firstName", searchDTO.getFirstName());
        addPredicateIfNotNull(predicates, root, "email", searchDTO.getEmail());
        addPredicateIfNotNull(predicates, root, "username", searchDTO.getUsername());
        addPredicateIfNotNull(predicates, root, "phoneNumber", searchDTO.getPhoneNumber());

        return predicates;
    }

    private void addPredicateIfNotNull(List<Predicate> predicates, Root<Users> root,
                                       String field, String value) {
        if (StringUtils.hasText(value)) {
            log.info("USER_REPOSITORY  -> addPredicateIfNotNull");
            predicates.add(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(field)),
                            "%" + value.toLowerCase() + "%"
                    )
            );
        }
    }

    private Order createOrder(Root<Users> root, SearchUserDTO searchDTO) {

        log.info("USER_REPOSITORY  -> createOrder");
        String sortBy = searchDTO.getSortBy();
        String direction = searchDTO.getSortDirection();

        if ("DESC".equalsIgnoreCase(direction)) {
            return criteriaBuilder.desc(root.get(sortBy));
        }
        return criteriaBuilder.asc(root.get(sortBy));
    }

    private Long getTotalCount(SearchUserDTO searchDTO) {

        log.info("USER_REPOSITORY  -> getTotalCount");
        CriteriaQuery<Long> countQuery = createCountQuery();
        Root<Users> root = countQuery.from(Users.class);

        countQuery.select(criteriaBuilder.count(root));

        List<Predicate> predicates = createPredicates(root, searchDTO);
        countQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    protected Class<Users> getEntityClass() {
        return Users.class;
    }
}
