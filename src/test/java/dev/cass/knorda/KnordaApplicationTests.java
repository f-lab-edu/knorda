package dev.cass.knorda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * spring-boot-test 라이브러리에서 제공하는 어노테이션
 * 스프링의 전체 컨텍스트를 로드하여 테스트를 진행하게 해 줌
 * - 특정 빈이나 서비스를 모킹하기 위해서는 @MockBean을 사용해서 목 객체를 주입해야 함
 * SpringBootTest의 인스턴스 변수
 * - properties: 테스트에 사용할 properties 파일을 지정
 * - classes: 테스트에 사용할 Configuration 클래스를 지정 (빈을 생성할 클래스)
 * - webEnvironment: 테스트에 사용할 웹 환경을 지정
 * 		- MOCK: mock servlet 환경을 사용
 * 		- RANDOM_PORT: EmbeddedWebApplicationContext를 로드해서, 실제 서블릿 환경을 구성
 * 		- DEFINED_PORT: 실제 서블릿 환경을 구성하고, 포트는 지정
 * 		- NONE: 웹 환경을 사용하지 않음 (서블릿 환경을 구성하지 않음)
 * - useMainMethod: main 메소드를 사용할 것인지 지정
 * - args: main 메소드에 전달할 인자를 지정
 */
@SpringBootTest
class KnordaApplicationTests {

	/**
	 * JUnit에서 제공하는 어노테이션으로, 해당 메소드가 테스트 메소드임을 명시하는 데 사용
	 * 메소드나 변수가 없는 어노테이션으로, 마킹 어노테이션이라 할 수 있음
	 */
	@Test
	void contextLoads() {
		assert true;
	}
}