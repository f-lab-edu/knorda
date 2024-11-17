package dev.cass.knorda.api.product.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.cass.knorda.api.member.service.MemberService;
import dev.cass.knorda.api.product.dto.ProductFindDto;
import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.exception.AlreadyExistProductNameException;
import dev.cass.knorda.api.product.exception.NotYourProductException;
import dev.cass.knorda.api.product.service.ProductService;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.product.Product;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductFacade {
	private final ProductService productService;
	private final MemberService memberService;

	public ProductFindDto.GetProductResponse getProductById(int productId) {
		return ProductFindDto.GetProductResponse.of(productService.findById(productId));
	}

	public ProductRegisterDto.response registerProduct(ProductRegisterDto.request request, String memberName) {
		Member member = memberService.findMemberByMemberName(memberName);
		if (productService.isExistProductName(request.getProductName())) {
			throw new AlreadyExistProductNameException();
		}
		return ProductRegisterDto.response.of(productService.save(request, member));
	}

	public List<ProductFindDto.GetProductResponse> getProductListByQuery(ProductFindDto.GetProductQuery productQuery) {
		return productService.findAllByQuery(productQuery).stream()
			.map(ProductFindDto.GetProductResponse::of)
			.toList();
	}

	public void deleteProduct(int productId, String loggedInUser) {
		Product product = productService.findById(productId);
		if (!product.getMember().getMemberName().equals(loggedInUser)) {
			throw new NotYourProductException();
		}
		productService.delete(product);
	}

	public ProductRegisterDto.response updateProduct(int productId, ProductRegisterDto.request request,
		String loggedInUser) {
		Product product = productService.findById(productId);
		if (!product.getMember().getMemberName().equals(loggedInUser)) {
			throw new NotYourProductException();
		}
		if (!product.getName().equals(request.getProductName()) && productService.isExistProductName(
			request.getProductName())) {
			throw new AlreadyExistProductNameException();
		}
		return ProductRegisterDto.response.of(productService.update(product, request));
	}
}
