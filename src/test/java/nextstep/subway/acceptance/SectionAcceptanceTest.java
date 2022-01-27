package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.utils.LineUtils.지하철노선_생성요청;
import static nextstep.subway.acceptance.utils.SectionUtils.지하철노선_구간생성요청;
import static nextstep.subway.acceptance.utils.StationUtils.지하철역_생성요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선의 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest{

    long 생성된_노선_코드;
    long 생성된_노선_상행역_코드;
    long 생성된_노선_하행역_코드;

    @BeforeEach
    @Override
    public void setUp() {
        RestAssured.port = port;

        생성된_노선_상행역_코드 = 지하철역_생성요청("신도림역").jsonPath().getLong("id");
        생성된_노선_하행역_코드 = 지하철역_생성요청("문래역").jsonPath().getLong("id");

       LineRequest request = LineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림역")
                .downStationName("문래역")
                .distance(7)
                .build();

        ExtractableResponse<Response> response = 지하철노선_생성요청(request);

        생성된_노선_코드 = response.jsonPath().getLong("id");
        List<Map<String, Object>> 노선_역목록 = response.jsonPath().getList("stations");

        생성된_노선_상행역_코드 = Long.valueOf(String.valueOf(노선_역목록.get(0).get("id")));
        생성된_노선_하행역_코드 = Long.valueOf(String.valueOf(노선_역목록.get(1).get("id")));

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 구간의 상행역이 현재 등록되어있는 하행 종점역이 아니고
     * When 새로운 구간의 생성을 요청하면
     * Then 새로운 구간 생성이 실패한다.
     */
//    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.")
//    @Test
//    void validateSectionUpStation() {
//        // given
//        long sectionUpStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");
//        long sectionDownStationId = 지하철역_생성요청("당산역").jsonPath().getLong("id");
//        int distance = 5;
//
//        Map<String, String> params = new HashMap<>();
//        params.put("upStationId", String.valueOf(sectionUpStationId));
//        params.put("downStationId", String.valueOf(sectionDownStationId));
//        params.put("distance", String.valueOf(distance));
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .body(params)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().post("/lines/" + lineId + "/sections")
//                .then().log().all().extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//    }

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

//        Map<String, String> params = new HashMap<>();
//        params.put("upStationId", String.valueOf(firstDownStationId));
//        params.put("downStationId", String.valueOf(upStationId));
//        params.put("distance", String.valueOf(distance));
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .body(params)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().post("/lines/" + lineId + "/sections")
//                .then().log().all().extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

//        Map<String, String> params = new HashMap<>();
//        params.put("upStationId", String.valueOf(firstDownStationId));
//        params.put("downStationId", String.valueOf(sectionDownStationId));
//        params.put("distance", String.valueOf(distance));
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .body(params)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when().post("/lines/" + lineId + "/sections")
//                .then().log().all().extract();
//
//        ExtractableResponse<Response> findLineResponse = 지하철노선_단건조회(lineId);
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
//        assertThat(findLineResponse.jsonPath().getList("stations").size()).isEqualTo(3);

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
//        long stationId = firstDownStationId;
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .when().delete("/lines/" + lineId + "/sections?stationId=" + stationId)
//                .then().log().all().extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
//        long sectionDownStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");
//
//        SectionRequest sectionRequest = new SectionRequest(firstDownStationId, sectionDownStationId, 5);
//        지하철노선_구간생성요청(lineId, sectionRequest);
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .when().delete("/lines/" + lineId + "/sections?stationId=" + upStationId)
//                .then().log().all().extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
//        long sectionDownStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");
//
//        SectionRequest sectionRequest = new SectionRequest(firstDownStationId, sectionDownStationId, 5);
//        지하철노선_구간생성요청(lineId, sectionRequest);
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .when().delete("/lines/" + lineId + "/sections?stationId=" + sectionDownStationId)
//                .then().log().all().extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
