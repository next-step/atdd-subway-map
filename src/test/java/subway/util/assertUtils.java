package subway.util;

import static org.assertj.core.api.Assertions.assertThat;

public class assertUtils {
    public static void assertHttpStatus(int actualHttpStatus, int expectHttpStatus) {
        assertThat(actualHttpStatus).isEqualTo(expectHttpStatus);
    }
}
