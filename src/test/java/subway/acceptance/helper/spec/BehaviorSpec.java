package subway.acceptance.helper.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@DisplayNameGeneration(BehaviorSpecDisplayNameGenerator.class)
@Nested
public @interface BehaviorSpec {
}
