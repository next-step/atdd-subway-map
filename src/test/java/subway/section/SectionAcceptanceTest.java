package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간에 대한 인수테스트 입니다.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest implements SectionFixture {

    /**
     * When 지하철 라인에 대한 첫 구간 등록 시
     * Then 조건에 상관없이 등록이 된다.
     */
    @DisplayName("지하철의 새로운 구간을 생성하고 노선 목록 조회 시 생성한 구간을 조회 할 수 있다.")
    @Test
    void createLine_will_success() {
        // when
        ExtractableResponse<Response> response = createSection(
                createStationId("상행선"),
                createStationId("하행선"),
                createLineId()
        );

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철의 구간을 하나 생성하고
     * When 새로운 구간을 생성할 시 상행역이 현재 라인에 마지막 하행역이 아닌 경우
     * Then 예외를 발생시킨다.
     */
    @DisplayName("구간 생성 시 상행역이 현재 라인에 마지막 하행역이 아닌 경우 예외를 발생시킨다.")
    @Test
    void fail_equal_station_direction() {
        // given
        Long lineId = createLineId();
        createSection(createStationId("상행선"), createStationId("하행선"), lineId);

        // when
        ExtractableResponse<Response> response = createSection(createStationId("상행선"), createStationId("하행선"), lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertFail(response, "새로운 구간의 상행역은 해당 노선의 하행 종점역과 일치해야합니다.");
    }

    /**
     * Given 지하철의 구간을 하나 생성하고
     * When 새로운 구간을 생성할 시 하행선이 이미 존재하는 역일 경우
     * Then 예외를 발생시킨다.
     */
    @DisplayName("이미 존재하는 구간을 등록시 예외를 발생시킨다.")
    @Test
    void fail_containsSection() {
        // given
        Long lineId = createLineId();
        Long upStationId = createStationId("상행선");
        Long downStationId = createStationId("하행선");

        createSection(upStationId, downStationId, lineId);

        // when
        ExtractableResponse<Response> response = createSection(downStationId, upStationId, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertFail(response, "이미 존재하는 역입니다.");
    }

    /**
     * Given 지하철의 구간을 하나 생성하고
     * When 새로운 구간을 생성할 시
     * Then 조건을 충족하면 등록이 된다.
     */
    @DisplayName("조건을 만족하는 구간을 생성하면 정상 등록이 된다.")
    @Test
    void success_create_section() {
        // given
        Long lineId = createLineId();
        Long upStationId = createStationId("A");
        Long downStationId = createStationId("C");

        createSection(upStationId, downStationId, lineId);

        // when
        ExtractableResponse<Response> response = createSection(downStationId, createStationId("B"), lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> createSection(Long upStationId, Long downStationId, Long LineId) {
        Map<String, Object> params = new HashMap<>();
        params.put("distance", 10);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + LineId + "/sections")
                .then().log().all()
                .extract();
        return response;
    }


    private void assertFail(ExtractableResponse<Response> response, String message) {
        String failMessage = response.jsonPath().getString("message");
        assertThat(failMessage).isEqualTo(message);
    }
}
