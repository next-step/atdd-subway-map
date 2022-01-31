package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
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
        요청_응답을_확인한다(response, HttpStatus.OK);
    }

    @DisplayName("구간 생성 상행역 에러 테스트")
    @Test
    void validateUpStation() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().as(LineResponse.class).getId();
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().as(StationResponse.class).getId();
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().as(StationResponse.class).getId();
        Long stationId3 = StationSteps.지하철역_생성("선릉역").body().as(StationResponse.class).getId();
        SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);

        // when
        ExtractableResponse<Response> response = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId3, 10), lineId);

        // then
        요청_응답을_확인한다(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("하행역 중복 에러 테스트")
    @Test
    void validateDownStation() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().as(LineResponse.class).getId();
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().as(StationResponse.class).getId();
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().as(StationResponse.class).getId();
        SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);

        // when
        ExtractableResponse<Response> response = SectionSteps.지하철_구간_생성(new SectionRequest(stationId2, stationId1, 10), lineId);

        // then
        요청_응답을_확인한다(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("구간 제거")
    @Test
    void deleteSection() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().as(LineResponse.class).getId();
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().as(StationResponse.class).getId();
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().as(StationResponse.class).getId();
        Long stationId3 = StationSteps.지하철역_생성("선릉역").body().as(StationResponse.class).getId();
        ExtractableResponse<Response> response1 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);
        ExtractableResponse<Response> response2 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId2, stationId3, 5), lineId);

        // when
        ExtractableResponse<Response> deleteResponse = SectionSteps.지하철_구간_삭제(lineId, stationId3);

        // then
        요청_응답을_확인한다(deleteResponse, HttpStatus.NO_CONTENT);
    }

    @DisplayName("구간 제거 시 마지막 역(하행 종점역)이 아닌 경우 에러가 발생한다.")
    @Test
    void deleteSectionValidateLastDownStation() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().as(LineResponse.class).getId();
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().as(StationResponse.class).getId();
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().as(StationResponse.class).getId();
        Long stationId3 = StationSteps.지하철역_생성("선릉역").body().as(StationResponse.class).getId();
        ExtractableResponse<Response> response1 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);
        ExtractableResponse<Response> response2 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId2, stationId3, 5), lineId);

        // when
        ExtractableResponse<Response> deleteResponse = SectionSteps.지하철_구간_삭제(lineId, stationId2);

        // then
        요청_응답을_확인한다(deleteResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("구간이 1개인 경우 역을 삭제하면 에러가 발생한다. ")
    @Test
    void deleteSingleSection() {
        // given
        Long lineId = LineSteps.지하철_노선_생성(LineSteps.이호선_요청_생성()).body().as(LineResponse.class).getId();
        Long stationId1 = StationSteps.지하철역_생성("강남역").body().as(StationResponse.class).getId();
        Long stationId2 = StationSteps.지하철역_생성("역삼역").body().as(StationResponse.class).getId();
        ExtractableResponse<Response> response1 = SectionSteps.지하철_구간_생성(new SectionRequest(stationId1, stationId2, 10), lineId);

        // when
        ExtractableResponse<Response> deleteResponse = SectionSteps.지하철_구간_삭제(lineId, stationId2);

        // then
        요청_응답을_확인한다(deleteResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
