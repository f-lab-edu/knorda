package dev.cass.knorda.domain.product;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository
	extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

	Optional<Product> findFirstByProductId(int productId);

	/**
	 * EntityGraph - 연관된 엔티티를 함께 조회할 때 사용 (fetch join)
	 * n+1문제를 방지하기 위해, 상품 리스트를 조회할 떄, fetch 조인을 사용해 member 엔티티도 함께 조회하겠다는 의미
	 */
	@EntityGraph(attributePaths = "member")
	ArrayList<Product> findAllByMemberMemberId(int memberId);

	boolean existsByName(String name);

}
