package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.enums.exception.ErrorCode;
import nextstep.subway.util.DataBaseClean;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Sql("/truncate.sql")
public class AbstractAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DataBaseClean dataBaseClean;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        dataBaseClean.execute();
    }

    public static void 상태코드_검증(ExtractableResponse<Response> 결과, HttpStatus httpStatus) {
        assertThat(결과.statusCode()).isEqualTo(httpStatus.value());
    }

    public static void 상태메세지_검증(ExtractableResponse<Response> 결과, ErrorCode errorCode) {
        String message = 결과.jsonPath().getString("message");
        assertThat(message).isEqualTo(errorCode.getMessage());
    }
}
