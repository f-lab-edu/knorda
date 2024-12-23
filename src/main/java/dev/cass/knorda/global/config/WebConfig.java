package dev.cass.knorda.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import dev.cass.knorda.api.product.converter.MultipartToBytesConverter;

/**
 * Configuration - 클래스가 하나 이상의 @Bean 메소드를 제공하고, 스프링 컨테이너에 의해 관리되어, 런타임에 해당 빈에 대한 정의와 request를 제공함을 의미
 * 해당 클래스가 Singleton으로 생성됨을 보장
 *
 * 스프링에서 제공하는 WebMvcConfigurationSupport를 상속받아서, 기본 설정에 추가적으로 원하는 설정을 추가한다
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	@Override
	protected void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new MultipartToBytesConverter());
	}
}
