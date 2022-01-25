package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.steps.SectionSteps;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.steps.LineSteps.지하철노선_생성요청2;
import static nextstep.subway.acceptance.steps.SectionSteps.지하철노선_구간생성요청;
import static nextstep.subway.acceptance.steps.StationSteps.지하철역_생성요청;

@DisplayName("지하철 노선의 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest{

    long lineId;
    long upStationId;
    long downStationId;

    @BeforeEach
    @Override
    public void setUp() {
        RestAssured.port = port;

        upStationId = 지하철역_생성요청("신도림역").jsonPath().getLong("id");
        downStationId = 지하철역_생성요청("문래역").jsonPath().getLong("id");

        LineRequest request = LineRequest.builder()
                .name("2호선")
                .color("bg-green")
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(7)
                .build();

        lineId = 지하철노선_생성요청2(request).jsonPath().getLong("id");
    }

    // 지하철 노선 생성 부분을 setUp 부분으로 분리시켰는데 시나리오 Given에다 적어야 하는가...?
    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 구간의 상행역이 현재 등록되어있는 하행 종점역이 아니고
     * When 새로운 구간의 생성을 요청하면
     * Then 새로운 구간 생성이 실패한다.
     */
    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void validateSectionUpStation() {
        // given
        long sectionUpStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");
        long sectionDownStationId = 지하철역_생성요청("당산역").jsonPath().getLong("id");
        int distance = 5;

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(sectionUpStationId));
        params.put("downStationId", String.valueOf(sectionDownStationId));
        params.put("distance", String.valueOf(distance));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 구간의 하행역이 현재 등록되어있는 역 중 하나이고
     * When 새로운 구간의 생성을 요청하면
     * Then 새로운 구간 생성이 실패한다.
     */
    @DisplayName("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.")
    @Test
    void validateSectionDownStation() {
        // given
        int distance = 7;

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(downStationId));
        params.put("downStationId", String.valueOf(upStationId));
        params.put("distance", String.valueOf(distance));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 새로운 구간의 생성을 요청하면
     * Then 새로운 구간 생성이 성공한다.
     */
    @DisplayName("지하철 노선의 구간 생성")
    @Test
    void createSection() {
        // given
        long sectionDownStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");
        int distance = 3;

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(downStationId));
        params.put("downStationId", String.valueOf(sectionDownStationId));
        params.put("distance", String.valueOf(distance));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 구간이 1개인 경우 역 삭제 요청을 하면
     * Then 역 삭제 요청이 실패한다.
     */
    @DisplayName("지하철 노선에 구간이 1개인 경우 역 삭제를 할 수 없다")
    @Test
    void validateDeleteSection_OneSection() {
        // given
        long stationId = downStationId;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 노선 구간 생성을 요청 하고
     * When 지하철 노선의 마지막 역이 아닌 역 삭제 요청을 하면
     * Then 역 삭제 요청이 실패한다.
     */
    // displayName에 대한 의견 구하기
    @DisplayName("지하철 노선에 등록된 마지막 역만 제거할 수 있다")
    @Test
    void validateDeleteSection_NonDownStation() {
        // given
        long sectionDownStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");

        // downStationId를 전역변수로 설정했는데 뭔가 헷갈릴 수 있겠다는 생각이 듭니다.
        // 좀더 명시적인 네이밍에 대한 의견을 구하고 싶습니다.
        SectionRequest sectionRequest = new SectionRequest(downStationId, sectionDownStationId, 5);
        지하철노선_구간생성요청(lineId, sectionRequest);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + upStationId)
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 노선 구간 생성을 요청 하고
     * When 지하철 노선의 마지막 역 삭제 요청을 하면
     * Then 역 삭제 요청이 성공한다.
     */
    @DisplayName("지하철 노선의 역 삭제 요청")
    @Test
    void deleteSection() {
        // given
        long sectionDownStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");

        SectionRequest sectionRequest = new SectionRequest(downStationId, sectionDownStationId, 5);
        지하철노선_구간생성요청(lineId, sectionRequest);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/" + lineId + "/sections?stationId=" + sectionDownStationId)
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
