package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.step.LineStationAcceptanceStep.*;
import static nextstep.subway.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선에 역 제외 관련 기능")
public class LineStationRemoveAcceptanceTest extends AcceptanceTest {
    Long lineId;
    Long stationId1;
    Long stationId2;
    Long stationId3;

    @BeforeEach
    void setup() {
        super.setUp();

        ExtractableResponse<Response> createdLineResponse = 지하철_노선_등록되어_있음("2호선", "GREEN");
        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("선릉역");

        lineId = createdLineResponse.as(LineResponse.class).getId();
        stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        stationId3 = createdStationResponse3.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록_요청(lineId, null, stationId1, "4", "2");
        지하철_노선에_지하철역_등록_요청(lineId, stationId1, stationId2, "4", "2");
        지하철_노선에_지하철역_등록_요청(lineId, stationId2, stationId3, "4", "2");
    }

    @DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
    @Test
    void removeLastStationInLine() {
        // when
        ExtractableResponse<Response> deleteResponse = 지하철_노선의_마지막에_지하철역_제외_요청(lineId, stationId3);

        // then
        지하철_노선에_지하철역_제외됨(deleteResponse);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(lineId);

        // then
        지하철_노선에_지하철역_제외_확인됨(response, stationId3);
        지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(stationId1, stationId2));
    }
}
