package dev.cass.knorda.api.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {
	@GetMapping("/hello")
	public String hello() {
		return "Hello, world!";
	}
}
