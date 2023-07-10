package subway.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    int port;
    @BeforeEach
    void setUp() {
        RestAssured.port = port;

    }

    /**
     * Given 지하철역이 4개가 등록되어있다.
     * Given 지하철 노선이 1개가 등록되어있다.
     * When 지하철 노선에 구간을 등록한다
     * Then 지하철 노선 조회 시, 노선의 하행역은 등록한 구간의 하행역이어야한다.
     * Then 지하철 노선의 길이가, 기존 노선의 길이와 등록한 구간의 길이의 합이어야한다.
     */



    @DisplayName("지하찰_구간_등록")
    @Test
    void createStation() {
        지하철_구간_생성(1L, new SectionRequest());
    }

    private static void 지하철_구간_생성(Long lineId, SectionRequest reqeust) {
        RestAssured.given().log().all()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reqeust)
                .post(String.format("/lines/%d/sections", lineId))
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
    /**
     * Given 지하철역이 4개가 등록되어있다.
     * And 지하철 노선이 1개가 등록되어있다.
     * And 지하철 구간이 등록되어 있다.
     * When 지하철 구간을 제거한다.
     * Then 지하철 노선 조회 시, 하행역은 제거한 구간의 상행역이어야한다.
     * Then 지하철 노선의 길이가, 기존 노선의 길이에서 제거한 구간의 길이를 뺀 값이어야한다.
     */
}
