package subway.common;

import java.util.Objects;
import java.util.function.Consumer;

public class Validation {
    public static <V> void setIfNotNull(V value, Consumer<V> setter) {
        if (Objects.nonNull(value)) {
            setter.accept(value);
        }
    }
}
