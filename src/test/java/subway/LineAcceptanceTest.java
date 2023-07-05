package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.factory.LineFactory;
import subway.utils.RestAssuredClient;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static final String path = "/lines";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {
        // when
        RestAssuredClient.requestPost(path, LineFactory.create(LineFactory.LINE_NAMES[0]))
            .statusCode(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> response = RestAssuredClient.requestGet(path)
            .statusCode(HttpStatus.OK.value())
            .extract();

        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames.size()).isEqualTo(1);
        assertThat(lineNames).contains(LineFactory.LINE_NAMES[0]);
    }

}
