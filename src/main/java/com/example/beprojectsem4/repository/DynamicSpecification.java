package com.example.beprojectsem4.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class DynamicSpecification<T> implements Specification<T> {
    private String value;
    public DynamicSpecification(String value){
        this.value = value;
    }
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.or(root.getModel().getDeclaredSingularAttributes().stream()
                .filter(attribute -> attribute.getJavaType().equals(String.class))
                .map(attribute -> criteriaBuilder.like(root.get(attribute.getName()), "%" + value + "%"))
                .toArray(Predicate[]::new));
    }
}
