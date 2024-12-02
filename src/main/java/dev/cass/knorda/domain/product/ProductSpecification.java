package dev.cass.knorda.domain.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import dev.cass.knorda.api.product.dto.ProductFindDto;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {

	public static Specification<Product> searchProductQuery(ProductFindDto.GetProductQuery productQuery) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			root.fetch("member", JoinType.LEFT);

			if (productQuery.getProductName() != null) {
				predicates.add(criteriaBuilder.equal(root.get("name"), productQuery.getProductName()));
			}

			if (productQuery.getMemberName() != null) {
				predicates.add(
					criteriaBuilder.equal(root.get("member").get("memberName"), productQuery.getMemberName()));
			}

			if (productQuery.getStartDateTime() != null) {
				predicates.add(
					criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), productQuery.getStartDateTime()));
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), productQuery.getEndDateTime()));
			}

			predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
