package dev.cass.knorda.api.product.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.cass.knorda.api.product.dto.ProductFindDto;
import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.facade.ProductFacade;
import dev.cass.knorda.global.controller.V1Controller;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@V1Controller
public class ProductController {
	private final ProductFacade productFacade;

	@GetMapping("/products/{productId}")
	public ResponseEntity<ProductFindDto.GetProductResponse> getProduct(@PathVariable int productId) {
		return ResponseEntity.status(HttpStatus.OK).body(productFacade.getProductById(productId));
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable int productId, HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		productFacade.deleteProduct(productId, loggedInMember);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/products/{productId}")
	public ResponseEntity<ProductRegisterDto.RegisterResponse> updateProduct(@PathVariable int productId,
		@RequestBody @Valid ProductRegisterDto.RegisterRequest registerRequest, HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		return ResponseEntity.status(HttpStatus.OK)
			.body(productFacade.updateProduct(productId, registerRequest, loggedInMember));
	}

	@GetMapping("/products")
	public ResponseEntity<List<ProductFindDto.GetProductResponse>> getProductList(
		@RequestParam(required = false, name = "productName") String productName,
		@RequestParam(required = false, name = "memberName") String memberName,
		@RequestParam(required = false, name = "startDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDateTime,
		@RequestParam(required = false, name = "endDateTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDateTime) {
		ProductFindDto.GetProductQuery productQuery = ProductFindDto.GetProductQuery.builder()
			.productName(productName)
			.memberName(memberName)
			.startDateTime(startDateTime)
			.endDateTime(endDateTime)
			.build();

		return ResponseEntity.status(HttpStatus.OK).body(productFacade.getProductListByQuery(productQuery));
	}

	@PostMapping("/products")
	public ResponseEntity<ProductRegisterDto.RegisterResponse> registerProduct(
		@RequestBody @Valid ProductRegisterDto.RegisterRequest registerRequest, HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(productFacade.registerProduct(registerRequest, loggedInMember));
	}
}
