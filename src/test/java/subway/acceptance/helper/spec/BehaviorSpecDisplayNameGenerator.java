package subway.acceptance.helper.spec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGenerator;

public class BehaviorSpecDisplayNameGenerator extends DisplayNameGenerator.Standard {

    private static final List<Class<? extends Annotation>> NESTED_ANNOTATIONS = List.of(Given.class, When.class);
    private static final List<Class<? extends Annotation>> METHOD_ANNOTATIONS = List.of(Then.class);

    @Override
    public String generateDisplayNameForClass(final Class<?> testClass) {
        return super.generateDisplayNameForClass(testClass);
    }

    @Override
    public String generateDisplayNameForNestedClass(final Class<?> nestedClass) {
        final var displayName = super.generateDisplayNameForNestedClass(nestedClass);

        final var prefix = Arrays.stream(nestedClass.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .filter(NESTED_ANNOTATIONS::contains)
                .findFirst()
                .map(Class::getSimpleName);

        return prefix.map(s -> s + " " + displayName).orElse(displayName);
    }

    @Override
    public String generateDisplayNameForMethod(final Class<?> testClass, final Method testMethod) {
        final var displayName = super.generateDisplayNameForMethod(testClass, testMethod);

        final var prefix = Arrays.stream(testMethod.getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .filter(METHOD_ANNOTATIONS::contains)
                .findFirst()
                .map(Class::getSimpleName);

        return prefix.map(s -> s + " " + displayName).orElse(displayName);
    }
}
