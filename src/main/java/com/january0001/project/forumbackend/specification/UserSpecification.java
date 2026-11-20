package com.january0001.project.forumbackend.specification;

import com.january0001.project.forumbackend.entity.User;
import com.january0001.project.forumbackend.filter.UserFilter;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {

    private final UserFilter filter;

    public UserSpecification(UserFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if(filter.getUsername() != null && !filter.getUsername().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("username")),
                    "%" + filter.getUsername().trim().toLowerCase() + "%"));
        }

        if(filter.getRoleId() != null) {
            predicates.add(cb.equal(root.get("role").get("id"), filter.getRoleId()));
        }

        if(filter.getExcludeUsername() != null) {
            predicates.add(cb.notEqual(root.get("username"), filter.getExcludeUsername()));
        }

        if(query.getResultType() != Long.class && query.getResultType() != long.class) {
            root.fetch("role", JoinType.LEFT);
        }

        return cb.and(predicates.toArray(new Predicate[0]));

    }

}
