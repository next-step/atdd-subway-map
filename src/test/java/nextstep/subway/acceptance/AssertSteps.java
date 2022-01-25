package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertSteps {

    public static void httpStatusCode_검증(int actual, int expected) {
        assertThat(actual).isEqualTo(expected);
    }

}
