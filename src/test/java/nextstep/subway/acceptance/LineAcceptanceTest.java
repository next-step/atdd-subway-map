package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /*
    When 지하철 노선을 생성하면
    Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선 생성")
    void getLines() {
        //when
        final Map<String, Object> param = Map.of(
            "name", "신분당선",
            "color", "bg-red-600",
            "upStationId", 1,
            "downStationId", 2,
            "distance", 10
        );

        RestAssured.given().log().all()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines")
            .then();

        //then
        final ExtractableResponse<Response> lineResponse = RestAssured.given().log().all()
            .when().get("/lines")
            .then().log().all()
            .extract();

        final List<String> name = lineResponse.jsonPath().getList("name", String.class);
        assertThat(name).containsExactly("신분당선");
    }

}
