package subway.acceptance.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Nested
@Test
public @interface Then {
}
