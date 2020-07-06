package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
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

    private long 이호선;
    private String 강남역;
    private String 역삼역;
    private String 선릉역;

    @DisplayName("지하철 역이 등록되어 있고 노선이 등록되어 있다.")
    @BeforeEach
    void background() {
        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");
        이호선 = createdLineResponse.as(LineResponse.class).getId();
        강남역 = createdStationResponse1.as(StationResponse.class).getId() + "";
        역삼역 = createdStationResponse2.as(StationResponse.class).getId() + "";
        선릉역 = createdStationResponse3.as(StationResponse.class).getId() + "";
    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation() {
        final ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선, "", 강남역, "4", "2");

        // then
        지하철_노선에_지하철역_등록됨(response);
    }

    @DisplayName("지하철 노선 상세정보 조회 시 역 정보가 포함된다.")
    @Test
    void getLineWithStations() {
        // given
        지하철_노선에_지하철역_등록_요청(이호선, "", 강남역, "4", "2");

        // when
        ExtractableResponse<Response> response = 지하철_노선_상세정보_조회_요청(이호선);

        // then
        지하철_노선_상세정보_응답됨(response, 1);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서대로 등록한다.")
    @Test
    void addLineStationInOrder() {
        // when
        ExtractableResponse<Response> lineStationResponse =
                지하철_노선에_지하철역_등록_요청(이호선, "", 강남역, "4", "2");
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, "4", "2");
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 선릉역, "4", "2");

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_상세정보_조회_요청(이호선);
        지하철_노선_상세정보_응답됨(response, 3);
        지하철_노선_등록_순서_검사(response, 1L, 2L, 3L);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서없이 등록한다.")
    @Test
    void addLineStationInAnyOrder() {
        // when
        final ExtractableResponse<Response> lineStationResponse =
                지하철_노선에_지하철역_등록_요청(이호선, "", 강남역, "4", "2");
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, "4", "2");
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 선릉역, "4", "2");

        // then
        지하철_노선에_지하철역_등록됨(lineStationResponse);

        // when
        final ExtractableResponse<Response> response = 지하철_노선_상세정보_조회_요청(이호선);

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
