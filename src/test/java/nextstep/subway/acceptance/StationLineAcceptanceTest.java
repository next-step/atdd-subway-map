package nextstep.subway.acceptance;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationLineAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * given 노선을 생성하면
     * when 노선이 생성된다.
     * then 목록 조회시 생성된 노선을 확인할 수 있다.
     */
    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        //given 노선을 생성하면
        Map<String, Object> body = Map.of("name", "신분당선",
                                            "color", "red",
                                            "upStationId", 1,
                                            "downStationId", 3);

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                                                            .body(body)
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .when().log().all()
                                                            .post("/station/line")
                                                            .then().log().all()
                                                            .extract();

        //when 노선이 생성된다
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then 목록 조회 시 생성된 노선을 찾을 수 있다
        ExtractableResponse<Response> selectResponse = RestAssured.given().log().all()
                                                           .when().log().all()
                                                           .get("/station/line")
                                                           .then().log().all()
                                                           .extract();

        assertThat(selectResponse.jsonPath().getList("name")).containsAnyOf("신분당선");
    }


}
