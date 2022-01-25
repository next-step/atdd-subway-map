package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    /**
     * Given 지하철 노선 생성을 요청하고
     * Given 상행역 생성을 요청하고
     * Given 하행역 생성을 요청하고
     * When 구간 생성을 요청 하면
     * Then 구간 생성이 성공한다.
     */
    @DisplayName("구간 생성")
    @Test
    void createSection() {
        // given
        ExtractableResponse<Response> createLineResponse = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성());
        ExtractableResponse<Response> createStationResponse1 = StationSteps.지하철역_생성("강남역");
        ExtractableResponse<Response> createStationResponse2 = StationSteps.지하철역_생성("역삼역");

        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", createStationResponse1.body().jsonPath().getLong("id"));
        params.put("downStationId", createStationResponse2.body().jsonPath().getLong("id"));
        params.put("distance", 10);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/1/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성을 요청하고
     * Given 지하철역 3개 생성을 요청하고
     * Given 구간을 생성을 요청하고
     * When 새로운 구간생성을 요청하면
     * Then 상행역은 현재 등록되어있는 하행 종점역이어야 합니다 에러가 발생한다.
     */
    @DisplayName("구간 생성 상행역 에러 테스트")
    @Test
    void validateUpStation() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().jsonPath().getLong("id");
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().jsonPath().getLong("id");
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().jsonPath().getLong("id");
        Long stationId3 = StationSteps.지하철역_생성("선릉역").body().jsonPath().getLong("id");
        SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);

        // when
        ExtractableResponse<Response> response = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId3, 10), lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 생성을 요청하고
     * Given 지하철역 3개 생성을 요청하고
     * Given 구간을 생성을 요청하고
     * When 중복 된 하행역으로 새로운 구간생성을 요청하면
     * Then 하행역은 현재 등록되어있는 역일 수 없습니다. 에러가 발생한다.
     */
    @DisplayName("하행역 중복 에러 테스트")
    @Test
    void validateDownStation() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().jsonPath().getLong("id");
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().jsonPath().getLong("id");
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().jsonPath().getLong("id");
        SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);

        // when
        ExtractableResponse<Response> response = SectionSteps.지하철_구간_생성(new SectionRequest(stationId2, stationId1, 10), lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선 생성
     * Given 지하철역 3개 생성
     * Given 구간 2개 생성
     * When 구간 삭제 요청
     * Then 구간 삭제 완료
     */
    @DisplayName("구간 제거")
    @Test
    void deleteSection() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().jsonPath().getLong("id");
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().jsonPath().getLong("id");
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().jsonPath().getLong("id");
        Long stationId3 = StationSteps.지하철역_생성("선릉역").body().jsonPath().getLong("id");
        ExtractableResponse<Response> response1 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);
        ExtractableResponse<Response> response2 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId2, stationId3, 5), lineId);

        // when
        ExtractableResponse<Response> deleteResponse = SectionSteps.지하철_구간_삭제(lineId, stationId3);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성
     * Given 지하철역 3개 생성
     * Given 구간 2개 생성
     * When 하행 종점역이 아닌 stationId로 구간 삭제 요청
     * Then 구간 삭제 실패
     */
    @DisplayName("구간 제거 시 마지막 역(하행 종점역)이 아닌 경우 에러가 발생한다.")
    @Test
    void deleteSectionValidateLastDownStation() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().jsonPath().getLong("id");
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().jsonPath().getLong("id");
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().jsonPath().getLong("id");
        Long stationId3 = StationSteps.지하철역_생성("선릉역").body().jsonPath().getLong("id");
        ExtractableResponse<Response> response1 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);
        ExtractableResponse<Response> response2 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId2, stationId3, 5), lineId);

        // when
        ExtractableResponse<Response> deleteResponse = SectionSteps.지하철_구간_삭제(lineId, stationId2);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선 생성
     * Given 지하철역 3개 생성
     * Given 구간 1개 생성
     * When 구간 삭제 요청
     * Then 구간 삭제 실패
     */
    @DisplayName("구간이 1개인 경우 역을 삭제하면 에러가 발생한다. ")
    @Test
    void deleteSingleSection() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().jsonPath().getLong("id");
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().jsonPath().getLong("id");
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().jsonPath().getLong("id");
        ExtractableResponse<Response> response1 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);

        // when
        ExtractableResponse<Response> deleteResponse = SectionSteps.지하철_구간_삭제(lineId, stationId2);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
