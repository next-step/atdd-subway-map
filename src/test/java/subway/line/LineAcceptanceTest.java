package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.station.domain.Station;

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
        /* # API 명세
         *
         * ## Request
         * POST /lines
         * Content-Type: application/json
         * Body
         * - name : 지하철 노선 이름 (ex: "신분당선")
         * - color : 지하철 노선 색상 (ex: "bg-red-600")
         * - upStationId : 상행 측면의 역 (ex: "상행 A - B 하행" 일 때 upStation=A)
         * - downStationId : 하행 측면의 역 (ex: "상행 A - B 하행" 일 때 downStation=B)
         * - distance: upStation과 downStation간의 거리
         *
         * ## Response
         * 201 Created
         * Location: /lines/1
         * Content-Type: application/json
         * - id : 생성된 지하철 노선의 id값 (ex: 1)
         * - name : 생성된 지하철 노선의 이름 (요청 명세 참고)
         * - color : 생성된 지하철 노선 색상 (요청 명세 참고)
         * - stations[] : 해당 노선에 속한 상행 지하철역과 하행 지하철역 리스트
         */

        // when
        Map<String, Object> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upstationId", 1L,
                "downStationId", 2L,
                "distance", 10
        );

        ExtractableResponse<Response> responseOfCreate = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(responseOfCreate.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> responseOfRead = RestAssured.given().log().all()
                .pathParam("id", 1L)  // lineId를 넣어야 한다
                .when().get("/lines")
                .then().log().all()
                .extract();

        long lineId = responseOfRead.jsonPath().getLong("id");
        List<Station> stations = responseOfRead.jsonPath().getList("stations", Station.class);

        assertThat(lineId).isEqualTo(1L);  // lineId를 넣어야 한다
        assertThat(stations).hasSize(2);  // 상행과 하행 두개가 있으므로 size는 2여야 한다
    }

    /**
     * Given: 2개의 지하철 노선을 생성하고
     * When: 지하철 노선 목록을 조회하면
     * Then: 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @Test
    void findAllLines() {

    }

    /**
     * Given: 지하철 노선을 생성하고
     * When: 생성한 지하철 노선을 조회하면
     * Then: 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @Test
    void findLine() {

    }

    /**
     * Given: 지하철 노선을 생성하고
     * When: 생성한 지하철 노선을 삭제하면
     * Then: 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void deleteLine() {

    }
}
