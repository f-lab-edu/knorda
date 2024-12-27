package dev.cass.knorda.global.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target(ElementType.PARAMETER) - 이 어노테이션을 적용할 대상을 지정
 * 매개변수에 사용할 것이므로 PARAMETER로 지정함
 * @Retention(RetentionPolicy.RUNTIME) - 어노테이션 정보를 어디까지 유지할 것인지 설정
 * 런타임 시에도 사용되므로 RUNTIME으로 지정함 (메소드 호출 시마다 매개 변수에 지정한 값을 넘겨줄 것이므로)
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberSession {
}
