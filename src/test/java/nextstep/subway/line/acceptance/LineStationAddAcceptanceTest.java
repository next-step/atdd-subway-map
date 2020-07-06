package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.station.acceptance.step.LineStationAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineStationAddAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        // when
        final ExtractableResponse<Response> response =
                지하철_노선에_지하철역_등록_요청(
                        createdLineResponse.as(LineResponse.class).getId(),
                        "",
                        createdStationResponse.as(StationResponse.class).getId() + "",
                        "4",
                        "2"
                );

        // then
        지하철_노선에_지하철역_등록됨(response);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse = 지하철역_등록되어_있음("강남역");

        지하철_노선에_지하철역_등록_요청(
                createdLineResponse.as(LineResponse.class).getId(),
                "",
                createdStationResponse.as(StationResponse.class).getId() + "",
                "4",
                "2"
        );

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = 지하철_노선_상세정보_조회_요청(createdLineResponse.as(LineResponse.class).getId());

        // then
        지하철_노선_상세정보_응답됨(response, 1);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(
                createdLineResponse.as(LineResponse.class).getId(),
                "",
                createdStationResponse1.as(StationResponse.class).getId() + "",
                "4",
                "2"
        );

        지하철_노선에_지하철역_등록_요청(
                createdLineResponse.as(LineResponse.class).getId(),
                createdStationResponse1.as(StationResponse.class).getId() + "",
                createdStationResponse2.as(StationResponse.class).getId() + "",
                "4",
                "2"
        );

        지하철_노선에_지하철역_등록_요청(
                createdLineResponse.as(LineResponse.class).getId(),
                createdStationResponse2.as(StationResponse.class).getId() + "",
                createdStationResponse3.as(StationResponse.class).getId() + "",
                "4",
                "2"
        );

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_상세정보_조회_요청(createdLineResponse.as(LineResponse.class).getId());
        지하철_노선_상세정보_응답됨(response, 3);
        지하철_노선_등록_순서_검사(response, 1L, 2L, 3L);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // given
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        // when
        final ExtractableResponse<Response> lineStationResponse = 지하철_노선에_지하철역_등록_요청(
                createdLineResponse.as(LineResponse.class).getId(),
                "",
                createdStationResponse1.as(StationResponse.class).getId() + "",
                "4",
                "2"
        );

        지하철_노선에_지하철역_등록_요청(
                createdLineResponse.as(LineResponse.class).getId(),
                createdStationResponse1.as(StationResponse.class).getId() + "",
                createdStationResponse2.as(StationResponse.class).getId() + "",
                "4",
                "2"
        );

        지하철_노선에_지하철역_등록_요청(
                createdLineResponse.as(LineResponse.class).getId(),
                createdStationResponse2.as(StationResponse.class).getId() + "",
                createdStationResponse3.as(StationResponse.class).getId() + "",
                "4",
                "2"
        );

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_상세정보_조회_요청(createdLineResponse.as(LineResponse.class).getId());

        지하철_노선_상세정보_응답됨(response, 3);
        지하철_노선_등록_순서_검사(response, 1L, 2L, 3L);
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_TIME));
        params.put("intervalTime", "5");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/lines").
                then().
                log().all().
                extract();
    }
}
