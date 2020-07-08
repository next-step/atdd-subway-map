package nextstep.subway.common.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.AbstractIntegerAssert;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonAcceptanceStep {

    public static void API_응답코드_검사(int actualStatus, HttpStatus expectedStatus) {
        assertThat(actualStatus).isEqualTo(expectedStatus.value());
    }
}
