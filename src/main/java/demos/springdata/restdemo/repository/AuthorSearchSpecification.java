package demos.springdata.restdemo.repository;

import demos.springdata.restdemo.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AuthorSearchSpecification implements Specification<User> {

    private final String authorName;
    private final String postTitle;

    public AuthorSearchSpecification(String authorName, String postTitle) {
        this.authorName = authorName;
        this.postTitle = postTitle;
    }


    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
