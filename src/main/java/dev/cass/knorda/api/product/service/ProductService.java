package dev.cass.knorda.api.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.product.dto.ProductFindDto;
import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.exception.ProductNotExistException;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.product.Product;
import dev.cass.knorda.domain.product.ProductRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public Product findById(int productId) {
		return productRepository.findFirstByProductId(productId).orElseThrow(ProductNotExistException::new);
	}

	public Product save(ProductRegisterDto.RegisterRequest registerRequest, Member member) {
		Product product = registerRequest.toEntity();
		product.setMember(member);
		return productRepository.saveAndFlush(product);
	}

	public void delete(Product product) {
		product.delete();
		productRepository.save(product);
	}

	public Product update(Product product, ProductRegisterDto.RegisterRequest registerRequest) {
		product.update(registerRequest.getProductName(), registerRequest.getDescription());
		return productRepository.saveAndFlush(product);
	}

	@Transactional(readOnly = true)
	public boolean isExistProductName(String productName) {
		return productRepository.existsByName(productName);
	}

	@Transactional(readOnly = true)
	public List<Product> findAllByQuery(ProductFindDto.GetProductQuery productQuery) {
		Specification<Product> specification = (root, query, criteriaBuilder) -> {
			List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

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
		return productRepository.findAll(specification);
	}

}
