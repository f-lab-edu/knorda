package dev.cass.knorda.api.product.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.product.dto.ProductFindDto;
import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.facade.ProductFacade;
import dev.cass.knorda.global.exception1.GlobalExceptionHandler;
import dev.cass.knorda.global.util.SessionManageUtils;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
	@InjectMocks
	private ProductController productController;

	@Mock
	private ProductFacade productFacade;

	private MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(productController)
			.setControllerAdvice(GlobalExceptionHandler.class).build();

		/*
		 * Java 8의 새로운 날짜와 시간 타입인 LocalDateTime, LocalDate, LocalTime 은 Jackson 라이브러리가 기본적으로 직렬화/역직렬화를 지원하지 않음
		 * 그래서 해당 날짜 타입들을 처리할 수 있는 모듈인 jackon-datatype-jsr310을 implement 해줘야 하고,
		 * 그 모듈을 objectMapper에서 사용하기 위해 register해줘야 정상적으로 동작한다
		 */
		objectMapper.registerModule(new JavaTimeModule());
	}

	@DisplayName("상품번호로 상품조회")
	@Test
	void findById() throws Exception {
		// given
		int productId = 1;
		ProductFindDto.GetProductResponse getProductResponse = new ProductFindDto.GetProductResponse(productId,
			"Product 1", null, "description", "admin",
			LocalDateTime.of(2020, 10, 10, 0, 0, 0),
			LocalDateTime.of(2020, 10, 10, 0, 0, 0));

		doReturn(getProductResponse).when(productFacade).getProductById(productId);

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/products/{productId}", productId));

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(getProductResponse)));
	}

	@DisplayName("상품번호로 상품 삭제")
	@Test
	void deleteById() throws Exception {
		// given
		int productId = 1;
		String name = "admin";
		int userId = 1;

		doNothing().when(productFacade).deleteProduct(productId, name);

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/products/{productId}", productId)
			.sessionAttr(SessionManageUtils.SESSION_USER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions
			.andExpect(status().isNoContent());
		verify(productFacade, times(1)).deleteProduct(productId, name);
	}

	@DisplayName("상품번호로 수정")
	@Test
	void updateById() throws Exception {
		// given
		int productId = 1;
		String name = "admin";
		int userId = 1;
		ProductRegisterDto.RegisterRequest updateProductRegisterRequest = new ProductRegisterDto.RegisterRequest(
			"Product 1 new",
			"Product 1 description new");
		ProductRegisterDto.RegisterResponse getProductRegisterResponse = new ProductRegisterDto.RegisterResponse(
			productId,
			"Product 1 new", "Product 1 description new",
			LocalDateTime.of(2020, 10, 10, 0, 0, 0));

		doReturn(getProductRegisterResponse).when(productFacade)
			.updateProduct(eq(productId), any(ProductRegisterDto.RegisterRequest.class), eq(name));

		// when
		ResultActions resultActions = mockMvc.perform(put("/api/v1/products/{productId}", productId)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(updateProductRegisterRequest))
			.sessionAttr(SessionManageUtils.SESSION_USER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(getProductRegisterResponse)));
	}

	@DisplayName("상품번호로 수정 - 이름이 없음")
	@Test
	void updateByIdWithoutName() throws Exception {
		// given
		int productId = 1;
		String name = "admin";
		int userId = 1;
		ProductRegisterDto.RegisterRequest updateProductRegisterRequest = new ProductRegisterDto.RegisterRequest(null,
			"Product 1 description new");

		// when
		ResultActions resultActions = mockMvc.perform(put("/api/v1/products/{productId}", productId)
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(updateProductRegisterRequest))
			.sessionAttr(SessionManageUtils.SESSION_USER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions
			.andExpect(status().isBadRequest());
	}

	@DisplayName("상품 쿼리 조회")
	@Test
	void getProductList() throws Exception {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery("Product 3", "admin", null,
			null);
		ProductFindDto.GetProductResponse getProductResponse = new ProductFindDto.GetProductResponse(3,
			"Product 3", null, "description", "admin",
			LocalDateTime.of(2020, 10, 10, 0, 0, 0),
			LocalDateTime.of(2020, 10, 10, 0, 0, 0));

		doReturn(List.of(getProductResponse)).when(productFacade)
			.getProductListByQuery(any(ProductFindDto.GetProductQuery.class));

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/products")
			.param("productName", productQuery.getProductName())
			.param("memberName", productQuery.getMemberName()));

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(List.of(getProductResponse))));
	}

	@DisplayName("상품 쿼리 조회 - 시작날짜가 없음")
	@Test
	void getProductListWithoutStartDate() throws Exception {
		// given
		String productName = "Product 3";
		String memberName = "admin";
		String startDateTime = LocalDateTime.of(2020, 10, 10, 0, 0, 0)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/products")
			.param("productName", productName)
			.param("memberName", memberName)
			.param("startDateTime", startDateTime));

		// then
		resultActions
			.andExpect(status().isBadRequest());
	}

	@DisplayName("상품 쿼리 조회 - 시작 날짜가 끝 날짜보다 뒤임")
	@Test
	void getProductListStartDateAfterEndDate() throws Exception {
		// given
		String productName = "Product 3";
		String memberName = "admin";
		String startDateTime = LocalDateTime.of(2020, 10, 10, 0, 0, 0)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		String endDateTime = LocalDateTime.of(2019, 10, 10, 0, 0, 0)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/products")
			.param("productName", productName)
			.param("memberName", memberName)
			.param("startDateTime", startDateTime)
			.param("endDateTime", endDateTime));

		// then
		resultActions
			.andExpect(status().isBadRequest());
	}

	@DisplayName("상품 쿼리 조회 - 모든 값이 없음")
	@Test
	void getProductListWithoutAll() throws Exception {
		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/products"));

		// then
		resultActions
			.andExpect(status().isBadRequest());
	}

	@DisplayName("상품 등록")
	@Test
	void registerProduct() throws Exception {
		// given
		String name = "admin";
		int userId = 1;
		ProductRegisterDto.RegisterRequest registerProductRegisterRequest = new ProductRegisterDto.RegisterRequest(
			"Product 1",
			"Product 1 description");

		ProductRegisterDto.RegisterResponse getProductRegisterResponse = new ProductRegisterDto.RegisterResponse(1,
			"Product 1", "Product 1 description",
			LocalDateTime.of(2020, 10, 10, 0, 0, 0));

		doReturn(getProductRegisterResponse).when(productFacade)
			.registerProduct(any(ProductRegisterDto.RegisterRequest.class), eq(name));

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/products")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(registerProductRegisterRequest))
			.sessionAttr(SessionManageUtils.SESSION_USER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(getProductRegisterResponse)));
	}

	@DisplayName("상품 등록 Validation 실패")
	@Test
	void registerProductNotValid() throws Exception {
		// given
		String name = "admin";
		int userId = 1;
		ProductRegisterDto.RegisterRequest registerProductRegisterRequest = new ProductRegisterDto.RegisterRequest(
			"Product 1",
			null);

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/products")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(registerProductRegisterRequest))
			.sessionAttr(SessionManageUtils.SESSION_USER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions
			.andExpect(status().isBadRequest());
	}
}