package dev.cass.knorda.api.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * REST API를 제공하는 컨트롤러
 * @Controller + @ResponseBody = @RestController
 * 즉 반환값은 view가 아닌 HTTP Response Body이며, 또한 DispatcherServlet에 의해서 요청을 처리할 대상이기도 함을 의미
 */
@RestController
public class HelloRestController {

	/*
	 * HTTP GET 요청을 처리하는 메소드
	 * @RequestMapping(method = RequestMethod.GET)과 동일한 기능
	 * 즉 URI에 해당하는 요청을 처리하는 메소드이면서, 그 중 GET 요청을 처리하는 메소드임을 의미
	 */
	@GetMapping("/hello")
	public SimpleResponseDto hello() {
		return new SimpleResponseDto("Hello, world!");
	}
}
