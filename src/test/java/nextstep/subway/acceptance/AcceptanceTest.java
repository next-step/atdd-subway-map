package nextstep.subway.acceptance;

import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AcceptanceTest {
    static void 지하철_API_응답_확인(int statusCode, HttpStatus status) {
        assertThat(statusCode).isEqualTo(status.value());
    }
}
