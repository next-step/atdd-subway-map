package subway.acceptance.helper.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DisplayNameGeneration(BehaviorSpecDisplayNameGenerator.class)
@Nested
@SpringBootTest
public @interface Given {
}
