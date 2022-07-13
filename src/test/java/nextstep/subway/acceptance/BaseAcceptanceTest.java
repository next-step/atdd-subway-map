package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.BaseSpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseAcceptanceTest extends BaseSpringBootTest {

    @LocalServerPort
    protected int port;


    protected void checkResponseStatus(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }


}
