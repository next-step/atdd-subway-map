package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import subway.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철노선 관리")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    StationRepository stationRepository;

    StationAcceptanceTest stationAcceptanceTest;

    Long 강남역;
    Long 잠실역;
    Long 천호역;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();

        stationAcceptanceTest = new StationAcceptanceTest();
        강남역 = stationAcceptanceTest.지하철역_생성("강남역");
        잠실역 = stationAcceptanceTest.지하철역_생성("잠실역");
        천호역 = stationAcceptanceTest.지하철역_생성("천호역");
    }

    @DisplayName("지하철노선 생성 성공")
    @Test
    void createLine() {
        지하철_노선_생성("이호선", "green", 강남역, 잠실역, 5L);
        List<String> lineNames = 지하철_노선_목록_조회();
        assertThat(lineNames)
                .hasSize(1)
                .containsAnyOf("이호선");
    }

    @DisplayName("지하철노선 2개 생성 성공")
    @Test
    void create_2_Line() {
        지하철_노선_생성("이호선", "green", 강남역, 잠실역, 10L);
        지하철_노선_생성("팔호선", "green", 잠실역, 천호역, 15L);

        List<String> lineNames = 지하철_노선_목록_조회();
        assertThat(lineNames)
                .hasSize(2)
                .contains("이호선", "팔호선");
    }

    @DisplayName("지하철노선 조회 성공")
    @Test
    void find_Line() {
        Long 이호선 = 지하철_노선_생성("이호선", "green", 강남역, 잠실역, 5L);
        ExtractableResponse<Response> getResponse = 지하철_노선_조회(이호선);
        assertAll(
                () -> assertEquals(getResponse.statusCode(), HttpStatus.OK.value()),
                () -> assertEquals(getResponse.jsonPath().get("name"), "이호선")
        );
    }

    @DisplayName("지하철노선 수정 성공")
    @Test
    void modify_line() {
        Long 이호선 = 지하철_노선_생성("이호선", "green", 강남역, 잠실역, 5L);
        ExtractableResponse<Response> putResponse = 지하철_노선_수정(이호선, "신분당선", "blue");
        ExtractableResponse<Response> getResponse = 지하철_노선_조회(이호선);
        assertAll(
                () -> assertEquals(putResponse.statusCode(), HttpStatus.OK.value()),
                () -> assertEquals(getResponse.statusCode(), HttpStatus.OK.value()),
                () -> assertEquals(getResponse.jsonPath().get("name"), "이호선")
        );
    }

    @DisplayName("지하철노선 삭제 성공")
    @Test
    void delete_line() {
        Long 이호선 = 지하철_노선_생성("이호선", "green", 강남역, 잠실역, 5L);
        지하철_노선_삭제(이호선);
    }

    private ExtractableResponse<Response> 지하철_노선_삭제(Long id) {
        return given().log().all()
                    .pathParam("id", id)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .delete("/lines/{id}")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .extract();
    }

    ExtractableResponse<Response> 지하철_노선_수정(Long id, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return given().log().all()
                    .body(params)
                    .pathParam("id", id)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .put("/lines/{id}")
                .then().log().all()
                    .extract();
    }

    Long 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));

        return given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract().jsonPath().getLong("id");
    }

    List<String> 지하철_노선_목록_조회() {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .get("/lines")
                .then().log().all()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract().jsonPath().getList("name", String.class);
    }

    ExtractableResponse<Response> 지하철_노선_조회(Long id) {
        return given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", id)
                .when()
                    .get("/lines/{id}")
                .then().log().all()
                    .extract();
    }
}
