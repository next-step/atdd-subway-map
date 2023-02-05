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

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철구간 관리")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    StationAcceptanceTest stationAcceptanceTest;
    LineAcceptanceTest lineAcceptanceTest;

    private Long 강남역;
    private Long 삼성역;
    private Long 잠실역;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();

        stationAcceptanceTest = new StationAcceptanceTest();
        lineAcceptanceTest = new LineAcceptanceTest();
        강남역 = stationAcceptanceTest.지하철역_생성("강남역");
        삼성역 = stationAcceptanceTest.지하철역_생성("삼성역");
        잠실역 = stationAcceptanceTest.지하철역_생성("잠실역");
    }

    /**
     * Given 노선 생성시 구간 함께 등록
     * When 새로운 구간 등록
     * Then 지하철 노선을 조회시 모든 지하철이 조회 된다.
     */
    @Test
    @DisplayName("성공: 구간 등록")
    void success_add_section() {
        //given
        Long 이호선 = lineAcceptanceTest.지하철_노선_생성("이호선", "green", 강남역, 삼성역, 5L);
        //when
        ExtractableResponse<Response> putResponse = 구간_등록_요청(이호선, 삼성역, 잠실역, 3L);
        //then
        ExtractableResponse<Response> postResponse = lineAcceptanceTest.지하철_노선_조회(이호선);
        assertAll(
                () -> assertEquals(putResponse.statusCode(), HttpStatus.OK.value()),
                () -> assertThat(postResponse.jsonPath().getList("stations.name"))
                        .hasSize(3)
                        .containsExactly("강남역", "삼성역", "잠실역")
        );
    }

    /**
     * Given 노선 생성시 구간 함께 등록
     * When 기존|상행ID:1 하행ID:2| 새로운구간|상행ID:3 하행ID:2|등록
     * Then 기존의 하행과 새로운 구간의 상행이 일치하지 않아 에러 발생
     */
    @Test
    @DisplayName("실패: 구간 등록 - 기존구간의 하행과 새로운구간의 상행이 일치하지 않음")
    void fail_add_section() {
        //given
        Long 이호선 = lineAcceptanceTest.지하철_노선_생성("이호선", "green", 강남역, 삼성역, 5L);
        //when
        ExtractableResponse<Response> putResponse = 구간_등록_요청(이호선, 잠실역, 삼성역, 3L);
        //then
        assertAll(
                () -> assertEquals(putResponse.statusCode(), HttpStatus.BAD_REQUEST.value()),
                () -> assertEquals(putResponse.jsonPath().get("message"),
                        "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다")
        );
    }

    /**
     * Given 노선 생성시 구간 함께 등록 and 새로운 구간 추가
     * When 마지막 구간을 삭제
     * Then 지하철 노선을 조회시 삭제된 구간은 조회되지 않는다.
     */
    @Test
    @DisplayName("성공: 구간 삭제")
    void success_delete_section() {
        //given
        Long 이호선 = lineAcceptanceTest.지하철_노선_생성("이호선", "green", 강남역, 삼성역, 5L);
        ExtractableResponse<Response> putResponse = 구간_등록_요청(이호선, 삼성역, 잠실역, 3L);
        //when
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(이호선, 잠실역);
        //then
        ExtractableResponse<Response> postResponse = lineAcceptanceTest.지하철_노선_조회(이호선);
        assertAll(
                () -> assertEquals(putResponse.statusCode(), HttpStatus.OK.value()),
                () -> assertEquals(deleteResponse.statusCode(), HttpStatus.NO_CONTENT.value()),
                () -> assertThat(postResponse.jsonPath().getList("stations.name"))
                        .hasSize(2)
                        .containsExactly("강남역", "삼성역")
        );
    }

    /**
     * Given 생성시 구간 함께 등록 and 새로운 구간 추가
     * When 마지막구간에 등록된 지하철 삭제 요청 한다.
     * Then 에러 발생
     */
    @Test
    @DisplayName("실패: 구간 삭제 - 마지막 구간이 아닌 지하철 삭제요청")
    void fail_delete_section2() {
        //given
        Long 이호선 = lineAcceptanceTest.지하철_노선_생성("이호선", "green", 강남역, 삼성역, 5L);
        ExtractableResponse<Response> putResponse = 구간_등록_요청(이호선, 삼성역, 잠실역, 3L);
        //when
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(이호선, 삼성역);
        //then
        assertAll(
                () -> assertEquals(deleteResponse.statusCode(), HttpStatus.BAD_REQUEST.value()),
                () -> assertEquals(deleteResponse.jsonPath().get("message"),
                        "지하철 노선에 등록된 마지막 구간만 제거할 수 있습니다")
        );
    }

    /**
     * Given 지하철 노선에 새로운 구간을 추가한다.
     * When 구간이 1개인 노선에 구간 삭제를 요청한다
     * Then 구간이 1개인 경우 역을 삭제 할 수 없다.
     */
    @Test
    @DisplayName("실패: 구간 삭제 - 구간이 1개인 경우")
    void fail_delete_section() {
        //given
        Long 이호선 = lineAcceptanceTest.지하철_노선_생성("이호선", "green", 강남역, 삼성역, 5L);
        //when
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(이호선, 잠실역);
        //then
        assertAll(
                () -> assertEquals(deleteResponse.statusCode(), HttpStatus.BAD_REQUEST.value()),
                () -> assertEquals(deleteResponse.jsonPath().get("message"), "구간이 1개인 경우 역을 삭제할 수 없습니다")
        );
    }

    ExtractableResponse<Response> 구간_등록_요청(Long lineId, Long upStationId, Long downStationId, Long distance) {
        Map<String, Long> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", lineId)
                .body(params)
                .when()
                .put("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }

    ExtractableResponse<Response> 구간_삭제_요청(Long lineId, Long stationId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", lineId)
                .param("stationId", stationId)
                .when()
                .delete("/lines/{id}/sections")
                .then().log().all()
                .extract();
    }
}
