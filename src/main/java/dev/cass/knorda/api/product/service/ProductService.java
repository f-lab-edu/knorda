package dev.cass.knorda.api.product.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.product.exception.ProductNotExistException;
import dev.cass.knorda.domain.product.Product;
import dev.cass.knorda.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	@Transactional(readOnly = true)
	public Product findById(int productId) {
		return productRepository.findFirstByProductId(productId).orElseThrow(ProductNotExistException::new);
	}

	@Transactional
	public Product save(Product product) {
		return productRepository.save(product);
	}

	@Transactional
	public void delete(Product product) {
		product.delete();
		productRepository.save(product);
	}

	@Transactional(readOnly = true)
	public boolean isExistProductName(String productName) {
		return productRepository.existsByName(productName);
	}

	@Transactional(readOnly = true)
	public List<Product> findAllByQuery(Specification<Product> productQuery) {
		return productRepository.findAll(productQuery);
	}

}
