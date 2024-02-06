package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Sql(scripts = "/truncate.sql", executionPhase = BEFORE_TEST_METHOD)
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

    /**
     * Given 지하철 노선의 구간을 생성 한 후
     * When 특정 구간을 삭제할 때 등록된 구간의 마지막 노선과 일치하지 않는다면
     * Then 예외를 발생시킨다.
     */
    @DisplayName("구간에 대한 삭제는 등록된 마지막 노선에 대해서만 가능하며 아닐 시 예외를 발생시킨다.")
    @Test
    void fail_delete_station_isLastDownStation() {
        // given
        Long lineId = createLineId();
        Long stationId = createStationId("상행선1");
        Long downStationId = createStationId("하행선1");
        createSection(stationId, downStationId, lineId);
        createSection(downStationId, createStationId("하행선2"), lineId);

        // when
        ExtractableResponse<Response> response = deleteSection(lineId, stationId);

        // then
        assertFail(response, "하행 종점역과 다릅니다. 하행 종점역만 삭제가 가능합니다.");
    }

    /**
     * Given 지하철 노선의 구간을 생성 한 후
     * When 특정 구간을 삭제 시 현재 남아있는 구간이 한개이면
     * Then 예외를 발생시킨다.
     */
    @DisplayName("구간삭제 시 남아있는 구간이 한개이면 예외를 발생시킨다.")
    @Test
    void fail_station_hasMoreThanOne() {
        // given
        Long lineId = createLineId();
        Long stationId = createStationId("하행선1");
        createSection(createStationId("상행선1"), stationId, lineId);

        // when
        ExtractableResponse<Response> response = deleteSection(lineId, stationId);

        // then
        assertFail(response, "노선의 구간이 하나인 경우 삭제가 불가합니다.");
    }

    /**
     * Given 지하철 노선의 구간을 생성 한 후
     * When 특정 구간을 삭제 시 조건에 통과되면
     * Then 정상적으로 진행이 된다.
     */
    @DisplayName("조건을 만족할 경우 정상 삭제가 진행된다.")
    @Test
    void success_delete_station() {
        // given
        Long lineId = createLineId();
        Long stationId1 = createStationId("하행선1");
        Long stationId2 = createStationId("하행선2");
        createSection(createStationId("상행선1"), stationId1, lineId);
        createSection(stationId1, stationId2, lineId);

        // when
        ExtractableResponse<Response> response = deleteSection(lineId, stationId2);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all()
                .extract();
        return response;
    }
}
