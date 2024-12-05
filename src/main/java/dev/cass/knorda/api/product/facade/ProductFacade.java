package dev.cass.knorda.api.product.facade;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.member.service.MemberService;
import dev.cass.knorda.api.product.dto.ProductFindDto;
import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.exception.AlreadyExistProductNameException;
import dev.cass.knorda.api.product.exception.ProductNotOwnedByLoggedInMemberException;
import dev.cass.knorda.api.product.service.ProductService;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.product.Product;
import dev.cass.knorda.domain.product.ProductSpecification;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFacade {
	private final ProductService productService;
	private final MemberService memberService;

	@Transactional(readOnly = true)
	public ProductFindDto.GetProductResponse getProductById(int productId) {
		return ProductFindDto.GetProductResponse.of(productService.findById(productId));
	}

	@Transactional
	public ProductRegisterDto.RegisterResponse registerProduct(ProductRegisterDto.RegisterRequest registerRequest,
		String memberName) {
		if (productService.isExistProductName(registerRequest.getProductName())) {
			throw new AlreadyExistProductNameException();
		}
		Member member = memberService.findMemberByMemberName(memberName);
		Product product = registerRequest.toEntity();
		product.setMember(member);
		return ProductRegisterDto.RegisterResponse.of(productService.save(product));
	}

	@Transactional(readOnly = true)
	public List<ProductFindDto.GetProductResponse> getProductListByQuery(ProductFindDto.GetProductQuery productQuery) {
		return productService.findAllByQuery(ProductSpecification.searchProductQuery(productQuery))
			.stream()
			.map(ProductFindDto.GetProductResponse::of)
			.toList();
	}

	@Transactional
	public void deleteProduct(int productId, String loggedInMember) {
		Product product = productService.findById(productId);
		if (!product.getMember().getMemberName().equals(loggedInMember)) {
			throw new ProductNotOwnedByLoggedInMemberException();
		}
		productService.delete(product);
	}

	@Transactional
	public ProductRegisterDto.RegisterResponse updateProduct(int productId,
		ProductRegisterDto.RegisterRequest registerRequest, String loggedInMember) {
		Product product = productService.findById(productId);
		if (!product.getMember().getMemberName().equals(loggedInMember)) {
			throw new ProductNotOwnedByLoggedInMemberException();
		}
		if (!product.getName().equals(registerRequest.getProductName())
			&& productService.isExistProductName(registerRequest.getProductName())) {
			throw new AlreadyExistProductNameException();
		}

		product.update(registerRequest.getProductName(), registerRequest.getDescription());
		return ProductRegisterDto.RegisterResponse.of(productService.save(product));
	}
}
