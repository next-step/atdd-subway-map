package subway.acceptance.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Nested
@SpringBootTest
public @interface Given {
}
