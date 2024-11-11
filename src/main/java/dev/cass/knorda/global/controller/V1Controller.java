package dev.cass.knorda.global.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * prefix로 사용할 API 버전을 명시하는 컨트롤러
 * <p>
 * Target - 어노테이션이 적용 가능한 대상을 지정하는 어노테이션
 * ElementType.TYPE - 클래스, 인터페이스, Enum 등에 어노테이션을 적용할 수 있음을 의미
 * Retention - 어노테이션이 유지되는 기간을 지정하는 어노테이션
 * RetentionPolicy.RUNTIME - 런타임까지 어노테이션 정보를 유지하겠다는 의미
 * RestController 클래스에 붙여서, 런타임에 참조할 것이므로 다음과 같이 설정함
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping("/api/v1")
public @interface V1Controller {
}
