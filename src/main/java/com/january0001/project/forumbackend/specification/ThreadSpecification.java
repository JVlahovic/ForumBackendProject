package com.january0001.project.forumbackend.specification;

import com.january0001.project.forumbackend.entity.Thread;
import com.january0001.project.forumbackend.filter.ThreadFilter;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ThreadSpecification implements Specification<Thread> {

    private final ThreadFilter filter;

    public ThreadSpecification(ThreadFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Thread> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")),
                    "%" + filter.getTitle().trim().toLowerCase() + "%"));
        }
        if (filter.getAllowedAccessCtrls() != null) {
            predicates.add(root.get("threadCategory").get("accessCtrl").in(filter.getAllowedAccessCtrls()));
        }
        if (query.getResultType() != Long.class && query.getResultType() != long.class) {
            root.fetch("author", JoinType.LEFT);
            root.fetch("threadCategory", JoinType.LEFT);
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
