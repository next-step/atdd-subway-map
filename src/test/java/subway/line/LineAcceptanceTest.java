package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * 프로그래밍 요구사항
 * - 아래의 순서로 기능을 구현한다
 *   1. 인수 조건을 검증하는 인수 테스트를 작성한다
 *   2. 인수 테스트를 충족하는 기능을 구현한다
 * - 인수 테스트의 결과가 서로 영향을 끼치지 않도록 인수테스트를 서로 격리시킨다
 * - 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링한다
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When: 지하철 노선을 생성하면
     * Then: 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void createLine() {
        지정된_이름의_지하철역을_생성한다("강남역");
        지정된_이름의_지하철역을_생성한다("양재역");

        // when
        ExtractableResponse<Response> responseOfCreate = 지하철_노선을_생성한다();

        // then
        assertThat(responseOfCreate.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //TODO: 아래 인수 테스트는 조회 로직 구현 후에 다시해보기
//        long id = responseOfCreate.jsonPath().getLong("id");
//        ExtractableResponse<Response> responseOfRead = RestAssured.given().log().all()
//                .pathParam("id", id)  // lineId를 넣어야 한다
//                .when().get("/lines/{id}")
//                .then().log().all()
//                .extract();
//
//        long lineId = responseOfRead.jsonPath().getLong("id");
//        List<Station> stations = responseOfRead.jsonPath().getList("stations", Station.class);
//
//        assertThat(lineId).isEqualTo(1L);  // lineId를 넣어야 한다
//        assertThat(stations).hasSize(2);  // 상행과 하행 두개가 있으므로 size는 2여야 한다
    }

    //TODO: 해당 로직은 StationAcceptanceTest와 중복이 된다. 어떻게 해결할 지 고민
    private void 지정된_이름의_지하철역을_생성한다(String stationName) {
        Map<String, String> params = Map.of("name", stationName);

        RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all();
    }

    private ExtractableResponse<Response> 지하철_노선을_생성한다() {
        Map<String, Object> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1L,  //TODO 고정된 id값이 아닌, 저장된 역의 id값을 사용해야 함
                "downStationId", 2L,
                "distance", 10
        );

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    /**
     * Given: 지하철 노선을 생성하고
     * When: 생성한 지하철 노선을 조회하면
     * Then: 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @Test
    void findLine() {
        /* # API 명세
         *
         * ## Request
         * GET /lines/{id}
         * Accept: application/json
         *
         * ## Response
         * status: 200 OK
         * Content-Type: application/json
         * Body
         * - id : 생성된 지하철 노선의 id값 (ex: 1)
         * - name : 생성된 지하철 노선의 이름 ("신분당선")
         * - color : 생성된 지하철 노선 색상 ("bg-red-600")
         * - stations[] : 해당 노선에 속한 상행 지하철역과 하행 지하철역 리스트
         *   - id : 지하철역 id
         *   - name : 지하철역 이름
         */

        // given
        지정된_이름의_지하철역을_생성한다("강남역");
        지정된_이름의_지하철역을_생성한다("양재역");
        지하철_노선을_생성한다();

        // when
        ExtractableResponse<Response> result = RestAssured.given().log().all()
                .pathParam("id", 1L)
                .when().get("/lines/{id}")
                .then().log().all()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.jsonPath().getLong("id")).isEqualTo(1L);
    }

    /**
     * Given: 2개의 지하철 노선을 생성하고
     * When: 지하철 노선 목록을 조회하면
     * Then: 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @Test
    void findAllLines() {
        /* # API 명세
         *
         * ## Request
         *
         * ## Response
         */
    }

    /**
     * Given: 지하철 노선을 생성하고
     * When: 생성한 지하철 노선을 삭제하면
     * Then: 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void deleteLine() {
        /* # API 명세
         *
         * ## Request
         *
         * ## Response
         */
    }
}
